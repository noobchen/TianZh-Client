package com.tianzh.pay.model;

import com.tianzh.pay.service.msg.SendMsgStatusReader;

/**
 * Created by pig on 2015-11-13.
 */
public class SimplePaidMsg extends PaidMsg {

    private String port;
    private String msg;
    private SendMsgStatusReader sendMsgStatusReader;

    public SimplePaidMsg(String port, String msg, SendMsgStatusReader sendMsgStatusReader) {
        this.port = port;
        this.msg = msg;
        this.sendMsgStatusReader = sendMsgStatusReader;
    }

    @Override
    public void pay() {
        sendMsgStatusReader.send(port, msg);
    }
}
