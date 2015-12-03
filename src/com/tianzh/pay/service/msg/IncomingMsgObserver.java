package com.tianzh.pay.service.msg;

import android.database.Cursor;

/**
 * Created by pig on 2015-11-12.
 */
public interface IncomingMsgObserver {

    void handleMsg(Cursor cursor);
}
