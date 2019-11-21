package com.starter.demo;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.binarywang.utils.qrcode.QrcodeUtils;
import com.starter.config.app.AppConfiguration;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class QRCodeTests {

	@Autowired
	private WxMpService wxMpService;
	@Autowired
	private AppConfiguration appConfiguration;

	@Test
	public void QRCodeCreate() {
		FileOutputStream fos = null;
		try {
			File logoFile = new File("D://idCard.jpg");
			
			byte[] content = QrcodeUtils.createQrcode(appConfiguration.getServerUrl() + "/authorize", logoFile);
			File outFile = new File("D://aa.jpg");
			WxMediaUploadResult res = wxMpService.getMaterialService().mediaUpload(WxConsts.MaterialType.IMAGE,
					outFile);
			res.getMediaId();
			fos = new FileOutputStream(outFile);
			InputStream ips = new ByteArrayInputStream(content);
			IOUtils.copy(ips, fos);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WxErrorException e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 根据byte数组，生成文件
	 * @param bfile 文件数组
	 * @param filePath 文件存放路径
	 * @param fileName 文件名称
	 */
	public static File byte2File(byte[] bytes,String filePath,String fileName){
	    BufferedOutputStream bos=null;
	    FileOutputStream fos=null;
	    File file=null;
	    try{
	        File dir=new File(filePath);
	        if(!dir.exists() && !dir.isDirectory()){//判断文件目录是否存在
	            dir.mkdirs();
	        }
	        file=new File(filePath+fileName);
	        fos=new FileOutputStream(file);
	        bos=new BufferedOutputStream(fos);
	        bos.write(bytes);
	    }
	    catch(Exception e){
	        System.out.println(e.getMessage());
	        e.printStackTrace();
	    }
	    finally{
	        try{
	            if(bos != null){
	                bos.close();
	            }
	            if(fos != null){
	                fos.close();
	            }
	        }
	        catch(Exception e){
	            System.out.println(e.getMessage());
	            e.printStackTrace();
	        }
	    }
		return file;
	}
	/**
	 * 将文件转换成byte数组
	 * 
	 * @param filePath
	 * @return
	 */
	public static byte[] File2byte(File tradeFile) {
		byte[] buffer = null;
		try {
			FileInputStream fis = new FileInputStream(tradeFile);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] b = new byte[1024];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}
}
