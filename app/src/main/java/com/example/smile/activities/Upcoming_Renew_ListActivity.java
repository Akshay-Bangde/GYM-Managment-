package com.example.smile.activities;

import android.app.DatePickerDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.smile.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Upcoming_Renew_ListActivity extends AppCompatActivity {

    EditText mDoj, mRenewDate;
    CoordinatorLayout mCoordinatorLayout;
    LinearLayout mLinearLayout;
    Button mSaveButton;
    Calendar dojCalender, renewDateCalender;
    TextView mFullName,mPackageCharge,mTrainerCharge;
    Spinner mDietPlanSpinner,mBatchSpinner,mPackageSpinner,
    mPersonalTrainerSpinner,mTrainerNameSpinner;

    ArrayList<String> DietPlanArrayList,BatchArrayList,PackageArrayList,
    PersonalTrainerArrayList,TrainerNameArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming__renew__list);
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


        init();

        String currentDate = new SimpleDateFormat("dd - MMM - yyyy",
                Locale.getDefault()).format(new Date());
        dojCalender = Calendar.getInstance();
        mDoj.setText(currentDate);

        renewDateCalender = Calendar.getInstance();
        mRenewDate.setText(currentDate);

        calender();

        ClicksHandler();
    }


    public void init(){
        mCoordinatorLayout = findViewById(R.id.coordinator_layout);
        mDoj = findViewById(R.id.doj);
        mRenewDate = findViewById(R.id.renewDate);
        mSaveButton = findViewById(R.id.saveBtn);
        mLinearLayout = findViewById(R.id.personalTrainerlayout);

        mDietPlanSpinner = findViewById(R.id.dietPlanSpinner);
        mBatchSpinner = findViewById(R.id.batchSpinner);
        mPackageSpinner = findViewById(R.id.packageSpinner);
        mPersonalTrainerSpinner = findViewById(R.id.personalTrainerSpinner);
        mTrainerNameSpinner = findViewById(R.id.TrainerSpinner);


        DietPlanArrayList = new ArrayList<String>();
        DietPlanArrayList.add("Select");
        ArrayAdapter<String> dietPlanAdapter = new ArrayAdapter<String>(Upcoming_Renew_ListActivity.this,
                android.R.layout.simple_list_item_1, DietPlanArrayList);
        dietPlanAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mDietPlanSpinner.setAdapter(dietPlanAdapter);



        BatchArrayList = new ArrayList<String>();
        BatchArrayList.add("Select");
        BatchArrayList.add("Morning");
        BatchArrayList.add("Afternoon");
        BatchArrayList.add("Evening");
        ArrayAdapter<String> batchAdapter = new ArrayAdapter<String>(Upcoming_Renew_ListActivity.this,
                android.R.layout.simple_list_item_1, BatchArrayList);
        batchAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mBatchSpinner.setAdapter(batchAdapter);


        PackageArrayList = new ArrayList<String>();
        PackageArrayList.add("Select");
        ArrayAdapter<String> PackageAdapter = new ArrayAdapter<String>(Upcoming_Renew_ListActivity.this,
                android.R.layout.simple_list_item_1, PackageArrayList);
        PackageAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mPackageSpinner.setAdapter(PackageAdapter);


        PersonalTrainerArrayList = new ArrayList<String>();
        PersonalTrainerArrayList.add("Select");
        PersonalTrainerArrayList.add("Yes");
        PersonalTrainerArrayList.add("No");
            ArrayAdapter<String> PersonalTrainerAdapter = new ArrayAdapter<String>(Upcoming_Renew_ListActivity.this,
                android.R.layout.simple_list_item_1, PersonalTrainerArrayList);
        PersonalTrainerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mPersonalTrainerSpinner.setAdapter(PersonalTrainerAdapter);


        TrainerNameArrayList = new ArrayList<String>();
        TrainerNameArrayList.add("Select");
        ArrayAdapter<String> TrainerNameAdapter = new ArrayAdapter<String>(Upcoming_Renew_ListActivity.this,
                android.R.layout.simple_list_item_1, TrainerNameArrayList);
        TrainerNameAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mTrainerNameSpinner.setAdapter(TrainerNameAdapter);



    }

    public void ClicksHandler(){
        mPersonalTrainerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                String statusVal = selectedItem;
                if (statusVal.equals("Select")) {
                    mLinearLayout.setVisibility(View.GONE);
                } else if (statusVal.equals("Yes")) {
                    mLinearLayout.setVisibility(View.VISIBLE);
                }else if (statusVal.equals("No")) {
                    mLinearLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void updateLabel() {
        String myFormat = "dd - MMM - yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
        mDoj.setText(sdf.format(dojCalender.getTime()));
        mRenewDate.setText(sdf.format(renewDateCalender.getTime()));
    }

    private void calender() {

        // datepicker for date of joining
        final DatePickerDialog.OnDateSetListener date =
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        dojCalender.set(Calendar.YEAR, year);
                        dojCalender.set(Calendar.MONTH, monthOfYear);
                        dojCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();

                    }

                };

        mDoj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Upcoming_Renew_ListActivity.this, R.style.DialogTheme, date,
                        dojCalender.get(Calendar.YEAR), dojCalender.get(Calendar.MONTH),
                        dojCalender.get(Calendar.DAY_OF_MONTH)).show();

            };

        });


        // datePicker for birthdate
        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                renewDateCalender.set(Calendar.YEAR, year);
                renewDateCalender.set(Calendar.MONTH, monthOfYear);
                renewDateCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();

            }

        };
        mRenewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Upcoming_Renew_ListActivity.this, R.style.DialogTheme, date1,
                        renewDateCalender.get(Calendar.YEAR),
                        renewDateCalender.get(Calendar.MONTH),
                        renewDateCalender.get(Calendar.DAY_OF_MONTH)).show();
            };

        });

    }

    public void SearchButton() {

        mSaveButton.setOnClickListener(new View.OnClickListener() {
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
