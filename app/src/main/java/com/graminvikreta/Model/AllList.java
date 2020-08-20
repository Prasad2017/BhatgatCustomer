package com.graminvikreta.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AllList {

    @SerializedName("getdatas")
    @Expose
    private List<Qdata> getdatas = null;

    @SerializedName("data")
    @Expose
    private List<OrderData> orderDataList = null;

    public List<Qdata> getGetdatas() {
        return getdatas;
    }

    public void setGetdatas(List<Qdata> getdatas) {
        this.getdatas = getdatas;
    }

    public List<OrderData> getOrderDataList() {
        return orderDataList;
    }

    public void setOrderDataList(List<OrderData> orderDataList) {
        this.orderDataList = orderDataList;
    }
}
