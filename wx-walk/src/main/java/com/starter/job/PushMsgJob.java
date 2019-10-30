package com.starter.job;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.alibaba.fastjson.JSONObject;
import com.starter.api.VedioApi;
import com.starter.api.enums.ApiManifest;
import com.starter.api.strategy.PlatformApiContext;
import com.starter.config.disconf.AppConfiguration;
import com.starter.service.FileDownload;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpKefuService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.kefu.WxMpKefuMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;

//@Component
@Slf4j
public class PushMsgJob {
	
	@Autowired
	private WxMpService wxMpService;
	@Autowired
	private AppConfiguration appConfiguration;
	
	@Autowired
	private VedioApi vedioApi;
	@Autowired
	private PlatformApiContext platformApiContext;
	/**
	 * 每天8点执行一次
	 */
    //@Scheduled(cron="*0 0 8 * * ?")
	@Scheduled(cron="*/60 * * * * ?")
	public void pushPic() {
		log.info("pushPic推送开始");
		try {
			WxMpUserList wxMpUserList = wxMpService.getUserService().userList(null);
			List<String> openids = wxMpUserList.getOpenids();
			if(!openids.isEmpty()) {
				for (String openid : openids) {
					WxMpKefuService wxMpKefuService = wxMpService.getKefuService();
					JSONObject satin = vedioApi.satin();
					wxMpKefuService.sendKefuMessage(WxMpKefuMessage.IMAGE().mediaId(getMediaId(satin.getString("bimageuri"))).toUser(openid).build());
					wxMpKefuService.sendKefuMessage(WxMpKefuMessage.TEXT().content(satin.getString("text")).toUser(openid).build());
				}
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("pushPic推送结束");
	}
	private String getMediaId(String picUrl) throws WxErrorException {
		File musicFile=new File(this.appConfiguration.getUploadFolder()+File.separator+"push_pic_"+new SimpleDateFormat("yyyy-MM-dd").format(new Date())+".jpg");
		FileDownload.download(picUrl,musicFile);
		String thumbMediaId=wxMpService.getMaterialService().mediaUpload(WxConsts.MaterialType.IMAGE,
				musicFile).getMediaId();
		return thumbMediaId;
	}
	
	
	/**
	 * 每天9点执行一次
	 */
	@Scheduled(cron="*0 0 9 * * ?")
    //@Scheduled(cron="*/60 * * * * ?")
	public void pushHisTeday() {
		log.info("pushHisTeday推送开始");
		try {
			WxMpUserList wxMpUserList = wxMpService.getUserService().userList(null);
			List<String> openids = wxMpUserList.getOpenids();
			if(!openids.isEmpty()) {
				for (String openid : openids) {
		    		wxMpService.getKefuService().sendKefuMessage(WxMpKefuMessage.TEXT().content(platformApiContext.excuteHttp(null, ApiManifest.HIS_TODAY_NEW)).toUser(openid).build());
				}
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("pushHisTeday推送结束");
	}
}