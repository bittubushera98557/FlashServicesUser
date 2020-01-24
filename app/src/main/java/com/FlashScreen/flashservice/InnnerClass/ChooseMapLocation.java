package com.FlashScreen.flashservice.InnnerClass;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.FlashScreen.flashservice.Common.Const;
import com.FlashScreen.flashservice.Common.GPSTracker;
import com.FlashScreen.flashservice.Common.Shaved_shared_preferences;
import com.FlashScreen.flashservice.R;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class ChooseMapLocation extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {
    TextView btn_continue;
    String str_STATE_ID, str_catId, str_getname, str_getimage, str_getprice, str_getserviceId,
            str_CITY_ID = "0", str_AREA, str_DATE, str_TIME, str_userId, str_CityName, str_StateName, dummyArea;
    ProgressDialog progressDialog;
    Shaved_shared_preferences shaved_shared_preferences;
    LinearLayout iv_mapRefresh;
    FrameLayout fl_map;
    private GoogleMap googleMap_address;
    private double lattitude, longitude;
    SupportMapFragment mapFragment;
    TextView tv_mapAddress, tv_fetchedAddress;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    GPSTracker gpsTracker;
    private int num = 0;

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    private String TAG = "ChooseMapLocation ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_map_location);
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
        btn_continue = (TextView) findViewById(R.id.btn_continue);
        btn_continue.setOnClickListener(this);
        progressDialog = new ProgressDialog(ChooseMapLocation.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please Wait ...");
        progressDialog.setCancelable(false);
        shaved_shared_preferences = new Shaved_shared_preferences(getApplicationContext());
        initView();
        iv_mapRefresh.setOnClickListener(this);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.frag_map);

        mapFragment.getMapAsync(this);
        getIntentData();

    }

    private void initView() {
        tv_mapAddress = (TextView) findViewById(R.id.tv_mapAddress);
        fl_map = (FrameLayout) findViewById(R.id.fl_map);
        iv_mapRefresh = (LinearLayout) findViewById(R.id.iv_mapRefresh);
        gpsTracker = new GPSTracker(ChooseMapLocation.this);
        tv_fetchedAddress = (TextView) findViewById(R.id.tv_fetchedAddress);

    }

    private void getIntentData() {
        Intent i = getIntent();
        str_catId = i.getStringExtra("catId");
        str_getname = i.getStringExtra("name");
        str_getimage = i.getStringExtra("image");
        str_getprice = i.getStringExtra("price");
        str_getserviceId = i.getStringExtra("serviceId");
        str_STATE_ID = i.getStringExtra("stateid");
        str_CITY_ID = i.getStringExtra("cityid");
        str_AREA = i.getStringExtra("areaname");
        str_CityName = i.getStringExtra("cityName");
        str_StateName = i.getStringExtra("stateName");
        dummyArea = str_AREA + " " + str_CityName + " " + str_StateName;
        str_DATE = i.getStringExtra("date");
        str_TIME = i.getStringExtra("time");
        if (i.getStringExtra("lattitude") == null || i.getStringExtra("lattitude").equals(""))
            lattitude = 0;
        else
            lattitude = Double.parseDouble(i.getStringExtra("lattitude"));

        if (i.getStringExtra("longitude") == null || i.getStringExtra("longitude").equals(""))
            longitude = 0;
        else
            longitude = Double.parseDouble(i.getStringExtra("longitude"));

        str_userId = shaved_shared_preferences.get_userid();

        Log.e(TAG, "str_catId-- " + str_catId);
        Log.e(TAG + "", "str_getname-- " + str_getname);
        Log.e(TAG + "", "str_getimage-- " + str_getimage);
        Log.e(TAG + "", "str_getprice-- " + str_getprice);
        Log.e(TAG + "", "str_getserviceId-- " + str_getserviceId);

        Log.e(TAG + "", "str_STATE_ID-- " + str_STATE_ID);
        Log.e(TAG + "", "str_CITY_ID-- " + str_CITY_ID);
        Log.e(TAG + "", "str_AREA-- " + str_AREA);
        Log.e(TAG + "", "str_DATE-- " + str_DATE);
        Log.e(TAG + "", "str_TIME-- " + str_TIME);


        tv_mapAddress.setText(dummyArea);
        //    initilizeMap();//.
         /*   int cityPosition = sp_cityName.getSelectedItemPosition();
            final String city = arrayListCity.get(cityPosition).toString();
*/
        num = 0;
        //   String urlLocAddress=  "https://maps.google.com/maps/api/geocode/json?address="+str_street1+","+str_street2+","+dummyArea+","+str_landmark+","+city+","+str_pin;//+"&sensor=false";
        String urlLocAddress = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=" + dummyArea + "&inputtype=textquery&fields=photos,formatted_address,name,geometry&key=AIzaSyC8jFU-yrO7rzRh4gWaMlB3L82U1gDc_3A";

        callGoogleLocationLatLng(urlLocAddress);
    }

    private void callGoogleLocationLatLng(String urlLocAddress) {
        progressDialog.show();
        num++;
        urlLocAddress = urlLocAddress.replaceAll(",,", ",");
        urlLocAddress = urlLocAddress.replaceAll("#", "");
        urlLocAddress = urlLocAddress.replaceAll("  ", " ");
        urlLocAddress = urlLocAddress.replaceAll(" ", "%20");

        Log.e("<>API urlLocAddress", " " + urlLocAddress);
        //   int ciPo  = sp_cityName.getSelectedItemPosition();
        //  final String city = arrayListCity.get(ciPo).toString();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlLocAddress,
                new Response.Listener<String>() {
                    //response handling
                    @Override
                    public void onResponse(String response) {
                        progressDialog.cancel();
                        Log.e(TAG + " callGoogleLocLatLng", " " + response);
                        String Status = null;

                        try {
                            JSONObject objMain = new JSONObject(response);
                            Status = objMain.getString("status");
                            if (Status.equalsIgnoreCase("OK")) {
                                JSONArray resultsArray = objMain.getJSONArray("candidates");
                                JSONObject arrObj = resultsArray.getJSONObject(0);
                                final String formatted_address = arrObj.getString("formatted_address");
                                JSONObject geometryObj = arrObj.getJSONObject("geometry");

                                JSONObject locationObj = geometryObj.getJSONObject("location");
                                lattitude = Double.parseDouble(locationObj.getString("lat"));
                                longitude = Double.parseDouble(locationObj.getString("lng"));

                                CameraPosition cameraPosition = new CameraPosition.Builder().target(
                                        new LatLng(lattitude, longitude)).zoom(18.0F)
                                        .bearing(300F) // orientation
                                        .tilt(30F) // viewing angle
                                        .build();

                                googleMap_address.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.e(TAG, "cancelling dialog");
                                        tv_fetchedAddress.setText(formatted_address);
                                    }
                                }, 2000);
                            } else {
                                if (num < 3) {
//                             String       urlLocAdd = "https://maps.google.com/maps/api/geocode/json?address=" + city + "&sensor=false";
                                    String urlLocAdd = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=" + dummyArea + "&inputtype=textquery&fields=formatted_address,name,geometry&key=AIzaSyC8jFU-yrO7rzRh4gWaMlB3L82U1gDc_3A";

                                    callGoogleLocationLatLng(urlLocAdd);
                                }
                            }
                            Log.e(TAG + "<>api status", "" + Status);


                        } catch (Exception e) {
                            //         alertMessage.cancelDialog();
                            Log.e(TAG + "excep callCategoryApi", "" + e.toString());
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(com.android.volley.VolleyError error) {
                        progressDialog.cancel();
                        if (error instanceof NoConnectionError) {
                            Toast.makeText(getApplicationContext(), "No internet Access, Check your internet connection.", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                        }
                    }
                }) {

            //send parameters
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


    }

    private void callSubmitOrder() {

        try {
            if (str_TIME.contains("am") || str_TIME.contains("AM")) {
                str_TIME = str_TIME.replaceAll("am", "").trim();
                str_TIME = str_TIME.replaceAll("AM", "").trim();
                int numberTIME = Integer.parseInt(str_TIME);
                str_TIME = numberTIME + ":00:00";
            } else if (str_TIME.contains("pm") || str_TIME.contains("PM"))
                str_TIME = str_TIME.replaceAll("pm", "").trim();
            str_TIME = str_TIME.replaceAll("PM", "").trim();
            int numberTIME = Integer.parseInt(str_TIME) + 12;
            str_TIME = numberTIME + ":00:00";
        } catch (Exception e) {

        }


        progressDialog.show();
        Log.e(TAG + "callBookService  ", "--" + Const.URL_SEND_SERVICE + "?categoryid=" + str_catId + "&customerid=" + str_userId +
                "&serviceid=" + str_getserviceId + "&serviceprice=" + str_getprice + "&stateid=" + str_STATE_ID + "&cityid=" + str_CITY_ID +
                "&areaname=" + str_AREA + "&date=" + str_DATE + "&time=" + str_TIME + "&lat=" + lattitude + "&lng=" + longitude+"&orderFrom=androidApp"
        );

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_SEND_SERVICE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG + "callBookService  ", "--" + response.toString());

                        try {
                            JSONObject ob = new JSONObject(response);
                            String text = ob.getString("text");
                            String Locationid = ob.getString("Locationid");

                            if (text.equals("1")) {

                                Intent intent = new Intent(getApplicationContext(), ViewOrderDetail.class);
                                intent.putExtra("OrderId", "" + Locationid);
                                startActivity(intent);
                                finish();


                                Toast.makeText(getApplicationContext(), "SERVICE ADDED SUCESSFULLY", Toast.LENGTH_LONG).show();
                                progressDialog.hide();


                            } else {

                                Toast.makeText(getApplicationContext(), "SERVICE ERROR", Toast.LENGTH_LONG).show();
                                progressDialog.hide();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.hide();
                        }

                        progressDialog.hide();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChooseMapLocation.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("categoryid", "" + str_catId);
                params.put("customerid", "" + str_userId);
                params.put("serviceid", "" + str_getserviceId);
                params.put("serviceprice", "" + str_getprice);
                params.put("stateid", "" + str_STATE_ID);
                params.put("cityid", "" + str_CITY_ID);
                params.put("areaname", "" + str_AREA);
                params.put("date", "" + str_DATE);
                params.put("time", "" + str_TIME);
                params.put("lat", "" + lattitude);
                params.put("lng", "" + longitude);
                params.put("orderFrom", "androidApp"  );

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_continue:
                callSubmitOrder();
                break;
            case R.id.iv_mapRefresh:

                num = 0;
                String urlLocAddress = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?input=" + dummyArea + "&inputtype=textquery&fields=photos,formatted_address,name,geometry&key=AIzaSyC8jFU-yrO7rzRh4gWaMlB3L82U1gDc_3A";
                callGoogleLocationLatLng(urlLocAddress);
                break;
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap_address = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        googleMap_address.setMyLocationEnabled(false);

        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(lattitude, longitude)).zoom(18.0F)
                .bearing(300F) // orientation
                .tilt(30F) // viewing angle
                .build();

        googleMap_address.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        Log.e("cameraposition", "lat=  " + lattitude + "   lng=  " + longitude);

        // check if map is created successfully or not
        if (googleMap_address == null) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                    .show();
        }
        googleMap_address.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                lattitude = cameraPosition.target.latitude;
                longitude = cameraPosition.target.longitude;
                Geocoder geocoder = new Geocoder(
                        ChooseMapLocation.this, Locale
                        .getDefault());
                List<Address> addresses;
                try {

                    addresses = geocoder.getFromLocation(lattitude, longitude, 1);
                    Log.e(TAG + " onMapReady", "changedMap_location " + addresses);
                    if (addresses.size() != 0) {
                        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        Log.e("changedMap_location ", address);

                        tv_fetchedAddress.setText("" + address);
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                Log.e("onCameraChange", "lat=  " + lattitude + "   lng=  " + longitude);

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
/*  Intent i = new Intent(getApplicationContext(), OrderReadyForSubmit.class);
                i.putExtra("catId",""+str_catId);
                i.putExtra("name",""+str_getname);
                i.putExtra("image",""+str_getimage);
                i.putExtra("price",""+str_getprice);
                i.putExtra("serviceId", "" + str_getserviceId);
                i.putExtra("stateid",str_STATE_ID);
                i.putExtra("cityid",str_CITY_ID);
                i.putExtra("areaname",str_AREA);
                i.putExtra("date",str_DATE);
                i.putExtra("time",str_TIME);
   startActivity(i);
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
                finish();*/
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        /*try{
            String area[] =str_AREA.split(",");
            str_AREA=area[0];
        }
        catch (Exception e)
        {

        }*/
        Intent i = new Intent(getApplicationContext(), OrderReadyForSubmit.class);
        i.putExtra("catId", "" + str_catId);
        i.putExtra("name", "" + str_getname);
        i.putExtra("image", "" + str_getimage);
        i.putExtra("price", "" + str_getprice);
        i.putExtra("serviceId", "" + str_getserviceId);
        i.putExtra("stateid", str_STATE_ID);
        i.putExtra("cityid", str_CITY_ID);
        i.putExtra("cityName", str_CityName);
        i.putExtra("stateName", str_StateName);
        i.putExtra("areaname", str_AREA);
        i.putExtra("date", str_DATE);
        i.putExtra("time", str_TIME);
        i.putExtra("lattitude", lattitude);
        i.putExtra("longitude", longitude);
        startActivity(i);
        overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
        finish();

    }
}
