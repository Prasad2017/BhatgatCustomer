package com.bachatgatapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.bachatgatapp.Adapter.ExpandableListAdapter;
import com.bachatgatapp.Extra.Common;
import com.bachatgatapp.Fragment.Category;
import com.bachatgatapp.Fragment.Complaints;
import com.bachatgatapp.Fragment.Home;
import com.bachatgatapp.Fragment.MyOrders;
import com.bachatgatapp.Fragment.MyProfile;
import com.bachatgatapp.Fragment.PastOrder;
import com.bachatgatapp.Fragment.PrivatePolicy;
import com.bachatgatapp.Fragment.Profile_Management;
import com.bachatgatapp.Fragment.Searchview;
import com.bachatgatapp.Fragment.TermsAndConditions;
import com.bachatgatapp.Fragment.Wishlist;
import com.bachatgatapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cz.msebera.android.httpclient.Header;

public class MainPage extends AppCompatActivity {

    public static String  currency = "₹";;
    String from;
    public static View toolbar, searchLayout;
    boolean doubleBackToExitPressedOnce = false;
    public static ImageView menu, back, cart, search, logo;
    public static DrawerLayout drawerLayout;
    public static String userId,email,name,contact,address,cart_id;
    @BindView(R.id.nav_view)
    public NavigationView navigationView;

    public static BottomNavigationView bottomnavigation;
    public static TextView title, cartCount;
    int count;
    public static ProgressBar progressBar;
    TextView uname,uemail;

