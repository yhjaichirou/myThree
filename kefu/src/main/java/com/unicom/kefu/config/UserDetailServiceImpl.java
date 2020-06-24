package com.unicom.kefu.config;


import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.unicom.kefu.kit.HashKit;
import com.unicom.kefu.model.vo.AdminUser;

import cn.hutool.db.Db;


@Component
@Service(value = "userDetailServiceImpl")
public class UserDetailServiceImpl implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String params) throws UsernameNotFoundException {
		
		String args[]  = params.split(",");
		String userName = args[0];
		String pwd = args[1];

		List<AdminUser> adminUsers = null ;
		try {
			adminUsers = Db.use().query("SELECT u.*,ur.role_id FROM admin_user u "
					+ " left join admin_user_role ur on ur.user_id=u.id "
					+ " WHERE u.user_name =? ",AdminUser.class, userName);
//			adminUsers = Db.use().find(Entity.create("admin_user").set("user_name", userName), AdminUser.class);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new UsernameNotFoundException("数据错误！");
		}
		if(adminUsers != null && adminUsers.size()>0) {
			AdminUser adminUser = adminUsers.get(0);
			UserAuthentic user= new UserAuthentic();
			BeanUtils.copyProperties(adminUser, user);
			 //验证密码
	        if(pwd!=null && !HashKit.md5(pwd).equals(user.getPassword())) {
	        	throw new BadCredentialsException("密码错误！");
	        }
			return user;
		}
		throw new UsernameNotFoundException("用户不存在！");
	}

}
