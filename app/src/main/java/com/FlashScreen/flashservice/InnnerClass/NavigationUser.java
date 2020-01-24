package com.FlashScreen.flashservice.InnnerClass;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.FlashScreen.flashservice.App.AppController;
import com.FlashScreen.flashservice.BuildConfig;
import com.FlashScreen.flashservice.Common.Const;
import com.FlashScreen.flashservice.Common.GPSTracker;
import com.FlashScreen.flashservice.Common.Shaved_shared_preferences;
import com.FlashScreen.flashservice.R;
import com.FlashScreen.flashservice.adapter.MyAdapters;
import com.FlashScreen.flashservice.adapter.Pack;
import com.FlashScreen.flashservice.adapter.Player;
import com.FlashScreen.flashservice.database.CityDb;
import com.FlashScreen.flashservice.database.ServiceDb;
import com.FlashScreen.flashservice.database.StateDb;
import com.FlashScreen.flashservice.startscreen.LoginActivity;
import com.FlashScreen.flashservice.startscreen.RegisterScreen;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class NavigationUser extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BaseSliderView.OnSliderClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    Shaved_shared_preferences shaved_shared_preferences;
    GridView gridview;
    ProgressDialog progressDialog;
    String TAG = "NavigationUser ";
    ArrayList<Player> arrayList;
    ArrayList<HashMap<String, String>> hash_arrayList;
    MyAdapters myAdapter;
    ServiceDb serviceDb;
    StateDb stateDb;
    CityDb cityDb;
    SliderLayout mDemoSlider;
    FloatingActionButton call_now;
    TextView call,tv_ver;
    double lat, lng;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    PendingResult<LocationSettingsResult> result;
    final static int  REQUEST_LOCATION= 199;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static final Integer[] XMEN = {R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d, R.drawable.e};
    private ArrayList<Integer> XMENArray = new ArrayList<Integer>();
    GPSTracker gpsTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_customer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDemoSlider = (SliderLayout) findViewById(R.id.sliders);
        ActivityCompat.requestPermissions(NavigationUser.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        tv_ver= header.findViewById(R.id.tv_ver);
        tv_ver.setText("Version :  " + BuildConfig.VERSION_NAME);
        gridview = (GridView) findViewById(R.id.gridview);
        call = (TextView) findViewById(R.id.call);
        shaved_shared_preferences = new Shaved_shared_preferences(getApplicationContext());


        shaved_shared_preferences.set_user_add("0");

        progressDialog = new ProgressDialog(NavigationUser.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please Wait......");

        stateDb = new StateDb(getApplicationContext());
        cityDb = new CityDb(getApplicationContext());

        arrayList = new ArrayList<>();
        arrayList.clear();

        stateDb.delall();
        cityDb.delall();

        hash_arrayList = new ArrayList<>();
        hash_arrayList.clear();

        shaved_shared_preferences.set_packIdT("");
        shaved_shared_preferences.set_packNameT("");
        shaved_shared_preferences.set_price("");

        serviceDb = new ServiceDb(getApplicationContext());
        stateDb = new StateDb(getApplicationContext());
        cityDb = new CityDb(getApplicationContext());
        call_now = (FloatingActionButton) findViewById(R.id.call_now);




        if (AppController.getInstance().isOnline()) {
            callFetchVersionApi();
            Fetch_Banner();
            Fetch_Category();
        } else {

            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }

        gpsTracker= new GPSTracker(NavigationUser.this);
        gpsTracker.getLocation();
        if(gpsTracker.getLocation()==null)
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    TurnOnGps();

                }
            },3000);
        }

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = "9569622228";
           Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:9569622228"));
                startActivity(intent);
            }
        });

    }

    private void TurnOnGps() {
        Log.e("permissionx ", "------");
        gpsTracker = new GPSTracker(NavigationUser.this);

        if (gpsTracker.getIsGPSTrackingEnabled()) {

            Location loc = gpsTracker.getLocation();

            if (loc != null) {

                lat = loc.getLatitude();
                lng = loc.getLongitude();

                String stringLatitude =""+ Double.toString(loc.getLatitude());

                String stringLongitude =""+ Double.toString(loc.getLongitude());

                Log.e("current_locationQQ", "--\n LAT " + stringLatitude + "\nLONG " + stringLongitude);

                if (stringLatitude.equalsIgnoreCase("0.0") || stringLongitude.equalsIgnoreCase("0.0")||stringLatitude.equalsIgnoreCase("") || stringLongitude.equalsIgnoreCase("") ) {

                    if (checkLocationPermission()) {

                        Toast.makeText(NavigationUser.this, "Turn ON GPS", Toast.LENGTH_LONG).show();
                    } else {
                        ActivityCompat.requestPermissions(NavigationUser.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);

                    }

                } else {

                    lat = Double.parseDouble(stringLatitude);
                    lng = Double.parseDouble(stringLongitude);

                    Geocoder geocoder = new Geocoder(
                            NavigationUser.this, Locale
                            .getDefault());
                    List<Address> addresses;

                }
            } else {


            }
        } else {

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            mGoogleApiClient.connect();

        }
        //  Toast.makeText(NavigationScreen.this,"Turn ON GPS", Toast.LENGTH_LONG).show();

    }

    public boolean checkLocationPermission() {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }


    @Override
    protected void onStop() {
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this, slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            logout_app();
            // super.onBackPressed();
        }
    }

    private void logout_app() {
        Log.e("logout_123", "--" + shaved_shared_preferences.get_user_login());
   new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("You Want to Exit Application !")
                .setConfirmText("Yes!")
                .setCancelText("No")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();

                        serviceDb.delall();
                        stateDb.delall();
                        cityDb.delall();

                        finish();

                        Log.e("logout_1234 user", "--" + shaved_shared_preferences.get_user_login());


                    }
                })
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();

    }


    private void logout() {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText("Want to Logout !")
                .setConfirmText("Yes!")
                .setCancelText("No")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();

                        shaved_shared_preferences.set_user_login(0);
                        shaved_shared_preferences.set_userid(null);
                        finish();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {

                            finishAffinity();
                        }
                    }
                })
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();

    }

    private void callFetchVersionApi() {
        Log.e(TAG + "callFetchVersionApi", ""+Const.URL_VERSION+"?version="+ BuildConfig.VERSION_CODE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_VERSION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG + "callFetchVersionApi", response.toString());
                        try {
                            JSONObject object = new JSONObject(response);
                            String text = object.getString("text");


                            if (text.equals("1")) {
                            } else {

                                SweetAlertDialog sweetDialog  =new SweetAlertDialog(NavigationUser.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Upgrade App !")
                                        .setContentText("A new version available on store !")
                                        .setConfirmText("OK")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                            }
                                        });
                                        /*.setCancelText("No")
                                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismiss();
                                                sweetAlertDialog.cancel();
                                            }
                                        })*/
                                sweetDialog.setCancelable(false);
sweetDialog.show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(NavigationUser.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("version", "" + BuildConfig.VERSION_CODE);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void Fetch_Banner() {
        progressDialog.show();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Const.URL_FETCH_BANNER, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG + "Fetch_Banner", response.toString());
                        HashMap<String, String> url_maps = new HashMap<String, String>();
                        try {

                            JSONArray jsonArray = response.getJSONArray("data");

                            for (int j = 0; j < jsonArray.length(); j++) {

                                JSONObject jsonObject = jsonArray.getJSONObject(j);

                                String Id = jsonObject.getString("Id");
                                String name = jsonObject.getString("name");
                                String image_upload1 = jsonObject.getString("image_upload1");
                                String date = jsonObject.getString("date");

                                String Image = "http://iwwphp.info/flashservices/uploads/banner/" + image_upload1;

                                url_maps.put(name, Image);

                            }
                            for (String name : url_maps.keySet()) {
                                TextSliderView textSliderView = new TextSliderView(getApplicationContext());
                                // initialize a SliderLayout
                                textSliderView
                                        .description(name)
                                        .image(url_maps.get(name))
                                        .setScaleType(BaseSliderView.ScaleType.Fit);
                                //   .setOnSliderClickListener(NavigationUser.this);

                                //add your extra information
                                textSliderView.bundle(new Bundle());
                                textSliderView.getBundle()
                                        .putString("extra", name);

                                mDemoSlider.addSlider(textSliderView);
                            }
                            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
                            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                            mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                            mDemoSlider.setDuration(4000);


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


    private void Fetch_Category() {
         JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Const.URL_FETCH_CAT, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        progressDialog.hide();
                        try {

                            JSONArray jsonArray = response.getJSONArray("data");

                            for (int j = 0; j < jsonArray.length(); j++) {
                                Player player = new Player();

                                JSONObject jsonObject = jsonArray.getJSONObject(j);

                                String category_Id = jsonObject.getString("category_Id");
                                String category_name = jsonObject.getString("category_name");
                                String image_upload1 = jsonObject.getString("image_upload1");
                                String date = jsonObject.getString("date");

                                player.setCategory_Id(category_Id);
                                player.setCategory_name(category_name);
                                player.setImage_upload(image_upload1);
                                player.setDate(date);

                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("category_Id", category_Id);
                                hashMap.put("category_name", category_name);

                                hash_arrayList.add(hashMap);

                                arrayList.add(player);
                            }

                            myAdapter = new MyAdapters(getApplicationContext(), arrayList);
                            gridview.setAdapter(myAdapter);


                            if (serviceDb.CATID().size() > 0) {

                            } else {
                                FetchPACKAGE();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                progressDialog.hide();

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

    private void FetchPACKAGE() {
        progressDialog.show();

        serviceDb.delall();

        final JsonArrayRequest req = new JsonArrayRequest(Const.URL_FETCH_CAT_DETAILS,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            if (response.length() > 0) {
                                for (int i = 0; i < response.length(); i++) {

                                    JSONObject jsonObject = response.getJSONObject(i);

                                    Log.e("jsonObject ", "" + jsonObject);

                                    String category_Idd = jsonObject.getString("category_Id");

                                    Log.e("messagewwjsonObject ", "" + category_Idd);

                                    JSONArray jsonArrays = jsonObject.getJSONArray("categorieslevelone");

                                    Log.e("messagewwjsonObject..b ", "" + jsonArrays);

                                    for (int j = 0; j < jsonArrays.length(); j++) {
                                        Pack pack = new Pack();

                                        JSONObject jsonObject22 = jsonArrays.getJSONObject(j);

                                        String service_Id = jsonObject22.getString("service_Id");
                                        String category_Id = jsonObject22.getString("category_Id");
                                        String service1 = jsonObject22.getString("service1");
                                        String price1 = jsonObject22.getString("price1");

                                        String category_name = "";

                                        for (int ii = 0; ii < hash_arrayList.size(); ii++) {
                                            String catid = hash_arrayList.get(ii).get("category_Id");

                                            if (category_Id.equalsIgnoreCase(catid)) {
                                                category_name = hash_arrayList.get(ii).get("category_name");
                                            }

                                        }

                                        serviceDb.insertRecord(category_Id, category_name, service_Id, service1, price1);


                                    }
                                }
                            }
                            progressDialog.hide();
                        } catch (Exception e) {
                            e.getMessage();
                        }
                        progressDialog.hide();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req,
                "tag_json_arry");

    }

    private void FETCH_STATE() {
        stateDb.delall();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Const.URL_FETCH_STATE, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {

                            JSONArray jsonArray = response.getJSONArray("data");

                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(j);

                                String state_Id = jsonObject.getString("state_Id");
                                String state_name = jsonObject.getString("state_name");

                                stateDb.insertRecord(state_Id, state_name);
                            }
                            FETCH_CITY();

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

    private void FETCH_CITY() {
        cityDb.delall();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Const.URL_FETCH_CITY, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());

                        try {

                            JSONArray jsonArray = response.getJSONArray("data");

                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(j);

                                String state_Id = jsonObject.getString("state_Id");

                                String cityId = jsonObject.getString("cityId");
                                String city = jsonObject.getString("city");

                                cityDb.insertRecord(cityId, city);

                            }


                            // Drop down layout style - list view with radio button

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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            Intent i = new Intent(getApplicationContext(), NavigationUser.class);
            startActivity(i);
            // overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            finish();
        } else if (id == R.id.register) {
            Intent i = new Intent(getApplicationContext(), RegisterScreen.class);
            startActivity(i);
            //  overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            finish();

        } else if (id == R.id.login) {
            Intent i = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(i);
            // overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            finish();

        }
      /*  else if (id == R.id.V_register) {
            Intent i = new Intent(getApplicationContext(), Vendor_Register.class);
            startActivity(i);
            //  overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            finish();

        }*/
        else if (id == R.id.vendor_SignUp) {
            Intent i = new Intent(getApplicationContext(), VendorSignup.class);
            startActivity(i);
            // overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            finish();

        }
       /* else if (id == R.id.Adminlogin) {
            Intent i = new Intent(getApplicationContext(), AdminActivity.class);
            startActivity(i);
          //  overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            finish();

        }*/
        else if (id == R.id.follow_on_social) {
            Intent i = new Intent(getApplicationContext(), FollowUs.class);
            startActivity(i);
            // overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            finish();
        } else if (id == R.id.social_share) {
            socialShare();
        } else if (id == R.id.contactUs) {
            Intent i = new Intent(getApplicationContext(), ContactCustomerSupport.class);
            startActivity(i);
            // overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);

        } else if (id == R.id.vendor_SignUp) {
            Intent i = new Intent(getApplicationContext(), VendorSignup.class);
            startActivity(i);
            // overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            finish();

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void socialShare() {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, "Flash Services");
        share.putExtra(Intent.EXTRA_TEXT,
                "Flash Services \n" +
                        "Download the App and Enjoy the Services ,\n http://bit.ly/flashservices ");

        startActivity(Intent.createChooser(share, "Share to Friends !"));

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(30 * 1000);
        mLocationRequest.setFastestInterval(5 * 1000);

        final LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);

        result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                //final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        //...
                        Log.e("aaaaaaaaaa ", "----DONE----");

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        Log.e("aaaaaaaaaa ", "----PENDING----");


                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    NavigationUser.this,
                                    REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.e("aaaaaaaaaa ", "----CHANGE----");
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        //...
                        break;
                }

            }
        });

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
