package com.example.smile.activities;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.smile.R;
import com.example.smile.dataObjects.ActivityListModel;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.smile.activities.SignInActivity.BaseURL;

public class AddGymActivity_Activity extends AppCompatActivity {

    Button mSaveBtn;
    EditText mActivityName, mCharge, mDetails;
    LinearLayout mLinearLayout;
    String Add_Activity_URL=BaseURL+"Activity";
    String Message;
    ActivityListModel data = null;
    TextView toolbar_title ;
    ProgressBar AddProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gym_activity);

        Intent intent = getIntent();
        if(intent != null ){
            try {
                data = (ActivityListModel) intent.getSerializableExtra("packageData");
            }catch (Exception e){
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

        mLinearLayout = findViewById(R.id.linear_layout);
        mActivityName = findViewById(R.id.activity_name);
        mCharge = findViewById(R.id.charge);
        mDetails = findViewById(R.id.details);
        mSaveBtn = findViewById(R.id.save);
        toolbar_title = findViewById(R.id.toolbar_title);
        AddProgress=findViewById(R.id.pb);

        if(data != null){
            mCharge.setText(data.getCharge());
            if(data.getDetails() != null) {
                mDetails.setText(data.getDetails());
            }else{
                mDetails.setText("");
            }
            mActivityName.setText(data.getActivityname());

            toolbar_title.setText("Edit Activity Package");
        }

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (validation()) {
                mSaveBtn.setVisibility(View.GONE);
                AddProgress.setVisibility(View.VISIBLE);

                String activityName = mActivityName.getText().toString();
                String activityCharges = mCharge.getText().toString();
                String activityDetails = mDetails.getText().toString();

                if (data != null) {
                    String id = data.getId();
                    EditHandler handler = new EditHandler(id,activityName, activityCharges, activityDetails);

                    String result = null;
                    try {
                        result = handler.execute(Add_Activity_URL).get();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                } else {

                    PostHandler handler = new PostHandler(activityName, activityCharges, activityDetails);

                    String result = null;
                    try {
                        result = handler.execute(Add_Activity_URL).get();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
            }
        });
    }


    public class EditHandler extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();
        String name, amount, details, id;

        public EditHandler(String id,String name, String amount, String details) {
            this.name = name;
            this.amount = amount;
            this.details = details;
            this.id = id;
        }

        @Override
        protected String doInBackground(String... params) {
            RequestBody formBody = new FormBody.Builder()

                    .add("actvtname", name)
                    .add("charges", amount)
                    .add("details", details)
                    .add("id",id)
                    .build();

            Request request = new Request.Builder()
                    .url(Add_Activity_URL)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String myResponse = response.body().string();
                    AddGymActivity_Activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject forecast = null;
                            try {
                                forecast = new JSONObject(myResponse);
                                Message = forecast.getString("message");
                                mSaveBtn.setVisibility(View.VISIBLE);
                                AddProgress.setVisibility(View.GONE);
                                reset();
                                showDialog(Message);
                                toolbar_title.setText("Add Activity Package");

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
        String name, amount, details;

        public PostHandler(String name, String amount, String details) {
            this.name = name;
            this.amount = amount;
            this.details = details;
        }

        @Override
        protected String doInBackground(String... params) {
            RequestBody formBody = new FormBody.Builder()
                    .add("actvtname", name)
                    .add("charges", amount)
                    .add("details", details)
                    .build();

            Request request = new Request.Builder()
                    .url(Add_Activity_URL)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String myResponse = response.body().string();
                    AddGymActivity_Activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject forecast = null;
                            try {
                                forecast = new JSONObject(myResponse);
                                Message = forecast.getString("message");
                                mSaveBtn.setVisibility(View.VISIBLE);
                                AddProgress.setVisibility(View.GONE);
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

    public void showDialog(final String message){

        final Dialog dialog = new Dialog(AddGymActivity_Activity.this);
        dialog.setContentView(R.layout.custom_alert_dialog_box);
        TextView mesg = dialog.findViewById(R.id.msg);
        mesg.setText(message);
        Button okbtn=dialog.findViewById(R.id.okbtn);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                /*Intent intent = new Intent(AddGymActivity_Activity.this, GymActivityList_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                ActivityOptions options = ActivityOptions.makeCustomAnimation(AddGymActivity_Activity.this, R.anim.push_down_in, R.anim.push_down_out);
                startActivity(intent, options.toBundle());*/
                finish();
                overridePendingTransition(R.anim.push_down_in,R.anim.push_down_out);

            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();



    }


    public  void reset(){
        mActivityName.setText("");
        mCharge.setText("");
        mDetails.setText("");
    }

    public boolean validation(){
        String activityname = mActivityName.getText().toString();
        String charge = mCharge.getText().toString();
        String details = mDetails.getText().toString();

        Boolean flag = true;
        if (activityname.isEmpty())
        {
            flag = false;
            mActivityName.setError(getResources().getString(R.string.plz_enter_activity_name));
        }
        if (charge.isEmpty())
        {
            flag = false;
            mCharge.setError(getResources().getString(R.string.plz_enter_charge));
        }
        if (details.isEmpty())
        {
            flag = false;
           mDetails.setError(getResources().getString(R.string.plz_enter_details));
        }
        return flag;
    }

}
