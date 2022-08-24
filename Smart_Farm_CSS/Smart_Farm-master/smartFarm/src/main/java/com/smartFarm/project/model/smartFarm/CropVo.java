package com.smartFarm.project.model.smartFarm;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Entity(name = "crop")
public class CropVo {

		@Id
		@Column(nullable = false, length = 30)
		@NonNull private String crop_species;
		
		@Column(nullable = false)
		private int supply_water_timing; //권장 급수 간격
		
		@Column(nullable = false)
		private int supply_medium_timing; //권장 배양액 급수 간격
		
		@Column(nullable = false)
		private int supply_miticide_timing; // 권장 살충제 급수 간격
		
		@Column(nullable = false)
		private LocalTime light_up_timing; // 권장 불켜는 시간
		
		@Column(nullable = false)
		private LocalTime light_down_timing; // 권장 불끄는 시간
		
		@Column(nullable = false)
		private int temperature; // 권장 온도
		
		@Column(nullable = false)
		private int humidity; //권장 습도

}
