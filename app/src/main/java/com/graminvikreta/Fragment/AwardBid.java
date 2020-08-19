package com.graminvikreta.Fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.graminvikreta.Adapter.AwardordersAdapter;
import com.graminvikreta.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AwardBid extends Fragment {


    @BindView(R.id.simpleListView)
    RecyclerView recyclerView;
    View view;
    public static Activity activity;
    AwardordersAdapter orderAdapter;
    @BindView(R.id.simpleSwipeRefreshLayout)
    SwipeRefreshLayout award_swipe;
    @BindView(R.id.searchEditText)
    EditText searchEditText;
    @BindView(R.id.defaultMessage)
    TextView defaultMessage;
 /*   List<Order> clientList;
    public List<Order> raisedOrderData = new ArrayList();*/


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_award_bid, container, false);
        ButterKnife.bind(this, view);
        activity = (Activity) view.getContext();

        return view;
    }

}