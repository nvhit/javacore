package com.iist.core.importdb.excel.common.test;

import java.util.ArrayList;
import java.util.List;

import com.iist.core.importdb.excel.common.util.ExcelUtils;
import com.iist.core.importdb.excel.common.util.JsonUtils;

public class Test {
	
	public static void main(String[] args) {
	
		Person p = new Person();
		Car c =  new Car();
		//System.out.println(ExcelUtils.creteJSONFileFromExcel("E:\\20190709Template.xlsx" , p));
		//System.out.println(ExcelUtils.reading("E:\\20190709Template.xlsx", 5));
		//JsonUtils.parsingJsonFileFormatArray("Thoi viec.json", c);
		//System.out.println(JsonUtils.parsingJsonFileForNode("Thoi viec.json", c));
		JsonUtils.parsingJsonFileForNode("Thoi viec.json", c);
	}
	
}
