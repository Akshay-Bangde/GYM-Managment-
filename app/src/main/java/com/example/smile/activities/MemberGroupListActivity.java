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
import com.example.smile.adapters.MemberGroupListAdapter;
import com.example.smile.dataObjects.MemberGroupListModel;
import com.example.smile.dataObjects.PackageModel;
import com.example.smile.dataObjects.TrainerModel;
import com.example.smile.utils.CategoryCallBackInterface;
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

public class MemberGroupListActivity extends AppCompatActivity implements CategoryCallBackInterface {

    Spinner select_packageSpinner;
    ArrayList<String> packageList;
    ArrayList<PackageModel> mPackageModel;
    private Button mSearchButton;
    CoordinatorLayout mCoordinatorLayout;

    RecyclerView mRecyclerView;
    SwipyRefreshLayout pagination;
    int page = 0;
    MemberGroupListAdapter adapter;
    ArrayList<MemberGroupListModel> mMemberGroupListModel;
    String Grid_URL = BaseURL+"GroupGrid";
    String Package_URL = BaseURL+"Group";
    String pacakageId = "";
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_group_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);toolbar.setTitle(R.string.app_name);
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
                Intent add_group = new Intent(MemberGroupListActivity.this,Add_GroupActivity.class);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(MemberGroupListActivity.this, R.anim.push_up_in, R.anim.push_up_out);
                startActivity(add_group, options.toBundle());

            }
        });

        init();

        bindControls();
        mMemberGroupListModel = new ArrayList<MemberGroupListModel>();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MemberGroupListAdapter(MemberGroupListActivity.this,mMemberGroupListModel);
        mRecyclerView.setAdapter(adapter);

        progressBar.setVisibility(View.VISIBLE);
        Grid(Grid_URL, pacakageId);
        run(Package_URL);

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if(mMemberGroupListModel != null && mMemberGroupListModel.size() > 0)
                    mMemberGroupListModel.clear();

                if(pacakageId != null && !pacakageId.isEmpty() && pacakageId.equals("0")) {
                    pacakageId = "";
                }
                Grid(Grid_URL,pacakageId);
            }
        });

        select_packageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(mPackageModel != null && mPackageModel.size() > 0)
                    pacakageId = mPackageModel.get(position).packageId.toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    public void init(){
        select_packageSpinner = findViewById(R.id.package_spinner);
        mCoordinatorLayout = findViewById(R.id.coordinatorLayout);
        mSearchButton = findViewById(R.id.searchButton);
        mRecyclerView = findViewById(R.id.recyclerView);
        pagination = findViewById(R.id.pagination);
        progressBar = findViewById(R.id.pb);

        packageList = new ArrayList<String>();
        mPackageModel = new ArrayList<>();
        packageList.add("Select");



    }

    //Code for pagiantion
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

    //for Get Spinner data
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

                MemberGroupListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //JSONObject for Package
                        try {
                            JSONObject forecast = new JSONObject(myResponse);
                            JSONArray jsonArray = forecast.getJSONArray("Packages");
                            for (int i = 0; i <jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String packname = object.getString("onlyname");
                                String packageId = object.getString("package_id");
                                packageList.add(packname);

                                PackageModel packageModel = new PackageModel();
                                packageModel.packageName = packname;
                                packageModel.packageId = packageId;

                                mPackageModel.add(packageModel);

                            }

                            PackageModel packageModel = new PackageModel();
                            packageModel.packageName = "Select";
                            packageModel.packageId = "0";

                            mPackageModel.add(0,packageModel);

                            ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(MemberGroupListActivity.this,
                                    android.R.layout.simple_list_item_1,packageList);
                            yearAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                            select_packageSpinner.setAdapter(yearAdapter);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
        });
    }



    ///Get code for Grid
    public  void Grid(String url, final String pacakageId){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url)
                .addHeader("GroupGrid",String.valueOf(page))
                .addHeader("SelectPackId",pacakageId)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                try{
                    progressBar.setVisibility(View.GONE);
                }catch (Exception e1){

                }
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String myResponse = response.body().string();

                MemberGroupListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        JSONObject forecast = null;
                        try {
                            forecast = new JSONObject(myResponse);
                            int code = forecast.getInt("code");
                            if(code != 200){
                                progressBar.setVisibility(View.GONE);
                            }else {
                                JSONArray jsonArray = forecast.getJSONArray("temp");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String id = object.getString("id");
                                    String groupName = object.getString("groupname");
                                    String packName = object.getString("packname");
                                    String totalMembers = object.getString("totalmembers");
                                    String contactpersonName = object.getString("cpersonname");
                                    String mobileNo = object.getString("htmlne");
                                    String email = object.getString("emailid");
                                    String address = object.getString("address");
                                    String state = object.getString("state");
                                    String city = object.getString("city");
                                    String packageCharge = object.getString("traing_amt");
                                    String trainerCheck = object.getString("prsntr");
                                    String trainername = object.getString("trainername");
                                    String trainerCharge = object.getString("pttraing_amt");

                                    progressBar.setVisibility(View.GONE);


                                    MemberGroupListModel memberGroupListModel = new MemberGroupListModel();
                                    memberGroupListModel.setId(id);
                                    memberGroupListModel.setGroupName(groupName);
                                    memberGroupListModel.setPackageName(packName);
                                    memberGroupListModel.setTotalMembers(totalMembers);
                                    memberGroupListModel.setContactPersonName(contactpersonName);
                                    memberGroupListModel.setMobileNo(mobileNo);
                                    memberGroupListModel.setEmail(email);
                                    memberGroupListModel.setAddress(address);
                                    memberGroupListModel.setState(state);
                                    memberGroupListModel.setCity(city);
                                    memberGroupListModel.setPackageCharge(packageCharge);
                                    memberGroupListModel.setTrainerCheck(trainerCheck);
                                    memberGroupListModel.setTrainerName(trainername);
                                    memberGroupListModel.setTrainerCharge(trainerCharge);


                                    if (mMemberGroupListModel != null && mMemberGroupListModel.size() > 0 && pacakageId.isEmpty()) {
                                        int size = mMemberGroupListModel.size();

                                        memberGroupListModel.setSerialNo(String.valueOf(size + 1));
                                        mMemberGroupListModel.add(size, memberGroupListModel);
                                    } else if (!pacakageId.isEmpty()) {
                                        if (mMemberGroupListModel != null && mMemberGroupListModel.size() > 0) {
                                            mMemberGroupListModel.clear();
                                        }
                                        mMemberGroupListModel.add(memberGroupListModel);
                                    } else {

                                        memberGroupListModel.setSerialNo(String.valueOf(i + 1));
                                        mMemberGroupListModel.add(memberGroupListModel);
                                    }
                                }

                                adapter.notifyDataSetChanged();
                            }

                        } catch (JSONException e) {
                            progressBar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }



    public boolean validation(){
        boolean flag=true;
        if (select_packageSpinner.getSelectedItem().toString().trim().equals("Select *")){
            flag=false;
            Snackbar.make(mCoordinatorLayout,getResources().getString
                    (R.string.plz_select_package),Snackbar.LENGTH_SHORT).show();
        }
        return flag;
    }

    @Override
    public void onDeleteItemListener(int pos) {
        mMemberGroupListModel.remove(pos);
        adapter.notifyDataSetChanged();
    }
}
