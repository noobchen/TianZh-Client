package com.tianzh.pay.service;

import com.tianzh.pay.service.callback.NetWorkListener;

/**
 * Created by pig on 2015-09-10.
 */
public interface NetWorkService {
    void get(String jsonString, NetWorkListener listener);

    void post(String jsonString, NetWorkListener listener);
}
