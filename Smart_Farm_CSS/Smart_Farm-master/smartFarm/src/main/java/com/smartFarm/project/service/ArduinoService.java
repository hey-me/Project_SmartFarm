package com.smartFarm.project.service;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import com.smartFarm.project.controller.ArduinoController;
import com.smartFarm.project.model.smartFarm.MonitoringRepository;
import com.smartFarm.project.model.smartFarm.MonitoringVo;
import com.smartFarm.project.model.smartFarm.SmartFarmRepository;
import com.smartFarm.project.model.smartFarm.SmartFarmVo;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ArduinoService {

	@Autowired
	MonitoringRepository monitoringRepository;
	
	@Autowired
	SmartFarmRepository smartFarmRepository;

	public void insertSensingData(HashMap<String, Object> param) {
		MonitoringVo mvo = MonitoringVo.builder(param.get("device").toString())
				.firstWaterTankLevel(900-(Integer.parseInt(param.get("firstdistance").toString())*61)+"")
				.secondWaterTankLevel(900-(Integer.parseInt(param.get("secondDistance").toString())*61)+"")
				.thirdWaterTankLevel(900-(Integer.parseInt(param.get("thirdDistance").toString())*61)+"")
				.monitoringHumidity(param.get("humid").toString()).monitoringTemperature(param.get("tempC").toString())
				.magneticValue(param.get("magneticValue").toString())
				.monitoringIlluminance(param.get("illuminanceValue").toString()).build();
		
		
	
		
		if (mvo.getFirstWaterTankLevel().equals("") || mvo.getSecondWaterTankLevel().equals("")
				|| mvo.getThirdWaterTankLevel().equals("") || mvo.getMagneticValue().equals("")
				|| mvo.getMonitoringHumidity().equals("") || mvo.getMonitoringTemperature().equals("")
				|| mvo.getMonitoringIlluminance().equals("") 
				|| Integer.parseInt(mvo.getFirstWaterTankLevel()) >= 900 || Integer.parseInt(mvo.getFirstWaterTankLevel()) < 1
				|| Integer.parseInt(mvo.getSecondWaterTankLevel()) >= 900|| Integer.parseInt(mvo.getSecondWaterTankLevel()) < 1
				|| Integer.parseInt(mvo.getThirdWaterTankLevel()) >= 900 || Integer.parseInt(mvo.getThirdWaterTankLevel()) < 1 ||param.get("ip").toString().equals("")){
			log.info("잘못된 값 송신");
			return;
		}
		
		
		
		MonitoringVo mvos = monitoringRepository.save(mvo);
		monitoringRepository.flush();
		
		SmartFarmVo svo;	
		Optional<SmartFarmVo> optinal=smartFarmRepository.findById("sm02");
		if(optinal.isPresent()) {
			svo=optinal.get();
		}else {
			svo= SmartFarmVo.builder(param.get("device").toString())
				.ipAddress(param.get("ip").toString())
				.build();
		}
		
		smartFarmRepository.save(svo);
		smartFarmRepository.flush();
		
		log.info(mvos.toString());
		log.info("개수" + monitoringRepository.count());

	}

}
