package com.example.smile.activities;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.smile.R;
import com.example.smile.dataObjects.DietModel;
import com.example.smile.dataObjects.Product_List_Model;
import com.example.smile.utils.NetworkConnectivity;

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

public class Add_Diet_Plan_Activity extends AppCompatActivity {
    EditText dietplanedt, preworkedt, postworkedt, breakfatsedt, msnacksedt, lunchedt, esnacksedt, dinneredt, mealedt;
    Button mSave;
    String diet_URL=BaseURL+"DietPlan/AddDietPlnData";
    String Message;
    ProgressBar dietProgress;
    DietModel data = null;
    TextView heading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__diet__plan_);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backbutton);
        dietProgress=findViewById(R.id.pb);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            }
        });

        init();
        Intent intent = getIntent();
        if (intent != null) {

            try {
                data = (DietModel) intent.getSerializableExtra("packageData");
                if(data != null){
                    heading.setText(getResources().getString(R.string.edit_activity_diet_plan));
                    if(data.getBreakFast() != null) {
                        breakfatsedt.setText(data.getBreakFast());
                    }else{
                        breakfatsedt.setText("");
                    }

                    if(data.getMornSnk() != null) {
                        msnacksedt.setText(data.getMornSnk());
                    }else{
                        msnacksedt.setText("");
                    }

                    if(data.getEveSnk() != null) {
                        esnacksedt.setText(data.getEveSnk());
                    }else{
                        esnacksedt.setText("");
                    }

                    if(data.getMeall() != null) {
                        mealedt.setText(data.getMeall());
                    }else{
                        mealedt.setText("");
                    }
                    lunchedt.setText(data.getLunchh());
                    dietplanedt.setText(data.getPlanName());
                    preworkedt.setText(data.getPreWorkout());
                    postworkedt.setText(data.getPostWorkout());
                    dinneredt.setText(data.getDinnerr());

                }
                else{
                    heading.setText(getResources().getString(R.string.title_activity_diet_plan_));
                }

            }
            catch (Exception e) {
                Log.d("exception", e.toString());
            }
        }


        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean networkConectivity = NetworkConnectivity.isConnected(Add_Diet_Plan_Activity.this);
                if (networkConectivity) {
                    if (validation()) {
                        mSave.setVisibility(View.GONE);
                        dietProgress.setVisibility(View.VISIBLE);
                        String dietName = dietplanedt.getText().toString();
                        String preWork = preworkedt.getText().toString();
                        String postWork = postworkedt.getText().toString();
                        String breakFast = breakfatsedt.getText().toString();
                        String snack = msnacksedt.getText().toString();
                        String lunch = lunchedt.getText().toString();
                        String eveSnack = esnacksedt.getText().toString();
                        String dinner = dinneredt.getText().toString();
                        String meal = mealedt.getText().toString();


                        if (data != null) {
                            String id = data.getId();
                            EditHandler handler = new EditHandler(id, dietName, preWork, postWork, breakFast,
                                    snack, lunch, eveSnack, dinner, meal);

                            String result = null;
                            try {
                                result = handler.execute(diet_URL).get();

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        } else {

                            PostHandler handler = new PostHandler(dietName, preWork, postWork, breakFast,
                                    snack, lunch, eveSnack, dinner, meal);

                            String result = null;

                            try {
                                result = handler.execute().get();

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
                else {
                    NetworkConnectivity.networkConnetivityShowDialog(Add_Diet_Plan_Activity.this);

                }
            }
        });

    }



    public void init(){

        dietplanedt = findViewById(R.id.dietplan_edittext);
        preworkedt = findViewById(R.id.preworkout_edit);
        postworkedt = findViewById(R.id.postwork_edit);
        breakfatsedt = findViewById(R.id.breakfast_edit);
        msnacksedt = findViewById(R.id.msnacks_edit);
        lunchedt = findViewById(R.id.lunch_edit);
        esnacksedt = findViewById(R.id.esnacks_edit);
        dinneredt = findViewById(R.id.dinner_edit);
        mealedt = findViewById(R.id.meal_edit);
        mSave = findViewById(R.id.save);
        heading = findViewById(R.id.toolbar_title);

    }
    private boolean validation() {
        String dietPlan = dietplanedt.getText().toString();
        String preWork = dietplanedt.getText().toString();
        String postWork = dietplanedt.getText().toString();
        String breakfast = dietplanedt.getText().toString();
        String morningSnack = dietplanedt.getText().toString();
        String lunch = dietplanedt.getText().toString();
        String eveningSnack = dietplanedt.getText().toString();
        String dinner = dietplanedt.getText().toString();
        String meal = dietplanedt.getText().toString();
        boolean flag = true;

        if (dietPlan.isEmpty() ) {
            flag = false;
            dietplanedt.setError("Please enter diet plan.");
        }
        if ( preWork.isEmpty()) {
            flag = false;
            preworkedt.setError("Please enter pre-workout diet.");
        }
        if (postWork.isEmpty()) {
            flag = false;
            postworkedt.setError("Please enter post-workout diet.");
        }
        if (breakfast.isEmpty()) {
            flag = false;
            breakfatsedt.setError("Please enter breakfast.");
        }
        if (morningSnack.isEmpty() ) {
            flag = false;
            msnacksedt.setError("Please enter morning snack.");
        }

        if (lunch.isEmpty()) {
            flag = false;
            lunchedt.setError("Please enter lunch.");
        }
        if (eveningSnack.isEmpty()) {
            flag = false;
            esnacksedt.setError("Please enter evening snack.");
        }
        if (dinner.isEmpty() && meal.isEmpty()) {
            flag = false;
            dinneredt.setError("Please enter dinner.");
        }
        if ( meal.isEmpty()) {
            flag = false;
            mealedt.setError("Please enter meal.");
        }
        return flag;
    }
    public class EditHandler extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();
        String id, dietName, preWork, postWork, breakFast, snack, lunch, eveSnack, dinner, meal;

        public EditHandler(String id, String dietName, String preWork,
                           String postWork, String breakFast,
                           String snack, String lunch, String eveSnack,
                           String dinner, String meal) {
            this.id = id;
            this.dietName = dietName;
            this.preWork = preWork;
            this.postWork = postWork;
            this.breakFast = breakFast;
            this.snack = snack;
            this.lunch = lunch;
            this.eveSnack = eveSnack;
            this.dinner = dinner;
            this.meal = meal;
        }

        @Override
        protected String doInBackground(String... params) {
            RequestBody formBody = new FormBody.Builder()
                    .add("id", id)
                    .add("planname", dietName)
                    .add("preworkout", preWork)
                    .add("postworkout", postWork)
                    .add("breakfast", breakFast)
                    .add("lunch", lunch)
                    .add("morsnacks", snack)
                    .add("evesnacks", eveSnack)
                    .add("dinner", dinner)
                    .add("meal", meal)
                    .build();

            Request request = new Request.Builder()
                    .url(diet_URL)
                    .post(formBody)
                    .build();


            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String myResponse = response.body().string();
                    Add_Diet_Plan_Activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject forecast = null;
                            try {
                                forecast = new JSONObject(myResponse);
                                Message = forecast.getString("message");
                                mSave.setVisibility(View.VISIBLE);
                                dietProgress.setVisibility(View.GONE);
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

    class PostHandler extends AsyncTask<String, String, String> {
        OkHttpClient client = new OkHttpClient();

        String dietname, prework, postwork, breakfast, snack, lunch, evesnack, dinner, meal;

        public PostHandler(String dietname, String prework, String postwork,
                           String breakfast, String snack, String lunch, String evesnack,
                           String dinner, String meal) {
            this.dietname = dietname;
            this.prework = prework;
            this.postwork = postwork;
            this.breakfast = breakfast;
            this.snack = snack;
            this.lunch = lunch;
            this.evesnack = evesnack;
            this.dinner = dinner;
            this.meal = meal;
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestBody formBody = new FormBody
                    .Builder()
                    .add("planname", dietname)
                    .add("preworkout", prework)
                    .add("postworkout", postwork)
                    .add("breakfast", breakfast)
                    .add("morsnacks", snack)
                    .add("evesnacks", evesnack)
                    .add("dinner", dinner)
                    .add("lunch", lunch)
                    .add("meal", meal)
                    .build();


            Request request = new Request.Builder()
                    .url(diet_URL)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String myResponse = response.body().string();
                    Add_Diet_Plan_Activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject forecast = null;
                            try {
                                forecast = new JSONObject(myResponse);
                                Message = forecast.getString("message");
                                mSave.setVisibility(View.VISIBLE);
                                dietProgress.setVisibility(View.GONE);
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

        final Dialog dialog = new Dialog(Add_Diet_Plan_Activity.this);
        dialog.setContentView(R.layout.custom_alert_dialog_box);
        TextView mesg = dialog.findViewById(R.id.msg);
        mesg.setText(message);
        Button okbtn=dialog.findViewById(R.id.okbtn);
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


    private void reset(){

        dietplanedt.setText("");
        preworkedt.setText("");
        postworkedt.setText("");
        breakfatsedt.setText("");
        msnacksedt.setText("");
        msnacksedt.setText("");
        lunchedt.setText("");
        esnacksedt.setText("");
        dinneredt.setText("");
        mealedt.setText("");
    }



}