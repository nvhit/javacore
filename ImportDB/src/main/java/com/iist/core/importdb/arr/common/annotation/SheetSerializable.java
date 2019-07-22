package com.iist.core.importdb.arr.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SheetSerializable {
	final String STRING_DEFAUL ="";
	final int NUM_DEFAULT = 0;

	String sheetName() default STRING_DEFAUL;
	
	int indexBeginHeader() default NUM_DEFAULT;

	int headerIndexColumn() default NUM_DEFAULT;

	int indexBeginRowData() default NUM_DEFAULT;
}
