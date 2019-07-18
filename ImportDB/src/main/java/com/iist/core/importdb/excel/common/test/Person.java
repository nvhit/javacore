package com.iist.core.importdb.excel.common.test;

import com.iist.core.importdb.arr.common.annotation.Element;
import com.iist.core.importdb.arr.common.annotation.SheetSerializable;



@SheetSerializable
public class Person{
	@Element(type="String", name ="maSoNhanVien", level="parent")
	private String maSoNhanVien;
 
	@Element(type="String", name ="hoVaTen",level = "parent")
	private String hoVaTen;

	@Element(type="String", name ="hopDongLaoDong",level = "parent")
	public HopDongLaoDong hopDongLaoDong;



}