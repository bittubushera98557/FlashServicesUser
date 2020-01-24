package com.FlashScreen.flashservice.adapter;

/**
 * Created by indiaweb on 9/21/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.FlashScreen.flashservice.InnnerClass.MyService;
import com.FlashScreen.flashservice.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyAdapter extends BaseAdapter //implements Filterable
{
    Context c;
    ArrayList<Player> players;
    String name,image;
   // CustomFilter filter;
    ArrayList<Player> filterList;
    public MyAdapter(Context ctx, ArrayList<Player> players) {
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
            convertView=inflater.inflate(R.layout.model, null);
        }
        TextView cat_name=(TextView) convertView.findViewById(R.id.cat_name);
        ImageView cat_image=(ImageView) convertView.findViewById(R.id.cat_image);
        LinearLayout view = (LinearLayout) convertView.findViewById(R.id.view);

        cat_name.setText(players.get(pos).getCategory_name());

        name = players.get(pos).getCategory_name();

        image = "http://iwwphp.info/flashservices/uploads/"+players.get(pos).getImage_upload();


        Picasso.with(c).load(image).into(cat_image);

        view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                name = players.get(pos).getCategory_name();

                image = "http://iwwphp.info/flashservices/uploads/"+players.get(pos).getImage_upload();

                String catId = players.get(pos).getCategory_Id();

                Log.e("jsonObject123","--"+catId);

                Intent i = new Intent(c,MyService.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("catId", "" + catId);
                i.putExtra("name", "" + name);
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
