package com.example.smile.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.smile.R;
import com.example.smile.adapters.Tab_Adapter;
import com.example.smile.fragment.Group_Fragment;
import com.example.smile.fragment.Individual_Fragment;

public class Member_Payment_Report_Activity extends AppCompatActivity {

       private Tab_Adapter mAdapter;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member__payment__report);

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

        mViewPager = findViewById(R.id.viewPager);
        mTabLayout = findViewById(R.id.tabLayout);

        mAdapter = new Tab_Adapter(getSupportFragmentManager());
        mAdapter.addFragment(new Individual_Fragment(), "Individual");
        mAdapter.addFragment(new Group_Fragment(),"Group");

        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

}
