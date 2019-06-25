package com.sk.svdonation.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	private RestAuthenticationEntryPoint restAuthenticationEntryPoint;
	private SVAuthenticationSuccessHandler mySuccessHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.authorizeRequests()
				.and()
				.exceptionHandling()
				.authenticationEntryPoint(restAuthenticationEntryPoint)
				.and()
				.authorizeRequests()
				// .antMatchers("/v1/members/**").authenticated()
				.anyRequest().permitAll()
				.and()
				.formLogin()
				.loginProcessingUrl("/v1/login")
				.successHandler(mySuccessHandler)
				// .failureHandler(myFailureHandler)
				.and()
				.httpBasic()
				.and()
				.logout()
				.logoutUrl("/v1/logout");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		//return PasswordEncoderFactories.createDelegatingPasswordEncoder();
		//return new MessageDigestPasswordEncoder("MD5");
		return new BCryptPasswordEncoder();
	}	
}
