package com.jfeat.am.module.payment.api.param;

import com.jfeat.am.module.payment.constant.PaymentType;
import com.jfeat.am.module.payment.constant.TradeType;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author jackyhuang
 * @date 2018/10/17
 */
public class OrderModel {
    private String appId;
    private String sign;
    private String paymentType;
    private String tradeType;

    private Boolean sandbox;

    @NotNull
    private BigDecimal totalFee;
    @NotNull
    private String title;
    @NotNull
    private String detail;

    private String orderNum;
    private String notifyUrl;
    private String customerData;
    private String returnUrl;

    public Boolean getSandbox() {
        return sandbox;
    }

    public OrderModel setSandbox(Boolean sandbox) {
        this.sandbox = sandbox;
        return this;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public OrderModel setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
        return this;
    }

    public String getTradeType() {
        return tradeType;
    }

    public OrderModel setTradeType(String tradeType) {
        this.tradeType = tradeType;
        return this;
    }

    public String getCustomerData() {
        return customerData;
    }

    public OrderModel setCustomerData(String customerData) {
        this.customerData = customerData;
        return this;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public OrderModel setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
        return this;
    }

    public String getSign() {
        return sign;
    }

    public OrderModel setSign(String sign) {
        this.sign = sign;
        return this;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public OrderModel setOrderNum(String orderNum) {
        this.orderNum = orderNum;
        return this;
    }

    public String getAppId() {
        return appId;
    }

    public OrderModel setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public OrderModel setPaymentType(String paymentType) {
        this.paymentType = paymentType;
        return this;
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public OrderModel setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public OrderModel setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDetail() {
        return detail;
    }

    public OrderModel setDetail(String detail) {
        this.detail = detail;
        return this;
    }
}
