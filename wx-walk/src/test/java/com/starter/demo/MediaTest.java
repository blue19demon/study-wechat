package com.starter.demo;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONObject;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MediaTest {
	@Autowired
	private WxMpService wxMpService;

	@Test
	public void wxMediaUpload() {
		File file = new File("D:\\用户目录\\我的图片\\timg.jpg");
		try {
			WxMediaUploadResult wxMediaUploadResult = wxMpService.getMaterialService().mediaUpload("image", file);
			/**
			 * { "createdAt":1571811488,
			 * "mediaId":"GApZCAyKdIIadkhQpuFYGHVxzA3FBkSyQo7-XK46p1cdJBafY9nsBQXaoGsb2zpn",
			 * "type":"image" }
			 */
			log.info(JSONObject.toJSONString(wxMediaUploadResult, true));
		} catch (WxErrorException e) {
			e.printStackTrace();
		}
	}

}
