package com.example.smile.activities;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.smile.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Add_Employee_AttendanceActivity extends AppCompatActivity {

    Button mSearchButton;
    EditText mDateEdt;
    Calendar mDateCalender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__employee__attendance);
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


        String currentDate = new SimpleDateFormat("dd - MMM - yyyy", Locale.getDefault()).format(new Date());

        mDateCalender = Calendar.getInstance();
        mDateEdt = findViewById(R.id.date_Genrate);
        mDateEdt.setText(currentDate);
        SearchButton();
    }
    public void updateLabel(){
        String myFormat = " dd - MMM - yyyy";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(myFormat, Locale.UK);
        mDateEdt.setText(sdf.format(mDateCalender.getTime()));

    }
    public void SearchButton(){
        mSearchButton = findViewById(R.id.searchButton);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mDateCalender.set(Calendar.YEAR,year);
                mDateCalender.set(Calendar.MONTH,month);
                mDateCalender.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                updateLabel();
            }
        };

        mDateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Add_Employee_AttendanceActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mDateCalender.set(Calendar.YEAR,year);
                        mDateCalender.set(Calendar.MONTH,month);
                        mDateCalender.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        updateLabel();
                    }
                },mDateCalender.get(Calendar.YEAR),mDateCalender.get(Calendar.MONTH),mDateCalender.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }
}
