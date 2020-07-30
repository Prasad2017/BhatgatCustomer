package com.graminvikreta.Model;

import com.google.gson.annotations.SerializedName;

public class StatusResponse {

    @SerializedName("success")
    String success;

    @SerializedName("message")
    String message;

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
}
