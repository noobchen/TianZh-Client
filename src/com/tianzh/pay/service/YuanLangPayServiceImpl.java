package com.tianzh.pay.service;

import android.content.Context;
import android.content.Intent;
import com.tianzh.pay.YLPayActivity;
import com.tianzh.pay.utils.PayUtils;
import org.json.JSONObject;

/**
 * Created by pig on 2015-09-10.
 */
public class YuanLangPayServiceImpl implements LocalPayService {
    Context context = null;
    String productId = "";
    String userOrderId = "";
    String tianzhOrderId = "";

    public YuanLangPayServiceImpl(Context context) {
        this.context = context;
    }

    @Override
    public void pay(JSONObject params) {
        Intent intent = new Intent(context, YLPayActivity.class);
        try {
            intent.putExtra("is_online", params.getString("is_online"));
            intent.putExtra("unit_price", params.getString("unit_price"));
            intent.putExtra("quantity", params.getString("quantity"));
            intent.putExtra("goods_id", params.getString("goods_id"));
            intent.putExtra("goods_name", params.getString("goods_name"));
            intent.putExtra("user_order_id", params.getString("user_order_id"));
            intent.putExtra("requestType", params.getString("requestType"));

            productId = params.getString("productId");
            userOrderId = params.getString("userOrderId");
            tianzhOrderId = params.getString("tianzhOrderId");

            intent.putExtra("productId", productId);
            intent.putExtra("userOrderId", userOrderId);
            intent.putExtra("tianzhOrderId", tianzhOrderId);

            context.startActivity(intent);
        } catch (Exception e) {
            PayUtils.exceptionCallBack(context,tianzhOrderId, userOrderId, productId, "数据解析异常！", "30000");
        }
    }
}
