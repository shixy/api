package com.shixy.web.szlib.response;

/**
 * 借阅图书
 * @author shixy
 *
 */
public class LoanBook {
	
	private String recordNo;
	private String barCode;
	private int canRenew;
	private int renew;
	private String title;
	private String callNo;
	private String volumeNo;
	private String loanDate;
	private String returnDate;
	private String local;
	private String cirType;
	private String price;
	private String serviceType;
	private String ISBN;
	public String getRecordNo() {
		return recordNo;
	}
	public void setRecordNo(String recordNo) {
		this.recordNo = recordNo;
	}
	public String getBarCode() {
		return barCode;
	}
	public void setBarCode(String barCode) {
		this.barCode = barCode;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCallNo() {
		return callNo;
	}
	public void setCallNo(String callNo) {
		this.callNo = callNo;
	}
	public String getVolumeNo() {
		return volumeNo;
	}
	public void setVolumeNo(String volumeNo) {
		this.volumeNo = volumeNo;
	}
	public String getLoanDate() {
		return loanDate;
	}
	public void setLoanDate(String loanDate) {
		this.loanDate = loanDate;
	}
	public String getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}
	public String getLocal() {
		return local;
	}
	public void setLocal(String local) {
		this.local = local;
	}
	public String getCirType() {
		return cirType;
	}
	public void setCirType(String cirType) {
		this.cirType = cirType;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getISBN() {
		return ISBN;
	}
	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}
	public int getCanRenew() {
		return canRenew;
	}
	public void setCanRenew(int canRenew) {
		this.canRenew = canRenew;
	}
	public int getRenew() {
		return renew;
	}
	public void setRenew(int renew) {
		this.renew = renew;
	}
	
	
	
	

}
