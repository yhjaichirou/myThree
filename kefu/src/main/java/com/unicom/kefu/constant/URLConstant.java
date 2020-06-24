package com.unicom.kefu.constant;

public class URLConstant {
	/**
	 * 获取微信token
	 */
	public static final String URL_GET_TOKKEN = "https://api.weixin.qq.com/cgi-bin/token";

	/**
	 * 获取uid
	 */
	public static final String URL_GET_UID = "https://api.weixin.qq.com/sns/jscode2session";

	/**
	 * 识别身份证
	 */
	public static final String URL_POST_PHOTO = "https://api.weixin.qq.com/cv/ocr/idcard";

	/**
	 * 微信人脸识别
	 */
	public static final String URL_POST_WXFACE = "https://api.weixin.qq.com/cityservice/face/identify/getinfo";

	/**
	 * 百度获取token
	 */
	public static final String URL_BAIDU_POST_TOKEN = "https://aip.baidubce.com/oauth/2.0/token";

	/**
	 * 百度人脸识别验证
	 */
	public static final String URL_BAIDU_POST_FACE = "https://aip.baidubce.com/rest/2.0/face/v3/person/verify";

	/**
	 * 百度身份证识别头像
	 */
	public static final String URL_BAIDU_POST_IDCARDFACE = "https://aip.baidubce.com/rest/2.0/ocr/v1/idcard";

}
