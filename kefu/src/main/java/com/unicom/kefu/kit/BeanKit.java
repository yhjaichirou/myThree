package com.unicom.kefu.kit;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.BeanUtils;

import cn.hutool.core.date.DateUtil;

public class BeanKit {
	// 拷贝bean列表
	public static <T> List<T> copyBeanList(List<?> sourceList, Class<T> targetObj) {
		List<T> rs = new ArrayList<T>();
		if (sourceList == null || sourceList.size() == 0) {
			return rs;
		}
		for (Object object : sourceList) {
			if (object != null) {
				T target;
				try {
					target = targetObj.newInstance();
					BeanUtils.copyProperties(object, target);
					rs.add(target);
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return rs;
	}

	// 将集合中的Map转换成bean

	public static <T> List<T> changeToListBean(List<Map<String, Object>> list, Class<T> bean)
			throws IllegalArgumentException, IllegalAccessException, InstantiationException {
		List<T> newList = new ArrayList<T>();
		for (Map<String, Object> map : list) {
			// 生成对象实例
			T obj = bean.newInstance();
			String[] properties = map.keySet().toArray(new String[map.keySet().size()]);
			Field[] fields = bean.getDeclaredFields();
			// 给属性赋值
			for (String property : properties) {
				for (Field field : fields) {
					if (property.equals(field.getName())) {
						field.setAccessible(true);
						field.set(obj, map.get(property));
					}
				}
			}
			newList.add(obj);
		}
		return newList;
	}

	// 将record转成bean
	public static <T> T changeRecordToBean(Map<String, Object> record, Class<T> bean)
			throws IllegalArgumentException, IllegalAccessException, InstantiationException {
		String[] columNames = record.keySet().toArray(new String[record.keySet().size()]);
		T obj = bean.newInstance();
		for (String name : columNames) {
			for (Field field : bean.getDeclaredFields()) {
				if (name.equals(field.getName())) {
					field.setAccessible(true);
					field.set(obj, record.get(name));
				}
			}
		}
		return obj;
	}

	/**
	 * 将Object对象里面的属性和值转化成Map对象
	 *
	 * @param obj
	 * @return
	 * @throws IllegalAccessException
	 */
	public static Map<String, Object> objectToMap(Object obj) throws IllegalAccessException {
		Map<String, Object> map = new HashMap<String, Object>();
		Class<?> clazz = obj.getClass();
		for (Field field : clazz.getDeclaredFields()) {
			field.setAccessible(true);
			String fieldName = field.getName();
//	            Object value = StringUtils.nvl(field.get(obj));
			map.put(fieldName, field.get(obj));
		}
		return map;
	}

//	/**
//	 * 将Object对象里面的属性和值转化成Map对象
//	 *     未实现
//	 * @throws IllegalAccessException
//	 * @throws IllegalArgumentException
//	 */
//	public static List<Map<String, Object>> listToListMap(List<Object> sourceList)
//			throws IllegalArgumentException, IllegalAccessException {
//		List<Map<String, Object>> result = new ArrayList<>();
//		if (sourceList == null || sourceList.size() == 0) {
//			return result;
//		}
//		for (Object object : sourceList) {
//			Class<?> clazz = object.getClass();
//			Map<String, Object> map = new LinkedHashMap<>();
//			for (Field field : clazz.getDeclaredFields()) {
//				map.put(field.getName(), field.get(object));
//			}
//			result.add(map);
//		}
//		return result;
//	}

	/**
	 * 将List对象转化成List<Map>对象
	 *
	 * @param obj
	 * @return
	 * @throws IllegalAccessException
	 */
	public static <T> List<Map<Object, Object>> listToListMap(List<T> list) {
		List<Map<Object, Object>> l = new LinkedList<>();
		try {
			for (T t : list) {
				Map<Object, Object> map = new HashMap<>();
				Method[] methods = t.getClass().getMethods();
				for (Method method : methods) {
					if (method.getName().startsWith("get")) {
						String name = method.getName().substring(3);
						name = name.substring(0, 1).toLowerCase() + name.substring(1);
						Object value = method.invoke(t);
						if (method.getGenericReturnType().toString().equals("class java.util.Date") && value != null) {
							value = DateUtil.format((Date) value, "yyyy-MM-dd HH:mm:ss");
						}
						map.put(name, value);
					}
				}
				l.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return l;
	}
}
