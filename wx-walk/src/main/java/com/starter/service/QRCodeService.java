package com.starter.service;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.binarywang.utils.qrcode.QrcodeUtils;
import com.starter.config.AppConfiguration;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.result.WxMediaUploadResult;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;

@Service
public class QRCodeService {

	@Autowired
	private WxMpService wxMpService;
	@Autowired
	private AppConfiguration appConfiguration;

	// 保存到static文件夹下的qrfile目录
	@Value("${file.path}")
	private String path;
	
	public String QRCodeCreate(String openId,String headImageUrl) {
		FileOutputStream fos = null;
		try {
			File dir=new File(this.path);
			if(!dir.exists()) {
				dir.mkdir();
			}
			File logoFile=new File(this.path+File.separator+openId+".jpg");
			ImageDownload.downloadPicture(headImageUrl,logoFile);
			byte[] content = QrcodeUtils.createQrcode(appConfiguration.getServerUrl() + "/authorize", logoFile);
			File outFile = new File(path+File.separator+openId+"_logo.jpg");
			fos = new FileOutputStream(outFile);
			InputStream ips = new ByteArrayInputStream(content);
			IOUtils.copy(ips, fos);
			WxMediaUploadResult res = wxMpService.getMaterialService().mediaUpload(WxConsts.MaterialType.IMAGE,
					outFile);
			return res.getMediaId();
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
		return headImageUrl;
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
