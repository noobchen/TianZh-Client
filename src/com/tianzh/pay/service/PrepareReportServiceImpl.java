package com.tianzh.pay.service;

import com.tianzh.pay.service.callback.NetWorkListener;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by pig on 2015-09-10.
 */
public class PrepareReportServiceImpl implements PrepareRequestService {

    @Override
    public void assemblyRequest(HashMap<String, String> params) {
        final JSONObject jsonObject = new JSONObject(params);

        final NetWorkService reportNetWorkService = new ReportNetWorkServiceImpl();

        new Thread(new Runnable() {
            @Override
            public void run() {
                reportNetWorkService.post(jsonObject.toString(), new NetWorkListener() {
                    @Override
                    public void onResponse(int statusCode, String responseStr) {
                        //暂不处理
                    }
                });
            }
        }).start();

    }
}
