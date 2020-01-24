package com.FlashScreen.flashservice.adapter;

import android.content.Context;
import android.util.Log;
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
 * Created by indiaweb on 11/1/2017.
 */

/**
 * Created by indiaweb on 9/21/2017.
 */

public class NotificationAdapter  extends BaseAdapter //implements Filterable
{
    Context c;
    ArrayList<Notif> players;
    String name,image;
    TextView title_name, message,date;
    ArrayList<Notif> filterList;
    public NotificationAdapter (Context ctx, ArrayList<Notif> players) {
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
            convertView=inflater.inflate(R.layout.notification_view, null);
        }
        // Lookup view for data population
        title_name = (TextView)convertView. findViewById(R.id.title_name);
        message = (TextView)convertView. findViewById(R.id.notification_message);
        date = (TextView)convertView. findViewById(R.id.notification_date);

        String datee = parseDateToddMMyyyy(players.get(pos).getDate());

        try {

        Log.e("DATEEE","--"+players.get(pos).getDate());

        title_name.setText(players.get(pos).getTitle());
        message.setText(players.get(pos).getMessage());
        date.setText(datee);

    }
    catch (Exception ex)
    {
        ex.getMessage();
    }

        return convertView;
    }


    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd-MMM-yyyy";
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
