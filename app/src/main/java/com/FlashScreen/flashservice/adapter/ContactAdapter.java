package com.FlashScreen.flashservice.adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.FlashScreen.flashservice.Common.Shaved_shared_preferences;
import com.FlashScreen.flashservice.R;

import java.util.ArrayList;

/**
 * Created by indiaweb on 11/15/2017.
 */

/**
 * Created by indiaweb on 10/28/2017.
 */


public class ContactAdapter extends ArrayAdapter<Contacts> {

    private ArrayList<Contacts> dataSet;
    Context mContext;
    String date;
    String Category_Id;
    private LayoutInflater inflater;
    Shaved_shared_preferences shaved_shared_preferences;
    // View lookup cache

    public ContactAdapter(ArrayList<Contacts> data, Context context) {
        super(context, R.layout.contact_layout, data);
        this.dataSet = data;
        this.mContext=context;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Contacts dataModel = getItem(position);
      //  LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        shaved_shared_preferences = new Shaved_shared_preferences(mContext);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.contact_layout, null);
        }

        LinearLayout contact_linear = (LinearLayout) convertView.findViewById(R.id.contact_linear);
        TextView contact_info = (TextView) convertView.findViewById(R.id.contact_info);
        ImageView contact_call = (ImageView) convertView.findViewById(R.id.contact_call);

        contact_info.setText(dataModel.getName() + "\n" + dataModel.getPhone());

        contact_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = dataModel.getPhone();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+number));
                mContext.startActivity(intent);
/*

                String uri = "tel:" + number.trim();
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse(uri));
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mContext.startActivity(intent);
*/

            }
        });

        return convertView;

    }
}
