package com.graminvikreta.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.graminvikreta.Activity.MainPage;
import com.graminvikreta.Extra.DetectConnection;
import com.graminvikreta.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class Change_Password extends Fragment {

    View view;
    @BindViews({R.id.username, R.id.contact, R.id.email, R.id.password})
    List<EditText> editTexts;
    @BindView(R.id.changeLinearLayout)
    LinearLayout changeLinearLayout;
    AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
    public static String passwordurl = "http://graminvikreta.com/androidApp/Customer/change_password.php";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_change__password, container, false);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        return view;
    }
    @OnClick({R.id.change})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change:
                if (DetectConnection.checkInternetConnection(getActivity())) {
                    ChnagePassword();
                }
                break;
        }
    }

    private void ChnagePassword() {

        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading...");
        pDialog.setCancelable(true);
        pDialog.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("new_pass",editTexts.get(1).toString());
        requestParams.put("old_pass",editTexts.get(0).toString());
        requestParams.put("c_pass",editTexts.get(2).toString());
        requestParams.put("id",MainPage.userId);

        asyncHttpClient.post(passwordurl, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                pDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equalsIgnoreCase("success")){
                        Toast.makeText(getActivity(), "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }else  if (jsonObject.getString("status").equalsIgnoreCase("Fail")){
                        pDialog.dismiss();
                        Toast.makeText(getActivity(), "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }else if (jsonObject.getString("status").equalsIgnoreCase("Fail1")){
                        pDialog.dismiss();
                        Toast.makeText(getActivity(), "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }else if (jsonObject.getString("status").equalsIgnoreCase("Fail2")){
                        pDialog.dismiss();
                        Toast.makeText(getActivity(), "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                pDialog.dismiss();
                Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });


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
    public void onStart() {
        super.onStart();
        ((MainPage) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        MainPage.cart.setVisibility(View.GONE);
        MainPage.cartCount.setVisibility(View.GONE);
        MainPage.logo.setVisibility(View.GONE);
        MainPage.title.setVisibility(View.VISIBLE);
        MainPage.title.setText("Change Password");
        MainPage.search.setVisibility(View.GONE);
    }
}
