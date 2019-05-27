package com.wechat.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.junit.Test;

import com.wechat.utils.AuthUtil;

/*@RunWith(SpringRunner.class)
@SpringBootTest*/
public class WechatApplicationTests {

	@Test
	public void localFile() {
		try {
			String runningJarPath = System.getProperty("user.dir");
			File file=new File(runningJarPath+"/src/main/resources/logo.jpg");
			FileInputStream input = new FileInputStream(file); 
			System.out.println(new AuthUtil().uploadPermanentMaterial(input,"image",null,null,".jpg"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void netFile() {
		String url = "http://m10.music.126.net/20190422154205/ff9aa5d7ff3645d2dc87b6494ed2619f/ymusic/6e4b/49c7/6116/3b0ff11c6755f24d35a90626b27bfba0.mp3";
		InputStream input = new AuthUtil().getNetStream(url); 
		System.out.println(new AuthUtil().uploadPermanentMaterial(input,"voice",null,null,".mp3"));
	}
	
}