    public static String GetCartURL="http://logic-fort.com/androidApp/Customer/cartcount.php";
    AsyncHttpClient asyncHttpClient=new AsyncHttpClient();
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        ButterKnife.bind(this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initViews();
        //getCart();
        loadFragment(new Home(), false);
        uname = findViewById(R.id.uname);
        uemail = findViewById(R.id.uemail);
        try {

            /*String main = "session";
            SharedPreferences sharedPreferences = getSharedPreferences(main, Context.MODE_PRIVATE);
            userId = sharedPreferences.getString("user_id", null);
            Log.e("id",""+userId);
            email = sharedPreferences.getString("email", null);
            name = sharedPreferences.getString("name", null);
            contact = sharedPreferences.getString("contact", null);
            address = sharedPreferences.getString("address", null);*/

            userId=Common.getSavedUserData(MainPage.this,"user_id");
            Log.e("id",userId);
            email=Common.getSavedUserData(MainPage.this,"email");
            email=Common.getSavedUserData(MainPage.this,"name");
            contact=Common.getSavedUserData(MainPage.this,"contact");
            address=Common.getSavedUserData(MainPage.this,"address");

            SharedPreferences sharedPreferences = getSharedPreferences("cart",Context.MODE_PRIVATE);
            cart_id=sharedPreferences.getString("cart_id","");
            Log.e("cart",""+cart_id);

            uname.setText(name);
            uemail.setText(email);

        }catch (Exception e){
            e.printStackTrace();
           // Toast.makeText(this, "Update your profile", Toast.LENGTH_SHORT).show();
        }
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
// Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
               // listDataHeader.get(groupPosition);
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
               // listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);

                if((listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition)).equals("My Profile"))
                {
                    loadFragment(new MyProfile(),true);
                }else  if ((listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition)).equals("Wishlist")){
                    loadFragment(new Wishlist(),true);
                }else  if ((listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition)).equals("Save Address")){
                   // loadFragment(new Wishlist(),true);
                }else  if ((listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition)).equals("Change Password")){
                   // loadFragment(new Wishlist(),true);
                }else  if ((listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition)).equals("Logout")){
                    //loadFragment(new Wishlist(),true);
                    logout();
                }else if ((listDataHeader.get(groupPosition)).equals("Category")){
                    loadFragment(new Category(),true);
                }else if ((listDataHeader.get(groupPosition)).equals("Past Oder")){
                    loadFragment(new PastOrder(),true);
                }else if ((listDataHeader.get(groupPosition)).equals("Save Card")){
                    loadFragment(new Home(),true);
                }else if ((listDataHeader.get(groupPosition)).equals("Rate US")){
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                    }
                }else if ((listDataHeader.get(groupPosition)).equals("Share")){
                    shareApp();
                }else if ((listDataHeader.get(groupPosition)).equals("Term & Conditions")){
                    loadFragment(new TermsAndConditions(),true);
                }else if ((listDataHeader.get(groupPosition)).equals("Help & Support")){
                    loadFragment(new TermsAndConditions() ,true);
                }else if ((listDataHeader.get(groupPosition)).equals("Private Policy")){
                    loadFragment(new PrivatePolicy(),true);
                }
                return false;
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.category:
                        loadFragment(new Home(),true);
                        break;
                    case R.id.order:
                        loadFragment(new PastOrder(),true);
                        break;
                    case R.id.setting:
                        loadFragment(new Profile_Management(),true);
                        break;
                    /*case R.id.card:
                        loadFragment(new Home(),true);
                        break;*/
                    case R.id.rate:
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
                        }
                        break;
                    case R.id.share:
                        shareApp();
                        break;
                    case R.id.term:
                        loadFragment(new TermsAndConditions(),true);
                        break;
                    case R.id.help:
                        loadFragment(new Home(),true);
                        break;
                    case R.id.complaints:
                        loadFragment(new Complaints(),true);
                        break;
                    case R.id.policy:
                        loadFragment(new PrivatePolicy(),true);
                        break;
                    case R.id.logout:
                     logout();
                        break;
                }
                return false;
            }
        });

        bottomnavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        loadFragment(new Home(),true);
                        break;
                    case R.id.whishlist:
                        loadFragment(new Wishlist(),true);
                        break;
                    case R.id.notification:
                        loadFragment(new Home(),true);
                        break;
                }
                return true;
            }
        });
    }

    public void getCart() {
        MainPage.progressBar.setVisibility(View.VISIBLE);
        MainPage.cartCount.setVisibility(View.GONE);
        RequestParams requestParams = new RequestParams();
        requestParams.put("cart_pk",cart_id);

        asyncHttpClient.get(GetCartURL, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                MainPage.cartCount.setVisibility(View.VISIBLE);
                MainPage.progressBar.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("success");
                    for (int i =0;i<jsonArray.length();i++){
                        jsonObject = jsonArray.getJSONObject(i);
                        String total = jsonObject.getString("Total");
                        Log.e("total",""+total);

                        if (total==null){
                            MainPage.progressBar.setVisibility(View.GONE);
                            MainPage.cartCount.setText("0");
                            MainPage.cartCount.setVisibility(View.VISIBLE);
                        }else {
                            MainPage.progressBar.setVisibility(View.GONE);
                            MainPage.cartCount.setVisibility(View.VISIBLE);
                            MainPage.cartCount.setText(total);

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                // Toast.makeText(context, ""+error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void prepareListData() {
            listDataHeader = new ArrayList<String>();
            listDataChild = new HashMap<String, List<String>>();

            // Adding child data
            listDataHeader.add("Category");
            listDataHeader.add("Past Order");
            listDataHeader.add("Profile Management");
            listDataHeader.add("Save Card");
            listDataHeader.add("Rate US");
            listDataHeader.add("Share");
            listDataHeader.add("Term & Conditions");
            listDataHeader.add("Help & Support");
            listDataHeader.add("Private Policy");


            // Adding child data
            List<String> top250 = new ArrayList<String>();
            top250.add("My Profile");
            top250.add("Wishlist");
            top250.add("Save Address");
            top250.add("Change Password");
            top250.add("Logout");



            listDataChild.put(listDataHeader.get(0), null); // Header, Child data
            listDataChild.put(listDataHeader.get(1), null); // Header, Child data
            listDataChild.put(listDataHeader.get(2), top250); // Header, Child data
            listDataChild.put(listDataHeader.get(3), null); // Header, Child data
            listDataChild.put(listDataHeader.get(4), null); // Header, Child data
            listDataChild.put(listDataHeader.get(5), null); // Header, Child data
            listDataChild.put(listDataHeader.get(6), null); // Header, Child data
            listDataChild.put(listDataHeader.get(7), null); // Header, Child data
            listDataChild.put(listDataHeader.get(8), null); // Header, Child data
          //  listDataChild.put(listDataHeader.get(1), nowShowing);
           // listDataChild.put(listDataHeader.get(2), comingSoon);
        }

    private void logout() {

        final SweetAlertDialog alertDialog = new SweetAlertDialog(MainPage.this, SweetAlertDialog.WARNING_TYPE);
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
                Common.saveUserData(MainPage.this, "user_id", "");
                File file1 = new File("data/data/com.bachatgatapp/shared_prefs/admin.xml");
                if (file1.exists()) {
                    file1.delete();
                }
                Intent intent = new Intent(MainPage.this, LoginOtp.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finishAffinity();
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
        } else {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back once more to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    public void shareApp() {
        // share app with your friends
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/*");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Try this " + getResources().getString(R.string.app_name) + " App: https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
        startActivity(Intent.createChooser(shareIntent, "Share Using"));
    }

    public void removeCurrentFragmentAndMoveBack() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        /*FragmentTransaction trans = fragmentManager.beginTransaction();
        trans.remove(fragment);
        trans.commit();*/
        fragmentManager.popBackStack();
    }

    public void loadFragment(Fragment fragment, Boolean bool) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        if (bool) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    private void initViews() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        title = (TextView) findViewById(R.id.title);
        cartCount = (TextView) findViewById(R.id.cartCount);
        menu = (ImageView) findViewById(R.id.menu);
        cart = (ImageView) findViewById(R.id.cart);
        back = (ImageView) findViewById(R.id.back);
        logo = (ImageView) findViewById(R.id.logo);
        search = (ImageView) findViewById(R.id.search);
        bottomnavigation = (BottomNavigationView) findViewById(R.id.bottomnavigation);
    }

    @OnClick({R.id.menu, R.id.back, R.id.cart, R.id.cartCount, R.id.search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu:
                if (!Home.swipeRefreshLayout.isRefreshing())
                    if (!MainPage.drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                        MainPage.drawerLayout.openDrawer(Gravity.LEFT);
                    }
                break;
            case R.id.search:
                if (!Home.swipeRefreshLayout.isRefreshing())
                    loadFragment(new Searchview(),true);
                break;
            case R.id.back:
                removeCurrentFragmentAndMoveBack();
                break;
            case R.id.cart:
            case R.id.cartCount:
                if (!Home.swipeRefreshLayout.isRefreshing())

                    if ((cartCount.getText().toString().equalsIgnoreCase("0"))){
                        Toast.makeText(this, "Please Add Product in your cart", Toast.LENGTH_SHORT).show();
            }else{
                loadFragment(new MyOrders(), true);
            }
                break;
        }
    }

    public void lockUnlockDrawer(int lockMode) {
        drawerLayout.setDrawerLockMode(lockMode);
        if (lockMode == DrawerLayout.LOCK_MODE_LOCKED_CLOSED) {
            menu.setVisibility(View.GONE);
            back.setVisibility(View.VISIBLE);
            bottomnavigation.setVisibility(View.GONE);

        } else {
            menu.setVisibility(View.VISIBLE);
            back.setVisibility(View.GONE);
            bottomnavigation.setVisibility(View.VISIBLE);
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
