package com.tianzh.pay.utils;

import com.tianzh.pay.constant.Constant;
import com.tianzh.pay.service.PrepareReportServiceImpl;
import com.tianzh.pay.service.PrepareRequestService;

import java.util.HashMap;

/**
 * Created by pig on 2015-11-14.
 */
public class PaidWaysReportUtils {

    public static void sucessReport(int paidWaysId, String tianzhOrderId) {
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("reportType", "1");//按条回报计费状态
        params.put("tianzhOrderId", tianzhOrderId);
        params.put("paidWaysId", String.valueOf(paidWaysId));
        params.put("statusCode", "200");
        PrepareRequestService reportRequestService = new PrepareReportServiceImpl();

        reportRequestService.assemblyRequest(params);
    }


    public static void failReport(int paidWaysId, String tianzhOrderId, int statusCode) {
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("reportType", "1");//按条回报计费状态
        params.put("tianzhOrderId", tianzhOrderId);
        params.put("paidWaysId", String.valueOf(paidWaysId));
        params.put("statusCode", String.valueOf(statusCode));   //300 发送失败  301 未收到二次确认短信 302 短信1发送失败 303 短信2发送失败
        PrepareRequestService reportRequestService = new PrepareReportServiceImpl();

        reportRequestService.assemblyRequest(params);
    }
}
