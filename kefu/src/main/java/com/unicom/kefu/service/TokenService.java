package com.unicom.kefu.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.unicom.kefu.constant.LoginConstant;
import com.unicom.kefu.model.Token;
import com.unicom.kefu.model.User;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * token 服务
 * 
 * @author Administrator
 *
 */
@Service
public class TokenService {

	@Resource
	private UserService userService;

	/**
	 * 	操作token  
	 * @param userId
	 * @return
	 * @throws SQLException
	 */
	public String operateToKen(Integer userId) throws SQLException {
		//生成Token
		String tokenStr = creatToken(userId, new Date()); 
		
		List<User> user = Db.use().find(Entity.create("kefu_customer").set("id", userId),User.class);
		if(user.size()>0) {//判断用户是否存在
			Token token = getToken(userId);
			if(token!=null) {//有记录
				updateToken(userId,tokenStr);
			}else {
				addToken(userId,tokenStr);
			}
			return tokenStr;
		}
		return null;
	}
	
	/**
	 * 	创建token
	 * @param userId
	 * @param date
	 * @return
	 */
	private String creatToken(Integer userId, Date date) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT") // 设置header
                .setHeaderParam("alg", "HS256").setIssuedAt(date) // 设置签发时间
                .claim("userId",String.valueOf(userId) ) // 设置内容
                .setExpiration(new Date(date.getTime() + 1000 * 60 * 60 * 24 * LoginConstant.Overtime))//设置失效时间  单位 毫秒 （此处设置为 15天）
                .setIssuer("wwl")// 设置签发人
                .signWith(signatureAlgorithm,LoginConstant.PlatformKey ); // 签名，需要算法和key
        String jwt = builder.compact();
        return jwt;
    }
	
	private Long addToken(Integer userId, String token) throws SQLException {
		Long tokenId = Db.use().insertForGeneratedKey(
				Entity.create("kefu_token")
				.set("create_time", new Date())
				.set("user_id", userId)
				.set("token", token));
		return tokenId;
	}

	private void updateToken(Integer userId, String token) throws SQLException {
		Db.use().update(Entity.create("kefu_token").set("token", token)
				.set("create_time", new Date()),
				Entity.create("kefu_token").set("user_id", userId));
	}

	/**
	 * 	删除token
	 * @param userId
	 * @throws SQLException
	 */
	public void deleteToken(Integer userId) throws SQLException {
		Db.use().del(Entity.create("kefu_token").set("user_id", userId));
	}

	public Token getToken(Integer userId) throws SQLException {
		List<Token> tokens = Db.use().findAll(Entity.create("kefu_token").set("user_id", userId), Token.class);
		if (tokens.size() > 0) {
			Token token = tokens.get(0);
			return token;
		}
		return null;
	}
}
