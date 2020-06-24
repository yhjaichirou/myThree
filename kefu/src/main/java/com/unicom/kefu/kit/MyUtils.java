package com.unicom.kefu.kit;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 *  零碎的 工具集
 * @author Administrator
 *
 */
public class MyUtils {

	//list <map > 去重.filter(MyUtils.distinctByKey(MemberJoinVo::getUserid)).collect(Collectors.toList());
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object,Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
}
