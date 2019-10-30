package com.starter.config.disconf;

import org.springframework.context.annotation.Configuration;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;

@Configuration
@DisconfFile(filename = "baiduAPI.properties")
public class BaiduApiConfiguration {

	private String mapAk;

	private String mapUrlPlaceSearch;

	private String mapUrlGetLocation;

	private String mapConvertUrl;

	@DisconfFileItem(name = "baidu.api.mapAk", associateField = "mapAk")
	public String getMapAk() {
		return mapAk;
	}

	public void setMapAk(String mapAk) {
		this.mapAk = mapAk;
	}

	@DisconfFileItem(name = "baidu.api.mapUrlPlaceSearch", associateField = "mapUrlPlaceSearch")
	public String getMapUrlPlaceSearch() {
		return mapUrlPlaceSearch;
	}

	public void setMapUrlPlaceSearch(String mapUrlPlaceSearch) {
		this.mapUrlPlaceSearch = mapUrlPlaceSearch;
	}

	@DisconfFileItem(name = "baidu.api.mapUrlGetLocation", associateField = "mapUrlGetLocation")
	public String getMapUrlGetLocation() {
		return mapUrlGetLocation;
	}

	public void setMapUrlGetLocation(String mapUrlGetLocation) {
		this.mapUrlGetLocation = mapUrlGetLocation;
	}

	@DisconfFileItem(name = "baidu.api.mapConvertUrl", associateField = "mapConvertUrl")
	public String getMapConvertUrl() {
		return mapConvertUrl;
	}

	public void setMapConvertUrl(String mapConvertUrl) {
		this.mapConvertUrl = mapConvertUrl;
	}

}
