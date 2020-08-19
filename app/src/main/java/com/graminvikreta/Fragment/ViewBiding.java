package com.graminvikreta.Fragment;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.graminvikreta.API.Api;
import com.graminvikreta.Activity.MainPage;
import com.graminvikreta.Adapter.BidProductListAdapter;
import com.graminvikreta.Adapter.MyBidingAdpter;
import com.graminvikreta.Extra.DetectConnection;
import com.graminvikreta.Model.BidData;
import com.graminvikreta.R;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class ViewBiding extends Fragment {

    View view;
    @BindView(R.id.acceptsimpleListView)
    RecyclerView acceptsimpleListView;
    public static SwipeRefreshLayout swipeRefreshLayout;
    MyBidingAdpter myOrdersAdapter;
    public static BidData mybiddingResponse;
    String productId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_biding, container, false);
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        productId= bundle.getString("productId");


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainPage) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        MainPage.title.setText("View Bidding");
        MainPage.cart.setVisibility(View.VISIBLE);
        MainPage.logo.setVisibility(View.GONE);
        MainPage.title.setVisibility(View.VISIBLE);
        if (DetectConnection.checkInternetConnection(getActivity())) {
            getAllBiding();
        } else {
            Toast.makeText(getActivity(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }
    }

    private void getAllBiding() {

        try{

            Api.getClient().getBidding(MainPage.userId, productId, new Callback<BidData>() {
                @Override
                public void success(BidData biddingResponse, Response response) {
                    if (biddingResponse.getSuccess().equalsIgnoreCase("true")) {
                        try {

                            mybiddingResponse = biddingResponse;
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                            acceptsimpleListView.setLayoutManager(linearLayoutManager);
                            myOrdersAdapter = new MyBidingAdpter(getActivity(), biddingResponse.getOrderdata());
                            acceptsimpleListView.setAdapter(myOrdersAdapter);
                            myOrdersAdapter.notifyDataSetChanged();
                            acceptsimpleListView.setHasFixedSize(true);

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