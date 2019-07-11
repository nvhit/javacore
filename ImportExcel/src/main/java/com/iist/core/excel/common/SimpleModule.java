package com.iist.core.excel.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * 
 * @author HungNV
 *
 */
public abstract class SimpleModule {

	protected abstract void init(String args[]);

	protected abstract int execute();

	protected abstract void destroy();

	protected Properties props = new Properties();

	
	protected int getInt(String key) {
		return Integer.parseInt(props.getProperty(key));
	}

	protected String getString(String key) {
		return props.getProperty(key);
	}
}
	
