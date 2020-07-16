package com.hashexclude.instamojo_sdk_android.interfaces;

import com.google.gson.JsonObject;

/**
 * Created by Sidhant Panda on 28/02/16.
 * Email me at sidhantpanda@gmail.com
 */
public interface APIRequestListener {
    void success(JsonObject result);
    void error();
}
