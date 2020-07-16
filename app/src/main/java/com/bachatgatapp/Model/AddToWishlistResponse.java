package com.bachatgatapp.Model;

import java.util.HashMap;
import java.util.Map;

public class AddToWishlistResponse {

    private String success;
    private String message;
    private String cart_pk;

    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public String getCart_pk() {
        return cart_pk;
    }

    public void setCart_pk(String cart_pk) {
        this.cart_pk = cart_pk;
    }
}
