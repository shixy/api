package com.shixy.web;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;


/**
 *  DES加密解密
 * @author shixy
 * @date 2013-7-25上午11:10:59
 */
public class DESUtils {
	
	/**
	 * 将byte数组转换为16进制字符串
	 * @param bytes
	 * @return
	 */
	public static String byte2Hex( byte[] bytes){
		int len = bytes.length;
		StringBuffer sb = new StringBuffer(len*2);
		for(int i = 0; i<len; i++){
			int tmp = bytes[i];
			//负数转换为正数
			while(tmp < 0){
				tmp = tmp + 256;
			}
			//小于0F的数需要补0
			if(tmp < 16){
				sb.append("0");
			}
			sb.append(Integer.toString(tmp, 16));
		}
		return sb.toString();
	}
	
	/**
	 * 将16进制字符串转换为byte数组
	 * @param str
	 * @return
	 */
	public static byte[] hex2Byte(String str){
		byte[] bytes = str.getBytes();
		int len = bytes.length;
		byte[] out = new byte[len/2];
		for(int i = 0; i < len; i=i+2){
			String tmp = new String(bytes, i, 2);
			out[i/2] = (byte)Integer.parseInt(tmp, 16);
		}
		return out;
	}
	
	/**
	 * 从指定字符串生成密钥，长度为8位，不足后面补0，超过取前8位
	 * @param str
	 * @return
	 */
	private static Key getKey(String str){
		byte[] bytes = str.getBytes();
		byte[] tmp = new byte[8];
		for(int i = 0; i < bytes.length && i < tmp.length; i++){
			tmp[i] = bytes[i];
		}
		Key key = new SecretKeySpec(tmp, "DES");
		return key;
	}
	
	/**
	 * 加密
	 * @param data 需要加密的字符串
	 * @param keyStr 加密密码
	 * @return
	 */
	public static String encrypt(String data,String keyStr){
		//Security.addProvider(new SunJCE());
		Key key = getKey(keyStr);
		String result = "";
		try {
			Cipher cipher;cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			result = byte2Hex(cipher.doFinal(data.getBytes()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 解密
	 * @param data 需要解密的字符串
	 * @param keyStr 解密密码
	 * @return
	 */
	public static String decrypt(String data,String keyStr){
		//Security.addProvider(new SunJCE());
		Key key = getKey(keyStr);
		String result = "";
		try {
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			result = new String(cipher.doFinal(hex2Byte(data)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 自动生成appkey和scretkey
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static Map<String, String> generateKey() throws NoSuchAlgorithmException{
		KeyGenerator gen = KeyGenerator.getInstance("DES");
		String appKey = byte2Hex(gen.generateKey().getEncoded());
		String scretKey = byte2Hex(gen.generateKey().getEncoded());
		Map<String, String> map = new HashMap<String, String>();
		map.put("appKey", appKey);
		map.put("scretKey", scretKey);
		return  map;
	}
	
	public static void main(String[] args) {
		String key = "0eefe673";
		String data = "admin=1qazXSW@";
		try {
			String result = DESUtils.encrypt(data, key);
			System.out.println("加密前"+data);
			System.out.println("加密后"+result);
			System.out.println("解密后"+DESUtils.decrypt(result, key));
			Map<String, String> map = generateKey();
			System.out.println(map.get("appKey"));
			System.out.println(map.get("scretKey"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
