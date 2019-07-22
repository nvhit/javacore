package com.iist.core.importdb.excel.common.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.iist.core.importdb.arr.common.annotation.Element;
import com.iist.core.importdb.arr.common.annotation.SheetSerializable;
import com.iist.core.importdb.excel.common.constants.StringPool;

import net.sf.json.JSONObject;


/** Json Util
 * @author hungnv.iist@gmail.com
 * @date 19/7/2019
 *
 */
public class JsonUtils {
	private static ObjectMapper mapper = new ObjectMapper();

	/**
	 * pasrsing file json throw node 
	 * @param pathFileExcel
	 * @param fileName
	 * @param obj
	 */
	public static void parsingJsonFileForNode(String pathFileExcel, Object obj) {
		
		Class<?> clazz = obj.getClass();
		List<String> listNodeRemove = new ArrayList<String>();
		List<String> listNodeRow = ExcelUtils.getRow(pathFileExcel, obj);
		List<String> nodes = new ArrayList<String>();
		List<String> filterNode = new ArrayList<String>();
		ObjectMapper objectMapper = new ObjectMapper();

		SheetSerializable sheetSerializable = clazz.getDeclaredAnnotation(SheetSerializable.class);
		String sheetName = sheetSerializable.sheetName().trim();

		String filePath = com.iist.core.importdb.excel.common.util.StringUtils.getPathOutput() + sheetName +StringPool.PERIOD+StringPool.SUFFIX_JSON;
		for (String nodeRaw : listNodeRow) {
			StringBuilder node = com.iist.core.importdb.excel.common.util.StringUtils.convertStringToVar(nodeRaw);
			if(!node.toString().trim().equals(StringPool.BLANK)) {
				nodes.add(node.toString().trim());
			}
			
		}

		for (Field field : clazz.getDeclaredFields()) {
			Element element = field.getAnnotation(Element.class);
			if (!element.equals(null)) {
				listNodeRemove.add(element.name());
			}
		}

		filterNode=nodes.stream().filter(e -> !listNodeRemove.contains(e)).collect (Collectors.toList()); 

		try {
			JsonNode rootNode = mapper.readTree(new File(filePath));
			for (JsonNode root : rootNode) {
				((ObjectNode)root).remove(filterNode);
			}
			objectMapper.writeValue(new File(com.iist.core.importdb.excel.common.util.StringUtils.getPathOutput() + sheetName +StringPool.PERIOD+StringPool.SUFFIX_JSON), rootNode);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * method get data return json 
	 * @param dataTable: type List<List<String>>
	 * @param classObj : type Object use config index to annotation
	 * @return string json
	 * get data return json from list data
	 *@author hungnv.iist@gmail.com
	 *@date 19/7/2019
	 * 
	 */
	public static String getJSONStringFromList(List<List<String>> dataTable, Object obj) {
		String jsonString = StringPool.BLANK;
		String nodeName = StringPool.BLANK;

		int rowCount = dataTable.size();
		Class<?> clazz = obj.getClass();

		SheetSerializable sheetSerializable = clazz.getDeclaredAnnotation(SheetSerializable.class);
		int indexBeginHeader = sheetSerializable.indexBeginHeader();
		int indexBeginRowData  = sheetSerializable.indexBeginRowData();
		int headerIndexColumn = sheetSerializable.headerIndexColumn();
		

		if (!dataTable.equals(null)) {
			if (rowCount > 1) {
				//Create a JSONObject to store table data.
				net.sf.json.JSONObject tableJsonObject = new net.sf.json.JSONObject();
				// The first row is the header row, store each column name.
				List<String> headerRowsRaw = dataTable.get(indexBeginHeader);
				// The child header row
				List<String> childHeaderRowsRaw = dataTable.get(++indexBeginHeader);

				// convert vietkey to variable java
				List<String> headerRows = new ArrayList<String>();
				for (String headerRowRaw : headerRowsRaw) {
					StringBuilder headerRow = com.iist.core.importdb.excel.common.util.StringUtils.convertStringToVar(headerRowRaw);
					headerRows.add(headerRow.toString());
				}

				// convert vietkey to variable java
				List<String> childHeaderRows = new ArrayList<String>();
				for (String childHeaderRowRaw : childHeaderRowsRaw) {
					StringBuilder childHeaderRow = com.iist.core.importdb.excel.common.util.StringUtils.convertStringToVar(childHeaderRowRaw);
					childHeaderRows.add(childHeaderRow.toString());
				}

				JSONArray jsonArray = new JSONArray(); 
				for (int i= indexBeginRowData; i< rowCount; i++) {
					// Create a JSONObject object to store row data.
					JSONObject rowJsonObjectChild = new JSONObject();
					net.sf.json.JSONObject rowJsonObject = new net.sf.json.JSONObject();

					List<String> dataRow = dataTable.get(i);
					for (int j= headerIndexColumn; j < headerRows.size(); j++) {
						String columnObjectKey = childHeaderRows.get(j);
						String columnObjectValue = dataRow.get(j);
						String columnKey = headerRows.get(j);
						String columnValue = dataRow.get(j);

						if (!columnKey.equals(StringPool.BLANK)) {
							rowJsonObject.clear();
							nodeName = headerRows.get(j);
							rowJsonObjectChild.put(columnKey, columnValue);
						}

						if(!childHeaderRows.get(j).equals(StringPool.BLANK)){
							rowJsonObject.put(columnObjectKey, columnObjectValue);
							rowJsonObjectChild.put(nodeName, rowJsonObject);
						}

					}

					tableJsonObject.put(i,rowJsonObjectChild);
					jsonArray.put(rowJsonObjectChild);
				}

				jsonString = jsonArray.toString();
			}
		}
		return jsonString;
	}

	/**
	 *  create json file from excel
	 * @param 
	 * + excel file path <br>
	 * + object annotation use config index 
	 * 
	 * @return sheetDataTable
	 *@author hungnv.iist@gmail.com
	 *@date 19/7/2019
	 * 
	 */
	public static List<List<String>> createJSONFileFromExcel(String excelFilePath, Object obj) {
		Class<?> clazz = obj.getClass();
		SheetSerializable sheetSerializable = clazz.getDeclaredAnnotation(SheetSerializable.class);
		String prmSheetName = ExcelUtils.getSheetNameWithLimit(sheetSerializable.sheetName().trim());
		List<List<String>> sheetDataTable = new ArrayList<List<String>>();
		 try {
			 Workbook excelWorkBook = ExcelUtils.getWorkbook(excelFilePath);
			// Get all excel sheet count.
			int totalSheetNumber = excelWorkBook.getNumberOfSheets();
			for (int i = 0; i < totalSheetNumber; i++) {
				// Get current sheet.
				Sheet sheet = excelWorkBook.getSheetAt(i);
				// Get sheet name.
				String sheetName = ExcelUtils.getSheetNameWithLimit(sheet.getSheetName().trim());
				if((!sheetName.equals(null)) && sheetName.length() > 0 && sheetName.equals(prmSheetName)) {
					sheetDataTable = ExcelUtils.getSheetDataList(sheet);
					// Generate JSON format of above sheet data and write to a JSON file.
					String jsonString = getJSONStringFromList(sheetDataTable,obj);
					String jsonFileName = sheetName + StringPool.PERIOD+StringPool.SUFFIX_JSON;
					ExcelUtils.writeStringToFile(jsonString, jsonFileName);
				}else {
					System.err.println("Not exist sheet: "+prmSheetName);
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
	 * 
	 * @param pathFileExcel
	 * @param obj
	 */
	public static void convertDataExcelToJson(String pathFileExcel, Object obj) {
		JsonUtils.createJSONFileFromExcel(pathFileExcel , obj);
		JsonUtils.parsingJsonFileForNode(pathFileExcel, obj);
	}
}