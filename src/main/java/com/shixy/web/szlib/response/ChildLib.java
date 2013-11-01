package com.shixy.web.szlib.response;

public class ChildLib {
	private String name;
	private String tel;
	private String adress;
	public String getTel() {
		return tel;
	}
	
	public ChildLib(String name,String adress,String tel) {
		this.name = name;
		this.tel = tel;
		this.adress = adress;
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getAdress() {
		return adress;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}
	
	

}
