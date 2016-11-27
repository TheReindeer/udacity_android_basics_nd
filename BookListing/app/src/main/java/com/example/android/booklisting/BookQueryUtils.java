package com.example.android.booklisting;

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

/**
 * Created by john on 01.11.2016.
 */

public class BookQueryUtils {

    public static final String LOG_TAG = BookQueryUtils.class.getName();
    static String author;

    private BookQueryUtils(){
        Log.v(LOG_TAG, "constructor");
    }

    public static List<Book> extractFeatureFromJSON(String bookJSON){
        Log.v(LOG_TAG, "extractFeatureFromJSON");
        if (TextUtils.isEmpty(bookJSON)){
            return null;
        }

        List<Book> books = new ArrayList<>();

        try{
            //Get the root JSONObject
            JSONObject baseJSONResponse = new JSONObject(bookJSON);

            //Get the items array
            JSONArray booksArray = baseJSONResponse.getJSONArray("items");

            for (int i=0; i<booksArray.length(); i++){
                //Get the current book
                JSONObject currentBook = booksArray.getJSONObject(i);

                //Get the url
                String url = currentBook.getString("selfLink");

                //Get the volumeInfo JSONObject where title and authors are stored
                JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");

                //Get the title
                String title = volumeInfo.getString("title");

                //Get the authors, if they exist as array and concatenate them to a single String
                if(volumeInfo.has("authors")) {
                    JSONArray authors = volumeInfo.getJSONArray("authors");
                    if (authors != null) {
                        author = "";
                        for (int j = 0; j < authors.length(); j++) {
                            if (authors.getString(j) != null) {
                                String returnedAuthor = authors.getString(j);
                                author += returnedAuthor + "; ";
                            }
                        }
                    }
                } else {
                    Log.i(LOG_TAG, "No author for current book.");
                }

                //Create a new Book with the attributes tite, author and url
                Book book = new Book(author, title, url);

                //Add the previous book to the list of books.
                books.add(book);
            }
        } catch(JSONException e){
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results ", e);
        }
        return books;
    }

    private static URL createUrl(String stringUrl){
        Log.v(LOG_TAG, "createUrl");
        URL url = null;
        try{
            url = new URL(stringUrl);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        Log.v(LOG_TAG, "readFromStream");
        StringBuilder output = new StringBuilder();
        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line!= null){
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    private static String makeHttpRequest(URL url) throws IOException{
        Log.v(LOG_TAG, "makeHttpRequest");
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
                Log.e(LOG_TAG, "Error response code: "+urlConnection.getResponseCode());
            }
        }catch (IOException e){
            Log.e(LOG_TAG, "Problem retrieving the books JSON results.", e);
        }finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }

            if (inputStream != null){
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    public static List<Book> fetchBookData(String requestUrl){
        Log.v(LOG_TAG, "fetchBookData");
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try{
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e){
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<Book> books = extractFeatureFromJSON(jsonResponse);
        return books;
    }
}
