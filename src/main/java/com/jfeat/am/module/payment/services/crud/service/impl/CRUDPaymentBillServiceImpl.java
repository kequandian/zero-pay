package com.jfeat.am.module.payment.services.crud.service.impl;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jfeat.am.common.crud.impl.CRUDServiceOnlyImpl;
import com.jfeat.am.module.payment.services.crud.service.CRUDPaymentBillService;
import com.jfeat.am.module.payment.services.persistence.dao.PaymentBillMapper;
import com.jfeat.am.module.payment.services.persistence.model.PaymentBill;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * implementation
 * </p>
 * CRUDPaymentBillService
 *
 * @author Code Generator
 * @since 2018-10-18
 */

@Service
public class CRUDPaymentBillServiceImpl extends CRUDServiceOnlyImpl<PaymentBill> implements CRUDPaymentBillService {


    @Resource
    protected PaymentBillMapper paymentBillMapper;

    @Override
    protected BaseMapper<PaymentBill> getMasterMapper() {
        return paymentBillMapper;
    }


}


