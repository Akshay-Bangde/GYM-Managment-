package com.example.smile.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.example.smile.R;
import com.example.smile.adapters.GYMActivityListAdapter;
import com.example.smile.dataObjects.ActivityListModel;
import com.example.smile.utils.CategoryCallBackInterface;
import com.example.smile.utils.NetworkConnectivity;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.smile.activities.SignInActivity.BaseURL;

public class GymActivityList_Activity extends AppCompatActivity  implements CategoryCallBackInterface {
    RecyclerView mRecyclerView;
    private ArrayList<ActivityListModel> mActivityPackageArrayList;
    SwipyRefreshLayout pagination;
    int page = 0;

    GYMActivityListAdapter adapter;

    String Grid_URL = BaseURL+"VMActivityGrid";

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gymactivitylist_);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backbutton);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GymActivityList_Activity.this, AddGymActivity_Activity.class);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(GymActivityList_Activity.this, R.anim.push_up_in, R.anim.push_up_out);
                startActivity(intent, options.toBundle());


            }
        });



        mRecyclerView = findViewById(R.id.recyclerView);
        pagination = findViewById(R.id.pagination);

        progressBar = findViewById(R.id.pb);
        mActivityPackageArrayList = new ArrayList<ActivityListModel>();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
         adapter = new GYMActivityListAdapter
                (GymActivityList_Activity.this, mActivityPackageArrayList);
        mRecyclerView.setAdapter(adapter);



        boolean networkConectivity =  NetworkConnectivity.isConnected(this);
        if (networkConectivity) {
            progressBar.setVisibility(View.VISIBLE);
            Grid(Grid_URL);
        }
        else {
            NetworkConnectivity.networkConnetivityShowDialog(this);

        }
        bindControls();
    }
    private void bindControls(){

        pagination.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                pagination.setRefreshing(false);
                page = page + 1;
                Grid(Grid_URL);
                pagination.setDirection(SwipyRefreshLayoutDirection.BOTTOM);
            }
        });

    }

    @Override
    public void onDeleteItemListener(int pos) {
        mActivityPackageArrayList.remove(pos);
        adapter.notifyDataSetChanged();
    }

    ///Get code for Grid
    public  void Grid(String url){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url)
                .addHeader("Pagenation",String.valueOf(page))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String myResponse = response.body().string();

                GymActivityList_Activity.this.runOnUiThread(
                        new Runnable() {
                    @Override
                    public void run() {


                        JSONObject forecast = null;
                        try {
                            forecast = new JSONObject(myResponse);
                            // JSONObject data = forecast.getJSONObject("data");
                            JSONArray jsonArray = forecast.getJSONArray("ActivityData");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);


                                String id = object.getString("id");
                                String activityname = object.getString("actvtname");
                                String charges = object.getString("charges");
                                String details = object.getString("details");

                                ActivityListModel activityListModel = new ActivityListModel();
                                activityListModel.setId(id);
                                activityListModel.setActivityname(activityname);
                                activityListModel.setCharge(charges);
                                if (!details.equals("null"))
                                activityListModel.setDetails(details);

                                if(mActivityPackageArrayList != null && mActivityPackageArrayList.size() > 0){
                                    int size = mActivityPackageArrayList.size();
                                    activityListModel.setSerial_no(String.valueOf(size+1));
                                    mActivityPackageArrayList.add(size, activityListModel);
                                }else  {

                                    activityListModel.setSerial_no(String.valueOf(i+1));
                                    mActivityPackageArrayList.add(activityListModel);
                                }
                            }

                            adapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }



}
