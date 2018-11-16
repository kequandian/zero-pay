package com.jfeat.am.module.payment.listener;

import com.jfeat.am.modular.wechat.event.PaidBean;
import com.jfeat.am.modular.wechat.event.PaidEvent;
import com.jfeat.am.modular.wechat.service.WechatPushOrderService;
import com.jfeat.am.module.payment.services.domain.dao.QueryPaymentBillDao;
import com.jfeat.am.module.payment.services.domain.service.PaymentBillService;
import com.jfeat.am.module.payment.services.persistence.model.PaymentBill;
import com.jfeat.module.event.BasicEvent;
import com.jfeat.module.event.BasicEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author jackyhuang
 * @date 2018/10/17
 */
@Component
public class OrderPaidListener extends BasicEventListener<PaidBean> {

    private static final Logger logger = LoggerFactory.getLogger(OrderPaidListener.class);

    // 添加 Lazy注解 防止H2数据库环境下的循环依赖
    @Resource
    @Lazy
    PaymentBillService paymentBillService;
    @Resource
    @Lazy
    WechatPushOrderService wechatPushOrderService;
    @Resource
    @Lazy
    QueryPaymentBillDao queryPaymentBillDao;

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> aClass) {
        return aClass == PaidEvent.class;
    }

    @Override
    protected void onBasicEvent(BasicEvent<PaidBean> basicEvent) {
        // do your business
        PaidBean paidBean = basicEvent.getTarget();
        String[] strings = paidBean.getOrderNumber().split("_");
        if (strings.length != 2) {
            logger.debug("invalid order number. {}", paidBean.getOrderNumber());
            return;
        }
        String appId = strings[0];
        String orderNum = strings[1];
        paymentBillService.notifyPayResult(appId, orderNum, paidBean.getTransactionId());

        // 对于demo appid，直接发起退款
        if (appId.equalsIgnoreCase("demo")) {
            PaymentBill bill = queryPaymentBillDao.selectOne(appId, orderNum);
            boolean result = wechatPushOrderService.refund(paidBean.getOrderNumber(), paidBean.getOrderNumber(), bill.getTotalFee(), bill.getTotalFee());
            logger.debug("refund {} result = {}", paidBean.getOrderNumber(), result);
        }
    }
}

