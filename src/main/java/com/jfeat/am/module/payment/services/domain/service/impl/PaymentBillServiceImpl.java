package com.jfeat.am.module.payment.services.domain.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jfeat.am.core.support.StrKit;
import com.jfeat.am.core.util.JsonKit;
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

    @Override
    public void notifyPayResult(String appId, String orderNum) {
        PaymentApp paymentApp = queryPaymentAppDao.findByAppId(appId);
        if (paymentApp == null || paymentApp.getStatus().equals(AppStatus.DISABLED.toString())) {
            return;
        }
        PaymentBill bill = queryPaymentBillDao.selectOne(appId, orderNum);
        if (bill == null) {
            return;
        }
        if (!bill.getStatus().equals(BillStatus.PAID.toString())) {
            return;
        }
        if (StrKit.isBlank(bill.getNotifyUrl())) {
            return;
        }
        if (bill.getNotifyResult() == BillNotifyResult.NOTIFIED.getValue()) {
            return;
        }

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
}
