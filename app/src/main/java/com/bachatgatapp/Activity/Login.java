package com.bachatgatapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.bachatgatapp.Extra.Common;
import com.bachatgatapp.Extra.Config;
import com.bachatgatapp.Extra.DetectConnection;
import com.bachatgatapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;

public class Login extends AppCompatActivity {

    @BindViews({R.id.email, R.id.password})
    List<EditText> editTexts;
    @BindView(R.id.loginLinearLayout)
    LinearLayout loginLinearLayout;
    AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
    public static String loginurl = "http://prabhagmaza.com/androidApp/Customer/login.php";
    SharedPreferences pref;
    String email,str_edt_get_otp,get_number;
    SharedPreferences.Editor editor;
    String password,Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        loginLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(view);
            }
        });

        File file = new File("data/data/com.bachatgatapp/shared_prefs/admin.xml");
        if (file.exists())
        {
            Intent intent = new Intent(Login.this, MainPage.class);
            startActivity(intent);
            finish();
        }

    }
    protected void hideKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @OnClick({R.id.txtForgotPassword, R.id.skipLoginLayout, R.id.signIn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.skipLoginLayout:
                Config.moveTo(Login.this, SignUp.class);
                break;
            case R.id.txtForgotPassword:
                Config.moveTo(Login.this, ForgotPassword.class);
                break;
            case R.id.signIn:
                if (Config.validateEmail(editTexts.get(0),Login.this) && validatePassword(editTexts.get(1))) {
                    Password = editTexts.get(1).getText().toString();
                    password = null;
                    try {
                        password = md5new(Password);
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    Log.e("pass",password);
                    login();
                }
                break;
        }
    }

    public static String md5new(String input) throws NoSuchAlgorithmException {
        String result = input;
        if(input != null) {
            MessageDigest md = MessageDigest.getInstance("MD5"); //or "SHA-1"
            md.update(input.getBytes());
            BigInteger hash = new BigInteger(1, md.digest());
            result = hash.toString(16);
            while(result.length() < 32) { //40 for SHA-1
                result = "0" + result;
            }
        }
        return result;
    }

    private void login() {

        if (DetectConnection.checkInternetConnection(getApplicationContext()))
        {
            final SweetAlertDialog pDialog = new SweetAlertDialog(Login.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
            pDialog.setTitleText("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();

            RequestParams requestParams = new RequestParams();
            requestParams.put("email",editTexts.get(0).getText().toString());
            requestParams.put("password",password);

            asyncHttpClient.post(loginurl, requestParams, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String s=new String(responseBody);
                    pDialog.dismiss();
                    try {
                        JSONObject jsonObject=new JSONObject(s);
                        if (jsonObject.getString("status").equals("success")){

                            Toast.makeText(Login.this," Success "+jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                            pref = getSharedPreferences("admin", Context.MODE_PRIVATE);
                            editor = pref.edit();
                            editor.putString("AdminLogin","AdminLoginSuccesssful");
                            editor.commit();

                            String id = jsonObject.getString("id");
                            String user_emailid = jsonObject.getString("email");
                            String name = jsonObject.getString("name");
                            String mobileno = jsonObject.getString("mobileno");
                            String address = jsonObject.getString("address");

                            //Put Maintain All user Details on that
                           /* SharedPreferences sharedPreferences;
                            String main = "session";
                            sharedPreferences = getSharedPreferences(main, Context.MODE_PRIVATE);
                            SharedPreferences.Editor e = sharedPreferences.edit();
                            e.putString("user_id", id);
                            e.putString("email",user_emailid);
                            e.putString("name",name);
                            e.putString("contact",mobileno);
                            e.putString("address",address);
                            e.apply();*/

                            Common.saveUserData(Login.this,"user_id",id);
                            Common.saveUserData(Login.this,"email",user_emailid);
                            Common.saveUserData(Login.this,"name",name);
                            Common.saveUserData(Login.this,"contact",mobileno);
                            Common.saveUserData(Login.this,"address",address);

                            Intent intent = new Intent(Login.this,MainPage.class);
                            startActivity(intent);
                            finish();

                        }else if (jsonObject.getString("status").equals("Fail")){
                            pDialog.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    pDialog.dismiss();
                }
            });


        } else
        {
            Toast.makeText(this, "No Internet Connection.....", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validatePassword(EditText editText) {
        if (editText.getText().toString().trim().length() > 5) {
            return true;
        } else if (editText.getText().toString().trim().length() > 0) {
            editText.setError("Password must be of 6 characters");
            editText.requestFocus();
            return false;
        }
        editText.setError("Please Fill This");
        editText.requestFocus();
        return false;
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

