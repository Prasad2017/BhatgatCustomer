package com.bachatgatapp.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.bachatgatapp.Activity.MainPage;
import com.bachatgatapp.Fragment.PastOrderDetails;
import com.bachatgatapp.Model.Orders;
import com.bachatgatapp.R;

import java.util.List;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.MyViewHolder> {

    private List<Orders> OderList;
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


    public MyOrdersAdapter(Context context, List<Orders> moviesList) {
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
        final Orders orders = OderList.get(position);

        holder.productName1.setText(orders.getProduct_name());
        holder.amount.setText(MainPage.currency + " " + orders.getTotal_price());
        holder.quantity.setText(orders.getQty());
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
                    .load("http://logic-fort.com/Customer.in/" + orders.getProduct_image1())
                    .placeholder(R.drawable.defaultimage)
                    .into(holder.productImage1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.viewOrderDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PastOrderDetails pastOrderDetails = new PastOrderDetails();
                Bundle bundle = new Bundle();
                bundle.putInt("position", orders.getProduct_fk());
                pastOrderDetails.setArguments(bundle);
                ((MainPage) context).loadFragment(pastOrderDetails, true);
            }
        });
    }


    @Override
    public int getItemCount() {
        return OderList.size();
    }
}