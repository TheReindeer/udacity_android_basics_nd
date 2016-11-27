package com.system.newsapp;

import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class NewsQueryUtils {

    public static final String LOG_TAG = NewsQueryUtils.class.getName();

    private NewsQueryUtils(){}

    public static List<News> extractFeatureFromJSON(String newsJSON){
        //Testing if the parameter String is null
        if (TextUtils.isEmpty(newsJSON)){
            return null;
        }

        //Creating an empty list in which the news objects will be added
        List<News> newsList = new ArrayList<>();

        //Trying to parse the JSON. It will throws JSONException if something goes wrong.
        try {
            //Create a JSONObject from the JSON response
            JSONObject baseJsonResponse = new JSONObject(newsJSON);

            //Extract the JSONObject associated with the key called response
            JSONObject newsJsonResponse = baseJsonResponse.getJSONObject("response");

            //Extract the JSONArray associated with the key called results
            JSONArray newsJsonArray = newsJsonResponse.getJSONArray("results");

            //Extract fields needed from each earthquake
            for (int i=0; i<newsJsonArray.length(); i++){
                //Get the current news item
                JSONObject currentNews = newsJsonArray.getJSONObject(i);

                //Declaring the needed items to be extracted
                String section = "";
                String title = "";
                String author = "";
                String date = "";
                String url = "";

                //Extract the news items, if they exist
                //Extract the section
                if (currentNews.has("sectionName")) {
                    section = currentNews.getString("sectionName");
                } else {
                    Log.i(LOG_TAG, "No section found!");
                }

                //Extract the title
                if (currentNews.has("webTitle")){
                    title = currentNews.getString("webTitle");
                } else {
                    Log.i(LOG_TAG, "No title found!");
                }

                //Extract the author
                if (currentNews.has("author")){
                    author = currentNews.getString("author");
                } else {
                    Log.i(LOG_TAG, "No author found!");
                }

                //Extract the date
                if (currentNews.has("webPublicationDate")){
                    date = currentNews.getString("webPublicationDate");
                } else {
                    Log.i(LOG_TAG, "No date found!");
                }

                //Extract the url
                if (currentNews.has("webUrl")){
                    url = currentNews.getString("webUrl");
                } else {
                    Log.i(LOG_TAG, "No url found!");
                }

                //Create the news object with the extracted values
                News news = new News(section, title, author, date, url);

                //Add the newly created news object in the list of news
                newsList.add(news);
            }

        } catch (JSONException e){
            Log.e(LOG_TAG, "Something went wrong while parsing the JSON results: ", e);
        }
        return newsList;
    }

    //Create URL from given String
    private static URL createUrl(String stringUrl){
        URL url = null;
        try{
            url = new URL(stringUrl);
        } catch (MalformedURLException e){
            Log.e(LOG_TAG, "Problem building the URL: ", e);
        }
        return url;
    }

    //Create and make the http request to the web api
    private static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse = "";

        if (url == null){
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Testing if the request was successful
            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e){
            Log.e(LOG_TAG, "Problem while reading the JSON results", e);
        } finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }

            if (inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    //Convert InputStream in String
    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null){
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    public static List<News> fetchNewsData(String requestUrl){
        //Create URL object
        URL url = createUrl(requestUrl);

        //Perform HTTP to the URL and receive a JSON response back
        String jsonResponse = null;
        try{
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e){
            Log.e(LOG_TAG, "Problem making the HTTP request: ", e);
        }

        //Extract the relevant fields from the JSON response
        List<News> newsList = extractFeatureFromJSON(jsonResponse);

        //Return the formatted news list
        return newsList;
    }
}
