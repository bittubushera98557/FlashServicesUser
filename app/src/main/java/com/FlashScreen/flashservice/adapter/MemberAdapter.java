package com.FlashScreen.flashservice.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.FlashScreen.flashservice.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by indiaweb on 11/1/2017.
 */

/**
 * Created by indiaweb on 9/21/2017.
 */

public class MemberAdapter   extends BaseAdapter //implements Filterable
{
    Context c;
    TextView Member_Id,member_name,member_info,member_city,member_address;
    LinearLayout cat_linear;
    ArrayList<Members> players;
    String name,image;
    String IDD;
    // CustomFilter filter;
    ArrayList<Members> filterList;
    public MemberAdapter  ( ArrayList<Members> players,Context ctx) {
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
            convertView=inflater.inflate(R.layout.member_layout, null);
        }



        Member_Id = (TextView) convertView.findViewById(R.id.Member_Id);
        member_name = (TextView) convertView.findViewById(R.id.member_namee);
        member_info = (TextView) convertView.findViewById(R.id.member_info);
        member_city = (TextView) convertView.findViewById(R.id.member_city);
        member_address = (TextView) convertView.findViewById(R.id.member_address);

        IDD = players.get(pos).getId();

        Log.e("hhhhhhhhh ", "--" + IDD);

        try {

            member_name.setText(players.get(pos).getName() + "\n" + players.get(pos).getPhone());
            member_info.setText("Ref.By: ");
            Member_Id.setText("ID: " + IDD);
            //viewHolder.member_city.setText("Ref.By: ");
            //    viewHolder.member_address.setText(member.getAddress());
        }
        catch (Exception ex)
        {
            ex.getMessage();
        }
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
