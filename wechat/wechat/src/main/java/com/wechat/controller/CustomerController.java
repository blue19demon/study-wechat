package com.wechat.controller;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wechat.bean.TextMessage;
import com.wechat.utils.MessageUtil;
import com.wechat.utils.TulingApiUtil;

/**
 * 机器人
 * @author Administrator
 *
 */
@Controller
public class CustomerController {
	@PostMapping("/ownerCheck")
	@ResponseBody
	public String ownerCheck(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			Map<String, String> map = MessageUtil.xmlToMap(request);
			String toUserName = map.get("ToUserName");
			String fromUserName = map.get("FromUserName");
			String msgType = map.get("MsgType");
			String content = map.get("Content");
			String message = null;
			if ("text".equals(msgType)) {
				TextMessage text = new TextMessage();
				text.setFromUserName(toUserName);
				text.setToUserName(fromUserName);
				text.setMsgType("text");

				//这里填写回复内容
				text.setContent(TulingApiUtil.getTulingResult(content));

				text.setCreateTime(new Date().getTime());
				message = MessageUtil.textMessageToXml(text);
			}
			return message;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
