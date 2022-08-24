package com.smartFarm.project.model.smartFarm;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Data
@Entity
@NoArgsConstructor
@Table(name="user_id")
public class KwangjinVo {
	@Id
	String id;
}
