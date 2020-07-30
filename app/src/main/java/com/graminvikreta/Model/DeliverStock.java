package com.graminvikreta.Model;

import java.util.List;

public class DeliverStock {

    List<Qdata> dates =null;
    String id, order_title;

    public String getOrder_title() {
        return order_title;
    }

    public void setOrder_title(String order_title) {
        this.order_title = order_title;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public List<Qdata> getDates() {
        return dates;
    }

    public void setDates(List<Qdata> dates) {
        this.dates = dates;
    }

}
