package com.FlashScreen.flashservice.GoogleClasses;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.FlashScreen.flashservice.Common.Const;
import com.FlashScreen.flashservice.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by tuff on 14-Apr-16.
 */
public class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
    private ArrayList resultList;
    private static String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY2 =  "AIzaSyB0AlQDQNwZd5GNEk9ZZCwoomXMmU_Pcn0";
    String TAG="GooglePlacesAutocompleteAdapter ";
    public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
   super(context, textViewResourceId);
        Log.e(TAG+" URL", " "+PLACES_API_BASE    );

    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public String getItem(int index) {
        return resultList.get(index).toString();


    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    // Retrieve the autocomplete results.
                    resultList = autocomplete(constraint.toString()+" ludhiana punjab,india");

                    // Assign the data to the FilterResults
                    filterResults.values = resultList;
                    filterResults.count = resultList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };

        return filter;

    }

    public static ArrayList autocomplete(String input) {
        ArrayList resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            ///////////
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY2);
            // sb.append("&types=geocode");
            //  sb.append("&components=country:in");
            sb.append("&sensor=false");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));
            ////////////////


/*            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY2);
            sb.append("&types=geocode");
            //  sb.append("&components=country:in");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));*/
            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            // Load the results into a StringBuilder
            Log.d( "autocomplete URL", " " + url);
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e("autocomplete", "ErrorprocessingPlacesAPI URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e("autocomplete", "ErrorconnectingtPlacesAPI", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
            Log.d("autocomplete DATA", "" + jsonObj);
            // Extract the Place descriptions from the results
            resultList = new ArrayList(predsJsonArray.length());
            Const.place_id=new  String[predsJsonArray.length()];
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));

           //     Log.e("-----place_id", predsJsonArray.getJSONObject(i).getString("place_id"));
                Const.place_id[i]=predsJsonArray.getJSONObject(i).getString("place_id");
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }





            /*// Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");
            Log.d("DATA", "" + jsonObj);
            // Extract the Place descriptions from the results
            resultList = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }*/
        } catch (JSONException e) {
            Log.e("autocomplete", "CanprocessJSONresults", e);
        }

        return resultList;
    }

}