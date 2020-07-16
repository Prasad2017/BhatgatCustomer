package com.bachatgatapp.Adapter;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.bachatgatapp.Activity.MainPage;
import com.bachatgatapp.Fragment.ProductDetail;
import com.bachatgatapp.Model.Product;
import com.bachatgatapp.R;

import java.util.List;

public class SearchProductListAdapter extends RecyclerView.Adapter<HomeProductsViewHolder> {
    Context context;
    List<Product> productList;

    public SearchProductListAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @Override
    public HomeProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.home_products_list_items, null);
        HomeProductsViewHolder homeProductsViewHolder = new HomeProductsViewHolder(context, view, productList);
        return homeProductsViewHolder;
    }

    @Override
    public void onBindViewHolder(final HomeProductsViewHolder holder, final int position) {

        holder.cardView.setVisibility(View.GONE);
        holder.cardView1.setVisibility(View.VISIBLE);
        holder.productName1.setText(productList.get(position).getProduct_name());
        holder.price1.setText(MainPage.currency + " " + productList.get(position).getProduct_final_price());
        try {
            String image = String.valueOf(productList.get(position).getProduct_image1());
            String string = image.substring(1, image.length()-1);
            Log.e("img",""+string);
            Picasso.with(context)
                    .load("http://prabhagmaza.com/Customer.in/"+string)
                    .placeholder(R.drawable.defaultimage)
                    .into(holder.image1);
        } catch (Exception e) {
        }
        try {

            double discountPercentage = Integer.parseInt(productList.get(position).getProduct_price()) - Integer.parseInt(productList.get(position).getProduct_final_price());
            Log.d("percentage", discountPercentage + "");
            discountPercentage = (discountPercentage / Integer.parseInt(productList.get(position).getProduct_price())) * 100;
            if ((int) Math.round(discountPercentage) > 0) {
                holder.discountPercentage1.setText(((int) Math.round(discountPercentage) + "% Off"));
            }
            holder.actualPrice1.setText(MainPage.currency + " " + productList.get(position).getProduct_price());
            holder.actualPrice1.setPaintFlags(holder.actualPrice1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        }catch (Exception e){}
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductDetail.productList.clear();
                ProductDetail.productList.addAll(productList);
                ProductDetail productDetail = new ProductDetail();
                Bundle bundle = new Bundle();
                bundle.putInt("position", Integer.parseInt(productList.get(position).getProduct_pk()));
                Log.e("positions..",""+position);
                productDetail.setArguments(bundle);
                ((MainPage) context).loadFragment(productDetail, true);
            }
        });


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

}
