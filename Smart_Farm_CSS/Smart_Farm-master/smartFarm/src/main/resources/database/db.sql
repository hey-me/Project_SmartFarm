CREATE TABLE smart_farm(
	user_id varchar(20)  NOT NULL COMMENT '사용자 아이디',
	crop_species varchar(20) not null comment '작물 종류',
	supply_water_timing int NOT NULL COMMENT '급수 간격',
    supply_medium_timing int NOT NULL COMMENT '배양액 급수 간격',
    supply_miticide_timing int NOT NULL COMMENT '살충제 급수 간격',
	light_up_timing time NOT NULL COMMENT '불켜는 시간',
	light_down_timing time NOT NULL COMMENT '불끄는 시간',
	plant_day date NOT NULL default (current_date) COMMENT '심은 날짜',
	door_lock TINYINT NOT NULL default 1 COMMENT '문 잠김 여부',
	plant_plate TINYINT not null default 0 COMMENT '모종판 유무', 
	PRIMARY KEY (user_id)
);

insert into smart_farm(user_id, crop_species, supply_water_timing, supply_medium_timing, supply_miticide_timing, light_up_timing, light_down_timing)
 value('lsu878','상추',300,300,300,"06:00:00","20:00:00");

insert into monitoring(user_id,monitoring_temperature,monitoring_humidity,monitoring_illuminance,first_water_tank_level,second_water_tank_level,third_water_tank_level) value('lsu878',);