package com.starter.config.disconf;

import org.springframework.context.annotation.Configuration;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;

@Configuration
@DisconfFile(filename = "platformAPI.properties")
public class PlatformAPIConfig {

	private String tulingKey;

	private String tulingAPI;

	private String mobileQueryAPI;

	private String weatherQueryKey;

	private String weatherQueryAPI;

	private String jokeKey;

	private String jokeAPI;

	private String idcardKey;

	private String idcardAPI;

	private String hisTodayKey;

	private String hisTodayAPI;

	private String transApiUrl;

	private String transAppid;

	private String transSecurityKey;

	private String weixinQueryKey;

	private String weixinQueryAPI;

	private String toutiaoKey;

	private String toutiaoAPI;

	private String musicAPI;

	private String vedioSearchAPI;

	private String vedioDetailAPI;

	private String hisTodayNewAPI;

	private String satinApi;
	@DisconfFileItem(name = "platform.api.tulingKey", associateField = "tulingKey")
	public String getTulingKey() {
		return tulingKey;
	}

	public void setTulingKey(String tulingKey) {
		this.tulingKey = tulingKey;
	}
	@DisconfFileItem(name = "platform.api.tulingAPI", associateField = "tulingAPI")
	public String getTulingAPI() {
		return tulingAPI;
	}

	public void setTulingAPI(String tulingAPI) {
		this.tulingAPI = tulingAPI;
	}
	@DisconfFileItem(name = "platform.api.mobileQueryAPI", associateField = "mobileQueryAPI")
	public String getMobileQueryAPI() {
		return mobileQueryAPI;
	}

	public void setMobileQueryAPI(String mobileQueryAPI) {
		this.mobileQueryAPI = mobileQueryAPI;
	}
	@DisconfFileItem(name = "platform.api.weatherQueryKey", associateField = "weatherQueryKey")
	public String getWeatherQueryKey() {
		return weatherQueryKey;
	}

	public void setWeatherQueryKey(String weatherQueryKey) {
		this.weatherQueryKey = weatherQueryKey;
	}
	@DisconfFileItem(name = "platform.api.weatherQueryAPI", associateField = "weatherQueryAPI")
	public String getWeatherQueryAPI() {
		return weatherQueryAPI;
	}

	public void setWeatherQueryAPI(String weatherQueryAPI) {
		this.weatherQueryAPI = weatherQueryAPI;
	}
	@DisconfFileItem(name = "platform.api.jokeKey", associateField = "jokeKey")
	public String getJokeKey() {
		return jokeKey;
	}

	public void setJokeKey(String jokeKey) {
		this.jokeKey = jokeKey;
	}
	@DisconfFileItem(name = "platform.api.jokeAPI", associateField = "jokeAPI")
	public String getJokeAPI() {
		return jokeAPI;
	}

	public void setJokeAPI(String jokeAPI) {
		this.jokeAPI = jokeAPI;
	}
	@DisconfFileItem(name = "platform.api.idcardKey", associateField = "idcardKey")
	public String getIdcardKey() {
		return idcardKey;
	}

	public void setIdcardKey(String idcardKey) {
		this.idcardKey = idcardKey;
	}
	@DisconfFileItem(name = "platform.api.idcardAPI", associateField = "idcardAPI")
	public String getIdcardAPI() {
		return idcardAPI;
	}

	public void setIdcardAPI(String idcardAPI) {
		this.idcardAPI = idcardAPI;
	}
	@DisconfFileItem(name = "platform.api.hisTodayKey", associateField = "hisTodayKey")
	public String getHisTodayKey() {
		return hisTodayKey;
	}

	public void setHisTodayKey(String hisTodayKey) {
		this.hisTodayKey = hisTodayKey;
	}
	@DisconfFileItem(name = "platform.api.hisTodayAPI", associateField = "hisTodayAPI")
	public String getHisTodayAPI() {
		return hisTodayAPI;
	}

	public void setHisTodayAPI(String hisTodayAPI) {
		this.hisTodayAPI = hisTodayAPI;
	}
	@DisconfFileItem(name = "platform.api.transApiUrl", associateField = "transApiUrl")
	public String getTransApiUrl() {
		return transApiUrl;
	}

	public void setTransApiUrl(String transApiUrl) {
		this.transApiUrl = transApiUrl;
	}
	@DisconfFileItem(name = "platform.api.transAppid", associateField = "transAppid")
	public String getTransAppid() {
		return transAppid;
	}

	public void setTransAppid(String transAppid) {
		this.transAppid = transAppid;
	}
	@DisconfFileItem(name = "platform.api.transSecurityKey", associateField = "transSecurityKey")
	public String getTransSecurityKey() {
		return transSecurityKey;
	}

	public void setTransSecurityKey(String transSecurityKey) {
		this.transSecurityKey = transSecurityKey;
	}
	@DisconfFileItem(name = "platform.api.weixinQueryKey", associateField = "weixinQueryKey")
	public String getWeixinQueryKey() {
		return weixinQueryKey;
	}

	public void setWeixinQueryKey(String weixinQueryKey) {
		this.weixinQueryKey = weixinQueryKey;
	}
	@DisconfFileItem(name = "platform.api.weixinQueryAPI", associateField = "weixinQueryAPI")
	public String getWeixinQueryAPI() {
		return weixinQueryAPI;
	}

	public void setWeixinQueryAPI(String weixinQueryAPI) {
		this.weixinQueryAPI = weixinQueryAPI;
	}
	@DisconfFileItem(name = "platform.api.toutiaoKey", associateField = "toutiaoKey")
	public String getToutiaoKey() {
		return toutiaoKey;
	}

	public void setToutiaoKey(String toutiaoKey) {
		this.toutiaoKey = toutiaoKey;
	}
	@DisconfFileItem(name = "platform.api.toutiaoAPI", associateField = "toutiaoAPI")
	public String getToutiaoAPI() {
		return toutiaoAPI;
	}

	public void setToutiaoAPI(String toutiaoAPI) {
		this.toutiaoAPI = toutiaoAPI;
	}
	@DisconfFileItem(name = "platform.api.musicAPI", associateField = "musicAPI")
	public String getMusicAPI() {
		return musicAPI;
	}

	public void setMusicAPI(String musicAPI) {
		this.musicAPI = musicAPI;
	}
	@DisconfFileItem(name = "platform.api.vedioSearchAPI", associateField = "vedioSearchAPI")
	public String getVedioSearchAPI() {
		return vedioSearchAPI;
	}

	public void setVedioSearchAPI(String vedioSearchAPI) {
		this.vedioSearchAPI = vedioSearchAPI;
	}
	@DisconfFileItem(name = "platform.api.vedioDetailAPI", associateField = "vedioDetailAPI")
	public String getVedioDetailAPI() {
		return vedioDetailAPI;
	}

	public void setVedioDetailAPI(String vedioDetailAPI) {
		this.vedioDetailAPI = vedioDetailAPI;
	}
	@DisconfFileItem(name = "platform.api.hisTodayNewAPI", associateField = "hisTodayNewAPI")
	public String getHisTodayNewAPI() {
		return hisTodayNewAPI;
	}

	public void setHisTodayNewAPI(String hisTodayNewAPI) {
		this.hisTodayNewAPI = hisTodayNewAPI;
	}
	@DisconfFileItem(name = "platform.api.satinApi", associateField = "satinApi")
	public String getSatinApi() {
		return satinApi;
	}

	public void setSatinApi(String satinApi) {
		this.satinApi = satinApi;
	}
	
}
