package com.remote.framework;

public interface CommandShell {
	public static final String CMD_START_WXWALK = "cd /home/apps/wx-walk && ./jar.sh start wxWalk.jar";
	public static final String CMD_STOP_WXWALK = "cd /home/apps/wx-walk && ./jar.sh stop wxWalk.jar";
}
