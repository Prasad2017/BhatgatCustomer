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

import com.graminvikreta.API.APIClient;
import com.graminvikreta.API.APInterface;
import com.graminvikreta.API.Api;
import com.graminvikreta.API.ApiInterface;
import com.graminvikreta.Activity.MainPage;
import com.graminvikreta.Adapter.AwardordersAdapter;
import com.graminvikreta.Adapter.MyBidingAdpter;
import com.graminvikreta.Extra.DetectConnection;
import com.graminvikreta.Model.AllList;
import com.graminvikreta.Model.BidData;
import com.graminvikreta.Model.Qdata;
import com.graminvikreta.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.RetrofitError;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
    public List<Qdata> raisedOrderData = new ArrayList();


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
        MainPage.title.setText("Award Orders");
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

            APInterface apiService = APIClient.getClient().create(APInterface.class);
            Call<AllList> call = apiService.GetAwardOrders(MainPage.userId);

            call.enqueue(new Callback<AllList>() {
                @Override
                public void onResponse(Call<AllList> call, Response<AllList> response) {

                    if (response.isSuccessful()) {

                        AllList data = response.body();
                        raisedOrderData =response.body().getGetdatas();

                        if (raisedOrderData==null){
                            defaultMessage.setVisibility(View.VISIBLE);
                            defaultMessage.setText("No Order Found");
                        }else {

                            orderAdapter = new AwardordersAdapter(getActivity(), raisedOrderData);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerView.setAdapter(orderAdapter);
                            orderAdapter.notifyDataSetChanged();
                            defaultMessage.setVisibility(View.GONE);

                        }

                    }
                }

                @Override
                public void onFailure(Call<AllList> call, Throwable t) {
                    // Log error here since request failed
                    defaultMessage.setVisibility(View.VISIBLE);
                    defaultMessage.setText("No Order Found");
                    Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}