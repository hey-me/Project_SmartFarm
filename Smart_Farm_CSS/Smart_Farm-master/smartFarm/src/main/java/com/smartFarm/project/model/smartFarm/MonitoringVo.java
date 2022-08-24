package com.smartFarm.project.model.smartFarm;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Builder(builderMethodName = "monitoringVoBuilder")
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@IdClass(MonitoringVoId.class)
@Entity(name = "monitoring")
public class MonitoringVo {
	
	@Id
	@Column(name ="device_code", nullable = false, length = 30) // column의 조건
	@NonNull
	private String deviceCode;
	
	@Id
	@Builder.Default
	@Column(name ="monitoring_time", nullable = false)
	private LocalTime monitoringTime=LocalTime.now();
	
	@Id
	@Builder.Default
	@Column(name ="monitoring_date", nullable = false)
	private LocalDate monitoringDate=LocalDate.now(); // 모니터링 시간
	
	@Column(name ="monitoring_temperature", length = 10, nullable = false)
	private String monitoringTemperature; // 온도
	
	@Column(name ="monitoring_humidity", length = 10, nullable = false)
	private String monitoringHumidity; // 습도
	
	@Column(name ="monitoring_illuminance", length = 10, nullable = false)
	private String monitoringIlluminance; // 조도
	
	@Column(name ="first_water_tank_level", length = 10, nullable = false)
	private String firstWaterTankLevel; //  1 물탱크 수위
	
	@Column(name ="second_water_tank_level", length = 10, nullable = false)
	private String secondWaterTankLevel; // 2 물탱크 수위
	
	@Column(name ="third_water_tank_level", length = 10, nullable = false)
	private String thirdWaterTankLevel; // 3 물탱크 수위
	
	@Column(name ="magnetic_value", length = 3, nullable = false)
	private String magneticValue; // 자석센서
	
	public static MonitoringVoBuilder builder(String deviceCode) {
        if(deviceCode == null) {
            throw new IllegalArgumentException("필수 파라미터 누락");
        }
        return monitoringVoBuilder().deviceCode(deviceCode);
    }

}
