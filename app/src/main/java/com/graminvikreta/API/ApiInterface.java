package com.graminvikreta.API;


import com.graminvikreta.Model.StatusResponse;
import com.graminvikreta.Model.WishlistResponse;
import com.graminvikreta.Model.AddToWishlistResponse;
import com.graminvikreta.Model.Product;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit2.Call;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("/androidApp/Customer/addwishlist.php")
    public void addToWishList(@Field("product_fk") String product_id, @Field("user_fk") String user_id, Callback<AddToWishlistResponse> callback);

    @FormUrlEncoded
    @POST("/androidApp/Customer/wishcheck.php")
    public void checkWishList(@Field("product_fk") String product_id, @Field("user_fk") String user_id, Callback<AddToWishlistResponse> callback);

    @FormUrlEncoded
    @POST("/androidApp/Customer/Wishlist.php")
    public void getWishList(@Field("user_fk") String user_id, Callback<WishlistResponse> callback);


    @FormUrlEncoded
    @POST("/androidApp/Customer/Product_data.php")
    public void getProductDetails(@Field("product_pk") String product_id, Callback<Product> callback);

    @FormUrlEncoded
    @POST("/androidApp/Customer/addtocart.php")
    public void addToCart(@Field("productid_fk") String product_id, @Field("user_fk") String user_id, @Field("qnty") String qnty,
                          @Field("cart_pk") String cart_pk, @Field("total1") String total, @Field("Final_total") String Final_total,Callback<AddToWishlistResponse> callback);

    // API's endpoints
    @GET("/androidApp/Customer/allproducts.php")
    public void getAllProducts(Callback<List<Product>> callback);



}
