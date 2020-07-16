package com.bachatgatapp.Fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.bachatgatapp.Activity.MainPage;
import com.bachatgatapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class Complaints extends Fragment {


    View view;
    Button submit;
    EditText complaint;
    Spinner spinner;
    String cart_id;
    public static String complaintUrl="http://prabhagmaza.com/androidApp/Customer/ComplaintUser.php";
    String[] city_id_pk, city_name;
    String cityid,city_value;
    AsyncHttpClient asyncHttpClient=new AsyncHttpClient();


    public Complaints() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_complaints, container, false);
        submit=view.findViewById(R.id.submit);
        complaint=view.findViewById(R.id.complaint);
        spinner=view.findViewById(R.id.spinner);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("cart", Context.MODE_PRIVATE);
        cart_id=sharedPreferences.getString("cart_id","not");

        asyncHttpClient.get(complaintUrl, null,new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {

                String string = new String(responseBody);


                try {
                    JSONObject jsonObject = new JSONObject(string);

                    JSONArray jsonarray;
                    jsonarray = jsonObject.getJSONArray("success");
                    city_name = new String[jsonarray.length()];
                    city_id_pk = new String[jsonarray.length()];
                    for (int i = 0; i < jsonarray.length(); i++) {
                        jsonObject = jsonarray.getJSONObject(i);
                        city_name[i] = jsonObject.getString("product_unit_value");
                        city_id_pk[i] = jsonObject.getString("product_unit_pk");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item, city_name);


                spinnerAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                spinner.setAdapter(spinnerAdapter);

            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cityid = city_id_pk[i];
                city_value = String.valueOf(adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
                pDialog.setTitleText("Loading...");
                pDialog.setCancelable(true);
                pDialog.show();

                complaint.setText("");
                RequestParams requestParams = new RequestParams();
                requestParams.put("order_id_fk","");
                requestParams.put("Order_number","");
                requestParams.put("complaint_sender_id","");
                requestParams.put("complaint_against_id","");
                requestParams.put("message","");

                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                asyncHttpClient.post("", requestParams, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String s = new String(responseBody);
                        pDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            pDialog.dismiss();
                            if (jsonObject.getString("success").equalsIgnoreCase("1")){
                                pDialog.dismiss();
                                Toast.makeText(getActivity(), "Your Complaint submitted successfully", Toast.LENGTH_SHORT).show();
                            }else  if (jsonObject.getString("success").equalsIgnoreCase("0")){
                                pDialog.dismiss();
                                Toast.makeText(getActivity(), "Your Complaint Not submit", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        pDialog.dismiss();
                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        ((MainPage) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        MainPage.title.setText("Complaints");
        MainPage.cart.setVisibility(View.GONE);
        MainPage.cartCount.setVisibility(View.GONE);
        MainPage.logo.setVisibility(View.GONE);
        MainPage.title.setVisibility(View.VISIBLE);
        MainPage.search.setVisibility(View.GONE);
        MainPage.bottomnavigation.setVisibility(View.GONE);
    }
}
