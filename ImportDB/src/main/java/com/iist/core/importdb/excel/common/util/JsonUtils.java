package com.iist.core.importdb.excel.common.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.iist.core.importdb.arr.common.annotation.Element;
import com.iist.core.importdb.excel.common.constants.StringPool;

import net.sf.json.JSONObject;



/**
 * 
 * @author hungnv.iist@gmail.com
 *
 */
public class JsonUtils {
	private static final ObjectMapper mapper = new ObjectMapper();
	public static void parsingJsonFileForNode(String fileName, Object obj) {
		Class<?> clazz = obj.getClass();
		List<String> listNode = new ArrayList<String>();
		for (Field field : clazz.getDeclaredFields()) {
			Element element = field.getAnnotation(Element.class);
			if (element != null) {
				listNode.add(element.name());
			}
		}
		ObjectMapper objectMapper = new ObjectMapper();
		String filePath = com.iist.core.importdb.excel.common.util.StringUtils.getPathOutput()+fileName;
		try {
			JsonNode rootNode = mapper.readTree(new File(filePath));
			for (JsonNode root : rootNode) {
				((ObjectNode)root).remove(listNode);

			}
			objectMapper.writeValue(new File(com.iist.core.importdb.excel.common.util.StringUtils.getPathOutput()+"outputfile.json"), rootNode);
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
		String ret = StringPool.BLANK;
		String nodeName = StringPool.BLANK;
		List<String> headerRows = new ArrayList<String>();
		int indexBeginHeader = 0;
		Class<?> clazz = obj.getClass();

		for (Field field : clazz.getDeclaredFields()) {
			Element element = field.getAnnotation(Element.class);
			if (element != null) {
				indexBeginHeader = element.indexBeginHeader();
			}
		}
		if (dataTable != null) {
			int rowCount = dataTable.size();
			if (rowCount > 1) {
				//Create a JSONObject to store table data.
				net.sf.json.JSONObject tableJsonObject = new net.sf.json.JSONObject();
				// The first row is the header row, store each column name.
				List<String> headerRowsRaw = dataTable.get(indexBeginHeader);
				// The child header row
				List<String> childHeaderRowsRaw = dataTable.get(3);

				
				for (String headerRowRaw : headerRowsRaw) {
					StringBuilder headerRow = com.iist.core.importdb.excel.common.util.StringUtils.convertStringToVar(headerRowRaw);
					headerRows.add(headerRow.toString());
				}

				List<String> childHeaderRows = new ArrayList<String>();
				for (String childHeaderRowRaw : childHeaderRowsRaw) {
					StringBuilder childHeaderRow = com.iist.core.importdb.excel.common.util.StringUtils.convertStringToVar(childHeaderRowRaw);
					childHeaderRows.add(childHeaderRow.toString());
				}

				JSONArray jsonArray = new JSONArray(); 
				for (int i= 5; i< rowCount; i++) {
					// Create a JSONObject object to store row data.
					JSONObject rowJsonObjectChild = new JSONObject();
					net.sf.json.JSONObject rowJsonObject = new net.sf.json.JSONObject();

					List<String> dataRow = dataTable.get(i);
					for (int j= 0; j < headerRows.size(); j++) {
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

				ret = jsonArray.toString();
			}
		}
		return ret;
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
	public static List<List<String>> creteJSONFileFromExcel(String excelFilePath, Object obj) {
		List<List<String>> sheetDataTable = new ArrayList<List<String>>();
		 try {
			 Workbook excelWorkBook = ExcelUtils.getWorkbook(excelFilePath);
			// Get all excel sheet count.
			int totalSheetNumber = excelWorkBook.getNumberOfSheets();
			for (int i = 0; i < totalSheetNumber; i++) {
				// Get current sheet.
				Sheet sheet = excelWorkBook.getSheetAt(i);
				// Get sheet name.
				String sheetName = sheet.getSheetName();
				if(sheetName != null && sheetName.length() > 0) {
					sheetDataTable = ExcelUtils.getSheetDataList(sheet);
					// Generate JSON format of above sheet data and write to a JSON file.
					String jsonString = getJSONStringFromList(sheetDataTable,obj);
					ExcelUtils.getSheetNameWithLimit(sheetName);
					String jsonFileName = ExcelUtils.getSheetNameWithLimit(sheetName) + StringPool.PERIOD+StringPool.SUFFIX_JSON;
					ExcelUtils.writeStringToFile(jsonString, jsonFileName);
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
	 * get JSON from list
	 * @param dataTable
	 * @return String
	 * @author hungnv.iist@gmail.com
	 * @date 19/7/2019
	 */
	public static String getJSONStringFromList(List<List<String>> dataTable) {
		JSONObject rowJsonObjectChild = new JSONObject();
		JSONObject rowJsonObject = new JSONObject();
		JSONObject tableJsonObject = new JSONObject();

		String ret = StringPool.BLANK;
		if (dataTable != null) {
			int rowCount = dataTable.size();
			if (rowCount > 1) {
				// The first row is the header row, store each column name.
				List<String> headerRowsRaw = dataTable.get(2);
				// The child header row
				List<String> childHeaderRowsRaw = dataTable.get(3);

				List<String> headerRows = new ArrayList<String>();
				for (String headerRowRaw : headerRowsRaw) {
					StringBuilder headerRow = com.iist.core.importdb.excel.common.util.StringUtils.convertStringToVar(headerRowRaw);
					headerRows.add(headerRow.toString());
				}

				List<String> childHeaderRows = new ArrayList<String>();
				for (String childHeaderRowRaw : childHeaderRowsRaw) {
					StringBuilder childHeaderRow = com.iist.core.importdb.excel.common.util.StringUtils.convertStringToVar(childHeaderRowRaw);
					childHeaderRows.add(childHeaderRow.toString());
				}

				// Loop in the row data list.
				for (int i= 5; i< rowCount; i++) {
					// Create a JSONObject object to store row data.
					
					
					String nodeName = StringPool.BLANK;
					List<String> dataRow = dataTable.get(i);
					
					for (int j= 0; j < headerRows.size(); j++) {
						
						String columnKey = headerRows.get(j);
						String columnValue = dataRow.get(j);

						String columnObjectKey = childHeaderRows.get(j);
						String columnObjectValue = dataRow.get(j);
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

					tableJsonObject.putAll(rowJsonObjectChild);
				}
				ret = tableJsonObject.toString();
			}
		}
		return ret;
	}
}
