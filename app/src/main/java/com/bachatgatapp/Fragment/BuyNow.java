package com.bachatgatapp.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.hashexclude.instamojo_sdk_android.activities.PaymentActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.bachatgatapp.Activity.MainPage;
import com.bachatgatapp.Activity.OrderConfirmed;
import com.bachatgatapp.Adapter.SizeListAdapter;
import com.bachatgatapp.Extra.Config;
import com.bachatgatapp.Model.GeocodingLocation;
import com.bachatgatapp.R;
import com.xiaofeng.flowlayoutmanager.FlowLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class BuyNow extends Fragment {


    View view;
    @BindView(R.id.sizeRecyclerView)
    RecyclerView sizeRecyclerView;
    @BindView(R.id.addNewAddressLayout)
    LinearLayout addNewAddressLayout;
    @BindView(R.id.addressCheckBox)
    CheckBox addressCheckBox;
    @BindView(R.id.addNewAddress)
    TextView addNewAddress;
    @BindView(R.id.fillAddress)
    TextView fillAddress;
    @BindView(R.id.paymentMethodsGroup)
    RadioGroup paymentMethodsGroup;
    @BindView(R.id.makePayment)
    Button makePayment;
    @BindView(R.id.apply)
    Button apply;
    String paymentMethod;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.choosePaymentLayout)
    LinearLayout choosePaymentLayout;
    @BindViews({R.id.fullNameEdt, R.id.mobEditText, R.id.cityEditText, R.id.areaEditText, R.id.buildingEditText, R.id.pincodeEditText, R.id.stateEditText, R.id.landmarkEditText, R.id.promocode})
    List<EditText> editTexts;
    public static String address, mobileNo,userEmail,profilePinCode,userName;
    Intent intent;
    String total,cart_id,otp;
    String coupon_code_name;
    TextView warning_txt, grandtotala, offertxtamoount, gtotala;
    CardView offercard;
    ArrayList<String> sizeList = new ArrayList<>();
    AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
    public static String profileurl = "http://graminvikreta.com/androidApp/Customer/getprofile.php";
    public static String finalorder = "http://graminvikreta.com/androidApp/Customer/Final_order.php";
    public static String offerurl = "http://graminvikreta.com/androidApp/Customer/GetCoupon.php";
    public static String messageurl = "http://graminvikreta.com/androidApp/Customer/Customer_Delivery/send_msg_on_dispatch.php";
    //Your authentication key
    String authkey = "217749A4rjW4ii5b0cf071";
    //Sender ID,While using route4 sender id should be 6 characters long.
    String senderId = "MSOFTS";
    //define route
    String route="4";
    URLConnection myURLConnection=null;
    URL myURL=null;
    BufferedReader reader=null;
    //Send SMS API
    String mainUrl="http://api.msg91.com/api/sendhttp.php?";
    public BuyNow() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        int layout = R.layout.fragment_buy_now;
        view = inflater.inflate(layout, container, false);
        ButterKnife.bind(this, view);
        MainPage.title.setText("Choose Payment Method");
        MainPage.cart.setVisibility(View.GONE);
        MainPage.cartCount.setVisibility(View.GONE);

        warning_txt = view.findViewById(R.id.warning_txt);
        grandtotala = view.findViewById(R.id.grandtotala);
        offertxtamoount = view.findViewById(R.id.offertxtamoount);
        offercard = view.findViewById(R.id.offercard);
        gtotala = view.findViewById(R.id.gtotala);
        Bundle bundle = getArguments();
        total = bundle.getString("total");

        grandtotala.setText("Total Amount = "+MainPage.currency +total);
        editTexts.get(8).setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("cart",Context.MODE_PRIVATE);
        cart_id=sharedPreferences.getString("cart_id","");
        Log.e("cart",""+cart_id);

        getUserProfileData();
        setoffer();

        addressCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    addNewAddressLayout.setVisibility(View.GONE);
                    addNewAddress.setText("Add New Address");
                }
            }
        });
        choosePaymentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(view);

            }
        });

        return view;
    }

    private void setoffer() {

        asyncHttpClient.get(offerurl, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                String s = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("getdatas");
                    for (int i = 0;i<jsonArray.length();i++) {

                        jsonObject = jsonArray.getJSONObject(i);
                        coupon_code_name = jsonObject.getString("coupon_code_name");
                    }
                        if (coupon_code_name.length()>0) {
                            String[] sizeArray = coupon_code_name.split(",");
                            sizeList = new ArrayList<>(Arrays.asList(sizeArray));
                            Log.e("sizeList", coupon_code_name + "");
                            setSizeListData();
                        } else {
                            offercard.setVisibility(View.GONE);
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

    private void setSizeListData() {
        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        flowLayoutManager.setAutoMeasureEnabled(true);
        sizeRecyclerView.setLayoutManager(flowLayoutManager);
        SizeListAdapter topListAdapter = new SizeListAdapter(getActivity(), sizeList);
        sizeRecyclerView.setAdapter(topListAdapter);
    }

    @OnClick({R.id.addNewAddress, R.id.makePayment, R.id.fillAddress, R.id.apply})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addNewAddress:
                addNewAddressLayout.setVisibility(View.VISIBLE);
                addressCheckBox.setChecked(false);
                addNewAddress.setText("Use This Address");
                break;
            case R.id.makePayment:
                if (!addressCheckBox.isChecked()) {
                    if (addNewAddressLayout.getVisibility() == View.VISIBLE) {
                        if (validate(editTexts.get(0))
                                && validate(editTexts.get(1))
                                && validate(editTexts.get(2))
                                && validate(editTexts.get(3))
                                && validate(editTexts.get(4))
                                && validatePinCode(editTexts.get(5))
                                && validate(editTexts.get(6))) {
                            String s = "";
                            if (editTexts.get(6).getText().toString().trim().length() > 0) {
                                s = ", " + editTexts.get(6).getText().toString().trim();
                            }
                            address = editTexts.get(4).getText().toString().trim()
                                    + s
                                    + ", " + editTexts.get(3).getText().toString().trim()
                                    + ", " + editTexts.get(2).getText().toString().trim()
                                    + ", " + editTexts.get(6).getText().toString().trim()
                                    + ", " + editTexts.get(5).getText().toString().trim()
                                    + "\n" + editTexts.get(1).getText().toString().trim();

                            /*editTexts.get(0).getText().toString().trim()
                                    + ", "
                                    + */
                            mobileNo = editTexts.get(1).getText().toString().trim();
                            userName = editTexts.get(0).getText().toString().trim();
                            profilePinCode = editTexts.get(5).getText().toString().trim();
                            Log.e("address",""+address);

                            GeocodingLocation locationAddress = new GeocodingLocation();
                            locationAddress.getAddressFromLocation(address,
                                    getActivity(), new GeocoderHandler());
                            moveNext();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please Enter your address", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    moveNext();
                    }

                break;
            case R.id.fillAddress:
                ((MainPage) getActivity()).loadFragment(new MyProfile(), true);
                break;
            case R.id.apply:

                if(editTexts.get(8).getText().toString().isEmpty()) {
                    warning_txt.setVisibility(View.VISIBLE);
                    warning_txt.setText("Coupon Code Not Applied !");
                    warning_txt.setTextColor(getActivity().getResources().getColor(R.color.light_red));
                }else {
                    warning_txt.setVisibility(View.VISIBLE);
                    warning_txt.setTextColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
                    warning_txt.setText("Coupon Code Applied !");
                    float offer = Float.parseFloat(total) / 2;
                    Log.e("offer",""+offer);
                    offertxtamoount.setVisibility(View.VISIBLE);
                    offertxtamoount.setText("Total Discount = "+MainPage.currency+offer);
                    gtotala.setText("Total Amount = "+MainPage.currency+offer);
                    gtotala.setTextColor(getActivity().getResources().getColor(R.color.colorPrimary));
                }

                break;
        }

    }

    private void moveNext() {
        switch (paymentMethodsGroup.getCheckedRadioButtonId()) {

            case R.id.razorPay:
                paymentMethod = "hdfc";

                intent = new Intent(getActivity(), PaymentActivity.class);
                intent.putExtra("id",MainPage.userId);
                intent.putExtra("cart_pk",cart_id);
                intent.putExtra("user",userName);
                intent.putExtra("paymnet_amt",total);
                intent.putExtra("contact",mobileNo);
                intent.putExtra("shipping_address",address);
                intent.putExtra("billing_address",address);
                intent.putExtra("postcode",profilePinCode);
                intent.putExtra("shipping_lat",GeocodingLocation.latitude);
                intent.putExtra("shipping_lang",GeocodingLocation.longitude);
                startActivity(intent);

                break;
            case R.id.cod:
                paymentMethod = "cod";

                RequestParams requestParams = new RequestParams();
                requestParams.put("firstname",userName);
                requestParams.put("billing_address",address);
                requestParams.put("shipping_address",address);
                requestParams.put("phone",mobileNo);
                requestParams.put("user_fk",MainPage.userId);
                requestParams.put("cart_pk",cart_id);
                requestParams.put("postcode",profilePinCode);
                requestParams.put("grand_amount",total);
                requestParams.put("shipping_lat",GeocodingLocation.latitude);
                requestParams.put("shipping_lang",GeocodingLocation.longitude);


                asyncHttpClient.post(finalorder, requestParams, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        String s = new String(responseBody);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getString("success").equalsIgnoreCase("1")){
                                Toast.makeText(getActivity(), ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                otp =jsonObject.getString("otp");
                                Log.e("otp",""+otp);

                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("cart", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.remove("cart_id");
                                editor.clear();
                                editor.apply();
                                editor.commit();

                                //getMessage();
                                String message = "Your order has been placed successfully. \n Your OTP is : "+otp+" \n Thank you.\n Customer Team.";
                                String encoded_message= URLEncoder.encode(message);

                                if (android.os.Build.VERSION.SDK_INT > 9) {
                                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                    StrictMode.setThreadPolicy(policy);
                                }
                                //Prepare parameter string
                                StringBuilder sbPostData= new StringBuilder(mainUrl);
                                sbPostData.append("authkey="+authkey);
                                sbPostData.append("&mobiles="+"91"+mobileNo);
                                sbPostData.append("&message="+encoded_message);
                                sbPostData.append("&route="+route);
                                sbPostData.append("&sender="+senderId);

//final string
                                mainUrl = sbPostData.toString();
                                try
                                {
                                    //prepare connection
                                    myURL = new URL(mainUrl);
                                    myURLConnection = myURL.openConnection();
                                    myURLConnection.connect();
                                    reader= new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
                                    //reading response
                                    String response;
                                    while ((response = reader.readLine()) != null)
                                        //print response
                                        Log.e("RESPONSE", ""+response);
                                    //finally close connection
                                    reader.close();
                                }
                                catch (IOException e)
                                {
                                    e.printStackTrace();
                                }
                                Intent intent = new Intent(getActivity(), OrderConfirmed.class);
                                startActivity(intent);
                                getActivity().finishAffinity();
                            }else if (jsonObject.getString("success").equalsIgnoreCase("0")){
                                Toast.makeText(getActivity(), ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(getActivity(), "error.....", Toast.LENGTH_SHORT).show();
                    }
                });

                break;

            default:
                paymentMethod = "";
                Config.showCustomAlertDialog(getActivity(),
                        "Payment Method",
                        "Select your payment method to make payment",
                        SweetAlertDialog.NORMAL_TYPE);
                break;


        }

        Log.d("paymentMethod", paymentMethod);
    }

    private void getMessage() {

        String message = "Your order has been placed successfully. \n Your OTP is"+otp+" \n Thank you.\n Customer Team.";

        RequestParams requestParams1 = new RequestParams();
        requestParams1.put("mobiles",mobileNo);
        requestParams1.put("message",message);
        asyncHttpClient.get(messageurl, requestParams1, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s1= new String(responseBody);
                try {
                    JSONObject jsonObject1 = new JSONObject(s1);
                    if (jsonObject1.getString("success").equals(1)){
                        Toast.makeText(getActivity(), "Message Send", Toast.LENGTH_SHORT).show();
                        /* Intent intent = new Intent(getActivity(), OrderConfirmed.class);
                        startActivity(intent);
                        getActivity().finishAffinity(); */
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void hideKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainPage.cart.setVisibility(View.VISIBLE);
        MainPage.cartCount.setVisibility(View.VISIBLE);
    }
    private void getUserProfileData() {
        progressBar.setVisibility(View.VISIBLE);
        makePayment.setClickable(false);

        RequestParams requestParams = new RequestParams();
        requestParams.put("id",MainPage.userId);

        asyncHttpClient.post(profileurl, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                makePayment.setClickable(true);
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(s);

                    if (jsonObject.getString("status").equals("success")){

                        userName = jsonObject.getString("full_name");
                        mobileNo = jsonObject.getString("mobileno");
                        String state = jsonObject.getString("state_name");
                        String city = jsonObject.getString("city_name");
                        profilePinCode = jsonObject.getString("pincode");
                        String landmark = jsonObject.getString("landmark");
                        String building_name = jsonObject.getString("building_name");
                        String street = jsonObject.getString("street");

                        if (userName.equalsIgnoreCase("") || building_name.equalsIgnoreCase("")
                                || street.equalsIgnoreCase("") || landmark.equalsIgnoreCase("null") ||
                                city.equalsIgnoreCase("null")||state.equalsIgnoreCase("null")||profilePinCode.equalsIgnoreCase("null")){

                            addNewAddressLayout.setVisibility(View.VISIBLE);
                            addressCheckBox.setChecked(false);
                            addNewAddress.setText("Use This Address");
                        }else {
                            address = building_name + ", " +
                                    street + ", " +
                                    landmark + ", " +
                                    city + ", " +
                                    state + ", " +
                                    profilePinCode;
                            Log.e("address==", "" + address);
                            addressCheckBox.setText(address);
                            //mobileNo = mobileno;
                            // userName + ", " +
                            GeocodingLocation locationAddress = new GeocodingLocation();
                            locationAddress.getAddressFromLocation(address,
                                    getActivity(), new GeocoderHandler());

                        }
                    }else if (jsonObject.getString("status").equals("Fail")){
                        Toast.makeText(getActivity(), "Something went wrong..", Toast.LENGTH_SHORT).show();
                        makePayment.setClickable(true);
                        progressBar.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getActivity(), "Error ", Toast.LENGTH_SHORT).show();
                makePayment.setClickable(true);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainPage) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        MainPage.title.setText("Buy Now");
        MainPage.cart.setVisibility(View.GONE);
        MainPage.cartCount.setVisibility(View.GONE);
        MainPage.logo.setVisibility(View.GONE);
        MainPage.title.setVisibility(View.VISIBLE);
        MainPage.search.setVisibility(View.GONE);
        MainPage.bottomnavigation.setVisibility(View.GONE);
    }

    private boolean validate(EditText editText) {
        if (editText.getText().toString().trim().length() > 0) {
            return true;
        }
        editText.setError("Please Fill This");
        editText.requestFocus();
        return false;
    }

    private boolean validatePinCode(EditText editText) {
        if (editText.getText().toString().trim().length() > 0) {
            return true;
        }
                editText.setError("Not available");
                editText.requestFocus();
                return false;
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
        }
    }
}
