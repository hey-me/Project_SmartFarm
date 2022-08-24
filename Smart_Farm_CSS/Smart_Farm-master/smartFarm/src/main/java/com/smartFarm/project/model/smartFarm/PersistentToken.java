package com.smartFarm.project.model.smartFarm;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity(name = "PersistentLogins")
public class PersistentToken {

	@Id
	@GeneratedValue
    private String series;
    private String username;
    private String token;
    private Date lastUsed;
}
