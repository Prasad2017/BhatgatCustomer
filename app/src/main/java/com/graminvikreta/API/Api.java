package com.graminvikreta.API;

import retrofit.RestAdapter;

/**
 * Created by Pratik
 */
public class Api {

    public static ApiInterface getClient() {

        // change your base URL
        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint("http://graminvikreta.com") //Set the Root URL
                .build(); //Finally building the adapter

        //Creating object for our interface
        ApiInterface api = adapter.create(ApiInterface.class);
        return api;
    }
}
