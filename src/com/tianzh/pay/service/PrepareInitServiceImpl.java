package com.tianzh.pay.service;

import android.content.Context;
import com.tianzh.pay.constant.Constant;
import com.tianzh.pay.service.callback.NetWorkListener;
import com.tianzh.pay.utils.ConfigUtils;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by pig on 2015-09-28.
 */
public class PrepareInitServiceImpl implements PrepareRequestService {

    private Context context;

    public PrepareInitServiceImpl(Context context) {
        this.context = context;
    }

    @Override
    public void assemblyRequest(HashMap<String, String> params) {
        final JSONObject jsonObject = new JSONObject(params);

        final NetWorkService netWorkService = new InitNetWorkServiceImpl();

        new Thread(new Runnable() {
            @Override
            public void run() {
                netWorkService.post(jsonObject.toString(), new NetWorkListener() {
                    @Override
                    public void onResponse(int statusCode, String responseStr) {
                        //状态码是404，服务端错误，不记录服务端返回token
                        if (statusCode == 404) {

                            return;
                        }

                        //记录服务端返回token
                        if (statusCode == 200) {
                            try {
                                JSONObject resJson = new JSONObject(responseStr);
                                String usertoken = resJson.getString("usertoken");
                                ConfigUtils.setConfig(context, Constant.Config.USERTOKENKEY, usertoken);

                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                        }
                    }
                });
            }
        }).start();

    }
}
