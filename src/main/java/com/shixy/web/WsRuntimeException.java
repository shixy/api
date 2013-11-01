package com.shixy.web;


public class WsRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private ErrorType type;
	
	public WsRuntimeException(ErrorType type) {
		super();
		this.type = type;
	}

	public WsRuntimeException(ErrorType type, Throwable cause) {
		super(cause);
		this.type = type;;
	}
	
	public ErrorType getErrorType(){
		return type;
	}

	
	public enum ErrorType {
		NEED_APPKEY("0","缺少APPKEY"),ERR_APPKEY("1","APPKEY错误"), INVALID_SESSION("2","无效的SESSIONID,请重新授权"),NEED_SESSION("3","缺少sessionId参数");
		private String code;
		private String message;
		private ErrorType(String code,String message){
			this.code = code;
			this.message = message;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		
		
		
	}

}


