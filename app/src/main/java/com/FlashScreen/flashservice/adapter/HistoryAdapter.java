package com.FlashScreen.flashservice.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.FlashScreen.flashservice.App.AppController;
import com.FlashScreen.flashservice.Common.Const;
import com.FlashScreen.flashservice.InnnerClass.History;
import com.FlashScreen.flashservice.InnnerClass.ViewOrderDetail;
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

import cn.pedant.SweetAlert.SweetAlertDialog;

public class HistoryAdapter extends BaseAdapter //implements Filterable
{
    Context a;
    Activity c = (Activity) a;
    ArrayList<His> players;
    String name, image, TAG = "HistoryAdapter ";
    ArrayList<His> filterList;

    public HistoryAdapter(Activity ctx, ArrayList<His> players) {
        // TODO Auto-generated constructor stub
        this.c = ctx;
        this.players = players;
        this.filterList = players;
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

        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.history_view, null);
        }
        TextView cat_name = (TextView) convertView.findViewById(R.id.v_order);
        TextView v_date = (TextView) convertView.findViewById(R.id.v_date);
        TextView v_time = (TextView) convertView.findViewById(R.id.v_time);
        TextView v_work = (TextView) convertView.findViewById(R.id.v_work);
        TextView v_workk = (TextView) convertView.findViewById(R.id.v_workk);
        ImageView view = (ImageView) convertView.findViewById(R.id.vieww);
        ImageView del = (ImageView) convertView.findViewById(R.id.del);
        TextView tv_alreadyCancelled = (TextView) convertView.findViewById(R.id.tv_alreadyCancelled);
        cat_name.setText("Order Id: " + players.get(pos).getId());
        v_work.setText(players.get(pos).getCategory_Id());
        v_workk.setText(players.get(pos).getService_Id());
        v_date.setText("Date: " + players.get(pos).getDate());
        String strTm = "" + players.get(pos).getTime();
        try {
     String[]  tmArr=strTm.split("\\.");
     strTm=tmArr[0];
        } catch (Exception e) {

        }
        v_time.setText("Time: " +strTm);
        String strIsActive = players.get(pos).getIsActive();
        String task = players.get(pos).getTask();

      /*  if(isActive.equalsIgnoreCase("1"))
        {
            del.setVisibility(View.VISIBLE);
            tv_alreadyCancelled.setVisibility(View.GONE);
        }
        else {
            del.setVisibility(View.GONE);
            tv_alreadyCancelled.setVisibility(View.VISIBLE);
        }*/

        String status = "";
        if (strIsActive.equalsIgnoreCase("1") && task.equalsIgnoreCase("0")) {
         /*   ll_CancelledBy.setVisibility(View.GONE);
            tv_orderStatus.setText("Pending");*/
            del.setVisibility(View.VISIBLE);
            tv_alreadyCancelled.setVisibility(View.GONE);
            status = "Pending";
        } else if (strIsActive.equalsIgnoreCase("0") && task.equalsIgnoreCase("1")) {
         /*   ll_CancelledBy.setVisibility(View.VISIBLE);
            tv_orderStatus.setText("Cancelled");*/
            del.setVisibility(View.GONE);
            tv_alreadyCancelled.setVisibility(View.VISIBLE);
            tv_alreadyCancelled.setText("Cancelled");
            status = "cancelled";

        } else if (strIsActive.equalsIgnoreCase("1") && task.equalsIgnoreCase("1")) {
            /*ll_CancelledBy.setVisibility(View.GONE);
            tv_orderStatus.setText("Completed");*/
            del.setVisibility(View.GONE);
            tv_alreadyCancelled.setVisibility(View.VISIBLE);
            tv_alreadyCancelled.setText("Completed");
            tv_alreadyCancelled.setTextColor(Color.parseColor("#4CAF50"));

            status = "completed";
        } else if (strIsActive.equalsIgnoreCase("0") && task.equalsIgnoreCase("0")) {
            /*ll_CancelledBy.setVisibility(View.VISIBLE);
            tv_orderStatus.setText("Cancelled");*/
            del.setVisibility(View.GONE);
            tv_alreadyCancelled.setVisibility(View.VISIBLE);
            tv_alreadyCancelled.setText("Cancelled");
            status = "Cancelled";
        }
        Log.e(TAG, " OrderId=" + players.get(pos).getId() + "     isActive=" + strIsActive + "  status=" + status);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String OrderId = players.get(pos).getId();
                String isActive = players.get(pos).getIsActive();
                Intent i = new Intent(c, ViewOrderDetail.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("OrderId", "" + OrderId);
                i.putExtra("Active", "" + isActive);
                c.startActivity(i);
            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String OrderId = players.get(pos).getId();

                new SweetAlertDialog(c, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Cancel Order")
                        .setContentText("Do you want to cancel order? ")
                        .setConfirmText("Yes!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {

                                if (AppController.getInstance().isOnline()) {


                                    callCancelOrderApi(OrderId);
                                } else {

                                    Toast.makeText(c, "No Internet Connection", Toast.LENGTH_LONG).show();
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
                        })
                        .show();


            }
        });

        Log.e(TAG + "order Status", "" + status);

        return convertView;
    }

    private void callCancelOrderApi(final String OrderId) {
        Log.e(TAG + "callCancelOrderApi", Const.URL_CANCEL_ORDER + "?orderId=" + OrderId);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Const.URL_CANCEL_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("TAGGGGGG", response.toString());

                        try {
                            JSONObject ob = new JSONObject(response);
                            String text = ob.getString("text");

                            if (text.equals("1")) {


                                Toast.makeText(c, "ORDER CANCELLED SUCESSFULLY", Toast.LENGTH_LONG).show();

                                Intent i = new Intent(c, History.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                c.startActivity(i);

                            } else {

                                Toast.makeText(c, "SERVER ERROR", Toast.LENGTH_LONG).show();

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
                params.put("orderId", "" + OrderId);
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
