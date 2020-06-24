package com.unicom.kefu.config;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.unicom.kefu.kit.HashKit;


/**
 * 
 * @author yhj
 *
 */
public class BCryptPasswordEncoderImpl implements PasswordEncoder {

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return encodedPassword.equals(HashKit.md5(rawPassword.toString()));
	}
	
	@Override
	public String encode(CharSequence rawPassword) {
		return HashKit.md5(rawPassword.toString());
	}

}
