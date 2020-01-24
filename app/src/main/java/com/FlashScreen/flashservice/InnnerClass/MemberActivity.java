package com.FlashScreen.flashservice.InnnerClass;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.FlashScreen.flashservice.App.AppController;
import com.FlashScreen.flashservice.Common.Const;
import com.FlashScreen.flashservice.Common.Shaved_shared_preferences;
import com.FlashScreen.flashservice.R;
import com.FlashScreen.flashservice.adapter.MemberAdapter;
import com.FlashScreen.flashservice.adapter.Members;
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

public class MemberActivity extends AppCompatActivity {
    String TAG="TAG_MEMBER";
    ProgressDialog progressDialog;
    Shaved_shared_preferences shaved_shared_preferences;
    MemberAdapter myAdapter;
    ListView listview;
    ArrayList<Members> arrayList;
    String userId,TokenId,Token;
    int total = 0 ;
    int length = 0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

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

        progressDialog = new ProgressDialog(MemberActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Please Wait.....");


        arrayList = new ArrayList<>();
        arrayList.clear();

        shaved_shared_preferences = new Shaved_shared_preferences(getApplicationContext());

        listview = (ListView)findViewById(R.id.mylist_member);

        userId = shaved_shared_preferences.get_userid();
        TokenId = shaved_shared_preferences.get_TokenId();
        Token = shaved_shared_preferences.get_Token();

        Log.e("ffffffff ", "-" + userId + "--" + TokenId + "--" + Token);



        if (AppController.getInstance().isOnline())
        {
            Fetch();

        } else

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
                        Fetch();
                    }

                }
            }
        });

    }

    private void Fetch() {
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.Fetch_members,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("REFFFF", response.toString());

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
                                JSONObject jsonObject = jsonArray.getJSONObject(j);

                                Members pack =  new Members();


                                String r_Id = jsonObject.getString("r_Id");
                                String name = jsonObject.getString("name");
                                String email = jsonObject.getString("email");
                                String phone_number = jsonObject.getString("phone_number");

                                Log.e("bbbbbbbb ","--"+r_Id);

                                pack.setId(r_Id);
                                pack.setName(name);
                                pack.setEmail(email);
                                pack.setPhone(phone_number);

                                arrayList.add(pack);
                                myAdapter = new MemberAdapter(arrayList,getApplicationContext());
                                listview.setAdapter(myAdapter);
                            }
                            progressDialog.hide();

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
                Intent intent = new Intent(MemberActivity.this, NavigationAdmin.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_right_out, R.anim.push_right_in);

            default:
                return super.onOptionsItemSelected(item);
        }
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