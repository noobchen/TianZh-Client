package com.tianzh.pay.service.callback;

import android.app.Activity;
import android.content.Context;
import android.os.Message;
import android.text.TextUtils;
import com.tianzh.pay.TianZhPay;
import com.tianzh.pay.constant.Constant;
import com.tianzh.pay.service.*;
import com.tianzh.pay.utils.ConfigUtils;
import com.tianzh.pay.utils.PayUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by pig on 2015-09-10.
 */
public class PayNetWorkListener implements NetWorkListener {
    Activity context = null;

    String userOrderId = "";
    String productId = "";
    String tianzhOrderId = "";

    public PayNetWorkListener(Activity context) {
        this.context = context;
    }

    @Override
    public void onResponse(int statusCode, String responseStr) {
        try {
            JSONObject response = new JSONObject(responseStr);

            userOrderId = response.getString("userOrderId");
            productId = response.getString("productId");
            tianzhOrderId = response.getString("tianzhOrderId");

            String userToken = ConfigUtils.getConfig(context, Constant.Config.USERTOKENKEY);

            if (TextUtils.isEmpty(userToken)) {
                userToken = response.getString("userToken");
                ConfigUtils.setConfig(context, Constant.Config.USERTOKENKEY, userToken);
            }

            int mStatusCode = response.getInt("statusCode");

            if (statusCode == 404 || mStatusCode != 200) {
                PayUtils.exceptionCallBack(context, tianzhOrderId, userOrderId, productId, "服务端返回404！", "30001");
                TianZhPay.payProcessHandler.sendEmptyMessage(9999);


            } else {
                int payType = response.getInt("thpayType");
                Message processMsg = TianZhPay.payProcessHandler.obtainMessage();
                processMsg.what = payType;
                processMsg.obj = response;

                TianZhPay.payProcessHandler.sendMessage(processMsg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            PayUtils.exceptionCallBack(context, tianzhOrderId, userOrderId, productId, "数据解析异常！", "30000");
            TianZhPay.payProcessHandler.sendEmptyMessage(9999);


        }
    }
}
