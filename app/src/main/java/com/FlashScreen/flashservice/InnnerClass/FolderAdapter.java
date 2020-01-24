package com.FlashScreen.flashservice.InnnerClass;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;

import com.FlashScreen.flashservice.Common.Shaved_shared_preferences;
import com.FlashScreen.flashservice.R;
import com.FlashScreen.flashservice.adapter.Pack;

import java.util.ArrayList;

/**
 * Created by indiaweb on 12/6/2017.
 */


class FolderAdapter extends ArrayAdapter<String>
{
    Shaved_shared_preferences shaved_shared_preferences;
    ArrayList<Pack> list;
    Context context;
    RadioButton selected=null;
    private LayoutInflater inflater;
    public FolderAdapter(Context context, int resource, ArrayList<Pack> list)
    {
        super(context, resource);
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
      //  LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (inflater == null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)

        convertView = inflater.inflate(R.layout.grid_layout, null);
        
        final RadioButton rbFolder = (RadioButton) convertView.findViewById(R.id.radio_button);
        shaved_shared_preferences = new Shaved_shared_preferences(context);
        rbFolder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(selected != null)
                {
                    selected.setChecked(false);
                }

                rbFolder.setChecked(true);
                selected = rbFolder;

                Log.e("item_checked_ID", "--" + list.get(position).getService_Id());
                Log.e("item_checked_NAME", "--" + list.get(position).getService1());

                String PACKID = list.get(position).getService_Id();
                String PACKNAME = list.get(position).getService1();
                String PRICE = list.get(position).getPrice1();

                Log.e("priceeeee ","--"+PRICE);

                shaved_shared_preferences.set_packIdT(PACKID);
                shaved_shared_preferences.set_packNameT(PACKNAME);
                shaved_shared_preferences.set_price(PRICE);
            }
        });
        rbFolder.setText(list.get(position).getService1());

        Log.e("fffffffff ", "--" + list.get(position).getService1());
        Log.e("fffffffff ","--"+list.get(position).getService_Id());

        return convertView;
    }

    @Override
    public int getCount()
    {
        return list.size();
    }
}

