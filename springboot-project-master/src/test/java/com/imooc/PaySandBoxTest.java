package com.imooc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.lly835.bestpay.service.impl.BestPayServiceImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by SqMax on 2018/3/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class PaySandBoxTest {

	 @Autowired
	 private BestPayServiceImpl bestPayService;

    @Test
    public void create() {
    	try {
			//log.info("【创建微信菜单】 result={}",wxMpService.getMenuService().menuCreate(menu));
		} catch (Exception e) {
			e.printStackTrace();
		}
        
    }

}