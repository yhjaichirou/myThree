package com.unicom.kefu.controller.api;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.unicom.kefu.constant.URLConstant;
import com.unicom.kefu.kit.RetKit;
import com.unicom.kefu.kit.StrKit;
import com.unicom.kefu.kit.UploadFile;
import com.unicom.kefu.kit.WxResult;
import com.unicom.kefu.model.WxUserInfo;
import com.unicom.kefu.service.ApiService;
import com.unicom.kefu.service.TokenService;

import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;

/**
 * 业务逻辑
 * 
 * @author Administrator
 * 
 */
@RestController
@RequestMapping("api")
public class MyWorkController {
	private static Log log = LogFactory.getLog(MyWorkController.class);

	@Value("${appid}")
	private String appid;
	@Value("${secret}")
	private String secret;
	@Value("${path.photo}")
	private String photoPath;
	@Value("${path.getphoto}")
	private String getphotoPath;

//	@Autowired
//	private CacheManager cacheManager;

	@Resource
	private ApiService apiService;
	
//	@Resource
//	private BaiduService bdService;

	/**
	 * 查询是否认证
	 * 
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/isAuth", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@ResponseBody
	public RetKit isAuth(String openid) {
		try {
			List<WxUserInfo> wxinfo = Db.use().find(Entity.create("kefu_customer").set("openid", openid),
					WxUserInfo.class);
			if (wxinfo.size() > 0) {
				return RetKit.okData(wxinfo.get(0));
			}
			return RetKit.fail("未认证！");
		} catch (Exception e) {
			e.printStackTrace();
			return RetKit.fail(e.getMessage());
		}
	}

	/**
	 * -------------------------微信在使用的 接口--------------------------
	 */
	//@RequestMapping(value = "/auth", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	@RequestMapping("/auth")
	@ResponseBody
	public RetKit getAuthentication(String code) {
		try {
			if (StrKit.isBlank(code)) {
				return RetKit.fail("认证码不能为空！");
			}
			Map<String, Object> paramMap = new HashMap<>();
			paramMap.put("appid", appid);
			paramMap.put("secret", secret);
			paramMap.put("js_code", code);
			paramMap.put("grant_type", "authorization_code");
			String result3 = HttpUtil.get("https://api.weixin.qq.com/sns/jscode2session", paramMap);
			WxResult appuser = JSONObject.parseObject(result3, WxResult.class);
			if (appuser.getErrcode() != null) {
				return RetKit.fail(appuser.getErrmsg());
			}
			return RetKit.okData(appuser);
		} catch (Exception e) {
			e.printStackTrace();
			return RetKit.fail(e.getMessage());
		}
	}

	@RequestMapping("updateUserInfo")
	@ResponseBody
	public RetKit getAuthentication(String userinfo, String openid) {
		WxUserInfo _userinfo = JSONObject.parseObject(userinfo, WxUserInfo.class);
		return apiService.addWxUserInfo(_userinfo, openid);
	}

	@RequestMapping("isRegister")
	@ResponseBody
	public RetKit isRegister(String unionid) {
		// String userStr =
		// HttpRequest.get("https://wlj.wulanchabu.gov.cn/api/wxuser/uid")
		String userStr = HttpRequest.get("https://wlj.wulanchabu.gov.cn/api/wxuser/uid")
				.header("Content-Type", "application/x-www-form-urlencoded").header("uid", unionid)// 头信息，多个头信息多次调用此方法即可
//			    .form(paramMap)//表单内容
				.timeout(20000)// 超时，毫秒
				.execute().body();
		JSONObject ob = JSONObject.parseObject(userStr);
		if (ob.containsKey("code") && ob.getInteger("code") == 200) {
			JSONObject data = (JSONObject) ob.get("data");
			Map<String, Object> resultMap = new HashMap<>();
			resultMap.put("name", data.get("name"));
			resultMap.put("phone", data.get("phone"));
			resultMap.put("idcard", data.get("idcard"));
			return RetKit.okData(resultMap);
		}
		return RetKit.fail("未获取到用户数据！");
	}

