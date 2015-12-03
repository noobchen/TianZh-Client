package com.tianzh.pay.service;

import android.text.TextUtils;
import com.tianzh.pay.constant.Constant;
import com.tianzh.pay.service.callback.NetWorkListener;
import com.tianzh.pay.utils.AESUtils;
import com.tianzh.pay.utils.StreamUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by pig on 2015-09-28.
 */
public class InitNetWorkServiceImpl implements NetWorkService {
    private String urlStr = Constant.HOST+"/init";

    @Override
    public void get(String jsonString, NetWorkListener listener) {

    }

    @Override
    public void post(String jsonString, NetWorkListener listener) {
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            URL url = new URL(urlStr);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(10 * 1000);
            urlConnection.setReadTimeout(10 * 1000);

            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);

            urlConnection.connect();

            OutputStream outputStream = urlConnection.getOutputStream();

            String postContent = AESUtils.encode(jsonString);

            outputStream.write(postContent.getBytes());
            outputStream.flush();
            outputStream.close();

            int responseCode = urlConnection.getResponseCode();

            if (responseCode != 200) {
                throw new Exception();
            }

            inputStream = urlConnection.getInputStream();
            String response = StreamUtils.readFromInputStream(inputStream);

            if (TextUtils.isEmpty(response)) {
                throw new Exception();
            }

            response = AESUtils.decode(response);

            listener.onResponse(200, response);
        } catch (Exception e) {
            e.printStackTrace();
            listener.onResponse(404, jsonString);
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
        }
    }
}
