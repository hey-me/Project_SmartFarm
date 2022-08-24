package com.smartFarm.project.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.smartFarm.project.model.smartFarm.UserRepository;
import com.smartFarm.project.model.smartFarm.UserVo;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new SHA512Encoder();
    }
    
    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String insertedId) throws UsernameNotFoundException {
        UserVo user = userRepository.findById(insertedId).get();
       
        if (user == null) {
            return null;
        }
        
        String pw = user.getUser_password(); //"d404559f602eab6fd602ac7680dacbfaadd13630335e951f097af3900e9de176b6db28512f2e000b9d04fba5133e8b1c6e8df59db3a8ab9d60be4b97cc9e81db"
        String roles = user.getUser_grade(); //"USER"

//        return User.builder()
//              .username(insertedId)
//              .password(pw)
//              .roles(roles)
//             .build();
     
       return new CustomUserDetails(user);
    }
}