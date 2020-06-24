package com.unicom.kefu.kit;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StrKit {
	/**
	 * 棣栧瓧姣嶅彉灏忓啓
	 */
	public static String firstCharToLowerCase(String str) {
		char firstChar = str.charAt(0);
		if (firstChar >= 'A' && firstChar <= 'Z') {
			char[] arr = str.toCharArray();
			arr[0] += ('a' - 'A');
			return new String(arr);
		}
		return str;
	}
	
	/**
	 * 棣栧瓧姣嶅彉澶у啓
	 */
	public static String firstCharToUpperCase(String str) {
		char firstChar = str.charAt(0);
		if (firstChar >= 'a' && firstChar <= 'z') {
			char[] arr = str.toCharArray();
			arr[0] -= ('a' - 'A');
			return new String(arr);
		}
		return str;
	}
	
	/**
	 * 瀛楃涓蹭负 null 鎴栬�呭唴閮ㄥ瓧绗﹀叏閮ㄤ负 ' ' '\t' '\n' '\r' 杩欏洓绫诲瓧绗︽椂杩斿洖 true
	 */
	public static boolean isBlank(String str) {
		if (str == null) {
			return true;
		}
		int len = str.length();
		if (len == 0) {
			return true;
		}
		for (int i = 0; i < len; i++) {
			switch (str.charAt(i)) {
			case ' ':
			case '\t':
			case '\n':
			case '\r':
			// case '\b':
			// case '\f':
				break;
			default:
				return false;
			}
		}
		return true;
	}
	
	public static boolean notBlank(String str) {
		return !isBlank(str);
	}
	
	public static boolean notBlank(String... strings) {
		if (strings == null) {
			return false;
		}
		for (String str : strings) {
			if (isBlank(str)) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean notNull(Object... paras) {
		if (paras == null) {
			return false;
		}
		for (Object obj : paras) {
			if (obj == null) {
				return false;
			}
		}
		return true;
	}
	
	public static String toCamelCase(String stringWithUnderline) {
		if (stringWithUnderline.indexOf('_') == -1) {
			return stringWithUnderline;
		}
		
		stringWithUnderline = stringWithUnderline.toLowerCase();
		char[] fromArray = stringWithUnderline.toCharArray();
		char[] toArray = new char[fromArray.length];
		int j = 0;
		for (int i=0; i<fromArray.length; i++) {
			if (fromArray[i] == '_') {
				// 褰撳墠瀛楃涓轰笅鍒掔嚎鏃讹紝灏嗘寚閽堝悗绉讳竴浣嶏紝灏嗙揣闅忎笅鍒掔嚎鍚庨潰涓�涓瓧绗﹁浆鎴愬ぇ鍐欏苟瀛樻斁
				i++;
				if (i < fromArray.length) {
					toArray[j++] = Character.toUpperCase(fromArray[i]);
				}
			}
			else {
				toArray[j++] = fromArray[i];
			}
		}
		return new String(toArray, 0, j);
	}
	
	public static String join(String[] stringArray) {
		StringBuilder sb = new StringBuilder();
		for (String s : stringArray) {
			sb.append(s);
		}
		return sb.toString();
	}
	
	public static String join(String[] stringArray, String separator) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<stringArray.length; i++) {
			if (i > 0) {
				sb.append(separator);
			}
			sb.append(stringArray[i]);
		}
		return sb.toString();
	}
	
	public static boolean slowEquals(String a, String b) {
		byte[] aBytes = (a != null ? a.getBytes() : null);
		byte[] bBytes = (b != null ? b.getBytes() : null);
		return HashKit.slowEquals(aBytes, bBytes);
	}
	
	public static boolean equals(String a, String b) {
		return a == null ? b == null : a.equals(b);
	}
	
	public static String getRandomUUID() {
		return java.util.UUID.randomUUID().toString().replace("-", "");
	}
	
	public static List<String> getList(String strs,String regex){
		List<String> lists = new ArrayList<>();
		if (StrKit.notBlank(strs)) {
			for (String str : strs.split(regex)) {
				lists.add(str);
			}
		}
		return lists;
	}
	
	 /** @Title: getRandomString 
     * @Description: 随机生成字符串
     * @param @param length 字符串长度
     * @return String
     * @date createTime：2018年4月9日下午1:45:52
     */
    public static String getRandomString(int length){
        //产生随机数
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        //循环length次
        for(int i=0; i<length; i++){
          //产生0-2个随机数，既与a-z，A-Z，0-9三种可能
          int number=random.nextInt(3);
          long result=0;
          switch(number){
          //如果number产生的是数字0；
          case 0:
            //产生A-Z的ASCII码
            result=Math.round(Math.random()*25+65);
            //将ASCII码转换成字符
            sb.append(String.valueOf((char)result));
            break;
            case 1:
              //产生a-z的ASCII码
            result=Math.round(Math.random()*25+97);
              sb.append(String.valueOf((char)result));
            break;
            case 2:
              //产生0-9的数字
                       sb.append(String.valueOf
                              (new Random().nextInt(10)));
            break; 
          }
        }
        return sb.toString();
      }
	
}
