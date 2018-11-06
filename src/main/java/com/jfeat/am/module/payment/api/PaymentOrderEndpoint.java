package com.jfeat.am.module.payment.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.IdWorker;
import com.jfeat.am.common.constant.tips.SuccessTip;
import com.jfeat.am.common.constant.tips.Tip;
import com.jfeat.am.common.controller.BaseController;
import com.jfeat.am.common.persistence.model.WechatConfig;
import com.jfeat.am.core.support.StrKit;
import com.jfeat.am.core.util.JsonKit;
import com.jfeat.am.modular.system.service.TenantService;
import com.jfeat.am.modular.wechat.service.WechatConfigService;
import com.jfeat.am.modular.wechat.service.WechatPushOrderService;
import com.jfeat.am.module.config.PaymentProperties;
import com.jfeat.am.module.payment.api.param.NotifyModel;
import com.jfeat.am.module.payment.api.param.OrderModel;
import com.jfeat.am.module.payment.constant.PaymentBizException;
import com.jfeat.am.module.payment.constant.PaymentType;
import com.jfeat.am.module.payment.constant.TradeType;
import com.jfeat.am.module.payment.services.domain.dao.QueryPaymentAppDao;
import com.jfeat.am.module.payment.services.domain.dao.QueryPaymentBillDao;
import com.jfeat.am.module.payment.services.domain.model.PaymentBillRecord;
import com.jfeat.am.module.payment.services.domain.service.PaymentBillService;
import com.jfeat.am.module.payment.services.persistence.model.PaymentApp;
import com.jfeat.am.module.payment.services.persistence.model.PaymentBill;
import com.jfeat.am.module.payment.utils.PaymentKit;
import com.jfinal.weixin.sdk.kit.IpKit;
import io.swagger.annotations.ApiOperation;
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
public class PaymentOrderEndpoint extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(PaymentOrderEndpoint.class);

    @Resource
    WechatConfigService wechatConfigService;
    @Resource
    WechatPushOrderService wechatPushOrderService;

    @Resource
    TenantService tenantService;

    @Resource
    PaymentProperties paymentProperties;
    @Resource
    PaymentBillService paymentBillService;
    @Resource
    QueryPaymentBillDao queryPaymentBillDao;
    @Resource
    QueryPaymentAppDao queryPaymentAppDao;

    /**
     * 测试用，只有 demo appId的才可以通过
     *
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
    @ApiOperation(value = "查询账单数据", response = PaymentBillRecord.class)
    public Tip queryOrder(@RequestParam(name = "pageNum", required = false) Integer pageNum,
                          @RequestParam(name = "pageSize", required = false) Integer pageSize,
                          @RequestParam(name = "status", required = false) String status,
                          @RequestParam String appId,
                          @RequestParam String sign) {
        PaymentApp paymentApp = queryPaymentAppDao.findByAppId(appId);
        if (paymentApp == null) {
            throw PaymentBizException.INVALID_APP_ID.create();
        }

        Map<String, String> params = new HashMap<>();
        params.put("appId", appId);
        params.put("sign", sign);
        if (pageNum != null) {
            params.put("pageNum", pageNum.toString());
        }
        else {
            pageNum = 1;
        }
        if (pageSize != null) {
            params.put("pageSize", pageSize.toString());
        }
        else {
            pageSize = 50;
        }
        if (StrKit.notBlank(status)) {
            params.put("status", status);
        }
        if (!PaymentKit.verifySign(params, paymentApp.getAppCode())) {
            throw PaymentBizException.INVALID_SIGN.create();
        }

        Page<PaymentBillRecord> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);

        PaymentBillRecord record = new PaymentBillRecord();
        record.setAppId(appId);
        record.setStatus(status);

        page.setRecords(queryPaymentBillDao.findPaymentBillPage(page, record, null, null));

        return SuccessTip.create(page);
    }

    @PostMapping
    @ApiOperation(value = "下单")
    public Tip createOrder(@Valid @RequestBody OrderModel orderModel) throws UnsupportedEncodingException {
        PaymentApp paymentApp = queryPaymentAppDao.findByAppId(orderModel.getAppId());
        if (paymentApp == null) {
            throw PaymentBizException.INVALID_APP_ID.create();
        }

        Map<String, String> params = PaymentKit.convertToMap(orderModel);
        if (!PaymentKit.verifySign(params, paymentApp.getAppCode())) {
            throw PaymentBizException.INVALID_SIGN.create();
        }

        if (StrKit.isBlank(orderModel.getPaymentType())) {
            orderModel.setPaymentType(PaymentType.WECHAT.toString());
        }
        if (StrKit.isBlank(orderModel.getTradeType())) {
            orderModel.setTradeType(TradeType.WPA.toString());
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
        paymentBill.setReturnUrl(orderModel.getReturnUrl());
        paymentBillService.createMaster(paymentBill);
        paymentBill.setBillNum(IdWorker.getIdStr());
        if (StrKit.isBlank(orderModel.getOrderNum())) {
            logger.debug("business layer not provides ordernum. use payment gateway bill num.");
            paymentBill.setOutOrderNum(paymentBill.getBillNum());
        }
        paymentBillService.updateMaster(paymentBill);

        Map<String, String> result = new HashMap<>();
        result.put("billNum", paymentBill.getBillNum());

        if (TradeType.WPA.toString().equals(orderModel.getTradeType())) {
            handleWechat(result, paymentBill, paymentApp);
        }
        if (TradeType.NATIVE.toString().equals(orderModel.getTradeType())) {
            handleWechatNative(result, paymentBill, paymentApp);
        }

        return SuccessTip.create(result);
    }

    private void handleWechatNative(Map<String, String> result, PaymentBill paymentBill, PaymentApp paymentApp) {
        Map<String, String> pushOrderResult = wechatPushOrderService.pushOrder(paymentBill.getTitle(),
                paymentBill.getDetail(),
                paymentBill.getOutOrderNum(),
                PaymentKit.convertPrice(paymentBill.getTotalFee().doubleValue()),
                null,
                IpKit.getRealIp(getHttpServletRequest()),
                paymentProperties.getApiHost(),
                com.jfeat.am.modular.wechat.constant.TradeType.NATIVE_TYPE
        );
        result.put("wpayCode", pushOrderResult.get("codeUrl"));
    }

    private void handleWechat(Map<String, String> result, PaymentBill paymentBill, PaymentApp paymentApp) throws UnsupportedEncodingException {
        StringBuilder queryString = new StringBuilder();
        queryString.append("title=").append(PaymentKit.urlEncode(paymentBill.getTitle()))
                .append("&detail=").append(PaymentKit.urlEncode(paymentBill.getDetail()))
                .append("&totalFee=").append(paymentBill.getTotalFee())
                .append("&orderNum=").append(paymentBill.getAppId() + "_" + paymentBill.getOutOrderNum())
                .append("&appName=").append(paymentApp.getAppName())
                .append("&returnUrl=").append(paymentBill.getReturnUrl());

        WechatConfig wechatConfig = wechatConfigService.getByTenantId(tenantService.getDefaultTenant().getId());
        String url = buildUrl(wechatConfig.getHost() + "/payment/wpay", queryString.toString());
        result.put("wpayUrl", url);
    }

    private String buildUrl(String url, String queryString) {
        if (StrKit.isBlank(queryString)) {
            return url;
        }
        return url + "?" + queryString;
    }
}
