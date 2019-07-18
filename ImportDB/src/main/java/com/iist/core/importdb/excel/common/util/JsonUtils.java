package com.iist.core.importdb.excel.common.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.iist.core.importdb.arr.common.annotation.Element;

import org.json.simple.JSONObject;

public class JsonUtils {
	private static final ObjectMapper mapper = new ObjectMapper();
	 public static void parsingJsonFile(String fileName, Object obj) {
		 String filePath = com.iist.core.importdb.excel.common.util.StringUtils.getPathOutput()+fileName;
		 ObjectMapper mapper = new ObjectMapper();

		 try {
			 	obj = mapper.readValue(new File(filePath), obj.getClass());

				mapper.writeValue(new File(com.iist.core.importdb.excel.common.util.StringUtils.getPathOutput()+ fileName), obj);
				System.out.println(obj);
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}	
	}

	public static StringBuilder parsingJsonFileFormatArray(String fileName, Object obj) {
		StringBuilder stringBuilder = new StringBuilder();
		String filePath = com.iist.core.importdb.excel.common.util.StringUtils.getPathOutput()+fileName;
		 try {

			ObjectMapper mapper = new ObjectMapper();

			Object[] myObjects = mapper.readValue(new File(filePath), Object[].class);
			
			for (Object o : myObjects) {
				stringBuilder.append(o+"\n");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return stringBuilder;
	}

	public static void parsingJsonFileForNode(String fileName, Object obj) {

		Class<?> clazz = obj.getClass();
		List<String> listNode = new ArrayList<String>();
		for (Field field : clazz.getDeclaredFields()) {
			Element element = field.getAnnotation(Element.class);
			
			if (element != null) {
				listNode.add(element.name());
				//System.out.println(listNode);
			}
		}
		//System.out.println(listNode);
		ObjectNode objectNode = null ;
		String filePath = com.iist.core.importdb.excel.common.util.StringUtils.getPathOutput()+fileName;
		BufferedWriter str;
		try {
			JsonNode rootNode = mapper.readTree(new File(filePath));
			for (String node : listNode) {
				for (JsonNode root : rootNode) {
					
					objectNode = (ObjectNode) root;
					objectNode.remove(node);
				
				}
				
			}
			File file = new File(filePath);

			FileWriter fw = new FileWriter(file);

			str = new BufferedWriter(fw);

			str.write(str.toString());

			str.flush();

			str.close();

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
}
