package com.tianzh.pay;

import android.app.Application;
import android.content.Context;
import com.lyhtgh.pay.application.PayApplication;

/**
 * Created by pig on 2015-09-10.
 */
public class TianZhPayApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        init(getApplicationContext());
    }

    public void init(Context context){
        PayApplication payApplication = new PayApplication();
        payApplication.a(context);

        System.loadLibrary("cpid11");
        System.loadLibrary("mgpbase");

    }
}
