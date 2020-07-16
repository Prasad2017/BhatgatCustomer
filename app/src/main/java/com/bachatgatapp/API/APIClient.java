package com.bachatgatapp.API;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Prasad on 01-03-2017.
 */

public class APIClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {


        if (retrofit == null) {

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(1, TimeUnit.MINUTES)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl("http://logic-fort.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

      /*  Retrofit retrofit = new Retrofit.Builder()
                .baseUrl()
                .addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit;*/


}
