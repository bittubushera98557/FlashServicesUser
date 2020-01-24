package com.FlashScreen.flashservice.InnnerClass;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.FlashScreen.flashservice.App.AppController;
import com.FlashScreen.flashservice.Common.Const;
import com.FlashScreen.flashservice.Common.Shaved_shared_preferences;
import com.FlashScreen.flashservice.R;
import com.FlashScreen.flashservice.adapter.Player;
import com.FlashScreen.flashservice.database.CityDb;
import com.FlashScreen.flashservice.database.ServiceDb;
import com.FlashScreen.flashservice.database.StateDb;
import com.FlashScreen.flashservice.startscreen.SplashScreen;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VendorSignup extends AppCompatActivity implements View.OnClickListener {
    Shaved_shared_preferences shaved_shared_preferences;
    ServiceDb serviceDb;
    StateDb stateDb;
    CityDb cityDb;
    ProgressDialog progressDialog;
    String STATE_ID, CITY_ID, STATE_IDD = "0", CITY_IDD = "0", STATE, CITY, AREA;
    ArrayList<String> arrayListService, arrayList_State, arrayList_StateId, arrayList_City, arrayList_CityId;

    ArrayList<HashMap<String, String>> hash_arrayList;
    ArrayAdapter<String> stateadp, cityadp, servicesAdap;
    String TAG = "VendorSignup ";
    Spinner spinner_category;
    Spinner spinner_state;
    SearchableSpinner spinner_city;
    LinearLayout linear;
    Boolean bol = false;
    TextView signUp_Customer;

    EditText input_name, input_mobile;
    private String str_phone,str_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_signup);
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
        progressDialog = new ProgressDialog(VendorSignup.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please Wait Fetching Areas......");
        progressDialog.setCancelable(false);



        stateDb = new StateDb(getApplicationContext());
        cityDb = new CityDb(getApplicationContext());

        arrayList_StateId = new ArrayList<>();
        arrayList_State = new ArrayList<>();
        arrayList_StateId.clear();
        arrayList_State.clear();

        arrayList_CityId = new ArrayList<>();
        arrayList_City = new ArrayList<>();
        arrayList_CityId.clear();
        arrayList_City.clear();

        arrayListService = new ArrayList<>();
        arrayList_State.clear();
        arrayList_StateId.clear();

        arrayList_State.add("Select City");
        arrayList_StateId.add("0");

        spinner_category = (Spinner) findViewById(R.id.spinner_category);
        spinner_state = (Spinner) findViewById(R.id.spinner_city);
        spinner_city = (SearchableSpinner) findViewById(R.id.spinner_area);
        linear = (LinearLayout) findViewById(R.id.linear);
        signUp_Customer = (TextView) findViewById(R.id.signUp_Customer);
        input_name = (EditText) findViewById(R.id.input_name);
        input_mobile = (EditText) findViewById(R.id.input_mobile);


        signUp_Customer.setOnClickListener(this);
        shaved_shared_preferences = new Shaved_shared_preferences(getApplicationContext());
        if (AppController.getInstance().isOnline()) {

            Fetch_Category();
            FETCH_STATE();

        } else {

            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

        spinner_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isOnline()) {

                    STATE_ID = arrayList_StateId.get(position);
                    STATE = arrayList_State.get(position);

                    Log.e("state123..", "-----" + STATE_ID);
                    Log.e("state123..", "-----" + STATE);

                    if (STATE_ID.equalsIgnoreCase("0")) {
                        bol = false;
                        linear.setVisibility(View.GONE);
                        arrayList_City.clear();
                        arrayList_City.clear();


                        spinner_city.setSelection(position);

                        for (int k = 0; k < arrayList_StateId.size(); k++) {
                            String state = arrayList_StateId.get(k);
                            if (state.equalsIgnoreCase(STATE_IDD)) {
                                spinner_state.setSelection(k);
                            }
                        }

                    } else {
                        bol = false;
                        linear.setVisibility(View.VISIBLE);
                        FETCH_CITY(STATE_ID);

                    }

                } else {

                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /////////////////////////////

        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                CITY_ID = arrayList_CityId.get(position);
                CITY = arrayList_City.get(position);

                Log.e("city123..", "-----" + CITY_ID);
                Log.e("city123..", "-----" + CITY);

                if (CITY_ID.equalsIgnoreCase("0")) {
                    bol = false;

                    for (int k = 0; k < arrayList_CityId.size(); k++) {
                        String city = arrayList_CityId.get(k);
                        if (city.equalsIgnoreCase(CITY_IDD)) {
                            spinner_city.setSelection(k);
                        }
                    }


                } else {
                    bol = true;
                    CITY_ID = arrayList_CityId.get(position);
                    CITY = arrayList_City.get(position);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void FETCH_CITY(final String mystate) {
        progressDialog.show();

        arrayList_City.clear();
        arrayList_CityId.clear();

        Log.e(TAG+"mystate  ..", "-----" + mystate);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Const.URL_FETCH_CITY, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG+"FETCH_CITY res", response.toString());

                        try {

                            JSONArray jsonArray = response.getJSONArray("data");

                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(j);

                                String state_Id = jsonObject.getString("state_Id");

                                if (mystate.equalsIgnoreCase(state_Id)) {
                                    String cityId = jsonObject.getString("cityId");
                                    String city = jsonObject.getString("city");

                                    arrayList_CityId.add(cityId);
                                    arrayList_City.add(city);

                                    cityDb.insertRecord(cityId, city);
                                }
                            }
                            cityadp = new ArrayAdapter<String>(VendorSignup.this, R.layout.textview, arrayList_City) {

                                @TargetApi(Build.VERSION_CODES.M)
                                @Override
                                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                    View v = null;
                                    v = super.getDropDownView(position, null, parent);
                                    // If this is the selected item position

                                    // for other views
                                    TextView text = (TextView) v.findViewById(R.id.textvw);
                                    text.setTextColor(Color.parseColor("#3d406b"));
                                    Drawable backgroundColor = getResources().getDrawable(R.drawable.box);
                                    v.setBackground(backgroundColor);
   return v;
                                }
                            };


                            // Drop down layout style - list view with radio button
                            cityadp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            cityadp.setDropDownViewResource(R.layout.textview);
                            spinner_city.setAdapter(cityadp);

                        } catch (Exception e) {
                            Log.e(TAG+"FETCH_CITY Exce", e.toString());

                            e.printStackTrace();
                        }

                        progressDialog.hide();

                        if (CITY_IDD.equalsIgnoreCase("0")) {

                        } else {
                            for (int h = 0; h < arrayList_CityId.size(); h++) {
                                if (CITY_IDD.equalsIgnoreCase(arrayList_CityId.get(h))) {
                                    spinner_city.setSelection(h);
                                }
                            }
                        }
                        progressDialog.hide();
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
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public boolean isOnline() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    private void Fetch_Category() {
        progressDialog.show();
        Log.d(TAG, Const.URL_FETCH_CAT);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Const.URL_FETCH_CAT, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "Fetch_Category res"+ response.toString());

                        try {

                            JSONArray jsonArray = response.getJSONArray("data");

                            for (int j = 0; j < jsonArray.length(); j++) {
                                Player player = new Player();

                                JSONObject jsonObject = jsonArray.getJSONObject(j);

                                String category_Id = jsonObject.getString("category_Id");
                                String category_name = jsonObject.getString("category_name");
                                String image_upload1 = jsonObject.getString("image_upload1");
                                String date = jsonObject.getString("date");

                             /*   player.setCategory_Id(category_Id);
                                player.setCategory_name(category_name);
                                player.setImage_upload(image_upload1);
                                player.setDate(date);
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("category_Id", category_Id);
                                hashMap.put("category_name", category_name);
                                hash_arrayList.add(hashMap);*/
                                arrayListService.add(category_name);
                            }
                            servicesAdap = new ArrayAdapter<String>(VendorSignup.this, R.layout.textview, arrayListService) {

                                @TargetApi(Build.VERSION_CODES.M)
                                @Override
                                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                    View v = null;
                                    v = super.getDropDownView(position, null, parent);
                                    // If this is the selected item position

                                    // for other views
                                    TextView text = (TextView) v.findViewById(R.id.textvw);
                                    text.setTextColor(Color.parseColor("#F86F5C"));
                                    Drawable backgroundColor = getResources().getDrawable(R.drawable.box);
                                    v.setBackground(backgroundColor);
                                    return v;
                                }
                            };

                            servicesAdap.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            servicesAdap.setDropDownViewResource(R.layout.textview);
                            spinner_category.setAdapter(servicesAdap);
                            progressDialog.hide();
                        } catch ( Exception e) {
                            Log.e(TAG, "Fetch_Category excep"+e.toString());

                            e.printStackTrace();
                        }
                        progressDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e(TAG, "Error: " + error.getMessage());

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

    private void FETCH_STATE() {
        Log.d(TAG +"FETCH_STATE url ",Const.URL_FETCH_STATE);

        progressDialog.show();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Const.URL_FETCH_STATE, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG +"FETCH_STATE response", response.toString());

                        try {
                            JSONArray jsonArray = response.getJSONArray("data");
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(j);
                                String state_Id = jsonObject.getString("state_Id");
                                String state_name = jsonObject.getString("state_name");
                                arrayList_StateId.add(state_Id);
                                arrayList_State.add(state_name);
                                stateDb.insertRecord(state_Id, state_name);
                            }

                            stateadp = new ArrayAdapter<String>(VendorSignup.this, R.layout.textview, arrayList_State) {
                                @TargetApi(Build.VERSION_CODES.M)
                                @Override
                                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                                    View v = null;
                                    v = super.getDropDownView(position, null, parent);
                                    // If this is the selected item position
                                    // for other views
                                    TextView text = (TextView) v.findViewById(R.id.textvw);
                                    text.setTextColor(Color.parseColor("#3d406b"));
                                    Drawable backgroundColor = getResources().getDrawable(R.drawable.box);
                                    v.setBackground(backgroundColor);
                                    return v;
                                }
                            };

                            // Drop down layout style - list view with radio button
                            stateadp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            stateadp.setDropDownViewResource(R.layout.textview);
                            spinner_state.setAdapter(stateadp);
                            progressDialog.hide();
                        } catch (Exception e) {
                            Log.d(TAG +"FETCH_STATE excep", e.toString());
                            e.printStackTrace();
                        }

                        if (STATE_IDD.equalsIgnoreCase("0")) {

                        } else {
                            for (int h = 0; h < arrayList_StateId.size(); h++) {
                                if (STATE_IDD.equalsIgnoreCase(arrayList_StateId.get(h))) {
                                    spinner_state.setSelection(h);
                                }
                            }
                        }
                        if (CITY_IDD.equalsIgnoreCase("0")) {

                        } else {
                            bol = false;
                            linear.setVisibility(View.VISIBLE);
                            FETCH_CITY(STATE_IDD);
                        }
                        progressDialog.hide();
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
    public void onBackPressed() {
        Intent intent;
        if (shaved_shared_preferences.get_user_login() == 1)
            intent = new Intent(getApplicationContext(), NavigationScreen.class);
        else
            intent = new Intent(getApplicationContext(), NavigationUser.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.signUp_Customer:
                checkEditVal();
                break;
        }
    }

    private void checkEditVal() {
        Log.d(TAG, "vendor signup");
        /*input_name,input_mobile;*/
        boolean cancel = false;
        View focusView = null;
         str_name = input_name.getText().toString();
       str_phone = input_mobile.getText().toString();

        if (TextUtils.isEmpty(str_name)) {
            input_name.setError(getString(R.string.error_field_required));
            focusView = input_mobile;
            cancel = true;
        }
        // Check for a valid name

        // Check for a valid email address.


        // Check for a valid mobile, if the user entered one.
        if (TextUtils.isEmpty(str_phone)) {
            input_mobile.setError(getString(R.string.error_field_required));
            focusView = input_mobile;
            cancel = true;
            focusView.requestFocus();
        } else if (!isMobile(str_phone)) {
            input_mobile.setError(getString(R.string.error_invalid_number));
            focusView = input_mobile;
            cancel = true;
            focusView.requestFocus();
        } else if (STATE_ID.equalsIgnoreCase("0") || STATE_ID.equalsIgnoreCase("")) {
            cancel = true;
            Toast.makeText(VendorSignup.this, "Choose any city", Toast.LENGTH_SHORT).show();
        } else if (CITY_ID.equalsIgnoreCase("0") || CITY_ID.equalsIgnoreCase("")) {
            cancel = true;
            Toast.makeText(VendorSignup.this, "Choose Area", Toast.LENGTH_SHORT).show();
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.

        } else {
            Log.e("Name", "" + str_name);
            Log.e("Phone", "" + str_phone);
            Log.e("put STATE", "" + STATE);
            Log.e("put STATE_ID", "" + STATE_ID);
            Log.e("put CITY", "" + CITY);
            Log.e("put CITY_ID", "" + CITY_ID);
            Log.e("put service", "" + spinner_category.getSelectedItem());
            vendorSignup();
        }
    }

    private void vendorSignup() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_ADD_VENDOR_SIGNUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG+"vendorSignup", response.toString());
                        try
                        {
                            JSONObject object = new JSONObject(response);
                            String text = object.getString("text");
                            if(text.equals("1"))
                            {
                                Toast.makeText(getApplicationContext(),"Admin will contact you very soon .",Toast.LENGTH_LONG).show();
                                onBackPressed();
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"REGISTER ERROR",Toast.LENGTH_LONG).show();
                            }

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        progressDialog.hide();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(VendorSignup.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", "" + str_name);
                params.put("dob", "" + "");
                params.put("number", "" + str_phone);
                params.put("type_id", "" + spinner_category.getSelectedItem());

                params.put("type_name", "" + spinner_category.getSelectedItem());
                params.put("city_name", "" + STATE);
                params.put("city_id", "" + STATE_ID);
                params.put("sub_city", "" + CITY);

              /*  Log.e("put STATE", "" + STATE);
            Log.e("put STATE_ID", "" + STATE_ID);
            Log.e("put CITY", "" + CITY);
            Log.e("put CITY_ID", "" + CITY_ID);
            Log.e("put service", "" + spinner_category.getSelectedItem());*/
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private boolean isMobile(String address) {
        //TODO: Replace this with your own logic
        if (address.length() >= 10) {
            return true;
        }
        return false;
    }

}
