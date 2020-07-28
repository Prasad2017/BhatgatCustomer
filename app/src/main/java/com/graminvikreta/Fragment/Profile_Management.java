package com.graminvikreta.Fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.graminvikreta.Activity.Login;
import com.graminvikreta.Activity.MainPage;
import com.graminvikreta.Extra.Common;
import com.graminvikreta.R;

import java.io.File;
import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class Profile_Management extends Fragment {

    View view;
    @BindViews({R.id.my_profile, R.id.address, R.id.change_password, R.id.logout})
    List<TextView> textViews;
    public static Activity activity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_managment, container, false);
        ButterKnife.bind(this, view);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        activity = (Activity) view.getContext();
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        ((MainPage) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        MainPage.cart.setVisibility(View.GONE);
        MainPage.cartCount.setVisibility(View.GONE);
        MainPage.logo.setVisibility(View.GONE);
        MainPage.title.setVisibility(View.VISIBLE);
        MainPage.title.setText("Profile Management");
        MainPage.search.setVisibility(View.GONE);
    }

    @OnClick({R.id.my_profile, R.id.address, R.id.change_password, R.id.logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_profile:
                ((MainPage) getActivity()).loadFragment(new MyProfile(), true);
                break;
            case R.id.address:

                break;
            case R.id.change_password:
                ((MainPage)getActivity()).loadFragment(new Change_Password(),true);
                break;
            case R.id.logout:
                logout();
                break;
        }
    }

    private void logout() {

        final SweetAlertDialog alertDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
        alertDialog.setTitleText("Are you sure you want to logout?");
        alertDialog.setCancelText("Cancel");
        alertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                alertDialog.dismissWithAnimation();
            }
        });
        alertDialog.show();
        Button btn = alertDialog.findViewById(R.id.confirm_button);
        btn.setBackground(getResources().getDrawable(R.drawable.custom_dialog_button));
        btn.setText("Logout");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Common.saveUserData(getActivity(), "user_id", "");
                File file1 = new File("data/data/com.graminvikreta/shared_prefs/admin.xml");
                if (file1.exists()) {
                    file1.delete();
                }
                Intent intent = new Intent(getActivity(), Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                getActivity().finishAffinity();
            }
        });
    }
}
