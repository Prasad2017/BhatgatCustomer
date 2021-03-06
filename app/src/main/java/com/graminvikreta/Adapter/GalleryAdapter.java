package com.graminvikreta.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.graminvikreta.Activity.MainPage;
import com.graminvikreta.Fragment.Category;
import com.graminvikreta.Model.CategoryData;
import com.graminvikreta.R;

import java.util.Collections;
import java.util.List;

    public class GalleryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context context;
        private LayoutInflater inflater;
        List<CategoryData> data = Collections.emptyList();
        CategoryData current;
        int currentPos = 0;

        // create constructor to innitilize context and data sent from MainActivity
        public GalleryAdapter(Context context, List<CategoryData> data) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.data = data;
        }

        // Inflate the layout when viewholder created
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.squarelayout, parent, false);
            MyHolder holder = new MyHolder(view);
            return holder;
        }

        // Bind data
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

            // Get current position of item in recyclerview to bind data and assign values from list
            MyHolder myHolder = (MyHolder) holder;
            final CategoryData current = data.get(position);

            myHolder.thumbnailtxt.setText(current.catName);
            final String category_id = current.categary_id;
            final String category_name = current.catName;
            Log.e("current.categary_id",""+category_id);
            // load image into imageview using glide
            try {
                Picasso.with(context)
                        .load("http://graminvikreta.com/graminvikreta/"+current.catImage)
                        .placeholder(R.drawable.defaultimage)
                        .into(myHolder.thumbnail);
            }catch (Exception e){
                e.printStackTrace();
            }
            myHolder.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Category category = new Category();
                    Bundle bundle = new Bundle();
                    bundle.putString("position",category_id);
                    bundle.putString("category_name",category_name);
                    Log.e("position",""+category_id);
                    category.setArguments(bundle);
                    ((MainPage)context).loadFragment(category,true);
                }
            });
        }

        // return total item from List
        @Override
        public int getItemCount() {
            return data.size();
        }

       public class MyHolder extends RecyclerView.ViewHolder {

            TextView thumbnailtxt;
            ImageView thumbnail;

            // create constructor to get widget reference
            public MyHolder(View itemView) {
                super(itemView);

                thumbnailtxt = (TextView) itemView.findViewById(R.id.thumbnailtxt);
                thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            }

        }
    }
