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
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.smile.R;
import com.example.smile.adapters.MemberListAdapter;
import com.example.smile.adapters.StaffListAdapter;
import com.example.smile.dataObjects.MemberAttendanceModel;
import com.example.smile.dataObjects.MemberListModel;
import com.example.smile.dataObjects.MemberNameModel;
import com.example.smile.dataObjects.PackageModel;
import com.example.smile.dataObjects.StaffListModel;
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

public class Member_ListActivity extends AppCompatActivity implements CategoryCallBackInterface {

    Spinner spinner_memberType,
            spinner_memberName, spinner_selectPackage;
    ArrayList<String> memberTypeList, memberNameList, packageList;
    CoordinatorLayout mCoordinatorLayout;
    Button mSearchButton;
    ProgressBar progressBar;
    RecyclerView mRecyclerView;
    SwipyRefreshLayout pagination;
    int page = 0;
    String Grid_URL = BaseURL + "MemberRegistrationGrid";
    String pacakge_id = "";
    String member_id = "";


    MemberListAdapter adapter;
    ArrayList<MemberListModel> mMemberListmodel;

    String StatusActive = "Active";
    String Active = "Y";

    ArrayList<PackageModel> mPackageArr;
    ArrayList<MemberNameModel> mMemberNameArr, mIndividualArr, mGroupArr;
    ArrayAdapter<String> memberNameAdapater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member__list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setNavigationIcon(R.drawable.backbutton);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            }
        });


        init();
        memberTypeList = new ArrayList<String>();
        mPackageArr = new ArrayList<PackageModel>();
        mMemberNameArr = new ArrayList<MemberNameModel>();
        mIndividualArr = new ArrayList<MemberNameModel>();
        mGroupArr = new ArrayList<MemberNameModel>();

        memberTypeList.add("Select");
        memberTypeList.add("Individual");
        memberTypeList.add("Group");
        ArrayAdapter<String> memberTypeAdapter = new ArrayAdapter<String>(Member_ListActivity.this,
                android.R.layout.simple_list_item_1, memberTypeList);
        memberTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_memberType.setAdapter(memberTypeAdapter);

        memberNameList = new ArrayList<String>();
        memberNameList.add("Select");

        packageList = new ArrayList<String>();
        packageList.add("Select");



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent add_member_details = new Intent(Member_ListActivity.this, Add_Member_DeatailsActivity.class);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(Member_ListActivity.this, R.anim.push_up_in, R.anim.push_up_out);
                startActivity(add_member_details, options.toBundle());

            }
        });


        getData();
        SearchButton();
        Grid(Grid_URL,"","","");
        bindControls();

    }

    public void init() {
        mCoordinatorLayout = findViewById(R.id.coordinatorLayout);
        mSearchButton = findViewById(R.id.searchButton);
        spinner_memberType = findViewById(R.id.memberTypeSpinner);
        spinner_memberName = findViewById(R.id.memberNameSpinner);
        spinner_selectPackage = findViewById(R.id.pacakgeSpinner);

        pagination = findViewById(R.id.pagination);
        mRecyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.pb);

        mMemberListmodel = new ArrayList<MemberListModel>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MemberListAdapter(Member_ListActivity.this, mMemberListmodel);
        mRecyclerView.setAdapter(adapter);

        spinner_memberType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String type = memberTypeList.get(position).toString();
                if(mMemberNameArr != null && mMemberNameArr.size() > 0){
                    mMemberNameArr.clear();
                }
                if(memberNameList != null && memberNameList.size() > 0){
                    memberNameList.clear();
                }
                if(type != null && !type.isEmpty() && type.equalsIgnoreCase("Individual")){
                    mMemberNameArr.addAll(mIndividualArr);
                    for(int i = 0 ; i< mMemberNameArr.size() ; i++){
                        if(mMemberNameArr.get(i).fullname != null && !mMemberNameArr.get(i).fullname.isEmpty() && !mMemberNameArr.get(i).fullname.equalsIgnoreCase("null"))
                            memberNameList.add(mMemberNameArr.get(i).fullname);
                    }
                    if(memberNameAdapater != null)
                        memberNameAdapater.notifyDataSetChanged();
                }else{
                    mMemberNameArr.addAll(mGroupArr);
                    for(int i = 0 ; i< mMemberNameArr.size() ; i++){
                        if(mMemberNameArr.get(i).fullname != null && !mMemberNameArr.get(i).fullname.isEmpty() && !mMemberNameArr.get(i).fullname.equalsIgnoreCase("null"))
                            memberNameList.add(mMemberNameArr.get(i).fullname);
                    }
                    if(memberNameAdapater != null)
                        memberNameAdapater.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner_memberName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(mMemberNameArr != null && mMemberNameArr.size() > 0){
                    member_id = mMemberNameArr.get(position).member_id;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_selectPackage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(mPackageArr != null && mPackageArr.size() > 0){
                    pacakge_id = mPackageArr.get(position).packageId;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void SearchButton() {

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if (validation()) ;

                String memberType = spinner_memberType.getSelectedItem().toString();
                progressBar.setVisibility(View.VISIBLE);
                if(mMemberListmodel != null && mMemberListmodel.size() > 0)
                    mMemberListmodel.clear();

                if(member_id != null && !member_id.isEmpty() && member_id.equals("0")) {
                    member_id = "";
                }

                if(pacakge_id != null && !pacakge_id.isEmpty() && pacakge_id.equals("0")) {
                    pacakge_id = "";
                }

                if(memberType != null && !memberType.isEmpty() && memberType.equals("Select")) {
                    memberType = "";
                }



                Grid(Grid_URL,memberType,member_id,pacakge_id);
            }
        });
    }

    //Code for pagiantion
    private void bindControls() {

        pagination.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                pagination.setRefreshing(false);
                page = page + 1;
                Grid(Grid_URL,"","","");
                pagination.setDirection(SwipyRefreshLayoutDirection.BOTTOM);
            }
        });

    }

    public void getData() {
        try {
            boolean networkConectivity = NetworkConnectivity.isConnected(Member_ListActivity.this);
            if (networkConectivity) {
                progressBar.setVisibility(View.VISIBLE);
                //  ll_progresbar.setVisibility(View.VISIBLE);
                run(BaseURL + "AddMemberAttendance", "MemName");

                    run(BaseURL + "Group", "Packages");
            } else {
                NetworkConnectivity.networkConnetivityShowDialog(Member_ListActivity.this);
            }
        } catch (Exception ex) {
            Log.d("error", ex.toString());
            ex.printStackTrace();
        }
    }

    public void run(String url, final String name) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("jhsj", e.toString());
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {

                final String myResponse = response.body().string();

                Member_ListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        JSONObject forecast = null;
                        try {
                            forecast = new JSONObject(myResponse);
                            if (!name.isEmpty() && name.equals("MemName")) {

                                JSONArray jsonArray = forecast.getJSONArray("MemberName");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    MemberNameModel nameModel = new MemberNameModel();

                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String name = object.getString("fullname");
                                    String id = object.getString("id");
                                    String member_id = object.getString("member_id");
                                    String memberType = object.getString("membertype");
                                    memberNameList.add(name);


                                    nameModel.id= id;
                                    nameModel.fullname = name;
                                    nameModel.member_id = member_id;
                                    nameModel.memberType = memberType;
                                    mMemberNameArr.add(nameModel);

                                    if(memberType != null && !memberType.isEmpty() && memberType.equalsIgnoreCase("Individual")){
                                        mIndividualArr.add(nameModel);
                                    }else{
                                        mGroupArr.add(nameModel);
                                    }



                                }
                                progressBar.setVisibility(View.GONE);
                                MemberNameModel packageModel1 = new MemberNameModel();
                                packageModel1.id = "0";
                                packageModel1.member_id="0";
                                packageModel1.fullname = "Select";
                                mMemberNameArr.add(0, packageModel1);
                                mGroupArr.add(0, packageModel1);
                                mIndividualArr.add(0, packageModel1);
                                memberNameAdapater = new ArrayAdapter<>(Member_ListActivity.this,
                                        android.R.layout.simple_list_item_1, memberNameList);
                                memberNameAdapater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner_memberName.setAdapter(memberNameAdapater);

                            } else {
                                JSONArray jsonArray = forecast.getJSONArray("Packages");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    PackageModel packageModel = new PackageModel();
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String packname = object.getString("onlyname");
                                    String packageId = object.getString("package_id");
                                    packageList.add(packname);
                                    packageModel.packageId = packageId;
                                    packageModel.packageName = packname;

                                    mPackageArr.add(packageModel);

                                }

                                PackageModel packageModel1 = new PackageModel();
                                packageModel1.packageId = "0";
                                packageModel1.packageName = "Select";
                                mPackageArr.add(0, packageModel1);

                                ArrayAdapter<String> packageAdapater = new ArrayAdapter<>(Member_ListActivity.this,
                                        android.R.layout.simple_list_item_1, packageList);
                                packageAdapater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner_selectPackage.setAdapter(packageAdapater);
                            }
                        } catch (Exception e) {
                            progressBar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }

                    }
                });

            }
        });
    }


    ///Get code for Grid
    public void Grid(String url, final String memberType, final String MemberId, final String PackageId) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url)
                .addHeader("Pagenation", String.valueOf(page))
                .addHeader("MemberType", memberType)
                .addHeader("MemberId", MemberId)
                .addHeader("PackageId", PackageId)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                try {
                    progressBar.setVisibility(View.GONE);
                }catch (Exception e1){
                    Log.d("Exception",e1.toString());
                }

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String myResponse = response.body().string();

                Member_ListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        JSONObject forecast = null;
                        try {
                            forecast = new JSONObject(myResponse);
                            JSONArray jsonArray = forecast.getJSONArray("MembRegData");

                            if(jsonArray != null && jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);

                                    String id = object.getString("id");
                                    String punchId = object.getString("member_id");
                                    String staffname = object.getString("fullname");
                                    String phone = object.getString("phone");
                                    String dob = object.getString("dob");
                                    String doj = object.getString("doj");
                                    String reamingAmount = object.getString("remains");
                                    String status = object.getString("status");

                                    String membertype = object.getString("membertype");
                                    String packId = object.getString("packid");
                                    String email = object.getString("emailid");
                                    String state = object.getString("state");
                                    String city = object.getString("city");
                                    String address = object.getString("address");
                                    String batch = object.getString("batchid");
                                    String maritalstatus = object.getString("margstatus");
                                    String dietplan = object.getString("dietplan");
                                    String gender = object.getString("gender");

                                    MemberListModel memberListModel = new MemberListModel();
                                    memberListModel.setId(id);
                                    memberListModel.setPunchId(punchId);
                                    memberListModel.setName(staffname);
                                    memberListModel.setContactno(phone);
                                    memberListModel.setDob(dob);
                                    memberListModel.setDoj(doj);
                                    if (reamingAmount.equals("null")) {
                                        memberListModel.setRemains("");
                                    } else {
                                        memberListModel.setRemains(reamingAmount);
                                    }
                                    memberListModel.setStatus(status);
                                    if (status.equals(Active)) {
                                        memberListModel.setStatus(StatusActive);

                                    }

                                    memberListModel.setPackId(packId);
                                    memberListModel.setMembertype(membertype);
                                    memberListModel.setEmail(email);
                                    memberListModel.setState(state);
                                    memberListModel.setCity(city);
                                    memberListModel.setAddress(address);
                                    memberListModel.setBatchid(batch);
                                    memberListModel.setMargstatus(maritalstatus);
                                    memberListModel.setDietPlan(dietplan);
                                    memberListModel.setGender(gender);

                                    progressBar.setVisibility(View.GONE);

                                    memberListModel.setDoj(doj);
                                    if (!dob.equals("mm/dd/yyyy")) {
                                        memberListModel.setDob(dob);
                                    } else {
                                        memberListModel.setDob("");
                                    }
                                    if (mMemberListmodel != null && mMemberListmodel.size() > 0 && PackageId.isEmpty()) {
                                        int size = mMemberListmodel.size();

                                        memberListModel.setSerialNo(String.valueOf(size + 1));
                                        mMemberListmodel.add(size, memberListModel);
                                    } else if (!PackageId.isEmpty() || !MemberId.isEmpty() || !memberType.isEmpty()) {
                                        if (mMemberListmodel != null && mMemberListmodel.size() > 0) {
                                            mMemberListmodel.clear();
                                        }
                                        mMemberListmodel.add(memberListModel);
                                    } else {

                                        memberListModel.setSerialNo(String.valueOf(i + 1));
                                        mMemberListmodel.add(memberListModel);
                                    }
                                }
                            }else{
                                progressBar.setVisibility(View.GONE);
                            }

                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            progressBar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }


    private boolean validation() {
        boolean flag = true;
       /* if (spinner_memberType.getSelectedItem().toString().trim().equals("Select") ||
                spinner_selectPackage.getSelectedItem().toString().trim().equals("Select")) {
            flag = false;
            Snackbar.make(mCoordinatorLayout, getResources()
                    .getString(R.string.plz_select_membertype_package), Snackbar.LENGTH_SHORT).show();
        }*/
        return flag;
    }

    @Override
    public void onDeleteItemListener(int pos) {
        mMemberListmodel.remove(pos);
        adapter.notifyDataSetChanged();
    }
}


