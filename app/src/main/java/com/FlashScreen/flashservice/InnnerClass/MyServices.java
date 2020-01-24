package com.FlashScreen.flashservice.InnnerClass;


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
import com.FlashScreen.flashservice.adapter.Pack;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MyServices extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    String TAG="MyServices ",catId,getimage,getname;
    ListView listView;
    private FolderAdapter adapter;
    private ArrayList<Pack> arrayList_pack;
    ProgressDialog progressDialog;
    TextView become_member,title_name,tv_back_fromService;
    String packId,getprice;
    Shaved_shared_preferences shaved_shared_preferences;
    private ArrayList<String> arrayList;
    ImageView imageView;
    String  userId,name,mobile,email,password,member,packages,Seller,Packageid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_services);

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
        progressDialog  = new ProgressDialog(MyServices.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please Wait......");

        arrayList = new ArrayList<>();
        arrayList.clear();

        arrayList_pack=  new ArrayList<>();
        arrayList_pack.clear();

        shaved_shared_preferences =  new Shaved_shared_preferences(getApplicationContext());

        userId = shaved_shared_preferences.get_userid();
        mobile = shaved_shared_preferences.get_phone();
        email = shaved_shared_preferences.get_user_email();

        Log.e(TAG , "Packageid=" + Packageid + "   packages=" + packages);

        shaved_shared_preferences.set_packIdT("1");
        shaved_shared_preferences.set_packNameT("Free");

        become_member = (TextView)findViewById(R.id.become_pro_member);
        tv_back_fromService= (TextView)findViewById(R.id.tv_back_fromService);
        tv_back_fromService.setOnClickListener(this);

        Intent i = getIntent();
        catId = i.getStringExtra("catId");
        getname = i.getStringExtra("name");

        title_name.setText(getname);

        Log.e("imagee ", "---" + getimage);

        //   Picasso.with(getApplicationContext()).load(getimage).into(imageView);

        packId = shaved_shared_preferences.get_packId();

        if (isOnline()) {

            FetchPACKAGE(catId);

        } else

        {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();

        }


        become_member.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                packages = shaved_shared_preferences.get_packNameT();
                Packageid = shaved_shared_preferences.get_packIdT();
                getprice = shaved_shared_preferences.get_price();

                Log.e(".put customerid", "" + userId);
                Log.e(".put PRICE", "" + getprice);
                Log.e(".put CATID", "" + catId);
                Log.e(".put SERVICE_ID", "" + Packageid);

                if(Packageid.equalsIgnoreCase("1"))
                {
                    Nodata();
                }
                else {

                    Log.e("imagee ", "---" + getimage);
                    Intent i  = new Intent(getApplicationContext(), OrderReadyForSubmit.class);
                    i.putExtra("catId", "" + catId);
                    i.putExtra("name", "" + getname);
                    i.putExtra("price", "" +getprice);
                    i.putExtra("serviceId", "" +Packageid);
                    i.putExtra("stateid","0");
                    i.putExtra("cityid","0");
                    i.putExtra("areaname","");
                    i.putExtra("date","0");
                    i.putExtra("time","0");
                    i.putExtra("lattitude","0");
                    i.putExtra("longitude","0");
                    startActivity(i);

                    //  SENDDATA();

                }

            }
        });

    }
    private void Nodata()
    {
        new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Request!!")
                .setContentText("Please Select Atleast One Service")
                .setConfirmText("Ok")
                        // .setCancelText("Cancel")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();

                    }
                })

                .show();

    }


    private void FetchPACKAGE(final String catIdd)
    {
        progressDialog.show();
        final JsonArrayRequest req = new JsonArrayRequest(Const.URL_FETCH_CAT_DETAILS,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        try {
                            if(response.length()>0) {
                                for (int i = 0; i < response.length(); i++) {

                                    JSONObject jsonObject = response.getJSONObject(i);

                                    Log.e("jsonObject ", "" + jsonObject);

                                    String category_Idd = jsonObject.getString("category_Id");
                                    if (catIdd.equalsIgnoreCase(category_Idd)) {
                                    Log.e("messagewwjsonObject ", "" + category_Idd);

                                    JSONArray jsonArrays = jsonObject.getJSONArray("categorieslevelone");

                                    Log.e("messagewwjsonObject..2 ", "" + jsonArrays);


                                        for (int j = 0; j < jsonArrays.length(); j++) {
                                            Pack pack = new Pack();

                                            JSONObject jsonObject22 = jsonArrays.getJSONObject(j);

                                            String service_Id = jsonObject22.getString("service_Id");
                                            String category_Id = jsonObject22.getString("category_Id");
                                            String service1 = jsonObject22.getString("service1");
                                            String price1 = jsonObject22.getString("price1");

                                            pack.setService_Id(service_Id);
                                            pack.setService1(service1);
                                            pack.setCategory_Id(category_Id);
                                            pack.setPrice1(price1);

                                            arrayList_pack.add(pack);

                                            Log.e("ggggggg ","0-------"+service1+"--"+service_Id+"--"+price1);
                                        }
                                    }
                                }
                                adapter = new FolderAdapter(getApplicationContext(),R.layout.grid_layout, arrayList_pack);
                                listView.setAdapter(adapter);

                                progressDialog.hide();
                            }

                        }
                        catch (Exception e)
                        {
                            e.getMessage();
                        }
                        progressDialog.hide();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req,
                "tag_json_arry");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }



    public boolean isOnline() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getApplicationContext(). getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
              /*  Intent intent = new Intent(MyServices.this,NavigationUser.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);*/
              onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MyServices.this,NavigationUser.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_back_fromService:
                onBackPressed();
                break;
        }
    }
}
