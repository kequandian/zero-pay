
SET FOREIGN_KEY_CHECKS=0;

DROP TABLE IF EXISTS `p_payment_app`;
CREATE TABLE `p_payment_app` (
	id BIGINT (20) NOT NULL AUTO_INCREMENT,
	app_id varchar(50) NOT NULL COMMENT '应用ID',
	app_code varchar(100) NOT NULL COMMENT '应用秘钥',
	app_name varchar(100) NOT NULL COMMENT '应用名称',
	create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	status varchar(50) NOT NULL DEFAULT 'ENABLED' COMMENT '状态：ENABLED,DISABLED',
	note varchar(250) DEFAULT NULL COMMENT '备注',
	PRIMARY KEY (id),
	UNIQUE(app_id)
) ENGINE = INNODB DEFAULT CHARSET = utf8;


DROP TABLE IF EXISTS `p_payment_bill`;
CREATE TABLE `p_payment_bill` (
	id BIGINT (20) NOT NULL AUTO_INCREMENT,
	bill_num varchar(100) COMMENT '账单编号',
	app_id varchar(50) NOT NULL COMMENT '应用ID',
	payment_type varchar(50) DEFAULT NULL COMMENT '支付类型，WECHAT，ALIPAY',
	out_order_num varchar(100) COMMENT '业务系统的订单号',
	total_fee decimal(10, 2) NOT NULL COMMENT '订单价钱',
	title varchar(250) DEFAULT NULL COMMENT '订单标题',
	detail varchar(250) DEFAULT NULL COMMENT '订单描述',
	tran_id varchar(250) DEFAULT NULL COMMENT '外部支付系统的交易号',
	create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	status varchar(50) NOT NULL DEFAULT 'PAY_PENDING' COMMENT '状态：PAY_PENDING,PAID,REFUNDED',
	notify_url varchar(250) DEFAULT NULL COMMENT '支付结果回调地址',
	notify_result int DEFAULT 0 COMMENT '通知结果，1 为业务层返回确认',
	notify_attempt_count int DEFAULT 0 COMMENT '支付结果通知次数',
	last_notify_time datetime DEFAULT NULL COMMENT '最后一次通知时间',
	customer_data varchar(250) DEFAULT NULL COMMENT '业务层传来的数据,notify 时原样返回',
	PRIMARY KEY (id),
	UNIQUE(app_id, out_order_num)
) ENGINE = INNODB DEFAULT CHARSET = utf8;
