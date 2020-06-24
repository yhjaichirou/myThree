package com.unicom.kefu;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;

/**
 *  线程处理
 * @author admin
 *
 */
@Component
@Service
public class ThreadDoTask {

	private Log log = LogFactory.get();
	
	/**
	 *  购物每日返利
	 */
	public void calcCommodityRetail() {
		Runnable run = () -> {
			try {
				
			} catch (Exception e) {
				log.error("网络异常!");
			}
		};
		ThreadUtil.execute(run);
	}
	

	
}
