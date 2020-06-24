package com.unicom.kefu.model;

import java.util.Date;

/**
 * 房间号
 * @author admin
 *
 */
public class Room {

	private Integer id;
	private String roomName;
	private String roomCode;
	private Integer roomType;
	private Integer roomStatus;
	private Date roomCreateTime;
	private String roomImg;
	private String roomVideoUrl;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public String getRoomCode() {
		return roomCode;
	}
	public void setRoomCode(String roomCode) {
		this.roomCode = roomCode;
	}
	public Integer getRoomType() {
		return roomType;
	}
	public void setRoomType(Integer roomType) {
		this.roomType = roomType;
	}
	public Integer getRoomStatus() {
		return roomStatus;
	}
	public void setRoomStatus(Integer roomStatus) {
		this.roomStatus = roomStatus;
	}
	public Date getRoomCreateTime() {
		return roomCreateTime;
	}
	public void setRoomCreateTime(Date roomCreateTime) {
		this.roomCreateTime = roomCreateTime;
	}
	public String getRoomImg() {
		return roomImg;
	}
	public void setRoomImg(String roomImg) {
		this.roomImg = roomImg;
	}
	public String getRoomVideoUrl() {
		return roomVideoUrl;
	}
	public void setRoomVideoUrl(String roomVideoUrl) {
		this.roomVideoUrl = roomVideoUrl;
	}
	
	
	
}
