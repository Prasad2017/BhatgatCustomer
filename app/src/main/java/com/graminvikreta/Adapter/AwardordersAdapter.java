package com.graminvikreta.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.graminvikreta.Activity.MainPage;
import com.graminvikreta.Model.DeliverStock;
import com.graminvikreta.Model.Qdata;
import com.graminvikreta.R;
import com.loopj.android.http.RequestParams;

import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;

public class AwardordersAdapter extends RecyclerView.Adapter<AwardordersAdapter.MyOrderholder> {

    Context context;
    List<Qdata> results;

    public AwardordersAdapter(Context context, List<Qdata> results) {
        this.context = context;
        this.results = results;
    }

    @NonNull
    @Override
    public MyOrderholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.award_order_list, null);
        MyOrderholder MyOrdersViewHolder = new MyOrderholder(context, view);
        return MyOrdersViewHolder;
        
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderholder holder, final int position) {

        holder.textViews.get(0).setText(results.get(position).getDate());
        holder.textViews.get(1).setText(results.get(position).getBid_amount());
        holder.textViews.get(2).setText(results.get(position).getBid_id());

        holder.textViews.get(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });

    }

    @Override
    public int getItemCount() {
        return results.size();
    }


    public class MyOrderholder extends RecyclerView.ViewHolder {

        @BindViews({R.id.date2, R.id.rate2, R.id.OrderNo2, R.id.pay})
        List<TextView> textViews;

        public MyOrderholder(Context context, @NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}