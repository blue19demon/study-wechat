package com.remote.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.remote.config.AppConfiguration;
import com.remote.framework.RestResultDto;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;
import ch.ethz.ssh2.SFTPv3Client;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

@Service
public class RemoteCommandService {

	private static final Logger log = LoggerFactory.getLogger(RemoteCommandService.class);
	private static String DEFAULTCHART = "UTF-8";

	@Autowired
	private AppConfiguration appConfiguration;
	private Connection conn;

	/**
	 * 登录主机
	 * 
	 * @return 登录成功返回true，否则返回false
	 */
	public RestResultDto login() {
		boolean flg = false;
		try {
			conn = new Connection(appConfiguration.getTencentCloudIp());
			conn.connect();// 连接
			flg = conn.authenticateWithPassword(appConfiguration.getTencentCloudUsername(),
					appConfiguration.getTencentCloudPasswd());// 认证
			if (flg) {
				log.info("=========登录成功=========" + JSONObject.toJSONString(conn, true));
				return RestResultDto.succeed();
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
	public RestResultDto execute(String cmd) {
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
		return RestResultDto.succeed("执行命令成功,链接conn:" + conn.getHostname() + ",执行的命令：" + cmd + ",执行结果：" + result);
	}

	/**
	 * 上传文件到服务器
	 */
	public boolean ftpUpload(String remoteFilePath, String localFilePath) {
		boolean bool = false;
		try {
			SCPClient scpClient = conn.createSCPClient();
			scpClient.put(localFilePath, remoteFilePath);
			bool = true;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			bool = false;
		} finally {
			conn.close();
		}
		return bool;
	}

	/**
	 * 从服务器下载文件
	 */
	public boolean ftpDownload(String remoteFilePath, String localFilePath) {
		boolean bool = false;
		try {
			SCPClient scpClient = conn.createSCPClient();
			scpClient.get(remoteFilePath, localFilePath);
			bool = true;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			bool = false;
		} finally {
			conn.close();
		}
		return bool;
	}

	/**
	 * Remove an File
	 */
	public boolean deleteFile(String remoteFilePath) {
		boolean bool = false;
		try {
			SFTPv3Client sftpClient = new SFTPv3Client(conn);
			sftpClient.rm(remoteFilePath);// 删除文件
			bool = true;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			bool = false;
		} finally {
			conn.close();
		}
		return bool;
	}

	/**
	 * Remove an empty directory
	 */
	public boolean deleteEmptyDir(String remoteDirPath) {
		boolean bool = false;
		try {
			SFTPv3Client sftpClient = new SFTPv3Client(conn);
			sftpClient.rmdir(remoteDirPath);
			bool = true;
		} catch (IOException ioe) {
			ioe.printStackTrace();
			bool = false;
		} finally {
			conn.close();
		}
		return bool;
	}

	/**
	 * Remove an not empty directory
	 */
	public boolean deleteNotEmptyDir(String remoteDirPath) {
		boolean bool = false;
		try {
			String cmd = "rm -rf ".concat(remoteDirPath);
			execute(cmd);
			bool = true;
		} catch (Exception ioe) {
			ioe.printStackTrace();
			bool = false;
		} finally {
			conn.close();
		}
		return bool;
	}

	/**
	 * 远程传输单个文件
	 * 
	 * @param localFile
	 * @param remoteTargetDirectory
	 * @throws IOException
	 */
 
	public void transferFile(String localFile, String remoteTargetDirectory) throws IOException {
		File file = new File(localFile);
		if (file.isDirectory()) {
			throw new RuntimeException(localFile + "  is not a file");
		}
		String fileName = file.getAbsolutePath();
		SCPClient sCPClient = conn.createSCPClient();
		sCPClient.put(fileName, remoteTargetDirectory);
	}
 
	/**
	 * 传输整个目录
	 * 
	 * @param localFile
	 * @param remoteTargetDirectory
	 * @throws IOException
	 */
	public void transferDirectory(String localDirectory, String remoteTargetDirectory) throws IOException {
		File dir = new File(localDirectory);
		if (!dir.isDirectory()) {
			throw new RuntimeException(localDirectory + " is not directory");
		}
 
		String[] files = dir.list();
		for (String file : files) {
			if (file.startsWith(".")) {
				continue;
			}
			String fullName = localDirectory + "/" + file;
			if (new File(fullName).isDirectory()) {
				String rdir = remoteTargetDirectory + "/" + file;
				execCommand("mkdir -p " + remoteTargetDirectory + "/" + file);
				
				transferDirectory(fullName, rdir);
			} else {
				System.out.println("正在上传="+fullName);
				transferFile(fullName, remoteTargetDirectory);
			}
		}
 
	}
	public String execCommand(String command) throws IOException {
		Session session = conn.openSession();
		session.execCommand(command);
		InputStream streamGobbler = new StreamGobbler(session.getStdout());
 
		String result = IOUtils.toString(streamGobbler, StandardCharsets.UTF_8);
 
		session.waitForCondition(ChannelCondition.EXIT_SIGNAL, Long.MAX_VALUE);
 
		if (session.getExitStatus().intValue() == 0) {
			log.info("execCommand: {} success ", command);
		} else {
			log.error("execCommand : {} fail", command);
		}
 
		IOUtils.closeQuietly(streamGobbler);
		session.close();
		return result;
	}
	/**
	 * 解析脚本执行返回的结果集
	 * 
	 * @param in      输入流对象
	 * @param charset 编码
	 * @return 以纯文本的格式返回
	 */
	private String processStdout(InputStream in, String charset) {
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