package com.smartFarm.project.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.smartFarm.project.security.LoginFailHandler;
import com.smartFarm.project.security.LoginSuccessHandler;
import com.smartFarm.project.security.SessionFilter;
import com.smartFarm.project.security.UserDetailServiceImpl;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailServiceImpl cus;
   
    @Autowired
    SessionFilter sessionFilter;
    
    @Autowired
    DataSource dataSource;
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
        http
        .authorizeRequests()
        .antMatchers("/home","/explanation","/photo","/teamRole","/shamePoint","/arduino/","/app/monitoring").permitAll() 
            .antMatchers("/admin").hasAuthority("admin")
            .anyRequest().authenticated()
        .and()
            .formLogin()
            .loginPage("/home?error=false&exception=login")
            .loginProcessingUrl("/loginProc")
            .usernameParameter("user_id")
            .passwordParameter("user_password")
            
            .successHandler(loginSuccessHandler())
            .failureHandler(loginFailHandler())//로그인 실패 시 처리하는 핸들러 등록.
            .permitAll()
        .and()
            .logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/logoutProc"))
            .logoutSuccessUrl("/home")
        .and()
       		.sessionManagement()
        	.maximumSessions(1) //최대세션을 1로설정
        	.maxSessionsPreventsLogin(false)  // 중복로그인시 이전 로그인했던 세션 만료. true시 이전 세션 유지
        	.expiredUrl("/home?error=false&exception=logout");
        
       http.addFilterAfter(sessionFilter, UsernamePasswordAuthenticationFilter.class);
    
      

	}	
	
	
	public LoginSuccessHandler loginSuccessHandler() {
	return new LoginSuccessHandler();
	}

	
	public LoginFailHandler loginFailHandler(){
	    return new LoginFailHandler();
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception { //정적자원들은 시큐리티에서 예외
	    web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
	}
	
	@Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception { //커스텀 유저디테일 설정
        auth.userDetailsService(cus);
    }
   
}