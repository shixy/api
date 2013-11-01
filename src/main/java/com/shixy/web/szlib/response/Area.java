package com.shixy.web.szlib.response;

import java.util.List;

public class Area {
	private String areaName;
	private List<SelfLib> selflibs;
	private List<ChildLib> childlibs ;
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public List<SelfLib> getSelflibs() {
		return selflibs;
	}
	public void setSelflibs(List<SelfLib> selflibs) {
		this.selflibs = selflibs;
	}
	public List<ChildLib> getChildlibs() {
		return childlibs;
	}
	public void setChildlibs(List<ChildLib> childlibs) {
		this.childlibs = childlibs;
	}
	
	
	
}
