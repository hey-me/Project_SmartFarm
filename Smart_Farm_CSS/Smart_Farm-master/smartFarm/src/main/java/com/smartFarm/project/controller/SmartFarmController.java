package com.smartFarm.project.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.mysql.cj.xdevapi.JsonArray;
import com.smartFarm.project.model.smartFarm.*;
import com.smartFarm.project.security.CustomUserDetails;
import com.smartFarm.project.service.SmartFarmService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
public class SmartFarmController {

	@Autowired
	SmartFarmRepository smartFarmRepository;

	@Autowired
	SmartFarmService smartFarmService;

	@Autowired
	MonitoringRepository monitoringRepository;

	@Autowired
	UserRepository userRepository;

	ModelAndView mav;

	@RequestMapping(value = "/home")
	public ModelAndView home(HttpServletRequest request) {
		mav = smartFarmService.movePage("home");
		return mav;
	}
	
	@GetMapping(value = "/home")
	public ModelAndView homeLogin(@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "exception", required = false) String exception) {
		mav = smartFarmService.movePage("home");
		mav.addObject("error", error);
		mav.addObject("exception", exception);
		return mav;
	}


	@RequestMapping(value = "/monitoring")
	public ModelAndView monitoring(HttpServletRequest request) {
		mav = smartFarmService.movePage("monitoring");
		
		HttpSession session = request.getSession();
		String device_code="";
		
		
		if(session.getAttribute("user") instanceof CustomUserDetails) {
			CustomUserDetails user=(CustomUserDetails) session.getAttribute("user");
			UserVo userVo=user.getUserVo();
			device_code=userVo.getUser_device();
			List<MonitoringVo> monitoring = monitoringRepository.findByDevice_CodeDescLimit(device_code,24);
			mav.addObject("monitoringData", monitoring);
		}
		
		return mav;
	}

	@RequestMapping(value = "/settingChange")
	public ModelAndView settingChange(HttpServletRequest request) {
		mav = smartFarmService.movePage("settingChange");
		
		HttpSession session = request.getSession();
		String device_code="";
		
		if(session.getAttribute("user") instanceof CustomUserDetails) {
			CustomUserDetails user=(CustomUserDetails) session.getAttribute("user");
			UserVo userVo=user.getUserVo();
			device_code=userVo.getUser_device();
			Optional<SmartFarmVo> optinal=smartFarmRepository.findById(device_code);
			if(optinal.isPresent()) {
				SmartFarmVo svo=optinal.get();
				mav.addObject("SmartFarmVo", svo);
			}
			
		}

		
		return mav;
	}

	@RequestMapping(value = "/explanation")
	public ModelAndView explanation(HttpServletRequest request) {
		mav = smartFarmService.movePage("explanation");
		return mav;
	}

	@RequestMapping(value = "/photo")
	public ModelAndView photo(HttpServletRequest request) {
		mav = smartFarmService.movePage("photo");
		return mav;
	}

	@RequestMapping(value = "/teamRole")
	public ModelAndView teamRole(HttpServletRequest request) {
		mav = smartFarmService.movePage("teamRole");
		return mav;
	}

	@RequestMapping(value = "/shamePoint")
	public ModelAndView shamePoint(HttpServletRequest request) {
		mav = smartFarmService.movePage("shamePoint");
		
		HttpSession session = request.getSession();
		String device_code="";
		
		if(session.getAttribute("user") instanceof CustomUserDetails) {
			CustomUserDetails user=(CustomUserDetails) session.getAttribute("user");
			UserVo userVo=user.getUserVo();
			device_code=userVo.getUser_device();
			List<MonitoringVo> monitoring = monitoringRepository.findByDevice_CodeDescLimit(device_code,24);
			mav.addObject("monitoringData", monitoring);
		}
		
		return mav;
	}

	@ResponseBody
	@RequestMapping(value = "/app/monitoring")
	public String insertSensingData( @RequestParam String device_code) {
		List<MonitoringVo> monitoring = monitoringRepository.findByDevice_CodeDescLimit(device_code,24);
		String result=JSONArray.toJSONString(monitoring);
		return result;
		
	}

}
