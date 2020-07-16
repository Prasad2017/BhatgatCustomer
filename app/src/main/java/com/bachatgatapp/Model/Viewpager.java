package com.bachatgatapp.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Prasad on 06-03-2017.
 */

public class Viewpager {

    @SerializedName("slider_main_image")
    String slider_main_image;

    public String getImage() {
        return slider_main_image;
    }

    public void setImage(String image) {
        this.slider_main_image = image;
    }
}