	@RequestMapping("upload")
	@ResponseBody
	public RetKit upload(MultipartFile img) throws Exception {
		if (!img.getOriginalFilename().equals("") && img.getSize() > 0) {
			try {
				return RetKit.okData(getphotoPath + UploadFile.upload(photoPath, img));
			} catch (Exception e) {
				e.printStackTrace();
				return RetKit.fail("上传失败！");
			}
		}
		return RetKit.fail("上传内容不能为空！");
	}

	@RequestMapping("uploaddel")
	@ResponseBody
	public RetKit uploaddel(String url) throws Exception {
		if (StrKit.isBlank(url)) {
			return RetKit.fail("图片路径不能为空！");
		} else {
			try {
				String fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
				// 解析相对路径
				return UploadFile.deteleFile(photoPath + fileName) ? RetKit.fail("删除失败！") : RetKit.ok("删除成功！");
			} catch (Exception e) {
				e.printStackTrace();
				return RetKit.fail(e.getMessage());
			}
		}
	}

	@RequestMapping(value = "/getUserDetail", method = RequestMethod.POST)
	@ResponseBody
	public RetKit getUserDetail(String openid) {
		return apiService.getUserDetail(openid);
	}
	
	@RequestMapping(value = "/getRoomList", method = RequestMethod.POST)
	@ResponseBody
	public RetKit getRoomList() {
		return apiService.getRoomList();
	}
	

//	// -百度云token
//	@RequestMapping(value = "/baiduToken", method = RequestMethod.POST)
//	@ResponseBody
//	public RetKit baiduToken() {
//		return bdService.getToken();
//	}
//
//	// 人脸识别 --百度公安验证
//	@RequestMapping(value = "/baiduFace", method = RequestMethod.POST)
//	@ResponseBody
//	public RetKit baiduFace(String openid, MultipartFile photo) {
//		return bdService.baiduFace(openid, photo);
//	}
//
//	// 身份证识别头像 --百度
//	@RequestMapping(value = "/idcardFace", method = RequestMethod.POST)
//	@ResponseBody
//	public RetKit idcardFace(String idcardPic) {
//		return bdService.idcardFace(idcardPic);
//	}

