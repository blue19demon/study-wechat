package com.starter.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.starter.domain.UserLocation;
import com.starter.pojo.BaiduPlace;
import com.starter.service.QRCodeService;
import com.starter.service.UserLocationService;
import com.starter.utils.BaiduMapApi;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutImageMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

@RestController
@Slf4j
public class MpController {
	@Autowired
	private WxMpService wxMpService;
	@Autowired
	private UserLocationService userLocationService;
	@Autowired
	private BaiduMapApi baiduMapApi;

	@Autowired
	private QRCodeService QRCodeService;

	@GetMapping("/check")
	public void check(HttpServletRequest request, HttpServletResponse response) {
		try {
			String signature = request.getParameter("signature");
			String nonce = request.getParameter("nonce");
			String timestamp = request.getParameter("timestamp");

			if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
				// 消息签名不正确，说明不是公众平台发过来的消息
				log.info("非法请求");
				return;
			}
			String echostr = request.getParameter("echostr");
			if (!StringUtils.isEmpty(echostr)) {
				// 说明是一个仅仅用来验证的请求，回显echostr
				response.getWriter().println(echostr);
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@PostMapping("/check")
	@ResponseBody
	public String handleMsg(HttpServletRequest request) {
		try {
			WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(request.getInputStream());
			log.info("\n消息解密后内容为：\n{} ", JSONObject.toJSONString(inMessage, true));
			String eventType = inMessage.getEvent();
			String msgType = inMessage.getMsgType();

			// 默认返回的文本消息内容
			String respContent = null;
			String fromUserName = inMessage.getFromUser();
			String toUserName = inMessage.getToUser();
			String respXml = null;
			if (WxConsts.XmlMsgType.TEXT.equals(msgType)) {
				String content = inMessage.getContent().trim();
				if (content.equals("附近")) {
					respContent = getUsage();
				}
				// 周边搜索
				else if (content.startsWith("附近")) {
					String keyWord = content.replaceAll("附近", "").trim();
					// 获取用户最后一次发送的地理位置
					UserLocation location = userLocationService.getLastLocation(request, fromUserName);
					// 未获取到
					if (null == location) {
						respContent = getUsage();
					} else {
						// 根据转换后（纠偏）的坐标搜索周边POI
						List<BaiduPlace> placeList = baiduMapApi.searchPlace(keyWord, location.getBd09Lng(),
								location.getBd09Lat());
						// 未搜索到POI
						if (null == placeList || 0 == placeList.size()) {
							respContent = String.format("/难过，您发送的位置附近未搜索到“%s”信息！", keyWord);
						} else {
							List<WxMpXmlOutNewsMessage.Item> articleList = baiduMapApi.makeArticleList(placeList, location.getBd09Lng(),
									location.getBd09Lat());
							// 回复图文消息
							WxMpXmlOutNewsMessage newsMessage=WxMpXmlOutMessage.NEWS().articles(articleList).fromUser(toUserName)
									.toUser(fromUserName).build();
							log.info(JSONObject.toJSONString(newsMessage, true));
							respXml = newsMessage.toXml();
						}
					}
				} else
					respContent = getUsage();
			} // 地理位置消息
			else if (msgType.equals(WxConsts.XmlMsgType.LOCATION)) {
				// 用户发送的经纬度
				String lng = String.valueOf(inMessage.getLocationY());
				String lat = String.valueOf(inMessage.getLocationX());
				// 坐标转换后的经纬度
				String bd09Lng = null;
				String bd09Lat = null;
				// 调用接口转换坐标
				UserLocation userLocation = baiduMapApi.convertCoord(lng, lat);
				if (null != userLocation) {
					bd09Lng = userLocation.getBd09Lng();
					bd09Lat = userLocation.getBd09Lat();
				}
				// 保存用户地理位置
				userLocationService.saveUserLocation(inMessage.getLabel(), fromUserName, lng, lat, bd09Lng, bd09Lat);

				StringBuffer buffer = new StringBuffer();
				buffer.append("[愉快]").append("成功接收您的位置！").append("\n\n");
				buffer.append("您可以输入搜索关键词获取周边信息了，例如：").append("\n");
				buffer.append("        附近ATM").append("\n");
				buffer.append("        附近KTV").append("\n");
				buffer.append("        附近厕所").append("\n");
				buffer.append("必须以“附近”两个字开头！");
				respContent = buffer.toString();
			}
			// 事件推送
			else if (msgType.equals(WxConsts.XmlMsgType.EVENT)) {
				// 关注
				if (eventType.equals(WxConsts.EventType.SUBSCRIBE)) {
					respContent = getSubscribeMsg();
				}

				// 点击菜单 我的名片
				if (eventType.equals(WxConsts.EventType.CLICK)) {
					if ("MY_CARD".equals(inMessage.getEventKey())) {
						return getMyCard(request,fromUserName, toUserName);
					}
				}
			} else {
				respContent = getUsage();
			}
			if (null != respContent) {
				WxMpXmlOutTextMessage outMessage = WxMpXmlOutMessage.TEXT().content(respContent).fromUser(toUserName)
						.toUser(fromUserName).build();
				// 将文本消息对象转换成xml
				respXml = outMessage.toXml();
			}
			return respXml;
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getMyCard(HttpServletRequest request,String fromUserName, String toUserName) {
		String media_id = null;
		try {
			WxMpUser wxMpUser= wxMpService.getUserService().userInfo(fromUserName);
			media_id = QRCodeService.QRCodeCreate(fromUserName, wxMpUser.getHeadImgUrl());
		} catch (Exception e) {
			e.printStackTrace();
		}
		WxMpXmlOutImageMessage message = WxMpXmlOutMessage.IMAGE().mediaId(media_id).fromUser(toUserName)
				.toUser(fromUserName).build();
		return message.toXml();
	}

	/**
	 * 关注提示语
	 * 
	 * @return
	 */
	private static String getSubscribeMsg() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("您是否有过出门在外四处找ATM或厕所的经历？").append("\n\n");
		buffer.append("您是否有过出差在外搜寻美食或娱乐场所的经历？").append("\n\n");
		buffer.append("周边搜索为您的出行保驾护航，为您提供专业的周边生活指南，回复“附近”开始体验吧！");
		return buffer.toString();
	}

	/**
	 * 使用说明
	 * 
	 * @return
	 */
	private static String getUsage() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("周边搜索使用说明").append("\n\n");
		buffer.append("1）发送地理位置").append("\n");
		buffer.append("点击窗口底部的“+”按钮，选择“位置”，点“发送”").append("\n\n");
		buffer.append("2）指定关键词搜索").append("\n");
		buffer.append("格式：附近+关键词\n例如：附近ATM、附近KTV、附近厕所");
		return buffer.toString();
	}
}
