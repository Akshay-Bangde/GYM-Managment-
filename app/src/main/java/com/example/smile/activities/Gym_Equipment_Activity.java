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
import com.example.smile.adapters.Gym_Equipment_Adapter;
import com.example.smile.dataObjects.Equipment_Model;
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

public class Gym_Equipment_Activity extends AppCompatActivity implements CategoryCallBackInterface {

    RecyclerView  eqpRecyclerView;
    ArrayList<Equipment_Model> eqpListdata;
    int page=0;
    Gym_Equipment_Adapter eqpadapter;
    SwipyRefreshLayout mpagination;
    String murl=BaseURL+"EquipmentGrid";

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gym__equipment);

        mpagination=findViewById(R.id.pagination);

        progressBar = findViewById(R.id.pb);

        eqpRecyclerView =  findViewById(R.id.eqp_recycler_view);
        eqpListdata = new ArrayList<Equipment_Model>();

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

        eqpRecyclerView.setLayoutManager(new LinearLayoutManager(this));
          eqpadapter = new Gym_Equipment_Adapter(Gym_Equipment_Activity.this, eqpListdata);
        eqpRecyclerView.setHasFixedSize(true);
        eqpRecyclerView.setAdapter(eqpadapter);


        bindControls();
        boolean networkConectivity =  NetworkConnectivity.isConnected(this);
        if (networkConectivity) {
            progressBar.setVisibility(View.VISIBLE);
            run(murl);
        }
        else {
            NetworkConnectivity.networkConnetivityShowDialog(this);

        }


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent add_group = new Intent(Gym_Equipment_Activity.this, Add_Equipment_Acitivity.class);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(Gym_Equipment_Activity.this, R.anim.push_up_in, R.anim.push_up_out);
                startActivity(add_group, options.toBundle());
            }
        });


    }


    public  void run(String murl){
        OkHttpClient client = new OkHttpClient();




        Request request = new Request.Builder()
                .header("Pagenation",String.valueOf(page) )
                .url(murl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String myResponse = response.body().string();

                Gym_Equipment_Activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        JSONObject forecast = null;
                        try {
                            forecast = new JSONObject(myResponse);
                            //           JSONObject data = forecast.getJSONObject("Equipment");


                            JSONArray jsonArray = forecast.getJSONArray("EquipmentData");


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject eqpobject = jsonArray.getJSONObject(i);

                                String eqpid = eqpobject.getString("id");
                                String eqptranstn = eqpobject.getString("equipment_id");
                                String eqpname = eqpobject.getString("equipname");
                                String eqpdesc = eqpobject.getString("details");
                                String eqprate = eqpobject.getString("rate");
                                String eqpqty = eqpobject.getString("qty");
                                String eqpnetamt = eqpobject.getString("amount");

                                Equipment_Model equipment_model = new Equipment_Model();
                                equipment_model.setId(eqpid);
                                equipment_model.setEtransaction_id(eqptranstn);
                                equipment_model.seteName(eqpname);
                                equipment_model.seteDescription(eqpdesc);
                                equipment_model.seteRate(eqprate);
                                equipment_model.seteQty(eqpqty);
                                equipment_model.seteNetamt(eqpnetamt);



                                if (eqpListdata != null && eqpListdata.size() > 0) {
                                    int size = eqpListdata.size();
                                    equipment_model.seteSerialid(String.valueOf(size + 1));
                                    eqpListdata.add(size, equipment_model);


                                } else {
                                    equipment_model.seteSerialid(String.valueOf(i + 1));
                                    eqpListdata.add(equipment_model);
                                }

                            }
                            eqpadapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

  public void bindControls(){


        mpagination.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                mpagination.setRefreshing(false);
                page=page+1;
                run(murl);

            }
        });

  }
  @Override
    public void onDeleteItemListener(int pos) {
        eqpListdata.remove(pos);
        eqpadapter.notifyDataSetChanged();
    }
}
