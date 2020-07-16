
package com.bachatgatapp.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product {

    private String success;
    private String product_pk;
    private String product_discount;
    private String product_kg;
    private String master_product_id_fk;
    private String product_details_status;
    private String product_name;
    private String product_description;
    private String vendor_id_fk;
    private String product_price;
    private String product_final_price;
    private String product_actual_stock;
    private String stock_status;
    private List<String> product_image1 = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getProduct_final_price() {
        return product_final_price;
    }

    public void setProduct_final_price(String product_final_price) {
        this.product_final_price = product_final_price;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getProduct_pk() {
        return product_pk;
    }

    public void setProduct_pk(String product_pk) {
        this.product_pk = product_pk;
    }

    public String getProduct_discount() {
        return product_discount;
    }

    public void setProduct_discount(String product_discount) {
        this.product_discount = product_discount;
    }

    public String getProduct_kg() {
        return product_kg;
    }

    public void setProduct_kg(String product_kg) {
        this.product_kg = product_kg;
    }

    public String getProduct_details_status() {
        return product_details_status;
    }

    public void setProduct_details_status(String product_details_status) {
        this.product_details_status = product_details_status;
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

    public String getVendor_id_fk() {
        return vendor_id_fk;
    }

    public void setVendor_id_fk(String vendor_id_fk) {
        this.vendor_id_fk = vendor_id_fk;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_actual_stock() {
        return product_actual_stock;
    }

    public void setProduct_actual_stock(String product_actual_stock) {
        this.product_actual_stock = product_actual_stock;
    }

    public List<String> getProduct_image1() {
        return product_image1;
    }

    public void setProduct_image1(List<String> product_image1) {
        this.product_image1 = product_image1;
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, Object> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    public String getStock_status() {
        return stock_status;
    }

    public void setStock_status(String stock_status) {
        this.stock_status = stock_status;
    }

    public String getMaster_product_id_fk() {
        return master_product_id_fk;
    }

    public void setMaster_product_id_fk(String master_product_id_fk) {
        this.master_product_id_fk = master_product_id_fk;
    }
}