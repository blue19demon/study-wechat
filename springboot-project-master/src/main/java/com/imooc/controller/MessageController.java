package com.imooc.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.imooc.utils.TulingApiUtil;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage;
import me.chanjar.weixin.mp.builder.outxml.TextBuilder;
import me.chanjar.weixin.mp.util.xml.XStreamTransformer;

/**
 * @author Administrator
 *
 */
@Controller
@Slf4j
public class MessageController {
	@Autowired
	private WxMpService wxMpService;


	@RequestMapping("/access")
	@ResponseBody
	public String access(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			if (RequestMethod.GET.name().equalsIgnoreCase(request.getMethod())) {
				return doAccess(request);
			}
			String xmlMsg=inputStream2String(request);
			WxMpXmlMessage wxMpXmlMessage = XStreamTransformer.fromXml(WxMpXmlMessage.class, xmlMsg);
			log.info(new JSONObject(wxMpXmlMessage).toStringPretty());
			String message = null;
			String msgType=wxMpXmlMessage.getMsgType();
			String toUser = wxMpXmlMessage.getToUser();
			String fromUser =wxMpXmlMessage.getFromUser();
			String content =wxMpXmlMessage.getContent();
			if (WxConsts.XML_MSG_TEXT.equals(msgType)) {
				WxMpXmlOutTextMessage xml = new TextBuilder().build();
				xml.setFromUserName(toUser);
				xml.setToUserName(fromUser);
				String result= TulingApiUtil.getTulingResult(content);
				xml.setContent(StrUtil.isEmpty(result)?"对不起，你说的话真是太高深了……":result);
				message = xml.toXml();
			}
			if (WxConsts.XML_MSG_EVENT.equals(msgType)) {
				// 事件类型
				String eventType = wxMpXmlMessage.getEvent();
				if (WxConsts.EVT_SUBSCRIBE.equals(eventType)) {
					// 关注
					WxMpXmlOutTextMessage xml = new TextBuilder().build();
					xml.setFromUserName(toUser);
					xml.setToUserName(fromUser);
					xml.setContent("感谢您关注偶,这里会给您提供最新的资讯和公告！\n".concat("您还可以回复文字，体验相应服务"));
					message = xml.toXml();
					
				} else if (WxConsts.EVT_UNSUBSCRIBE.equals(eventType)) {
					// 取消关注,用户接受不到我们发送的消息了，可以在这里记录用户取消关注的日志信息
					WxMpXmlOutTextMessage xml = new TextBuilder().build();
					xml.setFromUserName(toUser);
					xml.setToUserName(fromUser);
					xml.setContent("感谢您关注偶,以后再见！");
					message = xml.toXml();
				}
			}
			log.info(message);
			return message;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param request
	 * @return
	 */
	private String inputStream2String(HttpServletRequest request) {
		try {
			InputStream in = request.getInputStream();
			  
			ByteArrayOutputStream bos = new ByteArrayOutputStream();  
			  
			//读取缓存  
			byte[] buffer = new byte[2048];  
			int length = 0;  
			while((length = in.read(buffer)) != -1) {  
			    bos.write(buffer, 0, length);//写入输出流  
			}  
			in.close();//读取完毕，关闭输入流  
			// 根据输出流创建字符串对象  
			return new String(bos.toByteArray(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;  
	}
	/**
	 * 接入验证
	 * 
	 * @param request
	 * @return
	 */
	private String doAccess(HttpServletRequest request) {
		// 微信加密签名
		String signature = request.getParameter("signature");
		// 时间戳
		String timestamp = request.getParameter("timestamp");
		// 随机数
		String nonce = request.getParameter("nonce");
		// 随机字符串
		String echostr = request.getParameter("echostr");
		if (wxMpService.checkSignature(timestamp, nonce, signature)) {
			return echostr;
		}
		return null;
	}
}
