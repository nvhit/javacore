package com.iist.core.importdb.arr.common.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Element {
	String name() default "";

	String level() default "";

	String nameParent() default "";

	String type() default "";

	
}
