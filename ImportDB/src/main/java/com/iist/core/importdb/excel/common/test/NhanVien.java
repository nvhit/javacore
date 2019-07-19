package com.iist.core.importdb.excel.common.test;

import com.iist.core.importdb.arr.common.annotation.Element;
import com.iist.core.importdb.arr.common.annotation.SheetSerializable;



@SheetSerializable
public class NhanVien{
	@Element(type="String", name ="maSoNhanVien", level = 1)
	private String maSoNhanVien;
 
	@Element(type="String", name ="hoVaTen",level = 1)
	private String hoVaTen;

	@Element(type="String", name ="hopDongLaoDong",level = 1)
	public HopDongLaoDong hopDongLaoDong;

	

	@Element(indexBeginHeader = 2)
	public static int indexBeginHeader = 2;

	@Element
	public int indexEndHeader;

	@Element
	public int headerIndexColumn;


}