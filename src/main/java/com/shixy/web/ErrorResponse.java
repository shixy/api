package com.shixy.web;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "error")
public class ErrorResponse implements Serializable{

    @XmlAttribute
    protected String code;

    @XmlElement
    protected String message;
    
    public ErrorResponse(){
    	
    };
    public ErrorResponse(String code,String message ){
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

