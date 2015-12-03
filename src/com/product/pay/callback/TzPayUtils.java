package com.product.pay.callback;

import android.app.Activity;
import com.tianzh.pay.TianZhPay;

import java.io.File;

/**
 * Created by pig on 2015-10-22.
 */
public class TzPayUtils {
    public static Activity mActivity;
    private static PayResultHandler resultHandler;

    public static void init(Activity activity) {
        mActivity = activity;
        resultHandler = new PayResultHandler(activity);

        TianZhPay.init(activity);
    }

    public static void pay(String payId) {
        String price = getPrice(payId);

        TianZhPay.pay(mActivity, price, System.currentTimeMillis() + "", resultHandler);
    }

    private static String getPriceId(String payId) {
        switch (Integer.parseInt(payId)) {
            case 100:
                return "3qYRFb";
            case 101:
                return "IjIz6v";
            case 102:
                return "RnE7na";
            case 103:
                return "m2aaMz";
            case 200:
                return "qAVfaq";
            case 201:
                return "yI7vQj";
            case 202:
                return "vMvQvm";
            case 203:
                return "J7JRj2";
            case 301:
                return "mUB3qu";
            case 302:
                return "z2M3iy";
            case 303:
                return "yyIRzu";
        }

        return "3qYRFb";
    }

    private static String getPrice(String productName) {
        if (productName.equals("4#buy#1500")) return "Fb2iy2";
        if (productName.equals("8#buy#1000")) return "VfeQRz";
        if (productName.equals("6#buy#400")) return "ayqAjq";
        if (productName.equals("1#buy#800")) return "YVVbei";
        if (productName.equals("2#buy#800")) return "mAFfa2";
        if (productName.equals("3#buy#800")) return "emqamu";
        if (productName.equals("5#buy#1000")) return "73uqie";
        if (productName.equals("7#1#400")) return "6Vjaui";

        return "6bEJru";
    }
}
