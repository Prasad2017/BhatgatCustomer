package com.graminvikreta.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.graminvikreta.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderConfirmed extends AppCompatActivity {

    @BindView(R.id.continueShopping)
    Button continueShopping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmed);
        ButterKnife.bind(this);

        SharedPreferences sharedPreferences = getSharedPreferences("cart", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("cart_id");
        editor.clear();
        editor.apply();
        editor.commit();

    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences sharedPreferences = getSharedPreferences("cart", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("cart_id");
        editor.clear();
        editor.apply();
        editor.commit();
    }

    @OnClick(R.id.continueShopping)
    public void onClick(View view) {
        Intent intent = new Intent(OrderConfirmed.this, MainPage.class);
        startActivity(intent);
        finishAffinity();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(OrderConfirmed.this, MainPage.class);
        startActivity(intent);
        finishAffinity();
    }
}
