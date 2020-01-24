package com.FlashScreen.flashservice.adapter;

/**
 * Created by indiaweb on 11/9/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.FlashScreen.flashservice.Common.Const;
import com.FlashScreen.flashservice.InnnerClass.AllServices;
import com.FlashScreen.flashservice.InnnerClass.AssignScreen;
import com.FlashScreen.flashservice.InnnerClass.SubScreen;
import com.FlashScreen.flashservice.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AllAdapter extends BaseAdapter //implements Filterable
{
    Context c;
    ArrayList<All> players;
    String Order_Id,Assignid,Category_Id,Service_Id,Category,ProId,Service,date,Time,State,City,Area;
    // CustomFilter filter;
    ArrayList<All> filterList;
    public AllAdapter(Context ctx, ArrayList<All> players) {
        // TODO Auto-generated constructor stub
        this.c=ctx;
        this.players=players;
        this.filterList=players;
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return players.size();
    }
    @Override
    public Object getItem(int pos) {
        // TODO Auto-generated method stub
        return players.get(pos);
    }
    @Override
    public long getItemId(int pos) {
        // TODO Auto-generated method stub
        return players.indexOf(getItem(pos));
    }
    @Override
    public View getView(final int pos, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        LayoutInflater inflater=(LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView==null)
        {
            convertView=inflater.inflate(R.layout.all_view, null);
        }
        TextView cat_name=(TextView) convertView.findViewById(R.id.v_order);
        TextView v_date=(TextView) convertView.findViewById(R.id.v_date);
        TextView v_time=(TextView) convertView.findViewById(R.id.v_time);
        TextView v_work=(TextView) convertView.findViewById(R.id.v_work);
        TextView v_workk=(TextView) convertView.findViewById(R.id.v_workk);

        LinearLayout assign_linear=(LinearLayout) convertView.findViewById(R.id.assign_linear);
        LinearLayout unassign_linear=(LinearLayout) convertView.findViewById(R.id.unassign_linear);
        LinearLayout doneassign_linear=(LinearLayout) convertView.findViewById(R.id.doneassign_linear);

        TextView assigned=(TextView) convertView.findViewById(R.id.assigned);
        TextView assignedd=(TextView) convertView.findViewById(R.id.assignedd);
        final TextView assign=(TextView) convertView.findViewById(R.id.assign);
        final TextView completed=(TextView) convertView.findViewById(R.id.completed);
        TextView cancel=(TextView) convertView.findViewById(R.id.cancel);
        TextView change=(TextView) convertView.findViewById(R.id.change);

        TextView view = (TextView) convertView.findViewById(R.id.vieww);

        cat_name.setText("Order Id: " + players.get(pos).getId());
        v_work.setText(players.get(pos).getCategory());
        v_workk.setText(players.get(pos).getService());
        v_date.setText("Date: " + players.get(pos).getDate());
        v_time.setText("Time: " + players.get(pos).getTime());

        if(players.get(pos).getPId().equalsIgnoreCase(""))

        {
            assign_linear.setVisibility(View.VISIBLE);
            unassign_linear.setVisibility(View.GONE);
            doneassign_linear.setVisibility(View.GONE);
        }
else {

            String TASK = players.get(pos).getTask();





            if(TASK.equalsIgnoreCase("1"))
            {
                assign_linear.setVisibility(View.GONE);
                doneassign_linear.setVisibility(View.VISIBLE);
                unassign_linear.setVisibility(View.GONE);

                assignedd.setText(players.get(pos).getPr_name() + "\n" + players.get(pos).getPr_num());
            }
            else {
                assign_linear.setVisibility(View.GONE);
                unassign_linear.setVisibility(View.VISIBLE);
                doneassign_linear.setVisibility(View.GONE);

                assigned.setText(players.get(pos).getPr_name() + "\n" + players.get(pos).getPr_num());
            }


        }
        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String catId = players.get(pos).getId();

                Log.e("oooooooooorr ", "--" + catId);

                Intent i = new Intent(c, SubScreen.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("OrderId", "" + catId);
                c.startActivity(i);

            }
        });

        assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Order_Id = players.get(pos).getId();
                Service_Id = players.get(pos).getService_Id();
                ProId = players.get(pos).getProvider_Id();
                Category = players.get(pos).getCategory();
                Service = players.get(pos).getService();
                date = players.get(pos).getDate();
                Time = players.get(pos).getTime();
                Assignid = players.get(pos).getPId();
                String customer_id = players.get(pos).getCustomer_Id();

                State = players.get(pos).getState_Id();
                City = players.get(pos).getCity_Id();
                Area = players.get(pos).getArea_name();

                Intent i = new Intent(c, AssignScreen.class);
                i.putExtra("Assign", "0");
                i.putExtra("Order_Id", "" + Order_Id);
                i.putExtra("Service_Id", "" + Service_Id);
                Category_Id = players.get(pos).getCategory_Id();
                i.putExtra("Assignid", "");
                i.putExtra("Category", "" + Category);
                i.putExtra("Category_Id", "" + Category_Id);
                i.putExtra("customer_id", "" + customer_id);
                i.putExtra("ProId", "0");
                i.putExtra("Service", "" + Service);
                i.putExtra("date", "" + date);
                i.putExtra("Time", "" + Time);
                i.putExtra("State", "" + State);
                i.putExtra("City", "" + City);
                i.putExtra("Area", "" + Area);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(i);
            }

        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String catId = players.get(pos).getId();
                String customer_id = players.get(pos).getCustomer_Id();

             sendCancel(catId,customer_id);
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Order_Id = players.get(pos).getId();
                ProId = players.get(pos).getProvider_Id();
                Category = players.get(pos).getCategory();
                Category_Id = players.get(pos).getCategory_Id();
                Service = players.get(pos).getService();
                date = players.get(pos).getDate();
                Time = players.get(pos).getTime();
                Assignid = players.get(pos).getPId();
                String customer_id = players.get(pos).getCustomer_Id();

                State = players.get(pos).getState_Id();
                City = players.get(pos).getCity_Id();
                Area = players.get(pos).getArea_name();

                Intent i = new Intent(c, AssignScreen.class);
                i.putExtra("Assign", "1");
                i.putExtra("Order_Id", "" + Order_Id);
                i.putExtra("Service_Id", "" + Service_Id);
                i.putExtra("Assignid", "" + Assignid);
                i.putExtra("Category", "" + Category);
                i.putExtra("Service", "" + Service);
                i.putExtra("date", "" + date);
                i.putExtra("Time", "" + Time);
                i.putExtra("ProId", "" + ProId);
                i.putExtra("customer_id", "" + customer_id);
                i.putExtra("Category_Id", "" + Category_Id);
                i.putExtra("State", "" + State);
                i.putExtra("City", "" + City);
                i.putExtra("Area", "" + Area);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(i);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String catId = players.get(pos).getId();

                Log.e("oooooooooorr ", "--" + catId);

                Intent i = new Intent(c, SubScreen.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("OrderId", "" + catId);
                c.startActivity(i);
            }
        });

        return convertView;
    }

    private void sendCancel(final String CATID,final String CUSID)
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_CANCEL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("TAGGGGGG", response.toString());

                        try {
                            JSONObject ob = new JSONObject(response);
                            String text = ob.getString("text");

                            if(text.equals("1"))
                            {
                                Toast.makeText(c, "Service Cancelled Successfully", Toast.LENGTH_LONG).show();

                                Intent i = new Intent(c, AllServices.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                c.startActivity(i);

                            }
                            else {
                                Toast.makeText(c,"Server Error",Toast.LENGTH_LONG).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(c, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("orderId", "" + CATID);
                params.put("userId", "" + CUSID);
                params.put("providerId", "0");
                params.put("assignId", "0");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(c);
        requestQueue.add(stringRequest);


    }


    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd hh:mm:ss";
        String outputPattern = "dd-MMM-yyyy\nhh:mm";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

}
