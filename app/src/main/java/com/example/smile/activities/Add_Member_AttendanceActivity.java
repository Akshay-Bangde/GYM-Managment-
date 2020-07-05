package com.example.smile.activities;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.smile.R;
import com.example.smile.adapters.GYMPackageListAdapter;
import com.example.smile.adapters.MemberAttendanceAdapter;
import com.example.smile.dataObjects.GYMPackageListModel;
import com.example.smile.dataObjects.MemberAttendanceModel;
import com.example.smile.dataObjects.WorkoutExerciseListModel;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.smile.activities.SignInActivity.BaseURL;

public class Add_Member_AttendanceActivity extends AppCompatActivity {

    Button mSearchButton;
    EditText mDate;
    Calendar mDateCalender;
    private ArrayList<MemberAttendanceModel> mMemberAttendanceModel;
    MemberAttendanceAdapter adapter;
    RecyclerView mRecyclerView;
    String Grid_URL = BaseURL+"AddMemberAttendance";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__member__attendance);
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

        String currentDate = new SimpleDateFormat("dd - MMM - yyyy", Locale.getDefault()).format(new Date());

        mDateCalender = Calendar.getInstance();
        mDate = findViewById(R.id.date_Genrate);
        mDate.setText(currentDate);
        SaveButton();



        mRecyclerView = findViewById(R.id.recyclerView);
        mMemberAttendanceModel = new ArrayList<MemberAttendanceModel>();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MemberAttendanceAdapter
                (Add_Member_AttendanceActivity.this,mMemberAttendanceModel);
        mRecyclerView.setAdapter(adapter);

        run(Grid_URL);

    }

    /*///Get code for Grid
    public  void Grid(String url){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url)

                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String myResponse = response.body().string();

                Add_Member_AttendanceActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject forecast = null;
                        try {
                            forecast = new JSONObject(myResponse);
                            // JSONObject data = forecast.getJSONObject("data");
                            JSONArray jsonArray = forecast.getJSONArray("MemberName");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                String id = object.getString("id");
                                String name = object.getString("fullname");

                                MemberAttendanceModel memberAttendanceModel = new MemberAttendanceModel();
                                memberAttendanceModel.setId(id);
                                memberAttendanceModel.setName(name);

                            }

                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }
*/
    // Get webservice for body Part Spinner
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

                Add_Member_AttendanceActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //JSONObject for Package Interested
                        try {
                            JSONObject forecast = new JSONObject(myResponse);
                            JSONArray jsonArray = forecast.getJSONArray("MemberName");
                            for (int i = 0; i <= jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String name = object.getString("fullname");

                                MemberAttendanceModel memberAttendanceModel = new MemberAttendanceModel();
                                memberAttendanceModel.setName(name);
                                mMemberAttendanceModel.add(memberAttendanceModel);
                                mRecyclerView.getAdapter().notifyDataSetChanged();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }


    public void updateLabel() {
        String myFormat = " dd - MMM - yyyy";
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(myFormat, Locale.UK);
        mDate.setText(sdf.format(mDateCalender.getTime()));

    }

    public void SaveButton () {
        mSearchButton = findViewById(R.id.searchButton);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
