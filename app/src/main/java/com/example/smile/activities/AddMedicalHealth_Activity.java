package com.example.smile.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.example.smile.R;
import com.example.smile.adapters.AddMedicalHealthViewPagerAdapter;

public class AddMedicalHealth_Activity extends AppCompatActivity {

    private TabLayout mTabLayout;
    public  ViewPager mViewPager;
    public AddMedicalHealthViewPagerAdapter addMedicalHealthViewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medical_health);
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

        mViewPager = findViewById(R.id.view_pager);
        mTabLayout = findViewById(R.id.tabLayout);

        addMedicalHealthViewPagerAdapter = new AddMedicalHealthViewPagerAdapter(getSupportFragmentManager(), mViewPager);
        mViewPager.setAdapter(addMedicalHealthViewPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);


    }
}