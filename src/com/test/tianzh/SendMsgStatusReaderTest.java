package com.test.tianzh;

import android.test.AndroidTestCase;
import android.util.Log;
import com.tianzh.pay.constant.Constant;
import com.tianzh.pay.service.msg.SendMsgStatusReader;
import com.tianzh.pay.utils.LogUtils;

/**
 * Created by pig on 2015-11-12.
 */
public class SendMsgStatusReaderTest extends AndroidTestCase {

    public void testSendMsgBr() {
        SendMsgStatusReader reader = new SendMsgStatusReader(getContext()) {
            @Override
            public void sendMsgSucess() {
                LogUtils.d("短信发送成功！");
            }

            @Override
            public void sendMsgFail() {
                LogUtils.e("短信发送失败！");
            }
        };

        reader.send("18123744403", "这是测试！！！");
    }


}
