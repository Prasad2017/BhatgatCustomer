package com.bachatgatapp.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.bachatgatapp.Extra.Config;
import com.bachatgatapp.Extra.DetectConnection;
import com.bachatgatapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;

public class SignUp extends AppCompatActivity {

    @BindViews({R.id.username, R.id.contact, R.id.email, R.id.password})
    List<EditText> editTexts;
    @BindView(R.id.loginLinearLayout)
    LinearLayout loginLinearLayout;
    TextView text;
    AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
    public static String Signinurl = "http://graminvikreta.com/androidApp/Customer/Registration.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        loginLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(view);
            }
        });
    }

    protected void hideKeyboard(View view) {
        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @OnClick({R.id.login, R.id.signup, R.id.backid})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                Config.moveTo(SignUp.this, Login.class);
                break;
            case R.id.backid:
                finish();
                break;
            case R.id.signup:
                if (validate(editTexts.get(0)) && validate(editTexts.get(1)) && Config.validateEmail(editTexts.get(2),
                        SignUp.this) && validatePassword(editTexts.get(3))) {
                    signUp();
                }
                break;
        }
    }

    private void signUp() {
        if (DetectConnection.checkInternetConnection(getApplicationContext()))
        {
            final SweetAlertDialog pDialog = new SweetAlertDialog(SignUp.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
            pDialog.setTitleText("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();

            RequestParams requestParams = new RequestParams();
            requestParams.put("full_name",editTexts.get(0).getText().toString());
            requestParams.put("mobileno",editTexts.get(1).getText().toString());
            requestParams.put("email",editTexts.get(2).getText().toString());
            requestParams.put("password",editTexts.get(3).getText().toString());

            asyncHttpClient.post(Signinurl, requestParams, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String s = new String(responseBody);
                    pDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.getString("status").equals("success")){

                            String id = jsonObject.getString("id");

                            final SweetAlertDialog alertDialog = new SweetAlertDialog(SignUp.this, SweetAlertDialog.SUCCESS_TYPE);
                            alertDialog.setTitleText("Done !");
                            alertDialog.setContentText("Registration Successfully");
                            alertDialog.setCancelable(false);
                            alertDialog.show();
                            Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Config.moveTo(SignUp.this,Login.class);
                                }
                            });

                        }else if (jsonObject.getString("status").equals("Fail")){
                            pDialog.dismiss();
                            Toast.makeText(SignUp.this, ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    pDialog.dismiss();
                    Toast.makeText(SignUp.this, "Error in Connection", Toast.LENGTH_SHORT).show();
                }
            });

        } else
        {
            Toast.makeText(this, "No Internet Connection.....", Toast.LENGTH_LONG).show();
        }
    }

    private boolean validate(EditText editText) {
        if (editText.getText().toString().trim().length() > 0) {
            return true;
        }
        editText.setError("Please Fill This");
        editText.requestFocus();
        return false;
    }

    private boolean validation(TextView textView) {
        if (textView.getText().toString().length()>0) {
            return false;
        }
        textView.setError("Please Fill this");
        textView.requestFocus();
        return false;
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
