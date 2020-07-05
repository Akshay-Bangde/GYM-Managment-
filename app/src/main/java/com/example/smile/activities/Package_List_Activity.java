package com.example.smile.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.smile.R;
import com.example.smile.adapters.GYMPackageListAdapter;
import com.example.smile.dataObjects.GYMPackageListModel;
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

public class Package_List_Activity extends AppCompatActivity implements CategoryCallBackInterface {
    SwipyRefreshLayout pagination;
    int page = 0;
    String substring = "";
    String Grid_URL = BaseURL + "PackageGrid";
    private ArrayList<GYMPackageListModel> mGYMPackageListModel;
    GYMPackageListAdapter adapter;
    RecyclerView mRecyclerView;
    EditText mSearchEdt;
    Button mSearchBtn;

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package_list);
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
                Intent intent = new Intent(Package_List_Activity.this, Add_Package_Activity.class);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(Package_List_Activity.this, R.anim.push_up_in, R.anim.push_up_out);
                startActivity(intent, options.toBundle());
            }
        });
        progressBar = findViewById(R.id.pb);
        pagination = findViewById(R.id.pagination);
        mRecyclerView = findViewById(R.id.recyclerView);
        mGYMPackageListModel = new ArrayList<GYMPackageListModel>();
        mSearchBtn = findViewById(R.id.searchBtn);
        mSearchEdt = findViewById(R.id.searchEdt);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GYMPackageListAdapter
                (Package_List_Activity.this, mGYMPackageListModel);
        mRecyclerView.setAdapter(adapter);

        bindControls();


        boolean networkConectivity =  NetworkConnectivity.isConnected(this);
        if (networkConectivity) {
            progressBar.setVisibility(View.VISIBLE);
            Grid(Grid_URL,"");
        }
        else {
            NetworkConnectivity.networkConnetivityShowDialog(this);

        }
        ClickHandler();

    }

    public void ClickHandler(){
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = mSearchEdt.getText().toString();
                Grid(Grid_URL, value);

            }
        });
    }

    //Code for Pagination
    private void bindControls() {

        pagination.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                pagination.setRefreshing(false);
                page = page + 1;
                Grid(Grid_URL,"");
                pagination.setDirection(SwipyRefreshLayoutDirection.BOTTOM);
            }
        });

        mSearchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()<=0){
                    Grid(Grid_URL,"");
                    if(mGYMPackageListModel != null && mGYMPackageListModel.size() > 0)
                    mGYMPackageListModel.clear();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    ///Get code for Grid
    public void Grid(String url, final String substring) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url)
                .addHeader("Pagenation", String.valueOf(page))
                .addHeader("Substring", substring)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String myResponse = response.body().string();

                Package_List_Activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        JSONObject forecast = null;
                        try {
                            forecast = new JSONObject(myResponse);
                            JSONArray jsonArray = forecast.getJSONArray("PackData");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String id = object.getString("id");
                                String packagename = object.getString("packname");
                                String details = object.getString("details");
                                String validity = object.getString("duration");
                                String packageamount = object.getString("charges");
                                String activityamount = object.getString("total_actamount");
                                String totalamount = object.getString("total_amount");

                                GYMPackageListModel gymPackageListModel = new GYMPackageListModel();
                                gymPackageListModel.setId(id);
                                gymPackageListModel.setPackagename(packagename);
                                gymPackageListModel.setDetails(details);
                                gymPackageListModel.setDuration(validity);
                                gymPackageListModel.setPackageamount(packageamount);
                                gymPackageListModel.setActivityamount(activityamount);
                                gymPackageListModel.setTotalamount(totalamount);


                                if (substring.isEmpty() && mGYMPackageListModel != null && mGYMPackageListModel.size() > 0) {
                                    int size = mGYMPackageListModel.size();
                                    gymPackageListModel.setSerial_no(String.valueOf(size + 1));
                                    mGYMPackageListModel.add(size, gymPackageListModel);
                                } else {
                                     if(!substring.isEmpty()){
                                         if(mGYMPackageListModel != null && mGYMPackageListModel.size() > 0)
                                         mGYMPackageListModel.clear();
                                     }
                                    gymPackageListModel.setSerial_no(String.valueOf(i + 1));
                                    mGYMPackageListModel.add(gymPackageListModel);
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

    @Override
    public void onDeleteItemListener(int pos) {
        mGYMPackageListModel.remove(pos);
        adapter.notifyDataSetChanged();
    }
}
