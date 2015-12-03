package com.tianzh.pay.service.callback;

/**
 * Created by pig on 2015-09-10.
 */
public interface NetWorkListener {
    void onResponse(int statusCode,String responseStr);
}
