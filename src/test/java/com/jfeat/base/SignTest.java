package com.jfeat.base;

import com.jfeat.am.module.payment.constant.PaymentType;
import com.jfeat.am.module.payment.constant.TradeType;
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
        params.put("title", "标题x");
        params.put("detail", "详情");
        params.put("totalFee", "2.02");
        params.put("orderNum", "E02E901634D701E9A626E69E3393F5E4");
        params.put("notifyUrl", "http://127.0.0.1:8080/api/pub/payment/order/notify");
        params.put("paymentType", PaymentType.WECHAT.toString());
        params.put("tradeType", TradeType.NATIVE.toString());
        params.put("customerData", "customer data");
        System.out.println(PaymentKit.createSign(params, "democode"));
    }

    @Test
    public void createSign2() {
        Map<String, String> params = new HashMap<>();
        params.put("appId", "demo");
        //params.put("status", "PAID");
        System.out.println(PaymentKit.createSign(params, "democode"));
    }
}
