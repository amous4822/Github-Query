package com.example.android.datafrominternet.utilities;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JsonParserUtils  {

    public static String JsonParser (String rawData) throws Exception {

        //Map<String,String> returnParsedData = new HashMap<>();
        JSONObject contents = new JSONObject(rawData);

        JSONArray items = contents.getJSONArray("items");
        //returnParsedData.put("name",items.getString("login"));
        //returnParsedData.put("url",items.getString("html_url"));
        JSONObject returnParsedData = items.getJSONObject(0);
        String data = returnParsedData.getString("login");

        return data;
    }




}
