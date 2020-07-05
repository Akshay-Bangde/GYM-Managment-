package com.example.smile.activities;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.smile.R;
import com.example.smile.adapters.PackageActivityDurationAdapter;
import com.example.smile.dataObjects.PackageActivityDurationModel;
import com.example.smile.dataObjects.WorkoutCategoryModel;
import com.example.smile.utils.CategoryCallBackInterface;
import com.example.smile.utils.DurationInterface;
import com.example.smile.utils.NetworkConnectivity;
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

public class Duration_PeriodActivity extends AppCompatActivity implements DurationInterface, CategoryCallBackInterface {

    private Spinner mPeriooTypeSpinner;
    ArrayList<String> mPriodList;
    private EditText mDuration;
    private CoordinatorLayout mCoordinatorLayout;
    private Button mAddBtn;
    RecyclerView mRecyclerView;
    String periodType;
    String Message;
    SwipyRefreshLayout pagination;
    public int page = 0;
    String AddDurationURL = BaseURL + "PackageAndActivityDuration";
    String Grid_URL = BaseURL + "PADurationGrid";

    PackageActivityDurationAdapter adapter;
    private ArrayList<PackageActivityDurationModel> mPackageActivityDurationArrayList;
    PackageActivityDurationModel data = null;
    String id = "";
    ProgressBar progressBar,addbuttonProgressBar;
    TextView toolbar_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duration__period);
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


        mPeriooTypeSpinner = findViewById(R.id.period_type_spinner);
        mPriodList = new ArrayList<String>();
        mPriodList.add("Select");
        mPriodList.add("Days");
        mPriodList.add("Month");
        mPriodList.add("Year");
        ArrayAdapter<String> fieldAdpater = new ArrayAdapter<String>(Duration_PeriodActivity.this,
                android.R.layout.simple_list_item_1, mPriodList);
        fieldAdpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPeriooTypeSpinner.setAdapter(fieldAdpater);


        mDuration = findViewById(R.id.duration_edt);
        mCoordinatorLayout = findViewById(R.id.coordinatorLayout);
        mAddBtn = findViewById(R.id.add_btn);
        mRecyclerView = findViewById(R.id.recyclerView);
        pagination = findViewById(R.id.pagination);
        progressBar = findViewById(R.id.pb);
        toolbar_title = findViewById(R.id.toolbar_title);
        addbuttonProgressBar = findViewById(R.id.progressBar);
        mPackageActivityDurationArrayList = new ArrayList<PackageActivityDurationModel>();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PackageActivityDurationAdapter
                (Duration_PeriodActivity.this, mPackageActivityDurationArrayList);
        mRecyclerView.setAdapter(adapter);


        AddButon();
        ClicksHandler();
        boolean networkConectivity =  NetworkConnectivity.isConnected(this);
        if (networkConectivity) {
            progressBar.setVisibility(View.VISIBLE);
            Grid(Grid_URL);
        }
        else {
            NetworkConnectivity.networkConnetivityShowDialog(this);

        }
        bindControls();


    }



    public void ClicksHandler() {
        mPeriooTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                periodType = mPriodList.get(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    //Code for pagiantion
    public void bindControls() {

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

    @Override
    public void onDeleteItemListener(int pos) {
        mPackageActivityDurationArrayList.remove(pos);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void getPeriodType(PackageActivityDurationModel model) {
        String type = model.getDuration();
        id = model.getId();
        String[] arr = type.split(" ");
        type = arr[1];
        int pos = 0;
        if (mPriodList != null && mPriodList.size() > 0) {
            for (int i = 0; i < mPriodList.size(); i++) {
                if (type.equals(mPriodList.get(i))) {
                    pos = i;
                }

            }
        }
        mPeriooTypeSpinner.setSelection(pos);
        mDuration.setText(arr[0]);
        toolbar_title.setText("Edit Package / Activity Duration");
    }

    private void AddButon() {

        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean networkConectivity = NetworkConnectivity.isConnected(Duration_PeriodActivity.this);
                if (networkConectivity) {
                    if (validation()) {
                        mAddBtn.setVisibility(View.GONE);
                        addbuttonProgressBar.setVisibility(View.VISIBLE);
                        if (!id.isEmpty()) {
                            String id1 = id;
                            String duration = mDuration.getText().toString();
                            String PeriodType = periodType;
                            EditHandler handler = new EditHandler(id1, duration, PeriodType);

                            String result = null;
                            try {
                                result = handler.execute(AddDurationURL).get();

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        } else {
                            String duration = mDuration.getText().toString();
                            String PeriodType = periodType;
                            PostHandler handler = new PostHandler(duration, PeriodType);

                            String result = null;
                            try {
                                result = handler.execute(AddDurationURL).get();

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
                    else {
                        NetworkConnectivity.networkConnetivityShowDialog(Duration_PeriodActivity.this);

                    }

            }
        });
    }

    public class EditHandler extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();
        String id, duration, periodType;

        public EditHandler(String id, String duration, String periodType) {
            this.id = id;
            this.duration = duration;
            this.periodType = periodType;
        }

        @Override
        protected String doInBackground(String... params) {
            RequestBody formBody = new FormBody.Builder()
                    .add("id", id)
                    .add("duration", duration)
                    .add("validity", periodType)
                    .build();

            Request request = new Request.Builder()
                    .url(AddDurationURL)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String myResponse = response.body().string();
                    Duration_PeriodActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject forecast = null;
                            try {
                                forecast = new JSONObject(myResponse);
                                mAddBtn.setVisibility(View.VISIBLE);
                                addbuttonProgressBar.setVisibility(View.GONE);
                                ResetButton();
                                Message = forecast.getString("message");
                                showDialog(Message);
                                toolbar_title.setText("Add Package / Activity Duration");


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
        String duration, periodType;

        public PostHandler(String duration, String periodType) {
            this.duration = duration;
            this.periodType = periodType;
        }

        @Override
        protected String doInBackground(String... params) {
            RequestBody formBody = new FormBody.Builder()
                    .add("duration", duration)
                    .add("validity", periodType)
                    .build();

            Request request = new Request.Builder()
                    .url(AddDurationURL)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String myResponse = response.body().string();
                    Duration_PeriodActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject forecast = null;
                            try {
                                forecast = new JSONObject(myResponse);
                                mAddBtn.setVisibility(View.VISIBLE);
                                addbuttonProgressBar.setVisibility(View.GONE);
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

                Duration_PeriodActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        JSONObject forecast = null;
                        try {
                            forecast = new JSONObject(myResponse);
                            JSONArray jsonArray = forecast.getJSONArray("PADData");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String id = object.getString("id");
                                String duration = object.getString("duration");

                                PackageActivityDurationModel packageActivityDurationModel = new PackageActivityDurationModel();
                                packageActivityDurationModel.setId(id);
                                packageActivityDurationModel.setDuration(duration);

                                if (mPackageActivityDurationArrayList != null && mPackageActivityDurationArrayList.size() > 0) {
                                    int size = mPackageActivityDurationArrayList.size();
                                    packageActivityDurationModel.setSerial_no(String.valueOf(size + 1));
                                    mPackageActivityDurationArrayList.add(size, packageActivityDurationModel);
                                } else {

                                    packageActivityDurationModel.setSerial_no(String.valueOf(i + 1));
                                    mPackageActivityDurationArrayList.add(packageActivityDurationModel);
                                }
                            }

                            progressBar.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public void showDialog(final String message) {

        final Dialog dialog = new Dialog(Duration_PeriodActivity.this);
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

    private void ResetButton() {
        mDuration.setText("");
        mPeriooTypeSpinner.setSelection(0);

    }

    private boolean validation() {
        String duration = mDuration.getText().toString();
        boolean flag = true;
        if (duration.isEmpty()) {
            flag = false;
            mDuration.setError(getResources().getString(R.string.plz_enter_the_duration));
        }
        if (mPeriooTypeSpinner.getSelectedItem().toString().trim().equals("Select")) {
            flag = false;
            Snackbar.make(mCoordinatorLayout, getResources().getString(R.string.plz_select_the_period), Snackbar.LENGTH_SHORT).show();
        }
        return flag;
    }

}
