package com.remote.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.remote.config.AppConfiguration;
import com.remote.framework.RestResultDto;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

@Service
public class RemoteCommandService {

	private static final Logger log = LoggerFactory.getLogger(RemoteCommandService.class);
	private static String DEFAULTCHART = "UTF-8";
	
	@Autowired
	private AppConfiguration appConfiguration;

	/**
	 * 登录主机
	 * 
	 * @return 登录成功返回true，否则返回false
	 */
	public RestResultDto login() {
		boolean flg = false;
		Connection conn = null;
		try {
			conn = new Connection(appConfiguration.getTencentCloudIp());
			conn.connect();// 连接
			flg = conn.authenticateWithPassword(appConfiguration.getTencentCloudUsername(),
					appConfiguration.getTencentCloudPasswd());// 认证
			if (flg) {
				log.info("=========登录成功=========" + conn);
				return RestResultDto.succeed(conn);
			}
		} catch (IOException e) {
			log.error("=========登录失败=========" + e.getMessage());
			e.printStackTrace();
			return RestResultDto.fail(e.getMessage());
		}
		return RestResultDto.fail("=========登录失败=========");
	}

	/**
	 * 远程执行shll脚本或者命令
	 * 
	 * @param cmd 即将执行的命令
	 * @return 命令执行完后返回的结果值
	 */
	public RestResultDto execute(Connection conn, String cmd) {
		String result = "";
		try {
			if (conn != null) {
				Session session = conn.openSession();// 打开一个会话
				session.execCommand(cmd);// 执行命令
				result = processStdout(session.getStdout(), DEFAULTCHART);
				// 如果为得到标准输出为空，说明脚本执行出错了
				if (StringUtils.isBlank(result)) {
					log.info("得到标准输出为空,链接conn:" + conn + ",执行的命令：" + cmd);
					result = processStdout(session.getStderr(), DEFAULTCHART);
				} else {
					log.info("执行命令成功,链接conn:" + conn + ",执行的命令：" + cmd);
				}
				conn.close();
				session.close();
			}
		} catch (IOException e) {
			log.info("执行命令失败,链接conn:" + conn + ",执行的命令：" + cmd + "  " + e.getMessage());
			e.printStackTrace();
			return RestResultDto.fail("执行命令失败,链接conn:" + conn + ",执行的命令：" + cmd + "  " + e.getMessage());
		}
		return RestResultDto.succeed("执行命令成功,链接conn:" + conn.getHostname() + ",执行的命令：" + cmd);
	}

	/**
	 * 解析脚本执行返回的结果集
	 * 
	 * @param in      输入流对象
	 * @param charset 编码
	 * @return 以纯文本的格式返回
	 */
	private static String processStdout(InputStream in, String charset) {
		InputStream stdout = new StreamGobbler(in);
		StringBuffer buffer = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(stdout, charset));
			String line = null;
			while ((line = br.readLine()) != null) {
				buffer.append(line + "\n");
			}
		} catch (UnsupportedEncodingException e) {
			log.error("解析脚本出错：" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			log.error("解析脚本出错：" + e.getMessage());
			e.printStackTrace();
		}
		return buffer.toString();
	}
}