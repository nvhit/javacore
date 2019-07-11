package com.iist.core.excel.common.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.iist.core.excel.importdb.CellValueBean;

/**
 * 
 * @author HungNV
 *
 */
public class ExcelUtils {

	private static final char[] EXCEL_SHEET_NAME_INVALID_CHARS = { '/', '\\', '?', '*', ']', '[', ':' };
	private static final char INVALID_REPLACE_CHAR = '_';

	 public static String getSheetNameWithLimit(String rawSheetname) {
		return getSheetNameWithLimit(rawSheetname, false);
		}

	public static String getSheetNameWithLimit(String rawSheetname, boolean right) {

		String sheetname = right ? StringUtils.right(rawSheetname, 31) : StringUtils.left(rawSheetname, 31);

		// Replace invalid characters
		for (char c : EXCEL_SHEET_NAME_INVALID_CHARS) {
			sheetname = StringUtils.replaceChars(sheetname, c, INVALID_REPLACE_CHAR);
		}

		return sheetname;

	}

	public static ArrayList<?> getListCells (Object obj ,Sheet sheet,int rowBegin) {
		ArrayList<Object> objs = new ArrayList<Object>();
		for (Row row : sheet) {
			if(row.getRowNum()>= 6) {
				objs.add(obj);
			}
		}
		return objs;
	}

}
