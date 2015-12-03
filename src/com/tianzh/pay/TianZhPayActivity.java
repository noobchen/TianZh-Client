package com.tianzh.pay;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.product.pay.callback.TzPayUtils;
import com.tianz.txgd.R;
import com.tianzh.pay.constant.Constant;
import com.tianzh.pay.service.msg.*;
import com.tianzh.pay.utils.LogUtils;
import com.tianzh.pay.utils.MsgObserverUtils;

public class TianZhPayActivity extends Activity {
    /**
     * Called when the activity is first created.
     */

    public static void main(String[] args) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TzPayUtils.init(this);
    }

    public void buy(View view) {
        String payId = "7#1#400";

        TzPayUtils.pay(payId);

    }

    private String getPrice(String productName) {
        if (productName.equals("4#buy#1500")) return "Fb2iy2";
        if (productName.equals("8#buy#1000")) return "VfeQRz";
        if (productName.equals("6#buy#400")) return "ayqAjq";
        if (productName.equals("1#buy#800")) return "YVVbei";
        if (productName.equals("2#buy#800")) return "mAFfa2";
        if (productName.equals("3#buy#800")) return "emqamu";
        if (productName.equals("5#buy#1000")) return "73uqie";
        if (productName.equals("7#1#400")) return "6Vjaui";

        return "";

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TianZhPay.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        TianZhPay.onResume();

    }


    @Override
    protected void onPause() {
        super.onPause();
        TianZhPay.onPause();
    }
}
