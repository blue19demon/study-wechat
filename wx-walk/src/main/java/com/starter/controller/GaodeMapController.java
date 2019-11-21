package com.starter.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/gaode")
public class GaodeMapController {

	@GetMapping("/routeBus")
	public ModelAndView routeBus(String p1Lng,String p1Lan,String p2Lng,String p2Lan) {
		Map<String,Object> model=new HashMap<String,Object>();
		model.put("p1Lng", p1Lng);
		model.put("p1Lan", p1Lan);
		model.put("p2Lng", p2Lng);
		model.put("p2Lan", p2Lan);
		return new ModelAndView("gaodeRouteBus", model);
	}
	
	@GetMapping("/routeWalk")
	public ModelAndView routeWalk(String p1Lng,String p1Lan,String p2Lng,String p2Lan) {
		Map<String,Object> model=new HashMap<String,Object>();
		model.put("p1Lng", p1Lng);
		model.put("p1Lan", p1Lan);
		model.put("p2Lng", p2Lng);
		model.put("p2Lan", p2Lan);
		return new ModelAndView("gaodeRouteWalk", model);
	}
	
	@GetMapping("/gaodeLocation")
	public String gaodeLocation(HttpServletRequest request, HttpServletResponse response) {
		return "gaodeLocation";
	}
}
