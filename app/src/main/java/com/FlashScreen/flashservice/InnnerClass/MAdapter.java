package com.FlashScreen.flashservice.InnnerClass;

import android.support.v4.view.PagerAdapter;

import java.util.ArrayList;

/**
 * Created by indiaweb on 11/29/2017.
 */

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.FlashScreen.flashservice.R;

import java.util.ArrayList;

public class MAdapter extends PagerAdapter {

    private ArrayList<Integer> images;
    private LayoutInflater inflater;
    private Context context;

    public MAdapter(Context context, ArrayList<Integer> images) {
        this.context = context;
        this.images=images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.slide, view, false);
        ImageView myImage = (ImageView) myImageLayout.findViewById(R.id.image);
        TextView tt = (TextView) myImageLayout.findViewById(R.id.ttt);
        myImage.setImageResource(images.get(position));

        try {


            if (position == 0) {
                tt.setText("SLIDE 2");
            } else if (position == 1) {
                tt.setText("Electrical Services");
            } else if (position == 2) {
                tt.setText("Carpenter Services");
            } else if (position == 3) {
                tt.setText("Painting Services");
            } else if (position == 4) {
                tt.setText("Plumbing Services");
            }
        }
        catch (Exception ex)
        {
            ex.getMessage();
        }
        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
