package com.example.smile.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.example.smile.R;
import com.example.smile.adapters.PackageActivityDurationAdapter;
import com.example.smile.adapters.StaffListAdapter;
import com.example.smile.dataObjects.PackageActivityDurationModel;
import com.example.smile.dataObjects.StaffListModel;
import com.example.smile.dataObjects.StaffModel;
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

public class Trainer_Employee_Activity extends AppCompatActivity implements CategoryCallBackInterface {

    Spinner Spinner_staff_type, Spinner_staff_name;
    ArrayAdapter<String> stypeAdapter;
    Button mSearchBtn;
    RecyclerView mRecyclerView;
    SwipyRefreshLayout pagination;
    int page = 0;
    String Grid_URL = BaseURL+"StaffGrid";
    ArrayList<StaffModel> mAccountantList, mEmployeeList, mReceptionist, mTrainerList;
    StaffListAdapter adapter;

    ArrayList<StaffListModel> mStaffListModel;

    ArrayList<String> mstaffName, mstaffType;
    ArrayList<StaffModel> mStaffModelList;
    String staffTypeVal = "";
    String staffNameVal = "";
    ProgressBar progressBar;
    //LinearLayout ll_progresbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trainer__employee);
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
        progressBar = findViewById(R.id.pb);
        // ll_progresbar  = findViewById(R.id.ll_progresbar);


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent add_staff = new Intent(Trainer_Employee_Activity.this, Add_Staff_Activity.class);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(Trainer_Employee_Activity.this, R.anim.push_up_in, R.anim.push_up_out);
                startActivity(add_staff, options.toBundle());



            }
        });

        init();
        getData();
        Grid(Grid_URL,"","");

        mstaffName = new ArrayList<>();
        mStaffModelList = new ArrayList<>();
        mstaffName.add("Select");

        mstaffType = new ArrayList<>();
        mstaffType.add("Select");


      /*  mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Trainer_Employee_Activity.this,"This is test",Toast.LENGTH_SHORT);
                *//*progressBar.setVisibility(View.VISIBLE);
                Grid(Grid_URL,staffTypeVal,staffNameVal);*//*
            }
        });*/

        Spinner_staff_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (mstaffType != null && mstaffType.size() > 0) {
                        staffTypeVal = mstaffType.get(position).toString();

                        if (staffTypeVal.equals("Accountant")) {
                            if (mstaffName != null && mstaffName.size() > 0) {
                                mstaffName.clear();
                            }
                            for (int i = 0; i < mAccountantList.size(); i++) {
                                mstaffName.add(mAccountantList.get(i).fullname);
                            }

                            stypeAdapter.notifyDataSetChanged();

                        } else if (staffTypeVal.equals("Employee")) {
                            if (mstaffName != null && mstaffName.size() > 0) {
                                mstaffName.clear();
                            }
                            for (int i = 0; i < mEmployeeList.size(); i++) {
                                mstaffName.add(mEmployeeList.get(i).fullname);
                            }

                            stypeAdapter.notifyDataSetChanged();

                        } else if (staffTypeVal.equals("Receptionist")) {
                            if (mstaffName != null && mstaffName.size() > 0) {
                                mstaffName.clear();
                            }
                            for (int i = 0; i < mReceptionist.size(); i++) {
                                mstaffName.add(mReceptionist.get(i).fullname);
                            }

                            stypeAdapter.notifyDataSetChanged();

                        } else if (staffTypeVal.equals("Trainer")) {
                            if (mstaffName != null && mstaffName.size() > 0) {
                                mstaffName.clear();
                            }
                            for (int i = 0; i < mTrainerList.size(); i++) {
                                mstaffName.add(mTrainerList.get(i).fullname);
                            }
                            stypeAdapter.notifyDataSetChanged();

                        }
                    }
                }catch (Exception e){
                    Log.d("exception",e.toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner_staff_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(mStaffModelList != null && mStaffModelList.size()>0)

                    if (staffTypeVal.equals("Accountant")) {
                        staffNameVal =mAccountantList.get(position).staff_id.toString();

                    } else if (staffTypeVal.equals("Employee")) {
                        staffNameVal =mEmployeeList.get(position).staff_id.toString();

                    } else if (staffTypeVal.equals("Receptionist")) {

                        staffNameVal =mReceptionist.get(position).staff_id.toString();
                    } else if (staffTypeVal.equals("Trainer")) {
                        staffNameVal =mTrainerList.get(position).staff_id.toString();
                    }else{
                        staffNameVal = "0";
                    }





            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if(staffNameVal != null && !staffNameVal.isEmpty() && staffNameVal.equals("0")){
                    staffNameVal= "";
                }
                if(staffTypeVal != null && !staffTypeVal.isEmpty() && staffTypeVal.equals("Select")){
                    staffTypeVal ="";
                }
                if(staffTypeVal.isEmpty() && staffNameVal.isEmpty()){
                    if(mStaffListModel != null && mStaffListModel.size() > 0)
                        mStaffListModel.clear();
                }
                Grid(Grid_URL,staffTypeVal,staffNameVal);

            }
        });


        bindControls();


    }



    public void getData(){
        try{
            boolean networkConectivity =  NetworkConnectivity.isConnected(Trainer_Employee_Activity.this);
            if (networkConectivity) {
                progressBar.setVisibility(View.VISIBLE);
                //  ll_progresbar.setVisibility(View.VISIBLE);
                run(BaseURL+"StaffReg", "staff");

                run(BaseURL+"AddStaffAttendance", "staffName");
            }else{
                NetworkConnectivity.networkConnetivityShowDialog(Trainer_Employee_Activity.this);
            }
        }

        catch (Exception ex){
            Log.d("error", ex.toString());
            ex.printStackTrace();
        }
    }

    public void run(String url, final String name){

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("jhsj", e.toString());
                progressBar.setVisibility(View.GONE);
                //ll_progresbar.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {

                final String myResponse = response.body().string();

                Trainer_Employee_Activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        JSONObject forecast = null;
                        try {
                            forecast = new JSONObject(myResponse);
                            if (!name.isEmpty() && name.equals("staff")) {

                                JSONArray jsonArray = forecast.getJSONArray("StaffType");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String stafftype = object.getString("Type");
                                    mstaffType.add(stafftype);

                                }
                                progressBar.setVisibility(View.GONE);
                                //  ll_progresbar.setVisibility(View.GONE);
                                ArrayAdapter<String> stype1Adapter = new ArrayAdapter<>(Trainer_Employee_Activity.this,
                                        android.R.layout.simple_list_item_1, mstaffType);
                                stype1Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                Spinner_staff_type.setAdapter(stype1Adapter);
                            }
                            else {
                                JSONArray jsonArray = forecast.getJSONArray("StaffName");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    StaffModel staffModel = new StaffModel();

                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String stafftype = object.getString("fullname");
                                    String id = object.getString("id");
                                    String staff_id = object.getString("staff_id");
                                    String phone = object.getString("phone");
                                    String employeeName = object.getString("emptype");



                                    staffModel.id = id;
                                    staffModel.emptype = employeeName;
                                    staffModel.fullname = stafftype;
                                    staffModel.phone =phone;
                                    staffModel.staff_id = staff_id;
                                    mStaffModelList.add(staffModel);
                                    if(!employeeName.isEmpty() && employeeName.equals("Trainer")){
                                        mTrainerList.add(staffModel);
                                    } else if(!employeeName.isEmpty() && employeeName.equals("Receptionist")){
                                        mReceptionist.add(staffModel);
                                    } else if(!employeeName.isEmpty() && employeeName.equals("Employee")){
                                        mEmployeeList.add(staffModel);
                                    } else if(!employeeName.isEmpty() && employeeName.equals("Accountant")){
                                        mAccountantList.add(staffModel);
                                    }



                                    mstaffName.add(stafftype);

                                }
                                StaffModel staffModel1= new StaffModel();
                                staffModel1.id= "0";
                                staffModel1.fullname="Select";
                                staffModel1.staff_id = "0";
                                mStaffModelList.add(0,staffModel1);

                                mTrainerList.add(0,staffModel1);
                                mEmployeeList.add(0,staffModel1);
                                mAccountantList.add(0,staffModel1);
                                mReceptionist.add(0,staffModel1);



                                stypeAdapter = new ArrayAdapter<>(Trainer_Employee_Activity.this,
                                        android.R.layout.simple_list_item_1, mstaffName);
                                stypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                Spinner_staff_name.setAdapter(stypeAdapter);
                            }
                        }

                        catch (Exception e){
                            progressBar.setVisibility(View.GONE);
                            // ll_progresbar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }

                    }
                });

            }
        });
    }




    public void init(){

        mAccountantList = new ArrayList<>();
        mEmployeeList = new ArrayList<>();
        mTrainerList = new ArrayList<>();
        mReceptionist = new ArrayList<>();
        Spinner_staff_type=findViewById(R.id.staffType);
        Spinner_staff_name=findViewById(R.id.staffName);
        mSearchBtn = findViewById(R.id.search_btn);
        pagination = findViewById(R.id.pagination);
        mStaffListModel = new ArrayList<StaffListModel>();
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StaffListAdapter(Trainer_Employee_Activity.this,mStaffListModel);
        mRecyclerView.setAdapter(adapter);

    }

    //Code for pagiantion
    private void bindControls(){
/*

        Spinner_staff_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(mstaffType != null && mstaffType.size()>0)
                    staffTypeVal = mstaffType.get(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/

       /* Spinner_staff_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(mStaffModelList != null && mStaffModelList.size()>0)
                    staffNameVal =mStaffModelList.get(position).staff_id.toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/



        pagination.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                pagination.setRefreshing(false);
                page = page + 1;
                Grid(Grid_URL,"","");
                pagination.setDirection(SwipyRefreshLayoutDirection.BOTTOM);
            }
        });

    }

    ///Get code for Grid
    public  void Grid(String url, String staffType, final String staffId){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url)
                .addHeader("Pagenation",String.valueOf(page))
                .addHeader("StaffType",staffType)
                .addHeader("StaffId",staffId)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                try {
                    progressBar.setVisibility(View.GONE);
                }catch (Exception e1){

                }

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String myResponse = response.body().string();

                Trainer_Employee_Activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        JSONObject forecast = null;
                        try {
                            forecast = new JSONObject(myResponse);
                            JSONArray jsonArray = forecast.getJSONArray("StaDetails");
                            progressBar.setVisibility(View.GONE);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                String id = object.getString("id");

                                JSONObject Detailsobject = object.getJSONObject("Details");
                                String punchId = Detailsobject.getString("staff_id");
                                String staffname = Detailsobject.getString("fullname");

                                String phone = object.getString("phone");
                                String empType = object.getString("emptype");


                                String email = object.getString("emailid");
                                String state = object.getString("state");
                                String city = object.getString("city");
                                String address = object.getString("address");
                                String biomatricId = object.getString("biomatricid");

                                String salary = object.getString("empsalary");
                                String trainingCharge = object.getString("traincharge");
                                String dob = object.getString("dob");
                                String doj = object.getString("doj");
                                String gender = object.getString("gender");


                                StaffListModel staffListModel = new StaffListModel();
                                staffListModel.setId(id);
                                staffListModel.setPunchId(punchId);
                                staffListModel.setName(staffname);
                                staffListModel.setContactno(phone);
                                staffListModel.setEmployeeType(empType);
                                staffListModel.setEmail(email);
                                staffListModel.setState(state);
                                staffListModel.setCity(city);
                                staffListModel.setAddress(address);
                                staffListModel.setBioMetricId(biomatricId);
                                if (!biomatricId.equals("null")){
                                    staffListModel.setBioMetricId(biomatricId);
                                }
                                else{
                                    staffListModel.setBioMetricId("0");
                                }

                                // ll_progresbar.setVisibility(View.GONE);
                                staffListModel.setSalary(salary);
                                staffListModel.setTrainingCharge(trainingCharge);
                                staffListModel.setDoj(doj);
                                if (!dob.equals("mm/dd/yyyy")){
                                    staffListModel.setDob(dob);
                                }
                                else{
                                    staffListModel.setDob("");
                                }

                                if(mStaffListModel != null && mStaffListModel.size() > 0 && staffId.isEmpty()){
                                    int size = mStaffListModel.size();

                                    staffListModel.setSerialNo(String.valueOf(size+1));
                                    mStaffListModel.add(size, staffListModel);
                                }else  if(!staffId.isEmpty()) {
                                    if(mStaffListModel != null && mStaffListModel.size() > 0){
                                        mStaffListModel.clear();
                                    }
                                    mStaffListModel.add(staffListModel);
                                }else{
                                    staffListModel.setSerialNo(String.valueOf(i+1));
                                    mStaffListModel.add(staffListModel);
                                }
                            }

                            adapter.notifyDataSetChanged();

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
        mStaffListModel.remove(pos);
        adapter.notifyDataSetChanged();
    }
}
