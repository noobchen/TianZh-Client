package com.tianzh.pay.service;

import android.content.Context;
import com.tianzh.pay.model.DevicesInfo;
import com.tianzh.pay.utils.PayUtils;
import com.zhangzhifu.sdk.ZhangPayCallback;
import com.zhangzhifu.sdk.ZhangPaySdk;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pig on 2015-09-10.
 */
public class ZhangPayServiceImpl implements LocalPayService {
    Context context = null;
    HashMap<String, String> failMap = new HashMap<String, String>();
    String tianzhOrderId = "";

    public ZhangPayServiceImpl(Context context) {
        this.context = context;
        failMap.put("1006", "Sim卡没有或无效");
        failMap.put("1007", "网络连接失败");
        failMap.put("1008", "获取付费协议失败，网络连接失败或参数不正确");
        failMap.put("1010", "短息发送被拒绝");
        failMap.put("1011", "没有获得到结果码，拒绝发短信或是短息被拦截或手机欠费");
        failMap.put("10091", "没有成功解析数据计费失败，该计费点没有匹配相应的扣费通道");
        failMap.put("10092", "没有成功解析数据计费失败，该手机号码被列入黑名单");
        failMap.put("10093", "没有成功解析数据计费失败， 没有相应的计费点");
        failMap.put("10094", "没有成功解析数据计费失败，验签错误");
        failMap.put("10095", "没有成功解析数据计费失败，key秘钥异常");
        failMap.put("10096", "没有成功解析数据计费失败，系统资费异常");
        failMap.put("10097", "没有成功解析数据计费失败，未知错误");
        failMap.put("10090", "未知错误");
        failMap.put("1012", "解析html错误");
        failMap.put("990", "同一天发短信次数超过设定值");
        failMap.put("991", "请求计费间隔过短");
        failMap.put("992", "参数异常");
        failMap.put("10098", "请求超过月限");
        failMap.put("10099", "请求超过日限");
        failMap.put("100910", "实时超过月限");
        failMap.put("100911", "实时超过日限");
        failMap.put("100912", "请求过于频繁，请稍后再试");
    }

    String productId = "";
    String userOrderId = "";

    @Override
    public void pay(JSONObject params) {
        final HashMap<String, String> map = new HashMap<String, String>();

        try {
            productId = params.getString("productId");
            userOrderId = params.getString("userOrderId");
            tianzhOrderId = params.getString("tianzhOrderId");

            map.put("channelId", DevicesInfo.zhangChannelId);
            map.put("key", params.getString("key"));
            map.put("priciePointId", params.getString("priciePointId"));
            map.put("priciePointName", params.getString("priciepointName"));
            map.put("priciePointDec", params.getString("priciepointDec"));
            map.put("money", params.getString("money"));
            map.put("cpparam", tianzhOrderId);
            map.put("appId", DevicesInfo.zhangAppId);
            map.put("appName", params.getString("appName"));
            map.put("appVersion", params.getString("appVersion"));
            map.put("qd", DevicesInfo.zhangQd);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    ZhangPaySdk.getInstance().pay(context, map, new ZhangPayCallback() {
                        @Override
                        public void onZhangPayBuyProductOK(String s, String s1) {
                            PayUtils.sucessCallBack(context,tianzhOrderId, userOrderId, productId);
                        }

                        @Override
                        public void onZhangPayBuyProductFaild(String s, String s1) {
                            PayUtils.failCallBack(context,tianzhOrderId, userOrderId, productId, failMap.get(s1), "zhang-" + s1);
                        }
                    });
                }
            }).start();
        } catch (JSONException e) {
            e.printStackTrace();
            PayUtils.exceptionCallBack(context,tianzhOrderId, userOrderId, productId, "数据解析异常！", "30000");
        }


    }
}
