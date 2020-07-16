package com.bachatgatapp.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.bachatgatapp.Activity.MainPage;
import com.bachatgatapp.R;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class TermsAndConditions extends Fragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_terms_and_conditions, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        ((MainPage) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        MainPage.title.setText("Terms & Conditions");
        MainPage.cart.setVisibility(View.GONE);
        MainPage.logo.setVisibility(View.GONE);
        MainPage.search.setVisibility(View.GONE);
        MainPage.title.setVisibility(View.VISIBLE);
    }

}
