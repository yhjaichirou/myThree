package com.unicom.kefu.model;

import java.util.Date;

/**
 * 客服 - 摄像头
 * @author admin
 *
 */
public class Camera {

	private Integer id;
	private String cameraCode;
	private Date cameraTime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCameraCode() {
		return cameraCode;
	}
	public void setCameraCode(String cameraCode) {
		this.cameraCode = cameraCode;
	}
	public Date getCameraTime() {
		return cameraTime;
	}
	public void setCameraTime(Date cameraTime) {
		this.cameraTime = cameraTime;
	}
	
	
}
