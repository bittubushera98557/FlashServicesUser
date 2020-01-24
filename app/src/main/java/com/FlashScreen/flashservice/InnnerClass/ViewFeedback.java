package com.FlashScreen.flashservice.InnnerClass;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.FlashScreen.flashservice.App.AppController;
import com.FlashScreen.flashservice.Common.Const;
import com.FlashScreen.flashservice.R;
import com.FlashScreen.flashservice.adapter.Notif;
import com.FlashScreen.flashservice.adapter.NotificationAdapter;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewFeedback extends AppCompatActivity {
    NotificationAdapter notificationAdapter;
    ArrayList<Notif> arrayList;
    ListView listView;
    String TAG="TAG";
    ProgressDialog progressDoalog;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_feedback);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        // actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }


        progressDoalog =  new ProgressDialog(ViewFeedback.this);
        progressDoalog.setMax(100);
        progressDoalog.setMessage("Please Wait...");
        progressDoalog.setProgressStyle(ProgressDialog.THEME_HOLO_LIGHT);

        listView = (ListView)findViewById(R.id.notification_list);

        arrayList =new ArrayList<>();
        arrayList.clear();

        if (isOnline()) {
            FetchType();

        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getApplicationContext(). getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    private void FetchType()
    {
        progressDoalog.show();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Const.URL_FETCH_FEEDBACK, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {

                            JSONArray jsonArray = response.getJSONArray("data");
                            int length = jsonArray.length()-1;

                            int len = 0;

                            if(length>50)
                            {
                                len = length - 50;
                            }
                            else {
                                len = 0;
                            }


                            for (int j = length ; j >= len; j--)
                            {

                                Log.e("values ","--"+j+"--"+len+"--"+length);

                                JSONObject jsonObject = jsonArray.getJSONObject(j);

                                Notif notif = new Notif();

                                String Id = jsonObject.getString("Id");
                                String title = jsonObject.getString("title");
                                String note = jsonObject.getString("note");
                                String date = jsonObject.getString("date");

                                Log.e("DATEES_567","--"+date);

                                notif.setId(Id);
                                notif.setTitle(title);
                                notif.setMessage(note);
                                notif.setDate(date);

                                arrayList.add(notif);
                            }
                            notificationAdapter = new NotificationAdapter(ViewFeedback.this,arrayList);

                            listView.setAdapter(notificationAdapter);

                            progressDoalog.hide();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDoalog.hide();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                "tag_json_obj");

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent i = new Intent(ViewFeedback.this, NavigationAdmin.class);
                startActivity(i);
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Override
    public void onBackPressed() {
        Intent i = new Intent(ViewFeedback.this, NavigationAdmin.class);
        startActivity(i);
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
        finish();
    }

}
