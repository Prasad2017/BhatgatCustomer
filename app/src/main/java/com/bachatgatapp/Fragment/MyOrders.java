package com.bachatgatapp.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.bachatgatapp.API.APIClient;
import com.bachatgatapp.API.APInterface;
import com.bachatgatapp.Activity.MainPage;
import com.bachatgatapp.Adapter.OrdersAdapter;
import com.bachatgatapp.Extra.Config;
import com.bachatgatapp.Extra.DetectConnection;
import com.bachatgatapp.Model.Orders;
import com.bachatgatapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyOrders extends Fragment {

    View view;
   // @BindView(R.id.myOrdersRecyclerView)
    public static RecyclerView myOrdersRecyclerView;
   // @BindView(R.id.emptyOrdersLayout)
   // LinearLayout emptyOrdersLayout;
   // @BindView(R.id.continueShopping)
   // Button continueShopping;
    AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
    public static String cart_id,grandamount;
    public static double total;
    private List<Orders> movieList = new ArrayList<>();
    public static OrdersAdapter mAdapter;
   // public static String ORDERSLISTURL="http://logic-fort.com/androidApp/Customer/GetCartList.php";
    public static String ORDERSLISTURL="http://logic-fort.com/androidApp/Customer/viewcart.php";
    public static Button paymentprocess, continueShopping1,continueShopping;
    public static TextView totalgrand;
    public static LinearLayout totlall,emptyOrdersLayout, safepay;

    public MyOrders() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_orders, container, false);
        ButterKnife.bind(this, view);
        paymentprocess = view.findViewById(R.id.paymentprocess);
        continueShopping1 = view.findViewById(R.id.continueShopping1);
        totalgrand = view.findViewById(R.id.totalgrand);
        totlall = view.findViewById(R.id.totlall);
        emptyOrdersLayout = view.findViewById(R.id.emptyOrdersLayout);
        myOrdersRecyclerView = view.findViewById(R.id.myOrdersRecyclerView);
        continueShopping = view.findViewById(R.id.continueShopping);
        safepay = view.findViewById(R.id.safepay);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("cart", Context.MODE_PRIVATE);
        cart_id=sharedPreferences.getString("cart_id","0");
        Log.e("cart_id_order==",""+cart_id);

        if (!MainPage.userId.equalsIgnoreCase("")) {
            if (DetectConnection.checkInternetConnection(getActivity())) {
                // getMyOrders();
                getOrders();
                getMyOrdersCount();
            }else {
                Toast.makeText(getActivity(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
        }

        mAdapter = new OrdersAdapter(getActivity(),movieList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        myOrdersRecyclerView.setLayoutManager(mLayoutManager);
       // myOrdersRecyclerView.setAdapter(mAdapter);
        myOrdersRecyclerView.setHasFixedSize(true);
        myOrdersRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        myOrdersRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter.notifyDataSetChanged();

        continueShopping1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.moveTo(getActivity(), MainPage.class);
                getActivity().finish();
            }
        });

        continueShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Config.moveTo(getActivity(), MainPage.class);
                getActivity().finish();
            }
        });

        paymentprocess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Float totall = Float.valueOf(total);

                Float f = Float.valueOf(grandamount);

                if (f > 10) {
                    BuyNow buyNow = new BuyNow();
                    Bundle bundle = new Bundle();
                    bundle.putString("total", String.valueOf(f));
                    Log.e("total==",""+f);
                    buyNow.setArguments(bundle);
                    ((MainPage) getActivity()).loadFragment(buyNow, true);
                } else {
                    Toast toast = Toast.makeText(getActivity(), "Please add more product in cart.\n because grand total should be greater than Rs. 10", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                    toast.show();
                }
            }
        });
        return view;
    }

    private void getOrders() {

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
                orders.setCart_pk(Integer.parseInt(cart_id));

                APInterface apiService = APIClient.getClient().create(APInterface.class);
                Call<Orders> call = apiService.sendAdUserId(String.valueOf(orders.getCart_pk()));
                call.enqueue(new Callback<Orders>() {
                    @Override
                    public void onResponse(Call<Orders> call, Response<Orders> response) {
                        //int statusCode = response.code();
                        pDialog.dismiss();
                        String result = response.body().getSuccess();
                        if (result.equalsIgnoreCase("true")) {

                            movieList = response.body().getUserQuestionsList();
                            mAdapter = new OrdersAdapter(getActivity(), movieList);
                            myOrdersRecyclerView.setAdapter(mAdapter);
                            mAdapter.notifyDataSetChanged();
                            safepay.setVisibility(View.GONE);
                            totlall.setVisibility(View.VISIBLE);
                            paymentprocess.setVisibility(View.VISIBLE);

                        } else {
                            pDialog.dismiss();
                            emptyOrdersLayout.setVisibility(View.VISIBLE);
                            totlall.setVisibility(View.GONE);
                            paymentprocess.setVisibility(View.GONE);
                            continueShopping1.setVisibility(View.GONE);
                            safepay.setVisibility(View.GONE);
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

            }catch(Exception e){
                e.printStackTrace();
            }
    }

    private void getMyOrdersCount() {

        if (DetectConnection.checkInternetConnection(getActivity())) {

            final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();

            RequestParams requestParamsg = new RequestParams();
            requestParamsg.put("cart_fk", cart_id);
            Log.e("cart_fk", cart_id);

            asyncHttpClient.get("http://logic-fort.com/androidApp/Customer/GetGrandCartCount.php", requestParamsg, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String s = new String(responseBody);
                    pDialog.dismiss();

                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        JSONArray jsonArray = jsonObject.getJSONArray("success");
                        pDialog.dismiss();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            pDialog.dismiss();
                            jsonObject = jsonArray.getJSONObject(i);
                            grandamount = jsonObject.getString("TOTAL");
                            Log.e("grandamount", "" + grandamount);
                            if (grandamount.equalsIgnoreCase("null")) {
                                pDialog.dismiss();
                                emptyOrdersLayout.setVisibility(View.VISIBLE);
                                paymentprocess.setVisibility(View.GONE);
                                totalgrand.setVisibility(View.GONE);
                                totlall.setVisibility(View.GONE);
                                myOrdersRecyclerView.setVisibility(View.GONE);
                                continueShopping1.setVisibility(View.GONE);
                                safepay.setVisibility(View.GONE);
                            /*Intent intent=new Intent(getActivity(),MainPage.class);
                            startActivity(intent);*/
                            } else {
                                totalgrand.setText(MainPage.currency + " " + grandamount);
                                pDialog.dismiss();
                            }

                            Log.e("total==", "" + grandamount);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    pDialog.dismiss();
                }
            });
        }else {
                Toast.makeText(getActivity(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
            }
    }

   /* @OnClick({R.id.continueShopping})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.continueShopping:
                Config.moveTo(getActivity(), MainPage.class);
                getActivity().finish();
                break;
        }
    }*/

    private void getMyOrders() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("cart", Context.MODE_PRIVATE);
        cart_id=sharedPreferences.getString("cart_id","");
        Log.e("cart_id_order==",""+cart_id);

        if (DetectConnection.checkInternetConnection(getActivity())){
        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("cart_pk",cart_id);

        asyncHttpClient.post(ORDERSLISTURL, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String s = new String(responseBody);

                pDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("success");
                    pDialog.dismiss();

                    if (jsonArray.length()==0){

                        emptyOrdersLayout.setVisibility(View.VISIBLE);
                        paymentprocess.setVisibility(View.GONE);
                        totalgrand.setVisibility(View.GONE);
                        totlall.setVisibility(View.GONE);
                        safepay.setVisibility(View.GONE);
                        Intent intent=new Intent(getActivity(),MainPage.class);
                        startActivity(intent);
                    }

                    for (int i=0;i<jsonArray.length();i++){

                        jsonObject = jsonArray.getJSONObject(i);

                        String product_name = jsonObject.getString("product_name");
                        int cart_pk = jsonObject.getInt("cart_pk");
                        total = jsonObject.getDouble("total");
                        String cart_product_pk = jsonObject.getString("cart_product_pk");
                        int product_fk = jsonObject.getInt("product_fk");
                        String qty = jsonObject.getString("qty");
                        double price = jsonObject.getDouble("price");
                        double total_price = jsonObject.getDouble("total_price");
                        String product_image1 = jsonObject.getString("product_image1");
                        String product_discount = jsonObject.getString("product_discount");
                        double product_final_price = jsonObject.getDouble("product_final_price");
                        Log.e("finalPirce", "" + product_final_price);
                        String product_description = jsonObject.getString("product_description");

                       // totalgrand.setText(MainPage.currency + " " + total);

                            Orders orders = new Orders();
                            orders.setProduct_name(product_name);
                            orders.setCart_pk(cart_pk);
                            orders.setTotal(total);
                            orders.setCart_product_pk(cart_product_pk);
                            orders.setProduct_fk(product_fk);
                            orders.setQty(qty);
                            orders.setTotal_price(total_price);
                            orders.setProduct_image1(product_image1);
                            orders.setPrice(price);
                            orders.setProduct_discount(product_discount);
                            orders.setProduct_final_price(product_final_price);
                            orders.setProduct_description(product_description);

                            movieList.add(orders);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                pDialog.dismiss();
            }
        });
        }else {
            Toast.makeText(getActivity(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }

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
            getOrders();
            getMyOrdersCount();
        }else {
            Toast.makeText(getActivity(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }
    }
}
