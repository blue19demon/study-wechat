package com.wechat.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.wechat.bean.AuthResult;
import com.wechat.bean.TemplateData;
import com.wechat.bean.WxTemplate;
import com.wechat.config.WechatAPIURL;
import com.wechat.utils.AuthUtil;
import com.wechat.utils.RedisUtils;
/**
 * 发送模板消息
 * @author Administrator
 *
 */
@Controller
public class TemplateMsgController {
	private final static Logger logger = LoggerFactory.getLogger(TemplateMsgController.class);
	@Autowired
	private AuthUtil authUtil;
	/**
	 * @return
	 */
	@RequestMapping("sendTemplateMsg")
	@ResponseBody
	public Map<String,String> sendTemplateMsg() {
		JSONObject send_template_result = null;
		Map<String,String> res = new HashMap<String,String>();
		try {
			String openId="o0BJQ5uR7L6QjrwRek8sklsuOXpc";
			String user_info_json=RedisUtils.read("user_info_"+openId);
			if(user_info_json != null && !"".equals(user_info_json)) {
				logger.info("从缓冲获取user——info");
				AuthResult authResult=JSONObject.parseObject(user_info_json, AuthResult.class);
				WxTemplate template = new WxTemplate();
				template.setUrl("www.baidu.com");
				template.setTouser(openId);
				template.setTopcolor("#000000");
				template.setTemplate_id("Mq9SOLsEZqCalWt0ZEVDkfQ5GCwbZ7eoKh_8wD99Ico");
				Map<String,TemplateData> m = new HashMap<String,TemplateData>();
				TemplateData first = new TemplateData();
				first.setColor("#000000");
				first.setValue("您好，您有一条待确认订单。");
				m.put("first", first);
				
				TemplateData keyword1 = new TemplateData();
				keyword1.setColor("#328392");
				keyword1.setValue("支付宝订单");
				m.put("keyword1", keyword1);
				
				TemplateData keyword2 = new TemplateData();
				keyword2.setColor("#328392");
				keyword2.setValue("大龙虾");
				m.put("keyword2", keyword2);
				
				TemplateData keyword3 = new TemplateData();
				keyword3.setColor("#328392");
				keyword3.setValue(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
				m.put("keyword3", keyword3);
				
				TemplateData keyword4 = new TemplateData();
				keyword4.setColor("#328392");
				keyword4.setValue(authResult.getNickname());
				m.put("keyword4", keyword4);
				
				TemplateData remark = new TemplateData();
				remark.setColor("#929232");
				remark.setValue("请及时确认订单！");
				m.put("remark", remark);
				template.setData(m);
				String template_access_token = authUtil.doAccessToken();
				String send_template_msg_url = WechatAPIURL.send_template_msg;
				send_template_msg_url = send_template_msg_url.replaceAll("ACCESS_TOKEN", template_access_token);
				String dataSend=JSONObject.toJSONString(template);
				
				logger.info(dataSend);
				send_template_result = authUtil.doPOSTJson(send_template_msg_url, dataSend);
				
				if("ok".equals(send_template_result.getString("errmsg"))) {
					res.put("code", "1000");
					res.put("msg", "发送成功");
				}else {
					res.put("code", "1001");
					res.put("msg", send_template_result.getString("errmsg"));
				}
			}else {
				res.put("code", "1002");
				res.put("msg", "请先登录！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
}
