package com.bachatgatapp.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.Settings;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.bachatgatapp.Extra.Common;
import com.bachatgatapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;

public class Verify extends AppCompatActivity {

    public EditText code1, code2, code3, code4;
    public Button submit;
    public String phoneNumber;
    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    SharedPreferences pref;
    public String code, OTP;
    SharedPreferences.Editor editor;
    public static final int RequestPermissionCode = 7;
    SweetAlertDialog pDialog;
    String email,str_edt_get_otp,get_number;
    public static final String OTPURL="http://prabhagmaza.com/androidApp/Customer/withoutlogin.php";
    //Your authentication key
    String authkey = "17398A5t2BE8n5e3bf000P20";
    //Sender ID,While using route4 sender id should be 6 characters long.
    String senderId = "SOFTMA";
    //define route
    String route="4";
    URLConnection myURLConnection=null;
    URL myURL=null;
    BufferedReader reader=null;
    //Send SMS API
    String mainUrl="http://api.msg91.com/api/sendhttp.php?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestPermission();
        Intent intent = getIntent();
        if (intent != null) {
          //  phoneNumber = intent.getStringExtra(LoginOtp.INTENT_PHONENUMBER);
           // OTP = intent.getStringExtra(LoginOtp.INTENT__CODE);
        }
        code1 = (EditText) findViewById(R.id.inputCode);
        code2 = (EditText) findViewById(R.id.inputCode1);
        code3 = (EditText) findViewById(R.id.inputCode2);
        code4 = (EditText) findViewById(R.id.inputCode3);
        submit = (Button) findViewById(R.id.submit);

        code1.addTextChangedListener(new GenericTextWatcher(code1));
        code2.addTextChangedListener(new GenericTextWatcher(code2));
        code3.addTextChangedListener(new GenericTextWatcher(code3));
        code4.addTextChangedListener(new GenericTextWatcher(code4));

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Success();
            }
        });

    }

    private void requestPermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            showCompleted();
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Verify.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }

        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void Success() {

        try {
            if (code.equalsIgnoreCase(OTP)) {

                pDialog = new SweetAlertDialog(Verify.this, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
                pDialog.setTitleText("Loading...");
                pDialog.setCancelable(true);
                pDialog.show();


                RequestParams requestParams=new RequestParams();
                requestParams.put("mobileno", phoneNumber);

                asyncHttpClient.post(OTPURL, requestParams, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String s=new String(responseBody);
                        pDialog.dismiss();
                        try {
                            JSONObject jsonObject=new JSONObject(s);
                            pDialog.dismiss();
                            if (jsonObject.getString("status").equals("success"))
                            {
                                Toast.makeText(Verify.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                pref = getSharedPreferences("admin", Context.MODE_PRIVATE);
                                editor = pref.edit();
                                editor.putString("AdminLogin","AdminLoginSuccesssful");
                                editor.commit();

                                pDialog.dismiss();



                                String id = jsonObject.getString("id");
                                Log.e("id",""+id);
                      /*  String user_emailid = jsonObject.getString("email");
                        String name = jsonObject.getString("name");
                        String mobileno = jsonObject.getString("mobileno");
                        String address = jsonObject.getString("address");*/


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

                                Common.saveUserData(Verify.this,"user_id",id);
                                // Config.moveTo(VerificationActivity.this,MainPage.class);
                                Intent intent = new Intent(Verify.this,MainPage.class);
                                startActivity(intent);
                                finishAffinity();

                            }
                            else if (jsonObject.getString("status").equals("Fail"))
                            {
                                pDialog.dismiss();
                                Toast.makeText(Verify.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
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

            } else {
                Toast.makeText(this, "Please Enter Valid OTP", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public class GenericTextWatcher implements TextWatcher {
        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            switch (view.getId()) {

                case R.id.inputCode:
                    if (text.length() == 1)
                        code2.requestFocus();
                    break;
                case R.id.inputCode1:
                    if (text.length() == 1)
                        code3.requestFocus();
                    break;
                case R.id.inputCode2:
                    if (text.length() == 1)
                        code4.requestFocus();
                    break;
                case R.id.inputCode3:
                    code = code1.getText().toString() + code2.getText().toString() + code3.getText().toString() + code4.getText().toString();
                    Log.e("text", "" + code);
                    hideKeypad();

                    break;
            }
        }
    }


    private void hideKeypad() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private void showCompleted() {

        Random rand = new Random();
        System.out.printf("%04d%n", rand.nextInt(10000));
        OTP = String.format("%04d", rand.nextInt(10000));
        //getMessage();
        String message = "Your Rera OTP is : "+OTP+" \n Thank you.\n Rera.";
        String encoded_message= URLEncoder.encode(message);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        //Prepare parameter string
        StringBuilder sbPostData= new StringBuilder(mainUrl);
        sbPostData.append("authkey="+authkey);
        sbPostData.append("&mobiles="+"91"+phoneNumber);
        sbPostData.append("&message="+encoded_message);
        sbPostData.append("&route="+route);
        sbPostData.append("&sender="+senderId);

//final string
        mainUrl = sbPostData.toString();
        try
        {
            //prepare connection
            myURL = new URL(mainUrl);
            myURLConnection = myURL.openConnection();
            myURLConnection.connect();
            reader= new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
            //reading response
            String response;
            while ((response = reader.readLine()) != null)
                //print response
                Log.e("RESPONSE", ""+response);
            //finally close connection
            reader.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }
}