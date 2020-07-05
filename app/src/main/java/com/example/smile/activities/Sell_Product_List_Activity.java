package com.example.smile.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.smile.R;

import java.util.ArrayList;
import java.util.Calendar;

public class Sell_Product_List_Activity extends AppCompatActivity {

    Calendar mStartDate,mEndDate;
    Button mSearchButton;
    Spinner mSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell__product__list);
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

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Sell_Product_List_Activity.this, Add_Sell_Product_Activity.class);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(Sell_Product_List_Activity.this, R.anim.push_up_in, R.anim.push_up_out);
                startActivity(intent, options.toBundle());


            }
        });

        mSpinner = findViewById(R.id.spinner);
        ArrayList<String> paidList = new ArrayList<String>();
        paidList.add("Paid");
        paidList.add("Unpaid");
        ArrayAdapter<String> batchAdapter = new ArrayAdapter<String>(Sell_Product_List_Activity.this,
                android.R.layout.simple_list_item_1, paidList);
        batchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(batchAdapter);

        SearchButton();
    }




    public void SearchButton() {
        mSearchButton = findViewById(R.id.searchButton);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
