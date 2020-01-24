package com.FlashScreen.flashservice.InnnerClass;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.FlashScreen.flashservice.App.AppController;
import com.FlashScreen.flashservice.Common.Const;
import com.FlashScreen.flashservice.Common.Shaved_shared_preferences;
import com.FlashScreen.flashservice.R;
import com.FlashScreen.flashservice.adapter.All;
import com.FlashScreen.flashservice.adapter.AllAdapter;
import com.FlashScreen.flashservice.database.ServiceDb;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllServices extends AppCompatActivity {

    ProgressDialog progressDialog;
    Shaved_shared_preferences shaved_shared_preferences;
    ExpandableListView exList ;
    AllAdapter myAdapter;
    ArrayList<HashMap<String,String>> hashMapArrayList;
    ArrayList<HashMap<String,String>> hashMappProvider;
    ListView listview;
    int total = 0 ;
    int length = 0 ;
    Boolean first= true;
    LinearLayoutManager mLayoutManager =null;// new LinearLayoutManager(getApplicationContext());

    ArrayList<All> arrayListt;
    ArrayList<String> contents,contentfound;
    String userId,TokenId,Token;
    String TAG="TAG_TAG";
    ServiceDb serviceDb;
    ArrayList<HashMap<String,String>> arrayList;
    ArrayList<HashMap<String,String>> arrayLists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_services);

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

        shaved_shared_preferences = new Shaved_shared_preferences(getApplicationContext());
        progressDialog = new ProgressDialog(AllServices.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading Data...");


        hashMapArrayList = new ArrayList<>();
        hashMappProvider = new ArrayList<>();
        arrayList = new ArrayList<>();
        arrayListt = new ArrayList<>();

        hashMapArrayList.clear();
        hashMappProvider.clear();
        arrayList.clear();
        arrayListt.clear();

        shaved_shared_preferences = new Shaved_shared_preferences(getApplicationContext());

        listview = (ListView)findViewById(R.id.mylist_service);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        userId = shaved_shared_preferences.get_userid();
        TokenId = shaved_shared_preferences.get_TokenId();
        Token = shaved_shared_preferences.get_Token();

        Log.e("ffffffff ", "-" + userId + "--" + TokenId + "--" + Token);

        serviceDb = new ServiceDb(getApplicationContext());


        if (AppController.getInstance().isOnline())
        {
            first= true;
            Fetch_Providers();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();

        }

        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int currentVisibleItemCount;
            private int currentScrollState;
            private int currentFirstVisibleItem;
            private int totalItem;
            private LinearLayout lBelow;


            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                this.currentScrollState = scrollState;
                this.isScrollCompleted();

                Log.e("SCROLL_STATE 1","--"+view+"---"+scrollState);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub
                this.currentFirstVisibleItem = firstVisibleItem;
                this.currentVisibleItemCount = visibleItemCount;
                this.totalItem = totalItemCount;

                Log.e("SCROLL_STATE 2","--"+view+"---"+firstVisibleItem+"--"+visibleItemCount+"--"+totalItemCount);

            }

            private void isScrollCompleted() {
                if (totalItem - currentFirstVisibleItem == currentVisibleItemCount
                        && this.currentScrollState == SCROLL_STATE_IDLE) {
                    /** To do code here*/
                    Log.e("SCROLL_STATE 3", "--" + totalItem + "---" + currentFirstVisibleItem + "--" + currentVisibleItemCount + "--" + currentScrollState);

                    progressDialog.setMessage("Loading More Data....");

                    if(total==length)
                    {

                    }
                    else {
                        first= false;
                        Fetch();
                    }

                }
            }
        });
    }

    private void Fetch_Providers() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_FETCH_PROVIDERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("TAGGGGGG", response.toString());

                        try {
                            JSONObject ob = new JSONObject(response);
                            String type = ob.getString("type");

                            JSONArray jsonArray = ob.getJSONArray("data");


                            for (int j = 0; j < jsonArray.length(); j++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(j);

                                String Id = jsonObject.getString("Id");
                                String category_Id = jsonObject.getString("category_Id");
                                String city_Id = jsonObject.getString("city_Id");
                                String area_Id = jsonObject.getString("area_Id");
                                String name = jsonObject.getString("name");
                                String email = jsonObject.getString("email");
                                String number = jsonObject.getString("number");

                                HashMap<String,String> hashMap = new HashMap<>();

                                hashMap.put("Id",""+Id);
                                hashMap.put("category_Id",""+category_Id);
                                hashMap.put("city_Id",""+city_Id);
                                hashMap.put("area_Id",""+area_Id);
                                hashMap.put("name",""+name);
                                hashMap.put("email",""+email);
                                hashMap.put("number",""+number);

                                hashMappProvider.add(hashMap);
                            }
                            progressDialog.hide();

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        progressDialog.hide();

                        Fetch_Assign();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("adminId", userId);
                params.put("Token", Token);
                params.put("Sessionvalue", "1");
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void Fetch_Assign() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_FETCH_ASSIGN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("TAGGGGGG", response.toString());

                        try {
                            JSONObject ob = new JSONObject(response);
                            String type = ob.getString("type");

                            JSONArray jsonArray = ob.getJSONArray("data");


                            for (int j = 0; j < jsonArray.length(); j++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(j);

                                String Id = jsonObject.getString("Id");
                                String provider_Id = jsonObject.getString("provider_Id");
                                String service_Id = jsonObject.getString("service_Id");

                                String pr_name="";
                                String pr_num="";
                                String pr_email="";
                                String pr_cat="";
                                String pr_city="";
                                String pr_area="";

                                for(int x =0;x<hashMappProvider.size();x++)
                                {
                                    if(hashMappProvider.get(x).get("Id").equals(provider_Id))
                                    {
                                        pr_name=hashMappProvider.get(x).get("name");
                                        pr_num=hashMappProvider.get(x).get("number");
                                        pr_email=hashMappProvider.get(x).get("email");
                                        pr_cat=hashMappProvider.get(x).get("category_Id");
                                        pr_city=hashMappProvider.get(x).get("city_Id");
                                        pr_area=hashMappProvider.get(x).get("area_Id");

                                    }
                                }

                                HashMap<String,String> hashMap = new HashMap<>();

                                hashMap.put("Id",""+Id);
                                hashMap.put("provider_Id",""+provider_Id);
                                hashMap.put("pr_name",""+pr_name);
                                hashMap.put("pr_num",""+pr_num);
                                hashMap.put("pr_email",""+pr_email);
                                hashMap.put("pr_cat",""+pr_cat);
                                hashMap.put("pr_city",""+pr_city);
                                hashMap.put("pr_area",""+pr_area);
                                hashMap.put("service_Id",""+service_Id);

                                Log.e("serviceee ","--"+service_Id);

                                hashMapArrayList.add(hashMap);
                            }
                            progressDialog.hide();

                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                        progressDialog.hide();

                        Fetch();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("adminId", userId);
                params.put("Token", Token);
                params.put("Sessionvalue", "1");
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    private void Fetch() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.Fetch_all,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("TAGGGGGG", response.toString());

                        try {
                            JSONObject ob = new JSONObject(response);
                            String type = ob.getString("type");

                            JSONArray jsonArray = ob.getJSONArray("data");

                            total = jsonArray.length();

                            Log.e("TOTAL_LENGTH ","--"+total);

                            int leng = length;

                            if(total>length)
                            {
                                if(total-length>10)
                                {
                                    length = length + 10;
                                }
                                else {
                                    int ss = total-length;

                                    length = length+ss;
                                }
                            }

                            Log.e("TOTAL_STATUS ","--"+leng+"---"+length);





                            for (int j = leng; j < length; j++) {

                                String pr_Id="";
                                String provider_Id="";
                                String pr_name="";
                                String pr_num="";
                                String pr_email="";
                                String pr_cat="";
                                String pr_city="";
                                String pr_area="";

                                JSONObject jsonObject = jsonArray.getJSONObject(j);

                                All all =new All();

                                String Id = jsonObject.getString("Id");
                                String category_Id = jsonObject.getString("category_Id");
                                String customer_Id = jsonObject.getString("customer_Id");
                                String service_Id = jsonObject.getString("service_Id");
                                String service_price = jsonObject.getString("service_price");
                                String area_name = jsonObject.getString("area_name");
                                String state_Id = jsonObject.getString("state_Id");
                                String city_Id = jsonObject.getString("city_Id");
                                String date = jsonObject.getString("date");
                                String time = jsonObject.getString("time");
                                String task = jsonObject.getString("task");

                                String category = serviceDb.getCat_Name(category_Id);
                                String service = serviceDb.getService_Name(service_Id);

                                Log.e("serviceee1 ","--"+service_Id);

                                for(int x =0;x<hashMapArrayList.size();x++)
                                {

                                    if(hashMapArrayList.get(x).get("service_Id").equals(Id))
                                    {
                                        Log.e("serviceee_match ","--"+Id+"--"+hashMapArrayList.get(x).get("service_Id"));

                                        pr_Id=hashMapArrayList.get(x).get("Id");
                                        provider_Id=hashMapArrayList.get(x).get("provider_Id");
                                        pr_name=hashMapArrayList.get(x).get("pr_name");
                                        pr_num=hashMapArrayList.get(x).get("pr_num");
                                        pr_email=hashMapArrayList.get(x).get("pr_email");
                                        pr_cat=hashMapArrayList.get(x).get("pr_cat");
                                        pr_city=hashMapArrayList.get(x).get("pr_city");
                                        pr_area=hashMapArrayList.get(x).get("pr_area");

                                    }
                                }


                                ///////////////////////

                                all.setId(Id);
                                all.setPId(pr_Id);
                                all.setProvider_Id(provider_Id);
                                all.setPr_name(pr_name);
                                all.setPr_num(pr_num);
                                all.setPr_email(pr_email);
                                all.setPr_cat(pr_cat);
                                all.setPr_city(pr_city);
                                all.setPr_area(pr_area);
                                all.setCategory_Id(category_Id);
                                all.setCategory(category);
                                all.setCustomer_Id(customer_Id);
                                all.setService_Id(service_Id);
                                all.setService(service);
                                all.setService_price(service_price);
                                all.setArea_name(area_name);
                                all.setState_Id(state_Id);
                                all.setCity_Id(city_Id);
                                all.setDate(date);
                                all.setTime(time);
                                all.setTask(task);



                                arrayListt.add(all);

                            }
                            myAdapter = new AllAdapter(getApplicationContext(),arrayListt);
                            listview.setAdapter(myAdapter);
                            progressDialog.hide();

                            if(first==true )
                            {
                                first= false;
                            }
                            else {
                                listview.setSelection(myAdapter.getCount() - 1);

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
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("adminId", userId);
                params.put("Token", Token);
                params.put("Sessionvalue", "1");
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
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
                Intent i = new Intent(getApplicationContext(),NavigationAdmin.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public boolean isOnline() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)getApplicationContext(). getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onBackPressed()
    {
        Intent i = new Intent(getApplicationContext(),NavigationAdmin.class);
        startActivity(i);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        finish();
       // super.onBackPressed();
    }
}
