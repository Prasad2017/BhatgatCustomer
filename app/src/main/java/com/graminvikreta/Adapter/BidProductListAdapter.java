package com.graminvikreta.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.graminvikreta.Model.Qdata;
import com.graminvikreta.R;

import java.util.List;

public class BidProductListAdapter extends RecyclerView.Adapter<MyHolder> {

    Context context;
    List<Qdata> newsListResponse;
    public static String OrdrId1, Vendor_id;

    public BidProductListAdapter(Context context, List<Qdata> newsListResponse) {
        this.context = context;
        this.newsListResponse = newsListResponse;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.odere_bid_rate_row_customer, null);
        MyHolder OrderedProductsListViewHolder = new MyHolder(context, view, newsListResponse);
        return OrderedProductsListViewHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        Vendor_id=newsListResponse.get(position).getVendor_id();
        holder.textViews.get(0).setText("Anonymous");
        holder.textViews.get(1).setText(newsListResponse.get(position).getBid_amount());
    }

    @Override
    public int getItemCount() {
        return newsListResponse.size();
    }
}

