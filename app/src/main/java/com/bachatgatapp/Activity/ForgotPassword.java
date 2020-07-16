package com.bachatgatapp.Activity;


import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.bachatgatapp.Extra.DetectConnection;
import com.bachatgatapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;

public class ForgotPassword extends AppCompatActivity {

    @BindView(R.id.emailId)
     EditText editTexts;
    AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
    public static String forgot = "http://logic-fort.com/androidApp/Customer/forgotpassword.php";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.back,R.id.submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.submit:
                if (editTexts.getText().length()==10) {
                    forgotPassword();
                }else {
                    editTexts.setError("Please enter valid number");
                    editTexts.requestFocus();
                }
                break;
        }
    }

    private void forgotPassword() {
        if (DetectConnection.checkInternetConnection(getApplicationContext())){

            final SweetAlertDialog pDialog = new SweetAlertDialog(ForgotPassword.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
            pDialog.setTitleText("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();

            RequestParams requestParams = new RequestParams();
            requestParams.put("mobileno",editTexts.getText().toString());

            asyncHttpClient.post(forgot, requestParams, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String s=new String(responseBody);
                    pDialog.dismiss();
                    try {
                        JSONObject jsonObject=new JSONObject(s);
                        if (jsonObject.getString("status").equals("success")){

                            String password = jsonObject.getString("password");
                            String name = jsonObject.getString("full_name");
                            sendPassword(editTexts.getText(),password);
                            Log.e("phone",""+password);
                            String message = "This is your password:-- "+password;
                            String no = editTexts.getText().toString();
                            //Intent intent=new Intent(ForgotPassword.this,Login.class);
                           // PendingIntent pi=PendingIntent.getActivity(ForgotPassword.this, 0, intent,0);
                           // SmsManager sms=SmsManager.getDefault();
                           // sms.sendTextMessage(no, null, message, pi,null);
                            Toast.makeText(ForgotPassword.this, "Password sent to mobile number", Toast.LENGTH_SHORT).show();

                        }else if (jsonObject.getString("status").equals("Fail")){
                            pDialog.dismiss();
                            Toast.makeText(ForgotPassword.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    pDialog.dismiss();
                    Toast.makeText(ForgotPassword.this, "Error", Toast.LENGTH_SHORT).show();
                }
            });

            pDialog.dismiss();
        }else {
            Toast.makeText(this, "No Internet Connection.....", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendPassword(final Editable text, final String password) {

        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Mail sender = new Mail("softmate.suport@gmail.com", "Softmate@2018!");
                    sender.sendMail("Customer Application",
                            "Hi "+text,"\n This is your Password: "+password,
                            "softmate.suport@gmail.com");

                } catch (Exception e) {
                    Log.e("mylog", "Error: " + e.getMessage());
                }
            }
        });
        sender.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (DetectConnection.checkInternetConnection(getApplicationContext())) {

        } else {
            Toast.makeText(getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }
    }
}
