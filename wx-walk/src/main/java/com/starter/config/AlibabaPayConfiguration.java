
package com.starter.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConstants;
import com.alipay.api.DefaultAlipayClient;
import com.starter.config.app.AliAPIConfiguration;

/**
 * 支付宝客户端自动配置
 */

@Configuration
public class AlibabaPayConfiguration {

	@Autowired
	private AliAPIConfiguration aliPayConfiguration;
	/**
	 * 构造支付宝客户端
	 *
	 * @param properties 支付宝配置
	 * @return 支付宝客户端
	 */
	@Bean
	public AlipayClient alipayClient() {
		String gatewayUrl = aliPayConfiguration.getGateteWay();
		String appId =aliPayConfiguration.getAppId();
		String privateKey = aliPayConfiguration.getPrivateKey();
		String format = AlipayConstants.FORMAT_JSON;
		String charset = AlipayConstants.CHARSET_UTF8;
		String alipayPublicKey =aliPayConfiguration.getAlipayPublicKey();
		String signType = aliPayConfiguration.getSignType();
		return new DefaultAlipayClient(gatewayUrl, appId, privateKey, format, charset, alipayPublicKey, signType);
	}
}
