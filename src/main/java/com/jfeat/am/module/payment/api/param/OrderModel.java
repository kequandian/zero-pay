package com.jfeat.am.module.payment.api.param;

import javax.validation.constraints.NotNull;

/**
 * @author jackyhuang
 * @date 2018/10/17
 */
public class OrderModel {
    private String appId;
    private String sign;
    private String paymentType = "WECHAT";
    @NotNull
    private String totalFee;
    @NotNull
    private String title;
    @NotNull
    private String detail;
    @NotNull
    private String orderNum;

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

    public String getTotalFee() {
        return totalFee;
    }

    public OrderModel setTotalFee(String totalFee) {
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
