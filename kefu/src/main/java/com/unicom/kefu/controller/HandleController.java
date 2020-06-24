package com.unicom.kefu.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.unicom.kefu.config.UserAuthentic;
import com.unicom.kefu.kit.RetKit;
import com.unicom.kefu.kit.StrKit;
import com.unicom.kefu.kit.UploadFile;
import com.unicom.kefu.model.Room;
import com.unicom.kefu.service.ExportService;


@Controller
@RequestMapping("/export")
@CrossOrigin
public class HandleController {

	@Value("${path.photo}")
	private String photoPath;
	@Resource
	private ExportService service;

	@RequestMapping("auth")
	public String download(HttpServletRequest request, HttpServletResponse response) {
		return "/views/export/auth";
	}
	
	@RequestMapping("room")
	public String room(HttpServletRequest request, HttpServletResponse response) {
		return "/views/export/room";
	}


	/**
	 * 超管后台 用户管理
	 */
	@RequestMapping("auth/getList")
	@ResponseBody
	public RetKit getList(@PathParam("city") String city, @PathParam("county") String county,
			@PathParam("province") String province, @PathParam("search") String search, @PathParam("auth") Integer auth,@PathParam("type") Integer type,
			@PathParam("limit") Integer limit, @PathParam("page") Integer page) {
		UserAuthentic user = (UserAuthentic) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Map<String, Object> map = service.getList(page - 1, limit, user, city, county, province, search, auth,type);
		return RetKit.ok_table_data(map.get("list"), (Integer) map.get("count"));
	}

	/**
	 * 超管后台 用户管理
	 */
	@RequestMapping("auth/updateAuth")
	@ResponseBody
	public RetKit updateAuth(Integer isPass, Integer id, String reason) {
		return service.updateAuth(isPass, id, reason);
	}
	
	/**
	 * 超管后台 用户管理
	 */
	@RequestMapping("auth/setKefu")
	@ResponseBody
	public RetKit setKefu(Integer status, Integer id) {
		return service.setKefu(status, id);
	}
	
	/**
	 * 超管后台 删除
	 */
	@RequestMapping("auth/del")
	@ResponseBody
	public RetKit del(Integer id) {
		return service.del(id);
	}
	
	
	/**
	 *  - -------------------房间管理----------------------
	 */

	/**
	 * 超管后台 房间
	 */
	@RequestMapping("room/getRoomList")
	@ResponseBody
	public RetKit getRoomList(@PathParam("limit") Integer limit, @PathParam("page") Integer page,
			@PathParam("search") String search,@PathParam("type") Integer type) {
		UserAuthentic user = (UserAuthentic) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Map<String, Object> map = service.getRoomList(page - 1, limit, user,search,type);
		return RetKit.ok_table_data(map.get("list"), (Integer) map.get("count"));
	}

	/**
	 * 超管后台 用户管理
	 * @throws Exception 
	 */
	@RequestMapping("auth/addOrUpdate")
	@ResponseBody
	public RetKit addOrUpdate(Room room,MultipartFile file) throws Exception {
		String fileName = UploadFile.upload(photoPath, file);
		return service.addOrUpdate(room ,fileName);
	}
	
//	/**
//	 * 超管后台 用户管理
//	 */
//	@RequestMapping("auth/updateAuth")
//	@ResponseBody
//	public RetKit updateAuth(Integer isPass, Integer id, String reason) {
//		return service.updateAuth(isPass, id, reason);
//	}
	
}