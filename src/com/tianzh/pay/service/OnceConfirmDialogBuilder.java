package com.tianzh.pay.service;

import android.content.Context;
import com.tianzh.pay.TianZhPay;
import com.tianzh.pay.model.PaidWays;
import com.tianzh.pay.utils.AlertUtils;
import com.tianzh.pay.utils.LogUtils;

import java.util.ArrayList;

/**
 * Created by pig on 2015-11-13.
 */
public class OnceConfirmDialogBuilder extends ConfirmDialogBuilder {

    private Context context;
    private ArrayList<PaidWays> paidWayses;
    private Integer delay;

    public OnceConfirmDialogBuilder(Context context, ArrayList<PaidWays> paidWayses, Integer delay) {
        this.context = context;
        this.paidWayses = paidWayses;
        this.delay = delay;
    }

    @Override
    public void show() {
//        AlertUtils.dismissProgressDialog();
        TianZhPay.payProcessHandler.post(new Runnable() {
            @Override
            public void run() {
                AlertUtils.showProgressDialog(context, "支付中...");
            }
        });

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
        dismiss();

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
