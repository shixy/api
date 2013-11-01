package com.shixy.web;

public class WsException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	public WsException() {

	}
	public WsException(String message){
		super(message);
	}
	public WsException(String message,Throwable cause){
		super(message,cause);
	}
	public WsException(Throwable cause){
		super(cause);
	}

}
