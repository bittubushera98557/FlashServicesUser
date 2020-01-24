package com.FlashScreen.flashservice.adapter;

/**
 * Created by indiaweb on 10/7/2017.*/

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;

import com.FlashScreen.flashservice.Common.Shaved_shared_preferences;
import com.FlashScreen.flashservice.R;

import java.util.ArrayList;

/**
 * Created by indiaweb on 9/26/2017.
 */

/**
 * Created by sonu on 08/02/17.
 */

public class GridListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Pack> arrayList;
    private LayoutInflater inflater;
    private int selectedPosition = -1;
    Shaved_shared_preferences shaved_shared_preferences;

    public GridListAdapter(Context context, ArrayList<Pack> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        shaved_shared_preferences = new Shaved_shared_preferences(context);

        if (view == null) {
            viewHolder = new ViewHolder();


            view = inflater.inflate(R.layout.grid_layout, null, false);

            viewHolder.radioButton = (RadioButton) view.findViewById(R.id.radio_button);

            view.setTag(viewHolder);






        } else {
            viewHolder = (ViewHolder) view.getTag();

        }

        viewHolder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemCheckChanged(v);
            }
        });

        String pc =arrayList.get(i).getService1();

        Log.e("radioButton","---"+pc);

        try {

            viewHolder.radioButton.setText("" + pc);

        }
        catch (Exception EX)
        {
            EX.getMessage();
        }
        //check the radio button if both position and selectedPosition matches

        try {

            viewHolder.radioButton.setChecked(i == selectedPosition);

            //Set the position tag to both radio button and label
            viewHolder.radioButton.setTag(i);
            //  viewHolder.label.setTag(i);
        }
        catch (Exception ex)
        {
            ex.getMessage();
        }
        return view;
    }

    //On selecting any view set the current position to selectedPositon and notify adapter
    private void itemCheckChanged(View v) {
        selectedPosition = (Integer) v.getTag();
        notifyDataSetChanged();

        Log.e("item_checked_ID", "--" + arrayList.get(selectedPosition).getService_Id());
        Log.e("item_checked_NAME", "--" + arrayList.get(selectedPosition).getService1());

        String PACKID = arrayList.get(selectedPosition).getService_Id();
        String PACKNAME = arrayList.get(selectedPosition).getService1();
        String PRICE = arrayList.get(selectedPosition).getPrice1();

        Log.e("priceeeee ","--"+PRICE);

        shaved_shared_preferences.set_packIdT(PACKID);
        shaved_shared_preferences.set_packNameT(PACKNAME);
        shaved_shared_preferences.set_price(PRICE);
    }

    private class ViewHolder {

        private RadioButton radioButton;
    }


}

