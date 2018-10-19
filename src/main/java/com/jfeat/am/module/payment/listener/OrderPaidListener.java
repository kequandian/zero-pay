package com.jfeat.am.module.payment.listener;

import com.jfeat.am.modular.wechat.event.PaidBean;
import com.jfeat.am.modular.wechat.event.PaidEvent;
import com.jfeat.am.module.payment.services.domain.service.PaymentBillService;
import com.jfeat.module.event.BasicEvent;
import com.jfeat.module.event.BasicEventListener;
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

    // 添加 Lazy注解 防止H2数据库环境下的循环依赖
    @Resource
    @Lazy
    PaymentBillService paymentBillService;

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> aClass) {
        return aClass == PaidEvent.class;
    }

    @Override
    protected void onBasicEvent(BasicEvent<PaidBean> basicEvent) {
        // do your business
        PaidBean paidBean = basicEvent.getTarget();
        String[] strings = paidBean.getOrderNumber().split("_");
        paymentBillService.notifyPayResult(strings[0], strings[1], paidBean.getTransactionId());
    }
}

