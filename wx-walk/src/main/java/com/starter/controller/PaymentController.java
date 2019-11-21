package com.starter.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PaymentController {

	@RequestMapping(value = "h5pay")
	public ModelAndView h5pay(HttpServletRequest request, Map<String, Object> map)  throws Exception {
		return new ModelAndView("h5pay", map);
	}

}
