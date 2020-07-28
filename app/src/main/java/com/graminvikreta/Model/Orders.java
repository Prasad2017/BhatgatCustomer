package com.graminvikreta.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Orders {

    @SerializedName("product_name")
    private String product_name;
    @SerializedName("product_description")
    private String product_description;
    @SerializedName("cart_pk")
    private int cart_pk;
    @SerializedName("user_fk")
    private int user_fk;
    @SerializedName("total")
    private double total;
    @SerializedName("cart_product_pk")
    private  String cart_product_pk;
    @SerializedName("product_fk")
    private int product_fk;
    @SerializedName("qty")
    private  String qty;
    @SerializedName("price")
    private double price;
    @SerializedName("product_image1")
    private String product_image1;
    @SerializedName("total_price")
    private double total_price;
    @SerializedName("product_discount")
    private String product_discount;
    @SerializedName("product_final_price")
    private double product_final_price;
    @SerializedName("order_status")
    private String order_status;
    @SerializedName("success")
    private String success;
    @SerializedName("UserQuestionsList")
    private List<Orders> UserQuestionsList;


    public Orders() {

        cart_pk=0;
        product_fk=0;
        price=0;
        product_description="";
        success="";
        UserQuestionsList=null;
        product_discount="";
        product_final_price=0;
        product_image1="";
        total_price=0;
        total=0;
        user_fk=0;
        order_status="";
        qty="";
    }


    public int getUser_fk() {
        return user_fk;
    }

    public void setUser_fk(int user_fk) {
        this.user_fk = user_fk;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_description() {
        return product_description;
    }

    public void setProduct_description(String product_description) {
        this.product_description = product_description;
    }

    public int getCart_pk() {
        return cart_pk;
    }

    public void setCart_pk(int cart_pk) {
        this.cart_pk = cart_pk;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getCart_product_pk() {
        return cart_product_pk;
    }

    public void setCart_product_pk(String cart_product_pk) {
        this.cart_product_pk = cart_product_pk;
    }

    public int getProduct_fk() {
        return product_fk;
    }

    public void setProduct_fk(int product_fk) {
        this.product_fk = product_fk;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getProduct_image1() {
        return product_image1;
    }

    public void setProduct_image1(String product_image1) {
        this.product_image1 = product_image1;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public String getProduct_discount() {
        return product_discount;
    }

    public void setProduct_discount(String product_discount) {
        this.product_discount = product_discount;
    }

    public double getProduct_final_price() {
        return product_final_price;
    }

    public void setProduct_final_price(double product_final_price) {
        this.product_final_price = product_final_price;
    }


    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<Orders> getUserQuestionsList() {
        return UserQuestionsList;
    }

    public void setUserQuestionsList(List<Orders> userQuestionsList) {
        UserQuestionsList = userQuestionsList;
    }
}
