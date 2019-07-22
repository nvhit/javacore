package com.iist.core.importdb.excel.common.test;

import com.iist.core.importdb.arr.common.annotation.Element;
import com.iist.core.importdb.arr.common.annotation.SheetSerializable;



@SheetSerializable( indexBeginHeader = 2, sheetName = "Thoi viec", headerIndexColumn = 1, indexBeginRowData = 5)
public class NhanVien {

	@Element(type="String", name ="luong", level = 1)
	private String luong;
 
	@Element(type="String", name ="hoVaTen",level = 1)
	private String hoVaTen;

	@Element(type="String", name ="hopDongLaoDong",level = 1)
	public HopDongLaoDong hopDongLaoDong;

}