package com.smartFarm.project.model.smartFarm;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<UserVo, String>{
	List<UserVo> findAll();
}
