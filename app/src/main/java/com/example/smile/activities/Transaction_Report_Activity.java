package com.example.smile.activities;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.smile.R;

import java.util.ArrayList;

public class Transaction_Report_Activity extends AppCompatActivity {
    Spinner spinner_month,spinner_year;
    ArrayList<String> monthList,yearList;
    private Button mSearchButton;
    LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction__report);
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
        spinner_month = findViewById(R.id.month_spinner);
        spinner_year = findViewById(R.id.year_spinner);

        monthList = new ArrayList<String>();
        monthList.add("Select");
        monthList.add("January");
        monthList.add("February");
        monthList.add("March");
        monthList.add("April");
        monthList.add("May");
        monthList.add("June");
        monthList.add("July");
        monthList.add("August");
        monthList.add("September");
        monthList.add("October");
        monthList.add("November");
        monthList.add("December");

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(Transaction_Report_Activity.this,
                android.R.layout.simple_list_item_1,monthList);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_month.setAdapter(monthAdapter);

        yearList = new ArrayList<String>();
        yearList.add("Select");
        yearList.add("2018");
        yearList.add("2019");
        yearList.add("2020");
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(Transaction_Report_Activity.this,
                android.R.layout.simple_list_item_1,yearList);
        yearAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_year.setAdapter(yearAdapter);

        SearchButton();

    } public void SearchButton (){
        mLinearLayout = findViewById(R.id.linear_layout);
        mSearchButton = findViewById(R.id.searchButton);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation());
            }
        });
    }

    public boolean validation(){
        if (spinner_month.getSelectedItem().toString().trim().equals("Select")||spinner_year.getSelectedItem().toString().trim().equals("Select")){
            Snackbar.make(mLinearLayout,getResources().getString(R.string.plz_select_month_year),Snackbar.LENGTH_SHORT).show();
        }
        return true;
    }
}
