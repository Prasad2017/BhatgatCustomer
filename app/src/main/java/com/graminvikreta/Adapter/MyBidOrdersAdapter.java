package com.graminvikreta.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.graminvikreta.Activity.MainPage;
import com.graminvikreta.Fragment.PastOrderDetails;
import com.graminvikreta.Model.OrderData;
import com.graminvikreta.Model.Orders;
import com.graminvikreta.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyBidOrdersAdapter extends RecyclerView.Adapter<MyBidOrdersAdapter.MyViewHolder> {

    private List<OrderData> OderList;
    public Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView productName1,viewOrderDetails,amount,status;
        public EditText quantity;
        public ImageView productImage1;

        public MyViewHolder(View view) {
            super(view);

            productName1 = (TextView) view.findViewById(R.id.productName1);
            viewOrderDetails = (TextView) view.findViewById(R.id.viewOrderDetails);
            status = (TextView) view.findViewById(R.id.date);
            amount = (TextView) view.findViewById(R.id.amount);
            quantity = (EditText) view.findViewById(R.id.quantity);
            productImage1 = (ImageView) view.findViewById(R.id.productImage1);

        }
    }


    public MyBidOrdersAdapter(Context context, List<OrderData> moviesList) {
        this.OderList = moviesList;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.past_orders_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final OrderData orders = OderList.get(position);

        holder.productName1.setText(orders.getProduct_name());
        holder.amount.setText(MainPage.currency + " " + orders.getGrand_amount());
        // holder.status.setText(orders.getOrder_status());
        try {
            if (orders.getOrder_status().equalsIgnoreCase("order_raised")) {
                holder.status.setText(orders.getOrder_status().replace("_"," "));
                holder.status.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            } else if (orders.getOrder_status().equalsIgnoreCase("order_accept")) {
                holder.status.setText(orders.getOrder_status().replace("_"," "));
                holder.status.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            } else if (orders.getOrder_status().equalsIgnoreCase("order_pickup")) {
                holder.status.setText(orders.getOrder_status().replace("_"," "));
                holder.status.setTextColor(context.getResources().getColor(R.color.light_red));
            } else if (orders.getOrder_status().equalsIgnoreCase("order_done")) {
                holder.status.setText(orders.getOrder_status().replace("_"," "));
                holder.status.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            } else if (orders.getOrder_status().equalsIgnoreCase("order_dispatched")) {
                holder.status.setText(orders.getOrder_status().replace("_"," "));
                holder.status.setTextColor(context.getResources().getColor(R.color.black));
            } else if (orders.getOrder_status().equalsIgnoreCase("order_completed")) {
                holder.status.setText(orders.getOrder_status().replace("_"," "));
                holder.status.setTextColor(context.getResources().getColor(R.color.colorAccent));
            } else if (orders.getOrder_status().equalsIgnoreCase("order_canceled")) {
                holder.status.setText(orders.getOrder_status().replace("_"," "));
                holder.status.setTextColor(context.getResources().getColor(R.color.red));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            Picasso.with(context)
                    .load("http://graminvikreta.com/graminvikreta/" + orders.getProduct_image())
                    .placeholder(R.drawable.defaultimage)
                    .into(holder.productImage1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        return OderList.size();
    }
}