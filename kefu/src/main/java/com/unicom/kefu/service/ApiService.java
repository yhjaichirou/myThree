package com.unicom.kefu.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.unicom.kefu.constant.URLConstant;
import com.unicom.kefu.kit.RetKit;
import com.unicom.kefu.kit.StrKit;
import com.unicom.kefu.kit.WxResult;
import com.unicom.kefu.model.Room;
import com.unicom.kefu.model.WxUserInfo;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.http.HttpUtil;

@Service
public class ApiService {

	@Value("${appid}")
	private String appid;
	@Value("${secret}")
	private String secret;
	@Value("${path.getphoto}")
	private String getphoto;

	@Resource
	private TokenService tokenService;
	
//	@Autowired
//	private CacheManager cacheManager;

	/**
	 * ---------------------------------微信服务接口---------------------------------
	 */
	public RetKit getAccessToken() {
		try {
			HashMap<String, Object> paramMap = new HashMap<>();
			paramMap.put("appid", appid);
			paramMap.put("grant_type", "client_credential");
			paramMap.put("secret", secret);
			String result3 = HttpUtil.get(URLConstant.URL_GET_TOKKEN, paramMap);
			WxResult appuser = JSONObject.parseObject(result3, WxResult.class);
//			Cache tokenCache = cacheManager.getCache("accessToken");
//			tokenCache.put(openid+"_token", appuser.getAccessToken());

			if (appuser.getErrcode() != null && appuser.getErrcode() != 0) {
				return RetKit.fail(appuser.getErrmsg());
			}
			return RetKit.okData(appuser.getAccess_token());
		} catch (Exception e) {
			e.printStackTrace();
			return RetKit.fail("网络错误！");
		}
	}
	
	public RetKit addWxUserInfo(WxUserInfo userinfo, String openid) {
		try {
			List<WxUserInfo> us = Db.use().find(Entity.create("kefu_customer").set("openid", openid),
					WxUserInfo.class);
			Integer userid = 0;
			if (us.size() <= 0) {
				// 创建
				Long id = Db.use().insertForGeneratedKey(Entity.create("kefu_customer").set("nick_name", userinfo.getNickName())
						.set("openid", openid).set("gender", userinfo.getGender())
						.set("type", 1)
						.set("avatar_url", userinfo.getAvatarUrl()));
				userid = id.intValue();
			}else {
				userid = us.get(0).getId();
			}
			String tokenStr = tokenService.operateToKen(userid);
			if(StrKit.isBlank(tokenStr)) {
				return RetKit.fail("获取token失败！");
			}
			return RetKit.okData(tokenStr);
		} catch (SQLException e) {
			e.printStackTrace();
			return RetKit.fail("网络错误！");
		}
	}


	/**
	 * ---------------------------------业务---------------------------------
	 */

	public RetKit getUserDetail(String openid) {
		try {
//			List<UserInfo> us = Db.use().find(Entity.create("handle_userinfo").set("openid", openid),UserInfo.class);
			List<WxUserInfo> us = Db.use().query("SELECT info.*,r.room_name as room_name,r.room_type as roomType,r.room_status as roomStatus,r.room_code as roomCode,r.room_create_time as roomCreateTime "
					+ " FROM kefu_customer info "
					+ " left join kefu_room r on r.id = info.room_id "
					+ " WHERE info.openid=?", WxUserInfo.class, openid);
			
			return us.size()>0 ?RetKit.okData(us.get(0)): RetKit.fail("用户不存在！");
		} catch (SQLException e) {
			e.printStackTrace();
			return RetKit.fail("网络错误！");
		}
	}
	
	public RetKit getRoomList() {
		try {
			List<Room> rooms = Db.use().findAll(Entity.create("kefu_room"),Room.class);
			rooms = rooms.stream().map((Room r )->{
				r.setRoomImg(getphoto+r.getRoomImg());
				return r;
			}).collect(Collectors.toList());
			return RetKit.okData(rooms);
		} catch (SQLException e) {
			e.printStackTrace();
			return RetKit.fail("网络错误！");
		}
	}

}
