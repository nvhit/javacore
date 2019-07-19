package com.iist.core.importdb.excel.common.test;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.iist.core.importdb.arr.common.annotation.Element;
 
@JsonIgnoreProperties({ "ignoreme1", "ignoreme2" })
public class Car {
	@Element(name="name")
	@JsonProperty
	public String name;

	
	
	public static class Hung {
		@JsonProperty
		public String maSoNhanVien ;
		@JsonProperty
		public String hoVaTen;
		@JsonProperty
		public String ghiChu;
		
		
		
	}
	
	
 
}