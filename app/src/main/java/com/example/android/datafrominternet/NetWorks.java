package com.example.android.datafrominternet;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.android.datafrominternet.utilities.JsonParserUtils;
import com.example.android.datafrominternet.utilities.NetworkUtils;

import java.io.InputStream;
import java.net.URL;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class NetWorks extends IntentService {

    public static final String GITHUB_URL = "com.example.android.datafrominternet.extra.PARAM2";

    static private String data;
    private String imageUrl;
    static Bitmap bitmap;

    public NetWorks() {
        super("NetWorks");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {

            String url = intent.getStringExtra(GITHUB_URL);

            try {

                URL query = new URL(url);
                String result = NetworkUtils.getResponseFromHttpUrl(query);

                JsonParserUtils.JsonParser(result);
                Log.e("ERZ","point4");

                imageUrl = JsonParserUtils.ReturnImageUrl();
                data = JsonParserUtils.ReturnName();

                InputStream in = new java.net.URL(url).openStream();
                bitmap = BitmapFactory.decodeStream(in);

            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public static Bitmap returnImage(){


        return bitmap;
    }

    public static String returnName(){
        return data;
    }

}
