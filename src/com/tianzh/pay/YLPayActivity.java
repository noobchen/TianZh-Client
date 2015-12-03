package com.tianzh.pay;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.tianzh.pay.utils.PayUtils;
import com.yuanlang.pay.AppTache;

/**
 * Created by pig on 2015-09-10.
 */
public class YLPayActivity extends Activity {
    private int REQUEST_TYPE;
    private String userOrderId = "";
    private String productId = "";
    String tianzhOrderId = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        String is_online = intent.getStringExtra("is_online");
        String unit_price = intent.getStringExtra("unit_price");
        String quantity = intent.getStringExtra("quantity");
        String goods_id = intent.getStringExtra("goods_id");
        String goods_name = intent.getStringExtra("goods_name");
        String user_order_id = intent.getStringExtra("tianzhOrderId");
        String requestType = intent.getStringExtra("requestType");


        productId = intent.getStringExtra("productId");
        userOrderId = intent.getStringExtra("userOrderId");
        tianzhOrderId = intent.getStringExtra("tianzhOrderId");

        REQUEST_TYPE = Integer.parseInt(requestType);

        AppTache.requestPay(this,
                Boolean.getBoolean(is_online), Integer.parseInt(unit_price),
                Integer.parseInt(quantity), goods_id, goods_name, user_order_id,
                Integer.parseInt(requestType));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TYPE) {//请求的requestCode
            Bundle bundle = data.getExtras();
            if (null != bundle) {
                String is_success = "" + (resultCode == 100);//是否成功
                String real_price = "" + bundle.getInt("order_price");//本次支付费用
                String user_order_id = "" + bundle.getString("order_id");//用户定义的订单号
                String error_code = "" + bundle.getString("pay_result_id");//支付结果错误编号
                String error_msg = "" + bundle.getString("pay_result_msg");//失败时返回的错误原因
                if (is_success.contains("true")) {
                    // 支付成功
                    PayUtils.sucessCallBack(this,tianzhOrderId, userOrderId, productId);
                    this.finish();
                } else {
                    // 支付失败
                    PayUtils.failCallBack(this,tianzhOrderId, userOrderId, productId, error_msg, "yl-" + error_code);
                    this.finish();
                }

            }
        }
    }
}