package com.unicom.kefu.service;

import java.awt.Menu;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.unicom.kefu.config.UserAuthentic;
import com.unicom.kefu.kit.BeanKit;
import com.unicom.kefu.kit.HashKit;
import com.unicom.kefu.kit.RetKit;
import com.unicom.kefu.model.vo.AdminNav;
import com.unicom.kefu.model.vo.AdminPrimary;
import com.unicom.kefu.model.vo.AdminRole;
import com.unicom.kefu.model.vo.AdminUser;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;

@Service
public class IndexService {

	/**
	 * 获取 {@link Menu}  如果存在角色 按角色去查询。
	 */
	public List<AdminNav> getMenus(Integer roleId) {
		try {
			List<AdminNav> allMenu = new ArrayList<>();
			if(roleId !=null && roleId!=0) {
				allMenu = Db.use().query("SELECT m.*,rm.role_id as roleId FROM  admin_menu m"
						+ " left join admin_role_menu rm on rm.menu_id = m.id"
						+ " WHERE rm.role_id=? AND m.status=1 ",AdminNav.class ,roleId);
			}else {
				allMenu = Db.use().findAll(Entity.create("admin_menu").set("status", 1),AdminNav.class) ;
			}
			List<AdminNav> navlists = BeanKit.copyBeanList(allMenu, AdminNav.class);
			navlists = navlists.stream().map((AdminNav vo)->{
				vo.setTitle(vo.getName());
				return vo;
			}).collect(Collectors.toList());
			List<AdminNav> menuParents = navlists.stream().filter(e -> e.getPid() == 0).collect(Collectors.toList());
			
			for (AdminNav navVo : menuParents) {
				List<AdminNav> childList = getChild(navVo.getId(), navlists);
				navVo.setChildren(childList);
			}
			return menuParents;
		} catch (Exception e) {
			e.printStackTrace();
			e.getMessage();
			return null;
		}
	}
	private List<AdminNav> getChild(Integer id, List<AdminNav> allMenu) {
		// 子菜单
		List<AdminNav> childList = new ArrayList<AdminNav>();
		for (AdminNav nav : allMenu) {
			if (nav.getPid().equals(id)) {
				childList.add(nav);
			}
		}
		// 递归
		for (AdminNav nav : childList) {
			nav.setChildren(getChild(nav.getId(), allMenu));
		}
		Collections.sort(childList, order());// 排序
		// 如果节点下没有子节点，返回一个空List（递归退出）
		if (childList.size() == 0) {
			return new ArrayList<AdminNav>();
		}
		return childList;
	}
	/*
	 * 排序,根据order排序
	 */
	public Comparator<AdminNav> order() {
		Comparator<AdminNav> comparator = new Comparator<AdminNav>() {
			@Override
			public int compare(AdminNav o1, AdminNav o2) {
				if (o1.getSort() != o2.getSort()) {
					return o1.getSort() - o2.getSort();
				}
				return 0;
			}
		};
		return comparator;
	}
	
	/**
	 * 菜单管理
	 */
	public RetKit addOrUpdateMenu(AdminNav menu) {
	
		try {
			List<AdminNav> isExit = Db.use().findAll(Entity.create("admin_menu").set("name", menu.getName()),AdminNav.class);
			if (menu.getId() == null) {
				if (isExit.size()> 0) {
					return RetKit.fail("已存在此菜单");
				}
				Db.use().insert(
					    Entity.create("admin_menu")
					    .set("name", menu.getName())
					    .set("sort", menu.getSort())
					    .set("status",1)
					    .set("pid", menu.getPid())
					    .set("url", menu.getUrl())
					    .set("icon", menu.getIcon())
					);
			} else {
				if(isExit.size()>0) {
					AdminNav old = isExit.get(0);
					if (!old.getName().equals(menu.getName())) {
						if (isExit != null) {
							return RetKit.fail("已存在此菜单");
						}
					}
					menu.setStatus(old.getStatus());
				}
				Db.use().update(
					    Entity.create("admin_menu")
					    .set("name", menu.getName())
					    .set("sort", menu.getSort())
					    .set("pid", menu.getPid())
					    .set("url", menu.getUrl())
					    .set("icon", menu.getIcon()),
					    Entity.create("admin_menu")
					    .set("id", menu.getId())
				);
			}
			return RetKit.ok();
		} catch (Exception e) {
			return RetKit.fail(e.getMessage());
		}
	}

