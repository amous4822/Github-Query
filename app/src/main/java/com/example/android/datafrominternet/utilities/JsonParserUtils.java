package com.example.android.datafrominternet.utilities;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class JsonParserUtils  {

    private static String data;
    private static String imageUrl;

    public static void JsonParser (String rawData) throws Exception {

        //Map<String,String> returnParsedData = new HashMap<>();
        JSONObject contents = new JSONObject(rawData);

        JSONArray items = contents.getJSONArray("items");
        //returnParsedData.put("name",items.getString("login"));
        //returnParsedData.put("url",items.getString("html_url"));
        JSONObject returnParsedData = items.getJSONObject(0);
        data = returnParsedData.getString("login");
        imageUrl = returnParsedData.getString("avatar_url");

    }

    public static String ReturnName(){
        return data;
    }

    public static String ReturnImageUrl(){
        return imageUrl;
    }





}
