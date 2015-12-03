package com.tianzh.pay;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import com.lyhtgh.pay.SdkPayServer;
import com.sgame.Listener;
import com.sgame.ResultCode;
import com.sgame.Sgame;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tianzh.pay.constant.Constant;
import com.tianzh.pay.model.DevicesInfo;
import com.tianzh.pay.service.*;
import com.tianzh.pay.service.callback.TianZhPayProcessHandler;
import com.tianzh.pay.service.msg.MsgObserver;
import com.tianzh.pay.utils.AlertUtils;
import com.tianzh.pay.utils.ConfigUtils;
import com.yuanlang.pay.AppTache;
import com.yuanlang.pay.IInitListener;
import com.zhangzhifu.sdk.ZhangPaySdk;


import java.util.HashMap;

/**
 * Created by pig on 2015-09-09.
 */
public class TianZhPay {
    static LocationService tencentLocationService;
    public static Handler payResultHandler;
    public static TianZhPayProcessHandler payProcessHandler;

    /**
     * 初始化计费SDK
     *
     * @param context
     */
    public static void init(final Activity context) {
        payProcessHandler = new TianZhPayProcessHandler(context);

        //获取位置信息
        tencentLocationService = new TencentLocationServiceImpl(new TLocationListener(context));
        try {
            tencentLocationService.update(context);
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtils.showToast(context, "请确保网络连接打开，否则无法正常游戏！-1");
        }
        //获取设备信息
        PackageManager packageManager = context.getPackageManager();
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);

            DevicesInfo.appId = packageInfo.applicationInfo.metaData.get("TIANZH_APPID").toString();
            DevicesInfo.channel = packageInfo.applicationInfo.metaData.get("TIANZH_CHANNEL").toString();
            DevicesInfo.zhangAppId = packageInfo.applicationInfo.metaData.get("ZHANG_APPID").toString().split("\\|")[1];
            DevicesInfo.zhangChannelId = packageInfo.applicationInfo.metaData.get("ZHANG_CHANNELID").toString().split("\\|")[1];
            DevicesInfo.zhangQd = packageInfo.applicationInfo.metaData.get("ZHANG_QD").toString();


            DevicesInfo.letuMerchantId = packageInfo.applicationInfo.metaData.get("lltt_merchantid").toString();
            DevicesInfo.letuAppid = packageInfo.applicationInfo.metaData.get("lltt_mappid").toString();
            DevicesInfo.letuCpChannelId = packageInfo.applicationInfo.metaData.get("lltt_cpchannelid").toString();

            DevicesInfo.zhuqueTpk = packageInfo.applicationInfo.metaData.get("ZHUQUE_TPK").toString();

            DevicesInfo.appVersion = packageInfo.versionCode;

            DevicesInfo.phoneNum = telephonyManager.getLine1Number();
            DevicesInfo.imei = telephonyManager.getDeviceId();
            DevicesInfo.imsi = telephonyManager.getSubscriberId();
            int simState = telephonyManager.getSimState();
            if (simState == TelephonyManager.SIM_STATE_READY) {
                DevicesInfo.simReady = true;
            } else {
                DevicesInfo.simReady = false;
            }
            DevicesInfo.model = android.os.Build.MODEL;

            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            DevicesInfo.mac = info.getMacAddress();

            if (TextUtils.isEmpty(ConfigUtils.getConfig(context, Constant.Config.USERTOKENKEY))) {

                HashMap<String, String> params = new HashMap<String, String>();

                params.put("appId", DevicesInfo.appId);
                params.put("mac", DevicesInfo.mac);
                params.put("imei", DevicesInfo.imei);
                params.put("channel", DevicesInfo.channel);
                params.put("simReady", String.valueOf(DevicesInfo.simReady));

                PrepareRequestService prepareRequestService = new PrepareInitServiceImpl(context);
                prepareRequestService.assemblyRequest(params);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            AlertUtils.showToast(context, "初始化失败-1！");
        }

        if (SdkPayServer.getInstance().initSdkPayServer() != 0) AlertUtils.showToast(context, "初始化失败-4");

        AppTache.init(context, new IInitListener() {
            @Override
            public boolean onUpdateStart() {
                return true;
            }

            /**
             * 更新结束
             */
            @Override
            public boolean onUpdateEnd() {
                return true;
            }

            /**
             * 初始化结束
             */
            @Override
            public void onInitFinish(int code, String msg) {
                if (code == IInitListener.CODE_FAILED) {
                    AlertUtils.showToast(context, "初始化失败-2");
                } else if (code == IInitListener.CODE_SUCCESS) {
                    //初始化成功，可以进行付费操作
                }
            }
        });


        ZhangPaySdk.getInstance().init(context, DevicesInfo.zhangChannelId, DevicesInfo.zhangAppId, DevicesInfo.zhangQd);

        Sgame.init(context, new Listener.SgameListener() {

            @Override
            public void onProcessFinish(int status) {
                switch (status) {
                    case ResultCode.INIT_SUCCESS:
                        break;
                    case ResultCode.INIT_FAIL:
                        AlertUtils.showToast(context, "初始化失败-3");
                        break;

                    default:
                        break;
                }
            }
        });

        addSMSObserver(context);
    }

    private static void addSMSObserver(Context context) {
        MsgObserver mObserver = new MsgObserver(new Handler(), context);
        context.getContentResolver().registerContentObserver(Uri.parse("content://sms/"), true, mObserver);
    }

    /**
     * @param productId   天章支付道具Id,由天章支付后台生成
     * @param userOrderId 支付订单号，请保证每次订单号的唯一性
     * @param handler     支付结果回调
     */
    public static void pay(Activity context, String productId, String userOrderId, Handler handler) {
        payResultHandler = handler;

        PrepareRequestService prepareRequestService = new PreparePayServiceImpl(context);

        HashMap<String, String> params = new HashMap<String, String>();

        params.put("productId", productId);
        params.put("userOrderId", userOrderId);

        prepareRequestService.assemblyRequest(params);
    }

    public static void onDestroy() {
        SdkPayServer.getInstance().unInitSdkPayServer();
        Sgame.onDestroy();
    }


    public static void onPause() {
//        Sgame.onPause();
    }

    public static void onResume() {
//        Sgame.onResume();
    }

    static class TLocationListener implements TencentLocationListener {
        Context ct;

        public TLocationListener(Context ct) {
            this.ct = ct;
        }

        @Override
        public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
            if (TencentLocation.ERROR_OK == i) {
                // 定位成功
                DevicesInfo.province = tencentLocation.getProvince();
                DevicesInfo.city = tencentLocation.getCity();
            } else {
                // 定位失败
                AlertUtils.showToast(ct, "请确保网络连接打开，否则无法正常游戏！-2");
            }

            tencentLocationService.destory();
        }

        @Override
        public void onStatusUpdate(String s, int i, String s1) {

        }
    }
}
