package com.example.smile.activities;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.smile.R;
import com.example.smile.adapters.WorkoutCategoryAdapter;
import com.example.smile.dataObjects.WorkoutCategoryModel;
import com.example.smile.utils.CategoryCallBackInterface;
import com.example.smile.utils.NetworkConnectivity;
import com.example.smile.utils.WorkoutCategoryInterface;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

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

public class Workout_CategoryActivity extends AppCompatActivity implements WorkoutCategoryInterface, CategoryCallBackInterface {

    private EditText mBodyPart;
    SwipyRefreshLayout pagination;
    int page = 0;
    private Button mAddBtn;
    String Message;
    RecyclerView mRecyclerView;
    WorkoutCategoryAdapter adapter;

    String Add_Body_Part_URL = BaseURL + "WorkoutManagement";
    String Grid_URL = BaseURL + "WorkoutCategoryGrid";
    private ArrayList<WorkoutCategoryModel> mWorkoutCategoryArrayList;
    ProgressBar progressBar, addbuttonProgressBar;

    String id = "";
    TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout__category);
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

        initViews();
        bindControls();

        AddButton();



        boolean networkConectivity =  NetworkConnectivity.isConnected(this);
        if (networkConectivity) {
            progressBar.setVisibility(View.VISIBLE);
            Grid(Grid_URL);
        }
        else {
            NetworkConnectivity.networkConnetivityShowDialog(this);

        }
    }


    private void initViews() {
        mBodyPart = findViewById(R.id.body_part);
        mAddBtn = findViewById(R.id.addBtn);
        pagination = findViewById(R.id.pagination);
        mRecyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.pb);
        addbuttonProgressBar = findViewById(R.id.progressBar);

        mWorkoutCategoryArrayList = new ArrayList<WorkoutCategoryModel>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WorkoutCategoryAdapter
                (Workout_CategoryActivity.this, mWorkoutCategoryArrayList);
        mRecyclerView.setAdapter(adapter);

    }

    private void bindControls() {

        pagination.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                pagination.setRefreshing(false);
                page = page + 1;
                Grid(Grid_URL);
                pagination.setDirection(SwipyRefreshLayoutDirection.BOTTOM);
            }
        });


    }

    private void AddButton() {

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean networkConectivity = NetworkConnectivity.isConnected(Workout_CategoryActivity.this);
                if (networkConectivity) {
                    if (validation()) {
                        mAddBtn.setVisibility(View.GONE);
                        addbuttonProgressBar.setVisibility(View.VISIBLE);

                        if (!id.isEmpty()) {
                            String id1 = id;
                            String bodyPart = mBodyPart.getText().toString();

                            EditHandler handler = new EditHandler(id1, bodyPart);

                            String result = null;
                            try {
                                result = handler.execute(Add_Body_Part_URL).get();

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }

                        } else {
                            String bodyPart = mBodyPart.getText().toString();
                            PostHandler handler = new PostHandler(bodyPart);
                            String result = null;
                            try {
                                result = handler.execute(Add_Body_Part_URL).get();

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                    else {
                        NetworkConnectivity.networkConnetivityShowDialog(Workout_CategoryActivity.this);

                    }

                }
        });
    }

    ///Get code for Grid
    public void Grid(String url) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url)
                .addHeader("Pagenation", String.valueOf(page))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String myResponse = response.body().string();

                Workout_CategoryActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        JSONObject forecast = null;
                        try {
                            forecast = new JSONObject(myResponse);

                            JSONArray jsonArray = forecast.getJSONArray("WCategoryData");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);


                                String id = object.getString("id");
                                String bodyPart = object.getString("bodypart");

                                WorkoutCategoryModel workoutCategoryModel = new WorkoutCategoryModel();
                                workoutCategoryModel.setId(id);
                                workoutCategoryModel.setBodypart(bodyPart);

                                if (mWorkoutCategoryArrayList != null && mWorkoutCategoryArrayList.size() > 0) {
                                    int size = mWorkoutCategoryArrayList.size();
                                    workoutCategoryModel.setSerial_no(String.valueOf(size + 1));
                                    mWorkoutCategoryArrayList.add(size, workoutCategoryModel);
                                } else {

                                    workoutCategoryModel.setSerial_no(String.valueOf(i + 1));
                                    mWorkoutCategoryArrayList.add(workoutCategoryModel);
                                }
                            }

                            adapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onDeleteItemListener(int pos) {
        mWorkoutCategoryArrayList.remove(pos);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void getBodyPart(WorkoutCategoryModel model) {
        String type = model.getBodypart();
        mBodyPart.setText(type);
        id = model.getId();
        toolbar_title.setText("Edit Workout Category");


    }

    //this webservices use for edit a record
    public class EditHandler extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();

        String id,bodyPart;

        public EditHandler(String id, String bodyPart) {
            this.id = id;
            this.bodyPart = bodyPart;
        }

        @Override
        protected String doInBackground(String... params) {
            RequestBody formBody = new FormBody.Builder()
                    .add("id", id)
                    .add("bodypart", bodyPart)
                    .build();

            Request request = new Request.Builder()
                    .url(Add_Body_Part_URL)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String myResponse = response.body().string();
                    Workout_CategoryActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject forecast = null;
                            try {
                                forecast = new JSONObject(myResponse);
                                mAddBtn.setVisibility(View.VISIBLE);
                                addbuttonProgressBar.setVisibility(View.GONE);
                                reset();
                                Message = forecast.getString("message");
                                showDialog(Message);
                                toolbar_title.setText("Add Workout Category");


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

    //this webservices use for Add a record
    public class PostHandler extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();
        String bodyPart;

        public PostHandler(String bodyPart) {
            this.bodyPart = bodyPart;
        }

        @Override
        protected String doInBackground(String... params) {
            RequestBody formBody = new FormBody.Builder()
                    .add("bodypart", bodyPart)
                    .build();

            Request request = new Request.Builder()
                    .url(Add_Body_Part_URL)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String myResponse = response.body().string();
                    Workout_CategoryActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject forecast = null;
                            try {
                                forecast = new JSONObject(myResponse);
                                mAddBtn.setVisibility(View.VISIBLE);
                                addbuttonProgressBar.setVisibility(View.GONE);
                                reset();
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

        final Dialog dialog = new Dialog(Workout_CategoryActivity.this);
        dialog.setContentView(R.layout.custom_alert_dialog_box);
        TextView mesg = dialog.findViewById(R.id.msg);
        mesg.setText(message);
        Button okbtn = dialog.findViewById(R.id.okbtn);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();


    }

    public void reset() {
        mBodyPart.setText("");
    }

    private boolean validation() {
        String body_part = mBodyPart.getText().toString();
        boolean flag = true;
        if (body_part.isEmpty()) {
            flag = false;
            mBodyPart.setError(getResources().getString(R.string.plz_enter_body_part));
        }
        return flag;
    }

}
