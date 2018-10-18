/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.datafrominternet;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.datafrominternet.utilities.JsonParserUtils;
import com.example.android.datafrominternet.utilities.NetworkUtils;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<String> {


    EditText mSearchBoxEditText;
    TextView mUrlDisplayTextView;
    TextView mSearchResultsTextView;
    ProgressBar mProgressBar;
    TextView mErrorMessage;
    ImageView mProfilePic;

    private String data;
    private String imageUrl;
    private String GITHUB_URL ="query";

    private static final int GITHUB_QUERY_LOADER = 896;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mSearchBoxEditText = (EditText) findViewById(R.id.et_search_box);
        mUrlDisplayTextView = (TextView) findViewById(R.id.tv_url_display);
        mSearchResultsTextView = (TextView) findViewById(R.id.tv_github_search_results_json);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_circular);
        mErrorMessage = (TextView) findViewById(R.id.error_message);
        mProfilePic = (ImageView) findViewById(R.id.profile_pic);

        showResult();
        getSupportLoaderManager().initLoader(GITHUB_QUERY_LOADER,null,this);
    }

    public void showErrorMessage() {
        mErrorMessage.setVisibility(View.VISIBLE);
        mSearchResultsTextView.setVisibility(View.INVISIBLE);
        mProfilePic.setVisibility(View.INVISIBLE);
    }

    public void showResult() {
        mErrorMessage.setVisibility(View.INVISIBLE);
        mSearchResultsTextView.setVisibility(View.VISIBLE);
        mProfilePic.setVisibility(View.VISIBLE);
    }


    //function to call the buildURL in Network utils
    public void makeGithubSearch() {

        String searchParam = mSearchBoxEditText.getText().toString();

        if (TextUtils.isEmpty(searchParam)){
            mUrlDisplayTextView.setText("No query entered");
        }

        URL searchUrl = NetworkUtils.buildUrl(searchParam);
        mUrlDisplayTextView.setText(searchUrl.toString());

        Bundle queryBundle =new Bundle();
        queryBundle.putString(GITHUB_URL,searchUrl.toString());

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> loader = loaderManager.getLoader(GITHUB_QUERY_LOADER);

        if (loader == null){
            loaderManager.initLoader(GITHUB_QUERY_LOADER,queryBundle,this);
        }else {
            loaderManager.restartLoader(GITHUB_QUERY_LOADER,queryBundle,this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int idOfSelectedItem = item.getItemId();
        if (idOfSelectedItem == R.id.search_menu) {
            //builds the query using parameter provided and displays the URL
            makeGithubSearch();

            InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<String> onCreateLoader(int i, final Bundle bundle) {

        return new AsyncTaskLoader<String>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (bundle == null)
                    return;

                mProgressBar.setVisibility(View.VISIBLE);
                forceLoad();
            }

            @Override
            public String loadInBackground() {

                String urlGithub = bundle.getString(GITHUB_URL);
                if (urlGithub == null || TextUtils.isEmpty(urlGithub)){
                    return null;
                }

                try {
                    URL query = new URL(urlGithub);
                    String result = NetworkUtils.getResponseFromHttpUrl(query);

                    JsonParserUtils.JsonParser(result);
                    data = JsonParserUtils.ReturnName();

                    return data;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String dataRecieved) {

        mProgressBar.setVisibility(View.INVISIBLE);

        try {

            imageUrl = JsonParserUtils.ReturnImageUrl();

        } catch (Exception e) {
            e.printStackTrace();

        }

        if (dataRecieved != null && !dataRecieved.equals("")) {

            mSearchResultsTextView.setText(dataRecieved);
            //new ProfilePicture().execute(imageUrl);
            showResult();

        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    public class ProfilePicture extends AsyncTask<String, Void, Bitmap> {

        private Bitmap bmp;

        @Override
        protected Bitmap doInBackground(String... strings) {
            String url = strings[0];

            try {


                InputStream in = new java.net.URL(url).openStream();
                bmp = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            mProfilePic.setImageBitmap(bitmap);
        }
    }


}