	public List<AdminNav> getFirstMenus() {
		try {
			return Db.use().findAll(Entity.create("admin_menu").set("pid", 0),AdminNav.class);
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public RetKit delMenu(Integer menuId) {
		try {
			Db.use().del(Entity.create("admin_menu").set("id", menuId));
		} catch (SQLException e) {
			return RetKit.fail(e.getMessage());
		}
		return RetKit.ok();
	}
	
	
	/**
	 * 角色管理
	 */
	public Map<String, Object> getRoles(Integer pn, Integer ps, Integer roleId) {
		try {
			Map<String, Object> result = new HashMap<>();
			List<AdminRole> roles = Db.use().findAll(Entity.create("admin_role").set("status", 1),AdminRole.class);
			Integer count = roles.size();
			roles = roles.stream().skip(ps * pn).limit(ps).collect(Collectors.toList());
			result.put("list", roles);
			result.put("count", count); // 总条数
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return new HashMap<>();
		}
	}
	public RetKit addOrUpdateRole(AdminRole role) {
		try {
			List<AdminRole> isExit = Db.use().findAll(Entity.create().set("name", role.getRoleName()),AdminRole.class);
			if (role.getId() == null) {
				if (isExit.size()> 0) {
					return RetKit.fail("已存在此菜单");
				}
				Db.use().insert(
					    Entity.create("admin_role")
					    .set("role_name", role.getRoleName())
					    .set("role_primary", role.getRolePrimary())
					    .set("role_describe",role.getRoleDescribe())
					    .set("status",1)
					);
			} else {
				if(isExit.size()>0) {
					AdminRole old = isExit.get(0);
					if (!old.getRoleName().equals(role.getRoleName())) {
						if (isExit != null) {
							return RetKit.fail("已存在此菜单");
						}
					}
					role.setStatus(old.getStatus());
				}
				Db.use().update(
					    Entity.create("admin_role")
					    .set("role_name", role.getRoleName())
					    .set("role_primary", role.getRolePrimary())
					    .set("role_describe",role.getRoleDescribe())
					    .set("status",role.getStatus()),
					    Entity.create("admin_role")
					    .set("name", role.getId())
				);
			}
			return RetKit.ok();
		} catch (Exception e) {
			return RetKit.fail(e.getMessage());
		}
	}

	public List<AdminPrimary> getPrimarys(Integer id) {
		try {
			return Db.use().findAll(Entity.create("admin_primary"),AdminPrimary.class);
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
	}
	
	public RetKit delRole(List<Integer> ids) {
		try {
			Db.use().tx(db -> {
				for (Integer id : ids) {
					Db.use().update(
						    Entity.create("admin_role")
						    .set("status", 0),
						    Entity.create("admin_role")
						    .set("id", id)
					);
				}
			});
		} catch (Exception e) {
			return RetKit.fail(e.getMessage());
		}
		return RetKit.ok();
	}

	

	public Map<String, Object> getRoleMenus(Integer pn, Integer ps) {
		try {
			List<AdminRole> roles = Db.use().findAll(Entity.create("admin_role"),AdminRole.class);
			Integer count = roles.size();
			roles = roles.stream().map((AdminRole rv) -> {
				try {
					List<AdminNav> menus = Db.use().query("SELECT m.*,rm.role_id as roleId FROM  admin_menu m"
							+ " left join admin_role_menu rm on rm.menu_id = m.id"
							+ " WHERE rm.role_id=? AND m.status=1 ",AdminNav.class ,rv.getId());
					rv.setMenus(menus);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				return rv;
			}).skip(ps * pn).limit(ps).collect(Collectors.toList());
			Map<String, Object> result = new HashMap<>();
			result.put("list", roles);
			result.put("count", count); // 总条数
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap<>();
		}
	}

	public RetKit addRoleMenus(Integer id, List<AdminNav> menus) {
		try {
			Db.use().tx(db -> {
				// 删除该角色 已经绑定的所有menu
				Db.use().del(Entity.create("admin_role_menu").set("role_id", id));
				List<Integer> menuIds = new ArrayList<>();
				menuIds = getMenuList(menus);
				for (Integer menuId : menuIds) {
					Db.use().insert(Entity.create("admin_role_menu").set("menu_id", menuId).set("role_id", id));
				}
				
			});
		} catch (Exception e) {
			return RetKit.fail();
		}
		return RetKit.ok();
	}
	
	/**
	 * 通过tree 获取出menu_list（非tree）
	 */
	private List<Integer> getMenuList(List<AdminNav> menus) {
		List<Integer> menuIds = new ArrayList<>();
		for (AdminNav men : menus) {
			menuIds.add(men.getId());
			if (men.getChildren().size() > 0) {
				menuIds.addAll(getMenuList(men.getChildren()));
			}
		}
		return menuIds;
	}
	
	/**
	 *     用户管理
	 */
	public Map<String, Object> getUsers(Integer pn, Integer ps,UserAuthentic user) {
		try {
			List<AdminUser> users = Db.use().query("SELECT u.*,r.role_name,r.id as roleId  FROM admin_user u "
					+ " left join admin_user_role ur on ur.user_id = u.id "
					+ " left join admin_role r on r.id = ur.role_id ", AdminUser.class);
			Integer count = users.size();
			users = users.stream().skip(ps * pn).limit(ps).collect(Collectors.toList());
			Map<String, Object> result = new HashMap<>();
			result.put("list", users);
			result.put("count", count); // 总条数
			return result;
		} catch (Exception e) {
			return null;
		}
	}
	public RetKit addOrUpdateUser(AdminUser user) {
		try {
			List<AdminUser> isExit = Db.use().findAll(Entity.create("admin_user").set("user_name", user.getUserName()),AdminUser.class);
			if (user.getId() == null) {
				if (isExit.size()> 0) {
					return RetKit.fail("已存在此菜单");
				}
				
				Db.use().tx(db -> {
					Long userid = Db.use().insertForGeneratedKey(
						    Entity.create("admin_user")
						    .set("user_name", user.getUserName())
						    .set("remark_name", user.getRemarkName())
						    .set("password", HashKit.md5("123456"))
						    .set("status",1)
						);
					
					Db.use().insert(
						    Entity.create("admin_user_role")
						    .set("user_id", userid.intValue())
						    .set("role_id", user.getRoleId())
						);
				});
				
			} else {
				if(isExit.size()>0) {
					AdminUser old = isExit.get(0);
					if (!old.getRoleName().equals(user.getUserName())) {
						if (isExit != null) {
							return RetKit.fail("已存在此用户");
						}
					}
				
					Db.use().tx(db -> {
						Db.use().update(
							    Entity.create("admin_user")
							    .set("user_name", user.getUserName()),
							    Entity.create("admin_user")
							    .set("user_id", user.getId())
							);
						
						Db.use().update(
							    Entity.create("admin_user_role")
							    .set("role_id", user.getRoleId()),
							    Entity.create("admin_user_role")
							    .set("user_id", user.getId())
							);
					});
				}
			}
			return RetKit.ok();
		} catch (Exception e) {
			return RetKit.fail(e.getMessage());
		}
	}

	public RetKit delUser(List<Integer> ids) {
		try {
			if(Db.use().findAll(Entity.create("admin_user")).size()<=1) {
				return RetKit.fail("只剩一个用户无法继续删除！");
			}
			Db.use().tx(db -> {
				for (Integer userId : ids) {
					Db.use().del(
						    Entity.create("admin_user_role")
						    .set("user_id", userId)
						);
					Db.use().del(
						    Entity.create("admin_user")
						    .set("id", userId)
						);
				}
			});	
		} catch (Exception e) {
			return RetKit.fail(e.getMessage());
		}
		return RetKit.ok();
	}
	
}
