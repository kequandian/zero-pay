package com.jfeat.am.module.payment.constant;

/**
 * @author jackyhuang
 * @date 2018/10/18
 */
public enum BillNotifyResult {
    NOT_NOTIFIED(0),
    NOTIFIED(1);

    private int value;
    BillNotifyResult(int val) {
        this.value = val;
    }

    public int getValue() {
        return this.value;
    }
}
