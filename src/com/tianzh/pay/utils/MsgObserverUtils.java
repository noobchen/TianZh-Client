package com.tianzh.pay.utils;

import android.database.Cursor;
import com.tianzh.pay.service.msg.IncomingMsgObserver;

import java.util.ArrayList;

/**
 * Created by pig on 2015-11-12.
 */
public class MsgObserverUtils {
    private static ArrayList<IncomingMsgObserver> observers = new ArrayList<IncomingMsgObserver>();

    public static void addObserver(IncomingMsgObserver observer) {
        observers.add(observer);
    }

    public static void addAllObserver(ArrayList<IncomingMsgObserver> observer) {
        observers.addAll(observer);
    }

    public static ArrayList<IncomingMsgObserver> getAllObservers() {
        return observers;
    }

    public static void notifyAllObservers(Cursor cursor) {
        for (IncomingMsgObserver observer : observers) {
            observer.handleMsg(cursor);
        }
    }
}
