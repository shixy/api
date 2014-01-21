package com.shixy.web;

import java.util.Locale;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvokeFaultExceptionMapper implements ExceptionMapper {

	@Override
	public Response toResponse(Throwable ex) {
		StackTraceElement[] trace = new StackTraceElement[1];
		trace[0] = ex.getStackTrace()[0];
		ex.setStackTrace(trace);
		ex.printStackTrace();
		ResponseBuilder rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		rb.type("application/json;charset=UTF-8");
		if (ex instanceof WsRuntimeException) {
			WsRuntimeException e = (WsRuntimeException) ex;
			ErrorResponse error = new ErrorResponse(e.getErrorType().getCode(), e.getErrorType().getMessage());
			rb.entity(error);
		} else if (ex instanceof WsException) {
			ErrorResponse error = new ErrorResponse("error", ex.getMessage());
			rb.entity(error);
		} else {
			ErrorResponse error = new ErrorResponse("error", "系统内部发生错误");
			rb.entity(error);
		}
		rb.language(Locale.SIMPLIFIED_CHINESE);
		Response r = rb.build();
		return r;
	}

}
