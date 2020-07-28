package com.graminvikreta.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.graminvikreta.API.APIClient;
import com.graminvikreta.API.APInterface;
import com.graminvikreta.Activity.MainPage;
import com.graminvikreta.Fragment.ProductDetail;
import com.graminvikreta.Model.Orders;
import com.graminvikreta.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.graminvikreta.Fragment.MyOrders.continueShopping1;
import static com.graminvikreta.Fragment.MyOrders.emptyOrdersLayout;
import static com.graminvikreta.Fragment.MyOrders.myOrdersRecyclerView;
import static com.graminvikreta.Fragment.MyOrders.paymentprocess;
import static com.graminvikreta.Fragment.MyOrders.safepay;
import static com.graminvikreta.Fragment.MyOrders.totalgrand;
import static com.graminvikreta.Fragment.MyOrders.totlall;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.MyViewHolder> {

    private List<Orders> OderList;
    public Context context;
    public Float totalprice;
    public String finalproductprice, cart_id_pk, cart_id, grandt;
    public  static String DeleteOrder="http://graminvikreta.com/androidApp/Customer/Delete_cart.php";
    public AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView productName1,viewOrderDetails,amount;
        public EditText quantity;
        public TextView totalgrand,quy,grandtotal1;
        public ImageView productImage1,delete,minus,plus;

        public MyViewHolder(View view) {
            super(view);

            productName1 = (TextView) view.findViewById(R.id.productName1);
            viewOrderDetails = (TextView) view.findViewById(R.id.viewOrderDetails);
            amount = (TextView) view.findViewById(R.id.amount);
            quantity = (EditText) view.findViewById(R.id.quantity);
            quy = (TextView) view.findViewById(R.id.quy);
            productImage1 = (ImageView) view.findViewById(R.id.productImage1);
            delete = (ImageView) view.findViewById(R.id.delete);
            minus = (ImageView) view.findViewById(R.id.minus);
            plus = (ImageView) view.findViewById(R.id.plus);

        }
    }


    public OrdersAdapter(Context context, List<Orders> moviesList) {
        this.OderList = moviesList;
        this.context=context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_orders_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Orders orders = OderList.get(position);

            holder.productName1.setText(orders.getProduct_name());
            holder.amount.setText(MainPage.currency + " " + orders.getTotal_price());
            holder.quantity.setText(orders.getQty());
            try {
                Picasso.with(context)
                        .load("http://graminvikreta.com/graminvikreta/" + orders.getProduct_image1())
                        .placeholder(R.drawable.defaultimage)
                        .into(holder.productImage1);
            } catch (Exception e) {
                e.printStackTrace();
            }

        holder.viewOrderDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductDetail productDetail = new ProductDetail();
                Bundle bundle = new Bundle();
                bundle.putInt("position", orders.getProduct_fk());
                productDetail.setArguments(bundle);
                ((MainPage) context).loadFragment(productDetail, true);
            }
        });

            holder.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double minteger = 0.5;
                    minteger = minteger - 0.5;
                    String qty = String.valueOf(minteger);
                    Log.e("qty", "" + qty);
                    if (qty.equalsIgnoreCase("0") || minteger>50 || minteger<0 || holder.quantity.getText().toString().equalsIgnoreCase("0")) {
                        holder.quantity.setText("0.5");
                    } else {
                        holder.quantity.setText(qty);
                    }
                }
            });

            holder.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    double minteger = 0.5;
                    minteger = minteger + 0.5;
                    String qty = String.valueOf(minteger);
                    Log.e("qty", "" + qty);
                    if (qty.equalsIgnoreCase("0") || minteger>50 || minteger<0) {
                        holder.quantity.setText("0.5");
                    } else {
                        holder.quantity.setText(qty);
                    }
                }
            });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Orders item = OderList.get(position);
                int cart_pk = item.getCart_pk();
                int cart_product_pk = Integer.parseInt(item.getCart_product_pk());
                double cart_total_price = item.getTotal_price();
                Log.e("totalPrice",""+cart_total_price);
                double product_total_price = item.getProduct_final_price();

                removeQuestionListItem(cart_pk,cart_product_pk,cart_total_price,product_total_price);
                OderList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, OderList.size());
                OrdersAdapter.this.notifyDataSetChanged();

            }
        });

        holder.quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (holder.quantity.getText().toString().equalsIgnoreCase("0")){
                    holder.quantity.setText("0.5");
                }
                    try {
                        totalprice = (Float.valueOf(holder.quantity.getText().toString()) * Float.valueOf(String.valueOf(orders.getProduct_final_price())));
                        Log.e("totalprice", "" + totalprice);
                        String price = String.valueOf(totalprice);
                        holder.amount.setText(MainPage.currency+" "+price);

                        SharedPreferences sharedPreferences = context.getSharedPreferences("cart", Context.MODE_PRIVATE);
                        cart_id=sharedPreferences.getString("cart_id","");
                        Log.e("cart_id_order==",""+cart_id);
                        final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

                        RequestParams requestParams = new RequestParams();
                        requestParams.put("cart_fk",orders.getCart_pk());
                        requestParams.put("qnty",holder.quantity.getText().toString());
                        requestParams.put("cart_product_pk",orders.getCart_product_pk());
                        Log.e("cardidfk",orders.getCart_product_pk());
                        requestParams.put("total",totalprice);

                        asyncHttpClient.post("http://graminvikreta.com/androidApp/Customer/Update_cart.php", requestParams, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                String s = new String(responseBody);
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    if (jsonObject.getString("success").equalsIgnoreCase("1")){
                                       // Toast.makeText(context, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                        RequestParams requestParams = new RequestParams();
                                        requestParams.put("cart_fk",orders.getCart_pk());

                                        asyncHttpClient.get("http://graminvikreta.com/androidApp/Customer/GetGrandCartCount.php", requestParams, new AsyncHttpResponseHandler() {
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                String s = new String(responseBody);

                                                try {
                                                    JSONObject jsonObject = new JSONObject(s);
                                                    final JSONArray jsonArray = jsonObject.getJSONArray("success");

                                                    for (int i =0;i<jsonArray.length();i++){

                                                        jsonObject = jsonArray.getJSONObject(i);
                                                        grandt = jsonObject.getString("TOTAL");
                                                       // Toast.makeText(context,""+grandt,Toast.LENGTH_SHORT).show();
                                                    }

                                                    RequestParams requestParamst = new RequestParams();
                                                    requestParamst.put("cart_pk",orders.getCart_pk());
                                                    requestParamst.put("cart_total",grandt);
                                                   // Toast.makeText(context,""+grandt,Toast.LENGTH_SHORT).show();

                                                    asyncHttpClient.post("http://graminvikreta.com/androidApp/Customer/Update_grandCart_total.php", requestParamst, new AsyncHttpResponseHandler() {
                                                        @Override
                                                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                            String s = new String(responseBody);

                                                            try {
                                                                JSONObject jsonObject = new JSONObject(s);
                                                                if (jsonObject.getString("success").equalsIgnoreCase("1")){
                                                                   // Toast.makeText(context, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                                                    //((MainPage) context).loadFragment(new MyOrders(),true);
                                                                    OrdersAdapter.this.notifyDataSetChanged();

                                                                }
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }

                                                        }

                                                        @Override
                                                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                                        }
                                                    });

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }

                                            @Override
                                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                            }
                                        });
                                    }else if (jsonObject.getString("success").equalsIgnoreCase("0")){

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        });
    }

    private void removeQuestionListItem(final int cart_pk, int cart_product_pk, double cart_total_price, double product_total_price) {

        final ProgressDialog Dialog = new ProgressDialog(context);
        Dialog.setMessage("Please wait...");
        Dialog.setIndeterminate(true);
        Dialog.setCancelable(true);
        Dialog.setProgress(android.R.drawable.progress_indeterminate_horizontal);
        Dialog.show();

        APInterface api = APIClient.getClient().create(APInterface.class);
        Call<Orders>call = api.deleteAdminQuestion(cart_product_pk,cart_total_price,product_total_price, String.valueOf(cart_pk));
        call.enqueue(new Callback<Orders>() {
            @Override
            public void onResponse(Call<Orders> call, Response<Orders> response) {
                if (Dialog.isShowing()) {
                    Dialog.dismiss();
                }
                String result = response.body().getSuccess();
                if (result.equalsIgnoreCase("1")) {
                    Toast.makeText(context, "Product Removed", Toast.LENGTH_SHORT).show();
                   // ((MainPage) context).loadFragment(new MyOrders(),true);
                    OrdersAdapter.this.notifyDataSetChanged();

                    SharedPreferences sharedPreferences = context.getSharedPreferences("cart", Context.MODE_PRIVATE);
                    cart_id=sharedPreferences.getString("cart_id","");
                    Log.e("cart_id_order==",""+cart_id);
                    final AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

                    RequestParams requestParamsg = new RequestParams();
                    requestParamsg.put("cart_fk",cart_id);
                    Log.e("cart_fk",cart_id);

                    asyncHttpClient.get("http://graminvikreta.com/androidApp/Customer/GetGrandCartCount.php", requestParamsg, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            String s = new String(responseBody);

                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                JSONArray jsonArray = jsonObject.getJSONArray("success");

                                for (int i =0;i<jsonArray.length();i++){

                                    jsonObject = jsonArray.getJSONObject(i);
                                    String grandamount = jsonObject.getString("TOTAL");
                                    Log.e("grandamount",""+grandamount);
                                    if (grandamount.equalsIgnoreCase("null")){
                                        OrdersAdapter.this.notifyDataSetChanged();
                                        emptyOrdersLayout.setVisibility(View.VISIBLE);
                                        paymentprocess.setVisibility(View.GONE);
                                        totalgrand.setVisibility(View.GONE);
                                        totlall.setVisibility(View.GONE);
                                        myOrdersRecyclerView.setVisibility(View.GONE);
                                        continueShopping1.setVisibility(View.GONE);
                                        safepay.setVisibility(View.GONE);
                                        OrdersAdapter.this.notifyDataSetChanged();

                                    }else {
                                        totalgrand.setText(MainPage.currency+" "+grandamount);
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                        }
                    });

                } else {
                    Toast.makeText(context, "Product Not Found", Toast.LENGTH_SHORT).show();
                }
                Dialog.dismiss();
            }

            @Override
            public void onFailure(Call<Orders> call, Throwable t) {
                Dialog.dismiss();
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return OderList.size();
    }
}
