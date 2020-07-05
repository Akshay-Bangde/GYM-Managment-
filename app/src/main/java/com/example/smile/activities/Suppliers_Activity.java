package com.example.smile.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.TextView;

import com.example.smile.R;
import com.example.smile.adapters.SuppliersListAdapter;
import com.example.smile.dataObjects.PackageModel;
import com.example.smile.dataObjects.StatusModel;
import com.example.smile.dataObjects.SuppliersListModel;
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

public class Suppliers_Activity extends AppCompatActivity implements CategoryCallBackInterface {
    Spinner mType;
    SwipyRefreshLayout pagination;
    int page = 0;
    String StatusId= "";
    RecyclerView mRecyclerView;
    SuppliersListAdapter adapter;
    String Grid_URL = BaseURL+"SupplireListGrid";
    String StatusType = BaseURL+"StatusTypeDD";
    private ArrayList<SuppliersListModel> mSuppliersListModel;

    String StatusActive="Active";
    String StatusDeactive="Deactive";
    String Active="Y";
    String Deactive="N";
    Button mSearchButton;
    ProgressBar progressBar;
    ArrayList<StatusModel> mStatusModel;
    ArrayList<String> batchList ;
    //TextView mStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suppliers);
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

        //mStatus = findViewById(R.id.SALARY);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent add_group = new Intent(Suppliers_Activity.this,Add_Supplier_Activity.class);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(Suppliers_Activity.this, R.anim.push_up_in, R.anim.push_up_out);
                startActivity(add_group, options.toBundle());


            }
        });
        mStatusModel = new ArrayList<>();
        pagination = findViewById(R.id.pagination);
        mRecyclerView = findViewById(R.id.recyclerView);
        mSearchButton = findViewById(R.id.searchButton);
        progressBar = findViewById(R.id.pb);
        mSuppliersListModel = new ArrayList<SuppliersListModel>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(Suppliers_Activity.this));
        adapter = new SuppliersListAdapter(Suppliers_Activity.this,mSuppliersListModel);
        mRecyclerView.setAdapter(adapter);

        init();
        boolean networkConectivity =  NetworkConnectivity.isConnected(this);
        if (networkConectivity) {
            progressBar.setVisibility(View.VISIBLE);
            Grid(Grid_URL,"");
        }
        else {
            NetworkConnectivity.networkConnetivityShowDialog(this);

        }

        bindControls();


        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if(mSuppliersListModel != null && mSuppliersListModel.size() > 0)
                    mSuppliersListModel.clear();

                if(StatusId != null && !StatusId.isEmpty() && StatusId.equals("0")) {
                    StatusId = "";
                }
                Grid(Grid_URL,StatusId);
            }
        });

        mType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    }

    public  void init(){
        mType = findViewById(R.id.type_spinner);
        batchList = new ArrayList<String>();
        batchList.add("Select");


    }

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

                Suppliers_Activity.this.runOnUiThread(new Runnable() {
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
                                batchList.add(statusType);

                                StatusModel statusModel = new StatusModel();
                                statusModel.statusid = statusId;
                                statusModel.statusty = statusType;

                                mStatusModel.add(statusModel);

                            }

                            StatusModel statusModel = new StatusModel();
                            statusModel.statusty = "Select";
                            statusModel.statusid = "0";

                            mStatusModel.add(0,statusModel);
                            ArrayAdapter<String> batchAdapter = new ArrayAdapter<String>(Suppliers_Activity.this,
                                    android.R.layout.simple_list_item_1, batchList);
                            batchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            mType.setAdapter(batchAdapter);



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

                Suppliers_Activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        JSONObject forecast = null;
                        try {
                            forecast = new JSONObject(myResponse);
                            // JSONObject data = forecast.getJSONObject("data");
                            JSONArray jsonArray = forecast.getJSONArray("Supiler");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);


                                String id = object.getString("id");
                                String suppliersName = object.getString("name");
                                String phone = object.getString("contact");
                                String email = object.getString("email");
                                String gstNo = object.getString("gstin");
                                String stateCode = object.getString("statecode");
                                String city = object.getString("city");
                                String status = object.getString("status");

                                SuppliersListModel suppliersListModel = new SuppliersListModel();
                                suppliersListModel.setId(id);
                                suppliersListModel.setSupplierName(suppliersName);
                                suppliersListModel.setPhone(phone);
                                suppliersListModel.setEmail(email);
                                suppliersListModel.setGstNo(gstNo);
                                suppliersListModel.setStateCode(stateCode);
                                suppliersListModel.setCity(city);
                                suppliersListModel.setStatus(status);
                                if (status.equals(Active)){
                                    suppliersListModel.setStatus(StatusActive);

                                  // mStatus.setBackground(getResources().getDrawable(R.drawable.rounded_activestatusbtn));

                                }
                                else if (status.equals(Deactive)){
                                    suppliersListModel.setStatus(StatusDeactive);

                                //    mStatus.setBackground(getResources().getDrawable(R.drawable.rounded_deactivestatusbtn));

                                }

                                if(mSuppliersListModel != null && mSuppliersListModel.size() > 0){
                                    int size = mSuppliersListModel.size();
                                    suppliersListModel.setSerialNo(String.valueOf(size + 1));
                                    mSuppliersListModel.add(size, suppliersListModel);
                                }else  {

                                    suppliersListModel.setSerialNo(String.valueOf(i+1));
                                    mSuppliersListModel.add(suppliersListModel);
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
        mSuppliersListModel.remove(pos);
        adapter.notifyDataSetChanged();
    }

}
