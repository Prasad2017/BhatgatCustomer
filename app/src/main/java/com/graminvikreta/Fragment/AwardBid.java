package com.graminvikreta.Fragment;

import android.app.Activity;
import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.graminvikreta.API.Api;
import com.graminvikreta.Activity.MainPage;
import com.graminvikreta.Adapter.AwardordersAdapter;
import com.graminvikreta.Adapter.MyBidingAdpter;
import com.graminvikreta.Extra.DetectConnection;
import com.graminvikreta.Model.BidData;
import com.graminvikreta.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


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
    public static BidData mybiddingResponse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_award_bid, container, false);
        ButterKnife.bind(this, view);
        activity = (Activity) view.getContext();

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainPage) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        MainPage.title.setText("My Orders");
        MainPage.cart.setVisibility(View.GONE);
        MainPage.cartCount.setVisibility(View.GONE);
        MainPage.logo.setVisibility(View.GONE);
        MainPage.title.setVisibility(View.VISIBLE);
        MainPage.search.setVisibility(View.GONE);
        MainPage.bottomnavigation.setVisibility(View.GONE);
        if (DetectConnection.checkInternetConnection(getActivity())) {
            getAwardOrders();
        }else {
            Toast.makeText(getActivity(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }
    }

    private void getAwardOrders() {

        try{

            Api.getClient().getAwardBidding(MainPage.userId, new Callback<BidData>() {
                @Override
                public void success(BidData biddingResponse, Response response) {
                    if (biddingResponse.getSuccess().equalsIgnoreCase("true")) {
                        try {

                            mybiddingResponse = biddingResponse;
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            recyclerView.setLayoutManager(linearLayoutManager);
                           //  AwardordersAdapter awardAdapter = new AwardordersAdapter(getActivity(), biddingResponse.getOrderdata());
                           //  recyclerView.setAdapter(awardAdapter);
                             //awardAdapter.notifyDataSetChanged();
                             recyclerView.setHasFixedSize(true);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else {

                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d("size", ""+error.toString());
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}