package com.FlashScreen.flashservice.vendor;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.FlashScreen.flashservice.R;

import java.util.ArrayList;

/**
 * Created by indiaweb on 12/5/2017.
 */
public class MyCustomAdapter extends ArrayAdapter<Country> {

    public ArrayList<Country> countryList;
    Context context;
    private LayoutInflater inflater;
    public MyCustomAdapter(Context context, int textViewResourceId,
                           ArrayList<Country> countryList) {
        super(context, textViewResourceId, countryList);
        this.countryList = new ArrayList<Country>();
        this.countryList.addAll(countryList);
        this.context  = context;
        inflater = LayoutInflater.from(context);
    }

    private class ViewHolder {
        TextView code;
        CheckBox name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Log.v("ConvertView", String.valueOf(position));

        if (convertView == null) {

            convertView = inflater.inflate(R.layout.country_info, null);

            holder = new ViewHolder();
            holder.code = (TextView) convertView.findViewById(R.id.code);
            holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
            convertView.setTag(holder);
            try {

                holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        Country country = (Country) cb.getTag();
            /*        Toast.makeText(context,
                            "Clicked on Checkbox: " + cb.getText() +
                                    " is " + cb.isChecked(),
                            Toast.LENGTH_LONG).show();*/
                        country.setSelected(cb.isChecked());
                    }
                });
            }
            catch (Exception ex)
            {
                ex.getMessage();
            }
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Country country = countryList.get(position);
        holder.code.setText("");
        holder.name.setText(country.getName());
        holder.name.setChecked(country.isSelected());
        holder.name.setTag(country);

        return convertView;

    }

}
