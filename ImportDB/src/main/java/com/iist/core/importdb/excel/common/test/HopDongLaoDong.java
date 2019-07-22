package com.iist.core.importdb.excel.common.test;

import com.iist.core.importdb.arr.common.annotation.Element;
import com.iist.core.importdb.arr.common.annotation.SheetSerializable;

@SheetSerializable
public class HopDongLaoDong {

	@Element(type="String", name ="tenHdld")
	private String tenHdld;
 
	@Element(type="String", name ="soHopDong")
	private String soHopDong;

}

