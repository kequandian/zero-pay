package com.jfeat.am.module.payment.services.domain.dao;

import com.jfeat.am.module.payment.services.domain.model.PaymentAppRecord;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.jfeat.am.module.payment.services.persistence.model.PaymentApp;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * Created by Code Generator on 2018-10-18
 */
public interface QueryPaymentAppDao extends BaseMapper<PaymentAppRecord> {
    List<PaymentAppRecord> findPaymentAppPage(Page<PaymentAppRecord> page, @Param("record") PaymentAppRecord record, @Param("search") String search, @Param("orderBy") String orderBy);
    PaymentApp findByAppId(@Param("appId") String appId);
}