package com.graminvikreta.Fragment;

import android.os.Bundle;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.graminvikreta.Activity.MainPage;
import com.graminvikreta.Adapter.BidProductListAdapter;
import com.graminvikreta.Adapter.ProductListAdapter;
import com.graminvikreta.Extra.DetectConnection;
import com.graminvikreta.Model.ProductData;
import com.graminvikreta.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;


public class BidProduct extends Fragment {

    View view;
    String position,category_name;
    @BindView(R.id.productRecyclerView)
    RecyclerView productRecyclerView;
    public static SwipeRefreshLayout swipeRefreshLayout;
    AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
    public static String ProductlistURL="http://graminvikreta.com/androidApp/Customer/bidproductList.php";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bid_product, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        position = bundle.getString("position");

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.simpleSwipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (DetectConnection.checkInternetConnection(getActivity())) {
                    getBidProductList();
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    Toast.makeText(getActivity(), "Internet Not Available", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        return view;
    }

    private void getBidProductList() {

        final List<ProductData> data = new ArrayList<>();

        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading...");
        pDialog.setCancelable(true);
        pDialog.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("productId", position);

        asyncHttpClient.get(ProductlistURL, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                pDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("success");

                    for (int i=0;i<jsonArray.length();i++) {
                        jsonObject = jsonArray.getJSONObject(i);

                        ProductData productData = new ProductData();
                        productData.product_pk = jsonObject.getInt("product_pk");
                        productData.product_name = jsonObject.getString("product_name");
                        productData.product_description = jsonObject.getString("product_description");
                        productData.product_specification = jsonObject.getString("product_specification");
                        productData.product_price = jsonObject.getString("product_price");
                        productData.product_discount = jsonObject.getString("product_discount");
                        productData.product_final_price = jsonObject.getString("product_final_price");
                        productData.product_actual_stock = jsonObject.getString("product_actual_stock");
                        productData.product_image1 = jsonObject.getString("product_image1");

                        data.add(productData);
                        //Set Adapter...
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                        productRecyclerView.setLayoutManager(gridLayoutManager);
                        BidProductAdapter productListAdapter = new BidProductAdapter(getActivity(), data);
                        productRecyclerView.setAdapter(productListAdapter);
                        productRecyclerView.setHasFixedSize(true);
                        productListAdapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                pDialog.dismiss();
                Toast.makeText(getActivity(), "Error..", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainPage) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        MainPage.title.setText("Products");
        MainPage.cart.setVisibility(View.VISIBLE);
        MainPage.logo.setVisibility(View.GONE);
        MainPage.title.setVisibility(View.VISIBLE);
        MainPage.search.setVisibility(View.VISIBLE);
        if (DetectConnection.checkInternetConnection(getActivity())) {
            getBidProductList();
        } else {
            Toast.makeText(getActivity(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }
    }
}