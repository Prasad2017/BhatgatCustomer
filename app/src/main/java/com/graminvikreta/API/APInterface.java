package com.graminvikreta.API;



import com.graminvikreta.Model.Orders;
import com.graminvikreta.Model.StatusResponse;
import com.graminvikreta.Model.ViewpagerResponce;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Prasad on 01-03-2017.
 */

public interface APInterface {

    @GET("androidApp/Customer/slider.php")
    Call<ViewpagerResponce>getViewDataList();

    @FormUrlEncoded
    @POST("androidApp/Customer/viewcart.php")
    Call<Orders> sendAdUserId(@Field("cart_pk") String cart_fk);

    @FormUrlEncoded
    @POST("androidApp/Customer/pastorders.php")
    Call<Orders> PastOrder(@Field("user_fk") String user_fk);

    @FormUrlEncoded
    @POST("androidApp/Customer/Delete_cart.php")
    Call<Orders> deleteAdminQuestion(@Field("cart_product_pk") int cart_product_pk, @Field("cart_total_price") double cart_total_price, @Field("product_total_price") double product_total_price, @Field("cart_pk") String cart_pk);

    @GET("/androidApp/Supplier/sendSMS.php")
    Call<JSONObject> sendSMS(@Query("number") String mobileNumber,
                             @Query("message") String message);

    @FormUrlEncoded
    @POST("/androidApp/Customer/start_bid.php")
    Call<StatusResponse> startBidding(@Field("user_fk") String userId,
                                      @Field("product_fk") String product_fk,
                                      @Field("bid_amount") String bid_amount);

    @GET("/androidApp/Customer/UpdateBid.php")
    Call<StatusResponse> UpdateVendorBid(@Query("") String order_id,
                                         @Query("") String userId,
                                         @Query("") String bid_amt);
}
