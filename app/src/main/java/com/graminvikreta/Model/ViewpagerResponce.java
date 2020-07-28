package com.graminvikreta.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ViewpagerResponce {


    @SerializedName("view")

    private List<Viewpager> viewpagerList;

    public List<Viewpager> getViewpagerList() {
        return viewpagerList;
    }

    public void setViewpagerList(List<Viewpager> viewpagerList) {
        this.viewpagerList = viewpagerList;
    }
}

