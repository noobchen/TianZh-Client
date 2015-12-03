package com.tianzh.pay.model;

/**
 * Created by pig on 2015-09-09.
 */
public class DevicesInfo {
    public static String appId;
    public static String channel;
    public static String province;
    public static String city;
    public static String imsi;
    public static String imei;
    public static String mac;
    public static String phoneNum;
    public static String model;
    public static boolean simReady;

    public static String zhangAppId;
    public static String zhangChannelId;
    public static String zhangQd;


    public static String letuMerchantId;
    public static String letuAppid;
    public static String letuCpChannelId;

    public static String zhuqueTpk;

    public static int appVersion;

    


    public static String string() {
        final StringBuilder sb = new StringBuilder("DevicesInfo{");
        sb.append("appId='").append(appId).append('\'');
        sb.append(", channel='").append(channel).append('\'');
        sb.append(", province=").append(province);
        sb.append(", city='").append(city).append('\'');
        sb.append(", imsi='").append(imsi).append('\'');
        sb.append(", imei='").append(imei).append('\'');
        sb.append(", mac='").append(mac).append('\'');
        sb.append(", phoneNum='").append(phoneNum).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
