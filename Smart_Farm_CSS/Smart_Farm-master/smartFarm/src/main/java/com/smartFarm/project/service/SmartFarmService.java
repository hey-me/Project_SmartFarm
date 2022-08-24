package com.smartFarm.project.service;

import java.security.MessageDigest;
import java.util.List;

import javax.transaction.Transactional;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.smartFarm.project.model.smartFarm.MonitoringRepository;
import com.smartFarm.project.model.smartFarm.MonitoringVo;
import com.smartFarm.project.model.smartFarm.SmartFarmRepository;
import com.smartFarm.project.model.smartFarm.SmartFarmVo;
import com.smartFarm.project.model.smartFarm.UserRepository;
import com.smartFarm.project.model.smartFarm.UserVo;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class SmartFarmService {

	@Autowired
	SmartFarmRepository smartFarmRepository;

	@Autowired
	MonitoringRepository monitoringRepository;

	@Autowired
	UserRepository userRepository;
	
	

	ModelAndView mav;

	public ModelAndView movePage(String menu) {
		mav = new ModelAndView();
		mav.addObject("menu", menu);
		mav.setViewName("contents/main");
		return mav;
	}



	// ---------------------암호화---------------------------//
	public String getSHA256(String data) {
		String SHA = "";
		try {
			MessageDigest sh = MessageDigest.getInstance("SHA-256");
			sh.update(data.getBytes());
			byte byteData[] = sh.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < byteData.length; i++) {
				sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
			}
			SHA = sb.toString();

		} catch (Exception e) {
			e.printStackTrace();
			SHA = null;
		}
		return SHA;
	}
	

	//--------로그인 user database내에 userdevice값 비교--------//
	public ModelAndView userComparison(String device_code,String sessionDevice_Code) {
		if(device_code.equals(sessionDevice_Code)) {
			System.out.printf(device_code,sessionDevice_Code);
			return mav=movePage("monitoring");
		}else {
			return mav=movePage("");
					
		}
	}
	public void listVo(List<MonitoringVo> monitoring) {
		monitoring.get(1);
		
	}
	
}
