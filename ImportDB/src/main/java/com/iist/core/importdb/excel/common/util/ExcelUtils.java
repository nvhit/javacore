package com.iist.core.importdb.excel.common.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.usermodel.HSSFDataValidationHelper;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
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






/**
 * 
 * @author HungNV
 *
 */
public class ExcelUtils {
	private static final char[] EXCEL_SHEET_NAME_INVALID_CHARS = { '/', '\\', '?', '*', ']', '[', ':' };
	private static final char INVALID_REPLACE_CHAR = '_';


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

	public static Row getRow(Sheet sheet, int rowIndex) {
		Row row = sheet.getRow(rowIndex);
		if (row == null) {
			row = sheet.createRow(rowIndex);
		}
		return row;
	}

	public static Cell getCell(Row row, int columnIndex) {
		Cell cell = row.getCell(columnIndex);
		if (cell == null) {
			cell = row.createCell(columnIndex);
		}
		return cell;
	}

	public static Cell setCellValue(Row row, int columnIndex, double value) {
		Cell cell = getCell(row, columnIndex);

		cell.setCellValue(value);

		return cell;
	}

	public static Cell setCellValue(Row row, int columnIndex, String value) {
		Cell cell = getCell(row, columnIndex);
		try {
			cell.setCellValue(value);
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
			System.out.println(value);
			cell.setCellValue(value.substring(0, 32767));

		}
		return cell;
	}

	public static void removeRows(Sheet sheet, int fromRow) {
		removeRows(sheet, fromRow, -1);
	}

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

	public static void moveToLast(Workbook workbook, String sheetName) {
		workbook.setSheetOrder(sheetName, workbook.getNumberOfSheets() - 1);
	}

	public static int removeSheet(Workbook workbook, String sheetName) {

		int sheetIndex = workbook.getSheetIndex(sheetName);

		if (sheetIndex >= 0) {
			workbook.removeSheetAt(sheetIndex);
		}

		return sheetIndex;
	}

	public static void moveTo(Workbook workbook, String sheetName, int pos) {
		workbook.setSheetOrder(sheetName, pos);
	}

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

	public static void mergeCell(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {
		sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
	}

	public static void setThinBorder(CellStyle cellStyle) {
		setBorder(cellStyle, BorderStyle.THIN);
	}

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

	public static void setCellAlignmentTopCenter(CellStyle cellStyle) {
		setCellAlignment(cellStyle, VerticalAlignment.TOP, HorizontalAlignment.CENTER);
	}

	public static void setCellAlignmentCenterLeft(CellStyle cellStyle) {
		setCellAlignment(cellStyle, VerticalAlignment.CENTER, HorizontalAlignment.LEFT);
	}

	public static void setCellAlignmentCenter(CellStyle cellStyle) {
		setCellAlignment(cellStyle, VerticalAlignment.CENTER, HorizontalAlignment.CENTER);
	}

	public static void setCellAlignment(CellStyle cellStyle, VerticalAlignment verticalAlignment,
			HorizontalAlignment horizontalAlignment) {

		cellStyle.setVerticalAlignment(verticalAlignment);
		cellStyle.setAlignment(horizontalAlignment);

	}

	public static void setHyperLinkStyle(Workbook workbook, CellStyle cellStyle) {

		Font font = workbook.createFont();

		font.setColor(IndexedColors.BLUE.getIndex());

		cellStyle.setFont(font);

	}

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

	public static int getColIndexByName(String colName) {
		int index = 0;
		String upper = colName.toUpperCase();
		char[]  arr = upper.toCharArray();
		index = arr[0] - 'A';
		return index;
	}

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

}
