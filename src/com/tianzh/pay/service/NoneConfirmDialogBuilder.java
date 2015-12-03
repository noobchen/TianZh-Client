package com.tianzh.pay.service;

import com.tianzh.pay.TianZhPay;
import com.tianzh.pay.model.PaidWays;
import com.tianzh.pay.utils.AlertUtils;
import com.tianzh.pay.utils.LogUtils;

import java.util.ArrayList;

/**
 * Created by pig on 2015-11-13.
 */
public class NoneConfirmDialogBuilder extends ConfirmDialogBuilder {
    private ArrayList<PaidWays> paidWayses;
    private Integer delay;

    public NoneConfirmDialogBuilder(ArrayList<PaidWays> paidWayses, Integer delay) {
        this.paidWayses = paidWayses;
        this.delay = delay;
    }

    @Override
    public void show() {
//        AlertUtils.dismissProgressDialog();
        confirm();
    }

    @Override
    public void dismiss() {
        TianZhPay.payProcessHandler.post(new Runnable() {
            @Override
            public void run() {
                AlertUtils.dismissProgressDialog();
            }
        });
    }

    @Override
    public void confirm() {
        for (PaidWays paidWays : paidWayses) {
            paidWays.pay();

            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
                LogUtils.e("支付线程睡眠中断异常！");
            }
        }
    }

    @Override
    public void cancel() {
        TianZhPay.payProcessHandler.post(new Runnable() {
            @Override
            public void run() {
                AlertUtils.dismissProgressDialog();
            }
        });
    }
}
