package com.example.android.quakereport;

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

public class QueryUtils {
    public static List<Modu> fetchEarthquakeData (String requestUrl){
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "problem making http request", e);
        }
        List<Modu> earthquake = extractFeaturesFromJson(jsonResponse);
        return earthquake;
    }

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();
    private QueryUtils() {
    }

    private static URL createUrl(String stringUrl){
        URL url = null;
        try {
            url = new URL(stringUrl);
        }
        catch (MalformedURLException e){
            Log.e(LOG_TAG, "Problem building the url", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException{
        String jasonResponse = "";
        if (url==null){
            return jasonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000 /*milliseconds*/);
                urlConnection.setConnectTimeout(15000 /*milliseconds*/);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                if (urlConnection.getResponseCode()==200){
                    inputStream = urlConnection.getInputStream();
                    jasonResponse = readFromStream (inputStream);
                }else {
                    Log.e(LOG_TAG,"Error response code:" + urlConnection.getResponseCode());
                }
        }
        catch (IOException e){
            Log.e(LOG_TAG, "problem receiving the earthquake Json result",e);
        }
        finally {
            if (urlConnection!=null){
                urlConnection.disconnect();
            }
            if (inputStream!=null){
                inputStream.close();
            }
        }
        return jasonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if (inputStream!=null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line!=null){
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Modu} objects that has been built up from
     * parsing a JSON response.
     */
    private static List<Modu> extractFeaturesFromJson(String earthquakeJson) {
        if (TextUtils.isEmpty(earthquakeJson)){
            return null;
        }
        List<Modu> earthquakes = new ArrayList<>();


        try {

            JSONObject baseJsonResponse = new JSONObject(earthquakeJson);

            JSONArray earthquakeArray = baseJsonResponse.getJSONArray("features");

            for (int i=0; i<earthquakeArray.length(); i++){
                JSONObject currentEarthquakes =  earthquakeArray.getJSONObject(i);
                JSONObject properties = currentEarthquakes.getJSONObject("properties");
                Double magnitude = properties.getDouble("mag");
                String place = properties.getString("place");
                long time = properties.getLong("time");
                String url = properties.getString("url");

                Modu modu = new Modu(magnitude,place,time,url);
                earthquakes.add(modu);


            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }
}
