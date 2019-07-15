package com.iist.core.importdb.excel.common.util;

import java.text.Normalizer;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class StringUtils {
	
	public static StringBuilder convertStringToVar(String rawString) {
		
		StringBuilder varConvert= new StringBuilder();
		String stringRemoveAccent = removeAccent(rawString);
		StringTokenizer varToken= new StringTokenizer(stringRemoveAccent);
		
		int j = 0;
		while(varToken.hasMoreTokens()){
			char arr[]= varToken.nextToken().toCharArray();
			if(j==0) {
				arr[0]= Character.toLowerCase(arr[0]);
			}else {
				arr[0]= Character.toUpperCase(arr[0]);
			}
			j++;
			for(int i=1;i<arr.length;i++) {
				arr[i]= Character.toLowerCase(arr[i]);
			}
			varConvert.append(arr);

		}
		return varConvert;
	}


	public static String removeAccent(String s) {
		String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(temp).replaceAll("").replace('đ','d').replace('Đ','D');
	}
}
