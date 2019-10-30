package com.starter.api;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.starter.config.disconf.AppConfiguration;
import com.starter.config.disconf.PlatformAPIConfig;
import com.starter.service.FileDownload;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;


@Service
@Slf4j
public class MusicApi {
	@Autowired
	private PlatformAPIConfig platformAPIConfig;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private WxMpService wxMpService;
	@Autowired
	private AppConfiguration appConfiguration;
	public String search(String fromUserName, String toUserName, String keyWord) {
		try {
			log.info("传入的内容->" + keyWord);
			String apiUrl = String.format(platformAPIConfig.getMusicAPI(), keyWord);
			String result = restTemplate.exchange(apiUrl, HttpMethod.GET, null, String.class).getBody();
			log.info("result->" + result);
			/** 请求失败处理 */
			if (null == result) {
				return WxMpXmlOutMessage
						.TEXT()
						.content("对不起，找不到您的音乐")
						.fromUser(toUserName)
						.toUser(fromUserName)
						.build()
						.toXml();
			}
			JSONObject json = JSONObject.parseObject(result);
			if(json.getInteger("code")==200) {
				JSONArray resultArray = json.getJSONArray("result");
				int index=(int)(Math.random()*resultArray.size());
				JSONObject music = resultArray.getJSONObject(index);
				String pic = music.getString("pic");
				File musicFile=new File(this.appConfiguration.getUploadFolder()+File.separator+"music_pic_"+music.getString("songid")+".jpg");
				FileDownload.download(pic,musicFile);
				String thumbMediaId=wxMpService.getMaterialService().mediaUpload(WxConsts.MaterialType.IMAGE,
						musicFile).getMediaId();
				return WxMpXmlOutMessage.MUSIC()
						.fromUser(toUserName)
						.toUser(fromUserName)
						.title(music.getString("title"))
						.description(music.getString("author"))
						.musicUrl(music.getString("url"))
						.thumbMediaId(thumbMediaId)
						.hqMusicUrl(music.getString("url"))
						.build()
						.toXml();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return WxMpXmlOutMessage
				.TEXT()
				.content("对不起，找不到您的音乐")
				.fromUser(toUserName)
				.toUser(fromUserName)
				.build()
				.toXml();
	}
}