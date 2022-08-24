package com.smartFarm.project.model.smartFarm;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PersistentTokenRepository extends JpaRepository<PersistentToken, String>{

}
