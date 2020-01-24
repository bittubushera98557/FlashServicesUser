package com.FlashScreen.flashservice.vendor;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.FlashScreen.flashservice.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by indiaweb on 12/7/2017.
 */

public class VendorAdapter extends BaseAdapter //implements Filterable
{
    Context c;
    ArrayList<Vendor> players;
    String name,image;
    // CustomFilter filter;
    ArrayList<Vendor> filterList;
    public VendorAdapter(Context ctx, ArrayList<Vendor> players) {
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
            convertView=inflater.inflate(R.layout.vendor_view, null);
        }
        TextView cat_name=(TextView) convertView.findViewById(R.id.v_order);
        TextView v_date=(TextView) convertView.findViewById(R.id.v_date);
        TextView v_time=(TextView) convertView.findViewById(R.id.v_time);
        TextView v_work=(TextView) convertView.findViewById(R.id.v_work);
        TextView v_workk=(TextView) convertView.findViewById(R.id.v_workk);

        TextView view = (TextView) convertView.findViewById(R.id.vieww);

        cat_name.setText("Order Id: "+players.get(pos).getId());
        v_work.setText(players.get(pos).getCategory());
        v_workk.setText(players.get(pos).getService());
        v_date.setText("Date: "+players.get(pos).getDate());
        v_time.setText("Time: "+players.get(pos).getTime());



        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String catId = players.get(pos).getId();
                Intent i = new Intent(c, VendorDetail.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("OrderId", "" + catId);
                c.startActivity(i);
            }
        });

        return convertView;
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
