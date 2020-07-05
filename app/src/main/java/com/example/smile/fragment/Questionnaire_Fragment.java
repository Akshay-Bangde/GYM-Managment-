package com.example.smile.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smile.R;
import com.example.smile.activities.Add_Diet_Plan_Activity;
import com.example.smile.dataObjects.GeneralFragmentModel;
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

public class Questionnaire_Fragment extends Fragment {
    View view;
    ViewPager viewPager;
    Button mSaveButton;

    EditText mHeartproblemEdt, mBloodPressureEdt,
            mIllnessEdt, mDiffcultyEdt, mAdviceEdt,
            mSurgeryEdt, mBreathingEdt, mMusclesEdt, mDiabetesEdt, mCigrettteEdt,
            mCholestrolEdt, mObesityEdt;


   public  String mMemberName,mWeightEdt, mHeightEdt, mNeckEdt, mShoulderEdt,
            mChestEdt, mUpperArmEdt, mmForeArmEdt, mWristEdt,
            mUpperAbdomenEdt, mLowerAbdomenedt, mWaistEdt,
            mHipsEdt, mThighEdt, mCalfEdt, mAnkleEd;

    String GeneralinfoADD_URL = BaseURL + "MedicalHealth";
    Bundle bundle ;



    @SuppressLint("ValidFragment")
    public Questionnaire_Fragment(ViewPager viewPager) {
        this.viewPager = viewPager;



    }
    public  Questionnaire_Fragment(){

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMemberName = this.mMemberName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_questionnaire, container, false);
        init();


