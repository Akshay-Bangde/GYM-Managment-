package com.example.smile.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.example.smile.R;
import com.example.smile.adapters.WorkoutCategoryAdapter;
import com.example.smile.adapters.WorkoutExerciseListAdapter;
import com.example.smile.dataObjects.WorkoutCategoryModel;
import com.example.smile.dataObjects.WorkoutExerciseListModel;
import com.example.smile.utils.CategoryCallBackInterface;
import com.example.smile.utils.NetworkConnectivity;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.smile.activities.SignInActivity.BaseURL;

public class Workout_Exercise_list_Activity extends AppCompatActivity implements CategoryCallBackInterface {
    SwipyRefreshLayout pagination;
    int page = 0;
    String Grid_URL = BaseURL+"WorkoutExerciseGrid";
    private ArrayList<WorkoutExerciseListModel> mWorkoutExerciseListModel;
    WorkoutExerciseListAdapter adapter;
    RecyclerView mRecyclerView;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_exercise_list);
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


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Workout_Exercise_list_Activity.this, Add_Workout_ExerciseActivity.class);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(Workout_Exercise_list_Activity.this, R.anim.push_up_in, R.anim.push_up_out);
                startActivity(intent, options.toBundle());

            }
        });
        pagination = findViewById(R.id.pagination);

        mRecyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.pb);
        mWorkoutExerciseListModel = new ArrayList<WorkoutExerciseListModel>();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
         adapter = new WorkoutExerciseListAdapter
                (Workout_Exercise_list_Activity.this,mWorkoutExerciseListModel);
        mRecyclerView.setAdapter(adapter);

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

    //Code for Pagination
    private void bindControls(){

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

    ///Get code for Grid
    public  void Grid(String url){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url)
                .addHeader("Pagenation",String.valueOf(page))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String myResponse = response.body().string();

                Workout_Exercise_list_Activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        JSONObject forecast = null;
                        try {
                            forecast = new JSONObject(myResponse);
                            JSONArray jsonArray = forecast.getJSONArray("WExerciseData");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);


                                String id = object.getString("id");
                                String bodyPart = object.getString("bodypart");
                                String workoutName = object.getString("workout");

                                WorkoutExerciseListModel workoutExerciseListModel = new WorkoutExerciseListModel();
                                workoutExerciseListModel.setId(id);
                                workoutExerciseListModel.setBodypart(bodyPart);
                                workoutExerciseListModel.setWorkoutName(workoutName);

                                if(mWorkoutExerciseListModel != null && mWorkoutExerciseListModel.size() > 0){
                                    int size = mWorkoutExerciseListModel.size();
                                    workoutExerciseListModel.setSerial_no(String.valueOf(size + 1));
                                    mWorkoutExerciseListModel.add(size, workoutExerciseListModel);
                                }else  {

                                    workoutExerciseListModel.setSerial_no(String.valueOf(i+1));
                                    mWorkoutExerciseListModel.add(workoutExerciseListModel);
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
        mWorkoutExerciseListModel.remove(pos);
        adapter.notifyDataSetChanged();
    }
}
