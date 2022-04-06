package com.example.newsxyz.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import com.example.newsxyz.CustomAdapter;
import com.example.newsxyz.Models.NewsApiResponse;
import com.example.newsxyz.Models.NewsHeadlines;
import com.example.newsxyz.Interfaces.OnFetchDataListener;
import com.example.newsxyz.R;
import com.example.newsxyz.RequestManager;
import com.example.newsxyz.Interfaces.SelectListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SelectListener {

    RecyclerView recyclerView;
    CustomAdapter adapter;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog  = new ProgressDialog(this);
        dialog.setTitle("Loading news articles..");
        dialog.show();

        recyclerView = findViewById(R.id.recview);

        loadData();


    }

    private void loadData() {
        if (isInternetIsConnected(MainActivity.this)) {
            RequestManager manager = new RequestManager(this);
            manager.getNewsHeadlines(listener,"business",null);
        }
        else
        {
            SharedPreferences sharedPreferences = getSharedPreferences("credentials",Context.MODE_PRIVATE);
            if (sharedPreferences.contains("taskList"))
            {
                dialog.dismiss();
                Gson mygson = new Gson();
                String myjson = sharedPreferences.getString("taskList",null);
                List<NewsHeadlines> mylist;
                Type type = new TypeToken<List<NewsHeadlines>>(){}.getType();
                mylist = mygson.fromJson(myjson,type);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new GridLayoutManager(this,1));
                CustomAdapter myadapter = new CustomAdapter(this,mylist,this);
                recyclerView.setAdapter(myadapter);

            }
            else {
            Toast.makeText(getApplicationContext(), "Check Your Internet Connection and try again..", Toast.LENGTH_SHORT).show();
        }}
    }



    private final OnFetchDataListener<NewsApiResponse> listener = new OnFetchDataListener<NewsApiResponse>() {
        @Override
        public void onFetchData(List<NewsHeadlines> list, String message) {
            showNews(list);
            dialog.dismiss();
        }

        @Override
        public void onError(String message) {
         dialog.dismiss();
        }
    };

    private void showNews(List<NewsHeadlines> list) {

            SharedPreferences preferences = getSharedPreferences("credentials",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(list);
            editor.putString("taskList",json);
            editor.apply();

            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(this,1));
            adapter = new CustomAdapter(this,list,this);
            recyclerView.setAdapter(adapter);
    }

    public static boolean isInternetIsConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                return true;

            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                return true;
            }
        } else {
            // not connected to the internet
            return false;
        }
        return false;
    }

    @Override
    public void OnNewsClicked(NewsHeadlines headlines) {
     startActivity(new Intent(MainActivity.this, DetailsActivity.class)
     .putExtra("data",headlines));
    }
}