package com.graminvikreta.Model;

import com.google.gson.annotations.SerializedName;

public class OrderData {


    @SerializedName("final_order_pk")
    private String final_order_pk;

    @SerializedName("pname")
    private String product_name;

    @SerializedName("firstname")
    private String firstname;

    @SerializedName("order_number")
    private String order_number;

    @SerializedName("grand_amount")
    private String grand_amount;

    @SerializedName("order_date")
    private String order_date;

    @SerializedName("order_status")
    private String order_status;

    @SerializedName("product_image")
    private String product_image;


    public String getFinal_order_pk() {
        return final_order_pk;
    }

    public void setFinal_order_pk(String final_order_pk) {
        this.final_order_pk = final_order_pk;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getGrand_amount() {
        return grand_amount;
    }

    public void setGrand_amount(String grand_amount) {
        this.grand_amount = grand_amount;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }
}
