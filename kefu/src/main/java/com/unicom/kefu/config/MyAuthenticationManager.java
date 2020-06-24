package com.unicom.kefu.config;


import javax.annotation.Resource;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


@Component("myAuthenticationManager")
public class MyAuthenticationManager implements AuthenticationManager {
	
	@Resource(name="userDetailServiceImpl")
	private  UserDetailsService userDetailsService;  
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
		String username = token.getName(); 
		String params = token.getCredentials().toString();

		//从数据库找到的用户
		UserDetails userDetails = null;
        if(username != null) {
        	userDetails = this.userDetailsService.loadUserByUsername(params);
        }
        if(userDetails == null) {
        	throw new UsernameNotFoundException("用户名不存在！");
        }
//        String pwd  = userDetails.getPassword();
//        //验证
//        if(pwd!=null && !pwd.equals(HashKit.md5(token.getCredentials().toString()))) {
//        	throw new BadCredentialsException("密码错误！");
//        }
		return new UsernamePasswordAuthenticationToken(userDetails,userDetails.getPassword(),userDetails.getAuthorities());
	}

}
