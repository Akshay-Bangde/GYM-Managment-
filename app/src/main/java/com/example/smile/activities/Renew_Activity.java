package com.example.smile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import com.example.smile.R;
import com.example.smile.adapters.MemberRenewalListAdapter;
import com.example.smile.dataObjects.MemberRenewalListModel;

import java.util.ArrayList;

public class Renew_Activity extends AppCompatActivity {
     private Spinner spinner_memberType,
            spinner_memberName, spinner_selectPackage;
    ArrayList<String> memberTypeList, memberNameList, packageList;
    CoordinatorLayout mCoordinatorLayout;
    Button mSearchBtn;

    RecyclerView mRecyclerView;
    MemberRenewalListAdapter adapter;
    ArrayList<MemberRenewalListModel> mMemberRenewalArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renew);
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


        Button upcoming_renew = findViewById(R.id.upcoming_renew);
        upcoming_renew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent upcoming_renew = new Intent (Renew_Activity.this,Upcoming_Renew_ListActivity.class);
                startActivity(upcoming_renew);
            }
        });


        init();
        SearchButton();

        mMemberRenewalArrayList = new ArrayList<MemberRenewalListModel>();
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MemberRenewalListAdapter(Renew_Activity.this, mMemberRenewalArrayList);
        mMemberRenewalArrayList.add(new MemberRenewalListModel("1","aksh","1/1/2013","1/2/2015","4/5/2015","Golden","1 years"));
        mMemberRenewalArrayList.add(new MemberRenewalListModel("1","aksh","1/1/2013","1/2/2015","4/5/2015","Golden","1 years"));
        mMemberRenewalArrayList.add(new MemberRenewalListModel("1","aksh","1/1/2013","1/2/2015","4/5/2015","Golden","1 years"));
        mMemberRenewalArrayList.add(new MemberRenewalListModel("1","aksh","1/1/2013","1/2/2015","4/5/2015","Golden","1 years"));
        mMemberRenewalArrayList.add(new MemberRenewalListModel("1","aksh","1/1/2013","1/2/2015","4/5/2015","Golden","1 years"));
        mRecyclerView.setAdapter(adapter);


    }

    public void init() {
        spinner_memberType = findViewById(R.id.memberTypeSpinner);
        spinner_memberName = findViewById(R.id.memberNameSpinner);
        spinner_selectPackage = findViewById(R.id.pacakgeSpinner);

        memberTypeList = new ArrayList<String>();
        memberTypeList.add("Select");
        memberTypeList.add("Individual");
        memberTypeList.add("Group");
        ArrayAdapter<String> memberTypeAdapter = new ArrayAdapter<String>(Renew_Activity.this,
                android.R.layout.simple_list_item_1, memberTypeList);
        memberTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_memberType.setAdapter(memberTypeAdapter);

        packageList = new ArrayList<String>();
        packageList.add("Select");
        ArrayAdapter<String> memberNameAdapater = new ArrayAdapter<>(Renew_Activity.this,
                android.R.layout.simple_list_item_1, packageList);
        memberNameAdapater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_memberName.setAdapter(memberNameAdapater);

        memberNameList = new ArrayList<String>();
        memberNameList.add("Select");
        ArrayAdapter<String> packageAdapater = new ArrayAdapter<>(Renew_Activity.this,
                android.R.layout.simple_list_item_1, packageList);
        packageAdapater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_selectPackage.setAdapter(packageAdapater);

    }

    public void SearchButton() {
        mCoordinatorLayout = findViewById(R.id.coordinator_layout);
        mSearchBtn = findViewById(R.id.searchButton);
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) ;
            }
        });
    }

    private boolean validation() {
        boolean flag = true;
        if (spinner_memberType.getSelectedItem().toString().trim().equals("Select") ||
                spinner_selectPackage.getSelectedItem().toString().trim().equals("Select")) {
            flag = false;
            Snackbar.make(mCoordinatorLayout, getResources()
                    .getString(R.string.plz_select_membertype_package), Snackbar.LENGTH_SHORT).show();
        }
        return flag;
    }



}


