package com.graminvikreta.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.graminvikreta.API.Api;
import com.graminvikreta.Extra.Common;
import com.graminvikreta.Extra.Config;
import com.graminvikreta.Extra.DetectConnection;
import com.graminvikreta.Model.Product;
import com.graminvikreta.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class SplashScreen extends AppCompatActivity {

    public static List<Product> allProductsData;
    public static List<Product> imagesList1;
    @BindView(R.id.errorText)
    TextView errorText;
    String id = "";
    @BindView(R.id.internetNotAvailable)
    LinearLayout internetNotAvailable;
    @BindView(R.id.splashImage)
    ImageView splashImage;
    SharedPreferences sharedPreference, sharedPreferencesCache;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh_screen);
        ButterKnife.bind(this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sharedPreferencesCache = getSharedPreferences("cacheExist", 0);

        // check data from FCM
        try {
            Intent intent = getIntent();
            id = intent.getStringExtra("id");
            Log.d("notification Data", id);
        } catch (Exception e) {
            Log.d("error notification data", e.toString());
        }
        sharedPreference = getSharedPreferences("localData", 0);
        editor = sharedPreference.edit();

        if (DetectConnection.checkInternetConnection(getApplicationContext())) {
            splashImage.setVisibility(View.VISIBLE);
            internetNotAvailable.setVisibility(View.GONE);
         //   getAllProducts();
            moveNext();
        } else {
            internetNotAvailable.setVisibility(View.VISIBLE);
            splashImage.setVisibility(View.GONE);
           // Toast.makeText(this, "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }
    }

    public void getAllProducts() {
        // getting news list data
        Api.getClient().getAllProducts(new Callback<List<Product>>() {
            @Override
            public void success(List<Product> allProducts, Response response) {
                Log.e("Success", ""+response.getStatus());
                try {
                    allProductsData = allProducts;
                    Log.d("allProductsData", allProducts.get(0).getProduct_name());
                    Gson gson = new Gson();
                    String json = gson.toJson(allProductsData);
                    editor.putString("newslist", json);
                    editor.commit();
                    moveNext();
                } catch (Exception e) {
                    Toast.makeText(SplashScreen.this, "No Product Added In This Store!", Toast.LENGTH_SHORT).show();
                    /*errorText.setText("No Product Added In This Store!");
                    internetNotAvailable.setVisibility(View.VISIBLE);
                    splashImage.setVisibility(View.GONE);*/
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("error", error.toString());
                errorText.setText("Internet Connection Not Available");
                internetNotAvailable.setVisibility(View.VISIBLE);
                splashImage.setVisibility(View.GONE);
            }
        });
    }


    @OnClick(R.id.tryAgain)
    public void onClick() {
        if (DetectConnection.checkInternetConnection(getApplicationContext())) {
            splashImage.setVisibility(View.VISIBLE);
            internetNotAvailable.setVisibility(View.GONE);
            moveNext();
        } else {
            internetNotAvailable.setVisibility(View.VISIBLE);
            splashImage.setVisibility(View.GONE);
            //Toast.makeText(this, "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }
    }
    private void moveNext() {
        boolean isFromNotification;
        try {
            imagesList1 = new ArrayList<>();
            if (id.length() > 0) {
                for (int j = 0; j < allProductsData.size(); j++) {
                    if (allProductsData.get(j).getProduct_pk().trim().equalsIgnoreCase(id)) {
                        imagesList1.add(allProductsData.get(j));
                    }
                }

                isFromNotification = true;
            } else {
                isFromNotification = false;
            }
        } catch (Exception e) {
            Log.d("error notification data", e.toString());
        }
        /*Thread timerThread = new Thread() {
            public void run() {
                try {
                    sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    Intent intent = new Intent(SplashScreen.this, LoginOtp.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        timerThread.start();*/

        if (Common.getSavedUserData(SplashScreen.this, "user_id").equalsIgnoreCase("")) {
            Config.moveTo(SplashScreen.this, LoginOtp.class);
            finishAffinity();
        } else {
            Intent intent = new Intent(SplashScreen.this, MainPage.class);
            startActivity(intent);
            finishAffinity();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            trimCache(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        }
        else {
            return false;
        }
    }
}
