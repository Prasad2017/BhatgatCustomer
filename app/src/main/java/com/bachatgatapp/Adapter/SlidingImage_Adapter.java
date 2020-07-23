package com.bachatgatapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.squareup.picasso.Picasso;
import com.bachatgatapp.Model.Viewpager;
import com.bachatgatapp.R;

import java.util.ArrayList;

/**
 * Created by Prasad on 06-03-2017.
 */

public class SlidingImage_Adapter extends PagerAdapter {


    private ArrayList<Viewpager> arrayList;
    private LayoutInflater inflater;
    private Context context;
    
    public SlidingImage_Adapter(Context context, ArrayList<Viewpager> arr) {
        Log.e("arr======",""+arr);
        this.context = context;
        this.arrayList= arr;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {

        View itemview = inflater.inflate(R.layout.custom_pager, view, false);
       // assert imageLayout != null;
        ImageView imageView = itemview.findViewById(R.id.imageView);
        String imag1 = "http://graminvikreta.com/bachatgat/"+arrayList.get(position).getImage();
        Log.e("imag1==",""+imag1);
        Log.e("arraylist==",""+arrayList);
        try {
            Picasso.with(context)
                    .load("http://graminvikreta.com/bachatgat/"+arrayList.get(position).getImage())
                    .placeholder(R.drawable.defaultimage)
                    .into(imageView);
            view.addView(itemview);
        }catch (Exception e){
            e.printStackTrace();
        }
        return itemview;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view==(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
