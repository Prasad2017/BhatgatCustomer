package com.bachatgatapp.Fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.bachatgatapp.API.APIClient;
import com.bachatgatapp.API.APInterface;
import com.bachatgatapp.Activity.MainPage;
import com.bachatgatapp.Adapter.MyOrdersAdapter;
import com.bachatgatapp.Extra.DetectConnection;
import com.bachatgatapp.Model.Orders;
import com.bachatgatapp.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PastOrder extends Fragment {

    View view;
    @BindView(R.id.myOrdersRecyclerView1)
    RecyclerView myOrdersRecyclerView;
    AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
    String cart_id,grandamount;
    double total;
    private List<Orders> movieList = new ArrayList<>();
    public MyOrdersAdapter mAdapter;
    public static String ORDERSLISTURL="http://graminvikreta.com/androidApp/Customer/viewcart.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_past_order, container, false);
        ButterKnife.bind(this, view);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("cart", Context.MODE_PRIVATE);
        cart_id=sharedPreferences.getString("cart_id","0");
        Log.e("cart_id_order==",""+cart_id);

        if (!MainPage.userId.equalsIgnoreCase("")) {
            if (DetectConnection.checkInternetConnection(getActivity())) {
                getMyOrders();
            }else {
                Toast.makeText(getActivity(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Update Profile.", Toast.LENGTH_SHORT).show();
        }

        mAdapter = new MyOrdersAdapter(getActivity(),movieList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        myOrdersRecyclerView.setLayoutManager(mLayoutManager);
        // myOrdersRecyclerView.setAdapter(mAdapter);
        myOrdersRecyclerView.setHasFixedSize(true);
        myOrdersRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        myOrdersRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter.notifyDataSetChanged();

        return view;
    }

    private void getMyOrders() {
        try {

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("cart", Context.MODE_PRIVATE);
            cart_id = sharedPreferences.getString("cart_id", "");
            Log.e("cart_id_order==", "" + cart_id);

            if (DetectConnection.checkInternetConnection(getActivity())) {
                final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
                pDialog.setTitleText("Loading");
                pDialog.setCancelable(false);
                pDialog.show();

                final Orders orders = new Orders();
                orders.setUser_fk(Integer.parseInt(MainPage.userId));

                APInterface apiService = APIClient.getClient().create(APInterface.class);
                Call<Orders> call = apiService.PastOrder(String.valueOf(orders.getUser_fk()));
                call.enqueue(new Callback<Orders>() {
                    @Override
                    public void onResponse(Call<Orders> call, Response<Orders> response) {
                        //int statusCode = response.code();
                        pDialog.dismiss();
                        String result = response.body().getSuccess();
                        if (result.equalsIgnoreCase("true")) {
                            pDialog.dismiss();
                            movieList = response.body().getUserQuestionsList();
                            Log.e("movie", "" + movieList);
                            mAdapter = new MyOrdersAdapter(getActivity(), movieList);
                            myOrdersRecyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();

                        } else {
                            pDialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<Orders> call, Throwable t) {
                        pDialog.dismiss();
                        Toast.makeText(getActivity(), "Something went wrong..", Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                    Toast.makeText(getActivity(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
                }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainPage) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        MainPage.title.setText("Past Order");
        MainPage.cart.setVisibility(View.GONE);
        MainPage.cartCount.setVisibility(View.GONE);
        MainPage.logo.setVisibility(View.GONE);
        MainPage.title.setVisibility(View.VISIBLE);
        MainPage.search.setVisibility(View.GONE);
    }


}
