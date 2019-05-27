package com.wechat.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.wechat.config.WechatAPIURL;
import com.wechat.utils.AuthUtil;
/**
 * 自定义菜单
 * @author Administrator
 *
 */
@Controller
public class MenuController {
	private final static Logger logger = LoggerFactory.getLogger(MenuController.class);
	@Autowired
	private AuthUtil authUtil;
	/**一级菜单最多三个，测试环境
	 * @param code
	 * @param model
	 * @return
	 */
	@RequestMapping("create_menu")
	@ResponseBody
	public String create_menu() {
		JSONObject create_menu_result = null;
		try {
			String access_token = authUtil.doAccessToken();
			String create_menu_url = WechatAPIURL.create_menu;
			create_menu_url = create_menu_url.replaceAll("ACCESS_TOKEN", access_token);
			String dataSend="{\r\n" + 
					"    \"button\": [\r\n" + 
					"        {\r\n" + 
					"            \"name\": \"扫码\", \r\n" + 
					"            \"sub_button\": [\r\n" + 
					"                {\r\n" + 
					"                    \"type\": \"scancode_waitmsg\", \r\n" + 
					"                    \"name\": \"扫码带提示\", \r\n" + 
					"                    \"key\": \"rselfmenu_0_0\", \r\n" + 
					"                    \"sub_button\": [ ]\r\n" + 
					"                }, \r\n" + 
					"                {\r\n" + 
					"                    \"type\": \"scancode_push\", \r\n" + 
					"                    \"name\": \"扫码推事件\", \r\n" + 
					"                    \"key\": \"rselfmenu_0_1\", \r\n" + 
					"                    \"sub_button\": [ ]\r\n" + 
					"                }\r\n" + 
					"            ]\r\n" + 
					"        }, \r\n" + 
					"        {\r\n" + 
					"            \"name\": \"发图\", \r\n" + 
					"            \"sub_button\": [\r\n" + 
					"                {\r\n" + 
					"                    \"type\": \"pic_sysphoto\", \r\n" + 
					"                    \"name\": \"系统拍照发图\", \r\n" + 
					"                    \"key\": \"rselfmenu_1_0\", \r\n" + 
					"                   \"sub_button\": [ ]\r\n" + 
					"                 }, \r\n" + 
					"                {\r\n" + 
					"                    \"type\": \"pic_photo_or_album\", \r\n" + 
					"                    \"name\": \"拍照或者相册发图\", \r\n" + 
					"                    \"key\": \"rselfmenu_1_1\", \r\n" + 
					"                    \"sub_button\": [ ]\r\n" + 
					"                }, \r\n" + 
					"                {\r\n" + 
					"                    \"type\": \"pic_weixin\", \r\n" + 
					"                    \"name\": \"微信相册发图\", \r\n" + 
					"                    \"key\": \"rselfmenu_1_2\", \r\n" + 
					"                    \"sub_button\": [ ]\r\n" + 
					"                }\r\n" + 
					"            ]\r\n" + 
					"        }, \r\n" + 
					"        {\r\n" + 
					"            \"name\": \"发送位置\", \r\n" + 
					"            \"type\": \"location_select\", \r\n" + 
					"            \"key\": \"rselfmenu_2_0\"\r\n" + 
					"        }" +
					"    ]\r\n" + 
					"}";
			logger.info(dataSend);
			create_menu_result = authUtil.doPOSTJson(create_menu_url, dataSend);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return create_menu_result.toJSONString();
	}
	
	/**
	 * 删除菜单
	 * @return
	 */
	@RequestMapping("delete_menu")
	@ResponseBody
	public String delete_menu() {
		JSONObject delete_menu_result = null;
		try {
			String access_token = authUtil.doAccessToken();
			String delete_menu_url = WechatAPIURL.delete_menu;
			delete_menu_url = delete_menu_url.replaceAll("ACCESS_TOKEN", access_token);
			String dataSend="";
			logger.info(dataSend);
			delete_menu_result = authUtil.doPOSTJson(delete_menu_url, dataSend);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return delete_menu_result.toJSONString();
	}
	
	/**
	 * 创建个性化菜单
	 * @return
	 */
	@RequestMapping("addconditional_menu")
	@ResponseBody
	public String addconditional_menu() {
		JSONObject delete_menu_result = null;
		try {
			String access_token = authUtil.doAccessToken();
			String addconditional_menu_url = WechatAPIURL.addconditional_menu;
			addconditional_menu_url = addconditional_menu_url.replaceAll("ACCESS_TOKEN", access_token);
			String dataSend="{\r\n" + 
					"     \"button\":[\r\n" + 
					"     {    \r\n" + 
					"        \"type\":\"click\",\r\n" + 
					"        \"name\":\"今日歌曲\",\r\n" + 
					"         \"key\":\"V1001_TODAY_MUSIC\" },\r\n" + 
					"    {     \"name\":\"菜单\",\r\n" + 
					"        \"sub_button\":[\r\n" + 
					"        {            \r\n" + 
					"            \"type\":\"view\",\r\n" + 
					"            \"name\":\"搜索\",\r\n" + 
					"            \"url\":\"http://www.soso.com/\"},\r\n" +
					"             {\r\n" + 
					"        \"type\":\"click\",\r\n" + 
					"        \"name\":\"赞一下我们\",\r\n" + 
					"        \"key\":\"V1001_GOOD\"\r\n" + 
					"           }]\r\n" + 
					" }],\r\n" + 
					"\"matchrule\":{\r\n" + 
					"  \"tag_id\":\"2\",\r\n" + 
					"  \"sex\":\"1\",\r\n" + 
					"  \"country\":\"中国\",\r\n" + 
					"  \"province\":\"广东\",\r\n" + 
					"  \"city\":\"广州\",\r\n" + 
					"  \"client_platform_type\":\"2\",\r\n" + 
					"  \"language\":\"zh_CN\"\r\n" + 
					"  }\r\n" + 
					"}";
			logger.info(dataSend);
			delete_menu_result = authUtil.doPOSTJson(addconditional_menu_url, dataSend);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//{"menuid":449649634}
		return delete_menu_result.toJSONString();
	}
}
