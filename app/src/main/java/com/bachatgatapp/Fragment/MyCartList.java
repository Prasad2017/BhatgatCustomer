package com.bachatgatapp.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bachatgatapp.Activity.MainPage;
import com.bachatgatapp.Extra.Config;
import com.bachatgatapp.Extra.DetectConnection;
import com.bachatgatapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyCartList extends Fragment {

    View view;
    @BindView(R.id.myOrdersRecyclerView)
    RecyclerView myOrdersRecyclerView;
  //  public static MyOrdersResponse myOrdersResponseData;

    @BindView(R.id.emptyOrdersLayout)
    LinearLayout emptyOrdersLayout;
    @BindView(R.id.loginLayout)
    LinearLayout loginLayout;
    @BindView(R.id.continueShopping)
    Button continueShopping;

    public static Context context;
    public MyCartList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_cart_list, container, false);
        ButterKnife.bind(this, view);
        context = getActivity();
        MainPage.title.setText("My Cart");
        if (!MainPage.userId.equalsIgnoreCase("")) {
            if (DetectConnection.checkInternetConnection(getActivity())){
            getMyOrders();
            }else {
                Toast.makeText(getActivity(), "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
            }
        } else {
            loginLayout.setVisibility(View.VISIBLE);
        }
        return view;
    }
    @OnClick({R.id.continueShopping})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.continueShopping:
                Config.moveTo(getActivity(), MainPage.class);
                getActivity().finish();
                break;
        }
    }
    private void getMyOrders() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainPage) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        MainPage.cart.setVisibility(View.GONE);
        MainPage.logo.setVisibility(View.GONE);
        MainPage.title.setVisibility(View.VISIBLE);
        MainPage.bottomnavigation.setVisibility(View.GONE);
    }
}
