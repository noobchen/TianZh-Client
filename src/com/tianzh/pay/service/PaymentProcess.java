package com.tianzh.pay.service;

import com.tianzh.pay.service.msg.IncomingMsgObserver;
import com.tianzh.pay.service.msg.SendMsgStatusReader;
import com.tianzh.pay.utils.MsgObserverUtils;

import java.util.ArrayList;

/**
 * Created by pig on 2015-11-13.
 */
public class PaymentProcess implements Runnable {
    private ArrayList<IncomingMsgObserver> observer;
    private ConfirmDialogBuilder dialogBuilder;

    public PaymentProcess(ArrayList<IncomingMsgObserver> observer, ConfirmDialogBuilder dialogBuilder) {
        this.observer = observer;
        this.dialogBuilder = dialogBuilder;
    }


    @Override
    public void run() {
        dialogBuilder.show();

        MsgObserverUtils.addAllObserver(observer);
    }
}
