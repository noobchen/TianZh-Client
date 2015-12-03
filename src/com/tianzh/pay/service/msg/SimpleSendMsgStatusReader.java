package com.tianzh.pay.service.msg;

import android.content.Context;
import com.tianzh.pay.utils.PaidWaysReportUtils;
import com.tianzh.pay.utils.PayUtils;

/**
 * Created by pig on 2015-11-12.
 */
public class SimpleSendMsgStatusReader extends SendMsgStatusReader {
    private String productId;
    private String userOrderId;
    private String tianzhOrderId;
    private boolean sucessCallBack = false;
    private boolean failCallBack = false;
    private int paidWayId;

    public SimpleSendMsgStatusReader(Context context, int paidWayId, String productId, String userOrderId, String tianzhOrderId, boolean sucessCallBack, boolean failCallBack) {
        super(context);
        this.productId = productId;
        this.userOrderId = userOrderId;
        this.tianzhOrderId = tianzhOrderId;
        this.sucessCallBack = sucessCallBack;
        this.failCallBack = failCallBack;
        this.paidWayId = paidWayId;
    }

    @Override
    public void sendMsgSucess() {
        if (sucessCallBack) {
            PayUtils.sucessCallBack(context, tianzhOrderId, userOrderId, productId);
        }

        PaidWaysReportUtils.sucessReport(paidWayId, tianzhOrderId);
    }

    @Override
    public void sendMsgFail() {
        if (failCallBack) {
            PayUtils.failCallBack(context, tianzhOrderId, userOrderId, productId, "支付短信发送失败", "30002");
        }

        PaidWaysReportUtils.failReport(paidWayId, tianzhOrderId,300);
    }
}
