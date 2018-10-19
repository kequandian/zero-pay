package com.jfeat.am.module.payment.services.domain.service;

import com.jfeat.am.module.payment.services.crud.service.CRUDPaymentBillService;

/**
 * Created by vincent on 2017/10/19.
 */
public interface PaymentBillService extends CRUDPaymentBillService{
    public void notifyPayResult(String appId, String orderNum, String tranId);
}