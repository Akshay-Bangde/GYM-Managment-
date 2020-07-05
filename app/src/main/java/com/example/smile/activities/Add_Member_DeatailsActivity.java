package com.example.smile.activities;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smile.R;
import com.example.smile.dataObjects.DietPlanModel;
import com.example.smile.dataObjects.MemberListModel;
import com.example.smile.dataObjects.PackageModel;
import com.example.smile.dataObjects.StaffListModel;
import com.example.smile.utils.NetworkConnectivity;
import com.example.smile.utils.Permissions;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.smile.activities.SignInActivity.BaseURL;

public class Add_Member_DeatailsActivity extends AppCompatActivity {

    private Spinner spinner_batch, spinner_marital_status,
            spinner_diet_plan;
    ArrayList<String> batchList, marital_statusList, dietplan_List;

    String batches, martitalStatus, dietPlan;
    private EditText mName, mContactNo, mEmailId, mAddress, mState,
            mcity, mBirthdate, mDateOfJoining;
    ArrayList<DietPlanModel> arrayDietList;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private CoordinatorLayout mCoordinatorLayout;
    private LinearLayout mLinearLayout;
    String photos = "";
    String Message;
    String memberType = "";
    String genderType = "";
    AppCompatRadioButton male, femail;

    ProgressBar progressBarNew;
    RadioGroup memberTypeRadioGroup, genderRadioGroup;

    Button mNext;
    ProgressBar progressBar;
    Calendar mBirthdayCalender, mDOJoiningCalender, mGBirthdayCalender;
    private CircleImageView mImageView;
    private Integer REQUEST_CAMERA = 1, SELECT_FILE = 0;

    TextView mPackageTotalAmount;
    final String Spinner_URL = BaseURL + "MemberRegistration";
    final String DietPlanSpinner_URL = BaseURL + "DietPlanDropdown";
    final String AddMember_URL = BaseURL + "MemberRegistration";

    MemberListModel data = null;
    TextView toolbar_title;
    String role = "";
    RadioButton maleRadioButton, femaleRadioButton, individualRadioButton, groupRadioButton;


