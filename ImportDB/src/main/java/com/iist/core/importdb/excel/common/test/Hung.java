package com.iist.core.importdb.excel.common.test;

import com.fasterxml.jackson.annotation.JsonProperty;

public  class Hung {
	@JsonProperty
	public String a ;
	@JsonProperty
	public String b;
	@Override
	public String toString() {
		return "Hung [a=" + a + ", b=" + b + "]";
	}
	
	
}
