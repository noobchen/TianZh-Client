package com.tianzh.pay.service;

import android.content.Context;
import com.product.pay.callback.TzPayUtils;
import com.tianzh.pay.TianZhPay;
import com.tianzh.pay.model.PaidWays;
import com.tianzh.pay.service.callback.OnDialogClickListener;
import com.tianzh.pay.utils.AlertUtils;
import com.tianzh.pay.utils.LogUtils;
import com.tianzh.pay.utils.PayUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by pig on 2015-11-13.
 */
public class TwiceConfirmDialogBuilder extends ConfirmDialogBuilder {

    private Context context;
    private ArrayList<PaidWays> paidWayses;
    private Integer delay;
    private String msg;
    String userOrderId = "";
    String productId = "";
    String tianzhOrderId = "";


    public TwiceConfirmDialogBuilder(Context context, ArrayList<PaidWays> paidWayses, HashMap<String, String> map) {
        this.context = context;
        this.paidWayses = paidWayses;
        this.delay = Integer.valueOf(map.get("delay"));
        this.msg = map.get("productPrompt");
        this.userOrderId = map.get("userOrderId");
        this.productId = map.get("productId");
        this.tianzhOrderId = map.get("tianzhOrderId");
    }

    @Override
    public void show() {
        TianZhPay.payProcessHandler.post(new Runnable() {
            @Override
            public void run() {
                AlertUtils.showAlertDialog(context, msg, new OnDialogClickListener() {
                    @Override
                    public void onPositiveClick() {
                        confirm();
                    }

                    @Override
                    public void onNegativeClick() {
                        cancel();
                    }
                });
            }
        });

    }

    @Override
    public void dismiss() {
        TianZhPay.payProcessHandler.post(new Runnable() {
            @Override
            public void run() {
                AlertUtils.dismissAlertDialog();
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
                AlertUtils.dismissAlertDialog();
            }
        });
        PayUtils.failCallBack(context, tianzhOrderId, userOrderId, productId, "用户取消付费", "3004");
    }
}
