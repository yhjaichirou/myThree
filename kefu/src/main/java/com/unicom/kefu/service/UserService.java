package com.unicom.kefu.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.unicom.kefu.kit.RetKit;
import com.unicom.kefu.kit.StrKit;
import com.unicom.kefu.model.User;
import com.unicom.kefu.model.WxUserInfo;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;

/**
 * 	用户管理
 * @author Administrator
 *
 */
@Service
public class UserService {

	@Value("${path.getphoto}")
	private String getphoto;
	
	/**
	 * 
	 * @param pn
	 * @param ps
	 * @param account 用户名
	 * @param recommendUser 推荐人
	 * @param isAuth 是否认证
	 * @param isEnabled 是否激活
	 * @param isLock 是否锁定
	 * @param level 级别 （  普通 vip 至尊 ）
	 * @return
	 */
	public Map<String, Object> getUserList(Integer pn, Integer ps, String account, String recommendUser, 
			Integer isAuth, Integer isEnabled, Integer isLock, Integer level) {
		try {
			String where = "";
			where += com.unicom.kefu.kit.StrKit.notBlank(account)?" AND account = "+account : "";
			where += StrKit.notBlank(recommendUser)?" AND account = "+account : "";
			where += isAuth!=null?" AND is_auth = "+isAuth : "";
			where += isEnabled!=null?" AND is_enabled = "+isEnabled : "";
			where += isLock!=null?" AND is_lock = "+isLock : "";
			where += level!=null?" AND level = "+level : "";
			Map<String, Object> result = new HashMap<>();
			List<WxUserInfo> users = Db.use().query("SELECT * FROM kefu_customer WHERE 1=1 "+where, WxUserInfo.class);
			Integer count = users.size();
			users = users.stream().skip(ps * pn).limit(ps).collect(Collectors.toList());
			result.put("list", users);
			result.put("count", count); // 总条数
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return new HashMap<>();
		}
	}

	
}
