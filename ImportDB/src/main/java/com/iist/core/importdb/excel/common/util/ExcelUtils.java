package com.iist.core.importdb.excel.common.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.usermodel.HSSFDataValidationHelper;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.iist.core.importdb.arr.common.annotation.Element;
import com.iist.core.importdb.arr.common.annotation.SheetSerializable;
import com.iist.core.importdb.excel.common.constants.StringPool;

/**
 * 
 * @author HungNV
 * <p>
 * This library use execute with excel file
 * <p/>
 *
 * @author hungnv.iist@gmail.com
 * @date 19/7/2019
 * 
 */
/**
 * @author computer
 *
 */
public class ExcelUtils {
	private static final char[] EXCEL_SHEET_NAME_INVALID_CHARS = { '/', '\\', '?', '*', ']', '[', ':' };
	private static final char INVALID_REPLACE_CHAR = '_';
	
	/**
	 * @param row
	 * @param colIndex
	 * @return
	 * @author hungnv.iist@gmail.com
	 * @date 19/7/2019
	 * 
	 */
	public static String getStringCellValue(Row row, int colIndex) {
		String result = null;
		if (row != null) {
			Cell cell = row.getCell(colIndex);
			if (cell != null) {
				try {
					result = cell.getStringCellValue();
				} catch (IllegalStateException e) {
					result = String.valueOf(getNumericCellValue(row, colIndex));
				}
			}
		}
		return result;
	}

