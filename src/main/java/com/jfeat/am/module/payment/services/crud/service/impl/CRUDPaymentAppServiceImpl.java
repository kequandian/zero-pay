package com.jfeat.am.module.payment.services.crud.service.impl;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jfeat.am.common.crud.impl.CRUDServiceOnlyImpl;
import com.jfeat.am.module.payment.services.crud.service.CRUDPaymentAppService;
import com.jfeat.am.module.payment.services.persistence.dao.PaymentAppMapper;
import com.jfeat.am.module.payment.services.persistence.model.PaymentApp;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * implementation
 * </p>
 * CRUDPaymentAppService
 *
 * @author Code Generator
 * @since 2018-10-18
 */

@Service
public class CRUDPaymentAppServiceImpl extends CRUDServiceOnlyImpl<PaymentApp> implements CRUDPaymentAppService {


    @Resource
    protected PaymentAppMapper paymentAppMapper;

    @Override
    protected BaseMapper<PaymentApp> getMasterMapper() {
        return paymentAppMapper;
    }


}


