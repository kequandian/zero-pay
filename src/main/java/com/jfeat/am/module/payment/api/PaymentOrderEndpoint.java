package com.jfeat.am.module.payment.api;

import com.jfeat.am.common.constant.tips.SuccessTip;
import com.jfeat.am.common.constant.tips.Tip;
import com.jfeat.am.core.support.StrKit;
import com.jfeat.am.module.config.PaymentProperties;
import com.jfeat.am.module.payment.api.param.OrderModel;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jackyhuang
 * @date 2018/10/17
 */
@RestController
@RequestMapping("/api/pub/payment/order")
public class PaymentOrderEndpoint {

    @Resource
    PaymentProperties paymentProperties;

    @GetMapping
    public Tip queryOrder(@RequestParam String appId,
                          @RequestParam String sign) {
        //TODO
        return null;
    }

    @PostMapping
    public Tip createOrder(@Valid @RequestBody OrderModel orderModel) {
        //TODO: 1. check with appId and sign
        //      2. check the payment type
        //      3. create an internal payment bill
        Map<String, String> result = new HashMap<>();
        StringBuilder queryString = new StringBuilder();
        queryString.append("title=").append(orderModel.getTitle())
                .append("&detail=").append(orderModel.getDetail())
                .append("&totalFee=").append(orderModel.getTotalFee())
                .append("&orderNum=").append(orderModel.getOrderNum());
        String url = buildUrl(paymentProperties.getWechatPayUrl(), queryString.toString());
        result.put("wpayUrl", url);
        return SuccessTip.create(result);
    }

    private String buildUrl(String url, String queryString) {
        if (StrKit.isBlank(queryString)) {
            return url;
        }
        return url + "?" + queryString;
    }
}
