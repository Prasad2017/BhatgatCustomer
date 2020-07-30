package com.graminvikreta.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.graminvikreta.API.Api;
import com.graminvikreta.Activity.MainPage;
import com.graminvikreta.Extra.Config;
import com.graminvikreta.Model.AddToWishlistResponse;
import com.graminvikreta.Model.Product;
import com.graminvikreta.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class ProductDetail  extends Fragment {

    View view;
    int position;
    @BindView(R.id.dotsRecyclerView)
    RecyclerView dotsRecyclerView;
   // public static DotsAdapter dotsAdapter;
    Activity activity;
    ArrayList<String> sliderImages = new ArrayList<>();
    @BindViews({R.id.productName, R.id.price, R.id.actualPrice, R.id.discountPercentage, R.id.quantity, R.id.status})
    List<TextView> textViews;
    public static List<Product> productList = new ArrayList<>();
    ArrayList<String> sizeList = new ArrayList<>();
    ArrayList<String> colorList = new ArrayList<>();
    @BindView(R.id.productDescWebView)
    WebView productDescWebView;
    TextView addToWishList;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.progressBar1)
    ProgressBar progressBar1;
    public static Button addToCart;
    public static String productQuantity;
    @BindView(R.id.noImageAdded)
    ImageView noImageAdded;
    public static String ProductdataURL="http://graminvikreta.com/androidApp/Customer/Productdata.php";
    public static String GetCartURL="http://graminvikreta.com/androidApp/Customer/cartcount.php";
    public static String ProductURL="http://graminvikreta.com/androidApp/Customer/add_to_cart.php";
    public static String ImagesUrl="http://graminvikreta.com/androidApp/Customer/getProductImages.php";
    public static String city="http://graminvikreta.com/androidApp/Customer/product_quantity.php";
    String[] city_id_pk, city_name;
    String cityid,city_value;
    AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
    String cart_id,total,lastmain_total;
    EditText quantity,quantitygrams;
    TextView actulpricedata;
    Spinner spinner;
    double minteger = 1;
    ImageView minus,plus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        ButterKnife.bind(this, view);
        activity = (Activity) view.getContext();
        Bundle bundle = getArguments();
        position = bundle.getInt("position");
        addToCart = (Button) view.findViewById(R.id.addToCart);
        addToWishList = (TextView) view.findViewById(R.id.addToWishList);
        //actulpricedata = (TextView) view.findViewById(R.id.actulpricedata);
        quantity = (EditText) view.findViewById(R.id.quantitypro);
        quantitygrams = (EditText) view.findViewById(R.id.quantitygrams);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        minus = (ImageView) view.findViewById(R.id.minus);
        plus = (ImageView) view.findViewById(R.id.plus);


        getCart();
        getProductDetails();
        checkWishList();


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("cart", Context.MODE_PRIVATE);
        cart_id=sharedPreferences.getString("cart_id","not");

        RequestParams requestParams = new RequestParams();
        requestParams.put("product_id",position);

        asyncHttpClient.get(city, requestParams,new AsyncHttpResponseHandler() {
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

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minteger = minteger - 1;
                String qty = String.valueOf(minteger);
                Log.e("qty", "" + qty);
                if (qty.equalsIgnoreCase("0") || minteger>50 || minteger<0) {
                    quantity.setText("1");
                } else {
                    quantity.setText(qty);
                }
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minteger = minteger + 1;
                String qty = String.valueOf(minteger);
                Log.e("qty", "" + qty);
                if (qty.equalsIgnoreCase("0") || minteger>50 || minteger<0) {
                    quantity.setText("1");
                } else {
                    quantity.setText(qty);
                }
            }
        });

        return view;
    }


    @OnClick({R.id.addToWishListLayout, R.id.addToWishList, R.id.addToCart, R.id.bidding})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addToWishList:
            case R.id.addToWishListLayout:
                if (!MainPage.userId.equalsIgnoreCase("")) {
                    addToWishList();
                } else {
                    Toast.makeText(getActivity(), "Error in getting user_id..", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.addToCart:
                if (!MainPage.userId.equalsIgnoreCase("")) {
                    if (addToCart.getText().toString().trim().equalsIgnoreCase("Add To Cart")) {
                        if (quantity.getText().toString().isEmpty() || Float.valueOf(quantity.getText().toString())>50){
                            Toast.makeText(getActivity(), "Enter quantity less than 50 or greater than 0", Toast.LENGTH_SHORT).show();
                        }else {
                            addCart();
                            // addToCart();
                        }
                    } else if (addToCart.getText().toString().trim().equalsIgnoreCase("0")) {
                        Config.showCustomAlertDialog(getActivity(),
                                "Out Of Stock",
                                "This Product is out of stock.",
                                SweetAlertDialog.ERROR_TYPE);
                    } else {
                        ((MainPage) getActivity()).loadFragment(new MyCartList(), true);
                    }
                }else {
                    Toast.makeText(getActivity(), "Error in getting user_id", Toast.LENGTH_SHORT).show();
                }

                break;
        }

    }

    private void addToWishList() {

        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
        Api.getClient().addToWishList(String.valueOf(position),
                MainPage.userId,
                new Callback<AddToWishlistResponse>() {
                    @Override
                    public void success(AddToWishlistResponse addToWishlistResponse, Response response) {
                        pDialog.dismiss();
                        Log.d("addToWishListResponse", addToWishlistResponse.getSuccess() + "");
                        if (addToWishlistResponse.getSuccess().equalsIgnoreCase("true")) {
                            Toast.makeText(getActivity(),""+addToWishlistResponse.getMessage(),Toast.LENGTH_SHORT).show();
                            checkWishList();
                        } else {
                            Toast.makeText(activity, "Error....", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        pDialog.dismiss();
                        Log.e("error", error.toString());
                    }
                });
    }

    private void checkWishList() {

        progressBar.setVisibility(View.VISIBLE);
        addToWishList.setVisibility(View.GONE);
        Api.getClient().checkWishList(String.valueOf(position),
                MainPage.userId,
                new Callback<AddToWishlistResponse>() {
                    @Override
                    public void success(AddToWishlistResponse addToWishlistResponse, Response response) {

                        progressBar.setVisibility(View.GONE);
                        addToWishList.setVisibility(View.VISIBLE);
                        Log.d("addToWishListResponse", addToWishlistResponse.getSuccess() + "");
                        if (addToWishlistResponse.getSuccess().equalsIgnoreCase("true")) {
                           // Toast.makeText(getActivity(),"Add to wishlist",Toast.LENGTH_SHORT).show();
                            addToWishList.setCompoundDrawablesWithIntrinsicBounds(R.drawable.favorited_icon, 0, 0, 0);
                        } else
                           // Toast.makeText(getActivity(),"Remove from wishlist",Toast.LENGTH_SHORT).show();
                            addToWishList.setCompoundDrawablesWithIntrinsicBounds(R.drawable.unfavorite_icon, 0, 0, 0);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        progressBar.setVisibility(View.GONE);
                        addToWishList.setVisibility(View.VISIBLE);
                        Log.e("error", error.toString());
                    }
                });
    }


    private  void addCart() {

        if (quantity.getText().toString().isEmpty() || quantity.getText().toString().equalsIgnoreCase("0")) {
            Toast.makeText(activity, "Please Select Quantity", Toast.LENGTH_SHORT).show();
            quantity.setError("please fill this");
            quantity.requestFocus();
        } else

        {
            String qtyy = quantity.getText().toString();
            Float price = Float.valueOf(total);
            Float qntity = Float.valueOf(quantity.getText().toString());
            Float main_total = price * qntity;
            lastmain_total = String.valueOf(main_total);

            final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();
            Api.getClient().addToCart(String.valueOf(position),
                    MainPage.userId,
                    qtyy,
                    cart_id,
                    total,
                    lastmain_total,
                    new Callback<AddToWishlistResponse>() {
                        @Override
                        public void success(AddToWishlistResponse addToWishlistResponse, Response response) {
                            pDialog.dismiss();

                            Log.d("addToCartResponse", addToWishlistResponse.getSuccess() + "");
                            if (addToWishlistResponse.getSuccess().equalsIgnoreCase("true")) {
                                addToCart.setText("Go to Cart");
                                cart_id = addToWishlistResponse.getCart_pk();
                                Log.e("card_id_details",""+cart_id);
                                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("cart",Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("cart_id",cart_id);
                                editor.apply();
                                editor.commit();
                                getCart();
                                Toast.makeText(activity, ""+addToWishlistResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(activity, ""+addToWishlistResponse.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void failure(RetrofitError error) {
                            pDialog.dismiss();

                            Log.e("error", error.toString());
                        }
                    });
        }
    }
    private void addToCart() {

        if (quantity.getText().toString().isEmpty() || quantity.getText().toString().equalsIgnoreCase("0")){
            Toast.makeText(activity, "Please Select Quantity", Toast.LENGTH_SHORT).show();
            quantity.setError("please fill this");
            quantity.requestFocus();
        }else {
            final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();

            Float price = Float.valueOf(total);
            Float qntity = Float.valueOf(quantity.getText().toString());
            Float main_total = price * qntity;
            lastmain_total = String.valueOf(main_total);

            RequestParams requestParams = new RequestParams();
            requestParams.put("productid_fk", String.valueOf(position));
            requestParams.put("user_fk", MainPage.userId);
            requestParams.put("cart_pk", cart_id);
            requestParams.put("qnty", quantity.getText().toString());
            requestParams.put("total", total);
            requestParams.put("Final_total", lastmain_total);
            requestParams.put("cart_total", lastmain_total);

            asyncHttpClient.post(ProductURL, requestParams, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String s = new String(responseBody);
                    pDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.getString("success").equalsIgnoreCase("1")) {
                            cart_id = jsonObject.getString("cart_pk");
                            //Common.saveUserData(getActivity(), "cart_id", cart_id);
                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("cart",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("cart_id",cart_id);
                            editor.apply();
                            editor.commit();

                            Log.e("cart_id==details", "" + cart_id);
                            addToCart.setText("Go to Cart");
                            getCart();
                            Toast.makeText(activity, "Product Add to cart", Toast.LENGTH_SHORT).show();

                        } else if (jsonObject.getString("success").equalsIgnoreCase("0")) {
                            pDialog.dismiss();
                            Toast.makeText(activity, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        } else if (jsonObject.getString("success").equalsIgnoreCase("6")) {
                            pDialog.dismiss();
                            Toast.makeText(activity, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

    }

        public void getCart() {

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("cart",Context.MODE_PRIVATE);
            cart_id=sharedPreferences.getString("cart_id","");
            Log.e("cart",""+cart_id);

            MainPage.progressBar.setVisibility(View.VISIBLE);
            MainPage.cartCount.setVisibility(View.GONE);
            RequestParams requestParams = new RequestParams();
            requestParams.put("cart_pk",cart_id);

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

                }
            });
        }

    private void getProductDetails() {
        progressBar1.setVisibility(View.VISIBLE);
        addToCart.setVisibility(View.GONE);
        Api.getClient().getProductDetails(String.valueOf(position),
                new Callback<Product>() {
                    @Override
                    public void success(Product product, Response response) {

                        progressBar1.setVisibility(View.GONE);
                        addToCart.setVisibility(View.VISIBLE);
                        Log.e("product", product.getProduct_pk());
                        textViews.get(5).setText(product.getStock_status());
                        total = product.getProduct_final_price();
                        Log.e("total", "" + total);
                        String image = String.valueOf(product.getProduct_image1());
                        String string = image.substring(1, image.length() - 1);
                        Log.e("string image",""+string);
                        try {
                            Picasso.with(getActivity())
                                    .load("http://graminvikreta.com/graminvikreta/" + string)
                                    .placeholder(R.drawable.defaultimage)
                                    .into(noImageAdded);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        productDescWebView.loadDataWithBaseURL(null, product.getProduct_description(), "text/html", "utf-8", null);
                        textViews.get(0).setText(product.getProduct_name());
                        textViews.get(1).setText(MainPage.currency + " " + product.getProduct_final_price());
                        textViews.get(2).setText(MainPage.currency + " " + product.getProduct_price());
                        textViews.get(2).setPaintFlags(textViews.get(2).getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                        try {
                            double discountPercentage = Float.valueOf(product.getProduct_price()) - Float.valueOf(product.getProduct_final_price());
                            Log.e("percentage", discountPercentage + "");
                            discountPercentage = (discountPercentage / Float.valueOf(product.getProduct_price())) * 100;
                            Log.e("percentage==", ""+ Math.round(discountPercentage));
                            if ((double) Math.round(discountPercentage) > 0) {
                                textViews.get(3).setText(((int) Math.round(discountPercentage) + "% Off"));
                            }

                        } catch (Exception e) {
                        }
                        }

                    @Override
                    public void failure(RetrofitError error) {
                        progressBar1.setVisibility(View.GONE);
                        addToCart.setVisibility(View.VISIBLE);
                        Log.e("error", error.toString());
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainPage) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        MainPage.title.setText("Product Details");
        MainPage.cart.setVisibility(View.VISIBLE);
        MainPage.cartCount.setVisibility(View.VISIBLE);
        MainPage.logo.setVisibility(View.GONE);
        MainPage.title.setVisibility(View.VISIBLE);
        MainPage.search.setVisibility(View.GONE);
        MainPage.bottomnavigation.setVisibility(View.GONE);
    }
}
