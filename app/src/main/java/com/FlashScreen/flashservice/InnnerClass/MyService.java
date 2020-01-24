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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.FlashScreen.flashservice.App.AppController;
import com.FlashScreen.flashservice.Common.Const;
import com.FlashScreen.flashservice.Common.Shaved_shared_preferences;
import com.FlashScreen.flashservice.R;
import com.FlashScreen.flashservice.adapter.GridListAdapter;
import com.FlashScreen.flashservice.adapter.Pack;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MyService extends AppCompatActivity {
    private Context context;
    String TAG="MyService ",catId,getimage,getname;
    ListView listView;
    Context mContext;
    RadioButton selected=null;
    private FolderAdapter adapter;
    private ArrayAdapter<String> adp;
    private ArrayList<Pack> arrayList_pack;
    ProgressDialog progressDialog;
    TextView become_member,title_name,tv_back_fromService;
    String packId,getprice;
    Shaved_shared_preferences shaved_shared_preferences;
    private ArrayList<String> arrayList;
    ImageView imageView;
    String  userId,name,mobile,email,password,member,packages,Seller,Packageid;
    String DATE,TIME,CITY_ID,CITY,AREA,STATE_IDD,CITY_IDD;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_service);

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

        listView = (ListView) findViewById(R.id.list_vieww);
        imageView = (ImageView) findViewById(R.id.imageView);
        title_name = (TextView) findViewById(R.id.title_name);
        tv_back_fromService= (TextView) findViewById(R.id.tv_back_fromService);
        progressDialog  = new ProgressDialog(MyService.this,
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

        Log.e("oooooooooo ", "--" + Packageid + "--" + packages);

        shaved_shared_preferences.set_packIdT("1");
        shaved_shared_preferences.set_packNameT("Free");



        become_member = (TextView)findViewById(R.id.become_pro_member);

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


        become_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                packages = shaved_shared_preferences.get_packNameT();
                Packageid = shaved_shared_preferences.get_packIdT();

                getprice = shaved_shared_preferences.get_price();

                Log.e(".put customerid", "" + userId);
                Log.e(".put PRICE", "" + getprice);
                Log.e(".put CATID", "" + catId);
                Log.e(".put SERVICE_ID", "" + Packageid);

                if (Packageid.equalsIgnoreCase("1")) {
                    Nodata();
                } else {

                    Log.e("imagee ", "---" + getimage);
                    Intent i = new Intent(getApplicationContext(), ServiceDetails.class);
                    i.putExtra("catId", "" + catId);
                    i.putExtra("name", "" + getname);
                    i.putExtra("price", "" + getprice);
                    i.putExtra("serviceId", "" + Packageid);
                    i.putExtra("stateid", "0");
                    i.putExtra("cityid", "0");
                    i.putExtra("areaname", "");
                    i.putExtra("date", "0");
                    i.putExtra("time", "0");
                    startActivity(i);

                    //  SENDDATA();

                }

            }
        });

        tv_back_fromService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("position 2", "--" + position);
            }
        });



        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
              //  RecyclerView.ViewHolder mHolder;
                // if (convertView == null) {
              //  view = LayoutInflater.from(mContext).inflate(R.layout.grid_v, null);
              //  RadioButton text = (RadioButton) view.findViewById(R.id.gridvw);

              Log.e("position 3","--"+position);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

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

                                   Log.e("jsonObject123 ", "" + jsonObject);

                                   String category_Idd = jsonObject.getString("category_Id");

                                   Log.e("jsonObject123x", "" + category_Idd+"--"+catIdd);

                                   if (catIdd.equalsIgnoreCase(category_Idd)) {
                                 //  Log.e("messagewwjsonObject ", "" + category_Idd);

                                   JSONArray jsonArrays = jsonObject.getJSONArray("categorieslevelone");


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

                                           arrayList.add(service1);
                                       }
                                     adapter = new FolderAdapter(getApplicationContext(),R.layout.grid_layout, arrayList_pack);
                                       listView.setAdapter(adapter);

                                   /*    adp = new ArrayAdapter<String>(MyService.this, R.layout.grid_v, arrayList) {

                                           @Override
                                           public View getView(int position, View convertView, ViewGroup parent) {

                                               View v = null;
                                               v = super.getView(position, convertView, parent);
                                               // If this is the selected item position

                                               // for other views
                                               RadioButton text = (RadioButton) v.findViewById(R.id.gridvw);
                                               text.setTextColor(Color.parseColor("#3d406b"));
                                               Drawable backgroundColor = getResources().getDrawable(R.drawable.box);
                                               v.setBackground(backgroundColor);

                                               return v;
                                           }

                                       };

                   *//*                    view = inflater.inflate(R.layout.grid_layout, null, false);

                                       viewHolder.radioButton = (RadioButton) view.findViewById(R.id.radio_button);
*//*
                                       // Drop down layout style - list view with radio button
                                      // adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                       adp.setDropDownViewResource(R.layout.grid_v);
                                       listView.setAdapter(adp);*/
                                   }
                               }
                           }


                           progressDialog.hide();
                       }
                       catch (Exception e)
                       {
                           e.getMessage();
                       }

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
                Intent intent = new Intent(MyService.this,NavigationScreen.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(getApplicationContext(),NavigationScreen.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        finish();
        // super.onBackPressed();
    }
}