	// 人脸识别 --微信开放接口
	@RequestMapping(value = "/wxFace", method = RequestMethod.POST)
	@ResponseBody
	public RetKit wxFace(String openid, String verifyResult) {
		if (StrKit.isBlank(openid)) {
			return RetKit.ok("参数错误！");
		}
		String accessToken = (String) apiService.getAccessToken().get("data");
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("verify_result", verifyResult);// 和 img_url 选择其中一种
		String json = JSONObject.toJSONString(paramMap);
		// 链式构建请求
		String result2 = HttpRequest.post(URLConstant.URL_POST_WXFACE + "?access_token=" + accessToken)
				.header("Content-Type", "application/json;encoding-utf-8")// 头信息，多个头信息多次调用此方法即可
				.body(json).timeout(20000)// 超时，毫秒
				.execute().body();
		WxResult wxF = JSONObject.parseObject(result2, WxResult.class);
		if (wxF != null && wxF.getErrcode() == 0 && wxF.getErrmsg().equals("ok")) {
			return RetKit.ok("通过认证！");
		} else {
			return RetKit.fail("认证失败！");
		}
	}

//	@RequestMapping(value = "/photo", method = RequestMethod.POST)
//	@ResponseBody
//	public RetKit savePhoto(String openid, MultipartFile photo, String replaceCode) {
//		try {
//
//			if (photo != null && !photo.getOriginalFilename().equals("") && photo.getSize() > 0) {
//				// String img_url = getphotoPath + "zmb.png";
//				String idcardImage = UploadFile.upload(photoPath, photo);
//
//				log.info("-------上传身份证-------：" + idcardImage);
//
//				String img_url = getphotoPath + idcardImage;
//				String accessToken = (String) apiService.getAccessToken().get("data");
//				Map<String, Object> paramMap = new HashMap<>();
////				paramMap.put("img", img);//和 img_url 选择其中一种
////				paramMap.put("img_url", img_url);
////				paramMap.put("access_token", accessToken);
//				System.out.println(idcardImage);
//				String result3 = HttpUtil.post(
//						URLConstant.URL_POST_PHOTO + "?type=photo&img_url=" + img_url + "&access_token=" + accessToken,
//						paramMap);
//				System.out.println(result3);
//				WxResult appuser = JSONObject.parseObject(result3, WxResult.class);
//				log.info("-------识别身份证-------：" + result3);
//
//				if (appuser.getErrcode() != null) {
//					if (appuser.getErrcode() != 0) {
//						return RetKit.fail("身份证拍摄不完整，请重新拍摄！");
//					}
//					boolean isPhotoBack = appuser.getType().equals("Back");
//					String txPath = "";
//					if (!isPhotoBack) {
//						List<UserInfo> isExit = Db.use()
//								.find(Entity.create("handle_userinfo").set("idcard", appuser.getId()), UserInfo.class);
//						if (isExit.size() > 0) {
//							return RetKit.fail("该人员已经申请过居住证！");
//						}
//						// 校验身份证
//						if (!IdCardUtil.isValidatedAllIdcard(appuser.getId())) {
//							return RetKit.fail("身份证校验格式不正确！");
//						}
//						// 百度识别身份证头像
//						RetKit tx = bdService.idcardFace(idcardImage);
//						if (!tx.get("code").equals(200)) {
//							return tx;
//						} else {
//							txPath = tx.getStr("data");
//						}
//					}
//
//					// 成功识别
//					List<UserInfo> userinfo = Db.use().find(
//							Entity.create("handle_userinfo").set("openid", openid).set("replace_code", replaceCode),
//							UserInfo.class);
//					if (userinfo.size() > 0) {
//						UserInfo info = userinfo.get(0);
//
//						Db.use().update(
//								Entity.create("handle_userinfo")
//										.set(isPhotoBack ? "photo_back" : "photo_front", img_url)
//										.set("idcard",
//												isPhotoBack ? StrKit.notBlank(info.getIdcard()) ? info.getIdcard() : ""
//														: appuser.getId())
//										.set("username",
//												isPhotoBack
//														? StrKit.notBlank(info.getUsername()) ? info.getUsername() : ""
//														: appuser.getName())
//										// .set("police_station", StrKit.notBlank(info.getPoliceStation()) ?
//										// info.getPoliceStation() : "无法获取")
//										.set("idcard_date",
//												isPhotoBack ? appuser.getValid_date()
//														: StrKit.notBlank(info.getIdcardDate()) ? info.getIdcardDate()
//																: "")
//										.set("replace_code", StrKit.notBlank(replaceCode) ? replaceCode : 0)
//										.set("status", Constant.USTATUS_PHOTO).set("idcard_tx",
//												isPhotoBack
//														? StrKit.notBlank(info.getIdcardTx()) ? info.getIdcardTx() : ""
//														: txPath),
//								Entity.create("handle_userinfo").set("openid", openid).set("replace_code",
//										replaceCode));
//					} else {
//
//						Db.use().insert(Entity.create("handle_userinfo").set("openid", openid)
//								.set(isPhotoBack ? "photo_back" : "photo_front", img_url)
//								.set("idcard", isPhotoBack ? "" : appuser.getId())
//								.set("username", isPhotoBack ? "" : appuser.getName())
//								// .set("police_station", isPhotoBack ? "无法获取" : "") // 微信接口 无法获取到
//								.set("idcard_date", isPhotoBack ? appuser.getValid_date() : "")
//								.set("replace_code", StrKit.notBlank(replaceCode) ? replaceCode : 0)
//								.set("status", Constant.USTATUS_NONE).set("create_date", new Date())
//								.set("idcard_tx", isPhotoBack ? "" : txPath));
//					}
//					appuser.setBackUrl(img_url);
//					return RetKit.okData(appuser);
//				} else {
//					return RetKit.fail("请求错误！");
//				}
//			} else {
//				return RetKit.fail("上传内容不能为空！");
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			return RetKit.fail("网络错误，请稍后重试！" + e.getMessage());
//		}
//	}

	
}