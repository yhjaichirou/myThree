package com.unicom.kefu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 	资源配置
 * @author admin
 *
 */
@Configuration
public class MyApiConfig implements WebMvcConfigurer{
	  @Autowired
	  private TokenInterceptor tokenInterceptor;
	  
	  /**
	   * 针对异步的拦截器配置，拦截异步请求
	   * @param configurer
	  */  
	  @Override
	  public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		  WebMvcConfigurer.super.configureAsyncSupport(configurer);
	        //比如如下给异步服务请求添加拦截器
	        //configurer.registerCallableInterceptors((CallableProcessingInterceptor) timeInterceptor);
	  }
	    
	  @Override
	  public void addInterceptors(InterceptorRegistry registry) {
	      //注册tokenInterceptor拦截器
		  InterceptorRegistration ir = registry.addInterceptor(tokenInterceptor);
		  //配置拦截路径
		  ir.addPathPatterns("/api/**");
		  //配置不拦截路径
		//  ir.excludePathPatterns("/","/admin_login","/login");  
	  }
	 	
	  @Override
	  public void addResourceHandlers(ResourceHandlerRegistry registry) {
			registry.addResourceHandler("/layuiadmin/**")
			.addResourceLocations("classpath:/static/layuiadmin/");
			registry.addResourceHandler("/common/*.js")
			.addResourceLocations("classpath:/static/common/");
			registry.addResourceHandler("/image/**")
			.addResourceLocations("classpath:/static/image/");
//			.setCacheControl(CacheControl.maxAge(timeDay, timeUnit).cachePublic());
	  }
}
