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

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.datafrominternet.utilities.JsonParserUtils;
import com.example.android.datafrominternet.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    EditText mSearchBoxEditText;
    TextView mUrlDisplayTextView;
    TextView mSearchResultsTextView;
    ProgressBar mProgressBar;
    TextView mErrorMessage;
    Map<String,String> parsedData = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mSearchBoxEditText = (EditText) findViewById(R.id.et_search_box);
        mUrlDisplayTextView = (TextView) findViewById(R.id.tv_url_display);
        mSearchResultsTextView = (TextView) findViewById(R.id.tv_github_search_results_json);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_circular);
        mErrorMessage = (TextView) findViewById(R.id.error_message);

        showResult();
    }

    public void showErrorMessage() {

        mErrorMessage.setVisibility(View.VISIBLE);
        mSearchResultsTextView.setVisibility(View.INVISIBLE);

    }

    public void showResult() {

        mErrorMessage.setVisibility(View.INVISIBLE);
        mSearchResultsTextView.setVisibility(View.VISIBLE);

        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

    }


    //function to call the buildURL in Network utils
    public void makeGithubSearch() {

        String searchParam = mSearchBoxEditText.getText().toString();
        URL searchUrl = NetworkUtils.buildUrl(searchParam);
        mUrlDisplayTextView.setText(searchUrl.toString());

        new ConnectToInternet().execute(searchUrl);

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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class ConnectToInternet extends AsyncTask<URL, Void, String> {

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(URL... urls) {

            URL url = urls[0];
            String returnedResults = null;

            try {
                returnedResults = NetworkUtils.getResponseFromHttpUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return returnedResults;
        }

        @Override
        protected void onPostExecute(String returnedResults) {

            super.onPostExecute(returnedResults);
            mProgressBar.setVisibility(View.INVISIBLE);
            String parsedData = displayParsedData(returnedResults);

            if (parsedData != null && !parsedData.equals("")) {

                mSearchResultsTextView.setText(parsedData);
                showResult();

            } else {
                showErrorMessage();
            }

        }
    }

    public String displayParsedData(String raw){
        String data;

        try {
            //parsedData = JsonParserUtils.JsonParser(raw);
            data = JsonParserUtils.JsonParser(raw);
            return data;
        }catch (Exception e){
            e.printStackTrace();
            return "shit";
        }
    }


}


