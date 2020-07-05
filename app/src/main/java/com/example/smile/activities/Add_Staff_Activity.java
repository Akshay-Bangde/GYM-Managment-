package com.example.smile.activities;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
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
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRadioButton;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smile.R;
import com.example.smile.dataObjects.StaffListModel;
import com.example.smile.utils.CategoryCallBackInterface;
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

import com.example.smile.utils.NetworkConnectivity;

import static com.example.smile.activities.SignInActivity.BaseURL;


public class Add_Staff_Activity extends AppCompatActivity {

    private Spinner mFieldsSpinner;
    ArrayList<String> mFieldsList;
    private TextInputEditText mFullName, mContactNo,
            mEmailId, mState, mCity, mAddress, mBiomatricId, mSalary, mTrainingCharge;
    private EditText mDateOfJoining, mBirthDate;
    TextInputLayout mFullNameLay;
    private CoordinatorLayout mCoordiantorLayout;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Button mSave;
    Calendar myCalendar, mybirthCalender;
    private Integer REQUEST_CAMERA = 1, SELECT_FILE = 0;
    CircleImageView mImageView;
    RadioGroup mRadioGroup;

    final String Spinner_URL = BaseURL + "StaffReg";
    final String AddStaff_URL = BaseURL + "StaffReg";
    String Message;
    String Value = "";
    String photos = "";
    String staffType;
    ProgressBar progressBar;
    StaffListModel data = null;
    TextView toolbar_title;
    String role ="";
    AppCompatRadioButton maleButton, femaleButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staff);

        Intent intent = getIntent();
        if (intent != null) {
            try {
                data = (StaffListModel) intent.getSerializableExtra("packageData");
                role = intent.getStringExtra("role");
            } catch (Exception e) {
                Log.d("exception", e.toString());
            }
        }

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
        if(role != null && !role.isEmpty() && role.equals("edit")){
            toolbar_title.setText("Edit Staff");
        }else{
            toolbar_title.setText("Add Staff");
        }
        mImageView = findViewById(R.id.profile_image);
        maleButton = findViewById(R.id.maleRadioButton);
        femaleButton = findViewById(R.id.femaleRadioButton);
        mCoordiantorLayout = findViewById(R.id.add_staff_layout);
        mSave = findViewById(R.id.save);
        mFullNameLay = findViewById(R.id.full_nameLayout);
        mFullName = findViewById(R.id.full_name);
        mContactNo = findViewById(R.id.contact_no);
        mEmailId = findViewById(R.id.email_id);
        mState = findViewById(R.id.state);
        mCity = findViewById(R.id.city);
        mAddress = findViewById(R.id.address);
        mBiomatricId = findViewById(R.id.Bio_Matric_ID);
        mSalary = findViewById(R.id.salary);
        mTrainingCharge = findViewById(R.id.traning_charge);
        mDateOfJoining = findViewById(R.id.joining_date_generate);
        mBirthDate = findViewById(R.id.birth_date_generate);
        mFieldsSpinner = findViewById(R.id.fields);
        mRadioGroup = findViewById(R.id.radioGroup);
        progressBar = findViewById(R.id.progressBar);

        if (data != null) {
            mFullName.setText(data.getName());
            mContactNo.setText(data.getContactno());
            mEmailId.setText(data.getEmail());
            mState.setText(data.getState());
            mCity.setText(data.getCity());
            mAddress.setText(data.getAddress());
            mSalary.setText(data.getSalary());
            mTrainingCharge.setText(data.getTrainingCharge());
            mDateOfJoining.setText(data.getDoj());
            mBirthDate.setText(data.getDob());
            if(data.getGender() != null && (data.getGender().equals("M") || data.getGender().equals("Male"))){
                maleButton.setChecked(true);
            }
            else {
                femaleButton.setChecked(true);
            }

            if (data.getBioMetricId() != null) {
                mBiomatricId.setText(data.getBioMetricId());
            }else{
                mBiomatricId.setText("0");
            }

        }
        mFieldsList = new ArrayList<String>();
        mFieldsList.add("Select");

        String currentDate = new SimpleDateFormat("dd - MMM - yyyy",
                Locale.getDefault()).format(new Date());
        myCalendar = Calendar.getInstance();
        mBirthDate = findViewById(R.id.birth_date_generate);

        mybirthCalender = Calendar.getInstance();
        mDateOfJoining = findViewById(R.id.joining_date_generate);
        mDateOfJoining.setText(currentDate);
        //For Validation
        SaveButton();
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        //Radio Button select
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton button = (RadioButton) findViewById(checkedId);
                if (button.isChecked()) {
                    Value = button.getText().toString();
                }
            }
        });

        ClickHandler();
    }

    public void setSpinnerData(){
        String employeeType = data.getEmployeeType();
        int pos = 0;
        if(mFieldsList != null && mFieldsList.size() > 0) {
            for (int i = 0; i < mFieldsList.size(); i++) {
                if (employeeType.equals(mFieldsList.get(i))) {
                    pos = i;
                }
            }
        }
        mFieldsSpinner.setSelection(pos);
    }
    public void ClickHandler() {
        mFieldsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                staffType = mFieldsList.get(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    //Select Image from Mobile and Camera
    public void selectImage() {
        boolean checkPermissions = Permissions.checkPermission(Add_Staff_Activity.this);
        if (checkPermissions) {
            final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(Add_Staff_Activity.this);
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

    private void updateLabel() {
        String myFormat = "dd - MMM - yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
        mDateOfJoining.setText(sdf.format(myCalendar.getTime()));
        mBirthDate.setText(sdf.format(mybirthCalender.getTime()));
    }

    private void SaveButton() {

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean networkConectivity = NetworkConnectivity.isConnected(Add_Staff_Activity.this);
                if (networkConectivity) {
                    if (validation()) {
                        mSave.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);

                        if (data != null) {
                            String id = data.getId();
                            String empType = staffType;
                            String fullname = mFullName.getText().toString();
                            String phone = mContactNo.getText().toString();
                            String email = mEmailId.getText().toString();
                            String state = mState.getText().toString();
                            String city = mCity.getText().toString();
                            String address = mAddress.getText().toString();
                            String biomatricId = mBiomatricId.getText().toString();
                            String salary = mSalary.getText().toString();
                            String trainingCharge = mTrainingCharge.getText().toString();
                            String DOB = mBirthDate.getText().toString();
                            String DOJ = mDateOfJoining.getText().toString();
                            String gender = Value;

                            EditHandler handler = new EditHandler(id, empType, fullname, phone, email,
                                    state, city, address, biomatricId, salary, trainingCharge, DOB, DOJ, gender);

                            String result = null;
                            try {
                                result = handler.execute(AddStaff_URL).get();

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }

                        } else {
                            String empType = staffType;
                            String fullname = mFullName.getText().toString();
                            String phone = mContactNo.getText().toString();
                            String email = mEmailId.getText().toString();
                            String state = mState.getText().toString();
                            String city = mCity.getText().toString();
                            String address = mAddress.getText().toString();
                            String biomatricId = mBiomatricId.getText().toString();
                            String salary = mSalary.getText().toString();
                            String trainingCharge = mTrainingCharge.getText().toString();
                            String DOB = mBirthDate.getText().toString();
                            String DOJ = mDateOfJoining.getText().toString();
                            String gender = Value;

                            PostHandler handler = new PostHandler(empType, fullname, phone, email,
                                    state, city, address, biomatricId, salary, trainingCharge, DOB, DOJ, gender);

                            String result = null;
                            try {
                                result = handler.execute(AddStaff_URL).get();

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } else {
                    NetworkConnectivity.networkConnetivityShowDialog(Add_Staff_Activity.this);

                }
            }
        });

        calender();
        run(Spinner_URL);
    }

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

                Add_Staff_Activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //JSONObject for Package Interested
                        try {
                            JSONObject forecast = new JSONObject(myResponse);
                            JSONArray jsonArray = forecast.getJSONArray("StaffType");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String staffType = object.getString("Type");
                                mFieldsList.add(staffType);

                            }
                            ArrayAdapter<String> fieldAdpater = new ArrayAdapter<String>(Add_Staff_Activity.this,
                                    android.R.layout.simple_list_item_1, mFieldsList);
                            fieldAdpater.setDropDownViewResource(R.layout.spinner_dropdown_item);
                            mFieldsSpinner.setAdapter(fieldAdpater);

                            if (data != null){
                                setSpinnerData();
                            }
                        }

                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }

    public class EditHandler extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();
        String id,empType, fullname, phone, email, state, city, address,
                salary, biomatricId, trainingCharge, DOB, DOJ, gender;

        public EditHandler(String id, String empType, String fullname, String phone, String email, String state, String city, String address, String salary, String biomatricId, String trainingCharge, String DOB, String DOJ, String gender) {
            this.id = id;
            this.empType = empType;
            this.fullname = fullname;
            this.phone = phone;
            this.email = email;
            this.state = state;
            this.city = city;
            this.address = address;
            this.salary = salary;
            this.biomatricId = biomatricId;
            this.trainingCharge = trainingCharge;
            this.DOB = DOB;
            this.DOJ = DOJ;
            this.gender = gender;
        }

        @Override
        protected String doInBackground(String... params) {
            RequestBody formBody = new FormBody.Builder()
                    .add("id", id)
                    .add("photo_member", photos)
                    .add("emptype", empType)
                    .add("fullname", fullname)
                    .add("phone", phone)
                    .add("emailid", email)
                    .add("state", state)
                    .add("city", city)
                    .add("address", address)
                    .add("empsalary", salary)
                    .add("biomatricid", biomatricId)
                    .add("traincharge", trainingCharge)
                    .add("dob", DOB)
                    .add("doj", DOJ)
                    .add("gender", gender)
                    .build();

            Request request = new Request.Builder()
                    .url(AddStaff_URL)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String myResponse = response.body().string();
                    Add_Staff_Activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject forecast = null;
                            try {
                                forecast = new JSONObject(myResponse);
                                Message = forecast.getString("message");
                                mSave.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                reset();
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
        String empType, fullname, phone, email, state, city, address,
                salary, biomatricId, trainingCharge, DOB, DOJ, gender;

        public PostHandler(String empType, String fullname, String phone,
                           String email, String state, String city,
                           String address, String biomatricId,
                           String salary, String trainingCharge,
                           String DOB, String DOJ, String gender) {

            this.empType = empType;
            this.fullname = fullname;
            this.phone = phone;
            this.email = email;
            this.state = state;
            this.city = city;
            this.address = address;
            this.biomatricId = biomatricId;
            this.salary = salary;
            this.trainingCharge = trainingCharge;
            this.DOB = DOB;
            this.DOJ = DOJ;
            this.gender = gender;
        }

        @Override
        protected String doInBackground(String... params) {
            RequestBody formBody = new FormBody.Builder()

                    .add("photo_member", photos)
                    .add("emptype", empType)
                    .add("fullname", fullname)
                    .add("phone", phone)
                    .add("emailid", email)
                    .add("state", state)
                    .add("city", city)
                    .add("address", address)
                    .add("empsalary", salary)
                    .add("biomatricid", biomatricId)
                    .add("traincharge", trainingCharge)
                    .add("dob", DOB)
                    .add("doj", DOJ)
                    .add("gender", gender)
                    .build();

            Request request = new Request.Builder()
                    .url(AddStaff_URL)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String myResponse = response.body().string();
                    Add_Staff_Activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject forecast = null;
                            try {
                                forecast = new JSONObject(myResponse);
                                Message = forecast.getString("message");
                                mSave.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                reset();
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

        final Dialog dialog = new Dialog(Add_Staff_Activity.this);
        dialog.setContentView(R.layout.custom_alert_dialog_box);
        TextView mesg = dialog.findViewById(R.id.msg);
        mesg.setText(message);
        Button okbtn = dialog.findViewById(R.id.okbtn);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
                overridePendingTransition(R.anim.push_down_in,R.anim.push_down_out);


            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();


    }

    private void reset() {
        mFieldsSpinner.setSelection(0);
        mFullName.setText("");
        mContactNo.setText("");
        mEmailId.setText("");
        mState.setText("");
        mCity.setText("");
        mAddress.setText("");
        mBiomatricId.setText("");
        mSalary.setText("");
        mTrainingCharge.setText("");
        mDateOfJoining.setText("");
        mBirthDate.setText("");
        //  mRadioGroup.setOnCheckedChangeListener();


    }

    private void calender() {

        // datepicker for date of joining
        final DatePickerDialog.OnDateSetListener date =
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, monthOfYear);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();

                    }

                };

        mDateOfJoining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Add_Staff_Activity.this, R.style.DialogTheme, date,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }

            ;

        });


        // datePicker for birthdate
        final DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                mybirthCalender.set(Calendar.YEAR, year);
                mybirthCalender.set(Calendar.MONTH, monthOfYear);
                mybirthCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();

            }

        };
        mBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Add_Staff_Activity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        mybirthCalender.set(Calendar.YEAR, year);
                        mybirthCalender.set(Calendar.MONTH, monthOfYear);
                        mybirthCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();
                    }
                }, mybirthCalender.get(Calendar.YEAR), mybirthCalender.get(Calendar.MONTH), mybirthCalender.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

    }

    private boolean validation() {
        String full_name = mFullName.getText().toString();
        String contact_no = mContactNo.getText().toString();
        String date_of_joining = mDateOfJoining.getText().toString();
        String emailId = mEmailId.getText().toString();
        String city = mCity.getText().toString();
        String trainingCharge = mTrainingCharge.getText().toString();

        boolean flag = true;
        if (mFieldsSpinner.getSelectedItem().toString().trim().equals("Select")) {
            Snackbar.make(mCoordiantorLayout, getResources().getString(R.string.spinner), Snackbar.LENGTH_SHORT).show();
        }
        if (full_name.isEmpty()) {
            flag = false;
            mFullName.setError(getResources().getString(R.string.plz_enter_full_name));

        }
        if (contact_no.isEmpty()) {
            flag = false;
            mContactNo.setError(getResources().getString(R.string.plz_enter_contact_no));

        }
        if (emailId.isEmpty()) {
            flag = false;
            mEmailId.setError(getResources().getString(R.string.plz_enter_email));

        }
        if (city.isEmpty()) {
            flag = false;
            mCity.setError(getResources().getString(R.string.plz_enter_city));

        }
        if (trainingCharge.isEmpty()) {
            flag = false;
            mTrainingCharge.setError(getResources().getString(R.string.plz_enter_training_charge));

        } else if (contact_no.length() < 10) {
            flag = false;
            Snackbar.make(mCoordiantorLayout, getResources().getString(R.string.plz_enter_valid_contact_no), Snackbar.LENGTH_SHORT).show();
        } else if (emailId.length() > 0 && !emailId.matches(emailPattern)) {
            flag = false;
            Snackbar.make(mCoordiantorLayout, getResources().getString(R.string.valid_error_valid_email), Snackbar.LENGTH_SHORT).show();
        } else if (date_of_joining.isEmpty()) {
            flag = false;
            Snackbar.make(mCoordiantorLayout, getResources().getString(R.string.plz_select_date_of_joning), Snackbar.LENGTH_SHORT).show();

        }


        return flag;
    }

}


