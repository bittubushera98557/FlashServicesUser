package com.FlashScreen.flashservice.vendor;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.FlashScreen.flashservice.App.AppController;
import com.FlashScreen.flashservice.Common.Const;
import com.FlashScreen.flashservice.Common.Shaved_shared_preferences;
import com.FlashScreen.flashservice.R;
import com.FlashScreen.flashservice.adapter.Player;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;


public class Vendor_Register extends AppCompatActivity {
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
    private static final String TAG = "AAAAAAAAAAAAAAA";
    private static final int REQUEST_SIGNUP = 0;

    MyCustomAdapter dataAdapter = null;

    @Bind(R.id.linear_selection) ScrollView linear_selection;
    @Bind(R.id.yy) LinearLayout YY;
    @Bind(R.id.relative_selection)
    RelativeLayout relative_selection;
    @Bind(R.id.select_listview) ListView Select_listview;
    @Bind(R.id.input_name) EditText _nameText;
    ArrayList<Country> countryList;

    @Bind(R.id.spinner_cat) Spinner category;
    @Bind(R.id.spinner_stat) Spinner state;

    @Bind(R.id.u_address) TextView U_address;
    //  @Bind(R.id.input_address) EditText _addressText;
    @Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_mobile) EditText _mobileText;
    @Bind(R.id.input_passwords) EditText _passwordText;
    // @Bind(R.id.input_reEnterPasswords) EditText _reEnterPasswordText;
    Shaved_shared_preferences sharedPreferences;
    @Bind(R.id.signUp_Customer)
    TextView signUp_CUSTOMER;
    @Bind(R.id.signIn_Customer) TextView signIn_CUSTOMER;
    @Bind(R.id.previous_selection) TextView Previous_selection;
    @Bind(R.id.submit_selection) TextView Submit_selection;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String name ,U_Address,ADDRESS="",LICENCE="",IDENTITY="",email,mobile,password ,STATE="0",CITY="",CATEGORY="0";
    ProgressDialog progressDialog;

    ArrayList<String> arrayListAId;
    ArrayList<String> arrayListCId;
    ArrayList<String> arrayListId;
    ArrayList<String> arrayListA;
    ArrayList<String> arrayListC;
    ArrayList<String> arrayList;

    ArrayAdapter<String> cat_adp;
    ArrayAdapter<String> adp;

    TextView send_image1,send_image2,send_image3,send_image4,result;
    ImageView upload_image1,upload_image2,upload_image3;

    Button done11,done22,done33,done44;
    Bitmap Image1,Image2,Image3,Image4;
    String ProductId="";
    LinearLayout im1,im2,im3;
    String userChoosenTask = "";
    String image_path1="";
    String image_path2="";
    String image_path3="";
    String TOKEN="";
    byte[] byteArray1=null,byteArray2=null,byteArray3=null,byteArray4=null;
     Boolean bol1=false,bol2=false,bol3=false;

    private static final int STORAGE_PERMISSION_CODE = 123;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor__register);
        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(Vendor_Register.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
/*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        // actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

        }*/

        sharedPreferences = new Shaved_shared_preferences(getApplicationContext());

/*
        im1 = (LinearLayout) findViewById(R.id.im1);
        im2 = (LinearLayout) findViewById(R.id.im2);
        im3 = (LinearLayout) findViewById(R.id.im3);*/

//        im1.setVisibility(View.VISIBLE);
//        im2.setVisibility(View.GONE);
//        im3.setVisibility(View.GONE);

        send_image1 = (TextView) findViewById(R.id.send_image1);
        send_image2 = (TextView) findViewById(R.id.send_image2);
        send_image3 = (TextView) findViewById(R.id.send_image3);

       /* upload_image1 = (ImageView) findViewById(R.id.upload_image1);
        upload_image2 = (ImageView) findViewById(R.id.upload_image2);
        upload_image3 = (ImageView) findViewById(R.id.upload_image3);*/

        countryList = new ArrayList<Country>();
        countryList.clear();

        arrayListAId = new ArrayList<>();
        arrayListCId = new ArrayList<>();
        arrayListId = new ArrayList<>();
        arrayListA= new ArrayList<>();
        arrayListC = new ArrayList<>();
        arrayList = new ArrayList<>();

        arrayListCId.clear();
        arrayListAId.clear();
        arrayListId.clear();
        arrayListA.clear();
        arrayListC.clear();
        arrayList.clear();

        arrayList.add("Select State");
        arrayListId.add("0");

        arrayListC.add("Select Category");
        arrayListCId.add("0");

        linear_selection.setVisibility(View.VISIBLE);
        YY.setVisibility(View.VISIBLE);
        relative_selection.setVisibility(View.GONE);





//        Refcode.setImeOptions(EditorInfo.IME_ACTION_DONE);
//
//        Refcode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//
//            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
//                signUp_CUSTOMER.callOnClick();
//                return false;
//            }
//
//        });

        Previous_selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear_selection.setVisibility(View.VISIBLE);
                YY.setVisibility(View.VISIBLE);
                relative_selection.setVisibility(View.GONE);
            }
        });

        Submit_selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //

                StringBuffer responseText = new StringBuffer();
                responseText.append("");



                if(dataAdapter==null) {
                    Toast.makeText(getApplicationContext(), "Select State & Area", Toast.LENGTH_LONG).show();

                }
                else {
                    countryList = dataAdapter.countryList;

                    if (countryList.size() > 0) {
                        CITY  ="";
                        for (int i = 0; i < countryList.size(); i++) {

                            Country country = countryList.get(i);
                            if (country.isSelected()) {
                                responseText.append("," + country.getCode());
                                CITY = responseText.toString();

                                Log.e("data123", "--" + CITY);
                            }
                        }
                    }
else {
                        Toast.makeText(getApplicationContext(),
                                "Select Atleast One Area in State", Toast.LENGTH_LONG).show();

                    }

                    if (CATEGORY.equalsIgnoreCase("0")) {
                        Toast.makeText(getApplicationContext(), "Select Category", Toast.LENGTH_LONG).show();

                    } else {
                        if (STATE.equalsIgnoreCase("0")) {
                            Toast.makeText(getApplicationContext(), "Select State", Toast.LENGTH_LONG).show();

                        } else {
                            if (CITY.equalsIgnoreCase("")) {
                                Toast.makeText(getApplicationContext(),
                                        "Select Atleast One Area in State", Toast.LENGTH_LONG).show();
                            }
                                else {

                                if (AppController.getInstance().isOnline()) {

                                    //
                                    String s[] = CITY.split(",");

                                    CITY = s[1];

                                    Log.e("data123", "--" + CATEGORY);
                                    Log.e("data123", "--" + CITY);
                                    Log.e("data123", "--" + STATE);
                                    Log.e("data123", "--" + name);
                                    Log.e("data123", "--" + email);
                                    Log.e("data123", "--" + mobile);
                                    Log.e("data123", "--" + password);
                                    Log.e("data123", "--" + U_Address);

//                                    SENDDATA();
                                    SendDummyImages();
                                } else {
                                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                }
            }
        });


        signUp_CUSTOMER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



              signup();
            /*    if (AppController.getInstance().isOnline()) {
                    Fetch_Category();
                    linear_selection.setVisibility(View.GONE);
                    YY.setVisibility(View.GONE);
                    relative_selection.setVisibility(View.VISIBLE);

                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                }
*/


            }
        });

        signIn_CUSTOMER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), Vendor_Login.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });


        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    STATE = "0";
                    Select_listview.setVisibility(View.GONE);
                } else {
                    Select_listview.setVisibility(View.VISIBLE);
                    STATE = arrayListId.get(position);

                    FETCH_CITY(STATE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    CATEGORY = "0";

                } else {
                    CATEGORY = arrayListCId.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Select_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                Country country = (Country) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),
                        "Clicked on Row: " + country.getName(),
                        Toast.LENGTH_LONG).show();
            }
        });



        /*upload_image1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bol1 = true;
                bol2 = false;
                bol3 = false;

                selectImage(1);
            }
        });*/
        send_image1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bol1=true;
                bol2=false;
                bol3=false;

             //   selectImage(1);
            }
        });
      /*  upload_image2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bol2 = true;
                bol1 = false;
                bol3 = false;

                selectImage(1);
            }
        });*/
        send_image2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bol2=true;
                bol1=false;
                bol3=false;

                selectImage(1);
            }
        });
      /*  upload_image3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bol3 = true;
                bol1 = false;
                bol2 = false;

                selectImage(1);
            }
        });*/
        send_image3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bol3=true;
                bol1=false;
                bol2=false;

                selectImage(1);
            }
        });


     /*   done4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (image_path4.length() > 0) {

                    saveProfileAccount4();

                } else {
                    Toast.makeText(getApplicationContext(), " Upload Image", Toast.LENGTH_LONG).show();
                }
            }
        });*/


}
    public void signup() {
        Log.d(TAG, "Signupmmmmmmmmmmmmm");

        boolean cancel = false;
        View focusView = null;
       // REFER_CODE = Refcode.getText().toString();
        name = _nameText.getText().toString();
        U_Address = U_address.getText().toString();
        email = _emailText.getText().toString();
        mobile = _mobileText.getText().toString();
        password = _passwordText.getText().toString();
        // reEnterPassword = _reEnterPasswordText.getText().toString();



        // Check for a valid password, if the user entered one.
        // Check for a valid password, if the user entered one.
     /*   if (TextUtils.isEmpty(reEnterPassword)) {
            _reEnterPasswordText.setError(getString(R.string.error_field_required));
            focusView = _reEnterPasswordText;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            _reEnterPasswordText.setError(getString(R.string.error_invalid_password));
            focusView = _reEnterPasswordText;
            cancel = true;
        }
*/
        // Check for a valid name
        if (TextUtils.isEmpty(U_Address)) {
            U_address.setError(getString(R.string.error_field_required));
            focusView = U_address;
            cancel = true;
        }
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            _passwordText.setError(getString(R.string.error_field_required));
            focusView = _passwordText;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            _passwordText.setError(getString(R.string.error_invalid_password));
            focusView = _passwordText;
            cancel = true;
        }
        // Check for a valid mobile, if the user entered one.
        if (TextUtils.isEmpty(mobile)) {
            _mobileText.setError(getString(R.string.error_field_required));
            focusView = _mobileText;
            cancel = true;
        } else if (!isMobile(mobile)) {
            _mobileText.setError(getString(R.string.error_invalid_number));
            focusView = _mobileText;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            _emailText.setError(getString(R.string.error_field_required));
            focusView = _emailText;
            cancel = true;
        } else if (!isEmailValid(email)) {
            _emailText.setError(getString(R.string.error_invalid_email));
            focusView = _emailText;
            cancel = true;
        }

        if (TextUtils.isEmpty(name)) {
            _nameText.setError(getString(R.string.error_field_required));
            focusView = _nameText;
            cancel = true;
        } else if (!isName(name)) {
            _nameText.setError(getString(R.string.error_invalid_name));
            focusView = _nameText;
            cancel = true;
        }


        Log.e("data123", "..." + cancel);

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            if (image_path1.length() > 0 || image_path2.length() >0 || image_path3.length() >0) {

                if (image_path1.length() > 0 && image_path2.length() >0 ) {

                    if (AppController.getInstance().isOnline()) {
                        Fetch_Category();
                        linear_selection.setVisibility(View.GONE);
                        YY.setVisibility(View.GONE);
                        relative_selection.setVisibility(View.VISIBLE);

                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                    }

                }
                else if (image_path1.length() > 0 && image_path3.length() >0 ) {

                    if (AppController.getInstance().isOnline()) {
                        Fetch_Category();
                        linear_selection.setVisibility(View.GONE);
                        YY.setVisibility(View.GONE);
                        relative_selection.setVisibility(View.VISIBLE);

                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                    }

                }
                else if (image_path2.length() > 0 && image_path3.length() >0 ) {

                    if (AppController.getInstance().isOnline()) {
                        Fetch_Category();
                        linear_selection.setVisibility(View.GONE);
                        YY.setVisibility(View.GONE);
                        relative_selection.setVisibility(View.VISIBLE);



                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), " Upload 2 Doc Image", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(getApplicationContext(), " Upload Image", Toast.LENGTH_LONG).show();
            }





        }
    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        if(email.matches(emailPattern))
        {
            return  true;
        }
        return  false;
    }

    private void Fetch_Category()
    {
        progressDialog.show();

        arrayListC.clear();
        arrayListCId.clear();

        arrayListC.add("Select Category");
        arrayListCId.add("0");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Const.URL_FETCH_CAT, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("DATA_CAT", response.toString());

                        try {

                            JSONArray jsonArray = response.getJSONArray("data");

                            for (int j = 0; j < jsonArray.length(); j++)
                            {
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

                                HashMap<String,String> hashMap = new HashMap<>();
                                hashMap.put("category_Id", category_Id);
                                hashMap.put("category_name", category_name);

                                arrayListC.add(category_name);
                                arrayListCId.add(category_Id);


                            }

                            cat_adp = new ArrayAdapter<String>(Vendor_Register.this, R.layout.textview, arrayListC) {

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
                            cat_adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            cat_adp.setDropDownViewResource(R.layout.textview);
                            category.setAdapter(cat_adp);

                       //     cat_adp = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayListC);
                          //  category.setAdapter(cat_adp);
                            progressDialog.hide();



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.hide();

                        FETCH_STATE();
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

    private void FETCH_STATE()
    {
        arrayListId.clear();
        arrayList.clear();

        arrayList.add("Select State");
        arrayListId.add("0");
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


                                arrayList.add(state_name);
                                arrayListId.add(state_Id);


                            }
                           // adp = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,arrayList);


                            adp = new ArrayAdapter<String>(Vendor_Register.this, R.layout.textview, arrayList) {

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
                            adp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            adp.setDropDownViewResource(R.layout.textview);
                            state.setAdapter(adp);
                            progressDialog.hide();

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
    private void FETCH_CITY(final String CC)
    {
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

                                arrayListA.add(city);
                                arrayListAId.add(cityId);

                                if(CC.equalsIgnoreCase(state_Id)) {

                                    Country country = new Country(""+cityId, ""+city, false);
                                    countryList.add(country);
                                }
                                    //create an ArrayAdaptar from the String Array
                                    dataAdapter = new MyCustomAdapter(Vendor_Register.this,
                                            R.layout.country_info, countryList);

                                    // Assign adapter to ListView
                                    Select_listview.setAdapter(dataAdapter);


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

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        if( password.length() > 0)
        {
            return  true;
        }
        return  false;
    }


    private boolean isAddress(String address) {
        //TODO: Replace this with your own logic
        if( address.length() > 0)
        {
            return  true;
        }
        return  false;
    }

    private boolean isName(String name) {
        //TODO: Replace this with your own logic
        if( name.length() > 0)
        {
            return  true;
        }
        return  false;
    }


    private boolean isMobile(String address) {
        //TODO: Replace this with your own logic
        if( address.length() >=10 )
        {
            return  true;
        }
        return  false;
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }
    private void SendDummyImages() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://iwwphp.info/flashservices/json/shimla_blank_image_upload.php?",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("TAGGGGGG", response.toString());
                        try
                        {
                            JSONObject object = new JSONObject(response);
                            String text = object.getString("text");


                            if(text.equals("1"))
                            {
                                // String type = object.getString("type");
                                //  String CustomerIdd = object.getString("CustomerId");

                                Intent intent = new Intent(getApplicationContext(), Vendor_Login.class);
                                startActivityForResult(intent, REQUEST_SIGNUP);
                                finish();
                                // sharedPreferences.set_userid(CustomerIdd);
                                Toast.makeText(getApplicationContext(),"REGISTER SUCESSFULLY",Toast.LENGTH_LONG).show();
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
                        Toast.makeText(Vendor_Register.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_Id", "" + "36");
                params.put("blank_msg", "" +"evening");
                params.put("Image1", "" + IDENTITY);
                params.put("Image2", "" + ADDRESS);
                params.put("Image3", "" + LICENCE);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void SENDDATA() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_VREG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("TAGGGGGG", response.toString());
                        try
                        {
                            JSONObject object = new JSONObject(response);
                            String text = object.getString("text");


                            if(text.equals("1"))
                            {
                                // String type = object.getString("type");
                                //  String CustomerIdd = object.getString("CustomerId");

                                Intent intent = new Intent(getApplicationContext(), Vendor_Login.class);
                                startActivityForResult(intent, REQUEST_SIGNUP);
                                finish();
                                // sharedPreferences.set_userid(CustomerIdd);
                                Toast.makeText(getApplicationContext(),"REGISTER SUCESSFULLY",Toast.LENGTH_LONG).show();
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
                        Toast.makeText(Vendor_Register.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("categoryid", "" + CATEGORY);
                params.put("cityid", "" + STATE);
                params.put("areaid", "" + CITY);
                params.put("name", "" + name);
                params.put("number", "" + mobile);
                params.put("email", "" + email);
                params.put("password", "" + password);
                params.put("address", "" + U_Address);
                params.put("Image", "" + IDENTITY);
                params.put("Image1", "" + ADDRESS);
                params.put("Image2", "" + LICENCE);

                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void selectImage(final int val) {

        Log.e("valueeeee ", "------" + val);

        final CharSequence[] items = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(Vendor_Register.this);
        builder.setTitle("Upload Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(Vendor_Register.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent(val);
                } else if (items[item].equals("Choose from Gallery")) {
                    userChoosenTask = "Choose from Gallery";
                    if (result)
                        galleryIntent(val);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void cameraIntent(int v)
    {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 200);
    }

    private void galleryIntent(int v)
    {
        Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhotoIntent, 200);

    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


    public String getStringImage(Bitmap bmp)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    public static class Utility {
        public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public static boolean checkPermission(final Context context) {
            int currentAPIVersion = Build.VERSION.SDK_INT;
            if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Permission necessary");
                        alertBuilder.setMessage("External storage permission is necessary");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();
                    } else {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }}


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200 && data != null
                && data.getData() != null) {

            Uri uri = data.getData();
            String[] projection = { MediaStore.Images.Media.DATA };

            Cursor cursor = getApplicationContext().getContentResolver().query(uri, projection, null,
                    null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(projection[0]);

            String picturePath = cursor.getString(columnIndex); // returns null
            if(picturePath!=null) {
                picturePath = picturePath.substring(picturePath.lastIndexOf("/") + 1);

            }
            cursor.close();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                        getApplicationContext().getContentResolver(), uri);
                // image1.setImageBitmap(bitmap);

                String imgg   = getStringImage(bitmap);

                Log.e("IMAGE ", "--" + imgg);



                if(bol1==true) {
                    image_path1 = getStringImage(bitmap);

                    Image1 = bitmap;
                    bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
                    ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream1);
                    byteArray1 = stream1.toByteArray();

                    // Bitmap bit = compressImage(bitmap, picturePath);

                       image_path1 = getStringImage(bitmap);
                    IDENTITY = image_path1;



                 //   upload_image1.setImageBitmap(bitmap);

                    send_image1.setText(picturePath);

                    Log.e("xxxx3331-- ", "-------" + image_path1);

                }
                else if(bol2==true) {
                    image_path2 = getStringImage(bitmap);

                    Image2 = bitmap;

                    bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
                    ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG,90, stream1);
                    byteArray2 = stream1.toByteArray();

                    //  Bitmap bit = compressImage(bitmap, picturePath);


                     image_path2 = getStringImage(bitmap);
                    ADDRESS = image_path2;




                  //  upload_image2.setImageBitmap(bitmap);
                    send_image2.setText(picturePath);
                    Log.e("xxxx3332-- ", "-------" + image_path2);

                }
                else if(bol3==true) {
                    image_path3 = getStringImage(bitmap);

                    Image3 = bitmap;

                    bitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
                    ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream1);
                    byteArray3 = stream1.toByteArray();

                    //  Bitmap bit = compressImage(bitmap, picturePath);


                    image_path3 = getStringImage(bitmap);
                    LICENCE = image_path3;

                  //  upload_image3.setImageBitmap(bitmap);

                    send_image3.setText(picturePath);

                    Log.e("xxxx3333-- ", "-------" + image_path3);

                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        }


    }


    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent i = new Intent(getApplicationContext(),NavigationUser.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/
    public boolean isOnline() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getApplicationContext(). getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

}