package com.jfeat.base;

import com.jfeat.am.module.payment.constant.PaymentType;
import com.jfeat.am.module.payment.utils.PaymentKit;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jackyhuang
 * @date 2018/10/18
 */
public class SignTest {

    @Test
    public void createSign() {
        Map<String, String> params = new HashMap<>();
        params.put("appId", "demo");
        params.put("title", "标题");
        params.put("detail", "详情");
        params.put("totalFee", "2.02");
        params.put("orderNum", "1235");
        params.put("notifyUrl", "http://127.0.0.1:8080/api/pub/payment/order/notify");
        params.put("paymentType", PaymentType.WECHAT.toString());
        System.out.println(PaymentKit.createSign(params, "democode"));
    }
}
