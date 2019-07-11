package com.iist.core.excel.importdb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.iist.core.excel.common.CommonConst;
import com.iist.core.excel.common.SimpleModule;
import com.iist.core.excel.common.util.ExcelUtils;




/**
 * 
 * @author HungNV
 *
 */
public class ImportDBFromExcelFileModule extends SimpleModule {
	private final int BEGIN_ROW = 6;
	private String  excelFileImport = "";
	private String rawSheetname ="";
	private String repalceSheetName="";
	List<CellValueBean> listCellValues = new ArrayList<CellValueBean>();

	@Override
	protected void init(String[] args) {
		
		excelFileImport = args[1];
	}

	@Override
	protected int execute() {
		int exitCode = CommonConst.SUCCESS_CODE;
		CellValueBean cellValueBean = new CellValueBean();
		try {

			//check excel file exist
			File checkFileExcel = new File(excelFileImport);
			if (!checkFileExcel.exists()) {
				exitCode = CommonConst.ERROR_CODE;
				throw new FileNotFoundException("File not exist");
			}

			//open and reading file excel with path defined in props
			Workbook workbook = getWorkbook(excelFileImport);

			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				Sheet sheet = workbook.getSheetAt(i);
				rawSheetname = sheet.getSheetName();
				repalceSheetName = ExcelUtils.getSheetNameWithLimit(rawSheetname);

				for (Row row : sheet) {
					if(row.getRowNum()>= BEGIN_ROW) {
						cellValueBean = new CellValueBean(row.getCell(1).getStringCellValue().trim(),row.getCell(2).getStringCellValue().trim());
						listCellValues.add(cellValueBean);
					}
				}
			}
			for (CellValueBean ceBean : listCellValues) {
				System.out.println("list: " +ceBean.getMnv()+ ceBean.getHoTen());

			}

		} catch (Exception  e) {
			exitCode = CommonConst.ERROR_CODE;
			e.printStackTrace();

		}
		return exitCode;
	}

	@Override
	protected void destroy() {
		//
		
	}

	/**
	 * 
	 * @param inputStream
	 * @param excelFilePath
	 * @return
	 * @throws IOException
	 */
	public Workbook getWorkbook(String excelFilePath) throws IOException {
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
	

}
