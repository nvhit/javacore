package com.iist.core.importdb.arr.common.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Element {
	final int NUM_DEFAULT = 0; 

	final String STRING_DEFAULT = "";

	String name() default STRING_DEFAULT;

	int level() default NUM_DEFAULT;

	String nameParent() default STRING_DEFAULT;

	String type() default STRING_DEFAULT;

}
