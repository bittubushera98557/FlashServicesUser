package com.FlashScreen.flashservice.vendor;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.FlashScreen.flashservice.App.AppController;
import com.FlashScreen.flashservice.Common.Const;
import com.FlashScreen.flashservice.Common.Shaved_shared_preferences;
import com.FlashScreen.flashservice.InnnerClass.History;
import com.FlashScreen.flashservice.InnnerClass.NavigationScreen;
import com.FlashScreen.flashservice.R;
import com.FlashScreen.flashservice.adapter.GridListAdapter;
import com.FlashScreen.flashservice.adapter.Pack;
import com.FlashScreen.flashservice.database.CityDb;
import com.FlashScreen.flashservice.database.ServiceDb;
import com.FlashScreen.flashservice.database.StateDb;
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


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.FlashScreen.flashservice.App.AppController;
import com.FlashScreen.flashservice.Common.Const;
import com.FlashScreen.flashservice.Common.Shaved_shared_preferences;
import com.FlashScreen.flashservice.R;
import com.FlashScreen.flashservice.adapter.GridListAdapter;
import com.FlashScreen.flashservice.adapter.Pack;
import com.FlashScreen.flashservice.database.CityDb;
import com.FlashScreen.flashservice.database.ServiceDb;
import com.FlashScreen.flashservice.database.StateDb;
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

public class VendorDetail extends AppCompatActivity {
    private Context context;
    String TAG="taggss",catId,getimage,getname;
    ListView listView;
    private GridListAdapter adapter;
    private ArrayList<Pack> arrayList_pack;
    ProgressDialog progressDialog;
    TextView service_history,title_name;
    TextView job_id,name_id,area_id,address_id,city_id,email_id,phone_id,service_id,problem_id,cost_id;
    String user,packId,OrderId;
    Shaved_shared_preferences shaved_shared_preferences;
    private ArrayList<String> arrayList;
    ImageView imageView;
    String  userId,name,mobile,email,password,member,packages,Seller,Packageid;
    CityDb cityDb;
    StateDb stateDb;
    ServiceDb serviceDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_screen);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        // actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }

        listView = (ListView) findViewById(R.id.list_view);
        imageView = (ImageView) findViewById(R.id.imageView);
        title_name = (TextView) findViewById(R.id.title_name);

        job_id = (TextView) findViewById(R.id.job_id);
        name_id = (TextView) findViewById(R.id.name_id);
        area_id = (TextView) findViewById(R.id.area_id);
        address_id = (TextView) findViewById(R.id.address_id);
        city_id = (TextView) findViewById(R.id.city_id);
        email_id = (TextView) findViewById(R.id.email_id);
        phone_id = (TextView) findViewById(R.id.phone_id);
        service_id = (TextView) findViewById(R.id.service_id);
        problem_id = (TextView) findViewById(R.id.problem_id);
        cost_id = (TextView) findViewById(R.id.cost_id);

        progressDialog  = new ProgressDialog(VendorDetail.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please Wait......");

        arrayList = new ArrayList<>();
        arrayList.clear();

        arrayList_pack=  new ArrayList<>();
        arrayList_pack.clear();

        cityDb = new CityDb(getApplicationContext());
        stateDb = new StateDb(getApplicationContext());
        serviceDb = new ServiceDb(getApplicationContext());

        shaved_shared_preferences =  new Shaved_shared_preferences(getApplicationContext());

        userId = shaved_shared_preferences.get_userid();
        user = shaved_shared_preferences.get_name();
        mobile = shaved_shared_preferences.get_phone();
        email = shaved_shared_preferences.get_user_email();

        Log.e("oooooooooo ", "--" + Packageid + "--" + packages);

        shaved_shared_preferences.set_packIdT("1");
        shaved_shared_preferences.set_packNameT("Free");

        service_history = (TextView)findViewById(R.id.service_history);

        Intent i = getIntent();
        OrderId = i.getStringExtra("OrderId");

        title_name.setText(getname);

        Log.e("imagee ", "---" + getimage);

        //   Picasso.with(getApplicationContext()).load(getimage).into(imageView);

        service_history.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(getApplicationContext(), History.class);
                startActivity(i);
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
                finish();
            }
        });

        packId = shaved_shared_preferences.get_packId();

        if (isOnline()) {

            FETCH(OrderId);

        } else

        {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();

        }



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
                Intent intent = new Intent(VendorDetail.this,VendorNavigation.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void FETCH(final String Idd)
    {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Const.URL_ORDER, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {

                            JSONArray jsonArray = response.getJSONArray("data");

                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(j);


                                String Id = jsonObject.getString("Id");


                                if(Idd.equalsIgnoreCase(Id)) {
                                    String category_Id = jsonObject.getString("category_Id");
                                    String customer_Id = jsonObject.getString("customer_Id");
                                    String service_Id = jsonObject.getString("service_Id");
                                    String service_price = jsonObject.getString("service_price");
                                    String state_Id = jsonObject.getString("state_Id");
                                    String city_Id = jsonObject.getString("city_Id");
                                    String area_name = jsonObject.getString("area_name");
                                    String date = jsonObject.getString("date");
                                    String time = jsonObject.getString("time");

                                    String State = stateDb.getName(state_Id);
                                    String city = cityDb.getName(city_Id);
                                    String category = serviceDb.getCat_Name(category_Id);
                                    String service = serviceDb.getService_Name(service_Id);

                                    job_id.setText(Id);
                                    name_id.setText(user);
                                    area_id.setText(city);
                                    address_id.setText(area_name);
                                    city_id.setText(State);
                                    email_id.setText(email);
                                    phone_id.setText(mobile);
                                    service_id.setText(category);
                                    problem_id.setText(service);


                                    if(service_price.equalsIgnoreCase("On call"))
                                    {
                                        cost_id.setText(service_price);

                                    }
                                    else {
                                        cost_id.setText("Rs. "+service_price);

                                    }

                                }

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
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

    public boolean isOnline() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getApplicationContext(). getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(VendorDetail.this,VendorNavigation.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);

    }
}
