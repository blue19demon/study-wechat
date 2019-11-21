package com.starter.config.app;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
@ConfigurationProperties(prefix = "alipay")
public class AliAPIConfiguration {

	 /**
     * 支付网关
     */
    private String gateteWay;

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 私钥
     */
    private String privateKey;

    /**
     * 支付宝公钥
     */
    private String alipayPublicKey;

    /**
     * 签名模式
     */
    private String signType;

    /**
     * 授权网关
     */
    private String authGateWay;

}
