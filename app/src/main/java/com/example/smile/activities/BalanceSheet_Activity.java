package com.example.smile.activities;

import android.app.DatePickerDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.example.smile.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BalanceSheet_Activity extends AppCompatActivity {
    Spinner spinner_month, spinner_year;
    Button mSearchBtn, mSearchBtnDateWise;
    private LinearLayout mMonthYearLayout, mDateWiseLayout, mCoordinatorLayout;
    private RadioButton mMonthYearBtn, mDateWiseBtn;
    private RadioGroup mLayoutRadioGroup;
    Calendar mStartDateCalender, mEnddateCalender;
    EditText mStartdateEdt, mEnddateEdt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance_sheet);
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
        ArrayList<String> monthList = new ArrayList<String>();
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

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(BalanceSheet_Activity.this,
                android.R.layout.simple_list_item_1, monthList);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_month.setAdapter(monthAdapter);


        ArrayList<String> yearList = new ArrayList<String>();
        yearList.add("Select");
        yearList.add("2018");
        yearList.add("2019");
        yearList.add("2020");

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(BalanceSheet_Activity.this,
                android.R.layout.simple_spinner_dropdown_item, yearList);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_year.setAdapter(yearAdapter);

        /*String currentDate = new SimpleDateFormat("dd - MMM - yyyy", Locale.getDefault()).format(new Date());
*/
        mStartDateCalender = Calendar.getInstance();
        mStartdateEdt = findViewById(R.id.start_date);
     /*   mStartdateEdt.setText("Start Date");
*/
        mEnddateCalender = Calendar.getInstance();
        mEnddateEdt = findViewById(R.id.end_date);
      /*  mEnddateEdt.setText("End Date");*/

        RadioButtonChange();
        SearchButton();
        SearchButtonforDateWise();
    }

    public void SearchButton() {
        mCoordinatorLayout = findViewById(R.id.coordinator_layout);
        mSearchBtn = findViewById(R.id.searchButton);
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()) {

                }
            }
        });

    }

    public boolean validation() {
        if (spinner_month.getSelectedItem().toString().trim().equals("Select") ||
                spinner_year.getSelectedItem().toString().trim().equals("Select")) {
            Snackbar.make(mCoordinatorLayout, getResources().getString(R.string.plz_select_month_year), Snackbar.LENGTH_SHORT).show();
        }
        return true;
    }
    // Search function for Date wise

    public void updateLabel() {
        String myFormat = "dd - MMM - yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
        mStartdateEdt.setText(sdf.format(mStartDateCalender.getTime()));
        mEnddateEdt.setText(sdf.format(mEnddateCalender.getTime()));

    }
    public void SearchButtonforDateWise(){
        mSearchBtnDateWise = findViewById(R.id.searchBtn);
        mSearchBtnDateWise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Date Picker for starting date
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mStartDateCalender.set(Calendar.YEAR,year);
                mStartDateCalender.set(Calendar.MONTH,month);
                mStartDateCalender.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                updateLabel();
            }
        };
        mStartdateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(BalanceSheet_Activity.this, R.style.DialogTheme,
                        date, mStartDateCalender.get(Calendar.YEAR), mStartDateCalender.get(Calendar.MONTH), mStartDateCalender.get(Calendar.DAY_OF_MONTH)).show();
                   /* @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mStartDateCalender.set(Calendar.YEAR,year);
                        mStartDateCalender.set(Calendar.MONTH,month);
                        mStartDateCalender.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        updateLabel();
                    }
                }*//*,mStartDateCalender.get(Calendar.YEAR),mStartDateCalender.get(Calendar.MONTH),mStartDateCalender.get(Calendar.DAY_OF_MONTH)).show();*/

            };
            });

        // Date Picker for Ending date
        DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mEnddateCalender.set(Calendar.YEAR,year);
                mEnddateCalender.set(Calendar.MONTH,month);
                mEnddateCalender.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                updateLabel();
            }
        };
        mEnddateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(BalanceSheet_Activity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mEnddateCalender.set(Calendar.YEAR,year);
                        mEnddateCalender.set(Calendar.MONTH,month);
                        mEnddateCalender.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        updateLabel();
                    }
                },mEnddateCalender.get(Calendar.YEAR),mEnddateCalender.get(Calendar.MONTH),mEnddateCalender.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
//Layout change when user click on radio button

    private void RadioButtonChange() {
        mMonthYearLayout = findViewById(R.id.month_year);
        mDateWiseLayout = findViewById(R.id.date_wise);
        mMonthYearBtn = findViewById(R.id.monthYear);
        mDateWiseBtn = findViewById(R.id.dateWise);
        mLayoutRadioGroup = findViewById(R.id.radioGroup);
        mLayoutRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButtonClicked(checkedId);
            }
        });
    }

    private void radioButtonClicked(int flag) {

        switch (flag) {
            case R.id.monthYear:
                if (mMonthYearBtn.isChecked()) {
                    mMonthYearLayout.setVisibility(View.VISIBLE);
                    mDateWiseLayout.setVisibility(View.GONE);
                }
                break;
            case R.id.dateWise:
                if (mDateWiseBtn.isChecked()) {
                    mMonthYearLayout.setVisibility(View.GONE);
                    mDateWiseLayout.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }

    }
}
