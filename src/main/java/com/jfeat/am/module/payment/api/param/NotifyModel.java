package com.jfeat.am.module.payment.api.param;

/**
 * @author jackyhuang
 * @date 2018/10/18
 */
public class NotifyModel {
    public String appId;
    public String customerData;
    public String orderNum;
    public String sign;

    public String getOrderNum() {
        return orderNum;
    }

    public NotifyModel setOrderNum(String orderNum) {
        this.orderNum = orderNum;
        return this;
    }

    public String getAppId() {
        return appId;
    }

    public NotifyModel setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getSign() {
        return sign;
    }

    public NotifyModel setSign(String sign) {
        this.sign = sign;
        return this;
    }

    public String getCustomerData() {
        return customerData;
    }

    public NotifyModel setCustomerData(String customerData) {
        this.customerData = customerData;
        return this;
    }
}
