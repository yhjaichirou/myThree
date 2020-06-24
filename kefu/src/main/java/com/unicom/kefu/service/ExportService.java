package com.unicom.kefu.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.unicom.kefu.config.UserAuthentic;
import com.unicom.kefu.constant.Constant;
import com.unicom.kefu.kit.MDateUtil;
import com.unicom.kefu.kit.RetKit;
import com.unicom.kefu.kit.StrKit;
import com.unicom.kefu.model.Room;
import com.unicom.kefu.model.WxUserInfo;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;

@Service
public class ExportService {

	@Value("${path.getphoto}")
	private String getphotoPath;

	/**
	 * 用户管理
	 */
	public Map<String, Object> getList(Integer pn, Integer ps, UserAuthentic user, String city, String county,
			String province, String search, Integer auth,Integer type) {
		try {
			String where = "";
			where += StrKit.isBlank(search)? "":" AND info.nick_name like '%"+search+"%'";
			where += type==null? "":" AND info.type="+type;
			List<WxUserInfo> users = Db.use().query("SELECT info.*,r.room_name as roomName,r.room_type as roomType,r.room_status as roomStatus,r.room_code as roomCode,r.room_create_time as roomCreateTime "
					+ " FROM kefu_customer info "
					+ " left join kefu_room r on r.id = info.room_id "
					+ " WHERE 1=1 "+where, WxUserInfo.class);
			Integer count = users.size();
			users = users.stream().map((WxUserInfo info) -> {
				return info;
			}).skip(ps * pn).limit(ps).collect(Collectors.toList());

			Map<String, Object> result = new HashMap<>();
			result.put("list", users);
			result.put("count", count); // 总条数
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public RetKit updateAuth(Integer status, Integer id, String reason) {
		try {
			if (status.equals(Constant.USTATUS_LIVEMaterial) || status.equals(Constant.USTATUS_COM)
					|| status.equals(Constant.USTATUS_LIVEMaterialNo)) {
				Db.use().execute(
						"update handle_WxUserInfo set status=?,reason=?,auth_time=? where status in (4,5,6,7) AND id="
								+ id,
						status, reason == null ? "" : reason, new Date());
				return RetKit.ok(status.equals(Constant.USTATUS_COM) ? "认证已通过"
						: status.equals(Constant.USTATUS_LIVEMaterial) ? "已重置"
								: status.equals(Constant.USTATUS_LIVEMaterialNo) ? "重新认证" : "已通过");
			} else if (status.equals(Constant.USTATUS_LOGOUT)) {
				Db.use().execute("update handle_WxUserInfo set status=? where status in (5,6) AND id=" + id, status);
				return RetKit.ok("已注销");
			}
			return RetKit.fail("不符合条件");
		} catch (SQLException e) {
			e.printStackTrace();
			return RetKit.fail("网络错误！");
		}
	}

	public RetKit setKefu(Integer type, Integer id) {
		try {
			Db.use().execute("update kefu_customer set type="+type+"  where id="+ id);
			return RetKit.ok("已更新");
		} catch (SQLException e) {
			e.printStackTrace();
			return RetKit.fail("网络错误！");
		}
	}

	public RetKit del(Integer id) {
		try {
			Db.use().del(Entity.create("kefu_customer").set("id", id));
			return RetKit.ok("已删除！");
		} catch (SQLException e) {
			e.printStackTrace();
			return RetKit.fail("网络错误！");
		}
	}
	
	/**
	 *  - -------------------房间管理----------------------
	 * @param type 
	 * @param search 
	 */

	public Map<String, Object> getRoomList(Integer pn, Integer ps, UserAuthentic user, String search, Integer type) {
		try {
			String where = "";
			where += StrKit.isBlank(search)? "":" AND room_name like '%"+search+"%'";
			where += type==null? "":" AND room_type="+type;
			List<Room> rooms = Db.use().query("SELECT * FROM kefu_room WHERE 1=1 "+where, Room.class);
			Integer count = rooms.size();
			rooms = rooms.stream().map((Room ro) -> {
				return ro;
			}).skip(ps * pn).limit(ps).collect(Collectors.toList());
			Map<String, Object> result = new HashMap<>();
			result.put("list", rooms);
			result.put("count", count); // 总条数
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public RetKit addOrUpdate(Room room,String fileName) {
		try {
			List<Room> isExit = Db.use().findAll(Entity.create("kefu_room").set("id", room.getRoomName()),Room.class);
			if (room.getId() == null) {
				if (isExit.size()> 0) {
					return RetKit.fail("已房间已经存在！");
				}
				Db.use().insert(
						Entity.create("cat_commodity")
						  .set("room_name", room.getRoomName())
						    .set("room_type",1)
						    .set("room_status",1)
						    .set("room_code",room.getRoomCode())
						    .set("room_create_time",room.getRoomCreateTime())
						    .set("room_video_url",room.getRoomVideoUrl())
						    .set("room_img",room.getRoomImg())
					);
			} else {
//				if(isExit.size()>0) {
//					Room old = isExit.get(0);
//					if (!old.getComName().equals(room.getComName())) {
//						if (isExit != null) {
//							return RetKit.fail("已存在此商品！");
//						}
//					}
//					UploadFile.delFile(getFilePath+old.getComPic());//删除旧图片
//					Db.use().update(
//						    Entity.create("cat_commodity")
//						    .set("com_name", room.getComName())
//						    .set("com_pic",fileName)
//						    .set("com_type",room.getComType())
//						    .set("com_stock",room.getComStock())
//						    .set("com_sales",room.getComSales())
//						    .set("com_integral",room.getComIntegral())
//						    .set("com_shopbi",room.getComShopbi()),
//						    Entity.create("cat_commodity")
//						    .set("name", room.getId())
//					);
//				}
			}
			return RetKit.ok();
		} catch (Exception e) {
			return RetKit.fail(e.getMessage());
		}
	}


}
