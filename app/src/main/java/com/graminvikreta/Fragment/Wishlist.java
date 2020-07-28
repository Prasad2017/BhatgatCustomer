package com.graminvikreta.Fragment;


import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.graminvikreta.API.Api;
import com.graminvikreta.Model.WishlistResponse;
import com.graminvikreta.Activity.MainPage;
import com.graminvikreta.Adapter.WishListAdapter;
import com.graminvikreta.Extra.Config;
import com.graminvikreta.Model.Product;
import com.graminvikreta.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Wishlist extends Fragment {

    View view;
    @BindView(R.id.categoryRecyclerView)
    RecyclerView productsRecyclerView;
    public static int categoryPosition = 0;
    @BindView(R.id.emptyWishlistLayout)
    LinearLayout emptyWishlistLayout;
    @BindView(R.id.continueShopping)
    Button continueShopping;
    public static List<Product> productsData = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        ButterKnife.bind(this, view);
        if (!MainPage.userId.equalsIgnoreCase("")) {
            getWishList();
        } else {
            Toast.makeText(getActivity(), "Update Profile", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    @OnClick({R.id.continueShopping})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.continueShopping:
                Config.moveTo(getActivity(), MainPage.class);
                getActivity().finish();
                break;
        }
    }

    private void getWishList() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        Api.getClient().getWishList(MainPage.userId, new Callback<WishlistResponse>() {
            @Override
            public void success(WishlistResponse wishlistResponse, Response response) {
                pDialog.dismiss();
                try {
                    if (wishlistResponse.getSuccess().equalsIgnoreCase("true")) {

                        Log.d("wishId", wishlistResponse.getProducts().size() + "");
                        productsData.clear();
                        productsData = wishlistResponse.getProducts();
                        setProductsData();

                    } else {
                        emptyWishlistLayout.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    Log.d("wishList", "Not available");
                    emptyWishlistLayout.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void failure(RetrofitError error) {
                emptyWishlistLayout.setVisibility(View.VISIBLE);
                pDialog.dismiss();

            }
        });
    }

    private void setProductsData() {
        WishListAdapter wishListAdapter;
        GridLayoutManager gridLayoutManager;
        gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        productsRecyclerView.setLayoutManager(gridLayoutManager);
        wishListAdapter = new WishListAdapter(getActivity(), productsData);
        productsRecyclerView.setAdapter(wishListAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainPage) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        MainPage.title.setText("Wish List");
        MainPage.cart.setVisibility(View.GONE);
        MainPage.logo.setVisibility(View.GONE);
        MainPage.title.setVisibility(View.VISIBLE);
        MainPage.search.setVisibility(View.GONE);
        MainPage.cartCount.setVisibility(View.GONE);
    }
}
