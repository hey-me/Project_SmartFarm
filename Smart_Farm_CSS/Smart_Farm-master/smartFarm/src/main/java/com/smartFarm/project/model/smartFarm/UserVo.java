package com.smartFarm.project.model.smartFarm;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
@Entity(name = "user")
public class UserVo {

	@Id
	@GeneratedValue
	@Column(nullable = false, length = 30) // column의 조건
	@NonNull private String user_id;
	
	@Column(nullable = false, length = 256)
	private String user_password;
	
	@Column(nullable = false, length = 15)
	private String user_name;
	
	@Column(nullable = true, length = 30)
	private LocalDate user_birthday;
	
	@Column(nullable = true, length = 30)
	private String user_gender;
	
	@Column(nullable = false, length = 30)
	private String user_phone;
	
	@Column(nullable = true, length = 30)
	private String user_email;
	
	@Column(nullable = false, length = 10)
	private String user_grade;
	
	@Column(nullable = false, length = 20)
	private String user_device;
	
	
}
