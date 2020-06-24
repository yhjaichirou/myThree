package com.unicom.kefu.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.unicom.kefu.config.UserAuthentic;
import com.unicom.kefu.kit.RetKit;
import com.unicom.kefu.model.vo.AdminNav;
import com.unicom.kefu.model.vo.AdminPrimary;
import com.unicom.kefu.model.vo.AdminRole;
import com.unicom.kefu.model.vo.AdminUser;
import com.unicom.kefu.service.IndexService;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
/**
 * 	系统模块
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/index")
@CrossOrigin
public class IndexController {

	@Resource
	private IndexService indexService;
	
	@RequestMapping("init")
	public String initView(HttpServletRequest  request,HttpServletResponse response) {
		UserAuthentic user = (UserAuthentic)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<AdminNav> menus = indexService.getMenus(user.getRoleId());
		request.setAttribute("navList", menus);
		return "/views/index";
	}
	@RequestMapping("home")
	public String home(HttpServletRequest  request,HttpServletResponse response) {
		return "/views/home";
	}
	@RequestMapping("menu")
	public String menu(HttpServletRequest  request,HttpServletResponse response) {
		//默认获取超管列表
		List<AdminNav> menus = indexService.getMenus(0);
		request.setAttribute("allMenus", menus);
		return "/views/myweb/menu";
	}
	
	@RequestMapping("role")
	public String role(HttpServletRequest  request,HttpServletResponse response) {
		return "/views/myweb/roleIndex";
	}
	@RequestMapping("rolePrimary")
	public String rolePrimary(HttpServletRequest  request,HttpServletResponse response) {
		return "/views/myweb/rolePrimary";
	}
	@RequestMapping("roleMenu")
	public String roleMenu(HttpServletRequest  request,HttpServletResponse response) {
		List<AdminNav> menus = indexService.getMenus(0);
		request.setAttribute("allMenus", menus);
		return "/views/myweb/roleMenu";
	}
	
	@RequestMapping("user")
	public String user(HttpServletRequest request ,HttpServletResponse response) {
		return "/views/myweb/user";
	}
	
	/**
	 *    菜单管理
	 */
	@RequestMapping("menu/addorupdate")
	@ResponseBody
	public RetKit addOrUpdateMenu(String name,String icon ,String url , Integer pid,Integer id,Integer sort) {
		AdminNav menu = new AdminNav();
		if(id!=null) {
			menu.setId(id);
		}
		menu.setIcon(icon);
		menu.setName(name);
		menu.setUrl(url);
		menu.setPid(pid);
		menu.setSort(sort);
		return indexService.addOrUpdateMenu(menu);
	}
	
	@RequestMapping("menu/del")
	@ResponseBody
	public RetKit delMenu(Integer id) {
		return indexService.delMenu(id);
	}
	
	
	/**
	 *   角色管理  ：权限分配
	 */
	
	@RequestMapping("role/getRoles")
	@ResponseBody
	public RetKit getRoles(@PathParam("limit")Integer limit,@PathParam("page")Integer page,@PathParam("roleId")Integer roleId) {
		Map<String, Object> map = indexService.getRoles(page-1,limit,roleId);
		return RetKit.ok_table_data(map.get("list"),(Integer)map.get("count"));
	}
	
	@RequestMapping("role/getPrimarys")
	@ResponseBody
	public List<AdminPrimary> getPrimarys(Integer id) {
		return indexService.getPrimarys(id);
	}
	@RequestMapping("role/addorupdate")
	@ResponseBody
	public RetKit addOrUpdateRole(AdminRole role) {
		return indexService.addOrUpdateRole(role);
	}
	
	@RequestMapping("role/del")
	@ResponseBody
	public RetKit delRole(HttpServletRequest request) {
		String _ids = request.getParameter("ids");
		List<Integer> ids = JSONObject.parseArray(_ids, Integer.class); 
		return indexService.delRole(ids);
	}
	
	/**
	 *   角色管理  ：菜单分配
	 */
	@RequestMapping("role/getMenus")
	@ResponseBody
	public RetKit getRoleMenus(@PathParam("limit")Integer limit,@PathParam("page")Integer page) {
		Map<String, Object> map = indexService.getRoleMenus(page-1,limit);
		return RetKit.ok_table_data(map.get("list"),(Integer)map.get("count"));
	}
	@RequestMapping("role/getMenusTree")
	@ResponseBody
	public Map<String,Object> getMenusTree(Integer id) {
		List<AdminNav> menus = indexService.getMenus(0);
		List<AdminNav> menuFirst = indexService.getFirstMenus();
		List<Integer> firstMenusId = new ArrayList<>();
		for (AdminNav me : menuFirst) {
			firstMenusId.add(me.getId());
		}
		try {
			List<AdminNav> bangmenus = Db.use().query("SELECT rm.menu_id as menuId ,rm.role_id as roleId ,m.name as menuName,m.pid ,m.url FROM admin_role_menu rm " + 
					" JOIN admin_menu m on m.id= rm.menu_id " + 
					" WHERE rm.role_id=?",AdminNav.class ,id);
			bangmenus = bangmenus.stream().filter((AdminNav an)->(!firstMenusId.contains(an.getMenuId()))).collect(Collectors.toList());
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("allMenus", menus);
			map.put("bangMenus",bangmenus);
			return map;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	@RequestMapping("role/addRoleMenus")
	@ResponseBody
	public RetKit addRoleMenus(HttpServletRequest request,Integer id) {
		String _menuIds = request.getParameter("menuIds");
		List<AdminNav> menuIds = JSONObject.parseArray(_menuIds, AdminNav.class); 
		return indexService.addRoleMenus(id,menuIds);
	}
	
	/**
	 *    超管后台 用户管理
	 */
	@RequestMapping("user/getUsers")
	@ResponseBody
	public RetKit getUsers(@PathParam("limit")Integer limit,@PathParam("page")Integer page) {
		UserAuthentic user = (UserAuthentic)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Map<String, Object> map = indexService.getUsers(page-1,limit,user);
		return RetKit.ok_table_data(map.get("list"),(Integer)map.get("count"));
	}
	@RequestMapping("user/roles")
	@ResponseBody
	public List<AdminRole> getAdminRoles() {
		try {
			return Db.use().findAll(Entity.create("admin_role"),AdminRole.class);
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	@RequestMapping("user/addorupdate")
	@ResponseBody
	public RetKit addOrUpdateUser(AdminUser pr) {
		return indexService.addOrUpdateUser(pr);
	}
	
	@RequestMapping("user/del")
	@ResponseBody
	public RetKit delUser(HttpServletRequest request) {
		UserAuthentic user = (UserAuthentic)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String _ids = request.getParameter("ids");
		List<Integer> ids = JSONObject.parseArray(_ids, Integer.class); 
		if(ids.contains(user.getId())) {
			RetKit.fail("当前用户无法删除！");
		}
		return indexService.delUser(ids);
	}
	

	
}
