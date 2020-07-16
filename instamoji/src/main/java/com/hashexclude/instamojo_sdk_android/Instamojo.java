package com.hashexclude.instamojo_sdk_android;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Sidhant Panda on 28/02/16.
 * Email me at sidhantpanda@gmail.com
 */
public class Instamojo implements Parcelable {

    private static String API_KEY,
                          AUTH_TOKEN;

    protected Instamojo(Parcel in) {
        API_KEY = in.readString();
        AUTH_TOKEN = in.readString();
    }

    public static void setKeys(String API_KEY, String AUTH_TOKEN) {
        setApiKey(API_KEY);
        setAuthToken(AUTH_TOKEN);
    }

    public static void setApiKey(String apiKey) {
        API_KEY = apiKey;
    }

    public static void setAuthToken(String authToken) {
        AUTH_TOKEN = authToken;
    }

    public static String getApiKey() {
        return API_KEY;
    }

    public static String getAuthToken() {
        return AUTH_TOKEN;
    }

    public static final Creator<Instamojo> CREATOR = new Creator<Instamojo>() {
        @Override
        public Instamojo createFromParcel(Parcel in) {
            return new Instamojo(in);
        }

        @Override
        public Instamojo[] newArray(int size) {
            return new Instamojo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(API_KEY);
        dest.writeString(AUTH_TOKEN);
    }
}