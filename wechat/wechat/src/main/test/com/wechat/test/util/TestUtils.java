package com.wechat.test.util;

import java.io.IOException;
import java.util.List;

import com.wechat.music.api.HttpEngine;
import com.wechat.music.model.Song;
import com.wechat.music.provider.netease.NeteaseMusicApi;
import com.wechat.music.provider.netease.NeteaseSong;
import com.wechat.test.provider.NeteaseMusicTest;

import okhttp3.Request;
import okhttp3.Response;

public class TestUtils {
	public static int testDownload(String url) throws IOException {
		Request.Builder builder = new Request.Builder();
		builder.url(url);
		builder.get();
		Response response = HttpEngine.requestSync(builder.build(), false);
		int code = response.code();
		response.close();
		return code;
	}

	public static void main(String[] args) throws Exception {
		
		NeteaseMusicApi NeteaseMusicApi = new NeteaseMusicApi();
		List<NeteaseSong> result = (List<NeteaseSong>) NeteaseMusicApi.searchMusicSync("海阔天空", 1, true);
		if(result!=null&&result.size()>0) {
			NeteaseSong NeteaseSong=result.get(0);
			System.out.println(NeteaseSong.getName());
			System.out.println(NeteaseSong.getFormattedArtistsString());
			System.out.println(NeteaseSong.getPicUrl());
			System.out.println(NeteaseSong.getMusicLink().getUrl());
		}
	}
}
