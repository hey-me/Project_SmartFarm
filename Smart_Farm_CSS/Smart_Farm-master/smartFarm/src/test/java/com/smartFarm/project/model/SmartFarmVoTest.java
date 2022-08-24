//package com.smartFarm.project.model;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import java.time.LocalTime;
//import java.util.List;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import com.smartFarm.project.model.smartFarm.MonitoringRepository;
//import com.smartFarm.project.model.smartFarm.MonitoringVo;
//import com.smartFarm.project.model.smartFarm.SmartFarmRepository;
//import com.smartFarm.project.model.smartFarm.SmartFarmVo;
//import com.smartFarm.project.model.smartFarm.UserRepository;
//import com.smartFarm.project.model.smartFarm.UserVo;
//
//
//@SpringBootTest
//public class SmartFarmVoTest {
//
//    @Autowired
//    SmartFarmRepository smartFarmRepository;
//    
//    @Autowired
//    UserRepository userRepository;
//    
//    @Autowired
//    MonitoringRepository mr;
//    
//    @Test
//    void create() {
//    	UserVo user = UserVo.builder()
//    			.user_id("lsu878")
//    			.user_password("tmddnr12")
//    			.user_name("이승욱")
//    			.user_phone("010-9050-7094")
//    			.build();
//    	userRepository.save(user);
//    			
//    }
//
//    @Test
////    void save() {
////    	
////        // 1. 게시글 파라미터 생성
////        SmartFarmVo params = new SmartFarmVo("lsm878","바질",50,50,50,LocalTime.of(04,00,00),LocalTime.of(20,00,00));
////        
////
////        // 2. 게시글 저장
////        smartFarmRepository.save(params);
////
////        // 3. 1번 게시글 정보 조회
////        SmartFarmVo entity = smartFarmRepository.findById((String) "lsu878").get();
////        assertThat(entity.getCrop_species());
////        assertThat(entity.getLight_down_timing());
////        assertThat(entity.getUser_id());
////    }
//
//    @Test
//    void findAll() {
//
//        // 1. 전체 게시글 수 조회
//        long smartFarmCount = smartFarmRepository.count();
//
//        // 2. 전체 게시글 리스트 조회
//        List<SmartFarmVo> smartFarms = smartFarmRepository.findAll();
//    }
//
//    @Test
//    void delete() {
//
//        // 1. 게시글 조회
////        SmartFarmVo entity = smartFarmRepository.findById((String) "lsm878").get();
////
////        // 2. 게시글 삭제
////        smartFarmRepository.delete(entity);
//        
//        UserVo user=userRepository.findById((String)"lsu878").get();
//        userRepository.delete(user);
//    }
////    @Test
////    void tt() {
////		mr.save( 
////				MonitoringVo.builder("sf02")
////				 .first_water_tank_level("11")
////				 .second_water_tank_level("11")
////				 .third_water_tank_level("11")
////				 .monitoring_humidity("11")
////				 .monitoring_temperature("11")
////				 .monitoring_humidity("11")
////				 .magneticValue("11")
////				 .build());
////    }
//
//}