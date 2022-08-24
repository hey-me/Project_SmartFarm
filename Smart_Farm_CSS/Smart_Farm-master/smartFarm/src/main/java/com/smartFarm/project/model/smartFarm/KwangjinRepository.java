package com.smartFarm.project.model.smartFarm;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KwangjinRepository extends JpaRepository<KwangjinVo, String>{
	List<KwangjinVo> findAll();
}
