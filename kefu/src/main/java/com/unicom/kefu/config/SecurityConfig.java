package com.unicom.kefu.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoderImpl();
	}

	@Bean
	public UserDetailsService getUserDetails() {
		return new UserDetailServiceImpl();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(getUserDetails());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

	@Autowired
	public void configureGlobalSecurity(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(getUserDetails());
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().ignoringAntMatchers("/api/**").ignoringAntMatchers("/websocket/**");// 拦截忽视的路径
		// http.exceptionHandling().authenticationEntryPoint(new
		// CustomAuthenticationEntryPoint("/**"));

		// 只允许一个用户登录
		// http.sessionManagement().maximumSessions(1).expiredUrl("/login");
		// .usernameParameter("userName").passwordParameter("password")

		http.sessionManagement().invalidSessionUrl("/admin/login");
		
//		 http.formLogin().loginPage("/admin/login")
//		 .and()
//		 	.authorizeRequests()
//		 	.antMatchers("/admin/**","/static/**","/js/**","/css/**","/views/**","/layuiadmin/**").permitAll()
//		 	.anyRequest().permitAll()
//		 .and().logout().logoutUrl("/admin/login")
		 	;
		// .antMatchers("/error/**","/login","/csrf","/api/**","/websocket/**","/public/**","/static/**","/META-INF/resources/**",
		// "/allcss/**","/images/**","/appjs/**","/js/**","/lib/**","/validateHasUserName","/validatePwd","/","/static"
		// ,"/loginIn","/messageDefine").permitAll()
		// .anyRequest().authenticated();
		// .antMatchers("/**").authenticated().requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
		// .and().formLogin().loginPage("/login")
		// .and().logout().logoutUrl("/login").logoutSuccessUrl("/login").invalidateHttpSession(true).deleteCookies("JSESSIONID")
		// .and().httpBasic();
		
		http.headers().frameOptions().sameOrigin().httpStrictTransportSecurity().disable();;	
		http.csrf().disable();
		http.cors().and().csrf().disable().authorizeRequests();
//		http.
//			formLogin().loginPage("/admin/login").permitAll()// 定义当需要用户登录时候，转到的登录页面。
//				.failureUrl("/admin/fail")
//			.and()
//				.authorizeRequests() // 定义哪些URL需要被保护、哪些不需要被保护
//				.antMatchers("/api/**","/error/**","/admin/loginAdmin","/admin/**","/static/**","/common/**","/js/**","/css/**","/image/**","/views/**","/layuiadmin/**","/test/**")// 设置可以访问的路径
//				.permitAll()
//				.anyRequest().authenticated() // 任何请求,登录后可以访问
//				.and().logout().logoutUrl("/login").logoutSuccessUrl("/login")//此处可以省略，如果此处和登录路径一样会无线重定向
//				.invalidateHttpSession(true).deleteCookies("JSESSIONID").and().httpBasic();
		
		
		http.authorizeRequests() // 定义哪些URL需要被保护、哪些不需要被保护
		.antMatchers("/api/**","/error/**","/admin/loginAdmin","/admin/**","/static/**","/common/**","/js/**","/css/**","/image/**","/views/**","/layuiadmin/**","/test/**")// 设置可以访问的路径
		.permitAll()
		//其他任何请求
		.anyRequest().authenticated()
		//.and().sessionManagement().invalidSessionUrl("/admin/invalid")
		.and().formLogin().loginPage("/admin/login")
		.defaultSuccessUrl("/index.html").failureUrl("/admin/fail").permitAll()// 定义当需要用户登录时候，转到的登录页面。
		.failureUrl("/admin/fail")
		.and()
		.logout().logoutUrl("/login").logoutSuccessUrl("/login")//此处可以省略，如果此处和登录路径一样会无线重定向
			.invalidateHttpSession(true).deleteCookies("JSESSIONID").and().httpBasic();
	}
}
