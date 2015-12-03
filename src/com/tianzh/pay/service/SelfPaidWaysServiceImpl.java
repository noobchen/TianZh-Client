package com.tianzh.pay.service;

import android.content.Context;
import com.tianzh.pay.model.CombinationPaidMsg;
import com.tianzh.pay.model.ConfirmPaidMsg;
import com.tianzh.pay.model.PaidWays;
import com.tianzh.pay.model.SimplePaidMsg;
import com.tianzh.pay.service.msg.*;
import com.tianzh.pay.utils.PayUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by pig on 2015-11-13.
 */
public class SelfPaidWaysServiceImpl implements LocalPayService {
    private Context context;
    private String productId = "";
    private String userOrderId = "";
    private String tianzhOrderId = "";

    public SelfPaidWaysServiceImpl(Context context) {
        this.context = context;
    }

    @Override
    public void pay(JSONObject params) {
        try {
            productId = params.getString("productId");
            userOrderId = params.getString("userOrderId");
            tianzhOrderId = params.getString("tianzhOrderId");
            //支付方式
            JSONArray paidWays = params.getJSONArray("paidWays");
            ArrayList<PaidWays> paidWayses = new ArrayList<PaidWays>();
            ArrayList<IncomingMsgObserver> incomingMsgObserveres = new ArrayList<IncomingMsgObserver>();

            for (int i = 0; i < paidWays.length(); i++) {
                PaidWays paidWay = null;

                JSONObject paidWayJson = paidWays.getJSONObject(i);
                int paidWayId = paidWayJson.getInt("paidWayId");

                switch (paidWayJson.getInt("paidWayType")) {
                    case 0:
                        //普通单条短信
                    {
                        boolean sucessCallBack;
                        boolean failCallBack;

                        if (i == paidWays.length() - 1) {
                            sucessCallBack = true;
                            failCallBack = true;
                        } else {
                            sucessCallBack = false;
                            failCallBack = false;
                        }

                        String port = paidWayJson.getString("port");
                        String msg = paidWayJson.getString("msg");
                        paidWay = new SimplePaidMsg(port, msg, new SimpleSendMsgStatusReader(context, paidWayId, productId, userOrderId, tianzhOrderId, sucessCallBack, failCallBack));
                        break;

                    }
                    case 1:
                        //普通单条二次确认短信
                    {
                        boolean sucessCallBack;
                        boolean failCallBack;

                        if (i == paidWays.length() - 1) {
                            sucessCallBack = true;
                            failCallBack = true;
                        } else {
                            sucessCallBack = false;
                            failCallBack = false;
                        }

                        String port = paidWayJson.getString("port");
                        String msg = paidWayJson.getString("msg");

                        String interceptPort = paidWayJson.getString("interceptPort");
                        String interceptPreKeyWords = paidWayJson.getString("interceptPreKeyWords");
                        String interceptBackKeyWords = paidWayJson.getString("interceptBackKeyWords");

                        final String confirmPort = paidWayJson.getString("confirmPort");

                        HashMap<String, Object> map = new HashMap<String, Object>();

                        map.put("port", port);
                        map.put("msg", msg);
                        map.put("confirmPort", confirmPort);
                        map.put("sucessCallBack", sucessCallBack);
                        map.put("failCallBack", failCallBack);
                        map.put("interceptPort", interceptPort);
                        map.put("interceptPreKeyWords", interceptPreKeyWords);
                        map.put("interceptBackKeyWords", interceptBackKeyWords);
                        map.put("paidWayId", paidWayId);
                        map.put("productId", params.getString("productId"));
                        map.put("userOrderId", params.getString("userOrderId"));
                        map.put("tianzhOrderId", params.getString("tianzhOrderId"));
                        map.put("confirmMsgDelay", paidWayJson.getInt("confirmMsgDelay"));

                        paidWay = new ConfirmPaidMsg(context, map);
                        break;
                    }
                    case 2:
                        //两条组合短信
                        HashMap<String, String> map = new HashMap<String, String>();

                        map.put("firstPort", paidWayJson.getString("firstPort"));
                        map.put("firstMsg", paidWayJson.getString("firstMsg"));
                        map.put("secondPort", paidWayJson.getString("secondPort"));
                        map.put("secondMsg", paidWayJson.getString("secondMsg"));
                        map.put("productId", params.getString("productId"));
                        map.put("userOrderId", params.getString("userOrderId"));
                        map.put("tianzhOrderId", params.getString("tianzhOrderId"));
                        map.put("delay", paidWayJson.getString("delay"));
                        map.put("paidWayId", String.valueOf(paidWayId));

                        boolean sucessCallBack;
                        boolean failCallBack;
                        if (i == paidWays.length() - 1) {
                            sucessCallBack = true;
                            failCallBack = true;
                        } else {
                            sucessCallBack = false;
                            failCallBack = false;
                        }

                        paidWay = new CombinationPaidMsg(context, map, sucessCallBack, failCallBack);
                        break;
                }

                paidWayses.add(paidWay);

                IncomingMsgObserver incomingMsgObserver = null;
                switch (paidWayJson.getInt("interceptType")) {
                    case 0:
                        //不拦截

                        break;

                    case 1:
                        //拦截
                        String interceptPort = paidWayJson.getString("interceptPort");
                        String interceptKeyWords = paidWayJson.getString("interceptKeyWords");

                        incomingMsgObserver = new InterceptIncomingMsgImpl(context, interceptPort, interceptKeyWords);
                        break;

                }

                incomingMsgObserveres.add(incomingMsgObserver);
            }

            ConfirmDialogBuilder dialogBuilder = null;
            Integer delay = params.getInt("delay");

            switch (params.getInt("dialogType")) {
                case 0:
                    //无支付过程
                    dialogBuilder = new NoneConfirmDialogBuilder(paidWayses, delay);
                    break;

                case 1:
                    //单次确认
                    dialogBuilder = new OnceConfirmDialogBuilder(context, paidWayses, delay);
                    break;

                case 2:
                    //二次确认
                    HashMap<String, String> map = new HashMap<String, String>();

                    map.put("productPrompt", params.getString("productPrompt"));
                    map.put("delay", String.valueOf(delay));
                    map.put("productId", params.getString("productId"));
                    map.put("userOrderId", params.getString("userOrderId"));
                    map.put("tianzhOrderId", params.getString("tianzhOrderId"));
                    dialogBuilder = new TwiceConfirmDialogBuilder(context, paidWayses, map);
                    break;
            }
            PaymentProcess paymentProcess = new PaymentProcess(incomingMsgObserveres, dialogBuilder);

            new Thread(paymentProcess).start();
        } catch (Exception e) {
            PayUtils.exceptionCallBack(context, tianzhOrderId, userOrderId, productId, "数据解析异常！", "30000");
        }

    }
}