	/**
	 * @param row
	 * @param colIndex
	 * @return
	 * @author hungnv.iist@gmail.com
	 * @date 19/7/2019
	 * 
	 */
	public static double getNumericCellValue(Row row, int colIndex) {
		double result = -1;
		if (row != null) {
			Cell cell = row.getCell(colIndex);
			if (cell != null) {
				try {
					result = cell.getNumericCellValue();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 * method get 1 row with index row
	 * @param sheet
	 * @param rowIndex
	 * @return Row
	 * @author hungnv.iist@gmail.com
	 * @date 19/7/2019
	 * 
	 */
	public static Row getRow(Sheet sheet, int rowIndex) {
		Row row = sheet.getRow(rowIndex);
		if (row == null) {
			row = sheet.createRow(rowIndex);
		}
		return row;
	}
/**
 * 
 * @param excelFilePath
 * @param obj
 * @return
 */
	public static List<String> getRow(String excelFilePath, Object obj) {
		int indexBeginHeader = 0;
		List<String> rows = new ArrayList<String>();
		Class<?> clazz = obj.getClass();
		SheetSerializable sheetSerializable = clazz.getDeclaredAnnotation(SheetSerializable.class);
		indexBeginHeader = sheetSerializable.indexBeginHeader();
		rows = getListToExcel(excelFilePath, obj).get(indexBeginHeader);
		return rows;
	}

	/**
	 * 
	 * @param excelFilePath
	 * @param obj
	 * @return
	 */
	public static List<List<String>> getListToExcel(String excelFilePath, Object obj) {
		List<List<String>> sheetDataTable = new ArrayList<List<String>>();
		 try {
			 Workbook excelWorkBook = getWorkbook(excelFilePath);
			// Get all excel sheet count.
			int totalSheetNumber = excelWorkBook.getNumberOfSheets();
			for (int i = 0; i < totalSheetNumber; i++) {
				// Get current sheet.
				Sheet sheet = excelWorkBook.getSheetAt(i);
				// Get sheet name.
				String sheetName = sheet.getSheetName();
				if((!sheetName.equals(null)) && sheetName.length() > 0) {
					sheetDataTable = getSheetDataList(sheet);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		 return sheetDataTable;
	}


	/**
	 * method get cell with index row and index column
	 * @param row
	 * @param columnIndex
	 * @return cell
	 * @author hungnv.iist@gmail.com
	 * @date 19/7/2019
	 * 
	 */
	public static Cell getCell(Row row, int columnIndex) {
		Cell cell = row.getCell(columnIndex);
		if (cell == null) {
			cell = row.createCell(columnIndex);
		}
		return cell;
	}

	/**
	 * set value double for cell 
	 * @param row
	 * @param columnIndex
	 * @param value
	 * @return cell value double
	 * @author hungnv.iist@gmail.com
	 * @date 19/7/2019
	 * 
	 */
	public static Cell setCellValue(Row row, int columnIndex, double value) {
		Cell cell = getCell(row, columnIndex);

		cell.setCellValue(value);

		return cell;
	}

	/**
	 * 
	 * @param row
	 * @param columnIndex
	 * @param value
	 * @return
	 * @author hungnv.iist@gmail.com
	 * @date 19/7/2019
	 * 
	 */
	public static Cell setCellValue(Row row, int columnIndex, String value) {
		Cell cell = getCell(row, columnIndex);
		try {
			cell.setCellValue(value);
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
			cell.setCellValue(value.substring(0, 32767));

		}
		return cell;
	}

	/**
	 * @param sheet
	 * @param fromRow
	 * @author hungnv.iist@gmail.com
	 * @date 19/7/2019
	 * 
	 */
	public static void removeRows(Sheet sheet, int fromRow) {
		removeRows(sheet, fromRow, -1);
	}

	/**
	 * @param sheet
	 * @param fromRow
	 * @param toRow
	 * @author hungnv.iist@gmail.com
	 * @date 19/7/2019
	 * 
	 */
	public static void removeRows(Sheet sheet, int fromRow, int toRow) {

		while (true) {

			Row row = sheet.getRow(fromRow++);

			if (row == null) {
				break;
			} else if (toRow >= 0 && fromRow == toRow) {
				break;
			}
			sheet.removeRow(row);
		}

	}

	/**
	 * @param workbook
	 * @param sheetName
	 * @author hungnv.iist@gmail.com
	 * @date 19/7/2019
	 * 
	 */
	public static void moveToLast(Workbook workbook, String sheetName) {
		workbook.setSheetOrder(sheetName, workbook.getNumberOfSheets() - 1);
	}

	/**
	 * @param workbook
	 * @param sheetName
	 * @return
	 * @author hungnv.iist@gmail.com
	 * @date 19/7/2019
	 * 
	 */
	public static int removeSheet(Workbook workbook, String sheetName) {

		int sheetIndex = workbook.getSheetIndex(sheetName);

		if (sheetIndex >= 0) {
			workbook.removeSheetAt(sheetIndex);
		}

		return sheetIndex;
	}

	/**
	 * @param workbook
	 * @param sheetName
	 * @param pos
	 * @author hungnv.iist@gmail.com
	 * @date 19/7/2019
	 * 
	 */
	public static void moveTo(Workbook workbook, String sheetName, int pos) {
		workbook.setSheetOrder(sheetName, pos);
	}

	/**
	 * @param rawSheetname
	 * @return
	 */
	public static String getSheetNameWithLimit(String rawSheetname) {
		return getSheetNameWithLimit(rawSheetname, false);
	}

	/**
	 * @param rawSheetname
	 * @param right
	 * @return
	 * @author hungnv.iist@gmail.com
	 * @date 19/7/2019
	 * 
	 */
	public static String getSheetNameWithLimit(String rawSheetname, boolean right) {

		String sheetname = right ? StringUtils.right(rawSheetname, 31) : StringUtils.left(rawSheetname, 31);

		// Replace invalid characters
		for (char c : EXCEL_SHEET_NAME_INVALID_CHARS) {
			sheetname = StringUtils.replaceChars(sheetname, c, INVALID_REPLACE_CHAR);
		}

		return sheetname;

	}

	/**
	 * @param prefix
	 * @param before
	 * @return
	 * @author hungnv.iist@gmail.com
	 * @date 19/7/2019
	 * 
	 */
	public static String getSheetNameWithLimit(String prefix, String before) {
		String result = before;

		if (!StringUtils.isEmpty(before)) {
			before = before.trim();
			if (before.length() > 31) {
				if (before.indexOf(prefix) == 0) {
					result = before.substring(prefix.length());
				} else {
					result = before;
				}
			}
		}

		if (!StringUtils.isEmpty(result) && result.length() > 31) {
			result = result.substring(result.length() - 31);
		}
		return result;

	}

	public static void addValidationData(Sheet sheet, String listFormula, int firstRow, int lastRow, int colIndex) {
		addValidationData(sheet, listFormula, firstRow, lastRow, colIndex, colIndex);
	}

	/**
	 * @param sheet
	 * @param listFormula
	 * @param firstRow
	 * @param lastRow
	 * @param firstCol
	 * @param lastCol
	 * @author hungnv.iist@gmail.com
	 * @date 19/7/2019
	 * 
	 */
	public static void addValidationData(Sheet sheet, String listFormula, int firstRow, int lastRow, int firstCol,
			int lastCol) {

		DataValidationHelper dataValidationHelper = null;
		if (sheet instanceof XSSFSheet) {

			dataValidationHelper = new XSSFDataValidationHelper((XSSFSheet) sheet);

		} else if (sheet instanceof HSSFSheet) {

			dataValidationHelper = new HSSFDataValidationHelper((HSSFSheet) sheet);

		}

		DataValidationConstraint dvConstraint = dataValidationHelper.createFormulaListConstraint(listFormula);

		CellRangeAddressList addressList = new CellRangeAddressList(firstRow, lastRow, firstCol, lastCol);

		DataValidation validation = dataValidationHelper.createValidation(dvConstraint, addressList);

		sheet.addValidationData(validation);
	}

	/**
	 * @param sheet
	 * @param firstRow
	 * @param lastRow
	 * @param firstCol
	 * @param lastCol
	 * @author hungnv.iist@gmail.com
	 * @date 19/7/2019
	 * 
	 */
	public static void mergeCell(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {
		sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
	}

	/**
	 * @param cellStyle
	 */
	public static void setThinBorder(CellStyle cellStyle) {
		setBorder(cellStyle, BorderStyle.THIN);
	}

	/**
	 * @param cellStyle
	 * @param borderStyle
	 * @author hungnv.iist@gmail.com
	 * @date 19/7/2019
	 * 
	 */
	public static void setBorder(CellStyle cellStyle, BorderStyle borderStyle) {

		short borderColor = IndexedColors.BLACK.getIndex();

		setBorder(cellStyle, borderStyle, borderStyle, borderStyle, borderStyle, borderColor, borderColor, borderColor,
				borderColor);
	}

	public static CellStyle getCellStyle(Workbook workbook, Cell cell) {

		CellStyle newCellStyle = workbook.createCellStyle();
		CellStyle currentCellStyle = cell.getCellStyle();

		if (currentCellStyle != null) {
			newCellStyle.cloneStyleFrom(currentCellStyle);
		}

		return newCellStyle;

	}

	/**
	 * @param cellStyle
	 * @param top
	 * @param right
	 * @param bottom
	 * @param left
	 * @param topColor
	 * @param rightColor
	 * @param bottomColor
	 * @param leftColor
	 */
	public static void setBorder(CellStyle cellStyle, BorderStyle top, BorderStyle right, BorderStyle bottom,
			BorderStyle left, short topColor, short rightColor, short bottomColor, short leftColor) {

		cellStyle.setBorderTop(top);
		cellStyle.setBorderRight(right);
		cellStyle.setBorderBottom(bottom);
		cellStyle.setBorderLeft(left);

		cellStyle.setTopBorderColor(topColor);
		cellStyle.setRightBorderColor(rightColor);
		cellStyle.setBottomBorderColor(bottomColor);
		cellStyle.setLeftBorderColor(leftColor);

	}

	/**
	 * @param cellStyle
	 * @author hungnv.iist@gmail.com
	 * @date 19/7/2019
	 * 
	 */
	public static void setCellAlignmentTopCenter(CellStyle cellStyle) {
		setCellAlignment(cellStyle, VerticalAlignment.TOP, HorizontalAlignment.CENTER);
	}

	/**
	 * @param cellStyle
	 * @author hungnv.iist@gmail.com
	 * @date 19/7/2019
	 * 
	 */
	public static void setCellAlignmentCenterLeft(CellStyle cellStyle) {
		setCellAlignment(cellStyle, VerticalAlignment.CENTER, HorizontalAlignment.LEFT);
	}

	/**
	 * @param cellStyle
	 * @author hungnv.iist@gmail.com
	 * @date 19/7/2019
	 * 
	 */
	public static void setCellAlignmentCenter(CellStyle cellStyle) {
		setCellAlignment(cellStyle, VerticalAlignment.CENTER, HorizontalAlignment.CENTER);
	}

	/**
	 * @param cellStyle
	 * @param verticalAlignment
	 * @param horizontalAlignment
	 * @author hungnv.iist@gmail.com
	 * @date 19/7/2019
	 * 
	 */
	public static void setCellAlignment(CellStyle cellStyle, VerticalAlignment verticalAlignment,
			HorizontalAlignment horizontalAlignment) {

		cellStyle.setVerticalAlignment(verticalAlignment);
		cellStyle.setAlignment(horizontalAlignment);

	}

	/**
	 * @param workbook
	 * @param cellStyle
	 * @author hungnv.iist@gmail.com
	 * @date 19/7/2019
	 * 
	 */
	public static void setHyperLinkStyle(Workbook workbook, CellStyle cellStyle) {

		Font font = workbook.createFont();

		font.setColor(IndexedColors.BLUE.getIndex());

		cellStyle.setFont(font);

	}

	/**
	 * @param workbook
	 * @param row
	 * @param columnIndex
	 * @param cellContent
	 * @param filePath
	 * @author hungnv.iist@gmail.com
	 * @date 19/7/2019
	 * 
	 */
	public static void setHyperLinkToFile(Workbook workbook, Row row, int columnIndex, String cellContent,
			String filePath) {

		// Get workbook creation helper
		CreationHelper creationHelper = workbook.getCreationHelper();

		// Create new hyperlink
		XSSFHyperlink hyperlink = (XSSFHyperlink) creationHelper.createHyperlink(HyperlinkType.FILE);

		// Set address value
		hyperlink.setAddress(filePath);

		// Set value and the link of cell
		Cell cell = setCellValue(row, columnIndex, cellContent);
		cell.setHyperlink(hyperlink);

		// Update hyperlink style
		CellStyle cellStyle = getCellStyle(workbook, cell);
		setHyperLinkStyle(workbook, cellStyle);
		cell.setCellStyle(cellStyle);

	}

	/**
	 * @param workbook
	 * @param row
	 * @param columnIndex
	 * @param cellContent
	 * @param linkToSheetName
	 * @param linkToCellIndex
	 *@author hungnv.iist@gmail.com
	 *@date 19/7/2019
	 * 
	 */
	public static void setHyperLinkToSheet(Workbook workbook, Row row, int columnIndex, String cellContent,
			String linkToSheetName, String linkToCellIndex) {

		// Get workbook creation helper
		CreationHelper creationHelper = workbook.getCreationHelper();

		// Create new hyperlink
		XSSFHyperlink hyperlink = (XSSFHyperlink) creationHelper.createHyperlink(HyperlinkType.DOCUMENT);

		// Build address value
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("'");
		stringBuilder.append(getSheetNameWithLimit(linkToSheetName));
		stringBuilder.append("'");
		stringBuilder.append("!");
		stringBuilder.append(linkToCellIndex);

		// Set address value
		String address = stringBuilder.toString();
		hyperlink.setAddress(address);

		// Set value and the link of cell
		Cell cell = setCellValue(row, columnIndex, cellContent);
		cell.setHyperlink(hyperlink);

		// Update hyperlink style
		CellStyle cellStyle = getCellStyle(workbook, cell);
		setHyperLinkStyle(workbook, cellStyle);
		cell.setCellStyle(cellStyle);
	}

	/**
	 * @param colName
	 * @return number column
	 *@author hungnv.iist@gmail.com
	 *@date 19/7/2019
	 * 
	 */
	public static int getColIndexByName(String colName) {
		int index = 0;
		String upper = colName.toUpperCase();
		char[]  arr = upper.toCharArray();
		index = arr[0] - 'A';
		return index;
	}

	/**
	 * @param excelFilePath
	 * @return workbook
	 * @throws IOException
	 *@author hungnv.iist@gmail.com
	 *@date 19/7/2019
	 * 
	 */
	public static Workbook  getWorkbook(String excelFilePath) throws IOException {
		Workbook workbook = null;
		if (excelFilePath.endsWith("xlsx")) {
			workbook = new XSSFWorkbook(new FileInputStream(excelFilePath));
		} else if (excelFilePath.endsWith("xls")) {
			workbook = new HSSFWorkbook(new FileInputStream(excelFilePath));
		} else {
			throw new IllegalArgumentException("The specified file is not Excel file");
		}
		return workbook;
	}

	/**
	 * reading file excel with begin row
	 * @param excelFilePath
	 * @param beginRow
	 * @return list 
	 *@author hungnv.iist@gmail.com
	 *@date 19/7/2019
	 * 
	 */
	public static  ArrayList<String> reading(String excelFilePath, int beginRow) {
		Workbook workbook;
		ArrayList<String> cells = new ArrayList<String>();
		try {
			workbook = getWorkbook(excelFilePath);

			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				Sheet sheet = workbook.getSheetAt(i);
				String rawSheetname = sheet.getSheetName();
				getSheetNameWithLimit(rawSheetname);

				for (Row row : sheet) {
					if(row.getRowNum() >= beginRow) {
						for (Cell cell : row) {
							cells.add(cell.toString().trim());
						}
					}
				}
			}

		}
		 catch (IOException e) {
				e.printStackTrace();
			}
		return cells;
		
	}

	/**
	 * reading file excel with row limit
	 * @param excelFilePath
	 * @param beginRow
	 * @param endRow
	 * @return list
	 *@author hungnv.iist@gmail.com
	 *@date 19/7/2019
	 * 
	 */
	public static  ArrayList<String> reading(String excelFilePath, int beginRow, int endRow) {
		Workbook workbook;
		ArrayList<String> cells = new ArrayList<String>();
		try {
			workbook = getWorkbook(excelFilePath);

			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				Sheet sheet = workbook.getSheetAt(i);
				String rawSheetname = sheet.getSheetName();
				getSheetNameWithLimit(rawSheetname);

				for (Row row : sheet) {
					if(row.getRowNum() >= beginRow && row.getRowNum() <= endRow) {
						for (Cell cell : row) {
							cells.add(cell.toString().trim());
						}
					}
				}
			}

		}
		 catch (IOException e) {
				e.printStackTrace();
			}
		return cells;
		
	}

	/**
	 * reading one row in file excel
	 * @param excelFilePath
	 * @param oneRow
	 * @return list 
	 *@author hungnv.iist@gmail.com
	 *@date 19/7/2019
	 * 
	 */
	public static  ArrayList<String> readingOneRow(String excelFilePath, int oneRow) {
		Workbook workbook;
		ArrayList<String> cells = new ArrayList<String>();
		try {
			workbook = getWorkbook(excelFilePath);

			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				Sheet sheet = workbook.getSheetAt(i);
				String rawSheetname = sheet.getSheetName();
				getSheetNameWithLimit(rawSheetname);

				for (Row row : sheet) {
					if(row.getRowNum() == oneRow) {
						for (Cell cell : row) {
							cells.add(cell.toString().trim());
						}
					}
				}
			}
		}
		 catch (IOException e) {
				e.printStackTrace();
			}
		return cells;
	}

	/**
	 * write string to file 
	 * @param data
	 * @param fileName
	 */
	public static void writeStringToFile (String data, String fileName) {
		try {
			// Get the output file absolute path.
			String filePath = com.iist.core.importdb.excel.common.util.StringUtils.getPathOutput()+fileName;
			// Create File, FileWriter and BufferedWriter object.
			File file = new File(filePath);
			FileWriter fw = new FileWriter(file);
			BufferedWriter buffWriter = new BufferedWriter(fw);
			// Write string data to the output file, flush and close the buffered writer object.
			buffWriter.write(data);
			buffWriter.flush();
			buffWriter.close();
			System.out.println(filePath + " has been created.");
		}catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
	}

	/**
	 *  get data to sheet from list nest list
	 * @param sheet
	 * @return lists
	 * @author hungnv.iist@gmail.com
	 *@date 19/7/2019
	 * 
	 */
	public static List<List<String>> getSheetDataList(Sheet sheet) {
		List<List<String>> lists = new ArrayList<List<String>>();
		int firstRowNum = sheet.getFirstRowNum();
		int lastRowNum = sheet.getLastRowNum();
		if(lastRowNum > 0) {
			for(int i= firstRowNum; i< lastRowNum+ 1; i++) {
				// Get current row object.
				Row row = sheet.getRow(i);
				// Get first and last cell number.
				int firstCellNum = row.getFirstCellNum();
				int lastCellNum = row.getLastCellNum();

				// Create a String list to save column data in a row.
				List<String> rowDataList = new ArrayList<String>();

				// Loop in the row cells.
				for (int j = firstCellNum; j < lastCellNum; j++) {
					// Get current cell.
					Cell cell = row.getCell(j);

					// Get cell type.
					CellType cellType = cell.getCellType();

					if (cellType.equals(CellType.NUMERIC)) {
						if (HSSFDateUtil.isCellDateFormatted(cell)) {
							String stringCellValue = cell.toString();
							rowDataList.add(stringCellValue);
						} else {
							double numberValue = cell.getNumericCellValue();
							String stringCellValue = BigDecimal.valueOf(numberValue).toPlainString();
							rowDataList.add(stringCellValue);
						}
					} else if (cellType.equals(CellType.STRING) ) {
						String cellValue = cell.getStringCellValue();
						rowDataList.add(cellValue);
					} else if (cellType.equals(CellType.BOOLEAN)) {
						boolean numberValue = cell.getBooleanCellValue();
						String stringCellValue = String.valueOf(numberValue);
						rowDataList.add(stringCellValue);
					} else if (cellType.equals(CellType.BLANK)) {
						rowDataList.add(StringPool.BLANK);
					}
				}
				lists.add(rowDataList);
			}
		}
		return lists;
	}

	public static boolean validateDataExcel(String pathFileExcel, Object obj) {
		boolean status = true;

		Class<?> clazz = obj.getClass();
		SheetSerializable sheetSerializable = clazz.getDeclaredAnnotation(SheetSerializable.class);
		String prmSheetName = ExcelUtils.getSheetNameWithLimit(sheetSerializable.sheetName().trim());
		int indexBeginRowData = sheetSerializable.indexBeginRowData();
		int headerIndexColumn = sheetSerializable.headerIndexColumn();
		Map<String, String> mapCellsValue = new HashMap<String, String>();

		List<String> rowHeaders = new ArrayList<String>();
		for (String rawString : ExcelUtils. getRow(pathFileExcel, obj)) {
			rowHeaders.add(com.iist.core.importdb.excel.common.util.StringUtils.convertStringToVar(rawString).toString());
			
		}

		for (Field field : clazz.getDeclaredFields()) {
			Element element = field.getAnnotation(Element.class);
			if (!element.equals(null)) {
				mapCellsValue.put(element.name(), element.type());
			}
		}
		System.out.println(mapCellsValue);

		
		try {
			Workbook excelWorkBook = getWorkbook(pathFileExcel);
			// Get all excel sheet count.
			int totalSheetNumber = excelWorkBook.getNumberOfSheets();
			for (int i = 0; i < totalSheetNumber; i++) {
				// Get current sheet.
				Sheet sheet = excelWorkBook.getSheetAt(i);
				// Get sheet name.
				String sheetName = ExcelUtils.getSheetNameWithLimit(sheet.getSheetName().trim());
				if((!sheetName.equals(null)) && sheetName.length() > 0 && sheetName.equals(prmSheetName)) {
					int lastRowNum = sheet.getLastRowNum();
					if(lastRowNum > 0) {
						for(int j= indexBeginRowData; j< lastRowNum+ 1; j++) {
							// Get current row object.
							Row row = sheet.getRow(j);
							// Get first and last cell number.
							int firstCellNum = headerIndexColumn;
							int lastCellNum = row.getLastCellNum();

							// Loop in the row cells.
							for (int k = firstCellNum; k < lastCellNum; k++) {
								// Get current cell.
								Cell cell = row.getCell(k);

								
								for ( String key : mapCellsValue.keySet() ) {
									for (String rowa : rowHeaders) {
										if (key.equals(rowa)) {
											CellType cellType = cell.getCellType();
											if (cellType.equals(CellType.STRING)) {
												if(!mapCellsValue.get(key).equals("String")) {
													System.err.println("Error format cell String at: "+ cell.getColumnIndex()+":"+ cell.getRowIndex());
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return status;
	}
}
