package com.smartFarm.project.model.smartFarm;

import java.util.List;

import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MonitoringRepository extends JpaRepository<MonitoringVo, MonitoringVoId> {

	List<MonitoringVo> findAll();
	
	List<MonitoringVo> findByDeviceCode(String deviceCode);

	// SQL 일반 파라미터 쿼리, @Param 사용 O
	@Query(value = "select * from monitoring where device_code = :device_code order by monitoring_time DESC , monitoring_date LIMIT :limit", nativeQuery = true)
	public List<MonitoringVo> findByDevice_CodeDescLimit(@Param(value = "device_code") String device_code, @Param(value = "limit") int limit);

}
