package com.unicom.kefu.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class LoginService {

//	public Boolean hasUserName(String AdminUserName) {
//		Boolean flag = new Boolean(false);
//		AdminUser AdminUser = AdminUserRepository.findAdminUserByName(AdminUserName);
//		if(AdminUser != null) {
//			flag = true;
//		}
//		return flag;
//	}
//
//	public Boolean validatePwdAndName(String AdminUserName, String pwd) {
//		Boolean flag = new Boolean(false);
//		AdminUser AdminUser = AdminUserRepository.findAdminUserByName(AdminUserName);
//		if(AdminUser != null) {
//			String hashedPass = HashKit.md5(pwd);
//			if (AdminUser.getPwd().equals(hashedPass)) {
//				//验证通过
//				flag = true;
//			}
//		}
//		return flag;
//	}
//
//	public RetKit login(String AdminUserName, String password, boolean remember, String loginIp) {
//		
//		try {
//			AdminUserName = AdminUserName.toLowerCase().trim();
//			password = password.trim();
//			AdminUser loginAdminUser = AdminUserRepository.findAdminUserByName(AdminUserName);
//			if (loginAdminUser == null) {
//				return RetKit.fail("用户名或密码不正确");
//			}
//			String hashedPass = HashKit.md5(password);
//			// 未通过密码验证
//			if (loginAdminUser.getPwd().equals(hashedPass) == false) {
//				return RetKit.fail("用户名或密码不正确");
//			}
//			
////			Session session = new Session();
////			String sessionId = StrKit.getRandomUUID();
////			session.setId(sessionId);
////			session.setAdminUserId(loginAdminUser.getId());
////			if (sessionRepository.save(session) != null) {
////				return RetKit.fail("保存 session 到数据库失败，请联系管理员");
////			}
//			return RetKit.ok();
//		} catch (Exception e) {
//			e.printStackTrace();
//			return RetKit.fail();
//		}
//	}
//
//	public void createLoginLog(Integer AdminUserId, String loginIp) {
////		try {
////			LoginLog log = new LoginLog();
////			log.setAdminUserId(AdminUserId);
////			log.setIp(loginIp);
////			log.setLoginAt(new Date());
////			logRepository.save(log);
////		} catch (Exception e) {
////			e.printStackTrace();
////			throw new RuntimeException(MessageUtil.loadMessage("loggerError"));
////		}
//	}
//	
//	public RetKit updataPwd(Integer id,String oldP,String newP) {
//		Optional<AdminUser> AdminUser0 = AdminUserRepository.findById(id);
//		AdminUser AdminUser = AdminUser0.get();
//		if(HashKit.md5(oldP).equals(AdminUser.getPwd())) {
//			AdminUser.setPwd(HashKit.md5(newP));
//			AdminUserRepository.save(AdminUser);
//		}else{
//			return RetKit.fail("旧密码输出错误");
//		}
//		return RetKit.ok();
//	}
//
//	public boolean isLoginIn(Integer AdminUserId, Integer roleId) {
//		// TODO Auto-generated method stub
//		return false;
//	}


}
