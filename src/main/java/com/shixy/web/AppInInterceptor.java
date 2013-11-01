package com.shixy.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shixy.web.WsRuntimeException.ErrorType;
import com.shixy.web.session.Session;
import com.shixy.web.session.SimpleSessionManager;

/**
 * 主要用于对请求参数的加密或者其他需要预处理的功能
 * TODO secret key  
 * @author shixy
 * @date 2013-7-2下午3:36:39
 */
@Service("appInInterceptor")
public class AppInInterceptor extends AbstractPhaseInterceptor<Message> {

	

	public AppInInterceptor(String phase) {
		super(phase);
	}

	public AppInInterceptor() {
		super(Phase.RECEIVE);
	}
	
	/**
	 * 对请求参数进行预处理
	 * 检查appKey和sessionId的合法性
	 */
	@Override
	public void handleMessage(Message message) {
		String path = (String) message.get(Message.PATH_INFO);
		TreeMap headers =  (TreeMap) message.get(Message.PROTOCOL_HEADERS);
		ArrayList cookies = (ArrayList)headers.get("cookie");
		Map<String, String> cookieMap = this.getCookieMap(cookies);
		String appKey = cookieMap.get("appKey");
		String sessionId = cookieMap.get("sessionId");
		if(path.equals("/ws/auth/login")){
			this.checkAppKey(appKey);
		}else{
			this.checkArgs(appKey, sessionId);
		}
	}
	
	private Map<String, String> getCookieMap(ArrayList<String> cookies){
		Map<String, String> map = new HashMap<String, String>();
		for(String cookie : cookies){
			String[] cookieArr = cookie.split(";");
			for(String pair : cookieArr){
				String[] arr = pair.split("=");
				map.put(arr[0], arr[1]);
			}
		}
		return map;
	}
	
	/**
	 * 检查AppKey的合法性
	 * @param appKey
	 */
	private void checkAppKey(String appKey){
		if(appKey == null){
			throw new WsRuntimeException(ErrorType.NEED_APPKEY);
		}else{
//			String value = configService.getValueByKey(appKey);
//			if(StringUtils.isEmpty(value)){
//				throw new WsRuntimeException(ErrorType.ERR_APPKEY); 
//			}
		}
	}
	
	/**
	 * 检查session的合法性
	 * @param sessionId
	 */
	private void checkSessionId(String sessionId){
		if(sessionId == null ){
			throw new WsRuntimeException(ErrorType.INVALID_SESSION); 
		}else{
			Session session = SimpleSessionManager.getInstance().getSession(sessionId);
			if(session == null){
				throw new WsRuntimeException(ErrorType.NEED_SESSION); 
			}
		}
	}
	
	private void checkArgs(String appKey,String sessionId){
		this.checkAppKey(appKey);
		this.checkSessionId(sessionId);
	}

	
//	@Override
//	public void handleMessage(Message message) throws Fault {
//		String reqParams = null;
//		String path = (String) message.get(Message.PATH_INFO);
//		Map<String, String> paraMap = new HashMap<String,String>();
//		if (message.get(Message.HTTP_REQUEST_METHOD).equals("GET")) {// 采用GET方式请求
//			reqParams = (String) message.get(Message.QUERY_STRING);
//			paraMap = this.getParamsMap(reqParams);
//		} else if (message.get(Message.HTTP_REQUEST_METHOD).equals("POST")) {// 采用POST方式请求
//			try {
//				InputStream is = message.getContent(InputStream.class);
//				paraMap = this.getParamsMap(is.toString());
//			} catch (Exception e) {
//				logger.error("AppInInterceptor异常", e);
//			}
//		}
//		logger.info("请求的参数：" + reqParams);
//	}
//
//	private Map<String, String> getParamsMap(String strParams) {
//		if (strParams == null || strParams.trim().length() <= 0) {
//			return null;
//		}
//		Map<String, String> map = new HashMap<String, String>();
//		String[] params = strParams.split("&");
//		for (int i = 0; i < params.length; i++) {
//			String[] arr = params[i].split("=");
//			map.put(arr[0], arr[1]);
//		}
//		return map;
//	}
//
//	private String getParams(Map<String, String> map) {
//		if (map == null || map.size() == 0) {
//			return null;
//		}
//		StringBuffer sb = new StringBuffer();
//		Iterator<String> it = map.keySet().iterator();
//		while (it.hasNext()) {
//			String key = it.next();
//			String value = map.get(key);
//			/*
//			 * 这里可以对客户端上送过来的输入参数进行特殊处理。如密文解密；对数据进行验证等等。。。
//			 * if(key.equals("content")){ value.replace("%3D", "="); value =
//			 * DesEncrypt.convertPwd(value, "DES"); }
//			 */
//			if (sb.length() <= 0) {
//				sb.append(key + "=" + value);
//			} else {
//				sb.append("&" + key + "=" + value);
//			}
//		}
//		return sb.toString();
//	}
	
}
