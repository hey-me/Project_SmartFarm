package com.smartFarm.project.model.smartFarm;


import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.*;

import org.hibernate.annotations.ColumnDefault;

import com.smartFarm.project.model.smartFarm.MonitoringVo.MonitoringVoBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data //getter,setter,toString,defalt 자동생성
@Builder(builderMethodName = "smartFarmVoBuilder") 
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Entity(name="smart_farm")
public class SmartFarmVo {
	
	@Id //JPA 어노테이션 아이디 인것을 인지함
	@Column(name ="device_code", nullable = false, length = 30)
	@NonNull private String deviceCode; //
	
	@Column(name ="crop_species", nullable = true, length = 30)
	 private String cropSpecies; //식물 종류
	
	@Column(name ="supply_water_timing", nullable = true)
	private int supplyWaterTiming; // 급수 간격
	
	@Column(name ="supply_medium_timing",nullable = true)
	 private int supplyMediumTiming; // 배양액 급수 간격
	
	@Column(name ="supply_miticide_timing", nullable = true)
	 private int supplyMiticideTiming; // 살충제 급수 간격
	
	@Column(name ="light_up_timing", nullable = true)
	 private LocalTime lightUpTiming; // 불켜는 시간
	
	@Column(name ="light_down_timing", nullable = true)
	 private LocalTime lightDownTiming; // 불 끄는 시간
	
	@Column(name ="plant_day", nullable = true)
	private LocalDate plantDay=LocalDate.now(); // 심은 날짜
	
	@Column(name ="ip_address", nullable = true,  length = 30)
	private String ipAddress; //ip
	
	public static SmartFarmVoBuilder builder(String deviceCode) {
        if(deviceCode == null) {
            throw new IllegalArgumentException("필수 파라미터 누락");
        }
        return smartFarmVoBuilder().deviceCode(deviceCode);
    }
}
