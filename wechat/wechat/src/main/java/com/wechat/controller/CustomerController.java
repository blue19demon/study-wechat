package com.wechat.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.wechat.bean.ArticlesItem;
import com.wechat.bean.ArticlesMessage;
import com.wechat.bean.TextMessage;
import com.wechat.utils.MessageUtil;
import com.wechat.utils.TulingApiUtil;

/**
 * 机器人
 * 
 * @author Administrator
 *
 */
@Controller
public class CustomerController {
	private String imgMessage = "<xml><ToUserName>TOUSER</ToUserName><FromUserName>FROMUSER</FromUserName><CreateTime>CREATE_TIME</CreateTime><MsgType>image</MsgType><Image><MediaId>MEDIA_ID</MediaId></Image></xml>";

	@PostMapping("/ownerCheck")
	@ResponseBody
	public String ownerCheck(HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			Map<String, String> map = MessageUtil.xmlToMap(request);
			System.out.println("map=" + JSONObject.toJSONString(map));
			String toUserName = map.get("ToUserName");
			String fromUserName = map.get("FromUserName");
			String msgType = map.get("MsgType");
			String content = map.get("Content");
			String message = null;
			if ("text".equals(msgType)) {
				if ("2".equals(content)) {
					message = imgMessage.replace("TOUSER", fromUserName).replace("FROMUSER", toUserName)
							.replaceAll("CREATE_TIME", String.valueOf(new Date().getTime()))
							.replace("MEDIA_ID", "KDx0LTJLZYdlQKxdg5esSc6YXnYsawMtSCRAaHFro6-9ClnkdTUQIplNF_XUBA-7");
				}
				else if ("3".equals(content)) {
				     ArticlesMessage outputMsg = getBlogMessage(fromUserName, toUserName, new Date().getTime());
				     message = MessageUtil.messageToXml(outputMsg).replaceAll("com.wechat.bean.ArticlesItem", "item");
				}else {
					TextMessage text = new TextMessage();
					text.setFromUserName(toUserName);
					text.setToUserName(fromUserName);
					text.setMsgType("text");

					// 这里填写回复内容
					text.setContent(TulingApiUtil.getTulingResult(content));

					text.setCreateTime(new Date().getTime());
					message = MessageUtil.textMessageToXml(text);
				}

			}
			if (msgType.equals("event")) {
				// 事件类型
				String eventType = map.get("Event");
				if (eventType.equals("subscribe")) {
					// 关注
					StringBuffer contentMsg = new StringBuffer("感谢您关注偶,这里会给您提供最新的资讯和公告！\n");
					contentMsg.append("您还可以回复下列数字，体验相应服务").append("\n\n");
					contentMsg.append("1  我是文办").append("\n");
					contentMsg.append("2  我是图片").append("\n");
					contentMsg.append("3  我是多图文").append("\n");

					TextMessage text = new TextMessage();
					text.setFromUserName(toUserName);
					text.setToUserName(fromUserName);
					text.setMsgType("text");

					// 这里填写回复内容
					text.setContent(contentMsg.toString());

					text.setCreateTime(new Date().getTime());
					message = MessageUtil.textMessageToXml(text);

				} else if (eventType.equals("unsubscribe")) {
					// 取消关注,用户接受不到我们发送的消息了，可以在这里记录用户取消关注的日志信息
					message = "感谢您关注偶,以后再见！\n";
					TextMessage text = new TextMessage();
					text.setFromUserName(toUserName);
					text.setToUserName(fromUserName);
					text.setMsgType("text");

					// 这里填写回复内容
					text.setContent(message);

					text.setCreateTime(new Date().getTime());
					message = MessageUtil.textMessageToXml(text);
				} else if (eventType.equals("pic_weixin")) {
					// 事件KEY值，与创建自定义菜单时指定的KEY值对应
					String eventKey = map.get("EventKey");
					// 自定义菜单点击事件
					if (eventKey.equals("rselfmenu_1_2")) {
						message = "微信相册发图菜单项被点击！";
						TextMessage text = new TextMessage();
						text.setFromUserName(toUserName);
						text.setToUserName(fromUserName);
						text.setMsgType("text");

						// 这里填写回复内容
						text.setContent(message);

						text.setCreateTime(new Date().getTime());
						message = MessageUtil.textMessageToXml(text);
					}
				}

			}
			System.out.println(message);
			return message;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 *    * 获取博客图文消息    *    * @param custermName    * @param serverName    * @param
	 * createTime    * @return    
	 */
	private ArticlesMessage getBlogMessage(String custermName, String serverName, Long createTime) {
		ArticlesMessage outputMsg = new ArticlesMessage();
		outputMsg.setFromUserName(serverName);
		outputMsg.setToUserName(custermName);
		outputMsg.setCreateTime(createTime);
		outputMsg.setMsgType("news");
		List<ArticlesItem> articles = new ArrayList<>();
		ArticlesItem item1 = new ArticlesItem();
		item1.setTitle("晚天吹凉风");
		item1.setDescription("点击进入晚天吹凉风博客");
		item1.setPicUrl("https://ss0.baidu.com/73x1bjeh1BF3odCf/it/u=1163727520,2326205650&fm=85&s=FAD05B8DC2B02637103D65850300B097");
		item1.setUrl("https://github.com/1843080373/study-wechat");
		articles.add(item1);
		outputMsg.setArticles(articles);
		outputMsg.setArticleCount(articles.size());
		return outputMsg;
	}
}
