package com.graminvikreta.Fragment;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.graminvikreta.Activity.MainPage;
import com.graminvikreta.Model.CategoryData;
import com.graminvikreta.Model.ProductData;
import com.graminvikreta.R;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class BidProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

private Context context;
private LayoutInflater inflater;
        List<ProductData> data = Collections.emptyList();
        CategoryData current;
        int currentPos = 0;

// create constructor to innitilize context and data sent from MainActivity
public BidProductAdapter(Context context, List<ProductData> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        }

// Inflate the layout when viewholder created
@Override
public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.product_list, parent, false);
    MyHolder holder = new MyHolder(view);
        return holder;
        }

// Bind data
@Override
public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder = (BidProductAdapter.MyHolder) holder;
        final ProductData productData = data.get(position);

        myHolder.productname.setText(productData.product_name);
        myHolder.price.setText(MainPage.currency+" "+productData.product_final_price);
        final int product_pk = productData.product_pk;
        myHolder.actualPrice1.setText(MainPage.currency+ " "+productData.product_price);
        myHolder.actualPrice1.setPaintFlags(myHolder.actualPrice1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        // load image into imageview using glide
        try {
        Picasso.with(context)
        .load("http://graminvikreta.com/graminvikreta/" + productData.product_image1)
        .placeholder(R.drawable.defaultimage)
        .into(myHolder.productimage);
        } catch (Exception e) {
        e.printStackTrace();
        }

        try {
        double discountPercentage = Integer.parseInt(productData.product_price) - Integer.parseInt(productData.product_final_price);
        Log.d("percentage", discountPercentage + "");
        discountPercentage = (discountPercentage / Integer.parseInt(productData.product_price)) * 100;
        Log.e("percentage",""+discountPercentage);
        if ((int) Math.round(discountPercentage) > 0) {
        // myHolder.discountPercentage1.setText(((int) Math.round(discountPercentage) + "% Off"));
        }
        }catch (Exception e){

        }

        myHolder.productimage.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        ProductDetail productDetail = new ProductDetail();
        Bundle bundle = new Bundle();
        bundle.putInt("position", product_pk);
        productDetail.setArguments(bundle);
        ((MainPage) context).loadFragment(productDetail, true);
        }
        });
        }

// return total item from List
@Override
public int getItemCount() {
        return data.size();
        }

public class MyHolder extends RecyclerView.ViewHolder {

    TextView productname, actualPrice1;
    TextView price;
    ImageView productimage;

    // create constructor to get widget reference
    public MyHolder(View itemView) {
        super(itemView);

        productname = (TextView) itemView.findViewById(R.id.productname);
        price = (TextView) itemView.findViewById(R.id.price);
        actualPrice1 = (TextView) itemView.findViewById(R.id.actualPrice1);
        productimage = (ImageView) itemView.findViewById(R.id.productimage);
    }

}
}

