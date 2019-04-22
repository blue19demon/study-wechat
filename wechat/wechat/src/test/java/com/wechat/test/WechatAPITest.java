package com.wechat.test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
import com.wechat.config.WeChatConfig;
import com.wechat.utils.WeChatUtils;

/**
 * https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_1
 * @author Administrator
 *
 */
public class WechatAPITest {
	
	public static void main(String[] args) {
		getsignkey();
	}

	public static void downloadfundflow() {
		try {
			// 微信下载资金账单，商户可以通过该接口下载自2017年6月1日起 的历史资金流水账单。
			Map<String, String> param1 = new HashMap<String, String>();
			param1.put("mch_id", WeChatConfig.MCH_ID);// 商户号
			param1.put("nonce_str", WXPayUtil.generateNonceStr());// 随机字符串
			param1.put("bill_date", "20180824");// 下载对账单的日期，格式：20141110
			/**
			 * 账单的资金来源账户：
				Basic  基本账户
				Operation 运营账户
				Fees 手续费账户
			 */
			param1.put("account_type", "Basic");
			param1.put("tar_type", "GZIP");// 压缩账单,非必传参数，固定值：GZIP，返回格式为.gzip的压缩包账单。不传则默认为数据流形式
			param1.put("sign", WXPayUtil.generateSignature(param1,WeChatConfig.KEY,
					WXPayConstants.SignType.MD5));// 沙盒测试貌似只支持MD5加密
			
			String xml = "";
			try {
				xml = WXPayUtil.mapToXml(param1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String obj = doPOSTJson(WeChatConfig.DOWNLOAD_FUNDFLOW_URL, xml);
			String fileName="downloadfundflow_"+param1.get("bill_date");
			logResult(obj,fileName);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void downloadbill() {
		try {
			// 微信下载对账单
			Map<String, String> param1 = new HashMap<String, String>();
			param1.put("mch_id", WeChatConfig.MCH_ID);// 商户号
			param1.put("nonce_str", WXPayUtil.generateNonceStr());// 随机字符串
			param1.put("bill_date", "20180824");// 下载对账单的日期，格式：20141110
			/**
			 * ALL，返回当日所有订单信息，默认值

			SUCCESS，返回当日成功支付的订单
			
			REFUND，返回当日退款订单
			
			RECHARGE_REFUND，返回当日充值退款订单
			 */
			param1.put("bill_type", "ALL");
			param1.put("tar_type", "GZIP");// 压缩账单,非必传参数，固定值：GZIP，返回格式为.gzip的压缩包账单。不传则默认为数据流形式
			param1.put("sign", WXPayUtil.generateSignature(param1,WeChatConfig.KEY,
					WXPayConstants.SignType.MD5));// 沙盒测试貌似只支持MD5加密
			
			String xml = "";
			try {
				xml = WXPayUtil.mapToXml(param1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String obj = doPOSTJson(WeChatConfig.DOWNLOAD_BILL_URL, xml);
			String fileName="downloadbill_"+param1.get("bill_date");
			logResult(obj,fileName);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void logResult(String sWord,String fileName) {
		String log_path = "D:\\";
		FileWriter writer = null;
		try {
			writer = new FileWriter(log_path + fileName + ".txt");
			writer.write(sWord);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void refundquery() {
		try {
			// 微信查询退款
			Map<String, String> param1 = new HashMap<String, String>();
			param1.put("mch_id", WeChatConfig.MCH_ID);// 商户号
			param1.put("nonce_str", WXPayUtil.generateNonceStr());// 随机字符串
			param1.put("out_refund_no", "1535163255343");// 商户系统内部的退款单号，四选一
			param1.put("out_trade_no", "1535163403513");// 商户订单号，四选一
			param1.put("transaction_id", "4704663554020180825101041114954");// 微信订单号，四选一
			param1.put("refund_id", "4214278378220180825110451696");// 微信生成的退款单号，在申请退款接口返回，四选一
			param1.put("sign", WXPayUtil.generateSignature(param1,WeChatConfig.KEY,
					WXPayConstants.SignType.MD5));// 沙盒测试貌似只支持MD5加密
			
			String xml = "";
			try {
				xml = WXPayUtil.mapToXml(param1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String obj = doPOSTJson(WeChatConfig.REFUND_QUERY_URL, xml);
			System.out.println(obj);
			Map<String, String> res = WXPayUtil.xmlToMap(obj);
			if(WeChatUtils.isTenpaySign(res, WeChatConfig.KEY)) {
				System.out.println("验证签名通过！");
				/**
				 * {
						"nonce_str": "yfVOGuVpINXCf4sWcNooV69MnAD033YL",
						"device_info": "sandbox",
						"appid": "wxf5b5e87a6a0fde94",
						"sign": "9FF4AF46994E9A5316811EDE7A4A8036",
						"err_code": "ORDERNOTEXIST",
						"err_code_des": "ORDERNOTEXIST",
						"return_msg": "OK",
						"result_code": "FAIL",
						"mch_id": "1512382871",
						"return_code": "SUCCESS"
					}
				 */
				System.out.println(JSONObject.toJSONString(res));
			}else {
				System.out.println("验证签名失败！");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void refund() {
		try {
			// 微信申请退款
			Map<String, String> param1 = new HashMap<String, String>();
			param1.put("mch_id", WeChatConfig.MCH_ID);// 商户号
			param1.put("nonce_str", WXPayUtil.generateNonceStr());// 随机字符串
			param1.put("out_refund_no", "1535163255343");// 商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
			param1.put("out_trade_no", "1535163403513");// 商户订单号
			param1.put("refund_fee", "551");// 退款金额，沙箱环境只能写这个金额
			param1.put("total_fee", "552");// 订单金额，沙箱环境只能写这个金额
			param1.put("transaction_id", "4704663554020180825101041114954");// 微信订单号
			param1.put("notify_url", WeChatConfig.REFUND_NOTIFY_URL);// 退款结果通知url,可选,异步接收微信支付退款结果通知的回调地址，通知URL必须为外网可访问的url，不允许带参数
            //如果参数中传了notify_url，则商户平台上配置的回调地址将不会生效。
			param1.put("sign", WXPayUtil.generateSignature(param1,WeChatConfig.KEY,
					WXPayConstants.SignType.MD5));// 沙盒测试貌似只支持MD5加密
			
			String xml = "";
			try {
				xml = WXPayUtil.mapToXml(param1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String obj = doPOSTJson(WeChatConfig.REFUND_URL, xml);
			System.out.println(obj);
			Map<String, String> res = WXPayUtil.xmlToMap(obj);
			if(WeChatUtils.isTenpaySign(res, WeChatConfig.KEY)) {
				System.out.println("验证签名通过！");
				/**
				 * {
						"coupon_refund_id_0": "12345",
						"nonce_str": "X5Z9cwxcEXp1zMAMjrZLm1YlJVTsnc4b",
						"coupon_refund_fee_0": "1",
						"sign": "EB1496BF0E3E2ABABEFFFD8B61B4F90E",
						"err_code": "SUCCESS",
						"return_msg": "OK",
						"fee_type": "CNY",
						"mch_id": "1512382871",
						"cash_fee": "551",
						"cash_fee_type": "CNY",
						"total_fee": "552",
						"cash_refund_fee": "551",
						"transaction_id": "4704663554020180825101041114954",
						"coupon_type_0": "NO_CASH",
						"bank_type": "CMC",
						"out_refund_no": "1535163255343",
						"openid": "wxd930ea5d5a258f4f",
						"settlement_refund_fee": "551",
						"refund_id": "4214278378220180825110451696",
						"cash_refund_fee_type": "CNY",
						"coupon_refund_fee": "1",
						"device_info": "sandbox",
						"out_trade_no": "1535163403513",
						"err_msg": "SUCCESS",
						"refund_fee_type": "CNY",
						"refund_fee": "551",
						"appid": "wxf5b5e87a6a0fde94",
						"settlement_total_fee": "551",
						"trade_type": "APP",
						"coupon_refund_count": "1",
						"result_code": "SUCCESS",
						"return_code": "SUCCESS"
					}
				 */
				System.out.println(JSONObject.toJSONString(res));
			}else {
				System.out.println("验证签名失败！");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void closeorder() {
		try {
			// 微信关闭订单
			Map<String, String> param1 = new HashMap<String, String>();
			param1.put("mch_id", WeChatConfig.MCH_ID);// 商户号
			param1.put("nonce_str", WXPayUtil.generateNonceStr());// 随机字符串
			param1.put("out_trade_no", "1535163403513");// 商户订单号,商户订单号
			param1.put("sign", WXPayUtil.generateSignature(param1,WeChatConfig.KEY,
					WXPayConstants.SignType.MD5));// 沙盒测试貌似只支持MD5加密
			
			String xml = "";
			try {
				xml = WXPayUtil.mapToXml(param1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String obj = doPOSTJson(WeChatConfig.CLOSE_ORDER_URL, xml);
			Map<String, String> res = WXPayUtil.xmlToMap(obj);
			if(WeChatUtils.isTenpaySign(res, WeChatConfig.KEY)) {
				System.out.println("验证签名通过！");
				/**
				 * {
						"nonce_str": "wEcnhAOV75sLC7vz1MeHox63pqsoX9V0",
						"device_info": "sandbox",
						"appid": "wxf5b5e87a6a0fde94",
						"sign": "17FFDAE7FD22BCCCD47DD506FD389AA9",
						"err_code": "SUCCESS",
						"return_msg": "OK",
						"err_code_des": "SUCCESS",
						"result_code": "SUCCESS",
						"mch_id": "1512382871",
						"return_code": "SUCCESS"
					}
				 */
				System.out.println(JSONObject.toJSONString(res));
			}else {
				System.out.println("验证签名失败！");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void orderquery() {
		try {
			// 查询订单
			Map<String, String> param1 = new HashMap<String, String>();
			param1.put("mch_id", WeChatConfig.MCH_ID);// 商户号
			param1.put("nonce_str", WXPayUtil.generateNonceStr());// 随机字符串
			param1.put("transaction_id", "4205311962120180825101309928384");//微信的订单号，建议优先使用
			param1.put("out_trade_no", "1535163403513");// 商户订单号,商户订单号
			param1.put("sign", WXPayUtil.generateSignature(param1,WeChatConfig.KEY,
					WXPayConstants.SignType.MD5));// 沙盒测试貌似只支持MD5加密
			String xml = "";
			try {
				xml = WXPayUtil.mapToXml(param1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String obj = doPOSTJson(WeChatConfig.ORDER_QUERY_URL, xml);
			Map<String, String> res = WXPayUtil.xmlToMap(obj);
			if(WeChatUtils.isTenpaySign(res, WeChatConfig.KEY)) {
				System.out.println("验证签名通过！");
				/**
				 * {
						"nonce_str": "CySNY1ZoI7bgicpHNxm6MIEr7luvd6LT",
						"appid": "wxf5b5e87a6a0fde94",
						"sign": "029F1D3277110F151D88BC75BBFB7BC5",
						"err_code": "ORDERNOTEXIST",
						"err_code_des": "订单不存在",
						"return_msg": "OK",
						"result_code": "FAIL",
						"sub_mch_id": "",
						"mch_id": "1512382871",
						"return_code": "SUCCESS"
					}
				 */
				System.out.println(JSONObject.toJSONString(res));
			}else {
				System.out.println("验证签名失败！");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void getsignkey() {
		try {
			// 获取sandbox_signkey
			Map<String, String> param1 = new HashMap<String, String>();
			param1.put("mch_id", WeChatConfig.MCH_ID);// 商户号
			param1.put("nonce_str", WXPayUtil.generateNonceStr());// 随机字符串
			param1.put("sign", WXPayUtil.generateSignature(param1, "Qixingbaoduoduozhuanqian85910855",
					WXPayConstants.SignType.MD5));// 沙盒测试貌似只支持MD5加密，申请的时候使用真实API秘钥
			String xml = "";
			try {
				xml = WXPayUtil.mapToXml(param1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String obj = doPOSTJson(WeChatConfig.GET_SIGNKEY_URL, xml);
			Map<String, String> res = WXPayUtil.xmlToMap(obj);
			System.out.println(JSONObject.toJSONString(res));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String doPOSTJson(String url, String data) throws IOException {
		String result = null;

		HttpPost httpPost = new HttpPost(url);
		CloseableHttpClient client = HttpClients.createDefault();

		StringEntity entity = new StringEntity(data, "utf-8");// 解决中文乱码问题
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		HttpResponse resp = client.execute(httpPost);
		if (resp.getStatusLine().getStatusCode() == 200) {
			HttpEntity httpEntity = resp.getEntity();
			result = EntityUtils.toString(httpEntity, "UTF-8");
		}
		return result;
	}
}
