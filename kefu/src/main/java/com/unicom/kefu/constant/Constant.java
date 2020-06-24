package com.unicom.kefu.constant;

/**
 * 项目中的常量定义类
 */
public class Constant {

	public static final Integer USTATUS_NONE = 0;// 未完成身份证验证上传
	public static final Integer USTATUS_PHOTO = 1;// 身份证完成
	public static final Integer USTATUS_INFO = 2;// 基本信息完成
	public static final Integer USTATUS_LIVE = 3;// 居住信息完成
	public static final Integer USTATUS_LIVEMaterial = 4;// 居住资料完成
	public static final Integer USTATUS_LIVEMaterialNo = 5;// 居住资料拒绝
	public static final Integer USTATUS_COM = 6;// 审核完成
	public static final Integer USTATUS_LOGOUT = 7;// 注销用户

	public static final Integer LIVE_NO = 0;// 住所不在使用
	public static final Integer LIVE_USERING = 1;// 正在使用
}
