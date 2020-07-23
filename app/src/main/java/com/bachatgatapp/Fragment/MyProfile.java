package com.bachatgatapp.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.bachatgatapp.Activity.MainPage;
import com.bachatgatapp.Extra.DetectConnection;
import com.bachatgatapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyProfile extends Fragment {
    View view;
    @BindViews({R.id.fullNameEdt, R.id.mobEditText, R.id.areaEditText, R.id.buildingEditText, R.id.pincodeEditText, R.id.landmarkEditText,})
    List<EditText> editTexts;
    @BindView(R.id.submitBtn)
    Button submitBtn;
    @BindViews({R.id.male, R.id.female})
    List<CircleImageView> circleImageViews;
    String gender = "";
    @BindView(R.id.profileLayout)
    LinearLayout profileLayout;
    @BindView(R.id.logout)
    Button logout;
    Spinner cityspinner,statespinner;
    String[] city_id_pk, city_name;
    String[] state_id_pk, state_name;
    String cityid, stateid,state_value,city_value;
    SweetAlertDialog pDialog;
    AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
    public static String profileurl = "http://graminvikreta.com/androidApp/Customer/getprofile.php";
    public static String updateurl = "http://graminvikreta.com/androidApp/Customer/Update_profile.php";
    public static final String city="http://graminvikreta.com/androidApp/Customer/City.php";
    public static final String stateurl="http://graminvikreta.com/androidApp/Customer/State.php";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_profile, container, false);
        ButterKnife.bind(this, view);
        MainPage.title.setText("My Profile");
        if (!MainPage.userId.equalsIgnoreCase("")) {
            getUserdetails();
        } else {
            profileLayout.setVisibility(View.INVISIBLE);
        }
        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard(view);
            }
        });

        cityspinner = view.findViewById(R.id.cityEditText);
        statespinner = view.findViewById(R.id.stateEditText);

        asyncHttpClient.get(stateurl, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {

                String string = new String(responseBody);


                try {
                    JSONObject jsonObject = new JSONObject(string);

                    JSONArray jsonarray;
                    jsonarray = jsonObject.getJSONArray("success");
                    state_name = new String[jsonarray.length()];
                    state_id_pk = new String[jsonarray.length()];
                    for (int i = 0; i < jsonarray.length(); i++) {
                        jsonObject = jsonarray.getJSONObject(i);
                        state_name[i] = jsonObject.getString("state_name");
                        state_id_pk[i] = jsonObject.getString("state_pk");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item, state_name);


                spinnerAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                statespinner.setAdapter(spinnerAdapter);

            }

            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

        cityspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cityid = city_id_pk[i];
                city_value = String.valueOf(adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        statespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                stateid = state_id_pk[i];
                state_value = String.valueOf(adapterView.getItemAtPosition(i));
                getCity();
                cityspinner.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return view;
    }

    private void getCity() {

        RequestParams requestParams = new RequestParams();
        requestParams.put("state_fk",stateid);

        asyncHttpClient.get(city, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {

                String string = new String(responseBody);


                try {
                    JSONObject jsonObject = new JSONObject(string);

                    JSONArray jsonarray;
                    jsonarray = jsonObject.getJSONArray("success");
                    city_name = new String[jsonarray.length()];
                    city_id_pk = new String[jsonarray.length()];
                    for (int i = 0; i < jsonarray.length(); i++) {
                        jsonObject = jsonarray.getJSONObject(i);
                        city_name[i] = jsonObject.getString("city_name");
                        city_id_pk[i] = jsonObject.getString("city_pk");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_spinner_item, city_name);


                spinnerAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                cityspinner.setAdapter(spinnerAdapter);

            }


            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void getUserdetails() {

        pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading...");
        pDialog.setCancelable(true);
        pDialog.show();

        RequestParams requestParams = new RequestParams();
        requestParams.put("id",MainPage.userId);
        asyncHttpClient.post(profileurl, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                pDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    if (jsonObject.getString("status").equals("success")){

                        String name = jsonObject.getString("full_name");
                        String mobileno = jsonObject.getString("mobileno");
                        String state = jsonObject.getString("state_name");
                        String city = jsonObject.getString("city_name");
                        String pincode = jsonObject.getString("pincode");
                        String landmark = jsonObject.getString("landmark");
                        String building_name = jsonObject.getString("building_name");
                        String address = jsonObject.getString("address");
                        String street = jsonObject.getString("street");
                        int state_fk = jsonObject.getInt("state_fk");
                        int city_fk = jsonObject.getInt("city_fk");
                        gender = jsonObject.getString("gender");

                        statespinner.setSelection(getIndex(statespinner, state));

                        try {
                            if (gender.equalsIgnoreCase("Female")) {
                                circleImageViews.get(0).setImageResource(R.drawable.male_unselect);
                                circleImageViews.get(1).setImageResource(R.drawable.female_select);
                                gender = "Female";
                            } else if (gender.equalsIgnoreCase("Male")) {

                                circleImageViews.get(0).setImageResource(R.drawable.male_select);
                                circleImageViews.get(1).setImageResource(R.drawable.female_unselect);
                                gender = "Male";
                            }
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "please choose gender", Toast.LENGTH_SHORT).show();
                        }

                        editTexts.get(0).setText(name);
                        editTexts.get(1).setText(mobileno);
                        editTexts.get(2).setText(street);
                        editTexts.get(3).setText(building_name);
                        editTexts.get(4).setText(pincode);
                        editTexts.get(5).setText(landmark);

                    }else if (jsonObject.getString("status").equals("Fail")){
                        pDialog.dismiss();
                        Toast.makeText(getActivity(), "Something went wrong..", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                pDialog.dismiss();
                Toast.makeText(getActivity(), "Error ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private int getIndex(Spinner spinner, String myString){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }

    protected void hideKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @OnClick({R.id.male, R.id.female, R.id.submitBtn, R.id.logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.male:
                circleImageViews.get(0).setImageResource(R.drawable.male_select);
                circleImageViews.get(1).setImageResource(R.drawable.female_unselect);
                gender = "Male";
                break;
            case R.id.female:
                circleImageViews.get(0).setImageResource(R.drawable.male_unselect);
                circleImageViews.get(1).setImageResource(R.drawable.female_select);
                gender = "Female";
                break;
            case R.id.submitBtn:
                if (gender.equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please choose gender", Toast.LENGTH_SHORT).show();
                } else if (validate(editTexts.get(0))
                        && validate(editTexts.get(1))
                        && validate(editTexts.get(2))
                        && validate(editTexts.get(3))
                        && validate(editTexts.get(4)))


                    updateProfile();
                break;
        }
    }

    private void updateProfile() {

        if (DetectConnection.checkInternetConnection(getActivity())) {
            final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
            pDialog.setTitleText("Loading...");
            pDialog.setCancelable(false);
            pDialog.show();

            String address = editTexts.get(3).getText().toString()+","+editTexts.get(5).getText().toString()+","+editTexts.get(2).getText().toString()+","+cityid
                    +","+editTexts.get(4).getText().toString();

            Log.e("address",address);
            RequestParams requestParams = new RequestParams();
            requestParams.put("full_name", editTexts.get(0).getText().toString());
            requestParams.put("mobileno", editTexts.get(1).getText().toString());
            requestParams.put("street", editTexts.get(2).getText().toString());
            requestParams.put("building_name", editTexts.get(3).getText().toString());
            requestParams.put("pincode", editTexts.get(4).getText().toString());
            requestParams.put("landmark", editTexts.get(5).getText().toString());
            requestParams.put("city_fk", cityid);
            requestParams.put("state_fk", stateid);
            requestParams.put("gender", gender);
            requestParams.put("id", MainPage.userId);
            requestParams.put("address", address);

            asyncHttpClient.post(updateurl, requestParams, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    String s = new String(responseBody);
                    pDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(s);
                        if (jsonObject.getString("success").equals("1")) {
                            Toast.makeText(getActivity(), "" + jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            getUserdetails();
                        } else if (jsonObject.getString("success").equals("0")) {
                            pDialog.dismiss();
                            Toast.makeText(getActivity(), "Something went wrong..", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    pDialog.dismiss();
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
            });
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

    @Override
    public void onStart() {
        super.onStart();
        ((MainPage) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        MainPage.cart.setVisibility(View.GONE);
        MainPage.logo.setVisibility(View.GONE);
        MainPage.title.setVisibility(View.VISIBLE);
        if (DetectConnection.checkInternetConnection(getActivity())) {

        } else {
            Toast.makeText(getActivity(), "Internet Connection not Available", Toast.LENGTH_SHORT).show();
        }
    }
}
