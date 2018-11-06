package com.jfeat.am.module.payment.services.persistence.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author Code Generator
 * @since 2018-11-06
 */
@TableName("p_payment_bill")
public class PaymentBill extends Model<PaymentBill> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 账单编号
     */
	@TableField("bill_num")
	private String billNum;
    /**
     * 应用ID
     */
	@TableField("app_id")
	private String appId;
    /**
     * 支付类型，WECHAT，ALIPAY
     */
	@TableField("payment_type")
	private String paymentType;
    /**
     * 业务系统的订单号
     */
	@TableField("out_order_num")
	private String outOrderNum;
    /**
     * 订单价钱
     */
	@TableField("total_fee")
	private BigDecimal totalFee;
    /**
     * 订单标题
     */
	private String title;
    /**
     * 订单描述
     */
	private String detail;
    /**
     * 外部支付系统的交易号
     */
	@TableField("tran_id")
	private String tranId;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Date createTime;
    /**
     * 状态：PAY_PENDING,PAID,REFUNDED
     */
	private String status;
    /**
     * 支付结果回调地址
     */
	@TableField("notify_url")
	private String notifyUrl;
    /**
     * 通知结果，1 为业务层返回确认
     */
	@TableField("notify_result")
	private Integer notifyResult;
    /**
     * 支付结果通知次数
     */
	@TableField("notify_attempt_count")
	private Integer notifyAttemptCount;
    /**
     * 最后一次通知时间
     */
	@TableField("last_notify_time")
	private Date lastNotifyTime;
    /**
     * 业务层传来的数据,notify 时原样返回
     */
	@TableField("customer_data")
	private String customerData;
    /**
     * 支付后返回业务层的URL
     */
	@TableField("return_url")
	private String returnUrl;


	public Long getId() {
		return id;
	}

	public PaymentBill setId(Long id) {
		this.id = id;
		return this;
	}

	public String getBillNum() {
		return billNum;
	}

	public PaymentBill setBillNum(String billNum) {
		this.billNum = billNum;
		return this;
	}

	public String getAppId() {
		return appId;
	}

	public PaymentBill setAppId(String appId) {
		this.appId = appId;
		return this;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public PaymentBill setPaymentType(String paymentType) {
		this.paymentType = paymentType;
		return this;
	}

	public String getOutOrderNum() {
		return outOrderNum;
	}

	public PaymentBill setOutOrderNum(String outOrderNum) {
		this.outOrderNum = outOrderNum;
		return this;
	}

	public BigDecimal getTotalFee() {
		return totalFee;
	}

	public PaymentBill setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
		return this;
	}

	public String getTitle() {
		return title;
	}

	public PaymentBill setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getDetail() {
		return detail;
	}

	public PaymentBill setDetail(String detail) {
		this.detail = detail;
		return this;
	}

	public String getTranId() {
		return tranId;
	}

	public PaymentBill setTranId(String tranId) {
		this.tranId = tranId;
		return this;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public PaymentBill setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public PaymentBill setStatus(String status) {
		this.status = status;
		return this;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public PaymentBill setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
		return this;
	}

	public Integer getNotifyResult() {
		return notifyResult;
	}

	public PaymentBill setNotifyResult(Integer notifyResult) {
		this.notifyResult = notifyResult;
		return this;
	}

	public Integer getNotifyAttemptCount() {
		return notifyAttemptCount;
	}

	public PaymentBill setNotifyAttemptCount(Integer notifyAttemptCount) {
		this.notifyAttemptCount = notifyAttemptCount;
		return this;
	}

	public Date getLastNotifyTime() {
		return lastNotifyTime;
	}

	public PaymentBill setLastNotifyTime(Date lastNotifyTime) {
		this.lastNotifyTime = lastNotifyTime;
		return this;
	}

	public String getCustomerData() {
		return customerData;
	}

	public PaymentBill setCustomerData(String customerData) {
		this.customerData = customerData;
		return this;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public PaymentBill setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
		return this;
	}

	public static final String ID = "id";

	public static final String BILL_NUM = "bill_num";

	public static final String APP_ID = "app_id";

	public static final String PAYMENT_TYPE = "payment_type";

	public static final String OUT_ORDER_NUM = "out_order_num";

	public static final String TOTAL_FEE = "total_fee";

	public static final String TITLE = "title";

	public static final String DETAIL = "detail";

	public static final String TRAN_ID = "tran_id";

	public static final String CREATE_TIME = "create_time";

	public static final String STATUS = "status";

	public static final String NOTIFY_URL = "notify_url";

	public static final String NOTIFY_RESULT = "notify_result";

	public static final String NOTIFY_ATTEMPT_COUNT = "notify_attempt_count";

	public static final String LAST_NOTIFY_TIME = "last_notify_time";

	public static final String CUSTOMER_DATA = "customer_data";

	public static final String RETURN_URL = "return_url";

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "PaymentBill{" +
			"id=" + id +
			", billNum=" + billNum +
			", appId=" + appId +
			", paymentType=" + paymentType +
			", outOrderNum=" + outOrderNum +
			", totalFee=" + totalFee +
			", title=" + title +
			", detail=" + detail +
			", tranId=" + tranId +
			", createTime=" + createTime +
			", status=" + status +
			", notifyUrl=" + notifyUrl +
			", notifyResult=" + notifyResult +
			", notifyAttemptCount=" + notifyAttemptCount +
			", lastNotifyTime=" + lastNotifyTime +
			", customerData=" + customerData +
			", returnUrl=" + returnUrl +
			"}";
	}
}
