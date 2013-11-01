package com.shixy.web.szlib.response;

public class SelfLib {
	private String no;
	private String adress;
	
	public SelfLib(String no,String adress) {
		this.no = no;
		this.adress = adress;
	}
	
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getAdress() {
		return adress;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}
	

}
