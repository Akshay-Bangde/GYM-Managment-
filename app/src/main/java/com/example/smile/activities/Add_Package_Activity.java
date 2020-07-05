package com.example.smile.activities;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.smile.R;
import com.example.smile.dataObjects.ActivitiesModel;
import com.example.smile.dataObjects.DurationModel;
import com.example.smile.dataObjects.GYMPackageListModel;
import com.example.smile.dataObjects.StaffListModel;
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

public class Add_Package_Activity extends AppCompatActivity {

    Button mSaveBtn,mAddBtn;
    EditText mPackageName, mDetails, mPackageAmount;
    LinearLayout mLinearLayout;
    Spinner mDurationSpinner, mActivityNameSpinner;
    ArrayList<String> durationArrayList;
    ArrayList<String> activityNameArrayList;
    TextView mtotalAmount,mTotalActivityAmount;
    String durationSpinnerURL =BaseURL+"gymPackager";
    String activitySpinnerURL = BaseURL+"ActivityDropDown";
    final String AddPackage_URL = BaseURL + "gymPackager";
    GYMPackageListModel data = null;
    String mDuration,mActvityname;
    ArrayList<DurationModel> mDurationModels;
    ArrayList<ActivitiesModel> mActivitiesModels;
    String packDurationId,activitiID;
    TextView toolbar_title;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__package);

        Intent intent = getIntent();
        if (intent != null) {
            try {
                data = (GYMPackageListModel) intent.getSerializableExtra("packageData");
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

        init();

        DurationRun(durationSpinnerURL);
        ActivityRun(activitySpinnerURL);
        SaveButton();

        if (data != null) {
            mPackageName.setText(data.getPackagename());
            if (data.getDetails() != null){
                mDetails.setText(data.getDetails());
            }
            else {
                mDetails.setText("");
            }
            mPackageAmount.setText(data.getPackageamount());
            mtotalAmount.setText(data.getTotalamount());
            mTotalActivityAmount.setText(data.getActivityamount());
            toolbar_title.setText("Edit GYM Package");

        }

        mDurationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                mDuration = durationArrayList.get(position).toString();

                if(mDurationModels != null && mDurationModels.size() > 0)
                    packDurationId = mDurationModels.get(position).durationId.toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mActivityNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0){
                    mTotalActivityAmount.setText("0");
                }else {
                    if(mActivitiesModels != null && mActivitiesModels.size() > 0)
                        mTotalActivityAmount.setText(mActivitiesModels.get(position - 1).activityCharge);

                }

                mActvityname = activityNameArrayList.get(position).toString();
                if(mActivitiesModels != null && mActivitiesModels.size() > 0)
                    activitiID = mActivitiesModels.get(position).activitiID.toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mtotalAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPackageAmount.getText().toString().isEmpty()
                        || mTotalActivityAmount.getText().toString().isEmpty())

                {
                    mtotalAmount.setText("0");
                } else {

                    long subtotal = (Long.parseLong(mPackageAmount.getText().toString())) +
                            (Long.parseLong(mTotalActivityAmount.getText().toString()));
                    String subtotall = Long.toString(subtotal);
                    mtotalAmount.setText(subtotall);
                }
            }
        });

    }

    public void setSpinnerData(){
        String duration = data.getDuration();
        int pos = 0;
        if(durationArrayList != null && durationArrayList.size() > 0) {
            for (int i = 0; i < durationArrayList.size(); i++) {
                if (duration.equals(durationArrayList.get(i))) {
                    pos = i;
                }
            }
        }
        mDurationSpinner.setSelection(pos);

        String activity = data.getDuration();
        int posi = 0;
        if(activityNameArrayList != null && activityNameArrayList.size() > 0) {
            for (int i = 0; i < activityNameArrayList.size(); i++) {
                if (activity.equals(activityNameArrayList.get(i))) {
                    posi = i;
                }
            }
        }
        mActivityNameSpinner.setSelection(posi);
    }



    public void init(){
        toolbar_title = findViewById(R.id.toolbar_title);
        progressBar=findViewById(R.id.progressBar);
        mPackageName = findViewById(R.id.package_name);
        mPackageAmount = findViewById(R.id.charge);
        mDetails = findViewById(R.id.details);
        mAddBtn = findViewById(R.id.add_Btn);
        mSaveBtn = findViewById(R.id.save);
        mDurationSpinner = findViewById(R.id.duration_spinner);
        mActivityNameSpinner = findViewById(R.id.activity_spinner);
        mLinearLayout = findViewById(R.id.linear_layout);
        mTotalActivityAmount = findViewById(R.id.totalActivityAmount);
        mtotalAmount = findViewById(R.id.totalAmount);

        //Code For Duration Spinner
        mActivitiesModels = new ArrayList<>();
        mDurationModels = new ArrayList<>();
        durationArrayList = new ArrayList<>();
        durationArrayList.add("Select");

        //Code For Activity Spinner
        activityNameArrayList = new ArrayList<>();
        activityNameArrayList.add("Select");


    }

    public void SaveButton(){
       mSaveBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (validation()){
                   boolean networkConectivity = NetworkConnectivity.isConnected(Add_Package_Activity.this);
                   if (networkConectivity) {
                       if (validation()) {
                           mSaveBtn.setVisibility(View.GONE);
                           progressBar.setVisibility(View.VISIBLE);
                           if (data != null) {
                               String id = data.getId();
                               String packname = mPackageName.getText().toString();
                               String details = mDetails.getText().toString();
                               String duration = packDurationId;
                               String packAmount = mPackageAmount.getText().toString();
                               String selectActivity = activitiID;
                               String activityAmount = mTotalActivityAmount.getText().toString();
                               String totalAmount = mtotalAmount.getText().toString();

                               EditHandler handler = new EditHandler(id,packname, details, duration, packAmount,
                                       selectActivity, activityAmount, totalAmount);

                               String result = null;
                               try {
                                   result = handler.execute(AddPackage_URL).get();

                               } catch (InterruptedException e) {
                                   e.printStackTrace();
                               } catch (ExecutionException e) {
                                   e.printStackTrace();
                               }
                           }
                           else {
                               String packname = mPackageName.getText().toString();
                               String details = mDetails.getText().toString();
                               String duration = packDurationId;
                               String packAmount = mPackageAmount.getText().toString();
                               String selectActivity = activitiID;
                               String activityAmount = mTotalActivityAmount.getText().toString();
                               String totalAmount = mtotalAmount.getText().toString();

                               PostHandler handler = new PostHandler(packname, details, duration, packAmount,
                                       selectActivity, activityAmount, totalAmount);

                               String result = null;
                               try {
                                   result = handler.execute(AddPackage_URL).get();

                               } catch (InterruptedException e) {
                                   e.printStackTrace();
                               } catch (ExecutionException e) {
                                   e.printStackTrace();
                               }
                           }
                       }
                   } else {
                       NetworkConnectivity.networkConnetivityShowDialog(Add_Package_Activity.this);

                   }
               }
           }
       });

   }


    public class EditHandler extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();
        String id,name, details, duration, packAmount,
                selectActivity, activityAmount, totalAmount;

        public EditHandler(String id, String name, String details,
                           String duration, String packAmount,
                           String selectActivity, String activityAmount,
                           String totalAmount) {
            this.id = id;
            this.name = name;
            this.details = details;
            this.duration = duration;
            this.packAmount = packAmount;
            this.selectActivity = selectActivity;
            this.activityAmount = activityAmount;
            this.totalAmount = totalAmount;
        }

        @Override
        protected String doInBackground(String... params) {
            RequestBody formBody = new FormBody.Builder()
                    .add("id", id)
                    .add("packname", name)
                    .add("details", details)
                    .add("duration", duration)
                    .add("charges", packAmount)
                    .add("selactvt", selectActivity)
                    .add("total_actamount", activityAmount)
                    .add("total_amount", totalAmount)

                    .build();

            Request request = new Request.Builder()
                    .url(AddPackage_URL)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String myResponse = response.body().string();
                    Add_Package_Activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject forecast = null;
                            try {
                                forecast = new JSONObject(myResponse);
                                String Message = forecast.getString("message");
                                mSaveBtn.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                                showDialog(Message);
                                toolbar_title.setText("Add GYM Package");

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
        String name, details, duration, packAmount,
                selectActivity, activityAmount, totalAmount;

        public PostHandler(String name, String details, String duration,
                           String packAmount, String selectActivity,
                           String activityAmount, String totalAmount) {
            this.name = name;
            this.details = details;
            this.duration = duration;
            this.packAmount = packAmount;
            this.selectActivity = selectActivity;
            this.activityAmount = activityAmount;
            this.totalAmount = totalAmount;
        }

        @Override
        protected String doInBackground(String... params) {
            RequestBody formBody = new FormBody.Builder()

                    .add("packname", name)
                    .add("details", details)
                    .add("duration", duration)
                    .add("charges", packAmount)
                    .add("selactvt", selectActivity)
                    .add("total_actamount", activityAmount)
                    .add("total_amount", totalAmount)

                    .build();

            Request request = new Request.Builder()
                    .url(AddPackage_URL)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String myResponse = response.body().string();
                    Add_Package_Activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject forecast = null;
                            try {
                                forecast = new JSONObject(myResponse);
                                String Message = forecast.getString("message");
                                mSaveBtn.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
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

        final Dialog dialog = new Dialog(Add_Package_Activity.this);
        dialog.setContentView(R.layout.custom_alert_dialog_box);
        TextView mesg = dialog.findViewById(R.id.msg);
        mesg.setText(message);
        Button okbtn = dialog.findViewById(R.id.okbtn);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
               /* Intent intent = new Intent(Add_Package_Activity.this, Package_List_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                ActivityOptions options = ActivityOptions.makeCustomAnimation(Add_Package_Activity.this,
                        R.anim.push_down_in, R.anim.push_down_out);
                startActivity(intent, options.toBundle());*/

                finish();
                overridePendingTransition(R.anim.push_down_in,R.anim.push_down_out);


            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();


    }

    // Get webservice for duration Spinner
    public void DurationRun(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String myResponse = response.body().string();

                Add_Package_Activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //JSONObject for Package Interested
                        try {
                            JSONObject forecast = new JSONObject(myResponse);
                            JSONArray jsonArray = forecast.getJSONArray("ddPackdata");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String duration = object.getString("duration");
                                String durationId = object.getString("packduration_id");
                                durationArrayList.add(duration);


                                DurationModel durationModel = new DurationModel();
                                durationModel.duration = duration;
                                durationModel.durationId = durationId;
                                mDurationModels.add(durationModel);

                            }
                            ArrayAdapter durationAdapter = new ArrayAdapter<>(Add_Package_Activity.this,
                                    android.R.layout.simple_list_item_1,durationArrayList);
                            durationAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                            mDurationSpinner.setAdapter(durationAdapter);

                            if (data != null){
                                setSpinnerData();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }

    // Get webservice for duration Spinner
    public void ActivityRun(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String myResponse = response.body().string();

                Add_Package_Activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //JSONObject for Package Interested
                        try {
                            JSONObject forecast = new JSONObject(myResponse);
                            JSONArray jsonArray = forecast.getJSONArray("ADDown");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String activitiId = object.getString("activitie_id");
                                String actvtname = object.getString("actvtname");
                                String actvtCharge = object.getString("charges");
                                activityNameArrayList.add(actvtname);

                                ActivitiesModel activitiesModel = new ActivitiesModel();
                                activitiesModel.activitiID = activitiId;
                                activitiesModel.activityName = actvtname;
                                activitiesModel.activityCharge = actvtCharge;
                                mActivitiesModels.add(activitiesModel);



                            }
                            ArrayAdapter activityAdapter = new ArrayAdapter<>(Add_Package_Activity.this,
                                    android.R.layout.simple_list_item_1,activityNameArrayList);
                            activityAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                            mActivityNameSpinner.setAdapter(activityAdapter);

                            if (data != null){
                                setSpinnerData();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }


    private Boolean validation() {
     boolean flag = true;
        String packageName = mPackageName.getText().toString();
        String packageDetails = mDetails.getText().toString();
        String packageAmount = mPackageAmount.getText().toString();

        if (packageName.isEmpty() ){
            flag = false;
            mPackageName.setError(getResources().getString(R.string.plz_enter_package_name));
            }
        if ( packageDetails.isEmpty()){
            flag = false;
            mDetails.setError(getResources().getString(R.string.plz_enter_details));
        } if ( packageAmount.isEmpty()){
            flag = false;
            mPackageAmount.setError(getResources().getString(R.string.plz_enter_package_amount));
        } if (mDurationSpinner.getSelectedItem().toString().trim().equals("Select")) {
            flag = false;
            Snackbar.make(mLinearLayout, getResources().getString(R.string.plz_select_the_duration), Snackbar.LENGTH_SHORT).show();
        }
        return flag;
    }
}