    String serialId,memberId ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__member__details);
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


        toolbar_title = findViewById(R.id.toolbar_title);
        arrayDietList = new ArrayList<>();

        Intent intent = getIntent();
        if (intent != null) {
            try {
                data = (MemberListModel) intent.getSerializableExtra("packageData");
                role = intent.getStringExtra("role");
            } catch (Exception e) {
                Log.d("exception", e.toString());
            }
        }

        progressBarNew= findViewById(R.id.pb);
        spinner_batch = findViewById(R.id.batch_spinner);
        maleRadioButton = findViewById(R.id.maleRadioButton);
        femaleRadioButton= findViewById(R.id.femaleRadioButton);
        individualRadioButton = findViewById(R.id.individual_radio_button);
        groupRadioButton = findViewById(R.id.group_radio_button);
        spinner_marital_status = findViewById(R.id.marital_status_spinner);
        spinner_diet_plan = findViewById(R.id.diet_plan_spinner);
        if (role != null && !role.isEmpty() && role.equals("edit")) {
            toolbar_title.setText("Edit Member");
        } else {

            toolbar_title.setText("Add Member");
        }
        FrameLayout frameLayout = findViewById(R.id.frame);
        progressBar = frameLayout.findViewById(R.id.progressBar);
        mImageView = findViewById(R.id.profile_image);


        //findViewById for Individual layout
        mNext = findViewById(R.id.next);
        mName = findViewById(R.id.name);
        mContactNo = findViewById(R.id.contact_no);
        mEmailId = findViewById(R.id.email_id);
        mAddress = findViewById(R.id.address);
        mState = findViewById(R.id.state);
        mcity = findViewById(R.id.city);
        mBirthdate = findViewById(R.id.birth_date_generate);
        mDateOfJoining = findViewById(R.id.joining_date_generate);
        mCoordinatorLayout = findViewById(R.id.coordinator_layout);
        mPackageTotalAmount = findViewById(R.id.totalamount);
        memberTypeRadioGroup = findViewById(R.id.memberTypeRadioGroup);
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        male = findViewById(R.id.maleRadioButton);
        femail = findViewById(R.id.femaleRadioButton);

        Button paybtn = findViewById(R.id.nextPay);
        paybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Add_Member_DeatailsActivity.this, Indiviual_Member_PaymentActivity.class);
                startActivity(intent);
            }
        });


        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        batchList = new ArrayList<String>();
        batchList.add("Select");

        marital_statusList = new ArrayList<String>();
        marital_statusList.add("Select");
        marital_statusList.add("Single");
        marital_statusList.add("Married");

        ArrayAdapter<String> maritalstatusAdapter = new ArrayAdapter<>(Add_Member_DeatailsActivity.this,
                android.R.layout.simple_list_item_1, marital_statusList);
        maritalstatusAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner_marital_status.setAdapter(maritalstatusAdapter);

        if(data != null){
            setmaritalStatus();
        }

        dietplan_List = new ArrayList<String>();
        dietplan_List.add("Select Diet Plan");


        run(Spinner_URL);
        diet_run(DietPlanSpinner_URL);
        nextButton();


        String currentDate = new SimpleDateFormat("dd - MMM - yyyy", Locale.getDefault()).format(new Date());

        mBirthdayCalender = Calendar.getInstance();
        mBirthdate = findViewById(R.id.birth_date_generate);
        mBirthdate.setText(currentDate);

        mDOJoiningCalender = Calendar.getInstance();
        mDateOfJoining = findViewById(R.id.joining_date_generate);
        mDateOfJoining.setText(currentDate);

        mGBirthdayCalender = Calendar.getInstance();


        //member type Radio Button select
        memberTypeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton button = (RadioButton) findViewById(checkedId);
                if (button.isChecked()) {
                    memberType = button.getText().toString();
                }
            }
        });

        //member type Radio Button select
        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton button = (RadioButton) findViewById(checkedId);
                if (button.isChecked()) {
                    genderType = button.getText().toString();
                }
            }
        });

        clicksHandler();

        if (data != null) {
            mName.setText(data.getName());
            mContactNo.setText(data.getContactno());
            mEmailId.setText(data.getEmail());
            mAddress.setText(data.getAddress());
            mState.setText(data.getState());
            mcity.setText(data.getCity());
            mBirthdate.setText(data.getDob());
            mDateOfJoining.setText(data.getDoj());
            if(data.getGender() != null && (data.getGender().equals("M") || data.getGender().equals("Male"))){
                maleRadioButton.setChecked(true);
            }else {
                femaleRadioButton.setChecked(true);
            }

            if(data.getMembertype() != null && (data.getMembertype().equals("individual") || data.getMembertype().equals("Individual"))){
                individualRadioButton.setChecked(true);
            }else {
                groupRadioButton.setChecked(true);
            }

        }
    }
    //End On Create


    //on Item Selected Package
    private void clicksHandler() {

        spinner_batch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                batches = batchList.get(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_marital_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                martitalStatus = marital_statusList.get(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_diet_plan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                //dietPlan = dietplan_List.get(i).toString();
                dietPlan = arrayDietList.get(i).getId().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    //post webservices
    private void nextButton() {

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean networkConectivity = NetworkConnectivity.isConnected(Add_Member_DeatailsActivity.this);
                if (networkConectivity) {
                    if (validation()) {
                        mNext.setVisibility(View.GONE);
                        progressBarNew.setVisibility(View.VISIBLE);
                        if (data != null) {
                            String id = data.getId();
                            String MemberType = memberType;
                            String fullname = mName.getText().toString();
                            String phone = mContactNo.getText().toString();
                            String email = mEmailId.getText().toString();
                            String address = mAddress.getText().toString();
                            String state = mState.getText().toString();
                            String city = mcity.getText().toString();
                            String gender = genderType;
                            String Batches = batches;
                            String marital_status = martitalStatus;
                            String DOB = mBirthdate.getText().toString();
                            String DOJ = mDateOfJoining.getText().toString();
                            String DietPlan = dietPlan;
                            EditHandler handler = new EditHandler(id, MemberType, fullname, phone, email,
                                    address, state, city, gender, Batches, marital_status, DOB, DOJ, DietPlan
                            );

                            String result = null;
                            try {
                                result = handler.execute(AddMember_URL).get();

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        } else {
                            String MemberType = memberType;
                            String fullname = mName.getText().toString();
                            String phone = mContactNo.getText().toString();
                            String email = mEmailId.getText().toString();
                            String address = mAddress.getText().toString();
                            String state = mState.getText().toString();
                            String city = mcity.getText().toString();
                            String gender = genderType;
                            String Batches = batches;
                            String marital_status = martitalStatus;
                            String DOB = mBirthdate.getText().toString();
                            String DOJ = mDateOfJoining.getText().toString();
                            String DietPlan = dietPlan;


                            PostHandler handler = new PostHandler(MemberType, fullname, phone, email,
                                    address, state, city, gender, Batches, marital_status, DOB, DOJ, DietPlan
                            );

                            String result = null;
                            try {
                                result = handler.execute(AddMember_URL).get();

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    NetworkConnectivity.networkConnetivityShowDialog(Add_Member_DeatailsActivity.this);

                }

            }
        });

        //Set a calender
        calender();

    }

    public void setbatches(){
        String batchName = data.getBatchid();
        int pos = 0;
        if(batchList != null && batchList.size() > 0) {
            for (int i = 0; i < batchList.size(); i++) {
                if (batchName.equals(batchList.get(i))) {
                    pos = i;
                }
            }
        }
        spinner_batch.setSelection(pos);
    }

    public void setmaritalStatus(){
        String maritalStatus = data.getMargstatus();
        if(maritalStatus != null && !maritalStatus.isEmpty() && maritalStatus.equals("Unmaried")){
            maritalStatus = "Single";
        }
        int pos = 0;
        if(marital_statusList != null && marital_statusList.size() > 0) {
            for (int i = 0; i < marital_statusList.size(); i++) {
                if (maritalStatus.equals(marital_statusList.get(i))) {
                    pos = i;
                }
            }
        }
        spinner_marital_status.setSelection(pos);
    }

    public void setDietPlan(){
        String dietPlan = data.getDietPlan();
        int pos = 0;
        if(dietplan_List != null && dietplan_List.size() > 0) {
            for (int i = 0; i < dietplan_List.size(); i++) {
                if (dietPlan.equals(dietplan_List.get(i))) {
                    pos = i;
                }
            }
        }
        spinner_diet_plan.setSelection(pos);
    }

    public class EditHandler extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();
        String id, memberType, fullname, phone, email, address, state, city,
                genderType, batches, maritalstatus, DOB, DOJ, dietPlan;

        public EditHandler(String id, String memberType, String fullname,
                           String phone, String email, String address,
                           String state, String city, String genderType,
                           String batches, String maritalstatus, String DOB,
                           String DOJ, String dietPlan) {
            this.id = id;
            this.memberType = memberType;
            this.fullname = fullname;
            this.phone = phone;
            this.email = email;
            this.address = address;
            this.state = state;
            this.city = city;
            this.genderType = genderType;
            this.batches = batches;
            this.maritalstatus = maritalstatus;
            this.DOB = DOB;
            this.DOJ = DOJ;
            this.dietPlan = dietPlan;
        }

        @Override
        protected String doInBackground(String... params) {
            RequestBody formBody = new FormBody.Builder()

                    .add("id", id)
                    .add("uploadprofile", photos)
                    .add("membertype", memberType)
                    .add("fullname", fullname)
                    .add("phone", phone)
                    .add("emailid", email)
                    .add("address", address)
                    .add("state", state)
                    .add("city", city)
                    .add("gender", genderType)
                    .add("batchid", batches)
                    .add("margstatus", maritalstatus)
                    .add("dob", DOB)
                    .add("doj", DOJ)
                    .add("dietplan", dietPlan)


                    .build();

            Request request = new Request.Builder()
                    .url(AddMember_URL)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String myResponse = response.body().string();
                    Add_Member_DeatailsActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject forecast = null;
                            try {
                                forecast = new JSONObject(myResponse);
                                mNext.setVisibility(View.VISIBLE);
                                progressBarNew.setVisibility(View.GONE);
                                Message = forecast.getString("message");
                                // reset();
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

    public class PostHandler extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();
        String memberType, fullname, phone, email, address, state, city,
                genderType, batches, maritalstatus, DOB, DOJ, dietPlan;

        public PostHandler(String memberType, String fullname,
                           String phone, String email, String address,
                           String state, String city, String genderType,
                           String batches, String maritalstatus,
                           String DOB, String DOJ, String dietPlan) {
            this.memberType = memberType;
            this.fullname = fullname;
            this.phone = phone;
            this.email = email;
            this.address = address;
            this.state = state;
            this.city = city;
            this.genderType = genderType;
            this.batches = batches;
            this.maritalstatus = maritalstatus;
            this.DOB = DOB;
            this.DOJ = DOJ;
            this.dietPlan = dietPlan;

        }

        @Override
        protected String doInBackground(String... params) {
            RequestBody formBody = new FormBody.Builder()

                    .add("uploadprofile", photos)
                    .add("membertype", memberType)
                    .add("fullname", fullname)
                    .add("phone", phone)
                    .add("emailid", email)
                    .add("address", address)
                    .add("state", state)
                    .add("city", city)
                    .add("gender", genderType)
                    .add("batchid", batches)
                    .add("margstatus", maritalstatus)
                    .add("dob", DOB)
                    .add("doj", DOJ)
                    .add("dietplan", dietPlan)


                    .build();

            Request request = new Request.Builder()
                    .url(AddMember_URL)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String myResponse = response.body().string();
                    Add_Member_DeatailsActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject forecast = null;
                            try {
                                forecast = new JSONObject(myResponse);
                                mNext.setVisibility(View.VISIBLE);
                                progressBarNew.setVisibility(View.GONE);

                                Message = forecast.getString("message");
                                serialId = forecast.getString("responseId");
                                memberId = forecast.getString("randomId");
                                MemberListModel memberListModel = new MemberListModel();
                                memberListModel.setId(serialId);
                                memberListModel.setPunchId(memberId);


                                // reset();
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

    public void showDialog(final String message) {

        final Dialog dialog = new Dialog(Add_Member_DeatailsActivity.this);
        dialog.setContentView(R.layout.custom_alert_dialog_box);
        TextView mesg = dialog.findViewById(R.id.msg);
        mesg.setText(message);
        Button okbtn = dialog.findViewById(R.id.okbtn);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
                Intent intent = new Intent(Add_Member_DeatailsActivity.this, Indiviual_Member_PaymentActivity.class);

                Bundle extras = new Bundle();
              //  extras.putString("memberID",memberId);
                extras.putString("serialId",serialId);
                intent.putExtras(extras);
                startActivity(intent);



            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();


    }

    //Get SPINNER DATA API+
    public void run(String url) {
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

                Add_Member_DeatailsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //JSONObject for Batches
                        try {
                            JSONObject forecast = new JSONObject(myResponse);
                            JSONArray jsonArray = forecast.getJSONArray("Batchs");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String BatchName = object.getString("BatchName");
                                batchList.add(BatchName);

                            }

                            ArrayAdapter<String> batchAdapter = new ArrayAdapter<String>(Add_Member_DeatailsActivity.this,
                                    android.R.layout.simple_list_item_1, batchList);
                            batchAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                            spinner_batch.setAdapter(batchAdapter);
                            if(data != null){
                                setbatches();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
        });
    }

    public void diet_run(String url) {
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

                Add_Member_DeatailsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //JSONObject for Batches
                        try {
                            JSONObject forecast = new JSONObject(myResponse);
                            JSONArray jsonArray = forecast.getJSONArray("Diet");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String dietPlan = object.getString("planname");
                                String id = object.getString("id");
                                String dietplans_id = object.getString("dietplans_id");
                                dietplan_List.add(dietPlan);

                                DietPlanModel dietPlanModel = new DietPlanModel();
                                dietPlanModel.setId(id);
                                dietPlanModel.setDietplans_id(dietplans_id);
                                dietPlanModel.setPlanname(dietPlan);
                                arrayDietList.add(dietPlanModel);


                            }
                            ArrayAdapter<String> dietPlanAdapter = new ArrayAdapter<>(Add_Member_DeatailsActivity.this,
                                    android.R.layout.simple_list_item_1, dietplan_List);
                            dietPlanAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                            spinner_diet_plan.setAdapter(dietPlanAdapter);

                            if(data != null){
                                setDietPlan();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
        });
    }

    private void SelectImage() {
        boolean checkPermissions = Permissions.checkPermission(Add_Member_DeatailsActivity.this);
        if (checkPermissions) {
            final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(Add_Member_DeatailsActivity.this);
            builder.setTitle("Add Photo !");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    if (items[i].equals("Camera")) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, REQUEST_CAMERA);

                    } else if (items[i].equals("Gallery")) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);
                    } else if (items[i].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bundle bundle = data.getExtras();
                final Bitmap bitmap = (Bitmap) bundle.get("data");
                getBase64Image(bitmap);
                mImageView.setImageBitmap(bitmap);
            } else if (requestCode == SELECT_FILE) {
                Uri SelectedImageUri = data.getData();
                mImageView.setImageURI(SelectedImageUri);
            }
        } else if (requestCode == RESULT_CANCELED) {
            Toast.makeText(this, getResources().getString(R.string.picture_was_not_taken), Toast.LENGTH_SHORT).show();
        }
    }

    public void getBase64Image(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArrayImage = baos.toByteArray();
        photos = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
    }

    public void updateLabel() {
        String myFormat = "dd - MMM - yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
        mBirthdate.setText(sdf.format(mBirthdayCalender.getTime()));
        mDateOfJoining.setText(sdf.format(mDOJoiningCalender.getTime()));

    }

    private void calender() {
        // Date picker for Birthday
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mBirthdayCalender.set(Calendar.YEAR, year);
                mBirthdayCalender.set(Calendar.MONTH, month);
                mBirthdayCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        mBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Add_Member_DeatailsActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mBirthdayCalender.set(Calendar.YEAR, year);
                        mBirthdayCalender.set(Calendar.MONTH, month);
                        mBirthdayCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();
                    }
                }, mBirthdayCalender.get(Calendar.YEAR), mBirthdayCalender.get(Calendar.MONTH), mBirthdayCalender.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Date picker for joining date
        DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mDOJoiningCalender.set(Calendar.YEAR, year);
                mDOJoiningCalender.set(Calendar.MONTH, month);
                mDOJoiningCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        mDateOfJoining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Add_Member_DeatailsActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mDOJoiningCalender.set(Calendar.YEAR, year);
                        mDOJoiningCalender.set(Calendar.MONTH, month);
                        mDOJoiningCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();
                    }
                }, mDOJoiningCalender.get(Calendar.YEAR), mDOJoiningCalender.get(Calendar.MONTH), mDOJoiningCalender.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private boolean validation() {
        boolean flag = true;
        String name = mName.getText().toString();
        String contactNo = mContactNo.getText().toString();
        String emailId = mEmailId.getText().toString();
        String address = mAddress.getText().toString();
        String state = mState.getText().toString();
        String city = mcity.getText().toString();

        if (name.isEmpty()) {
            flag = false;
            mName.setError(getResources().getString(R.string.plz_enter_name));
        }
        if (contactNo.isEmpty()) {
            flag = false;
            mContactNo.setError(getResources().getString(R.string.plz_enter_contact_no));
        }
        if (emailId.isEmpty()) {
            flag = false;
            mEmailId.setError(getResources().getString(R.string.plz_enter_email));
        }
        if (address.isEmpty()) {
            flag = false;
            mAddress.setError(getResources().getString(R.string.plz_enter_address));
        }
        if (state.isEmpty()) {
            flag = false;
            mState.setError(getResources().getString(R.string.plz_enter_state));
        }
        if (city.isEmpty()) {
            flag = false;
            mcity.setError(getResources().getString(R.string.plz_enter_city));
        } else if (contactNo.length() < 10) {
            flag = false;
            Snackbar.make(mCoordinatorLayout, getResources().getString(R.string.plz_enter_valid_contact_no), Snackbar.LENGTH_SHORT).show();
        } else if (emailId.length() > 0 && !emailId.matches(emailPattern)) {
            flag = false;
            Snackbar.make(mCoordinatorLayout, getResources().getString(R.string.valid_error_valid_email), Snackbar.LENGTH_SHORT).show();
        } else if (spinner_batch.getSelectedItem().toString().trim().equals("Select")) {
            flag = false;
            Snackbar.make(mCoordinatorLayout, getResources().getString(R.string.plz_select_batch), Snackbar.LENGTH_SHORT).show();
        } else if (spinner_marital_status.getSelectedItem().toString().trim().equals("Marital Status")) {
            flag = false;
            Snackbar.make(mCoordinatorLayout, getResources().getString(R.string.plz_select_marital_status), Snackbar.LENGTH_SHORT).show();
        }


        return flag;
    }


}
