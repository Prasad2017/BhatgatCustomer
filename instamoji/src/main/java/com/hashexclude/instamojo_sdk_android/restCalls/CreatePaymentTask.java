package com.hashexclude.instamojo_sdk_android.restCalls;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonObject;
import com.hashexclude.instamojo_sdk_android.interfaces.APIRequestListener;
import com.hashexclude.instamojo_sdk_android.models.PaymentData;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.hashexclude.instamojo_sdk_android.activities.PaymentActivity.usercontact;
import static com.hashexclude.instamojo_sdk_android.activities.PaymentActivity.userid;
import static com.hashexclude.instamojo_sdk_android.activities.PaymentActivity.username;
import static com.hashexclude.instamojo_sdk_android.activities.PaymentActivity.userpayment;

/**
 * Created by Sidhant Panda on 28/02/16.
 * Email me at sidhantpanda@gmail.com
 */
public class CreatePaymentTask extends AsyncTask<Void, String, Object> {

    private final Context context;
    private final PaymentData paymentData;
    private APIRequestListener apiRequestListener;
    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    String reg = "http://choudharyscaffolding.in/androidApp/Nabhik-App/instamoji_payment.php";

    public CreatePaymentTask(Context context, PaymentData paymentData) {
        this.context = context;
        this.paymentData = paymentData;
    }

    public void setApiRequestListener(APIRequestListener apiRequestListener) {
        this.apiRequestListener = apiRequestListener;
    }

    @Override
    protected Object doInBackground(Void... params) {

        try {

            JsonObject json = new JsonObject();
            json.addProperty("purpose", "Customer Demo");
            json.addProperty("amount", userpayment);
            Log.e("amount",""+userpayment);
            json.addProperty("currency", "INR");
            json.addProperty("buyerName", username);
            Log.e("buyerName",""+username);
            json.addProperty("email", "");
            json.addProperty("phone", usercontact);
            String webhook = "http://softmatesystems.com/androidApp/crayon/instamoji_payment.php?amount="+userpayment+"&user_id="+userid;
            json.addProperty("webhook", webhook);
            Log.e("webhook",webhook);
            json.addProperty("redirect_url", "http://softmatesystems.com/androidApp/crayon/instamoji_payment.php?amount="+userpayment+"&user_id="+userid);

            Ion.with(context).load(Endpoints.CREATE_PAYMENT)
                    // .setHeader("X-Api-Key", "4e18dfaf0d1ce51cc0b67e829cabc17e")
                    .setHeader("X-Api-Key", "e1e53d97ed130d5714f1327bc9e80e8b")
                    // .setHeader("X-Auth-Token", "cb0d285c563dc5c514fab50be306103e")
                    .setHeader("X-Auth-Token", "7f6bd21b9295e60e0d673de5828c3f2d")
                    .setJsonObjectBody(json)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                                if (e == null) {
                                    if (apiRequestListener != null) {
                                        apiRequestListener.success(result);
                                        RequestParams requestParams=new RequestParams();
                                        requestParams.put("user_id",userid);
                                        requestParams.put("amount",userpayment);
                                        asyncHttpClient.get(reg, requestParams, new AsyncHttpResponseHandler() {
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
                                            {
                                                String update=new String(responseBody);

                                                try
                                                {
                                                    JSONObject jo=new JSONObject(update);

                                                    if (jo.getString("success").equals("1"))
                                                    {
                                                        // Toast.makeText(PaymentActivity.this, ""+jo.getString("message"), Toast.LENGTH_SHORT).show();
                                                        //finish();
                                                    }
                                                    else if (jo.getString("success").equals("0"))
                                                    {
                                                        // Toast.makeText(PaymentActivity.this, ""+jo.getString("message"), Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                                catch (JSONException e)
                                                {
                                                    e.printStackTrace();
                                                }

                                            }

                                            @Override
                                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                            }
                                        });
                                    }
                                } else {
                                    if (apiRequestListener != null) {
                                        apiRequestListener.error();
                                    }
                                }
                        }
                    });

        }catch (Exception e){

        }
        return null;
    }
}
