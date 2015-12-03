package com.tianzh.pay.service;

import android.content.Context;
import com.tencent.map.geolocation.TencentLocationListener;

import java.util.HashMap;

/**
 * Created by pig on 2015-09-09.
 */
public interface LocationService {

    void update(Context context) throws Exception;
    void destory();
}
