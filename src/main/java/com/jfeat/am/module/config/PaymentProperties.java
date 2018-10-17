package com.jfeat.am.module.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author jackyhuang
 * @date 2018/10/17
 */
@Component
@ConfigurationProperties(prefix = PaymentProperties.PREFIX)
public class PaymentProperties {
    public static final String PREFIX = "payment";

    private String wechatPayUrl;

    public String getWechatPayUrl() {
        return wechatPayUrl;
    }

    public PaymentProperties setWechatPayUrl(String wechatPayUrl) {
        this.wechatPayUrl = wechatPayUrl;
        return this;
    }
}
