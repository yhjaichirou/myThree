package com.unicom.kefu;


import java.io.IOException;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.unicom.kefu.constant.LoginConstant;
import com.unicom.kefu.kit.RetKit;
import com.unicom.kefu.model.Token;
import com.unicom.kefu.service.TokenService;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
/**
 * token 拦截器
 * @author admin
 *
 */
@Component
public class TokenInterceptor implements  HandlerInterceptor{
	private Log logger = LogFactory.get();
	private String accessToken = "";
	private String platform = "";// 接入平台类型 1：android 2:IOS 3：iPad 4:PC
	
	@Resource
	private TokenService tokenService;
	
	/**
     * 		在请求处理之前进行调用（Controller方法调用之前）
	 * @throws Exception 
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    	//此处为不需要登录的接口放行
        if ( request.getRequestURI().contains("/api/updateUserInfo") || request.getRequestURI().contains("/api/isAuth") || request.getRequestURI().contains("/api/auth") ||request.getRequestURI().contains("/api/login") || request.getRequestURI().contains("/api/register") 
        	||	request.getRequestURI().contains("/api/repassword")	|| request.getRequestURI().contains("/error") || request.getRequestURI().contains("/static")) {
            return true;
        }
        //权限路径拦截
        //PrintWriter resultWriter = arg1.getOutputStream();
        // TODO: 有时候用PrintWriter 回报 getWriter() has already been called for this response
        //换成ServletOutputStream就OK了
        //response.setContentType("text/html;charset=utf-8");
        ServletOutputStream resultWriter = response.getOutputStream();
        final String headerToken=request.getHeader("token");
        //判断请求信息
        if(null==headerToken||headerToken.trim().equals("")){
        	RetKit rk = RetKit.fail("请求header参数错误！");
            resultWriter.write(JSONObject.toJSONString(rk).getBytes());
            resultWriter.flush();
            resultWriter.close();
            return false;
        }
        //解析Token信息
        try {
            Claims claims = Jwts.parser().setSigningKey(LoginConstant.PlatformKey).parseClaimsJws(headerToken).getBody();
            String tokenUserId=(String)claims.get("userId");
            Long iTokenUserId = Long.parseLong(tokenUserId);
            //根据客户Token查找数据库Token
            Token token = tokenService.getToken(iTokenUserId.intValue());
            //数据库没有Token记录
            if(token == null) {
            	RetKit rk = RetKit.fail("token为空，请先登录！");
                resultWriter.write(JSONObject.toJSONString(rk).getBytes());
                resultWriter.flush();
                resultWriter.close();
                return false;
            }
            //数据库Token与客户Token比较
            if( !headerToken.equals(token.getToken()) ){
            	RetKit rk = RetKit.fail("token错误，请重新登录！");
                resultWriter.write(JSONObject.toJSONString(rk).getBytes());
                resultWriter.flush();
                resultWriter.close();
                return false;
            }
            //判断Token过期
            Date tokenDate= claims.getExpiration();
            int overTime=(int)(new Date().getTime()-tokenDate.getTime())/1000;
            if(overTime>60*60*24*(LoginConstant.Overtime)){ //15天过期
            	RetKit rk = RetKit.fail("token已过期,请重新登录！");
                resultWriter.write(JSONObject.toJSONString(rk).getBytes());
                resultWriter.flush();
                resultWriter.close();
                return false;
            }
 
        } catch (Exception e) {
        	RetKit rk = RetKit.fail("token错误，请先登录！");
            resultWriter.write(JSONObject.toJSONString(rk).getBytes());
            resultWriter.flush();
            resultWriter.close();
            return false;
        }
        //最后才放行
        return true;
    }
 
    /**
     * 请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
//         System.out.println("执行了TestInterceptor的postHandle方法");
    }
 
    /**
     * 在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
//        System.out.println("执行了TestInterceptor的afterCompletion方法");
    }
}
