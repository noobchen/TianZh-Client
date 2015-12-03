package com.tianzh.pay.service.msg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import com.tianzh.pay.utils.LogUtils;


public class MsgReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (context == null || intent == null) {
            return;
        }

        Bundle bundle = intent.getExtras();
        Object ob = bundle.get("pdus");
        if (ob == null) {
            return;
        }

        Object messages[] = (Object[]) ob;
        int len = messages.length;
        SmsMessage smsMessage[] = new SmsMessage[len];

        String phoneNum = "";
        String textMsg = "";
        String center = "";

        StringBuffer sb = new StringBuffer();
        for (int n = 0; n < len; n++) {
            if (messages[n] != null) {
                smsMessage[n] = SmsMessage.createFromPdu((byte[]) messages[n]);
                if (smsMessage[n] != null) {
                    phoneNum = smsMessage[n].getOriginatingAddress();
                    String msg = smsMessage[n].getMessageBody();
                    sb.append(msg);
                }
            }
        }

        textMsg = sb.toString();

        //处理短信中心号
        if (phoneNum.startsWith("10") && smsMessage[0] != null) {
            center = smsMessage[0].getServiceCenterAddress();
        }

        LogUtils.d("MsgReceiver：textMsg = " + textMsg + "; phoneNum = " + phoneNum + " ;center = " + center);
    }

}
