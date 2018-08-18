package com.wechat.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.wechat.bean.TemplateData;
import com.wechat.bean.WxTemplate;
import com.wechat.config.ReqURL;
import com.wechat.utils.AuthUtil;
/**
 * 发送模板消息
 * @author Administrator
 *
 */
@Controller
public class MessageController {
	private final static Logger logger = LoggerFactory.getLogger(MessageController.class);
	@Autowired
	private AuthUtil authUtil;
	/**
	 * @return
	 */
	@RequestMapping("sendTemplateMsg")
	@ResponseBody
	public String sendTemplateMsg() {
		JSONObject send_template_result = null;
		try {
			WxTemplate template = new WxTemplate();
			template.setUrl("www.baidu.com");
			template.setTouser("o0BJQ5uR7L6QjrwRek8sklsuOXpc");
			template.setTopcolor("#000000");
			template.setTemplate_id("O7n71NjSoc2dB6w4XOEfRl4aGVOxo-nhVu7w8oJhxnE");
			Map<String,TemplateData> m = new HashMap<String,TemplateData>();
			TemplateData first = new TemplateData();
			first.setColor("#000000");
			first.setValue("您好，您有一条待确认订单。");
			m.put("first", first);
			TemplateData keyword1 = new TemplateData();
			keyword1.setColor("#328392");
			keyword1.setValue("OD0001");
			m.put("keyword1", keyword1);
			TemplateData keyword2 = new TemplateData();
			keyword2.setColor("#328392");
			keyword2.setValue("预定订单");
			m.put("keyword2", keyword2);
			TemplateData keyword3 = new TemplateData();
			keyword3.setColor("#328392");
			keyword3.setValue("大龙虾");
			m.put("keyword3", keyword3);
			TemplateData remark = new TemplateData();
			remark.setColor("#929232");
			remark.setValue("请及时确认订单！");
			m.put("remark", remark);
			template.setData(m);
			String template_access_token = authUtil.doAccessToken();
			String send_template_msg_url = ReqURL.send_template_msg;
			send_template_msg_url = send_template_msg_url.replaceAll("ACCESS_TOKEN", template_access_token);
			String dataSend=JSONObject.toJSONString(template);
			/**
			 * {
						"data": {
							"keyword3": {
								"color": "#328392",
								"value": "大龙虾"
							},
							"keyword1": {
								"color": "#328392",
								"value": "OD0001"
							},
							"keyword2": {
								"color": "#328392",
								"value": "预定订单"
							},
							"remark": {
								"color": "#929232",
								"value": "请及时确认订单！"
							},
							"first": {
								"color": "#000000",
								"value": "您好，您有一条待确认订单。"
							}
						},
						"template_id": "O7n71NjSoc2dB6w4XOEfRl4aGVOxo-nhVu7w8oJhxnE",
						"topcolor": "#000000",
						"touser": "o0BJQ5uR7L6QjrwRek8sklsuOXpc",
						"url": "www.baidu.com"
					}
			 */
			logger.info(dataSend);
			send_template_result = authUtil.doPOSTJson(send_template_msg_url, dataSend);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return send_template_result.toJSONString();
	}
}
