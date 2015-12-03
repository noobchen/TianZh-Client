package com.tianzh.pay.service.callback;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import com.tianzh.pay.TianZhPay;
import com.tianzh.pay.service.*;
import com.tianzh.pay.utils.AlertUtils;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by pig on 2015-09-09.
 */
public class TianZhPayProcessHandler extends Handler {
    Activity context;

    public TianZhPayProcessHandler(Activity context) {
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

        AlertUtils.dismissProgressDialog();
        LocalPayService localPayService = null;

        switch (msg.what) {
            case 0:
                localPayService = new SelfPaidWaysServiceImpl(context);
                break;
            case 1:
                localPayService = new LetuPayServiceImpl(context);
                break;
            case 2:
                localPayService = new YuanLangPayServiceImpl(context);
                break;

            case 3:
                localPayService = new ZhangPayServiceImpl(context);
                break;

            case 4:
                localPayService = new ZhuQuePayServiceImpl(context);
                break;
            case 9997:
                AlertUtils.showToast(context, (String) msg.obj);
                break;
            case 9998:
                AlertUtils.showProgressDialog(context, "请稍等...");
                break;
            //异常
            case 9999:
                Message message = TianZhPay.payResultHandler.obtainMessage();

                HashMap<String, String> result = new HashMap<String, String>();
                message.what = 404;

                result.put("reason", "网络异常！");// "数据解析异常！"
                result.put("userOrderId", "");
                result.put("productId", "");

                message.obj = result;
                TianZhPay.payResultHandler.sendMessage(message);
                break;
        }

        if (localPayService != null) localPayService.pay((JSONObject) msg.obj);

    }
}
