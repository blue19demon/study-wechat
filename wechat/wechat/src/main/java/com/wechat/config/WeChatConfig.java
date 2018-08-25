package com.wechat.config;

public class WeChatConfig {
	 /**公众号AppId*/
    public static final String APP_ID = "wx4492fde581248a1d";    
 
    /**公众号AppSecret*/
    public static final String APP_SECRET = "a5a91cdbc9b737b028e8bd95d7ed65c0";
 
    /**微信支付商户号*/
    public static final String MCH_ID = "1512382871";
 
    /**微信支付API秘钥*/
    public static final String KEY = "bbfe124b99377409d104994f91e56bb1";//"Qixingbaoduoduozhuanqian85910855";
 
    /**微信支付api证书路径*/
    public static final String CERT_PATH = "***/apiclient_cert.p12";
 
    /**微信沙箱环境申请url*/
    public static final String GET_SIGNKEY_URL = "https://api.mch.weixin.qq.com/sandboxnew/pay/getsignkey";
    
    /**微信统一下单url*/
    public static final String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/sandboxnew/pay/unifiedorder";
    
    /**微信查询订单url*/
    public static final String ORDER_QUERY_URL = "https://api.mch.weixin.qq.com/sandboxnew/pay/orderquery";

    /**微信关闭订单url*/
    public static final String CLOSE_ORDER_URL = "https://api.mch.weixin.qq.com/sandboxnew/pay/closeorder";

    /**微信申请退款url*/
    public static final String REFUND_URL = "https://api.mch.weixin.qq.com/sandboxnew/pay/refund";

    /**微信查询退款url*/
    public static final String REFUND_QUERY_URL = "https://api.mch.weixin.qq.com/sandboxnew/pay/refundquery";

    /**微信下载对账单url*/
    public static final String DOWNLOAD_BILL_URL = "https://api.mch.weixin.qq.com/sandboxnew/pay/downloadbill";
 
    /**微信下载资金账单url*/
    public static final String DOWNLOAD_FUNDFLOW_URL = "https://api.mch.weixin.qq.com/sandboxnew/pay/downloadfundflow";
 
    /**微信退款通知url*/
    public static final String REFUND_NOTIFY_URL = "http://q5krec.natappfree.cc/getWeChatRefundReturn";
 
    /**微信支付通知url*/
    public static final String NOTIFY_URL = "http://q5krec.natappfree.cc/getWeChatPayReturn";
 
    /**微信交易类型:公众号支付*/
    public static final String TRADE_TYPE_JSAPI = "JSAPI";
 
    /**微信交易类型:原生扫码支付*/
    public static final String TRADE_TYPE_NATIVE = "NATIVE";
 
    /**微信甲乙类型:APP支付*/
    public static final String TRADE_TYPE_APP = "APP";
}
