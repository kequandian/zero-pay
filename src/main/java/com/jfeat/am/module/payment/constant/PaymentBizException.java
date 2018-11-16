package com.jfeat.am.module.payment.constant;

import com.jfeat.am.common.exception.BusinessException;

/**
 * @author jackyhuang
 * @date 2018/10/18
 */
public enum PaymentBizException {
    INVALID_APP_ID(4000, "非法APPID"),
    INVALID_SIGN(4001, "签名错误"),
    BILL_NOT_FOUND(4010, "账单不存在"),
    BILL_ALREADY_PAID(4011, "账单已支付"),
    INVALID_SANDBOX(4012, "该APPID不能使用沙箱");


    private int code;
    private String message;

    PaymentBizException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BusinessException create(){
        return new BusinessException(code,message);
    }
}