package com.graminvikreta.Model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BidData {

    String success ;
    List<DeliverStock> orderdata =null;
    Map<String,Object> additionalproperties = new HashMap<>();

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<DeliverStock> getOrderdata() {
        return orderdata;
    }

    public void setOrderdata(List<DeliverStock> orderdata) {
        this.orderdata = orderdata;
    }

    public Map<String, Object> getAdditionalproperties() {
        return additionalproperties;
    }

    public void setAdditionalproperties(Map<String, Object> additionalproperties) {
        this.additionalproperties = additionalproperties;
    }

}
