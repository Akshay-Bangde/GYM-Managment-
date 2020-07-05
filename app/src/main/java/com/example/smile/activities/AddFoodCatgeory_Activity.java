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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smile.R;
import com.example.smile.dataObjects.FoodCategoryListModel;
import com.example.smile.utils.NetworkConnectivity;

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

public class AddFoodCatgeory_Activity extends AppCompatActivity {
    EditText categoryName, totalCalorie;
    Button mSave;
    FoodCategoryListModel data = null;
    public String Add_Food_Url =BaseURL+"AddFoodCategory";
    TextView heading;
    ProgressBar dietProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_catgeory);
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
        dietProgress=findViewById(R.id.pb);
        categoryName = findViewById(R.id.foodcatName);
        totalCalorie = findViewById(R.id.total_calorie);
        mSave = findViewById(R.id.food_save);
        heading = findViewById(R.id.toolbar_title);
        Intent intent = getIntent();
        if (intent != null) {
            try {
                data = (FoodCategoryListModel) intent.getSerializableExtra("packageData");
            } catch (Exception e) {
                Log.d("exception", e.toString());
            }
        }

        if (data != null) {

            categoryName.setText(data.getCategory());
            totalCalorie.setText(data.getTotal_Calorie());
            heading.setText("Edit Food Category");
        }

        SaveButton();

    }

    public void  SaveButton(){
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean networkConectivity = NetworkConnectivity.isConnected(AddFoodCatgeory_Activity.this);
                if (networkConectivity) {
                    if (validation()) {
                        mSave.setVisibility(View.GONE);
                        dietProgress.setVisibility(View.VISIBLE);

                        if (data != null) {
                            String id = data.getId();
                            String category_name = categoryName.getText().toString();
                            String total_calories = totalCalorie.getText().toString();

                            EditHandler handler = new EditHandler(id,category_name, total_calories);
                            String result = null;
                            try {
                                result = handler.execute(Add_Food_Url).get();
                            } catch (InterruptedException ie) {
                                ie.printStackTrace();
                            } catch (ExecutionException ee) {
                                ee.printStackTrace();
                            }
                        } else {
                            String category_name = categoryName.getText().toString();
                            String total_calories = totalCalorie.getText().toString();

                            PostHandeler handler = new PostHandeler(category_name, total_calories);
                            String result = null;
                            try {
                                result = handler.execute(Add_Food_Url).get();
                            } catch (InterruptedException ie) {
                                ie.printStackTrace();
                            } catch (ExecutionException ee) {
                                ee.printStackTrace();
                            }
                        }
                    }
                }
                else {
                    NetworkConnectivity.networkConnetivityShowDialog(AddFoodCatgeory_Activity.this);

                }
            }
        });
    }

    public class EditHandler extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();
        String id,category_name, total_cal;

        public EditHandler(String id, String category_name, String total_cal) {
            this.id = id;
            this.category_name = category_name;
            this.total_cal = total_cal;
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody formBody = new FormBody.Builder()
                    .add("id",id)
                    .add("name",category_name)
                    .add("calorie",total_cal).build();

            Request request = new Request.Builder()
                    .url(Add_Food_Url)
                    .post(formBody).build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    final String myResponse = response.body().string();
                    AddFoodCatgeory_Activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject forecast = null;
                            try {
                                forecast = new JSONObject(myResponse);
                                String Message = forecast.getString("message");
                                mSave.setVisibility(View.VISIBLE);
                                dietProgress.setVisibility(View.GONE);
                                showDialog(Message);
                                heading.setText("Add Food Category");
                                 }

                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    });


                }
            });

            return null;
        }
    }


    public class PostHandeler extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();
        String category_name, total_cal;

        public PostHandeler(String cat_name, String total_calo){
            this.category_name = cat_name;
            this.total_cal = total_calo;
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody formBody = new FormBody.Builder()
                    .add("name",category_name)
                    .add("calorie",total_cal).build();

            Request request = new Request.Builder()
                    .url(Add_Food_Url)
                    .post(formBody).build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    final String myResponse = response.body().string();
                    AddFoodCatgeory_Activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject forecast = null;
                            try {
                                forecast = new JSONObject(myResponse);
                                String Message = forecast.getString("message");
                                mSave.setVisibility(View.VISIBLE);
                                dietProgress.setVisibility(View.GONE);
                                showDialog(Message);
                                }

                            catch (Exception e){
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

        final Dialog dialog = new Dialog(AddFoodCatgeory_Activity.this);
        dialog.setContentView(R.layout.custom_alert_dialog_box);
        TextView mesg = dialog.findViewById(R.id.msg);
        mesg.setText(message);
        Button okbtn = dialog.findViewById(R.id.okbtn);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                /*Intent intent = new Intent(AddFoodCatgeory_Activity.this, FoodCategory_Activity.class);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(AddFoodCatgeory_Activity.this, R.anim.push_down_in, R.anim.push_down_out);
                startActivity(intent, options.toBundle());
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);*/
                finish();
                overridePendingTransition(R.anim.push_down_in,R.anim.push_down_out);

            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();


    }

    public boolean validation() {
        String name = categoryName.getText().toString();
        String amount = totalCalorie.getText().toString();

        boolean flag = true;

        if (name.isEmpty()) {
            flag = false;
            categoryName.setError(getResources().getString(R.string.plz_enter_category));
        }

        if (amount.isEmpty()) {
            flag = false;
            totalCalorie.setError(getResources().getString(R.string.plz_enter_calories));
        }
        return flag;
    }
}
