package com.jfeat.am.module.payment.services.domain.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jfeat.am.core.support.StrKit;
import com.jfeat.am.core.util.JsonKit;
import com.jfeat.am.modular.system.service.TenantService;
import com.jfeat.am.modular.wechat.notification.MessageNotification;
import com.jfeat.am.module.config.PaymentProperties;
import com.jfeat.am.module.payment.constant.AppStatus;
import com.jfeat.am.module.payment.constant.BillNotifyResult;
import com.jfeat.am.module.payment.constant.BillStatus;
import com.jfeat.am.module.payment.services.domain.dao.QueryPaymentAppDao;
import com.jfeat.am.module.payment.services.domain.dao.QueryPaymentBillDao;
import com.jfeat.am.module.payment.services.domain.service.PaymentBillService;

import com.jfeat.am.module.payment.services.crud.service.impl.CRUDPaymentBillServiceImpl;
import com.jfeat.am.module.payment.services.persistence.model.PaymentApp;
import com.jfeat.am.module.payment.services.persistence.model.PaymentBill;
import com.jfeat.am.module.payment.utils.PaymentKit;
import com.jfeat.http.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author admin
 * @since 2017-10-16
 */
@Service("paymentBillService")
public class PaymentBillServiceImpl extends CRUDPaymentBillServiceImpl implements PaymentBillService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentBillService.class);

    @Resource
    QueryPaymentBillDao queryPaymentBillDao;
    @Resource
    PaymentBillService paymentBillService;
    @Resource
    QueryPaymentAppDao queryPaymentAppDao;
    @Resource
    MessageNotification messageNotification;
    @Resource
    PaymentProperties paymentProperties;
    @Resource
    TenantService tenantService;


    @Override
    public void notifyPayResult(String appId, String orderNum, String tranId) {
        PaymentApp paymentApp = queryPaymentAppDao.findByAppId(appId);
        if (paymentApp == null || paymentApp.getStatus().equals(AppStatus.DISABLED.toString())) {
            return;
        }
        PaymentBill bill = queryPaymentBillDao.selectOne(appId, orderNum);
        if (bill == null) {
            logger.debug("bill not found. appId={},orderNum={}", appId, orderNum);
            return;
        }
        if (!bill.getStatus().equals(BillStatus.PAY_PENDING.toString())) {
            logger.debug("bill is not PAY_PENDING. appId={},orderNum={},status={}", appId, orderNum,bill.getStatus());
            return;
        }
        if (StrKit.isBlank(bill.getNotifyUrl())) {
            logger.debug("not notifyUrl defined, appId={},orderNum={}", appId, orderNum);
            return;
        }
        if (bill.getNotifyResult() == BillNotifyResult.NOTIFIED.getValue()) {
            logger.debug("already notified, appId={},orderNum={}", appId, orderNum);
            return;
        }

        bill.setStatus(BillStatus.PAID.toString());
        bill.setTranId(tranId);
        paymentBillService.updateMaster(bill);

        try {
            Map<String, String> params = new HashMap<>();
            params.put("appId", bill.getAppId());
            params.put("orderNum", bill.getOutOrderNum());
            params.put("customerData", bill.getCustomerData());
            params.put("sign", PaymentKit.createSign(params, paymentApp.getAppCode()));
            logger.debug("notify params: {}", params);
            String result = HttpUtils.post(bill.getNotifyUrl(), JsonKit.toJson(params));
            logger.debug("notify result = {}", result);

            JSONObject jsonObject = JSONObject.parseObject(result);
            Integer code = jsonObject.getInteger("code");
            if (code == 200) {
                bill.setNotifyResult(BillNotifyResult.NOTIFIED.getValue());
                paymentBillService.updateMaster(bill);
            }
        }
        catch (Exception ex) {
            logger.error("error occurred while notifying. error = " + ex.getMessage());
        }
    }

    @Override
    public void notifySuperVisor(String orderNum, String tranId) {
        String openid = paymentProperties.getNotifyOpenid();
        if (StrKit.notBlank(openid)) {
            messageNotification.setTitle("微信支付款项进账通知")
                    .setContent("账单号:" + orderNum + ", 交易号：" + tranId)
                    .setOpenid(openid)
                    .setTenantId(tenantService.getDefaultTenant().getId())
                    .send();
        }
    }

}
