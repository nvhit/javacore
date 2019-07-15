package com.iist.core.importdb.excel.common.test;

import java.util.ArrayList;

import com.iist.core.importdb.excel.common.util.ExcelUtils;
import com.iist.core.importdb.excel.common.util.StringUtils;

public class Test {
	
	public static void main(String[] args) {
		NhanVien nv = new NhanVien();
		int index = nv.indexHeader;
		ArrayList<String> arr = ExcelUtils.readingOneRow("E://20190709Template.xlsx",index);
		
		System.out.println(ExcelUtils.creteJSONAndTextFileFromExcel("E://20190709Template.xlsx",index));
	}
	
}
