package com.smartFarm.project.security;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.smartFarm.project.model.smartFarm.UserVo;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class CustomUserDetails implements UserDetails{
	
private UserVo userVo;
	
	public CustomUserDetails(UserVo user) {
		this.userVo = user;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() { //유저가 갖고 있는 권한 목록

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		
	
			authorities.add(new SimpleGrantedAuthority(userVo.getUser_grade()));
		
		return authorities;
	}

	@Override
	public String getPassword() { //유저 비밀번호

		return userVo.getUser_password();
	}

	@Override
	public String getUsername() {// 유저 이름 혹은 아이디

		return userVo.getUser_name();
	}

	@Override
	public boolean isAccountNonExpired() {// 유저 아이디가 만료 되었는지

		return true;
	}

	@Override
	public boolean isAccountNonLocked() { // 유저 아이디가 Lock 걸렸는지

		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() { //비밀번호가 만료 되었는지

		return true;
	}

	@Override
	public boolean isEnabled() { // 계정이 활성화 되었는지

		return true;
	}

	
}