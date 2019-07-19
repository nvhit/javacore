package com.iist.core.importdb.excel.common.test;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

;

public class JSONObjectSample {

    public static void main (String [] args) {
        String jsonString = new String("{\"per_page\": 3,\"total\": 12,\"data\": [{\"color\": \"#98B2D1\",\"year\": 2000,\"name\": \"cerulean\",\"id\": 1,\"pantone_value\": \"15-4020\" }, {\"color\": \"#C74375\",\"year\": 2001,\"name\": \"fuchsia rose\",\"id\": 2,\"pantone_value\": \"17-2031\" }], \"page\": 1,\"total_pages\": 4 }\r\n");
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            Iterator<String> keys = jsonObject.keys();
            while(keys.hasNext()) {
                String key = keys.next();
                System.out.println(key);
                if(jsonObject.get(key) instanceof JSONArray) {
                    JSONArray array = (JSONArray) jsonObject.get(key);
                    JSONObject object = (JSONObject) array.get(0);
                    Iterator<String> innerKeys = object.keys();
                    while(innerKeys.hasNext()) {
                        String innerKey = innerKeys.next();
                        System.out.println(innerKey);
                    }
                }
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}