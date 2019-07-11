package com.iist.core.excel.importdb;

public class CellValueBean implements Cloneable {
	private String mnv;
	private String hoTen;
	public String getMnv() {
		return mnv;
	}
	public void setMnv(String mnv) {
		this.mnv = mnv;
	}
	public String getHoTen() {
		return hoTen;
	}
	public void setHoTen(String hoTen) {
		this.hoTen = hoTen;
	}
	public CellValueBean(String mnv, String hoTen) {
		super();
		this.mnv = mnv;
		this.hoTen = hoTen;
	}
	public CellValueBean() {
		super();
	}
	

	protected Object clone() throws CloneNotSupportedException 
	{
		CellValueBean cellValueBean = (CellValueBean) super.clone();  
	return cellValueBean; 
	}

	

}
