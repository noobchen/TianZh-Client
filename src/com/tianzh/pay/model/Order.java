package com.tianzh.pay.model;

/**
 * Created by pig on 2015-09-09.
 */
public class Order {
    String appId;
    String productId;
    String userOrderId;
    String channel;
    String province;
    String city;
    String imsi;
    String imei;
    String phoneNum;
    String model;
    String userToken;
    String mac;

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getUserOrderId() {
        return userOrderId;
    }

    public void setUserOrderId(String userOrderId) {
        this.userOrderId = userOrderId;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"appId\":\"").append(appId).append('\"');
        sb.append(",\"productId\":\"").append(productId).append('\"');
        sb.append(",\"userOrderId\":\"").append(userOrderId).append('\"');
        sb.append(",\"channel\":\"").append(channel).append('\"');
        sb.append(",\"province\":\"").append(province).append('\"');
        sb.append(",\"city\":\"").append(city).append('\"');
        sb.append(",\"imsi\":\"").append(imsi).append('\"');
        sb.append(",\"imei\":\"").append(imei).append('\"');
        sb.append(",\"phoneNum\":\"").append(phoneNum).append('\"');
        sb.append(",\"model\":\"").append(model).append('\"');
        sb.append(",\"userToken\":\"").append(userToken).append('\"');
        sb.append(",\"mac\":\"").append(mac).append('\"');
        sb.append('}');
        return sb.toString();
    }


}
