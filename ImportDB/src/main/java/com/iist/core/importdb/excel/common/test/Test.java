package com.iist.core.importdb.excel.common.test;

import com.iist.core.importdb.excel.common.util.ExcelUtils;
import com.iist.core.importdb.excel.common.util.JsonUtils;

public class Test {
	
	public static void main(String[] args) {

		NhanVien nhanVien = new NhanVien();
		JsonUtils.convertDataExcelToJson("E:\\20190709Template.xlsx" , nhanVien);
		ExcelUtils.validateDataExcel("E:\\20190709Template.xlsx" , nhanVien);
	}
}
 