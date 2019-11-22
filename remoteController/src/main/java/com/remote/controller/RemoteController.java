package com.remote.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.remote.core.RemoteCommandService;
import com.remote.framework.CommandShell;
import com.remote.framework.RestResultDto;

@Controller
public class RemoteController {

	@Autowired
	private RemoteCommandService remoteCommandService;

	@RequestMapping("/excuteStart")
	@ResponseBody
	public RestResultDto excuteStart() {
		RestResultDto login = remoteCommandService.login();
		if (login.isSuccess()) {
			return remoteCommandService.execute(CommandShell.CMD_START_WXWALK);
		}
		return login;
	}

	@RequestMapping("/excuteStop")
	@ResponseBody
	public RestResultDto excuteStop() {
		RestResultDto login = remoteCommandService.login();
		if (login.isSuccess()) {
			return remoteCommandService.execute(CommandShell.CMD_STOP_WXWALK);
		}
		return login;
	}
}
