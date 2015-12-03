package com.tianzh.pay.service;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import com.lyhtgh.pay.SdkPayServer;
import com.tianzh.pay.model.DevicesInfo;
import com.tianzh.pay.utils.PayUtils;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pig on 2015-09-10.
 */
public class LetuPayServiceImpl implements LocalPayService {
    Activity context = null;

    private Handler appResultCB = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1000) {
                String retInfo = (String) msg.obj;

                String[] keyValues = retInfo.split("&|=");
                Map<String, String> resultMap = new HashMap<String, String>();
                for (int i = 0; i < keyValues.length; i = i + 2) {
                    resultMap.put(keyValues[i], keyValues[i + 1]);
                }

                String payResult = resultMap.get(SdkPayServer.PAYRET_KEY_RESULT_STATUS);

                if (null != payResult && Integer.parseInt(payResult) == SdkPayServer.PAY_RESULT_SUCCESS) {
                    //付费成功
                    PayUtils.sucessCallBack(context, tianzhOrderId, userOrderId, productId);
                } else {
                    //付费失败
                    String failedCode = resultMap.get(SdkPayServer.PAYRET_KEY_FAILED_CODE);

                    PayUtils.failCallBack(context, tianzhOrderId, userOrderId, productId, failedCodeMap.get(failedCode), "lt-" + failedCode);
                }
            }
        }
    };
    private String productId = "";
    private String userOrderId = "";
    private String tianzhOrderId = "";

    HashMap<String, String> failedCodeMap = null;

    public LetuPayServiceImpl(Activity context) {
        this.context = context;
        failedCodeMap = new HashMap<String, String>();

        failedCodeMap.put("101", "加载插件失败");
        failedCodeMap.put("105", "订单参数错误或SIM卡未插入");
        failedCodeMap.put("106", "系统正在付费");
        failedCodeMap.put("108", "付费jar包不能混淆");
        failedCodeMap.put("111", "接收状态报告超时，手机短信拦截");
        failedCodeMap.put("112", "接收二次状态超时");
        failedCodeMap.put("113", "没有收到二次短信");
        failedCodeMap.put("114", "用户取消普通确认");
        failedCodeMap.put("115", "用户取消资费确认");
        failedCodeMap.put("116", "获取通道失败");
        failedCodeMap.put("120", "状态报告失败，手机短信拦截");
        failedCodeMap.put("121", "短信服务当前不可用");
        failedCodeMap.put("123", "无线广播被明确地关闭");
        failedCodeMap.put("126", "发送短信抛出异常");
        failedCodeMap.put("127", "初始化UI异常");
        failedCodeMap.put("128", "没有可用网络");
        failedCodeMap.put("129", "发送短信队列已满");
        failedCodeMap.put("130", "访问服务器地址失败");
        failedCodeMap.put("131", "访问SP地址失败");
        failedCodeMap.put("132", "服务端无数据返回");
        failedCodeMap.put("133", "服务端返回失败");
        failedCodeMap.put("134", "验证码获取失败");
        failedCodeMap.put("180", "需要重新获取通道");
        failedCodeMap.put("183", "wap付费不正常");
        failedCodeMap.put("184", "wap付费不正常");

    }

    @Override
    public void pay(JSONObject params) {
        try {
            productId = params.getString("productId");
            userOrderId = params.getString("userOrderId");
            tianzhOrderId = params.getString("tianzhOrderId");

            String sig = SdkPayServer.getInstance().getSignature(params.getString("merchantKey"),
                    SdkPayServer.ORDER_INFO_ORDER_ID, userOrderId,
                    SdkPayServer.ORDER_INFO_MERCHANT_ID, DevicesInfo.letuMerchantId,
                    SdkPayServer.ORDER_INFO_APP_ID, DevicesInfo.letuAppid,
                    SdkPayServer.ORDER_INFO_APP_VER, DevicesInfo.appVersion,
                    SdkPayServer.ORDER_INFO_APP_NAME, params.getString("appName"),
                    SdkPayServer.ORDER_INFO_PAYPOINT, params.getString("payPointNum"),
                    SdkPayServer.ORDER_INFO_PAY_PRICE, params.getString("price"),
                    SdkPayServer.ORDER_INFO_PRODUCT_NAME, params.getString("productName"),
                    SdkPayServer.ORDER_INFO_ORDER_DESC, params.getString("orderDesc"),
                    SdkPayServer.ORDER_INFO_CP_CHANNELID, DevicesInfo.letuCpChannelId,
                    SdkPayServer.ORDER_INFO_SDK_CHANNELID, params.getString("sdkChannelId"),
                    SdkPayServer.ORDER_INFO_PAY_TYPE, params.getString("payType"),
                    SdkPayServer.ORDER_INFO_GAME_TYPE, params.getString("gameType")
            );

            String orderInfo =
                    SdkPayServer.ORDER_INFO_ORDER_ID + "=" + userOrderId + "&" +
                            SdkPayServer.ORDER_INFO_MERCHANT_ID + "=" + DevicesInfo.letuMerchantId + "&" +
                            SdkPayServer.ORDER_INFO_APP_ID + "=" + DevicesInfo.letuAppid + "&" +
                            SdkPayServer.ORDER_INFO_APP_VER + "=" + DevicesInfo.appVersion + "&" +
                            SdkPayServer.ORDER_INFO_APP_NAME + "=" + params.getString("appName") + "&" +
                            SdkPayServer.ORDER_INFO_PAYPOINT + "=" + params.getString("payPointNum") + "&" +
                            SdkPayServer.ORDER_INFO_PAY_PRICE + "=" + params.getString("price") + "&" +
                            SdkPayServer.ORDER_INFO_PRODUCT_NAME + "=" + params.getString("productName") + "&" +
                            SdkPayServer.ORDER_INFO_ORDER_DESC + "=" + params.getString("orderDesc") + "&" +
                            SdkPayServer.ORDER_INFO_CP_CHANNELID + "=" + DevicesInfo.letuCpChannelId + "&" +
                            SdkPayServer.ORDER_INFO_SDK_CHANNELID + "=" + params.getString("sdkChannelId") + "&" +
                            SdkPayServer.ORDER_INFO_PAY_TYPE + "=" + params.getString("payType") + "&" +
                            SdkPayServer.ORDER_INFO_GAME_TYPE + "=" + params.getString("gameType") + "&" +
                            SdkPayServer.ORDER_INFO_MERCHANT_SIGN + "=" + sig + "&" +
                            SdkPayServer.ORDER_INFO_SHOW_PAYUIKEY + "=" + params.getString("showUIKey");

            int result = SdkPayServer.getInstance().startSdkServerPay(context, appResultCB, orderInfo);
            if (result != 0)
                throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
            PayUtils.exceptionCallBack(context, tianzhOrderId, userOrderId, productId, "数据解析异常！", "30000");
        }
    }
}
