package com.example.smile.activities;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.smile.R;
import com.example.smile.adapters.SalaryListAdapter;
import com.example.smile.dataObjects.PackageModel;
import com.example.smile.dataObjects.SalaryListModel;
import com.example.smile.dataObjects.StaffModel;
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

public class Salary_List_Activity extends AppCompatActivity {

    Spinner staffNameSpinner;
    ArrayList<String> mstaffName;

    private Button mSearchButton;
    CoordinatorLayout mCoordinatorLayout;
    RecyclerView mRecyclerView;
    SwipyRefreshLayout pagination;
    int page = 0;
    ArrayList<SalaryListModel> mSalaryListModel;
    SalaryListAdapter salaryListAdapter;
    String Grid_URL = BaseURL + "VMStaffSalaryGrid";
    String run_URL = BaseURL + "AddStaffAttendance";
    ProgressBar progressBar;

    ArrayList<StaffModel> mStaffnameModel;
    String staffName_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary__list);
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
        getData();

        Grid(Grid_URL,"");
        bindControls();
        SearchButton();

        mStaffnameModel = new ArrayList<>();
        mstaffName = new ArrayList<>();
        mstaffName.add("Select");


        staffNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(mStaffnameModel != null && mStaffnameModel.size() > 0)
                    staffName_id = mStaffnameModel.get(position).staff_id.toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void init() {
        mCoordinatorLayout = findViewById(R.id.coordinatorLayout);
        staffNameSpinner = findViewById(R.id.staffNameSpinner);
        mSearchButton = findViewById(R.id.searchButton);
        pagination = findViewById(R.id.pagination);
        progressBar = findViewById(R.id.pb);
        pagination = findViewById(R.id.pagination);
        mSalaryListModel = new ArrayList<SalaryListModel>();
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        salaryListAdapter = new SalaryListAdapter(Salary_List_Activity.this, mSalaryListModel);
        mRecyclerView.setAdapter(salaryListAdapter);

    }

    public void getData(){
        try{
            boolean networkConectivity =  NetworkConnectivity.isConnected(Salary_List_Activity.this);
            if (networkConectivity) {
                progressBar.setVisibility(View.VISIBLE);
                run(run_URL);
            }else{
                NetworkConnectivity.networkConnetivityShowDialog(Salary_List_Activity.this);
            }
        }

        catch (Exception ex){
            Log.d("error", ex.toString());
            ex.printStackTrace();
        }
    }
    public void SearchButton() {
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean networkConectivity =  NetworkConnectivity.isConnected(Salary_List_Activity.this);
                if (networkConectivity) {
                    if (validation()) {
                        progressBar.setVisibility(View.VISIBLE);
                        if(mSalaryListModel != null && mSalaryListModel.size() > 0)
                            mSalaryListModel.clear();

                        if(staffName_id != null && !staffName_id.isEmpty() && staffName_id.equals("0")) {
                            staffName_id = "";
                        }
                        Grid(Grid_URL,staffName_id);

                    }
                }
                else{
                    NetworkConnectivity.networkConnetivityShowDialog(Salary_List_Activity.this);
                }

            }
        });
    }

    public void run(String url){

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
                Salary_List_Activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject forecast = null;
                        try {
                            forecast = new JSONObject(myResponse);
                            JSONArray jsonArray = forecast.getJSONArray("StaffName");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String staffId = object.getString("staff_id");
                                String staffName = object.getString("fullname");
                                mstaffName.add(staffName);


                                StaffModel staffNameModel = new StaffModel();
                                staffNameModel.staff_id= staffId;
                                staffNameModel.fullname = staffName;
                                mStaffnameModel.add(staffNameModel);

                            }

                            /*StaffModel staffNameModel = new StaffModel();
                            staffNameModel.fullname = "Select";
                            staffNameModel.staff_id = "0";
                            mStaffnameModel.add(0,staffNameModel);
*/
                            ArrayAdapter<String> stypeAdapter = new ArrayAdapter<>(Salary_List_Activity.this,
                                    android.R.layout.simple_list_item_1, mstaffName);
                            stypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            staffNameSpinner.setAdapter(stypeAdapter);
                        }

                        catch (Exception e){
                            progressBar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }

                    }
                });

            }
        });
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

    ///Get code for Grid
    public  void Grid(String url, final String staff_id){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url)
                .addHeader("Pagenation",String.valueOf(page))
                .addHeader("StaffId",staff_id)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String myResponse = response.body().string();

                Salary_List_Activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        JSONObject forecast = null;
                        try {
                            forecast = new JSONObject(myResponse);
                            JSONArray jsonArray = forecast.getJSONArray("salDetails");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                String id = object.getString("id");
                                String staffId = object.getString("staff_id");
                                String staffname = object.getString("fullname");
                                String doj = object.getString("doj");
                                String phone = object.getString("phone");
                                String empType = object.getString("emptype");
                                String salary = object.getString("empsalary");
                                String trainingCharge = object.getString("traincharge");


                                SalaryListModel salaryListModel = new SalaryListModel();
                                salaryListModel.setId(id);
                                salaryListModel.setStaffId(staffId);
                                salaryListModel.setName(staffname);
                                salaryListModel.setDoj(doj);
                                salaryListModel.setContactno(phone);
                                salaryListModel.setEmployeeType(empType);
                                salaryListModel.setSalary(salary);
                                salaryListModel.setTrainingCharge(trainingCharge);
                                progressBar.setVisibility(View.GONE);

                                if(mSalaryListModel != null && mSalaryListModel.size() > 0){
                                    int size = mSalaryListModel.size();
                                    salaryListModel.setSerialNo(String.valueOf(size+1));
                                    mSalaryListModel.add(size, salaryListModel);
                                }else  {

                                    salaryListModel.setSerialNo(String.valueOf(i+1));
                                    mSalaryListModel.add(salaryListModel);
                                }
                            }

                            salaryListAdapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public boolean validation() {
        if (staffNameSpinner.getSelectedItem().toString().trim().equals("Select")) {
            Snackbar.make(mCoordinatorLayout, getResources().getString(R.string.plzSelectStaffname), Snackbar.LENGTH_SHORT).show();
        }
        return true;
    }
}
