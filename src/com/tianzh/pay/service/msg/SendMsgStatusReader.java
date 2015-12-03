package com.tianzh.pay.service.msg;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.util.Log;
import com.tianzh.pay.constant.Constant;
import com.tianzh.pay.utils.LogUtils;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by pig on 2015-11-12.
 */
public abstract class SendMsgStatusReader {
    protected Timer timer = new Timer();
    protected TimerTask task = new TimerTask() {
        @Override
        public void run() {
            sendMsgFail();
        }
    };

    protected BroadcastReceiver sendBr ;

    protected BroadcastReceiver receiveBr ;


    protected String SEND_SMS_ACTION = "SEND_SMS_ACTION";    //发送短信是否成功的广播
    protected String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION"; //接收者是否成功接收到短信的广播

    protected Context context;

    public SendMsgStatusReader(Context context) {
        this.context = context;
//        timer = new Timer();
//
//        task = new TimerTask() {
//            @Override
//            public void run() {
//                sendMsgFail();
//            }
//        };

        sendBr = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                timer.cancel();
                //判断短信是否发送成功
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        //短信发送成功
                        sendMsgSucess();
                        LogUtils.d("短信发送成功");
                        break;

                    default:
                        //短信发送失败
                        sendMsgFail();
                        LogUtils.d( "短信发送失败");
                        break;
                }

                try {
                    context.unregisterReceiver(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        receiveBr = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        //对方接收短信成功
                        LogUtils.d("对方接收短信成功");
                        break;
                    default:
                        //对方接收短信失败
                        LogUtils.d("对方接收短信失败");
                        break;
                }

                try {
                    context.unregisterReceiver(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        context.registerReceiver(sendBr, new IntentFilter(SEND_SMS_ACTION));
        context.registerReceiver(receiveBr, new IntentFilter(DELIVERED_SMS_ACTION));

    }

    public void send(String port, String message) {
        Intent sentIntent = new Intent(SEND_SMS_ACTION);
        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0, sentIntent,
                0);

        Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
        PendingIntent deliverPI = PendingIntent.getBroadcast(context, 0, deliverIntent,
                0);

        SmsManager smsManager = SmsManager.getDefault();

        if (message.length() > 70) {
            ArrayList<String> msgs = smsManager.divideMessage(message);
            for (String msg : msgs) {
                smsManager.sendTextMessage(port, null, msg, sentPI, deliverPI);
            }
        } else {
            smsManager.sendTextMessage(port, null, message, sentPI, deliverPI);
        }

        timer.schedule(task,20*1000);
    }


    public abstract void sendMsgSucess();

    public abstract void sendMsgFail();
}
