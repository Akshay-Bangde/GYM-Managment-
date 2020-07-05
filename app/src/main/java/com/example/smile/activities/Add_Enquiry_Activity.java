package com.example.smile.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.smile.R;
import com.example.smile.utils.NetworkConnectivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.smile.activities.SignInActivity.BaseURL;

public class Add_Enquiry_Activity extends AppCompatActivity {

    EditText an_name, an_contact, an_email, an_city, an_referred,
            an_purpose, an_dateofenq, an_reason,an_remark, followUpDate;
    Spinner Spinnerpackage, an_inquiredby, an_status;
    ArrayList<String> packageArrayList,enquiredByArrayList,enquiry_status;
    Button enquiry_savebtn, enquiry_resetbtn;
    Calendar dateofenq, followUpDateCalender;
    LinearLayout layoutonee;
    CoordinatorLayout mCoordinatorLayout;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    final String Spinner_URL = BaseURL+"Inquiry/GetPackage" ;
    final String AddEnquiry_URL = BaseURL+"Inquiry" ;
    ProgressBar an_progressBar;

    String packname,total_amount,statusname,fullname,emptype;
    String Message;
   // String network_message="Please check your network connection.";
    String packageInterested, enquiredBy, statusVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__enquiry);

        init();
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



        SaveButton();
        bindControls();

        run(Spinner_URL);
    }


    private void bindControls(){

        an_dateofenq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Add_Enquiry_Activity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateofenq.set(Calendar.YEAR, year);
                        dateofenq.set(Calendar.MONTH, monthOfYear);
                        dateofenq.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();
                    }
                }, dateofenq.get(Calendar.YEAR), dateofenq.get(Calendar.MONTH), dateofenq.get(Calendar.DAY_OF_MONTH)).show();

            }
        });


        followUpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Add_Enquiry_Activity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        followUpDateCalender.set(Calendar.YEAR, year);
                        followUpDateCalender.set(Calendar.MONTH, monthOfYear);
                        followUpDateCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();
                    }
                }, followUpDateCalender.get(Calendar.YEAR),
                        followUpDateCalender.get(Calendar.MONTH),
                        followUpDateCalender.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        enquiry_resetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ResetButton();
            }
        });



        //code for status selection and show extra editTexts like addRemark and addReason
        an_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                statusVal = selectedItem;
                if(selectedItem.equals("Select"))
                {
                    layoutonee.setVisibility(View.GONE);
                    an_remark.setVisibility(View.GONE);
                    an_reason.setVisibility(View.GONE);

                }
                else if(selectedItem.equals("Reject"))
                {
                    layoutonee.setVisibility(View.GONE);
                    an_remark.setVisibility(View.GONE);
                    an_reason.setVisibility(View.VISIBLE);
                } else if(selectedItem.equals("Done"))
                {
                    layoutonee.setVisibility(View.GONE);
                    an_remark.setVisibility(View.GONE);
                    an_reason.setVisibility(View.GONE);
                }
                else if (selectedItem.equals("Follow Up")){
                    layoutonee.setVisibility(View.VISIBLE);
                    an_remark.setVisibility(View.VISIBLE);
                    an_reason.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //code for package spinner
        Spinnerpackage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                packageInterested = packageArrayList.get(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //code for enquired by spinner
        an_inquiredby.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                enquiredBy =  enquiredByArrayList.get(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }




    //all widgets are initialized here
    private void init() {
        an_name = findViewById(R.id.enq_name);
        an_contact = findViewById(R.id.enq_contact);
        an_email = findViewById(R.id.enq_email);
        an_city = findViewById(R.id.enq_city);
        an_referred = findViewById(R.id.enq_refrred);
        an_dateofenq = findViewById(R.id.enq_dateofenq);
        an_purpose = findViewById(R.id.enq_purpose);
        an_reason = findViewById(R.id.enq_reason);
        an_remark = findViewById(R.id.enq_remark);
        Spinnerpackage = findViewById(R.id.packageSpinner);
        an_inquiredby = findViewById(R.id.enq_enquired_by);
        an_status = findViewById(R.id.enq_status);
        enquiry_savebtn = findViewById(R.id.enq_save);
        enquiry_resetbtn = findViewById(R.id.enq_reset);
        layoutonee=findViewById(R.id.layout_one);
        mCoordinatorLayout = findViewById(R.id.add_enquiry);
        an_progressBar=findViewById(R.id.enq_progressbar);


        String currentDate = new SimpleDateFormat("dd - MMM - yyyy",
                Locale.getDefault()).format(new Date());
        dateofenq = Calendar.getInstance();
        an_dateofenq = findViewById(R.id.enq_dateofenq);
        an_dateofenq.setText(currentDate);

        followUpDateCalender = Calendar.getInstance();
        followUpDate = findViewById(R.id.enq_followupdate);
        followUpDate.setText(currentDate);

        // datepicker for date of enquiry
        final DatePickerDialog.OnDateSetListener date =
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        dateofenq.set(Calendar.YEAR, year);
                        dateofenq.set(Calendar.MONTH, monthOfYear);
                        dateofenq.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();

                    }
                };


        // datepicker for date of follow up Enquiry
        final DatePickerDialog.OnDateSetListener daate =
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        followUpDateCalender.set(Calendar.YEAR, year);
                        followUpDateCalender.set(Calendar.MONTH, monthOfYear);
                        followUpDateCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();

                    }
                };


        //Package adapter
        packageArrayList = new ArrayList<String>();
        packageArrayList.add("Select");

        //Enquired By adapter
        enquiredByArrayList = new ArrayList<String>();
        enquiredByArrayList.add("Select");

        //Enquiry Adapter
        enquiry_status = new ArrayList<String>();





     /*  an_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               statusVal = enquiry_status.get(i).toString();
           }

           @Override
           public void onNothingSelected(AdapterView<?> adapterView) {

           }
       });*/

    }
    /// End Oncreate




    //code for get API
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

                Add_Enquiry_Activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //JSONObject for Package Interested
                        try {
                            JSONObject forecast = new JSONObject(myResponse);
                            JSONArray jsonArray = forecast.getJSONArray("Packages");
                            for (int i = 0; i <jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                packname = object.getString("packname");
                                 total_amount = object.getString("total_amount");
                                 packageArrayList.add(packname);

                            }
                            ArrayAdapter<String> packageAdapter = new ArrayAdapter<String>(Add_Enquiry_Activity.this,
                                    android.R.layout.simple_list_item_1, packageArrayList);
                            packageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            Spinnerpackage.setAdapter(packageAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //JSONObject for Enquiry Status
                        try {
                            JSONObject forecastt = new JSONObject(myResponse);
                            JSONArray jsonArray = forecastt.getJSONArray("Status");
                            for (int i = 0; i <jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                statusname = object.getString("StatusName");
                                enquiry_status.add(statusname);
                            }
                            ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(Add_Enquiry_Activity.this,
                                    android.R.layout.simple_list_item_1, enquiry_status);
                            statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            an_status.setAdapter(statusAdapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //JSONObject for Enquired By


                        try {
                            JSONObject forecast = new JSONObject(myResponse);
                            JSONArray jsonArray = forecast.getJSONArray("Staff");
                            for (int i = 0; i <jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                fullname = object.getString("fullname");
                                emptype = object.getString("emptype");
                                enquiredByArrayList.add(fullname+" "+"("+emptype+")");
                            }
                            ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(Add_Enquiry_Activity.this,
                                    android.R.layout.simple_list_item_1, enquiredByArrayList);
                            statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            an_inquiredby.setAdapter(statusAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }





  //code for save button
    private void SaveButton() {

        enquiry_savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean networkConectivity = NetworkConnectivity.isConnected(Add_Enquiry_Activity.this);
                if (networkConectivity) {
                if (validation()) {
                    an_progressBar.setVisibility(View.VISIBLE);

                    String name = an_name.getText().toString();
                    String contact = an_contact.getText().toString();
                    String refferedBy = an_referred.getText().toString();
                    String packageInserted = packageInterested;
                    String dateOfEnquiry = an_dateofenq.getText().toString();
                    String enquiredBy1 = enquiredBy;
                    String status = statusVal;
                    String purpose = an_purpose.getText().toString();
                    String city = an_city.getText().toString();
                    String email = an_email.getText().toString();
                    String remark = an_remark.getText().toString();
                    String reason = an_reason.getText().toString();
                    String followUp_date = followUpDate.getText().toString();

                    PostHandler handler = new PostHandler(name, contact, refferedBy, packageInserted,
                            dateOfEnquiry, enquiredBy1, status, purpose, city, email, remark, reason, followUp_date);

                    String result = null;

                    try {
                        result = handler.execute(AddEnquiry_URL).get();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }

                     else{
                    NetworkConnectivity.networkConnetivityShowDialog(Add_Enquiry_Activity.this);

                }

            }
        });
    }


    //code for post API
    public class PostHandler extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();
        String name, contactno,email, city, reffby, pack_name, doe,
                inquiryby,  remark, reason,
                status, purpose, created_by, IsActive, FollowUp_Date;

        public PostHandler(String name, String contactno, String reffby,
                           String pack_name, String doe, String inquiryby,
                           String status,String purpose,String city, String email,  String remark, String reason, String FollowUp_Date) {
            this.name = name;
            this.contactno = contactno;
            this.reffby = reffby;
            this.pack_name = pack_name;
            this.doe = doe;
            this.inquiryby = inquiryby;
            this.status = status;
            this.purpose = purpose;
            this.city=city;
            this.email=email;
            this.remark=remark;
            this.reason=reason;
            this.FollowUp_Date=FollowUp_Date;

        }

        @Override
        protected String doInBackground(String... params) {
            RequestBody formBody = new FormBody.Builder()
                    .add("name", name)
                    .add("contactno", contactno)
                    .add("created_by", reffby)
                    .add("pack_name", pack_name)
                    .add("doe", doe)
                    .add("inquiryby", inquiryby)
                    .add("inquiry_status", status)
                    .add("purpose", purpose)
                    .add("city", city)
                    .add("email", email)
                    .add("reason", reason)
                    .add("FollowUp_Date", FollowUp_Date)
                    .build();

            Request request = new Request.Builder()
                    .url(AddEnquiry_URL)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String myResponse = response.body().string();
                    Add_Enquiry_Activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject  forecast = null;
                            try {
                                forecast = new JSONObject(myResponse);
                                an_progressBar.setVisibility(View.GONE);

                                ResetButton();
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


    private void updateLabel() {
        String myFormat = "dd - MMM - yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
        an_dateofenq.setText(sdf.format(dateofenq.getTime()));
        followUpDate.setText(sdf.format(followUpDateCalender.getTime()));
    }

//for validations


    public Boolean validation() {
        String name = an_name.getText().toString().trim();
        String contactNo = an_contact.getText().toString();
        String email = an_email.getText().toString().trim();
        String dateOfInquiry = an_dateofenq.getText().toString();
        String addPurpose = an_purpose.getText().toString().trim();


        Boolean flag = true;
        if (name.isEmpty()) {
            flag = false;
            an_name.setError(getResources().getString(R.string.valid_error_name));
        }
        if (contactNo.isEmpty()) {
            flag = false;
            an_contact.setError(getResources().getString(R.string.plz_enter_contact_no));
            }
        if (dateOfInquiry.isEmpty()) {
            flag = false;
            an_dateofenq.setError(getResources().getString(R.string.plz_select_dateOfEqnuiry));
        }
        if (addPurpose.isEmpty()) {
            flag = false;
            an_purpose.setError(getResources().getString(R.string.plz_enter_purpose));
        }


         if (email.length() > 0 && !email.matches(emailPattern)) {
            flag = false;
            Snackbar.make(mCoordinatorLayout, getResources().getString(R.string.valid_error_valid_email), Snackbar.LENGTH_SHORT).show();
        }



         else  if (Spinnerpackage.getSelectedItem().toString().equals("Select") ){
             flag=false;
             Snackbar.make(mCoordinatorLayout,getResources().getString(R.string.plz_select_package)
                     ,Snackbar.LENGTH_SHORT).show();
         }
         else if (an_inquiredby.getSelectedItem().toString().trim().equals("Select") ){
             flag=false;
             Snackbar.make(mCoordinatorLayout,getResources().getString(R.string.plz_select_enquiredby)
                     ,Snackbar.LENGTH_SHORT).show();
         }
         else if (an_status.getSelectedItem().toString().trim().equals("Select")){
             flag=false;
             Snackbar.make(mCoordinatorLayout,getResources().getString(R.string.plz_select_status)
                     ,Snackbar.LENGTH_SHORT).show();
         }

        else if (an_contact.length() < 10) {
            flag = false;
            Snackbar.make(mCoordinatorLayout, getResources().getString(R.string.plz_enter_valid_contact_no), Snackbar.LENGTH_SHORT).show();
        }





        return flag;
    }

//to show alertDialog
    public void showDialog(String network_message) {

        final Dialog dialog = new Dialog(Add_Enquiry_Activity.this);
        dialog.setContentView(R.layout.custom_alert_dialog_box);
        TextView mesg = dialog.findViewById(R.id.msg);
        mesg.setText(network_message);
        Button okbtn=dialog.findViewById(R.id.okbtn);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);

            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(600,500);
        dialog.show();
    }


    //code for reset fields
    private void ResetButton() {
        an_name.setText("");
        an_contact.setText("");
        an_email.setText("");
        an_city.setText("");
        an_referred.setText("");
        an_purpose.setText("");
        an_status.setSelection(0);
        an_inquiredby.setSelection(0);
        Spinnerpackage.setSelection(0);


    }

}
