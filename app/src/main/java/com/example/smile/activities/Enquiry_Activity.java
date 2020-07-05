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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.example.smile.R;
import com.example.smile.adapters.Enquiry_Adapter;
import com.example.smile.dataObjects.Enquiry_Model;
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

public class Enquiry_Activity extends AppCompatActivity implements CategoryCallBackInterface {

    RecyclerView eRecyclerView;
    ArrayList<Enquiry_Model> enquiryModelArrayList;
    int page=0;
    Enquiry_Adapter enquiryAdapter;
    String enqurl=BaseURL+"InquiryGrid";
    SwipyRefreshLayout enquirypagination;
    String mNull="null";
    ProgressBar progressBar;
    Spinner statusType;
    ArrayList<String> mstatusTypeArrayList;
    Button searchButton;
    String StatusType = "" ;
    String statusfound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enquiry);
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


        searchButton = findViewById(R.id.enq_searchButton);
        statusType = findViewById(R.id.statusType);
        mstatusTypeArrayList = new ArrayList<>();
        mstatusTypeArrayList.add("Select");
        mstatusTypeArrayList.add("Follow Up ");
        mstatusTypeArrayList.add("Done");
        mstatusTypeArrayList.add("Reject");
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(Enquiry_Activity.this,
                android.R.layout.simple_list_item_1, mstatusTypeArrayList);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusType.setAdapter(spinAdapter);


        eRecyclerView =  findViewById(R.id.enq_recyclerview);
        enquirypagination=findViewById(R.id.enquirypagn);
        progressBar = findViewById(R.id.pb);
        FloatingActionButton enqfab=findViewById(R.id.fab);
        enqfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Enquiry_Activity.this, Add_Enquiry_Activity.class);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(Enquiry_Activity.this, R.anim.push_up_in, R.anim.push_up_out);
                startActivity(intent, options.toBundle());

            }
        });

        enquiryModelArrayList = new ArrayList<Enquiry_Model>();
        eRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        enquiryAdapter=new Enquiry_Adapter(Enquiry_Activity.this, enquiryModelArrayList);
        eRecyclerView.setHasFixedSize(true);
        eRecyclerView.setAdapter(enquiryAdapter);

        boolean networkConectivity =  NetworkConnectivity.isConnected(this);
        if (networkConectivity) {
            progressBar.setVisibility(View.VISIBLE);
            run(enqurl,"Select");
        }
        else {
            NetworkConnectivity.networkConnetivityShowDialog(this);

        }

        bindControls();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if( enquiryModelArrayList!= null && enquiryModelArrayList.size() > 0)
                    enquiryModelArrayList.clear();

                if(StatusType != null && !StatusType.isEmpty() && StatusType.equals("0")) {
                    StatusType = "";
                }
                run(enqurl,statusfound);
            }
        });

        statusType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                statusfound = mstatusTypeArrayList.get(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }




    public  void run(String enqurl, final String staType){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .header("Pagenation",String.valueOf(page) )
                .header("StatusType",staType )
                .url(enqurl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }


            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String myResponse = response.body().string();

                Enquiry_Activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        JSONObject forecast = null;
                        try {
                            forecast = new JSONObject(myResponse);
                            //           JSONObject data = forecast.getJSONObject("Equipment");


                            JSONArray jsonArray = forecast.getJSONArray("Inquiry");


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject enqobject = jsonArray.getJSONObject(i);

                                String enqid = enqobject.getString("id");
                                String enqName = enqobject.getString("name");
                                String enqDoe = enqobject.getString("doe");
                                String enqRemark = enqobject.getString("remark");
                                String enqStatus = enqobject.getString("inquiry_status");
                                String enqFollDate = enqobject.getString("followup");

                                JSONObject detailsObject = enqobject.getJSONObject("Details");

                                String enqcontactno = detailsObject.getString("contactno");
                                String enqemail = detailsObject.getString("email");
                                String enqcity = detailsObject.getString("city");


                                Enquiry_Model enquiry_model = new Enquiry_Model();
                                enquiry_model.setE_id(enqid);
                                enquiry_model.setE_name(enqName);
                                enquiry_model.setE_enqDate(enqDoe);
                                enquiry_model.setE_remark(enqRemark);

                                if (enqRemark.equals(mNull)){
                                    enquiry_model.setE_remark("");
                                }

                                enquiry_model.setE_enqStatus(enqStatus);
                                enquiry_model.setE_followUp(enqFollDate);

                                if (enqFollDate.equals(mNull)){
                                    enquiry_model.setE_followUp("");

                                }

                                enquiry_model.setE_contact(enqcontactno);
                                enquiry_model.setE_email(enqemail);

                                if (enqemail.equals(mNull)){
                                    enquiry_model.setE_email("");
                                }
                                enquiry_model.setE_city(enqcity);

                                if (enqcity.equals(mNull)){
                                    enquiry_model.setE_city("");
                                }


                                if (enquiryModelArrayList != null && enquiryModelArrayList.size() > 0) {
                                    int size = enquiryModelArrayList.size();
                                    enquiry_model.setE_serial_id(String.valueOf(size + 1));
                                    enquiryModelArrayList.add(size, enquiry_model);


                                } else {
                                    enquiry_model.setE_serial_id(String.valueOf(i + 1));
                                    enquiryModelArrayList.add(enquiry_model);
                                }

                            }
                            enquiryAdapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public void bindControls(){
        enquirypagination.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                enquirypagination.setRefreshing(false);
                page=page+1;
                run(enqurl,"Select");
            }
        });
    }

    @Override
    public void onDeleteItemListener(int pos) {
        enquiryModelArrayList.remove(pos);
        enquiryAdapter.notifyDataSetChanged();
    }
}
