package com.iist.core.importdb.excel.common.test;

import java.util.ArrayList;

import com.iist.core.importdb.excel.common.util.ExcelUtils;
import com.iist.core.importdb.excel.common.util.StringUtils;

public class Test {
	public static void main(String[] args) {
		
		
		ArrayList<String> arr = ExcelUtils.readingOneRow("E://20190709Template.xlsx",2);
		for (String obj : arr) {
			//System.out.print(obj+ ",");
			System.out.println(StringUtils.convertStringToVar(obj));
		}
		 
		
		
	}
}
