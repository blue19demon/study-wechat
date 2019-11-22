package com.remote.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONObject;
import com.remote.core.RemoteCommandService;
import com.remote.framework.CommandShell;
import com.remote.framework.RestResultDto;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RemoteCommandTest {
	@Autowired
	private RemoteCommandService remoteCommandService;

	@Test
	public void execute() {
		RestResultDto login = remoteCommandService.login();
		if (login.isSuccess()) {
			log.info(JSONObject.toJSONString(remoteCommandService.execute(CommandShell.CMD_STOP_WXWALK), true));
		}
	}
}