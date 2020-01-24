package com.FlashScreen.flashservice.InnnerClass;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ViewOrderDetail extends AppCompatActivity {
    private Context context;
    String TAG = "ViewOrderDetail ", catId, getimage, getname;
    ListView listView;
    private GridListAdapter adapter;
    private ArrayList<Pack> arrayList_pack;
    ProgressDialog progressDialog;
    TextView service_history, title_name, tv_cancelledBy, tv_orderStatus;
    TextView tv_cancelOrder, job_id, name_id, area_id, address_id, city_id, email_id, phone_id, service_id, problem_id, cost_id,tv_slotTm;
    String user, packId, OrderId;
    Shaved_shared_preferences shaved_shared_preferences;
    private ArrayList<String> arrayList;
    ImageView imageView;
    String userId, name, mobile, email, member, packages, Seller, Packageid;
    CityDb cityDb;
    StateDb stateDb;
    ServiceDb serviceDb;
    LinearLayout ll_CancelledBy,ll_slot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_sc);

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
        tv_slotTm= (TextView) findViewById(R.id.tv_slotTm);
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
        tv_cancelOrder = (TextView) findViewById(R.id.tv_cancelOrder);
        tv_cancelledBy = (TextView) findViewById(R.id.tv_cancelledBy);
        tv_orderStatus = (TextView) findViewById(R.id.tv_orderStatus);
        ll_CancelledBy = (LinearLayout) findViewById(R.id.ll_CancelledBy);
        ll_slot= (LinearLayout) findViewById(R.id.ll_slot);

        progressDialog = new ProgressDialog(ViewOrderDetail.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please Wait......");

        arrayList = new ArrayList<>();
        arrayList.clear();

        arrayList_pack = new ArrayList<>();
        arrayList_pack.clear();

        cityDb = new CityDb(getApplicationContext());
        stateDb = new StateDb(getApplicationContext());
        serviceDb = new ServiceDb(getApplicationContext());

        shaved_shared_preferences = new Shaved_shared_preferences(getApplicationContext());

        userId = shaved_shared_preferences.get_userid();
        user = shaved_shared_preferences.get_name();
        mobile = shaved_shared_preferences.get_phone();
        email = shaved_shared_preferences.get_user_email();

        Log.e("oooooooooo ", "--" + Packageid + "--" + packages);

        shaved_shared_preferences.set_packIdT("1");
        shaved_shared_preferences.set_packNameT("Free");
        service_history = (TextView) findViewById(R.id.service_history);
        Intent i = getIntent();
        OrderId = i.getStringExtra("OrderId");
        tv_cancelOrder.setVisibility(View.GONE);
        title_name.setText(getname);
        Log.e("imagee ", "---" + getimage);

        setEmptyValueForPendingOrder();


        //   Picasso.with(getApplicationContext()).load(getimage).into(imageView);

        service_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), History.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                //   overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
                finish();
            }
        });

        tv_cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetAlertDialog sweetAlertDialog=  new SweetAlertDialog(ViewOrderDetail.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Cancel Order")
                        .setContentText("Do you want to cancel order? ")
                        .setConfirmText("Yes!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                if (AppController.getInstance().isOnline()) {
                                    sweetAlertDialog.dismiss();
                                    sweetAlertDialog.cancel();
                                    DELETE(OrderId);
                                } else {
                                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setCancelText("No")
                        .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                sweetAlertDialog.dismiss();
                                sweetAlertDialog.cancel();
                            }
                        });
                sweetAlertDialog.setCancelable(false);
                 sweetAlertDialog
                        .show();


            }
        });
        packId = shaved_shared_preferences.get_packId();

        if (isOnline()) {

            FETCH(OrderId);

        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();

        }


    }

    private void setEmptyValueForPendingOrder() {
        Const.catId="";
        Const.serviceId="";
        Const.price="";
        Const.stateid="";
        Const.cityid="";
        Const.areaname="";
        Const.date="";
        Const.time="";
        Const.lattitude="";
        Const.longitude="";
    }

    private void DELETE(final String OrderId) {
        Log.e(TAG+" DELETE", "url = "+Const.URL_CANCEL_ORDER );
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_CANCEL_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG+" DELETE", response.toString());

                        try {
                            JSONObject ob = new JSONObject(response);
                            String text = ob.getString("text");

                            if (text.equals("1")) {


                                tv_cancelOrder.setVisibility(View.GONE);

                                SweetAlertDialog sweetAlertDialog          =new SweetAlertDialog(ViewOrderDetail.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("ORDER CANCELLED!")
                                        .setContentText("Your Order is cancelled!")
                                        .setConfirmText("Ok!")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                                onBackPressed();
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        onBackPressed();
                                                    }
                                                }, 500);
                                            }
                                        });
                                sweetAlertDialog .setCancelable(false);

                                sweetAlertDialog .show();

                                Toast.makeText(getApplicationContext(), "ORDER CANCELLED SUCESSFULLY", Toast.LENGTH_LONG).show();


                            } else {

                                Toast.makeText(getApplicationContext(), "SERVER ERROR", Toast.LENGTH_LONG).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }


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
                params.put("orderId", "" + OrderId);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
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
                Intent intent = new Intent(ViewOrderDetail.this, NavigationScreen.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void FETCH(final String Idd) {

        Log.e(TAG + "FETCH URL_ORDER", Const.URL_ORDER);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG + " URL_ORDER", response.toString());
                        try {
                            JSONObject mainObj = new JSONObject(response);
                            JSONArray jsonArray = mainObj.getJSONArray("data");
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(j);


                                String Id = jsonObject.getString("Id");


                                if (Idd.equalsIgnoreCase(Id)) {
                                    Log.e(TAG + " myOrder compared", ""+jsonObject);

                                    String category_Id = jsonObject.getString("category_Id");
                                    String customer_Id = jsonObject.getString("customer_Id");
                                    String service_Id = jsonObject.getString("service_Id");
                                    String service_price = jsonObject.getString("service_price");
                                    String state_Id = jsonObject.getString("state_Id");
                                    String city_Id = jsonObject.getString("city_Id");
                                    String area_name = jsonObject.getString("area_name");
                                    String date = jsonObject.getString("date");
                                    String time = jsonObject.getString("time");
                                    String slot= jsonObject.getString("slot");
                                    String strIsActive = jsonObject.getString("isActive");
                                    String strCancelledBy = jsonObject.getString("cancel_person");
                                    String task = jsonObject.getString("task");

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
                                    tv_slotTm.setText(""+slot);

                                    if(slot.equalsIgnoreCase(""))
                                    {
                                        ll_slot.setVisibility(View.GONE);
                                    }    tv_cancelledBy.setText("" + strCancelledBy);
                                    if (strIsActive.equalsIgnoreCase("1") && task.equalsIgnoreCase("0")) {
                                        ll_CancelledBy.setVisibility(View.GONE);
                                        tv_cancelOrder.setVisibility(View.VISIBLE);
                                        tv_orderStatus.setText("Pending");
                                    } else if (strIsActive.equalsIgnoreCase("0") && task.equalsIgnoreCase("1")) {
                                        ll_CancelledBy.setVisibility(View.VISIBLE);
                                        tv_orderStatus.setText("Cancelled");
                                    } else if (strIsActive.equalsIgnoreCase("1") && task.equalsIgnoreCase("1")) {
                                        ll_CancelledBy.setVisibility(View.GONE);
                                        tv_orderStatus.setText("Completed");
                                    } else if (strIsActive.equalsIgnoreCase("0") && task.equalsIgnoreCase("0")) {
                                        ll_CancelledBy.setVisibility(View.VISIBLE);
                                        tv_orderStatus.setText("Cancelled");
                                    }



                                    if (task.equalsIgnoreCase("0")) {
                                        cost_id.setText(service_price);

                                    } else {
                                        cost_id.setText("Rs. " + service_price);

                                    }
                                }
                            }
                        } catch (Exception e) {
                            Log.e(TAG + " URL_ORDER Exception ", e.toString());
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_type", "user");
/*                params.put("number", "" + mobile);
                params.put("password", "" + password);*/
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public boolean isOnline() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ViewOrderDetail.this, NavigationScreen.class);
        overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);
        startActivity(intent);
        finish();

    }
}
