package com.smartFarm.project.model.smartFarm;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;


import lombok.*;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class MonitoringVoId implements Serializable  {
	
	@EqualsAndHashCode.Include
	private String deviceCode;
	
	@EqualsAndHashCode.Include
	private LocalTime monitoringTime=LocalTime.now(); // 모니터링 시간
	
	@EqualsAndHashCode.Include
	private LocalDate monitoringDate=LocalDate.now(); // 모니터링 시간
	
}
