package com.unicom.kefu.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.unicom.kefu.config.UserAuthentic;
import com.unicom.kefu.kit.RetKit;
import com.unicom.kefu.service.LoginService;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;


/**
 * 	登陆
 * @author yhj
 */
@Controller
@EnableAutoConfiguration
@RequestMapping("/admin")
public class LoginController {
	private Log log = LogFactory.get();
	@Resource
	private LoginService loginService;

	@Resource(name = "myAuthenticationManager")
	private AuthenticationManager authenticationManager;
	
	/**
	 * 注册   跳转
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("register")
	public String register(HttpServletRequest request, HttpServletResponse response) {
		return "views/user/reg";
	}
	
	/**
	 * 注册
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/goregister")
	public RetKit goregister(HttpServletRequest request, HttpServletResponse response) {
		return RetKit.ok();
	}
	
	/**
	 * 登录 跳转
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/login")
	public String login(HttpServletRequest request, HttpServletResponse response) {
		return "views/login";
	}
	
	@RequestMapping("/")
	public String defaultHome() {
		Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		UserAuthentic user = null;
		try {
			user = (UserAuthentic) obj;
		} catch (Exception e) {
			return "forward:login";
		}
		//UserAuthentic user = (UserAuthentic) obj;
		if (user!=null){
			return "forward:codeAdmin/init";
		}
		return "forward:login";
	}

	@RequestMapping("/fail")
	public String error() {
		return "/error/loginfail";
	}
	


	
	@RequestMapping(value = "/loginIn", method = RequestMethod.POST)
	@ResponseBody
	public RetKit loginAdminIn(HttpServletRequest request, HttpServletResponse response) {

//		String loginIp = IpUtil.getIpAddr(request);
		String userName = request.getParameter("userName");
		String pwd = request.getParameter("password");
		String params = userName+","+pwd;
		
		try {
			UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(userName,params);
			Authentication authentication = authenticationManager.authenticate(authRequest); // 需要验证的对象authRequest ， 去验证authenticate
			SecurityContextHolder.getContext().setAuthentication(authentication);
			HttpSession session = request.getSession();
			session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
			
			if (authentication != null && authentication.isAuthenticated()) {// 判断验证是否成功
				UserAuthentic user = (UserAuthentic) authentication.getPrincipal();

				request.authenticate(response);
				request.getSession().setAttribute("loginUser", user);
//				loginService.createLoginLog(user.getId(), loginIp);
				log.info("用户'"+user.getUserName()+"'与"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+"登录！");
				
				return RetKit.ok().set("returnUrl", "/index");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return RetKit.fail(e.getMessage());
		}
		return RetKit.fail("登录失败!");
	}


//	@RequestMapping("/validateHasUserName")
//	@ResponseBody
//	public Boolean validateHasUserName(HttpServletRequest request) {
//		String userName = request.getParameter("userName");
//		return loginService.hasUserName(userName);
//	}
//
//	@RequestMapping("/validatePwd")
//	@ResponseBody
//	public Boolean validatePwd(HttpServletRequest request) {
//		String userName = request.getParameter("userName");
//		String pwd = request.getParameter("password");
//		return loginService.validatePwdAndName(userName, pwd);
//	}
//
//	@RequestMapping("/updataPwd")
//	@ResponseBody
//	public RetKit updataPwd(HttpServletRequest request) {
//		UserAuthentic user = (UserAuthentic) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		String params = request.getParameter("params");
//		JSONObject m = JSONObject.parseObject(params);
//		String oldP = m.getString("oldP");
//		String newP = m.getString("newP");
//		RetKit retKit = loginService.updataPwd(user.getId(), oldP, newP);
//		if (retKit.success()) {
//			// 清除session
//			Enumeration<String> em = request.getSession().getAttributeNames();
//			while (em.hasMoreElements()) {
//				request.getSession().removeAttribute(em.nextElement().toString());
//			}
//			request.getSession().removeAttribute(LoginConstant.loginUserCacheName);
//			request.getSession().invalidate();
//			return RetKit.ok();
//		} else {
//			return retKit;
//		}
//	}
//	
//	/**
//	 * 忘记密码验证
//	 */
//	@RequestMapping("/seniorValidate")
//	public String logout(HttpServletRequest request) {
//		return "/views/user/forget";
//	}
//	
//	
	/**
	 * 退出登录
	 */
	@RequestMapping("/logout")
	@ResponseBody
	public RetKit logout(HttpServletRequest request, HttpServletResponse response) {
		System.out.println(request.getSession());
		
		// 清除session
		Enumeration<String> em = request.getSession().getAttributeNames();
		while (em.hasMoreElements()) {
			request.getSession().removeAttribute(em.nextElement().toString());
		}
		request.getSession().removeAttribute("loginUser");
		request.getSession().invalidate();
//		Cookie[] cookies = request.getCookies();
//		if (cookies != null) {
//			for (Cookie cookie : cookies) {
//				// 如果找到同名cookie，就将value设置为null，将存活时间设置为0，再替换掉原cookie，这样就相当于删除了。
//				if (cookie.getName().equals(LoginConstant.loginUserCacheName)) {
//					cookie.setValue(null);
//					cookie.setMaxAge(0);
//					cookie.setPath(request.getContextPath());
//					response.addCookie(cookie);
//					break;
//				}
//			}
//		}
		return RetKit.ok();
	}
}
