package com.bachatgatapp.Fragment;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.bachatgatapp.API.APIClient;
import com.bachatgatapp.API.APInterface;
import com.bachatgatapp.Activity.MainPage;
import com.bachatgatapp.Adapter.GalleryAdapter;
import com.bachatgatapp.Extra.DetectConnection;
import com.bachatgatapp.Model.CategoryData;
import com.bachatgatapp.R;
import com.bachatgatapp.Adapter.SlidingImage_Adapter;
import com.bachatgatapp.Model.Viewpager;
import com.bachatgatapp.Model.ViewpagerResponce;
import com.viewpagerindicator.CirclePageIndicator;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class Home extends Fragment {

    View view;

    public static ViewPager mPager;
    @BindView(R.id.categoryRecyclerView)
    RecyclerView categoryRecyclerView;
    public static Activity activity;
    public static NestedScrollView nestedScrollView;
    private String TAG = "testing";
    @BindString(R.string.app_name)
    String app_name;
    @BindView(R.id.sliderLayout)
    LinearLayout sliderLayout;
    public static SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    String cart_id;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    ArrayList<Viewpager> arr = new ArrayList<>();
    CirclePageIndicator indicator;
    public  static  String CategoryURL = "http://graminvikreta.com/androidApp/Customer/Main_category.php";
    public static String GetCartURL="http://graminvikreta.com/androidApp/Customer/cartcount.php";
    AsyncHttpClient asyncHttpClient=new AsyncHttpClient();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        activity = (Activity) view.getContext();
        MainPage.logo.setVisibility(View.VISIBLE);
        nestedScrollView = (NestedScrollView) view.findViewById(R.id.nestedScrollView);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.simpleSwipeRefreshLayout);
        mPager = view.findViewById(R.id.pager);
        indicator = view.findViewById(R.id.indicator);

       // getViewpager();
      //  getCategory();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (DetectConnection.checkInternetConnection(getActivity())) {
                    getViewpager();
                    swipeRefreshLayout.setRefreshing(false);
                } else {
                    Toast.makeText(getActivity(), "Internet Not Available", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("cart", Context.MODE_PRIVATE);
        cart_id=sharedPreferences.getString("cart_id","");
        Log.e("cart",""+cart_id);

        return view;
    }

    public void getCart() {

       /* SharedPreferences sharedPreferences = getActivity().getSharedPreferences("cart",Context.MODE_PRIVATE);
        cart_id=sharedPreferences.getString("cart_id","");
        Log.e("cart",""+cart_id);*/

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

                    if (jsonArray.length()==0){
                        MainPage.progressBar.setVisibility(View.GONE);
                        MainPage.cartCount.setText("0");
                        MainPage.cartCount.setVisibility(View.VISIBLE);
                    }

                    for (int i =0;i<jsonArray.length();i++){
                        jsonObject = jsonArray.getJSONObject(i);
                        String total = jsonObject.getString("Total");
                        Log.e("total+home",""+total);

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

    private void getCategory() {

        final List<CategoryData> data = new ArrayList<>();

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.post(CategoryURL, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String s = new String(responseBody);
                try {
                    JSONObject jsonObject = new JSONObject(s);
                    JSONArray jsonArray = jsonObject.getJSONArray("success");

                   for (int i=0;i<jsonArray.length();i++){

                       jsonObject = jsonArray.getJSONObject(i);

                       //Set Model Class...
                       CategoryData categoryData = new CategoryData();
                       categoryData.catName =jsonObject.getString("main_category_name");
                       categoryData.catImage =jsonObject.getString("main_category_image");
                       categoryData.categary_id =jsonObject.getString("main_category_pk");
                       data.add(categoryData);

                       //Set Adapter...
                       GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
                       categoryRecyclerView.setLayoutManager(gridLayoutManager);
                       GalleryAdapter homeCategoryAdapter = new GalleryAdapter(getActivity(), data);
                       categoryRecyclerView.setAdapter(homeCategoryAdapter);
                       homeCategoryAdapter.notifyDataSetChanged();
                       categoryRecyclerView.setHasFixedSize(true);
                   }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getActivity(), "No Category Found", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getViewpager() {

        APInterface api = APIClient.getClient().create(APInterface.class);
        Call<ViewpagerResponce> call = api.getViewDataList();
        call.enqueue(new Callback<ViewpagerResponce>() {
            @Override
            public void onResponse(Call<ViewpagerResponce> call, Response<ViewpagerResponce> response) {
                try {
                    Log.e("response==", String.valueOf(response.body().getViewpagerList()));
                    arr = (ArrayList<Viewpager>) response.body().getViewpagerList();
                    Log.e("Arraylist==", "" + arr);
                    mPager.setAdapter(new SlidingImage_Adapter(getActivity(), arr));
                    indicator.setViewPager(mPager);

                    final float density = getResources().getDisplayMetrics().density;
                    indicator.setRadius(5 * density);
                    //Set circle indicator radius
                    indicator.setRadius(5 * density);

                    NUM_PAGES = arr.size();
                    // Auto start of viewpager
                    final Handler handler = new Handler();
                    final Runnable Update = new Runnable() {
                        public void run() {

                            if (currentPage == NUM_PAGES) {
                                currentPage = 0;
                            }
                            mPager.setCurrentItem(currentPage++, true);
                        }
                    };
                    Timer swipeTimer = new Timer();
                    swipeTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.post(Update);
                        }
                    }, 10000, 10000);

                    // Pager listener over indicator
                    indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                        @Override
                        public void onPageSelected(int position) {
                            currentPage = position;
                        }

                        @Override
                        public void onPageScrolled(int position, float arg1, int arg2) {

                        }

                        @Override
                        public void onPageScrollStateChanged(int position) {

                        }
                    });

                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<ViewpagerResponce> call, Throwable t) {
                Toast.makeText(getActivity(),"Error", Toast.LENGTH_SHORT).show();
                Log.e("Error:", t.getMessage());
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.e("onStart", "called");
        MainPage.cart.setVisibility(View.VISIBLE);
        MainPage.title.setVisibility(View.GONE);
        MainPage.logo.setVisibility(View.VISIBLE);
        MainPage.search.setVisibility(View.VISIBLE);
        ((MainPage) getActivity()).lockUnlockDrawer(DrawerLayout.LOCK_MODE_UNLOCKED);
        MainPage.drawerLayout.closeDrawers();
        if (DetectConnection.checkInternetConnection(getActivity())) {
            getViewpager();
            getCategory();
            getCart();
        } else {
            Toast.makeText(activity, "Internet Connection Not Available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            trimCache(getActivity());
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
