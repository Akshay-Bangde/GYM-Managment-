package com.example.smile.activities;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smile.R;
import com.example.smile.dataObjects.ActivityListModel;
import com.example.smile.dataObjects.MemberGroupListModel;
import com.example.smile.dataObjects.PackageModel;
import com.example.smile.dataObjects.TrainerModel;
import com.example.smile.utils.NetworkConnectivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.smile.activities.SignInActivity.BaseURL;

public class Add_GroupActivity extends AppCompatActivity {

    EditText mGroupName, mContactNumber, mContactPerson, mEmailId,
            mAddress, mState, mCity, mTotalmember;
    Spinner mPackage,spinnertrainerCheck, mTrainer;
    CoordinatorLayout mCoordinatorLayout;
    LinearLayout mLinearLayout,mpt_linearLayout;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Button mSubmit;

    ArrayList<String> package_List, trainerCheck,trainer_List;
    private ArrayList<PackageModel> mPackagesArr;
    private ArrayList<TrainerModel> mTrainerArr;
    TextView mPackageAmount,mTrainerCharge;
    final String Spinner_URL = BaseURL+"Group" ;
    private  TextView toolbar_title;
    final String AddGroup_URL = BaseURL+"Group" ;

    String selectPackage,personltrainercheck,selectTrainer,selectPackageId,TrainerID;
    String Message;
    ProgressBar progressBar;
    MemberGroupListModel data = null ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__group);

        Intent intent = getIntent();
        if(intent != null ){
            try {
                data = (MemberGroupListModel) intent.getSerializableExtra("packageData");
            }catch (Exception e){
                Log.d("exception", e.toString());
            }
        }


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
        toolbar_title = findViewById(R.id.toolbar_title);
        try {
            if (data != null) {
                toolbar_title.setText(getResources().getString(R.string.edit_group));
            } else {
                toolbar_title.setText(getResources().getString(R.string.title_activity_add_group));
            }
        }catch (Exception e){

        }
        progressBar = findViewById(R.id.pb);
        mSubmit = findViewById(R.id.submit_btn);
        mGroupName = findViewById(R.id.group_name);
        mContactPerson = findViewById(R.id.contact_person);
        mContactNumber = findViewById(R.id.contact_no);
        mEmailId = findViewById(R.id.email_id);
        mAddress = findViewById(R.id.address);
        mCity = findViewById(R.id.city);
        mState = findViewById(R.id.state);
        mTotalmember = findViewById(R.id.total_member);
        mPackage = findViewById(R.id.package_spinner);
        mTrainer = findViewById(R.id.trainer_spinner);
        mPackageAmount = findViewById(R.id.totalAmount);
        mTrainerCharge = findViewById(R.id.trainerCharge);
        spinnertrainerCheck = findViewById(R.id.selectTrainerSpinner);
        mCoordinatorLayout = findViewById(R.id.coordinator_layout);
        mpt_linearLayout = findViewById(R.id.pt_linearLayout);

        mPackagesArr = new ArrayList<>();
        mTrainerArr = new ArrayList<>();

        package_List = new ArrayList<String>();
        package_List.add("Select Package");

        trainerCheck = new ArrayList<String>();
        trainerCheck.add("Select");
        trainerCheck.add("Yes");
        trainerCheck.add("No");

        ArrayAdapter<String> checktrainerAdapter = new ArrayAdapter<String>
                (Add_GroupActivity.this,android.R.layout.simple_list_item_1,trainerCheck);
        checktrainerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnertrainerCheck.setAdapter(checktrainerAdapter);

        if(data != null){
            setcheckTrainerData();
        }

        trainer_List = new ArrayList<String>();
        trainer_List.add("Select Trainer");
        ArrayAdapter<String> TrainerAdapter = new ArrayAdapter<String>(Add_GroupActivity.this,
                android.R.layout.simple_list_item_1, trainer_List);
        TrainerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTrainer.setAdapter(TrainerAdapter);


        clicksHandler();
        SubmitButton();
        run(Spinner_URL);
        Edit();


    }

    public void setSpinnerData(){
        String packname = data.getPackageName();
        int pos = 0;
        if(package_List != null && package_List.size() > 0) {
            for (int i = 0; i < package_List.size(); i++) {
                if (packname.equals(package_List.get(i))) {
                    pos = i;
                }
            }
        }
        mPackage.setSelection(pos);
    }

    public void setcheckTrainerData(){
        String checktrainer = data.getTrainerCheck();
        int pos = 0;
        if(trainerCheck != null && trainerCheck.size() > 0) {
            for (int i = 0; i < trainerCheck.size(); i++) {
                if (checktrainer.equals(trainerCheck.get(i))) {
                    pos = i;
                }
            }
        }
        spinnertrainerCheck.setSelection(pos);
    }

    public void setTrainerNameData(){
        String trainername = data.getTrainerName();
        int pos = 0;
        if(trainer_List != null && trainer_List.size() > 0) {
            for (int i = 0; i < trainer_List.size(); i++) {
                if (trainername.equals(trainer_List.get(i))) {
                    pos = i;
                }
            }
        }
        mTrainer.setSelection(pos);
    }

    public void Edit() {
        if (data != null) {
            mGroupName.setText(data.getGroupName());
            mContactPerson.setText(data.getContactPersonName());
            mContactNumber.setText(data.getMobileNo());
            mEmailId.setText(data.getEmail());
            mAddress.setText(data.getAddress());
            mState.setText(data.getState());
            mCity.setText(data.getCity());
            mTotalmember.setText(data.getTotalMembers());
            mPackageAmount.setText(data.getPackageCharge());
            mTrainerCharge.setText(data.getTrainerCharge());

        }

    }

    // Validation for submitButton
    private void SubmitButton() {
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean networkConectivity =  NetworkConnectivity.isConnected(Add_GroupActivity.this);
                if (networkConectivity) {
                    if (validation()) {
                        if (data != null) {
                            String id = data.getId();
                            String groupName = mGroupName.getText().toString();
                            String contactPersonName = mContactPerson.getText().toString();
                            String mobileNo = mContactNumber.getText().toString();
                            String email = mEmailId.getText().toString();
                            String address = mAddress.getText().toString();
                            String state = mState.getText().toString();
                            String city = mCity.getText().toString();
                            String totalmember = mTotalmember.getText().toString();
                            String selectPackages = selectPackageId;
                            String packageCharge = mPackageAmount.getText().toString();
                            String trainerCheck = personltrainercheck;
                            String selectTrainers = TrainerID;
                            String trainerCharge = mTrainerCharge.getText().toString();

                            EditHandler handler = new EditHandler(id,groupName, contactPersonName,
                                    mobileNo, email, address, state, city, totalmember,
                                    selectPackages, packageCharge,trainerCheck,
                                    selectTrainers, trainerCharge);
                            progressBar.setVisibility(View.VISIBLE);
                            mSubmit.setVisibility(View.GONE);
                            String result = null;
                            try {
                                result = handler.execute(AddGroup_URL).get();

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                        else{
                            String groupName = mGroupName.getText().toString();
                            String contactPersonName = mContactPerson.getText().toString();
                            String mobileNo = mContactNumber.getText().toString();
                            String email = mEmailId.getText().toString();
                            String address = mAddress.getText().toString();
                            String state = mState.getText().toString();
                            String city = mCity.getText().toString();
                            String totalmember = mTotalmember.getText().toString();
                            String packId = selectPackageId;
                            String packageCharge = mPackageAmount.getText().toString();
                            String trainerCheck = personltrainercheck;
                            String selectTrainers = TrainerID;
                            String trainerCharge = mTrainerCharge.getText().toString();
                            progressBar.setVisibility(View.VISIBLE);
                            mSubmit.setVisibility(View.GONE);
                            PostHandler handler = new PostHandler(groupName, contactPersonName,
                                    mobileNo, email, address, state, city, totalmember,
                                    packId, packageCharge,trainerCheck,
                                    selectTrainers, trainerCharge);

                            String result = null;
                            try {
                                result = handler.execute(AddGroup_URL).get();

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                else {
                    NetworkConnectivity.networkConnetivityShowDialog(Add_GroupActivity.this);

                }

            }
        });


    }

    //on Item Selected Package
    private void clicksHandler(){
        mTrainer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    mTrainerCharge.setText("0");
                }else {
                    if(mTrainerArr != null && mTrainerArr.size() > 0)
                        mTrainerCharge.setText(mTrainerArr.get(position - 1).trainerCharge);

                }

                selectTrainer = trainer_List.get(position).toString();
                if(mTrainerArr != null && mTrainerArr.size() > 0)
                TrainerID = mTrainerArr.get(position).trainerId.toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mPackage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(position == 0){
                    mPackageAmount.setText("0");
                }else {
                    if(mPackagesArr != null && mPackagesArr.size() > 0)
                        mPackageAmount.setText(mPackagesArr.get(position - 1).totalAmount);

                }

                selectPackage = package_List.get(position).toString();
                selectPackageId = mPackagesArr.get(position).packageId.toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnertrainerCheck.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if(selectedItem.equals("Select"))
                {
                    mpt_linearLayout.setVisibility(View.GONE);
                    int pos = 0;
                    mTrainer.setSelection(pos);
                    mTrainerCharge.setText("0");

                }
                else if(selectedItem.equals("Yes"))
                {
                    mpt_linearLayout.setVisibility(View.VISIBLE);

                }
                else if (selectedItem.equals("No")){

                    mpt_linearLayout.setVisibility(View.GONE);
                    int pos = 0;
                    mTrainer.setSelection(pos);
                    mTrainerCharge.setText("0");
                }
                personltrainercheck = trainerCheck.get(position).toString();



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    //for Get Spinner data
    public  void run(String url){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String myResponse = response.body().string();

                Add_GroupActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //JSONObject for Package
                        try {
                            JSONObject forecast = new JSONObject(myResponse);
                            JSONArray jsonArray = forecast.getJSONArray("Packages");
                            for (int i = 0; i <jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String packname = object.getString("onlyname");
                                String package_id = object.getString("package_id");
                                String total_amount = object.getString("total_amount");
                                package_List.add(packname);

                                PackageModel packageModel = new PackageModel();
                                packageModel.packageName = packname;
                                packageModel.packageId = package_id;
                                packageModel.totalAmount = total_amount;
                                mPackagesArr.add(packageModel);

                            }
                            PackageModel packageModel = new PackageModel();
                            packageModel.packageName = "Select";
                            packageModel.packageId = "0";
                            packageModel.totalAmount = "123";
                            mPackagesArr.add(0, packageModel);
                            ArrayAdapter<String> packageAdapter = new ArrayAdapter<String>(Add_GroupActivity.this,
                                    android.R.layout.simple_list_item_1, package_List);
                            packageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            mPackage.setAdapter(packageAdapter);
                            if(data != null){
                                setSpinnerData();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //JSONObject for Trainer Name
                        try {
                            JSONObject forecast = new JSONObject(myResponse);
                            JSONArray jsonArray = forecast.getJSONArray("Trainers");
                            for (int i = 0; i <jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String trainerName = object.getString("onlyname");
                                String trai_id = object.getString("staff_id");
                                String trainerCharge = object.getString("Charge");
                                trainer_List.add(trainerName);

                                TrainerModel trainerModel = new TrainerModel();
                                trainerModel.trainerName = trainerName;
                                trainerModel.trainerId = trai_id;
                                trainerModel.trainerCharge = trainerCharge;
                                mTrainerArr.add(trainerModel);


                            }

                            TrainerModel trainerModel = new TrainerModel();
                            trainerModel.trainerName = "Select";
                            trainerModel.trainerId = "0";
                            trainerModel.trainerCharge = "123";
                            mTrainerArr.add(0,trainerModel);
                            ArrayAdapter<String> enquiryAdapter = new ArrayAdapter<String>(Add_GroupActivity.this,
                                    android.R.layout.simple_list_item_1, trainer_List);
                            enquiryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            mTrainer.setAdapter(enquiryAdapter);
                            if(data != null){
                                setTrainerNameData();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
        });
    }


    public class EditHandler extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();
        String id,groupName, contactperson, mobileNo, email, address,
                state,city,totalMember,selectpackage, packageCharge,
                trainercheck,trainerName,trainerCharge;

        public EditHandler(String id, String groupName,
                           String contactperson, String mobileNo,
                           String email, String address, String state,
                           String city, String totalMember,
                           String selectpackage, String packageCharge,
                           String trainercheck, String trainerName,
                           String trainerCharge) {
            this.id = id;
            this.groupName = groupName;
            this.contactperson = contactperson;
            this.mobileNo = mobileNo;
            this.email = email;
            this.address = address;
            this.state = state;
            this.city = city;
            this.totalMember = totalMember;
            this.selectpackage = selectpackage;
            this.packageCharge = packageCharge;
            this.trainercheck = trainercheck;
            this.trainerName = trainerName;
            this.trainerCharge = trainerCharge;
        }

        @Override
        protected String doInBackground(String... params) {
            RequestBody formBody = new FormBody.Builder()
                    .add("id", id)
                    .add("groupname", groupName)
                    .add("cpersonname", contactperson)
                    .add("htmlne", mobileNo)
                    .add("emailid", email)
                    .add("address", address)
                    .add("state", state)
                    .add("city", city)
                    .add("totalmembers", totalMember)
                    .add("packid", this.selectpackage)
                    .add("traing_amt", packageCharge)
                    .add("prsntr", trainercheck)
                    .add("prsntrid", this.trainerName)
                    .add("pttraing_amt", trainerCharge)
                    .build();

            Request request = new Request.Builder()
                    .url(AddGroup_URL)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                       try{
                           progressBar.setVisibility(View.GONE);
                       }catch (Exception e1){

                       }
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String myResponse = response.body().string();
                    Add_GroupActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject  forecast = null;
                            try {
                                mSubmit.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                forecast = new JSONObject(myResponse);
                                Message = forecast.getString("message");
                                showDialog(Message);



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                    });


                }
            });

            return null;
        }

    }


    //for Post Request
    public class PostHandler extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();
        String groupName, contactperson, mobileNo, email, address,
                state,city,totalMember,selectpackage, packageCharge,
                trainercheck,trainerName,trainerCharge;

        public PostHandler(String groupName, String contactperson,
                           String mobileNo, String email, String address,
                           String state, String city, String totalMember,
                           String selectpackage, String packageCharge,
                           String trainercheck, String trainerName,
                           String trainerCharge) {
            this.groupName = groupName;
            this.contactperson = contactperson;
            this.mobileNo = mobileNo;
            this.email = email;
            this.address = address;
            this.state = state;
            this.city = city;
            this.totalMember = totalMember;
            this.selectpackage = selectpackage;
            this.packageCharge = packageCharge;
            this.trainercheck = trainercheck;
            this.trainerName = trainerName;
            this.trainerCharge = trainerCharge;
        }

        @Override
        protected String doInBackground(String... params) {
            RequestBody formBody = new FormBody.Builder()
                    .add("groupname", groupName)
                    .add("cpersonname", contactperson)
                    .add("htmlne", mobileNo)
                    .add("emailid", email)
                    .add("address", address)
                    .add("state", state)
                    .add("city", city)
                    .add("totalmembers", totalMember)
                    .add("packid", this.selectpackage)
                    .add("traing_amt", packageCharge)
                    .add("prsntr", trainercheck)
                    .add("prsntrid",this.trainerName)
                    .add("pttraing_amt", trainerCharge)
                    .build();

            Request request = new Request.Builder()
                    .url(AddGroup_URL)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    try {
                    progressBar.setVisibility(View.GONE);
                    } catch (Exception e1) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String myResponse = response.body().string();
                    Add_GroupActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject  forecast = null;
                            try {
                                mSubmit.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                forecast = new JSONObject(myResponse);
                                Message = forecast.getString("message");
                                showDialog(Message);



                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }


                    });


                }
            });

            return null;
        }

    }


    private boolean validation() {
        String group_name = mGroupName.getText().toString();
        String contact_person = mContactPerson.getText().toString();
        String contact_number = mContactNumber.getText().toString();
        String email_id = mEmailId.getText().toString();
        String address = mAddress.getText().toString();
        String state = mState.getText().toString();
        String city = mCity.getText().toString();
        String total_member = mTotalmember.getText().toString();
        boolean flag = true;

        if (group_name.isEmpty()){
            flag = false;
            mGroupName.setError(getResources().getString(R.string.plz_enter_group_name));
        }
        if (contact_person.isEmpty()){
            flag = false;
            mContactPerson.setError(getResources().getString(R.string.plz_enter_contact_person));
        }
        if ( contact_number.isEmpty()){
            flag = false;
            mContactNumber.setError(getResources().getString(R.string.plz_enter_contact_no));
        }
        if (email_id.isEmpty()){
            flag = false;
            mEmailId.setError(getResources().getString(R.string.plz_enter_email));
        }
        if (address.isEmpty()){
            flag = false;
            mAddress.setError(getResources().getString(R.string.plz_enter_address));
        }
        if (state.isEmpty()){
            flag = false;
            mState.setError(getResources().getString(R.string.plz_enter_state));
        }
        if (city.isEmpty()){
            flag = false;
            mCity.setError(getResources().getString(R.string.plz_enter_city));
        }
        if (total_member.isEmpty()){
            flag = false;
            mTotalmember.setError(getResources().getString(R.string.plz_enter_totol_members));
        }
        else if (contact_number.length()>0 && contact_number.length()<10){
            flag = false;
            Snackbar.make(mCoordinatorLayout,getResources().getString(R.string.plz_enter_valid_contact_no),Snackbar.LENGTH_SHORT).show();
        }
        else if (email_id.length()>0 && !email_id.matches(emailPattern)){
            flag = false;
            Snackbar.make(mCoordinatorLayout,getResources().getString(R.string.valid_error_valid_email),Snackbar.LENGTH_SHORT).show();
        }
        else if (total_member.isEmpty()){
            flag = false;
            Snackbar.make(mCoordinatorLayout,getResources().getString(R.string.plz_enter_totol_members),Snackbar.LENGTH_SHORT).show();
        }
        return flag;
    }


    public void showDialog(final String message) {

        final Dialog dialog = new Dialog(Add_GroupActivity.this);
        dialog.setContentView(R.layout.custom_alert_dialog_box);
        TextView mesg = dialog.findViewById(R.id.msg);
        mesg.setText(message);
        Button okbtn = dialog.findViewById(R.id.okbtn);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                /*Intent intent = new Intent(Add_GroupActivity.this, MemberGroupListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                ActivityOptions options = ActivityOptions.makeCustomAnimation(Add_GroupActivity.this,
                        R.anim.push_down_in, R.anim.push_down_out);
                startActivity(intent, options.toBundle());*/
                finish();
                overridePendingTransition(R.anim.push_down_in,R.anim.push_down_out);


            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();


    }

}

