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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.smile.R;
import com.example.smile.dataObjects.FoodCategoryListModel;
import com.example.smile.dataObjects.WaterIntakeListModel;
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

public class Add_WaterIntake_Activity extends AppCompatActivity {

    EditText mnoWaterGlass;
    Button searchBtn;
    String Message;
    String AddNumber_URL = BaseURL+"AddWaterIntake";
    WaterIntakeListModel data =null;
    TextView heading;
    ProgressBar dietProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__water_intake_);

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
        Intent intent = getIntent();
        if (intent != null) {
            try {
                data = (WaterIntakeListModel) intent.getSerializableExtra("packageData");
            } catch (Exception e) {
                Log.d("exception", e.toString());
            }
        }

        if (data != null) {

            mnoWaterGlass.setText(data.getGlasssNo());
            heading.setText("Edit Water Intake");
        }
        clicksHandler();
    }

    public void init(){
        mnoWaterGlass = findViewById(R.id.numberGlass);
        searchBtn = findViewById(R.id.save);
        heading = findViewById(R.id.toolbar_title);
        dietProgress=findViewById(R.id.pb);
    }

    public void clicksHandler(){
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean networkConectivity = NetworkConnectivity.isConnected(Add_WaterIntake_Activity.this);
                if (networkConectivity) {
                    if (validation()) {
                        searchBtn.setVisibility(View.GONE);
                        dietProgress.setVisibility(View.VISIBLE);

                        if (data != null) {
                            String id = data.getId();
                            String noglass = mnoWaterGlass.getText().toString();

                            EditHandler handler = new EditHandler(id, noglass );
                            String result = null;
                            try {
                                result = handler.execute(AddNumber_URL).get();
                            } catch (InterruptedException ie) {
                                ie.printStackTrace();
                            } catch (ExecutionException ee) {
                                ee.printStackTrace();
                            }
                        } else {

                            String numglass = mnoWaterGlass.getText().toString();
                            PostHandler handler = new PostHandler(numglass);
                            String result = null;
                            try {
                                result = handler.execute(AddNumber_URL).get();

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                else {
                    NetworkConnectivity.networkConnetivityShowDialog(Add_WaterIntake_Activity.this);

                }


            }
        });
    }

    public class EditHandler extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();
        String id,numGlass;

        public EditHandler(String id, String numGlass) {
            this.id = id;
            this.numGlass = numGlass;
        }

        @Override
        protected String doInBackground(String... params) {
            RequestBody formBody = new FormBody.Builder()
                    .add("id", id)
                    .add("no", numGlass)
                    .build();

            Request request = new Request.Builder()
                    .url(AddNumber_URL)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String myResponse = response.body().string();
                    Add_WaterIntake_Activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject forecast = null;
                            try {
                                forecast = new JSONObject(myResponse);

                                Message = forecast.getString("message");
                                searchBtn.setVisibility(View.VISIBLE);
                                dietProgress.setVisibility(View.GONE);
                                showDialog(Message);
                                heading.setText("Add Water Intake");

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
        String numGlass;

        public PostHandler(String numGlass) {
            this.numGlass = numGlass;
        }

        @Override
        protected String doInBackground(String... params) {
            RequestBody formBody = new FormBody.Builder()
                    .add("no", numGlass)
                    .build();

            Request request = new Request.Builder()
                    .url(AddNumber_URL)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String myResponse = response.body().string();
                    Add_WaterIntake_Activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject forecast = null;
                            try {
                                forecast = new JSONObject(myResponse);
                                searchBtn.setVisibility(View.VISIBLE);
                                dietProgress.setVisibility(View.GONE);
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


    public void showDialog(final String message) {

        final Dialog dialog = new Dialog(Add_WaterIntake_Activity.this);
        dialog.setContentView(R.layout.custom_alert_dialog_box);
        TextView mesg = dialog.findViewById(R.id.msg);
        mesg.setText(message);
        Button okbtn = dialog.findViewById(R.id.okbtn);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(Add_WaterIntake_Activity.this, WaterIntakeList_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                ActivityOptions options = ActivityOptions.makeCustomAnimation(Add_WaterIntake_Activity.this,
                        R.anim.push_down_in, R.anim.push_down_out);
                startActivity(intent, options.toBundle());

            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();


    }


    private boolean validation() {
        String numberGlass = mnoWaterGlass.getText().toString();

        boolean flag = true;
        if (numberGlass.isEmpty()) {
            flag = false;
            mnoWaterGlass.setError(getResources().getString(R.string.plzenternumberglass));

        }

    return flag;
    }

}
