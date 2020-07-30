package com.graminvikreta.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.graminvikreta.Activity.MainPage;
import com.graminvikreta.Model.DeliverStock;
import com.graminvikreta.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyBidingAdpter extends RecyclerView.Adapter<MyOrderholder> {

    Context context;
    List<DeliverStock> results;
    AlertDialog alertDialog1;
    AlertDialog.Builder dialogBuilder1;
    LayoutInflater inflater1;
    View dialogView1;



    public MyBidingAdpter(Context context, List<DeliverStock> results) {
        this.context=context;
        this.results=results;
    }

    @NonNull
    @Override
    public MyOrderholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bid_list, null);
        MyOrderholder MyOrdersViewHolder = new MyOrderholder(context, view);
        return MyOrdersViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrderholder holder, final int position) {

        setProductsData(holder, position);
        holder.date.setText(results.get(position).getOrder_title());

        holder.bid_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                RequestParams requestParams= new RequestParams();
                requestParams.put("vendor_id", MainPage.userId);
                requestParams.put("order_id",results.get(position).getId());

                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                asyncHttpClient.post("http://softmate.in/androidApp/Qsar/DeliveryBoy/getVendorBid.php", requestParams, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String s = new String(responseBody);
                        try {
                            JSONObject jsonObject= new JSONObject(s);
                            if(jsonObject.getString("status").equalsIgnoreCase("success")){

                                dialogBuilder1 = new AlertDialog.Builder(v.getContext());
                                inflater1 = LayoutInflater.from(v.getContext());
                                dialogView1 = inflater1.inflate(R.layout.bid_dailog, null);
                                dialogBuilder1.setView(dialogView1);
                                alertDialog1 = dialogBuilder1.create();
                                alertDialog1.show();

                                final EditText bid_edt = (EditText) dialogView1.findViewById(R.id.bid_amount);
                                final TextView submit = (TextView) dialogView1.findViewById(R.id.yes);
                                final TextView cancel = (TextView) dialogView1.findViewById(R.id.no);
                                bid_edt.setText(jsonObject.getString("bid_amount"));
                                bid_edt.setSelection(bid_edt.getText().toString().length());
                                submit.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String bid =bid_edt.getText().toString();
                                        Log.e("orsderid",results.get(position).getId());
                                        UpdateVendorBid( results.get(position).getId(), bid);
                                        alertDialog1.dismiss();
                                    }
                                });
                                cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alertDialog1.dismiss();
                                    }
                                });

                            }else {

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
            }
        });


    }

    private void setProductsData(MyOrderholder holder, int position) {
        // Log.e("deliverystock",""+results.get(position).getDates().size());
        if (results.get(position).getDates().size()==0){

        }else {
            GridLayoutManager gridLayoutManager;
            gridLayoutManager = new GridLayoutManager(context, 1);
            holder.orderedProductsRecyclerView.setLayoutManager(gridLayoutManager);
            BidProductListAdapter myOrdersAdapter = new BidProductListAdapter(context, results.get(position).getDates());
            holder.orderedProductsRecyclerView.setAdapter(myOrdersAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void UpdateVendorBid(String order_id, String bid_amt) {

       /* ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Status> call = apiService.UpdateVendorBid(order_id, MainPageVendor.userId, bid_amt);
        call.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                Log.e("succces", "" + response.body().getSuccess());
                if (response.body().getSuccess().equalsIgnoreCase("1")) {
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    ((MainPageVendor) context).loadFragment(new OrderHistory(), true);
                    ((MainPageVendor) context).removeCurrentFragmentAndMoveBack();
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                // Log error here since request failed
                Toast.makeText(context, " failed", Toast.LENGTH_SHORT).show();

            }
        });*/
    }
}
