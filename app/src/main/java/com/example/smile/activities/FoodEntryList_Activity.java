package com.example.smile.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.smile.R;
import com.example.smile.adapters.FoodCategoryListAdapter;
import com.example.smile.adapters.FoodEntryListAdapter;
import com.example.smile.dataObjects.FoodCategoryListModel;
import com.example.smile.dataObjects.FoodEntryListModel;
import com.example.smile.dataObjects.StatusModel;
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

public class FoodEntryList_Activity extends AppCompatActivity  implements CategoryCallBackInterface {

    Spinner StatusSpinner;
    ArrayList<String> mfieldstatusspin;
    CoordinatorLayout mcoordinatelayout;
    Button searchbtn;
    SwipyRefreshLayout pagination;
    int page = 0;
    String Grid_URL = BaseURL+"FoodEntryGrid";
    private ArrayList<FoodEntryListModel> mFoodEntryListArrayList;
    FoodEntryListAdapter adapter;
    RecyclerView mRecyclerView;
    String StatusActive="Active";
    String StatusDeactive="Deactive";
    String Active="Y";
    String Deactive="N";
    ProgressBar progressBar;
    ArrayList<StatusModel> mStatusModel;
    String StatusId= "";
    String StatusType = BaseURL+"StatusTypeDD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_entry_list);
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

        init();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoodEntryList_Activity.this,AddFoodEntry_Activity.class);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(FoodEntryList_Activity.this, R.anim.push_up_in, R.anim.push_up_out);
                startActivity(intent, options.toBundle());
            }
        });

        mStatusModel = new ArrayList<>();
        StatusSpinner = findViewById(R.id.statustype_spinner);
        mfieldstatusspin = new ArrayList<>();
        mfieldstatusspin.add("Select");

        SearchButton();
        boolean networkConectivity =  NetworkConnectivity.isConnected(this);
        if (networkConectivity) {
            progressBar.setVisibility(View.VISIBLE);
            Grid(Grid_URL,"");
        }
        else {
            NetworkConnectivity.networkConnetivityShowDialog(this);

        }

        StatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(mStatusModel != null && mStatusModel.size() > 0)
                    StatusId = mStatusModel.get(position).statusid.toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        run(StatusType);
        bindControls();
    }


    public void init(){
        pagination = findViewById(R.id.pagination);
        progressBar = findViewById(R.id.pb);
        mRecyclerView = findViewById(R.id.recyclerView);
        mFoodEntryListArrayList = new ArrayList<FoodEntryListModel>();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FoodEntryListAdapter
                (FoodEntryList_Activity.this,mFoodEntryListArrayList);
        mRecyclerView.setAdapter(adapter);

    }

    public void SearchButton(){
        mcoordinatelayout = findViewById(R.id.coordinate_layout);
        searchbtn = findViewById(R.id.searchbtn);
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validation()){
                    progressBar.setVisibility(View.VISIBLE);
                    if(mFoodEntryListArrayList != null && mFoodEntryListArrayList.size() > 0)
                        mFoodEntryListArrayList.clear();

                    if(StatusId != null && !StatusId.isEmpty() && StatusId.equals("0")) {
                        StatusId = "";
                    }
                    Grid(Grid_URL,StatusId);
                }
            }
        });
    }

    //Code for Pagination
    private void bindControls(){

        pagination.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                pagination.setRefreshing(false);
                page = page + 1;
                Grid(Grid_URL,"");
                pagination.setDirection(SwipyRefreshLayoutDirection.BOTTOM);
            }
        });



    }

    public  void run(String url){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String myResponse = response.body().string();

                FoodEntryList_Activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //JSONObject for Package
                        try {
                            JSONObject forecast = new JSONObject(myResponse);
                            JSONArray jsonArray = forecast.getJSONArray("StatusType");
                            for (int i = 0; i <jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String statusId = object.getString("id");
                                String statusType = object.getString("statusty");
                                mfieldstatusspin.add(statusType);

                                StatusModel statusModel = new StatusModel();
                                statusModel.statusid = statusId;
                                statusModel.statusty = statusType;

                                mStatusModel.add(statusModel);

                            }

                            StatusModel statusModel = new StatusModel();
                            statusModel.statusty = "Select";
                            statusModel.statusid = "0";

                            mStatusModel.add(0,statusModel);

                            ArrayAdapter<String> spinAdapter = new ArrayAdapter<>(FoodEntryList_Activity.this,
                                    android.R.layout.simple_list_item_1, mfieldstatusspin);
                            spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            StatusSpinner.setAdapter(spinAdapter);




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
        });
    }

    ///Get code for Grid
    public  void Grid(String url, final String statusId){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url)
                .addHeader("Pagenation",String.valueOf(page))
                .addHeader("StatusId",statusId)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String myResponse = response.body().string();

                FoodEntryList_Activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        JSONObject forecast = null;
                        try {
                            forecast = new JSONObject(myResponse);
                            JSONArray jsonArray = forecast.getJSONArray("Entry");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String id = object.getString("id");
                                String profile = object.getString("profile");
                                String foodname = object.getString("fname");
                                String uom = object.getString("uom");
                                String calorie = object.getString("calorie");
                                String description = object.getString("description");
                                String selectFoodCat = object.getString("fcatname");
                                String status = object.getString("status");

                                FoodEntryListModel foodEntryListModel = new FoodEntryListModel();
                                foodEntryListModel.setId(id);
                                foodEntryListModel.setFoodname(foodname);
                                foodEntryListModel.setUom(uom);
                                foodEntryListModel.setCalorie(calorie);
                                foodEntryListModel.setDescription(description);
                                foodEntryListModel.setFoodCatName(selectFoodCat);
                                foodEntryListModel.setStatus(status);
                                if (status.equals(Active)) {
                                    foodEntryListModel.setStatus(StatusActive);

                                } else if (status.equals(Deactive)){
                                    foodEntryListModel.setStatus(StatusDeactive);

                                    //    mStatus.setBackground(getResources().getDrawable(R.drawable.rounded_deactivestatusbtn));

                                }

                                if(mFoodEntryListArrayList != null && mFoodEntryListArrayList.size() > 0){
                                    int size = mFoodEntryListArrayList.size();
                                    foodEntryListModel.setSerial_No(String.valueOf(size + 1));
                                    mFoodEntryListArrayList.add(size, foodEntryListModel);
                                }else  {

                                    foodEntryListModel.setSerial_No(String.valueOf(i+1));
                                    mFoodEntryListArrayList.add(foodEntryListModel);
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
        mFoodEntryListArrayList.remove(pos);
        adapter.notifyDataSetChanged();
    }

    public  boolean Validation(){

        boolean flag = true;
        if (StatusSpinner.getSelectedItem().toString().trim().equals("Select")){
            flag = false;
            Snackbar.make(mcoordinatelayout, "Please Select Status Type",Snackbar.LENGTH_SHORT).show();
        }
        return flag;
    }
}
