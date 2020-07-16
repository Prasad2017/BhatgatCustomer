package com.hashexclude.instamojo_sdk_android.restCalls;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.JsonObject;
import com.hashexclude.instamojo_sdk_android.Instamojo;
import com.hashexclude.instamojo_sdk_android.interfaces.APIRequestListener;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * Created by Sidhant Panda on 28/02/16.
 * Email me at sidhantpanda@gmail.com
 */
public class  GetPaymentDetailsTask extends AsyncTask<Void, String, Object> {

    private final Context context;
    private final String paymentRequestId;

    private APIRequestListener apiRequestListener;

    public GetPaymentDetailsTask(Context context, String paymentRequestId) {
        this.context = context;
        this.paymentRequestId = paymentRequestId;
    }

    public void setApiRequestListener(APIRequestListener apiRequestListener) {
        this.apiRequestListener = apiRequestListener;
    }

    @Override
    protected Object doInBackground(Void... params) {
        Ion.with(context).load(Endpoints.PAYMENT_STATUS + paymentRequestId + "/")
                .setHeader("X-Api-Key", Instamojo.getApiKey())
                .setHeader("X-Auth-Token", Instamojo.getAuthToken())
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if (e == null) {
                            if (apiRequestListener != null) {
                                apiRequestListener.success(result);
                            }
                        } else {
                            if (apiRequestListener != null) {
                                apiRequestListener.error();
                            }
                        }
                    }
                });
        return null;
    }
}
