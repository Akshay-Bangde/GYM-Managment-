package com.example.smile.activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smile.R;
import com.example.smile.dataObjects.MemberNameModel;
import com.example.smile.dataObjects.PackageModel;
import com.example.smile.dataObjects.StaffListModel;
import com.example.smile.utils.NetworkConnectivity;
import com.example.smile.utils.Permissions;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.smile.activities.SignInActivity.BaseURL;


public class Physiotherapy_Treatment_Activity extends AppCompatActivity {
    private Spinner memberName;
    private TextInputEditText neck, shoulder, chest, back, abdominal, hand, legs;
    Button save;
    LinearLayout mLinearLayout;
    ProgressBar progressBar;
    ArrayAdapter<String> memberNameAdapater;
    String Member_Name_Url = BaseURL + "AddMemberAttendance";
    String Add_Url = BaseURL + "PhysiotherapyPost";
    ArrayList<MemberNameModel> mMemberNameArr;
    ArrayList<String> memberNameList;
    String memberNameStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_physiotherapy__treatment);
        initView();
        bindControls();
        run(Member_Name_Url);
    }

    public void bindControls() {
        memberName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mMemberNameArr != null && mMemberNameArr.size() > 0) {
                    memberNameStr = mMemberNameArr.get(position).member_id;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean networkConectivity = NetworkConnectivity.isConnected(Physiotherapy_Treatment_Activity.this);
                if (networkConectivity) {
                    if (validation()) {
                        save.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);


                        String neckStr = neck.getText().toString();
                        String shoulderStr = shoulder.getText().toString();
                        String chestStr = chest.getText().toString();
                        String backStr = back.getText().toString();
                        String abdominalStr = abdominal.getText().toString();
                        String handStr = hand.getText().toString();
                        String legsStr = legs.getText().toString();

                        PostHandler handler = new PostHandler(memberNameStr, neckStr, shoulderStr, chestStr,
                                backStr, abdominalStr, handStr, legsStr);

                        String result = null;
                        try {
                            result = handler.execute(Add_Url).get();

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


    public class PostHandler extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();
        String memberName, neck, shoulder, chest, back, abdominal, hand, legs;

        public PostHandler(String memberNameStr, String neckStr, String shoulderStr, String chestStr,
                           String backStr, String abdominalStr, String handStr, String legsStr) {

            this.memberName = memberNameStr;
            this.neck = neckStr;
            this.shoulder = shoulderStr;
            this.chest = chestStr;
            this.back = backStr;
            this.abdominal = abdominalStr;
            this.hand = handStr;
            this.legs = legsStr;
        }

        @Override
        protected String doInBackground(String... params) {
            RequestBody formBody = new FormBody.Builder()

                    .add("member", memberName)
                    .add("neck", neck)
                    .add("shoulder", shoulder)
                    .add("chest", chest)
                    .add("back", back)
                    .add("abdominal", abdominal)
                    .add("hand", hand)
                    .add("legs", legs)
                    .build();

            Request request = new Request.Builder()
                    .url(Add_Url)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String myResponse = response.body().string();
                    Physiotherapy_Treatment_Activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject forecast = null;
                            try {
                                forecast = new JSONObject(myResponse);
                                String Message = forecast.getString("message");
                                save.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
                               // reset();
                                showDialogSuccess(Message);

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


    public void reset() {
        neck.setText("");
        shoulder.setText("");
        chest.setText("");
        back.setText("");
        abdominal.setText("");
        hand.setText("");
        legs.setText("");
        memberName.setSelection(0);
    }

    public void run(String url) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("jhsj", e.toString());
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {

                final String myResponse = response.body().string();

                Physiotherapy_Treatment_Activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        JSONObject forecast = null;
                        try {
                            forecast = new JSONObject(myResponse);


                            JSONArray jsonArray = forecast.getJSONArray("MemberName");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                MemberNameModel nameModel = new MemberNameModel();

                                JSONObject object = jsonArray.getJSONObject(i);
                                String name = object.getString("fullname");
                                String id = object.getString("id");
                                String member_id = object.getString("member_id");
                                String memberType = object.getString("membertype");
                                memberNameList.add(name);


                                nameModel.id = id;
                                nameModel.fullname = name;
                                nameModel.member_id = member_id;
                                nameModel.memberType = memberType;
                                mMemberNameArr.add(nameModel);


                            }
                            progressBar.setVisibility(View.GONE);
                            MemberNameModel packageModel1 = new MemberNameModel();
                            packageModel1.id = "0";
                            packageModel1.member_id = "0";
                            packageModel1.fullname = "Select";
                            mMemberNameArr.add(0, packageModel1);

                            memberNameAdapater = new ArrayAdapter<String>(Physiotherapy_Treatment_Activity.this,
                                    android.R.layout.simple_list_item_1, memberNameList);
                            memberNameAdapater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            memberName.setAdapter(memberNameAdapater);


                        } catch (Exception e) {
                            progressBar.setVisibility(View.GONE);
                            e.printStackTrace();
                        }

                    }
                });

            }
        });
    }

    private boolean validation() {
        boolean flag = true;
        String shoulderStr = shoulder.getText().toString();
        String chestStr = chest.getText().toString();


        if (memberName != null && memberName.getSelectedItem().toString().trim().equals("Select")) {
            Snackbar.make(mLinearLayout, getResources().getString(R.string.memberNameErr), Snackbar.LENGTH_SHORT).show();
            flag = false;
        }
        if (shoulderStr.isEmpty()) {
            flag = false;
            shoulder.setError(getResources().getString(R.string.error));
        }
        if (chestStr.isEmpty()) {
            flag = false;
            chest.setError(getResources().getString(R.string.error));
        }


        return flag;
    }

    private void initView() {

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
        mLinearLayout = findViewById(R.id.linearLayout);
        save = findViewById(R.id.save);
        memberName = findViewById(R.id.memberName);
        neck = findViewById(R.id.neck);
        shoulder = findViewById(R.id.shoulder);
        chest = findViewById(R.id.chest);
        back = findViewById(R.id.back);
        abdominal = findViewById(R.id.abdominal);
        hand =
                findViewById(R.id.hand);
        legs = findViewById(R.id.legs);
        progressBar = findViewById(R.id.progressBar);
        mMemberNameArr = new ArrayList<>();
        memberNameList = new ArrayList<>();
        memberNameList.add("Select Member");

    }

    public void showDialogSuccess(final String message) {

        final Dialog dialog = new Dialog(Physiotherapy_Treatment_Activity.this);
        dialog.setContentView(R.layout.custom_alert_dialog_box);
        TextView mesg = dialog.findViewById(R.id.msg);
        mesg.setText(message);
        Button okbtn = dialog.findViewById(R.id.okbtn);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);


            }
        });

dialog.show();
    }
}