        bundle = this.getArguments();
        if (bundle != null) {
            GeneralFragmentModel generalFragmentModel= (GeneralFragmentModel) getArguments().getSerializable("generalFragmentModel");

        }
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean networkConectivity = NetworkConnectivity.isConnected(getActivity());
                if (networkConectivity) {

                    String MemberName = mMemberName;
                    String WeightEdt = mWeightEdt;
                    String HeightEdt = mHeightEdt;
                    String NeckEdt = mNeckEdt;
                    String ShoulderEdt = mShoulderEdt;
                    String ChestEdt = mChestEdt;
                    String UpperArmEdt = mUpperArmEdt;
                    String ForeArmEdt = mmForeArmEdt;
                    String WristEdt = mWristEdt;
                    String UpperAbdomenEdt = mUpperAbdomenEdt;
                    String LowerAbdomenedt = mLowerAbdomenedt;
                    String WaistEdt = mWaistEdt;
                    String HipsEdt = mHipsEdt;
                    String ThighEdt = mThighEdt;
                    String CalfEdt = mCalfEdt;
                    String AnkleEd = mAnkleEd;

                    PostHandler handler = new PostHandler(MemberName, WeightEdt, HeightEdt,
                            NeckEdt, ShoulderEdt, ChestEdt, UpperArmEdt, ForeArmEdt, WristEdt,
                            UpperAbdomenEdt, LowerAbdomenedt, WaistEdt, HipsEdt, ThighEdt,
                            CalfEdt, AnkleEd);

                    String result = null;

                    try {
                        result = handler.execute().get();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }
                else {
                    NetworkConnectivity.networkConnetivityShowDialog(getActivity());
                }
            }

        });

        SaveButton();
        return view;
    }

    public void init(){
        mSaveButton = (Button) view.findViewById(R.id.save);
    }

    public void SaveButton(){

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean networkConectivity = NetworkConnectivity.isConnected(getActivity());
                if (networkConectivity) {
                   // if (bundle != null) {
                        String MemberName = mMemberName;
                        String WeightEdt = mWeightEdt;
                        String HeightEdt = mHeightEdt;
                        String NeckEdt = mNeckEdt;
                        String ShoulderEdt = mShoulderEdt;
                        String ChestEdt = mChestEdt;
                        String UpperArmEdt = mUpperArmEdt;
                        String ForeArmEdt = mmForeArmEdt;
                        String WristEdt = mWristEdt;
                        String UpperAbdomenEdt = mUpperAbdomenEdt;
                        String LowerAbdomenedt = mLowerAbdomenedt;
                        String WaistEdt = mWaistEdt;
                        String HipsEdt = mHipsEdt;
                        String ThighEdt = mThighEdt;
                        String CalfEdt = mCalfEdt;
                        String AnkleEd = mAnkleEd;

                        PostHandler handler = new PostHandler(MemberName, WeightEdt, HeightEdt,
                                NeckEdt, ShoulderEdt, ChestEdt, UpperArmEdt, ForeArmEdt, WristEdt,
                                UpperAbdomenEdt, LowerAbdomenedt, WaistEdt, HipsEdt, ThighEdt,
                                CalfEdt, AnkleEd);

                        String result = null;

                        try {
                            result = handler.execute().get();

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                   // }
                }
                else {
                    NetworkConnectivity.networkConnetivityShowDialog(getActivity());
                }
            }

        });
    }


    class PostHandler extends AsyncTask<String, String, String> {
        OkHttpClient client = new OkHttpClient();

        String MemberName,WeightEdt, HeightEdt, NeckEdt, ShoulderEdt,
                ChestEdt, UpperArmEdt, ForeArmEdt, WristEdt,
                UpperAbdomenEdt, LowerAbdomenedt, WaistEdt,
                HipsEdt, ThighEdt, CalfEdt, AnkleEd;

        public PostHandler(String memberName, String weightEdt,
                           String heightEdt, String neckEdt,
                           String shoulderEdt, String chestEdt,
                           String upperArmEdt, String foreArmEdt,
                           String wristEdt, String upperAbdomenEdt,
                           String lowerAbdomenedt, String waistEdt,
                           String hipsEdt, String thighEdt, String calfEdt,
                           String ankleEd) {
            MemberName = memberName;
            WeightEdt = weightEdt;
            HeightEdt = heightEdt;
            NeckEdt = neckEdt;
            ShoulderEdt = shoulderEdt;
            ChestEdt = chestEdt;
            UpperArmEdt = upperArmEdt;
            ForeArmEdt = foreArmEdt;
            WristEdt = wristEdt;
            UpperAbdomenEdt = upperAbdomenEdt;
            LowerAbdomenedt = lowerAbdomenedt;
            WaistEdt = waistEdt;
            HipsEdt = hipsEdt;
            ThighEdt = thighEdt;
            CalfEdt = calfEdt;
            AnkleEd = ankleEd;
        }

        @Override
        protected String doInBackground(String... strings) {
            RequestBody formBody = new FormBody
                    .Builder()
                    .add("member", MemberName)
                    .add("weight", WeightEdt)
                    .add("height", HeightEdt)
                    .add("neck", NeckEdt)
                    .add("shoulder", ShoulderEdt)
                    .add("chest", ChestEdt)
                    .add("upperarm", UpperArmEdt)
                    .add("forearm", ForeArmEdt)
                    .add("wrist", WristEdt)
                    .add("upperabdomen", UpperAbdomenEdt)
                    .add("lowerabdomen", LowerAbdomenedt)
                    .add("waist", WaistEdt)
                    .add("hips", HipsEdt)
                    .add("thigh", ThighEdt)
                    .add("calf", CalfEdt)
                    .add("ankle", AnkleEd)
                    .build();


            Request request = new Request.Builder()
                    .url(GeneralinfoADD_URL)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String myResponse = response.body().string();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject forecast = null;
                            try {
                                forecast = new JSONObject(myResponse);
                               String Message = forecast.getString("message");
                               // mSaveButton.setVisibility(View.VISIBLE);
                               // dietProgress.setVisibility(View.GONE);
                               // reset();
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

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_alert_dialog_box);
        TextView mesg = dialog.findViewById(R.id.msg);
        mesg.setText(message);
        Button okbtn=dialog.findViewById(R.id.okbtn);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                getActivity().getFragmentManager().popBackStack();

            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }


}
