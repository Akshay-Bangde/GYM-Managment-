package com.example.smile.activities;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.smile.R;
import com.example.smile.utils.NetworkConnectivity;

import java.util.ArrayList;

import static com.example.smile.activities.SignInActivity.BaseURL;

public class TrainingSchedule_Activity extends AppCompatActivity {

    LinearLayout mLinearLayout;
    Button mSaveBtn;
    Spinner mNameSpinner, mBodypartSpinner, mWorkoutSpinner, mSetSpinner;
    ArrayList<String> mNameArrayList,mBodypartArrayList,
            mWorkoutArrayList,mSetArrayList;

    EditText minutedEdt;
    CheckBox chk1,chk2,chk3,chk4,chk5,chk6,chk7;

    ArrayList<String> list ;


    String dataSpinnerURL =BaseURL+"gymPackager";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_schedule);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.backbutton);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            }
        });

        init();

        savebtn();

    }

    public void init() {
        mSaveBtn = findViewById(R.id.save);
        mNameSpinner = findViewById(R.id.nameSpinner);
        mBodypartSpinner = findViewById(R.id.Spinnerbody_part_name);
        mWorkoutSpinner = findViewById(R.id.Spinnerworkout);
        mSetSpinner = findViewById(R.id.SetSpinner);
        mLinearLayout = findViewById(R.id.linearLayout);
        minutedEdt = findViewById(R.id.minutes);


        list = new ArrayList<String>();
        chk1=(CheckBox)findViewById(R.id.monday);
        chk2=(CheckBox)findViewById(R.id.tuesdeay);
        chk3=(CheckBox)findViewById(R.id.wednesday);
        chk4=(CheckBox)findViewById(R.id.thrusday);
        chk5=(CheckBox)findViewById(R.id.friday);
        chk6=(CheckBox)findViewById(R.id.saturday);
        chk7=(CheckBox)findViewById(R.id.sunday);

        mNameArrayList = new ArrayList<String>();
        mNameArrayList.add("Select");
        mNameArrayList.add("asd");
        ArrayAdapter<String> nameAdapter = new ArrayAdapter<String>(TrainingSchedule_Activity.this,
                android.R.layout.simple_list_item_1, mNameArrayList);
        nameAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mNameSpinner.setAdapter(nameAdapter);

        mBodypartArrayList = new ArrayList<String>();
        mBodypartArrayList.add("Select");
        mBodypartArrayList.add("fdsf");
        ArrayAdapter<String> bodyPartAdapter = new ArrayAdapter<String>(TrainingSchedule_Activity.this,
                android.R.layout.simple_list_item_1, mBodypartArrayList);
        bodyPartAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mBodypartSpinner.setAdapter(bodyPartAdapter);


        mWorkoutArrayList = new ArrayList<String>();
        mWorkoutArrayList.add("Select");
        mWorkoutArrayList.add("dsf");
        ArrayAdapter<String> workoutAdapter = new ArrayAdapter<String>(TrainingSchedule_Activity.this,
                android.R.layout.simple_list_item_1, mWorkoutArrayList);
        workoutAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mWorkoutSpinner.setAdapter(workoutAdapter);


        mSetArrayList = new ArrayList<String>();
        mSetArrayList.add("Select");
        mSetArrayList.add("1");
        mSetArrayList.add("2");
        mSetArrayList.add("3");
        mSetArrayList.add("4");
        mSetArrayList.add("5");
        mSetArrayList.add("6");
        ArrayAdapter<String> setAdapter = new ArrayAdapter<String>(TrainingSchedule_Activity.this,
                android.R.layout.simple_list_item_1, mSetArrayList);
        setAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mSetSpinner.setAdapter(setAdapter);


    }

    public void savebtn(){
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean networkConectivity = NetworkConnectivity.isConnected(TrainingSchedule_Activity.this);
                if (networkConectivity) {
                    if (validation()) {

                       /* if (onCheckboxClicked(txt)) {
                            for (String str : list) {
                                txt.setText(txt.getText().toString() + " , " + str);

                            }
                        }*/
                    }

                    }
                else {
                    NetworkConnectivity.networkConnetivityShowDialog(TrainingSchedule_Activity.this);

                }

            }
        });
    }

    private Boolean validation() {
        boolean flag = true;
        String minutes = minutedEdt.getText().toString();

        if (mNameSpinner.getSelectedItem().toString().trim().equals("Select")) {
            flag = false;
            Snackbar.make(mLinearLayout, getResources().getString(R.string.plz_select_the_name), Snackbar.LENGTH_SHORT).show();
        }
        else if(!chk1.isChecked() && !chk2.isChecked() && !chk3.isChecked() &&
                !chk4.isChecked() && !chk5.isChecked() && !chk6.isChecked() &&
                !chk7.isChecked()){
            Snackbar.make(mLinearLayout, getResources().getString(R.string.plz_select_checkbox), Snackbar.LENGTH_SHORT).show();

        }
        else if (mBodypartSpinner.getSelectedItem().toString().trim().equals("Select")) {
            flag = false;
            Snackbar.make(mLinearLayout, getResources().getString(R.string.plz_select_the_bodypart), Snackbar.LENGTH_SHORT).show();
        }
        else if (mWorkoutSpinner.getSelectedItem().toString().trim().equals("Select")) {
            flag = false;
            Snackbar.make(mLinearLayout, getResources().getString(R.string.plz_select_the_workout), Snackbar.LENGTH_SHORT).show();
        }
        else if (mSetSpinner.getSelectedItem().toString().trim().equals("Select")) {
            flag = false;
            Snackbar.make(mLinearLayout, getResources().getString(R.string.plz_select_the_set), Snackbar.LENGTH_SHORT).show();
        }
        if (minutes.isEmpty()) {
            flag = false;
            minutedEdt.setError(getResources().getString(R.string.plz_enter_minutes));
        }

        return flag;
    }

    /*public boolean onCheckboxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.monday:
                list.add(chk1.getTag().toString());
                break;
            case R.id.tuesdeay:
                list.add(chk2.getTag().toString());
                break;
            case R.id.wednesday:
                list.add(chk3.getTag().toString());
                break;
            case R.id.thrusday:
                list.add(chk4.getTag().toString());
                break;
            case R.id.friday:
                list.add(chk5.getTag().toString());
                break;
            case R.id.saturday:
                list.add(chk6.getTag().toString());
                break;
            case R.id.sunday:
                list.add(chk7.getTag().toString());
                break;

        }
    return checked;
    }
*/



}
