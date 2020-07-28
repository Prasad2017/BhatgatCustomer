package com.graminvikreta.Fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.graminvikreta.Activity.MainPage;
import com.graminvikreta.Adapter.CategoryAdapter;
import com.graminvikreta.Extra.DetectConnection;
import com.graminvikreta.Model.CategoryData;
import com.graminvikreta.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class Category extends Fragment {

    View view;
    String position,category_name;
    @BindView(R.id.categoryRecyclerView)
    RecyclerView categoryRecyclerView;
    TextView sortbc;
    public static SwipeRefreshLayout swipeRefreshLayout;
    public  static  String SubategoryURL = "http://graminvikreta.com/androidApp/Customer/Category.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        position = bundle.getString("position");
        category_name = bundle.getString("category_name");
        Log.e("position",""+category_name);

        sortbc = (TextView) view.findViewById(R.id.sortbc);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.simpleSwipeRefreshLayout);
        sortbc.setText(category_name);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (DetectConnection.checkInternetConnection(getActivity())) {
                    getCategory();
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    Toast.makeText(getActivity(), "Internet Not Available", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        return view;
    }

    private void getCategory() {
        final List<CategoryData> data = new ArrayList<>();

        RequestParams requestParams = new RequestParams();
        requestParams.put("main_category_fk",position);

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(SubategoryURL, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
               try {
                    JSONObject jsonObject = new JSONObject(s.replace("\\\"","'"));
                    JSONArray jsonArray = jsonObject.getJSONArray("success");

                    for (int i=0;i<jsonArray.length();i++){

                        jsonObject = jsonArray.getJSONObject(i);

                        //Set Model Class...
                        CategoryData categoryData = new CategoryData();
                        categoryData.catName =jsonObject.getString("category_name");
                        categoryData.catImage =jsonObject.getString("category_image");
                        categoryData.categary_id =jsonObject.getString("category_pk");

                        data.add(categoryData);

                        //Set Adapter...
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                        categoryRecyclerView.setLayoutManager(gridLayoutManager);
                        CategoryAdapter homeCategoryAdapter = new CategoryAdapter(getActivity(), data);
                        categoryRecyclerView.setAdapter(homeCategoryAdapter);
                        categoryRecyclerView.setHasFixedSize(true);
                        homeCategoryAdapter.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getActivity(), "No Category Found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainPage) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        MainPage.title.setText("Category");
        MainPage.cart.setVisibility(View.VISIBLE);
        MainPage.logo.setVisibility(View.GONE);
        MainPage.title.setVisibility(View.VISIBLE);
        MainPage.search.setVisibility(View.VISIBLE);
        if (DetectConnection.checkInternetConnection(getActivity())) {
            getCategory();
        } else {
            Toast.makeText(getActivity(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }
    }

}
