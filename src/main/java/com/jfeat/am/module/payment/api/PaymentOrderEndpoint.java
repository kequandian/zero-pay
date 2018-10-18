package com.jfeat.am.module.payment.api;

import com.jfeat.am.common.constant.tips.ErrorTip;
import com.jfeat.am.common.constant.tips.SuccessTip;
import com.jfeat.am.common.constant.tips.Tip;
import com.jfeat.am.common.exception.BusinessCode;
import com.jfeat.am.common.exception.BusinessException;
import com.jfeat.am.common.persistence.model.WechatConfig;
import com.jfeat.am.core.support.StrKit;
import com.jfeat.am.modular.system.service.TenantService;
import com.jfeat.am.modular.wechat.service.WechatConfigService;
import com.jfeat.am.module.config.PaymentProperties;
import com.jfeat.am.module.payment.api.param.NotifyModel;
import com.jfeat.am.module.payment.api.param.OrderModel;
import com.jfeat.am.module.payment.constant.PaymentBizException;
import com.jfeat.am.module.payment.services.domain.dao.QueryPaymentAppDao;
import com.jfeat.am.module.payment.services.domain.service.PaymentAppService;
import com.jfeat.am.module.payment.services.domain.service.PaymentBillService;
import com.jfeat.am.module.payment.services.persistence.model.PaymentApp;
import com.jfeat.am.module.payment.services.persistence.model.PaymentBill;
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
        if ("demo".equals(notifyModel.getAppId())) {
            return SuccessTip.create();
        }
        throw PaymentBizException.INVALID_APP_ID.create();
    }

    @GetMapping
    public Tip queryOrder(@RequestParam String appId,
                          @RequestParam String orderNum,
                          @RequestParam String sign) {
        //TODO
        return null;
    }

    @PostMapping
    public Tip createOrder(@Valid @RequestBody OrderModel orderModel) {
        PaymentApp paymentApp = queryPaymentAppDao.findByAppId(orderModel.getAppId());
        if (paymentApp == null) {
            throw PaymentBizException.INVALID_APP_ID.create();
        }

        //TODO check sign


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
