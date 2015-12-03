package com.tianzh.pay.service;

import android.app.Activity;
import android.os.Message;
import android.text.TextUtils;
import com.tencent.map.geolocation.TencentLocation;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tianzh.pay.TianZhPay;
import com.tianzh.pay.constant.Constant;
import com.tianzh.pay.model.DevicesInfo;
import com.tianzh.pay.model.Order;
import com.tianzh.pay.service.callback.PayNetWorkListener;
import com.tianzh.pay.service.callback.TianZhPayProcessHandler;
import com.tianzh.pay.utils.AlertUtils;
import com.tianzh.pay.utils.ConfigUtils;
import com.tianzh.pay.utils.PayUtils;

import java.util.HashMap;

/**
 * Created by pig on 2015-09-09.
 */
public class PreparePayServiceImpl implements PrepareRequestService, TencentLocationListener {
    Activity context;
    String productId;
    String userOrderId;
    LocationService tencentLocationService = null;

    public PreparePayServiceImpl(Activity context) {
        this.context = context;
    }

    @Override
    public void assemblyRequest(HashMap<String, String> params) {
        productId = params.get("productId");
        userOrderId = params.get("userOrderId");

        if (!DevicesInfo.simReady) {
            Message message = TianZhPay.payProcessHandler.obtainMessage();

            message.what = 9997;
            message.obj = "无卡用户！";
            TianZhPay.payProcessHandler.sendMessage(message);
            exceptionCallBack("无卡用户！", productId);
            return;
        }

        if (TextUtils.isEmpty(DevicesInfo.appId) || TextUtils.isEmpty(DevicesInfo.channel)) {
            //未配置应用Id及渠道Id
            Message message = TianZhPay.payProcessHandler.obtainMessage();

            message.what = 9997;
            message.obj = "appId或channelId配置错误！";
            TianZhPay.payProcessHandler.sendMessage(message);
            exceptionCallBack("appId或channelId配置错误！", productId);
            return;
        }

        if (TextUtils.isEmpty(productId) || TextUtils.isEmpty(userOrderId)) {
            //未设置正确参数
            Message message = TianZhPay.payProcessHandler.obtainMessage();

            message.what = 9997;
            message.obj = "productId或userOrderId配置错误！";
            TianZhPay.payProcessHandler.sendMessage(message);
            exceptionCallBack("productId或userOrderId配置错误！", productId);
            return;
        }

        //请求服务器逻辑
        TianZhPay.payProcessHandler.sendEmptyMessage(9998);

        if (TextUtils.isEmpty(DevicesInfo.province)) {

            //获取位置信息
            tencentLocationService = new TencentLocationServiceImpl(this);
            try {
                tencentLocationService.update(context);
            } catch (Exception e) {
                e.printStackTrace();
                Message message = TianZhPay.payProcessHandler.obtainMessage();

                message.what = 9997;
                message.obj = "请确保网络连接打开，否则无法正常游戏！-3";
                TianZhPay.payProcessHandler.sendMessage(message);
                requestServer(userOrderId, productId);
            }

            return;
        }

        requestServer(userOrderId, productId);

    }


    private void requestServer(String userOrderId, String productId) {
        final Order order = new Order();

        order.setAppId(DevicesInfo.appId);
        order.setChannel(DevicesInfo.channel);
        order.setProvince(DevicesInfo.province);
        order.setCity(DevicesInfo.city);
        order.setUserOrderId(userOrderId);
        order.setProductId(productId);
        order.setImei(DevicesInfo.imei);
        order.setImsi(DevicesInfo.imsi);
        order.setPhoneNum(DevicesInfo.phoneNum);
        order.setModel(DevicesInfo.model);
        String userToken = ConfigUtils.getConfig(context, Constant.Config.USERTOKENKEY);
        order.setUserToken(userToken);
        order.setMac(DevicesInfo.mac);

        final NetWorkService payNetWorkService = new PayNetWorkServiceImpl();
        final PayNetWorkListener listener = new PayNetWorkListener(context);
        new Thread(new Runnable() {
            @Override
            public void run() {
                payNetWorkService.post(order.toString(), listener);

            }
        }).start();
    }

    private void exceptionCallBack(String reason, String productId) {
        Message message = TianZhPay.payResultHandler.obtainMessage();

        HashMap<String, String> result = new HashMap<String, String>();
        message.what = 404;

        result.put("reason", reason);// "数据解析异常！"
        result.put("userOrderId", "");
        result.put("productId", productId);

        message.obj = result;
        TianZhPay.payResultHandler.sendMessage(message);

    }

    @Override
    public void onLocationChanged(TencentLocation tencentLocation, int i, String s) {
        if (TencentLocation.ERROR_OK == i) {
            // 定位成功
            DevicesInfo.province = tencentLocation.getProvince();
            DevicesInfo.city = tencentLocation.getCity();
        } else {
            // 定位失败
            AlertUtils.showToast(context, "请确保网络连接打开，否则无法正常游戏！");
        }

        tencentLocationService.destory();
        requestServer(userOrderId, productId);
    }

    @Override
    public void onStatusUpdate(String s, int i, String s1) {

    }
}
