package com.example.smile.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.example.smile.R;
import com.example.smile.adapters.DietAdapter;
import com.example.smile.dataObjects.DietModel;
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

public class Diet_List_Activity extends AppCompatActivity implements CategoryCallBackInterface {


    RecyclerView dietRecyclerView;
    ArrayList<DietModel> dietListdata;
    int page = 0;
    DietAdapter dietAdapter;

   SwipyRefreshLayout dietpagination;
    String durl = BaseURL + "DietPlanGrid";
    String emptyValue="null";

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_list);
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

        progressBar = findViewById(R.id.pb);

        boolean networkConectivity =  NetworkConnectivity.isConnected(this);

        if (networkConectivity) {
            progressBar.setVisibility(View.VISIBLE);
            run(durl);
        }
        else {
            NetworkConnectivity.networkConnetivityShowDialog(this);

        }


        //swipe listener

        dietpagination = findViewById(R.id.diet_Pagination);
        dietpagination.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                dietpagination.setRefreshing(false);
                page=page+1;
                run(durl);
            }
        });
        dietRecyclerView = findViewById(R.id.dietRec);

        dietListdata = new ArrayList<DietModel>();


        dietRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dietAdapter = new DietAdapter(Diet_List_Activity.this, dietListdata);
        dietRecyclerView.setHasFixedSize(true);
        dietRecyclerView.setAdapter(dietAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Diet_List_Activity.this, Add_Diet_Plan_Activity.class);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(Diet_List_Activity.this, R.anim.push_up_in, R.anim.push_up_out);
                startActivity(intent, options.toBundle());

            }
        });
    }



    public void run (String durl){
        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder()
                .header("Pagenation", String.valueOf(page))
                .url(durl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String myResponse = response.body().string();

                Diet_List_Activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        JSONObject forecast = null;
                        try {
                            forecast = new JSONObject(myResponse);

                            JSONArray jsonArray = forecast.getJSONArray("DietPlanData");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject dietObject = jsonArray.getJSONObject(i);

                                String dietID = dietObject.getString("id");
                                String dietName = dietObject.getString("planname");
                                String preworkk = dietObject.getString("preworkout");
                                String postworkk = dietObject.getString("postworkout");
                                String brkfast = dietObject.getString("breakfast");
                                String lunchh = dietObject.getString("lunch");
                                String dmsnack = dietObject.getString("morsnacks");
                                String desnack = dietObject.getString("evesnacks");
                                String dinnerr = dietObject.getString("dinner");
                                String dmeal = dietObject.getString("meal");

                                DietModel  dietModel = new DietModel();
                                dietModel.setId(dietID);
                                dietModel.setPlanName(dietName);

                                if (postworkk.equals(emptyValue)){
                                    dietModel.setPostWorkout("");
                                }
                                  else{
                                    dietModel.setPostWorkout(postworkk);
                                }



                                if (preworkk.equals(emptyValue)){
                                    dietModel.setPreWorkout("");
                                }
                                else{
                                    dietModel.setPreWorkout(preworkk);
                                }


                                if (brkfast.equals(emptyValue)){
                                    dietModel.setBreakFast("");
                                }
                                else{
                                    dietModel.setBreakFast(brkfast);
                                }

                                if (lunchh.equals(emptyValue)){
                                    dietModel.setLunchh("");
                                }
                                else{
                                    dietModel.setLunchh(lunchh);
                                }
                                if (dmsnack.equals(emptyValue)){
                                    dietModel.setMornSnk("");
                                }
                                else{
                                    dietModel.setMornSnk(dmsnack);
                                }


                                if (desnack.equals(emptyValue)){
                                    dietModel.setEveSnk("");
                                }
                                else{
                                    dietModel.setEveSnk(desnack);
                                }

                                if (dinnerr.equals(emptyValue)){
                                    dietModel.setDinnerr("");
                                }
                                else{
                                    dietModel.setDinnerr(dinnerr);
                                }


                                if (dmeal.equals(emptyValue)){
                                    dietModel.setMeall("");
                                }
                                else{
                                    dietModel.setMeall(dmeal);
                                }



                                if (dietListdata != null && dietListdata.size() > 0) {
                                    int size = dietListdata.size();
                                    dietModel.setSerial_id(String.valueOf(size + 1));
                                    dietListdata.add(size, dietModel);


                                } else {
                                    dietModel.setSerial_id(String.valueOf(i + 1));
                                    dietListdata.add(dietModel);
                                }

                            }
                            dietAdapter.notifyDataSetChanged();
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
        dietListdata.remove(pos);
        dietAdapter.notifyDataSetChanged();
    }


}