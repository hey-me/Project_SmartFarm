package com.smartFarm.project.controller;


import java.time.LocalDateTime;
import java.util.HashMap;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smartFarm.project.service.ArduinoService;

import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
public class ArduinoController {
	@Autowired
	ArduinoService arduinoService;
	
	@ResponseBody
	@RequestMapping(value = "/arduino")
	public String insertSensingData( @RequestBody HashMap<String, Object> param ) {
		log.info(param.size())	;
		log.info(param.toString());
		arduinoService.insertSensingData(param);
		return "받기완료";
		
	}

}
