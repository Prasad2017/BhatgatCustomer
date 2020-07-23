package com.bachatgatapp.Fragment;


import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.bachatgatapp.Activity.MainPage;
import com.bachatgatapp.Adapter.ProductListAdapter;
import com.bachatgatapp.Extra.DetectConnection;
import com.bachatgatapp.Model.ProductData;
import com.bachatgatapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductList extends Fragment {

    View view;
    String position,category_name;
    @BindView(R.id.productRecyclerView)
    RecyclerView productRecyclerView;
    TextView sortbc,Sort,Filter;
    public static SwipeRefreshLayout swipeRefreshLayout;
    public static String ProductlistURL="http://graminvikreta.com/androidApp/Customer/Product_List.php";
    public static String GetCartURL="http://graminvikreta.com/androidApp/Customer/cartcount.php";
    public static String FilterURL="http://graminvikreta.com/androidApp/Customer/Filter.php";
    public static String LowURL="http://graminvikreta.com/androidApp/Customer/getlow.php";
    public static String HignURL="http://graminvikreta.com/androidApp/Customer/gethigh.php";
    public static String AtoZURL="http://graminvikreta.com/androidApp/Customer/getAtoZ.php";
    public static String ZtoAURL="http://graminvikreta.com/androidApp/Customer/getZtoA.php";
    AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
    String cart_id;
    String maxvalue,minvalue;
    @BindView(R.id.bottom_sheet)
    RelativeLayout layoutBottomSheet;
    BottomSheetBehavior sheetBehavior;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_product_list, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        position = bundle.getString("position");
        category_name = bundle.getString("category_name");
        Log.e("position", "" + category_name);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.simpleSwipeRefreshLayout);
        Sort = (TextView) view.findViewById(R.id.sort);
        Filter = (TextView) view.findViewById(R.id.filter);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);

        /*sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });*/
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (DetectConnection.checkInternetConnection(getActivity())) {
                    getProductList();
                    getCart();
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    Toast.makeText(getActivity(), "Internet Not Available", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        Filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.payment_option);

                final EditText min = (EditText) dialog.findViewById(R.id.min);
                final EditText max = (EditText) dialog.findViewById(R.id.max);
                final Button pay = (Button) dialog.findViewById(R.id.pay);
                dialog.show();

                min.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                       // min.setText(MainPage.currency);
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        minvalue = MainPage.currency+min.getText().toString();
                       // min.setText(minvalue);
                    }
                });

                max.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                       // max.setText(MainPage.currency);
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                       maxvalue = MainPage.currency+max.getText().toString();
                      // max.setText(maxvalue);
                    }
                });

                pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final List<ProductData> data = new ArrayList<>();

                        final SweetAlertDialog  pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
                        pDialog.setTitleText("Loading...");
                        pDialog.setCancelable(true);
                        pDialog.show();

                        RequestParams requestParams = new RequestParams();
                        requestParams.put("category_pk",position);
                        requestParams.put("min",minvalue.replace(MainPage.currency,""));
                        requestParams.put("max",maxvalue.replace(MainPage.currency,""));

                        asyncHttpClient.get(FilterURL, requestParams, new AsyncHttpResponseHandler() {
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
                                        Log.e("price==",""+productData.product_price);
                                        productData.product_discount = jsonObject.getString("product_discount");
                                        productData.product_final_price = jsonObject.getString("product_final_price");
                                        productData.product_actual_stock = jsonObject.getString("product_actual_stock");
                                        productData.product_image1 = jsonObject.getString("product_image1");

                                        data.add(productData);
                                        //Set Adapter...
                                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                                        productRecyclerView.setLayoutManager(gridLayoutManager);
                                        ProductListAdapter productListAdapter = new ProductListAdapter(getActivity(), data);
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
                        dialog.dismiss();
                    }
                });

            }
        });

        Sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View view = getLayoutInflater().inflate(R.layout.bottom_sheet_content, null);
                final BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
                dialog.setContentView(view);

                final TextView lowprice = (TextView) dialog.findViewById(R.id.lowprice);
                final TextView highprice = (TextView) dialog.findViewById(R.id.highprice);
                final TextView atoz = (TextView) dialog.findViewById(R.id.atoz);
                final TextView ztoa = (TextView) dialog.findViewById(R.id.ztoa);
                final ImageView cloose = (ImageView)dialog.findViewById(R.id.cloose);

                dialog.show();

                cloose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//                        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                        dialog.dismiss();
                    }
                });

                lowprice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        lowprice.setTextColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
                        highprice.setTextColor(getActivity().getResources().getColor(R.color.black));
                        ztoa.setTextColor(getActivity().getResources().getColor(R.color.black));
                        atoz.setTextColor(getActivity().getResources().getColor(R.color.black));
                        final List<ProductData> data = new ArrayList<>();

                        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
                        pDialog.setTitleText("Loading...");
                        pDialog.setCancelable(true);
                        pDialog.show();

                        RequestParams requestParams = new RequestParams();
                        requestParams.put("category_pk", position);

                        asyncHttpClient.get(LowURL, requestParams, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                String s = new String(responseBody);
                                pDialog.dismiss();
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    JSONArray jsonArray = jsonObject.getJSONArray("success");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        jsonObject = jsonArray.getJSONObject(i);

                                        ProductData productData = new ProductData();
                                        productData.product_pk = jsonObject.getInt("product_pk");
                                        productData.product_name = jsonObject.getString("product_name");
                                        productData.product_description = jsonObject.getString("product_description");
                                        productData.product_specification = jsonObject.getString("product_specification");
                                        productData.product_price = jsonObject.getString("product_price");
                                        Log.e("price==", "" + productData.product_price);
                                        productData.product_discount = jsonObject.getString("product_discount");
                                        productData.product_final_price = jsonObject.getString("product_final_price");
                                        productData.product_actual_stock = jsonObject.getString("product_actual_stock");
                                        productData.product_image1 = jsonObject.getString("product_image1");

                                        data.add(productData);
                                        //Set Adapter...
                                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                                        productRecyclerView.setLayoutManager(gridLayoutManager);
                                        ProductListAdapter productListAdapter = new ProductListAdapter(getActivity(), data);
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
                });

                highprice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        highprice.setTextColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
                        lowprice.setTextColor(getActivity().getResources().getColor(R.color.black));
                        ztoa.setTextColor(getActivity().getResources().getColor(R.color.black));
                        atoz.setTextColor(getActivity().getResources().getColor(R.color.black));
                        final List<ProductData> data = new ArrayList<>();

                        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
                        pDialog.setTitleText("Loading...");
                        pDialog.setCancelable(true);
                        pDialog.show();

                        RequestParams requestParams = new RequestParams();
                        requestParams.put("category_pk", position);

                        asyncHttpClient.get(HignURL, requestParams, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                String s = new String(responseBody);
                                pDialog.dismiss();
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    JSONArray jsonArray = jsonObject.getJSONArray("success");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        jsonObject = jsonArray.getJSONObject(i);

                                        ProductData productData = new ProductData();
                                        productData.product_pk = jsonObject.getInt("product_pk");
                                        productData.product_name = jsonObject.getString("product_name");
                                        productData.product_description = jsonObject.getString("product_description");
                                        productData.product_specification = jsonObject.getString("product_specification");
                                        productData.product_price = jsonObject.getString("product_price");
                                        Log.e("price==", "" + productData.product_price);
                                        productData.product_discount = jsonObject.getString("product_discount");
                                        productData.product_final_price = jsonObject.getString("product_final_price");
                                        productData.product_actual_stock = jsonObject.getString("product_actual_stock");
                                        productData.product_image1 = jsonObject.getString("product_image1");

                                        data.add(productData);
                                        //Set Adapter...
                                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                                        productRecyclerView.setLayoutManager(gridLayoutManager);
                                        ProductListAdapter productListAdapter = new ProductListAdapter(getActivity(), data);
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
                });
                atoz.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        atoz.setTextColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
                        highprice.setTextColor(getActivity().getResources().getColor(R.color.black));
                        ztoa.setTextColor(getActivity().getResources().getColor(R.color.black));
                        lowprice.setTextColor(getActivity().getResources().getColor(R.color.black));
                        final List<ProductData> data = new ArrayList<>();

                        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
                        pDialog.setTitleText("Loading...");
                        pDialog.setCancelable(true);
                        pDialog.show();

                        RequestParams requestParams = new RequestParams();
                        requestParams.put("category_pk", position);

                        asyncHttpClient.get(AtoZURL, requestParams, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                String s = new String(responseBody);
                                pDialog.dismiss();
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    JSONArray jsonArray = jsonObject.getJSONArray("success");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        jsonObject = jsonArray.getJSONObject(i);

                                        ProductData productData = new ProductData();
                                        productData.product_pk = jsonObject.getInt("product_pk");
                                        productData.product_name = jsonObject.getString("product_name");
                                        productData.product_description = jsonObject.getString("product_description");
                                        productData.product_specification = jsonObject.getString("product_specification");
                                        productData.product_price = jsonObject.getString("product_price");
                                        Log.e("price==", "" + productData.product_price);
                                        productData.product_discount = jsonObject.getString("product_discount");
                                        productData.product_final_price = jsonObject.getString("product_final_price");
                                        productData.product_actual_stock = jsonObject.getString("product_actual_stock");
                                        productData.product_image1 = jsonObject.getString("product_image1");

                                        data.add(productData);
                                        //Set Adapter...
                                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                                        productRecyclerView.setLayoutManager(gridLayoutManager);
                                        ProductListAdapter productListAdapter = new ProductListAdapter(getActivity(), data);
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
                });
                ztoa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        ztoa.setTextColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
                        highprice.setTextColor(getActivity().getResources().getColor(R.color.black));
                        atoz.setTextColor(getActivity().getResources().getColor(R.color.black));
                        lowprice.setTextColor(getActivity().getResources().getColor(R.color.black));
                        final List<ProductData> data = new ArrayList<>();

                        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
                        pDialog.setTitleText("Loading...");
                        pDialog.setCancelable(true);
                        pDialog.show();

                        RequestParams requestParams = new RequestParams();
                        requestParams.put("category_pk", position);

                        asyncHttpClient.get(ZtoAURL, requestParams, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                String s = new String(responseBody);
                                pDialog.dismiss();
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    JSONArray jsonArray = jsonObject.getJSONArray("success");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        jsonObject = jsonArray.getJSONObject(i);

                                        ProductData productData = new ProductData();
                                        productData.product_pk = jsonObject.getInt("product_pk");
                                        productData.product_name = jsonObject.getString("product_name");
                                        productData.product_description = jsonObject.getString("product_description");
                                        productData.product_specification = jsonObject.getString("product_specification");
                                        productData.product_price = jsonObject.getString("product_price");
                                        Log.e("price==", "" + productData.product_price);
                                        productData.product_discount = jsonObject.getString("product_discount");
                                        productData.product_final_price = jsonObject.getString("product_final_price");
                                        productData.product_actual_stock = jsonObject.getString("product_actual_stock");
                                        productData.product_image1 = jsonObject.getString("product_image1");

                                        data.add(productData);
                                        //Set Adapter...
                                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                                        productRecyclerView.setLayoutManager(gridLayoutManager);
                                        ProductListAdapter productListAdapter = new ProductListAdapter(getActivity(), data);
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
                });
            }
        });
        return view;
    }

    public void getCart() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("cart", Context.MODE_PRIVATE);
        cart_id=sharedPreferences.getString("cart_id","");
        Log.e("cart",""+cart_id);

        MainPage.progressBar.setVisibility(View.VISIBLE);
        MainPage.cartCount.setVisibility(View.GONE);
        RequestParams requestParams = new RequestParams();
        requestParams.put("cart_pk", cart_id);

        asyncHttpClient.get(GetCartURL, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                MainPage.cartCount.setVisibility(View.VISIBLE);
                MainPage.progressBar.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("success");

                    if (jsonArray.length()==0){
                        MainPage.progressBar.setVisibility(View.GONE);
                        MainPage.cartCount.setText("0");
                        MainPage.cartCount.setVisibility(View.VISIBLE);
                    }

                    for (int i =0;i<jsonArray.length();i++){
                        jsonObject = jsonArray.getJSONObject(i);
                        String total = jsonObject.getString("Total");
                        Log.e("total",""+total);

                        if (total==null){
                            MainPage.progressBar.setVisibility(View.GONE);
                            MainPage.cartCount.setText("0");
                            MainPage.cartCount.setVisibility(View.VISIBLE);

                        }else {
                            MainPage.progressBar.setVisibility(View.GONE);
                            MainPage.cartCount.setVisibility(View.VISIBLE);
                            MainPage.cartCount.setText(total);

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getProductList() {

        final List<ProductData> data = new ArrayList<>();

        final SweetAlertDialog  pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading...");
        pDialog.setCancelable(true);
        pDialog.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("category_pk",position);

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
                        ProductListAdapter productListAdapter = new ProductListAdapter(getActivity(), data);
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
            getProductList();
            getCart();
        } else {
            Toast.makeText(getActivity(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }
    }

}