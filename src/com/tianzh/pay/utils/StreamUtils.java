package com.tianzh.pay.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by pig on 2015-09-10.
 */
public class StreamUtils {

    public static String readFromInputStream(InputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffers = new byte[1024];

        int size = -1;
        try {
            while ((size = inputStream.read(buffers)) != -1)
                byteArrayOutputStream.write(buffers, 0, size);

            byteArrayOutputStream.flush();
            String string = byteArrayOutputStream.toString("utf-8");
            inputStream.close();
            byteArrayOutputStream.close();

            return string;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
