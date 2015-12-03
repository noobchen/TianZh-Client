package com.tianzh.pay.utils;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * Author: cyc
 * Date: 12-4-17
 * Time: 下午4:18
 * Description: to write something
 */
public class AESUtils {
    private static final String aaaa = "duew&^%5d54nc'KH";

    public static String encode(String in) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        String hex = "";
        byte[] bytIn = in.getBytes();
        SecretKeySpec skeySpec = new SecretKeySpec(aaaa.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] bytOut = cipher.doFinal(bytIn);
        hex = byte2hexString(bytOut);

        return hex;
    }

    public static String decode(String hex) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        String rr = "";

        byte[] bytIn = hex2Bin(hex);

        SecretKeySpec skeySpec = new SecretKeySpec(aaaa.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);

        byte[] bytOut = cipher.doFinal(bytIn);
        rr = new String(bytOut);


        return rr;
    }

    private static byte[] hex2Bin(String src) {
        if (src.length() < 1)
            return null;
        byte[] encrypted = new byte[src.length() / 2];
        for (int i = 0; i < src.length() / 2; i++) {
            int high = Integer.parseInt(src.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(src.substring(i * 2 + 1, i * 2 + 2), 16);
            encrypted[i] = (byte) (high * 16 + low);
        }
        return encrypted;
    }

    private static String byte2hexString(byte buf[]) {
        StringBuffer strbuf = new StringBuffer(buf.length * 2);
        int i;
        for (i = 0; i < buf.length; i++) {
            strbuf.append(Integer.toString((buf[i] >> 4) & 0xf, 16) +
                    Integer.toString(buf[i] & 0xf, 16));
        }

        return strbuf.toString();
    }
}
