package com.unicom.kefu.kit;

import java.io.File;
import org.springframework.web.multipart.MultipartFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * 上传文件工具
 * @author admin
 *
 */
public class UploadFile {
	private static Log log = LogFactory.getLog(UploadFile.class);
	public static final String url = "D:\\image\\";
	public static final String[] videoType = {"VIDEO","3gp","MOV","AVI","avi","video"};
	
	/**
	 * 上传
	 * @param path 
	 * @param mulFile
	 * @return
	 * @throws Exception 
	 */
	public static String upload(String path,MultipartFile mulFile) throws Exception {
		String nativeName = mulFile.getOriginalFilename();
		//上传后文件做处理
		//int indexPoint = nativeName.indexOf(".");
		int indexPoint = nativeName.lastIndexOf(".");
		String proName = nativeName.substring(0,indexPoint);
		String suffix = nativeName.substring(indexPoint+1);
		//有的手机存在 特殊字符不识别 。
		nativeName = StrKit.getRandomString(5)+"."+suffix;
		
		File dir = new File(path);
		if(!dir.exists()) {
			dir.mkdir();
		}
		String fileName = System.currentTimeMillis() +nativeName;
		String url = path + fileName;
		File dest = new File(url);
		mulFile.transferTo(dest);
		
		return fileName;
	}
	
	public static boolean deteleFile(String url) {
		File fileTemp = new File(url);
		if(fileTemp.exists()) {
			fileTemp.delete();
			return true;
		}
		return false;
	}  
	
}
