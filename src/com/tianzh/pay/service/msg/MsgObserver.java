package com.tianzh.pay.service.msg;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import com.tianzh.pay.utils.LogUtils;
import com.tianzh.pay.utils.MsgObserverUtils;

public class MsgObserver extends ContentObserver {


    private final Context context;

    public MsgObserver(Handler handler, Context context) {
        super(handler);
        this.context = context;

    }

    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        //读取收件箱中新短信 content://sms/inbox 后期需要加密
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/inbox"), null, "read=?", new String[]{"0"}, "date desc");
        if (cursor == null) {
            LogUtils.e("收到短信，但是Cursor==null");
            return;
        }

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                String textMsg = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                String phoneNum = cursor.getString(cursor.getColumnIndexOrThrow("address"));

                LogUtils.d("MsgObserver：textMsg = " + textMsg + "; phoneNum = " + phoneNum);

                MsgObserverUtils.notifyAllObservers(cursor);
            } while (cursor.moveToNext());

            cursor.close();
        }
    }
}
