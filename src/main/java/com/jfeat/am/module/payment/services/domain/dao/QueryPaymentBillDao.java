package com.jfeat.am.module.payment.services.domain.dao;

import com.jfeat.am.module.payment.services.domain.model.PaymentBillRecord;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jfeat.am.module.payment.services.persistence.model.PaymentBill;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * Created by Code Generator on 2018-10-18
 */
public interface QueryPaymentBillDao extends BaseMapper<PaymentBillRecord> {
    List<PaymentBillRecord> findPaymentBillPage(Page<PaymentBillRecord> page, @Param("record") PaymentBillRecord record, @Param("search") String search, @Param("orderBy") String orderBy);
    PaymentBill selectOne(@Param("appId") String appId, @Param("orderNum") String orderNum);
}