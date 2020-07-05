package com.example.smile.activities;

import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.smile.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Member_AttendanceActivity extends AppCompatActivity {


    EditText mfromDate, mtoDate, mMemberName;
    CoordinatorLayout mCoordinatorLayout;
    Button mSearchButton;
    Calendar fromdateCalender, todateCalender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member__attendance);

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

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent add_member_attendance = new Intent(Member_AttendanceActivity.this, Add_Member_AttendanceActivity.class);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(Member_AttendanceActivity.this, R.anim.push_up_in, R.anim.push_up_out);
                startActivity(add_member_attendance, options.toBundle());

            }
        });


        init();
        SearchButton();
    }

    public void init() {
        mCoordinatorLayout = findViewById(R.id.coordinator_layout);
        mfromDate = findViewById(R.id.fromDate);
        mtoDate = findViewById(R.id.toDate);
        mMemberName = findViewById(R.id.memberName);
        mSearchButton = findViewById(R.id.searchButton);

        String currentDate = new SimpleDateFormat("dd - MMM - yyyy",
                Locale.getDefault()).format(new Date());
        fromdateCalender = Calendar.getInstance();
        mfromDate.setText(currentDate);

        todateCalender = Calendar.getInstance();
       mtoDate.setText(currentDate);

        calender();


    }

    private void updateLabel() {
        String myFormat = "dd - MMM - yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
        mfromDate.setText(sdf.format(fromdateCalender.getTime()));
        mtoDate.setText(sdf.format(todateCalender.getTime()));
    }

    private void calender() {

        // datepicker for date of joining
        final DatePickerDialog.OnDateSetListener date =
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        fromdateCalender.set(Calendar.YEAR, year);
                        fromdateCalender.set(Calendar.MONTH, monthOfYear);
                        fromdateCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();

                    }

                };

        mfromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Member_AttendanceActivity.this, R.style.DialogTheme, date,
                        fromdateCalender.get(Calendar.YEAR), fromdateCalender.get(Calendar.MONTH),
                        fromdateCalender.get(Calendar.DAY_OF_MONTH)).show();

            };

        });


        // datePicker for birthdate
        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                todateCalender.set(Calendar.YEAR, year);
                todateCalender.set(Calendar.MONTH, monthOfYear);
                todateCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();

            }

        };
        mtoDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Member_AttendanceActivity.this, R.style.DialogTheme, date1,
                      todateCalender.get(Calendar.YEAR),
                        todateCalender.get(Calendar.MONTH),
                      todateCalender.get(Calendar.DAY_OF_MONTH)).show();
                    };

                });

    }

    public void SearchButton() {

        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {

                }
            }
        });

    }

    public boolean validation() {
        boolean flag = true;

        return flag;
    }
}
