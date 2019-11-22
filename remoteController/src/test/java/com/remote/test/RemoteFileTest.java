package com.remote.test;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.remote.core.RemoteCommandService;
import com.remote.framework.RestResultDto;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RemoteFileTest {
	@Autowired
	private RemoteCommandService remoteCommandService;

	@Before
	public void setup() {
		RestResultDto login = remoteCommandService.login();
		if (!login.isSuccess()) {
			throw new RuntimeException(login.getMessage());
		}
	}
	
	@Test
	public void executeUpload() {
		String remoteFilePath="/home/uploads/";
		String localFilePath="D:\\用户目录\\我的图片\\timg.jpg";
		log.info("result->"+remoteCommandService.ftpUpload( remoteFilePath, localFilePath));
	}
	
	@Test
	public void executeDownload() {
		String remoteFilePath="/home/uploads/o0BJQ5uR7L6QjrwRek8sklsuOXpc.jpg";
		String localFilePath="D:\\用户目录\\我的图片\\";
		log.info("result->"+remoteCommandService.ftpDownload( remoteFilePath, localFilePath));
	}
	
	@Test
	public void deleteFile() {
		String remoteFilePath="/home/uploads/timg.jpg";
		log.info("result->"+remoteCommandService.deleteFile( remoteFilePath));
	}
	
	@Test
	public void deleteEmptyDir() {
		String remoteDirPath="/home/uploads/bb/";
		log.info("result->"+remoteCommandService.deleteEmptyDir( remoteDirPath));
	}
	
	@Test
	public void deleteNotEmptyDir() {
		String remoteDirPath="/home/uploads/test";
		log.info("result->"+remoteCommandService.deleteNotEmptyDir(remoteDirPath));
	}
	
	@Test
	public void transferDirectory() throws IOException {
		String localDirectory="D:/用户目录/我的图片";
		String remoteDirPath="/home/uploads/rr";
		remoteCommandService.transferDirectory( localDirectory, remoteDirPath);
	}
}