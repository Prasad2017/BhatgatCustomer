package com.graminvikreta.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.graminvikreta.API.Api;
import com.graminvikreta.Activity.MainPage;
import com.graminvikreta.Adapter.SearchProductListAdapter;
import com.graminvikreta.Extra.DetectConnection;
import com.graminvikreta.Model.Product;
import com.graminvikreta.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Searchview extends Fragment {

    @BindView(R.id.searchProductsRecyclerView)
    RecyclerView searchProductsRecyclerView;
    @BindView(R.id.searchEditText)
    EditText searchEditText;
    List<Product> productList;
    public static List<Product> allProductsData;
    @BindView(R.id.defaultMessage)
    TextView defaultMessage;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_searchview, container, false);
        ButterKnife.bind(this, view);
        defaultMessage.setText("Search Any Product");

        if (DetectConnection.checkInternetConnection(getActivity())) {
            getAllProducts();
        }else {
            Toast.makeText(getActivity(), "Internet Connection not avalibale", Toast.LENGTH_SHORT).show();
        }

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (DetectConnection.checkInternetConnection(getActivity())) {
                    Log.d("text", editable.toString());
                    searchProducts(editable.toString());
                }else {
                    Toast.makeText(getActivity(), "Internet Connection not avalibale", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void getAllProducts() {

        Api.getClient().getAllProducts(new Callback<List<Product>>() {
            @Override
            public void success(List<Product> allProducts, Response response) {
                try {
                    allProductsData = allProducts;
                    Log.d("allProductsData", allProducts.get(0).getProduct_name());
                    Gson gson = new Gson();
                    String json = gson.toJson(allProductsData);

                } catch (Exception e) {
                    Toast.makeText(getActivity(), "No Product Added In This Store!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("error", error.toString());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainPage) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        MainPage.title.setText("Search");
        MainPage.cart.setVisibility(View.VISIBLE);
        MainPage.logo.setVisibility(View.GONE);
        MainPage.title.setVisibility(View.VISIBLE);
        if (DetectConnection.checkInternetConnection(getActivity())) {
            getAllProducts();
        } else {
            Toast.makeText(getActivity(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void searchProducts(String s) {
        productList = new ArrayList<>();
        if (s.length() > 0) {
            for (int i = 0; i < allProductsData.size(); i++)
                if (allProductsData.get(i).getProduct_name().toLowerCase().contains(s.toLowerCase().trim())) {
                    productList.add(allProductsData.get(i));
                }
            if (productList.size() < 1) {
                defaultMessage.setText("Record Not Found");
                defaultMessage.setVisibility(View.VISIBLE);
            } else {
                defaultMessage.setVisibility(View.GONE);
            }
            Log.e("size", productList.size() + "" + allProductsData.size());
        } else {
            productList = new ArrayList<>();
            defaultMessage.setText("Search Any Product");
            defaultMessage.setVisibility(View.VISIBLE);
        }
        setProductsData();


    }

    private void setProductsData() {
        SearchProductListAdapter productListAdapter;
        GridLayoutManager gridLayoutManager;
        gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        searchProductsRecyclerView.setLayoutManager(gridLayoutManager);
        productListAdapter = new SearchProductListAdapter(getActivity(), productList);
        searchProductsRecyclerView.setAdapter(productListAdapter);

    }
}
