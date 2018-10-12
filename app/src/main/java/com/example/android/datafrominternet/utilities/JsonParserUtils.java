package com.example.android.datafrominternet.utilities;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JsonParserUtils  {

    public static Map<String,String> JsonParser (String rawData) throws Exception {

        Map<String,String> returnParsedData = new HashMap<>();
        JSONObject contents = new JSONObject(rawData);

        JSONObject items = contents.getJSONObject("items");
        returnParsedData.put("name",items.getString("login"));
        returnParsedData.put("url",items.getString("html_url"));


        return returnParsedData;
    }




}
