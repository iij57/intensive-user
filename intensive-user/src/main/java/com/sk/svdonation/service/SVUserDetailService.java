package com.sk.svdonation.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import com.sk.svdonation.entity.SVMemberBaseEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SVUserDetailService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(SVUserDetailService.class);
    private SVMemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("CALL SVUserDetailService.loadUserByUsername {username: " + username);
        
        SVMemberBaseEntity entity;
        try{
            entity = memberService.getSVMemberEntity(username);
        } catch(NoSuchElementException e) {
            throw new UsernameNotFoundException("Invalid username/password.");
        }
        
        List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        authorities.add(new SimpleGrantedAuthority("ROLE_" + entity.getMemberType()));

        return new User(username, entity.getPassword(), authorities);
    }

    public String getEncodedPassword(String rawPassword) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(rawPassword);
	}

}