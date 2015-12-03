package com.tianzh.pay.service;

import android.content.Context;
import com.sgame.Listener;
import com.sgame.ResultCode;
import com.sgame.Sgame;
import com.tianzh.pay.model.DevicesInfo;
import com.tianzh.pay.utils.PayUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * Created by pig on 2015-10-20.
 */
public class ZhuQuePayServiceImpl implements LocalPayService, Listener.SgameListener,Listener.PayListener {

    private Context context;
    String tianzhOrderId = "";
    String productId = "";
    String userOrderId = "";

    public ZhuQuePayServiceImpl(Context context) {
        this.context = context;
    }

    @Override
    public void pay(JSONObject params) {
        try {
            productId = params.getString("productId");
            userOrderId = params.getString("userOrderId");
            tianzhOrderId = params.getString("tianzhOrderId");

            String cpParam = DevicesInfo.zhuqueTpk + "_" + params.getString("tianzhOrderId");
            int price = params.getInt("price") / 100;

            boolean payStatus = Sgame.sPay(String.valueOf(price), cpParam, "", "", "", this, this);

            if (!payStatus) {
                //本次支付接口调用失败
                PayUtils.failCallBack(context, tianzhOrderId, userOrderId, productId, "支付失败", "zhuque-");
            }

        } catch (JSONException e) {
            e.printStackTrace();
            PayUtils.exceptionCallBack(context, tianzhOrderId, userOrderId, productId, "数据解析异常！", "30000");
        }
    }

    @Override
    public void onProcessFinish(int i) {

    }

    @Override
    public void onProcessFinish(int i, Map<String, Object> map) {
        switch (i) {
            case ResultCode.PAY_SUCCESS:
                PayUtils.sucessCallBack(context, tianzhOrderId, userOrderId, productId);
                break;
            case ResultCode.PAY_FAIL:
                PayUtils.failCallBack(context, tianzhOrderId, userOrderId, productId, "支付失败", "zhuque-");
                break;
        }
    }
}
