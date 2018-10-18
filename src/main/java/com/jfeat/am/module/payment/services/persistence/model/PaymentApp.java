package com.jfeat.am.module.payment.services.persistence.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
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
 * @since 2018-10-18
 */
@TableName("p_payment_app")
public class PaymentApp extends Model<PaymentApp> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 应用ID
     */
	@TableField("app_id")
	private String appId;
    /**
     * 应用秘钥
     */
	@TableField("app_code")
	private String appCode;
    /**
     * 应用名称
     */
	@TableField("app_name")
	private String appName;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Date createTime;
    /**
     * 状态：ENABLED,DISABLED
     */
	private String status;
    /**
     * 备注
     */
	private String note;


	public Long getId() {
		return id;
	}

	public PaymentApp setId(Long id) {
		this.id = id;
		return this;
	}

	public String getAppId() {
		return appId;
	}

	public PaymentApp setAppId(String appId) {
		this.appId = appId;
		return this;
	}

	public String getAppCode() {
		return appCode;
	}

	public PaymentApp setAppCode(String appCode) {
		this.appCode = appCode;
		return this;
	}

	public String getAppName() {
		return appName;
	}

	public PaymentApp setAppName(String appName) {
		this.appName = appName;
		return this;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public PaymentApp setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public String getStatus() {
		return status;
	}

	public PaymentApp setStatus(String status) {
		this.status = status;
		return this;
	}

	public String getNote() {
		return note;
	}

	public PaymentApp setNote(String note) {
		this.note = note;
		return this;
	}

	public static final String ID = "id";

	public static final String APP_ID = "app_id";

	public static final String APP_CODE = "app_code";

	public static final String APP_NAME = "app_name";

	public static final String CREATE_TIME = "create_time";

	public static final String STATUS = "status";

	public static final String NOTE = "note";

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "PaymentApp{" +
			"id=" + id +
			", appId=" + appId +
			", appCode=" + appCode +
			", appName=" + appName +
			", createTime=" + createTime +
			", status=" + status +
			", note=" + note +
			"}";
	}
}
