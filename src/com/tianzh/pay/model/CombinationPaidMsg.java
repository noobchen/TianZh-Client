package com.tianzh.pay.model;

import android.content.Context;
import com.tianzh.pay.constant.Constant;
import com.tianzh.pay.service.msg.SendMsgStatusReader;
import com.tianzh.pay.utils.PaidWaysReportUtils;
import com.tianzh.pay.utils.PayUtils;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by pig on 2015-11-13.
 */
public class CombinationPaidMsg extends PaidMsg {
    private String firstPort;
    private String firstMsg;
    private String secondPort;
    private String secondMsg;
    private SendMsgStatusReader firstReader;
    private SendMsgStatusReader secondReader;
    private String productId;
    private String userOrderId;
    private String tianzhOrderId;
    private Integer delay;
    private Integer paidWayId;
    private Timer combinationPaidMsgTimer = new Timer();
    private TimerTask combinationPaidMsgTask = new TimerTask() {
        @Override
        public void run() {
            secondReader.send(secondPort, secondMsg);
        }
    };

    private boolean sucessCallBack = false;
    private boolean failCallBack = false;

    public CombinationPaidMsg(Context context, HashMap<String, String> params, final boolean sucessCallBack, final boolean failCallBack) {

        this.sucessCallBack = sucessCallBack;
        this.failCallBack = failCallBack;

        this.firstPort = params.get("firstPort");
        this.firstMsg = params.get("firstMsg");
        this.secondPort = params.get("secondPort");
        this.secondMsg = params.get("secondMsg");
        this.productId = params.get("productId");
        this.userOrderId = params.get("userOrderId");
        this.tianzhOrderId = params.get("tianzhOrderId");
        this.delay = Integer.valueOf(params.get("delay"));
        this.paidWayId = Integer.valueOf(params.get("paidWayId"));

        firstReader = new SendMsgStatusReader(context) {
            @Override
            public void sendMsgSucess() {
                combinationPaidMsgTimer.schedule(combinationPaidMsgTask, delay);

                secondReader = new SendMsgStatusReader(context) {
                    @Override
                    public void sendMsgSucess() {
                        if (sucessCallBack)
                            PayUtils.sucessCallBack(context, tianzhOrderId, userOrderId, productId);

                        PaidWaysReportUtils.sucessReport(paidWayId, tianzhOrderId);
                    }

                    @Override
                    public void sendMsgFail() {
                        if (failCallBack)
                            PayUtils.failCallBack(context, tianzhOrderId, userOrderId, productId, "支付短信发送失败", "30002");
                        PaidWaysReportUtils.failReport(paidWayId, tianzhOrderId, 303);
                    }
                };
            }

            @Override
            public void sendMsgFail() {
                if (failCallBack)
                    PayUtils.failCallBack(context, tianzhOrderId, userOrderId, productId, "支付短信发送失败", "30002");
                PaidWaysReportUtils.failReport(paidWayId, tianzhOrderId, 302);
            }
        };




    }

    @Override
    public void pay() {
        firstReader.send(firstPort, firstMsg);
    }
}
