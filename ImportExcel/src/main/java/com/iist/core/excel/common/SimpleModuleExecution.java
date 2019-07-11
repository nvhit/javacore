package com.iist.core.excel.common;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import com.iist.core.excel.common.CommonConst;
import com.iist.core.excel.common.SimpleModule;

/**
 * 
 * @author HungNV
 *
 */
public class SimpleModuleExecution {
	/*
	 * method main of library
	 */
	public static void main(String[] args) {
		String moduleName = null;
		SimpleDateFormat fm = new SimpleDateFormat();
		try {
			String moduleArgs[] = new String[0];
			if(args.length == 0) {
				throw new IllegalArgumentException("");
			}

			moduleName = args[0];
			System.out.println(fm.format(new Date()) +" "+ moduleName +" Start");
			if(args.length > 1) {
				moduleArgs = Arrays.copyOfRange(args, 0, args.length);
			}

			int exitCode = CommonConst.SUCCESS_CODE;

			Class<?> c = Class.forName(moduleName);

			SimpleModule module = (SimpleModule)c.newInstance();

			module.init(moduleArgs);

			exitCode = module.execute();

			module.destroy();
			System.out.println(fm.format(new Date()) +" "+ moduleName +" End");
			System.exit(exitCode);
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println(fm.format(new Date()) +" "+ moduleName +" End");
			System.exit(CommonConst.ERROR_CODE);
		}
	}

}
