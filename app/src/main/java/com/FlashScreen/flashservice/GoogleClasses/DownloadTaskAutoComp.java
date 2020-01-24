package com.FlashScreen.flashservice.GoogleClasses;

import android.os.AsyncTask;
import android.util.Log;

import com.FlashScreen.flashservice.Common.Const;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tuff on 14-Apr-16.
 */
public class DownloadTaskAutoComp extends AsyncTask<String, Integer, String> {

    String data = null;

    // Invoked by execute() method of this object

    protected String doInBackground(String... url) {
        try {
            data = downloadUrlAutoComp(url[0]);
        } catch (Exception e) {
            Log.d("Background Task", e.toString());
        }
        return data;
    }

    // Executed after the complete execution of doInBackground() method
    @Override
    public void onPostExecute(String result) {

        // Instantiating ParserTask which parses the json data from Geocoding webservice
        // in a non-ui thread

        ParserTaskAutoComp parserTask = new ParserTaskAutoComp();

        // Start parsing the places in JSON format
        // Invokes the "doInBackground()" method of the class ParseTask
        parserTask.execute(result);


    }

    public class ParserTaskAutoComp extends AsyncTask<String, Integer, List<HashMap<String, String>>> {
        JSONObject jObject;

        // Invoked by execute() method of this object

        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;
            GeocodeJSONParser parser = new GeocodeJSONParser();
            JSONObject jObject;

            try {
                jObject = new JSONObject(jsonData[0]);

                /** Getting the parsed data as a an ArrayList */
                places = parser.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;

        }

        // Executed after the complete execution of doInBackground() method

        protected void onPostExecute(List<HashMap<String, String>> list) {

            // Clears all the existing markers

            // map.clear();

            for (int i = 0; i < list.size(); i++) {

                // Creating a marker
                //  MarkerOptions markerOptions = new MarkerOptions();

                // Getting a place from the places list
                HashMap<String, String> hmPlace = list.get(i);

                // Getting latitude of the place
                double lat = Double.parseDouble(hmPlace.get("lat"));

                // Getting longitude of the place
                double lng = Double.parseDouble(hmPlace.get("lng"));

                // Getting name
                String name = hmPlace.get("formatted_address");
                LatLng latLng = new LatLng(lat, lng);

                Log.e("--latitude-", "" + latLng.latitude);

                Log.e("--longitude-", "" + latLng.longitude);
                //-------------

                //---------------
                Const.searchedOrderLat=""+latLng.latitude;
                Const.searchedOrderLng=""+latLng.longitude;


            }
        }
    }

    private String downloadUrlAutoComp(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            br.close();

        } catch (Exception e) {
            Log.d("while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }

        return data;
    }
}