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
		public String a ;
		@JsonProperty
		public String b;
		@Override
		public String toString() {
			return "Hung [a=" + a + ", b=" + b + "]";
		}
		
		
	}
	
	
 
}