package com.starter.api;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.starter.config.PlatformAPIConfig;
import com.starter.service.FileDownload;
import com.starter.utils.RandomArray;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;


@Service
@Slf4j
public class VedioApi {
	@Autowired
	private PlatformAPIConfig platformAPIConfig;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private WxMpService wxMpService;
	@Value("${file.path}")
	private String path;
	
	public static final String ALL="ALL";
	public static final String ONE="ONE";
	public String search(String fromUserName, String toUserName,String keyWord) {
		try {
			log.info("传入的内容->" + keyWord);
			String apiUrl = String.format(platformAPIConfig.getVedioSearchAPI());
			String result = restTemplate.exchange(apiUrl, HttpMethod.GET, null, String.class).getBody();
			log.info("result->" + result);
			JSONObject json = JSONObject.parseObject(result);
			if(json.getInteger("code")==200) {
				JSONArray resultArray = json.getJSONArray("result");
				if(ONE.equals(keyWord)) {
					int index=(int)(Math.random()*resultArray.size());
					JSONObject videoJson = resultArray.getJSONObject(index);
					String video = videoJson.getString("video");
					File videoFile=new File(this.path+File.separator+"vedio_"+videoJson.getString("sid")+".mp4");
					FileDownload.download(video,videoFile);
					String thumbMediaId=wxMpService.getMaterialService().mediaUpload(WxConsts.MaterialType.VIDEO,
							videoFile).getMediaId();
					return WxMpXmlOutMessage.VIDEO()
							.fromUser(toUserName)
							.toUser(fromUserName)
							.title(videoJson.getString("text"))
							.description(videoJson.getString("name"))
							.mediaId(thumbMediaId)
							.build()
							.toXml();
				}else if(ALL.equals(keyWord)){
					JSONArray data = RandomArray.createRandomList(resultArray,5);
					StringBuffer buffer = new StringBuffer();
					for (int i = 0; i < data.size(); i++) {
						JSONObject item = data.getJSONObject(i);
						buffer.append("<a href='"+item.getString("video")+"'>"+item.getString("text")+"</a>").append("\n");
						buffer.append(item.getString("name")+"("+item.getString("passtime")+")").append("\n");
						if (i != data.size() - 1) {
							buffer.append("----------------------------\n");
						}
					}
					return WxMpXmlOutMessage
							.TEXT()
							.content(buffer.toString())
							.fromUser(toUserName)
							.toUser(fromUserName)
							.build()
							.toXml();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return WxMpXmlOutMessage
				.TEXT()
				.content("对不起，出错了")
				.fromUser(toUserName)
				.toUser(fromUserName)
				.build()
				.toXml();
	}
	
	
	public JSONObject satin() {
		try {
			String apiUrl = String.format(platformAPIConfig.getSatinApi());
			String result = restTemplate.exchange(apiUrl, HttpMethod.GET, null, String.class).getBody();
			log.info("result->" + result);
			JSONObject json = JSONObject.parseObject(result);
			if(json.getInteger("code")==200) {
				JSONArray resultArray = json.getJSONArray("data");
				int index=(int)(Math.random()*resultArray.size());
				return resultArray.getJSONObject(index);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}