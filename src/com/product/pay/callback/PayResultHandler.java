package com.product.pay.callback;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import com.tianzh.pay.utils.AlertUtils;

import java.util.HashMap;

/**
 * Created by pig on 2015-10-21.
 */
public class PayResultHandler extends Handler {
    private Context context;

    public PayResultHandler(Context context) {
        this.context = context;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        HashMap<String, String> result = (HashMap<String, String>) msg.obj;
        switch (msg.what){
            case 200:
                String resultStr = "sucess";

                break;
            default:
                String resultStr2 = "fail";

                break;
        }

        AlertUtils.showToast(context, result.get("reason"));
    }
}
