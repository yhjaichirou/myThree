package com.unicom.kefu.service;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
//import com.baidu.aip.util.Base64Util;
//import com.baidu.handleResient.config.URLConstant;
//import com.baidu.handleResient.kit.HttpUtil;
//import com.baidu.handleResient.kit.RetKit;
//import com.baidu.handleResient.kit.StrKit;
//import com.baidu.handleResient.kit.UploadFile;
//import com.baidu.handleResient.model.vo.BdResult;
//import com.baidu.handleResient.model.vo.BdResult.Result;
//import com.baidu.handleResient.model.vo.UserInfo;

import cn.hutool.core.io.FileUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import cn.hutool.http.HttpRequest;

@Service
public class BaiduService {

//	private static Log log = LogFactory.getLog(BaiduService.class);
//
//	@Value("${baidu.appkey}")
//	private String appkey;
//	@Value("${baidu.secretkey}")
//	private String secretkey;
//
//	// 身份证头像获取
//	@Value("${baidu.appkey2}")
//	private String appkey2;
//	@Value("${baidu.secretkey2}")
//	private String secretkey2;
//
//	@Value("${path.photo}")
//	private String photoPath;
//	@Value("${path.getphoto}")
//	private String getphotoPath;
//
//	/**
//	 * 获取API访问token 该token有一定的有效期，需要自行管理，当失效时需重新获取.
//	 * 
//	 * @param ak - 百度云官网获取的 API Key
//	 * @param sk - 百度云官网获取的 Securet Key
//	 * @return assess_token 示例：
//	 *         "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567"
//	 */
//	public RetKit getToken() {
//		try {
//			HashMap<String, Object> paramMap = new HashMap<>();
//			paramMap.put("client_id", appkey);
//			paramMap.put("grant_type", "client_credentials");
//			paramMap.put("client_secret", secretkey);
//			String token = cn.hutool.http.HttpUtil.post(URLConstant.URL_BAIDU_POST_TOKEN, paramMap);
//			BdResult result = JSONObject.parseObject(token, BdResult.class);
////			Cache tokenCache = cacheManager.getCache("accessToken");
////			tokenCache.put(openid+"_token", appuser.getAccessToken());
//
//			if (result.getError() != null) {
//				return RetKit.fail(result.getError_description());
//			}
//			return RetKit.okData(result.getAccess_token());
//		} catch (Exception e) {
//			e.printStackTrace();
//			return RetKit.fail("获取token失败！" + e.getMessage());
//		}
//	}
//
//	public RetKit getToken2() {
//		try {
//			HashMap<String, Object> paramMap = new HashMap<>();
//			paramMap.put("client_id", appkey2);
//			paramMap.put("grant_type", "client_credentials");
//			paramMap.put("client_secret", secretkey2);
//			String token = cn.hutool.http.HttpUtil.post(URLConstant.URL_BAIDU_POST_TOKEN, paramMap);
//			BdResult result = JSONObject.parseObject(token, BdResult.class);
////			Cache tokenCache = cacheManager.getCache("accessToken");
////			tokenCache.put(openid+"_token", appuser.getAccessToken());
//
//			if (result.getError() != null) {
//				return RetKit.fail(result.getError_description());
//			}
//			return RetKit.okData(result.getAccess_token());
//		} catch (Exception e) {
//			e.printStackTrace();
//			return RetKit.fail("获取token失败！" + e.getMessage());
//		}
//	}
//
//	public RetKit baiduFace(String openid, MultipartFile face) {
//		try {
//
//			List<UserInfo> us = Db.use().find(Entity.create("handle_userinfo").set("openid", openid), UserInfo.class);
//			if (us.size() > 0) {
//				UserInfo info = us.get(0);
//				String idcard = info.getIdcard();
//				String name = info.getUsername();
//				if (StrKit.isBlank(idcard) && StrKit.isBlank(name)) {
//					return RetKit.fail("验证错误，身份证号或名字不能为空！");
//				}
//				// 上传图片
//				if (face != null && !face.getOriginalFilename().equals("") && face.getSize() > 0) {
//					String fileName = UploadFile.upload(photoPath, face);
//					String img_url = getphotoPath + fileName;
//
//					HashMap<String, Object> paramMap = new HashMap<>();
//					paramMap.put("image", img_url);
//					paramMap.put("image_type", "URL");
//					paramMap.put("id_card_number", idcard);
//					paramMap.put("name", name);
//					paramMap.put("quality_control", "NORMAL");
//					paramMap.put("liveness_control", "LOW");
//					String _result = HttpRequest
//							.post(URLConstant.URL_BAIDU_POST_FACE + "?access_token=" + getToken().getStr("data"))
//							.header("Content-Type", "application/json")// 头信息，多个头信息多次调用此方法即可
//							.form(paramMap)// 表单内容
//							.timeout(20000)// 超时，毫秒
//							.execute().body();
//					BdResult result = JSONObject.parseObject(_result, BdResult.class);
//
//					boolean bol = UploadFile.deteleFile(photoPath + fileName);
//					log.info("删除识别图片结果：" + bol);
//					log.info("百度身份识别：" + result);
//					if (result.getError() != null) {
//						log.error(result.getError_description());
//						return RetKit.fail(result.getError_description());
//					} else {
//						Result re = result.getResult();
//						if (re.getScore() > 80) {
//							return RetKit.ok("身份核验成功！");
//						} else {
//							return RetKit.fail("身份核验失败！");
//						}
//					}
//
//				} else {
//					return RetKit.fail("图片不能为空！");
//				}
//
//			} else {
//				return RetKit.fail("该用户不存在！");
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			return RetKit.fail("身份核验失败！" + e.getMessage());
//		}
//	}
//
//	public RetKit idcardFace(String idcardPic) {
//		try {
//			String idcardFront = photoPath + idcardPic;
//			if (StrKit.isBlank(idcardFront)) {
//				return RetKit.fail("图片不存在！");
//			}
//			// idcardFront = "d:\\" + idcardPic;
//			byte[] imgData = FileUtil.readBytes(idcardFront);
//			String imgStr = Base64Util.encode(imgData);
//			String imgParam = URLEncoder.encode(imgStr, "UTF-8");
//			String param = "id_card_side=" + "front" + "&detect_photo=true&id_card_side=front&image=" + imgParam;
//			String _result = HttpUtil.post(URLConstant.URL_BAIDU_POST_IDCARDFACE, getToken2().getStr("data"), param);
//			BdResult result = JSONObject.parseObject(_result, BdResult.class);
//
//			if (result != null && result.getLog_id() != 0) {
//				String picName = idcardPic.substring(0, idcardPic.lastIndexOf("."));
//				String result_tx_name = picName + "_tx.jpg";
//				OutputStream out = new FileOutputStream(photoPath + result_tx_name);
//				byte[] b = Base64Util.decode(result.getPhoto());
//				for (int i = 0; i < b.length; ++i) {
//					if (b[i] < 0) {// 调整异常数据
//						b[i] += 256;
//					}
//				}
//				out.write(b);
//				return RetKit.okData(getphotoPath + result_tx_name);
//			} else {
//				return RetKit.fail("获取失败！");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			return RetKit.fail("获取头像失败！" + e.getMessage());
//		}
//	}

}
