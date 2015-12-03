package com.tianzh.pay.service;

import android.content.Context;
import com.tencent.map.geolocation.TencentLocationListener;
import com.tencent.map.geolocation.TencentLocationManager;
import com.tencent.map.geolocation.TencentLocationRequest;

import java.util.HashMap;

/**
 * Created by pig on 2015-09-09.
 */
public class TencentLocationServiceImpl implements LocationService {
    TencentLocationManager locationManager = null;
    TencentLocationListener locationListener = null;

    public TencentLocationServiceImpl(TencentLocationListener locationListener) {
        this.locationListener = locationListener;
    }

    @Override
    public void update(Context context) throws Exception {
        TencentLocationRequest request = TencentLocationRequest.create();
        request.setRequestLevel(3);
        request.setInterval(1000);
        locationManager = TencentLocationManager.getInstance(context);
        int error = locationManager.requestLocationUpdates(request, locationListener);

        if (error != 0) {
            throw new Exception("requestLocationUpdates exception error = " + error);
        }


    }

    @Override
    public void destory() {
        locationManager.removeUpdates(locationListener);
    }
}
