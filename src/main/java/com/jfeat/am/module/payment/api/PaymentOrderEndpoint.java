package com.jfeat.am.module.payment.api;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONObject;
import com.jfeat.am.common.constant.tips.SuccessTip;
import com.jfeat.am.common.constant.tips.Tip;
import com.jfeat.am.common.persistence.model.WechatConfig;
import com.jfeat.am.core.support.BeanKit;
import com.jfeat.am.core.support.StrKit;
import com.jfeat.am.core.util.JsonKit;
import com.jfeat.am.modular.system.service.TenantService;
import com.jfeat.am.modular.wechat.service.WechatConfigService;
import com.jfeat.am.module.payment.api.param.NotifyModel;
import com.jfeat.am.module.payment.api.param.OrderModel;
import com.jfeat.am.module.payment.constant.PaymentBizException;
import com.jfeat.am.module.payment.services.domain.dao.QueryPaymentAppDao;
import com.jfeat.am.module.payment.services.domain.service.PaymentAppService;
import com.jfeat.am.module.payment.services.domain.service.PaymentBillService;
import com.jfeat.am.module.payment.services.persistence.model.PaymentApp;
import com.jfeat.am.module.payment.services.persistence.model.PaymentBill;
import com.jfeat.am.module.payment.utils.PaymentKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jackyhuang
 * @date 2018/10/17
 */
@RestController
@RequestMapping("/api/pub/payment/order")
public class PaymentOrderEndpoint {

    private static final Logger logger = LoggerFactory.getLogger(PaymentOrderEndpoint.class);

    @Resource
    WechatConfigService wechatConfigService;
    @Resource
    TenantService tenantService;
    @Resource
    PaymentBillService paymentBillService;
    @Resource
    PaymentAppService paymentAppService;
    @Resource
    QueryPaymentAppDao queryPaymentAppDao;

    /**
     * 测试用，只有 demo appId的才可以通过
     * @param notifyModel
     * @return
     */
    @PostMapping("/notify")
    public Tip testNotify(@RequestBody NotifyModel notifyModel) {
        logger.debug("notify model: {}", JsonKit.toJson(notifyModel));
        PaymentApp paymentApp = queryPaymentAppDao.findByAppId(notifyModel.getAppId());
        if (paymentApp == null || !"demo".equals(paymentApp.getAppId())) {
            throw PaymentBizException.INVALID_APP_ID.create();
        }
        Map<String, String> params = PaymentKit.convertToMap(notifyModel);
        boolean verify = PaymentKit.verifySign(params, paymentApp.getAppCode());
        logger.debug("notify verify sign result = {}", verify);
        return SuccessTip.create();
    }

    @GetMapping
    public Tip queryOrder(@RequestParam String appId,
                          @RequestParam String orderNum,
                          @RequestParam String sign) {
        //TODO
        return null;
    }

    @PostMapping
    public Tip createOrder(@Valid @RequestBody OrderModel orderModel) throws UnsupportedEncodingException {
        PaymentApp paymentApp = queryPaymentAppDao.findByAppId(orderModel.getAppId());
        if (paymentApp == null) {
            throw PaymentBizException.INVALID_APP_ID.create();
        }

        Map<String, String> params = PaymentKit.convertToMap(orderModel);
        if (!PaymentKit.verifySign(params, paymentApp.getAppCode())) {
            throw PaymentBizException.INVALID_SIGN.create();
        }

        PaymentBill paymentBill = new PaymentBill();
        paymentBill.setAppId(paymentApp.getAppId());
        paymentBill.setOutOrderNum(orderModel.getOrderNum());
        paymentBill.setTitle(orderModel.getTitle());
        paymentBill.setDetail(orderModel.getDetail());
        paymentBill.setTotalFee(orderModel.getTotalFee());
        paymentBill.setPaymentType(orderModel.getPaymentType());
        paymentBill.setNotifyUrl(orderModel.getNotifyUrl());
        paymentBill.setCustomerData(orderModel.getCustomerData());
        paymentBillService.createMaster(paymentBill);

        Map<String, String> result = new HashMap<>();
        StringBuilder queryString = new StringBuilder();
        queryString.append("title=").append(orderModel.getTitle())
                .append("&detail=").append(orderModel.getDetail())
                .append("&totalFee=").append(orderModel.getTotalFee())
                .append("&orderNum=").append(paymentBill.getAppId() + "_" + orderModel.getOrderNum());

        WechatConfig wechatConfig = wechatConfigService.getByTenantId(tenantService.getDefaultTenant().getId());
        String url = buildUrl(wechatConfig.getHost() + "/payment/wpay", queryString.toString());
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
