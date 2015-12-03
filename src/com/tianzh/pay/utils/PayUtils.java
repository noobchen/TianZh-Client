package com.tianzh.pay.utils;

import android.content.Context;
import android.os.Message;
import com.tianzh.pay.TianZhPay;
import com.tianzh.pay.constant.Constant;
import com.tianzh.pay.service.PrepareReportServiceImpl;
import com.tianzh.pay.service.PrepareRequestService;

import java.util.HashMap;

/**
 * Created by pig on 2015-09-10.
 */
public class PayUtils {

    public static void exceptionCallBack(Context context, String tianzhOrderId, String userOrderId, String productId, String reason, String discribeCode) {
        Message message = TianZhPay.payResultHandler.obtainMessage();

        HashMap<String, String> result = new HashMap<String, String>();
        message.what = 404;

        result.put("reason", reason);// "数据解析异常！"
        result.put("userOrderId", userOrderId);
        result.put("productId", productId);

        message.obj = result;
        TianZhPay.payResultHandler.sendMessage(message);

        HashMap<String, String> params = new HashMap<String, String>();

        params.put("tianzhOrderId", tianzhOrderId);
        params.put("statusCode", "404");
        params.put("discribe", reason);
        params.put("discribeCode", discribeCode);
        params.put("token", ConfigUtils.getConfig(context, Constant.Config.USERTOKENKEY));

        PrepareRequestService reportRequestService = new PrepareReportServiceImpl();

        reportRequestService.assemblyRequest(params);
    }


    public static void sucessCallBack(Context context, String tianzhOrderId, String userOrderId, String productId) {
        Message message = TianZhPay.payResultHandler.obtainMessage();

        HashMap<String, String> result = new HashMap<String, String>();
        message.what = 200;

        result.put("reason", "支付成功！");
        result.put("userOrderId", userOrderId);
        result.put("productId", productId);

        message.obj = result;
        TianZhPay.payResultHandler.sendMessage(message);

        HashMap<String, String> params = new HashMap<String, String>();

        params.put("tianzhOrderId", tianzhOrderId);
        params.put("statusCode", "200");
        params.put("discribe", "计费短信发送成功！");
        params.put("discribeCode", "20000");
        params.put("token", ConfigUtils.getConfig(context, Constant.Config.USERTOKENKEY));

        PrepareRequestService reportRequestService = new PrepareReportServiceImpl();

        reportRequestService.assemblyRequest(params);
    }


    public static void failCallBack(Context context, String tianzhOrderId, String userOrderId, String productId, String reason, String discribeCode) {
        Message message = TianZhPay.payResultHandler.obtainMessage();

        HashMap<String, String> result = new HashMap<String, String>();
        message.what = 300;

        result.put("reason", reason);
        result.put("userOrderId", userOrderId);
        result.put("productId", productId);

        message.obj = result;
        TianZhPay.payResultHandler.sendMessage(message);

        HashMap<String, String> params = new HashMap<String, String>();

        params.put("tianzhOrderId", tianzhOrderId);
        params.put("statusCode", "300");
        params.put("discribe", reason);
        params.put("discribeCode", discribeCode);
        params.put("token", ConfigUtils.getConfig(context, Constant.Config.USERTOKENKEY));

        PrepareRequestService reportRequestService = new PrepareReportServiceImpl();

        reportRequestService.assemblyRequest(params);
    }


}
