package com.example.smile.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.smile.R;
import com.example.smile.activities.AddMedicalHealth_Activity;
import com.example.smile.activities.Add_Diet_Plan_Activity;
import com.example.smile.activities.Add_Sell_Product_Activity;
import com.example.smile.activities.Add_Staff_Activity;
import com.example.smile.dataObjects.GeneralFragmentModel;
import com.example.smile.dataObjects.GroupNameModel;
import com.example.smile.dataObjects.MemberNameModel;
import com.example.smile.dataObjects.ProductNameModel;
import com.example.smile.utils.NetworkConnectivity;

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

public class General_Fragment extends Fragment {
    ViewPager viewPager;

    Spinner mMemberNameSpinner;
    ArrayList<String> mMembernameArrayList;
    ArrayList<MemberNameModel> mMembernameArr;
    String membername;
    String MemberSpinnerURL = BaseURL + "AddMemberAttendance";

    LinearLayout mLinearlayour;
    Button mNextbtn;

    EditText mWeightEdt, mHeightEdt, mNeckEdt, mShoulderEdt,
            mChestEdt, mUpperArmEdt, mmForeArmEdt, mWristEdt,
            mUpperAbdomenEdt, mLowerAbdomenedt, mWaistEdt,
            mHipsEdt, mThighEdt, mCalfEdt, mAnkleEdt,

    mHeartproblemEdt, mBloodPressureEdt,
            mIllnessEdt, mDiffcultyEdt, mAdviceEdt,
            mSurgeryEdt, mBreathingEdt, mMusclesEdt, mDiabetesEdt, mCigrettteEdt,
            mCholestrolEdt, mObesityEdt;

    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_general, container, false);


        init();

        run(MemberSpinnerURL);
        ClicksHandler();
        NextButton();


        return v;


    }

    public void init() {
        mNextbtn = (Button) v.findViewById(R.id.nextBtn);
        mLinearlayour = (LinearLayout) v.findViewById(R.id.linearLayout);
        mWeightEdt = (EditText) v.findViewById(R.id.weight);
        mHeightEdt = (EditText) v.findViewById(R.id.height);
        mNeckEdt = (EditText) v.findViewById(R.id.neck);
        mShoulderEdt = (EditText) v.findViewById(R.id.shoulder);
        mChestEdt = (EditText) v.findViewById(R.id.chest);
        mUpperArmEdt = (EditText) v.findViewById(R.id.upperArm);
        mmForeArmEdt = (EditText) v.findViewById(R.id.foreArm);
        mWristEdt = (EditText) v.findViewById(R.id.wrist);
        mUpperAbdomenEdt = (EditText) v.findViewById(R.id.upperAbdomen);
        mLowerAbdomenedt = (EditText) v.findViewById(R.id.lowerAbdomen);
        mWaistEdt = (EditText) v.findViewById(R.id.waist);
        mHipsEdt = (EditText) v.findViewById(R.id.hips);
        mThighEdt = (EditText) v.findViewById(R.id.thigh);
        mCalfEdt = (EditText) v.findViewById(R.id.calf);
        mAnkleEdt = (EditText) v.findViewById(R.id.ankle);


        mMemberNameSpinner = (Spinner) v.findViewById(R.id.memberName);
        mMembernameArr = new ArrayList<MemberNameModel>();
        mMembernameArrayList = new ArrayList<>();
        mMembernameArrayList.add("Select");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_list_item_1, mMembernameArrayList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMemberNameSpinner.setAdapter(adapter);

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NextButton();
            }
        });

    }

    public void NextButton() {
        mNextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean networkConectivity = NetworkConnectivity.isConnected(getActivity());
                if (networkConectivity) {
                    if (validation()) {



                        GeneralFragmentModel generalFragmentModel = new GeneralFragmentModel();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("generalFragmentModel", generalFragmentModel);

                        Questionnaire_Fragment fragment = new Questionnaire_Fragment();
                        fragment.setArguments(bundle);

                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        viewPager.setCurrentItem(1);
                        fragmentTransaction.replace(R.id.frameLayout, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }

                } else {
                    NetworkConnectivity.networkConnetivityShowDialog(getActivity());

                }
            }
        });

    }

    public void ClicksHandler() {
        mMemberNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                membername = mMembernameArrayList.get(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void run(String url) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("jhsj", e.toString());

            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {

                final String myResponse = response.body().string();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        JSONObject forecast = null;
                        try {
                            forecast = new JSONObject(myResponse);
                            {
                                JSONArray jsonArray = forecast.getJSONArray("MemberName");
                                for (int i = 0; i < jsonArray.length(); i++) {


                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String name = object.getString("fullname");
                                    String id = object.getString("id");
                                    String member_id = object.getString("member_id");
                                    mMembernameArrayList.add(name);

                                    MemberNameModel nameModel = new MemberNameModel();
                                    nameModel.id = id;
                                    nameModel.fullname = name;
                                    nameModel.member_id = member_id;
                                    mMembernameArr.add(nameModel);

                                }
                                MemberNameModel nameModel = new MemberNameModel();
                                nameModel.id = "0";
                                nameModel.member_id = "0";
                                nameModel.fullname = "Select";
                                mMembernameArr.add(0, nameModel);

                            }
                        } catch (Exception e) {

                            e.printStackTrace();
                        }

                    }
                });

            }
        });
    }

    private boolean validation() {
        String weight = mWeightEdt.getText().toString();
        String height = mHeightEdt.getText().toString();
        String neck = mNeckEdt.getText().toString();
        String shoulder = mShoulderEdt.getText().toString();
        String chest = mChestEdt.getText().toString();
        String upperAbdomen = mUpperAbdomenEdt.getText().toString();
        String lowerAbdomen = mLowerAbdomenedt.getText().toString();

        boolean flag = true;
        if (mMemberNameSpinner.getSelectedItem().toString().trim().equals("Select")) {
            Snackbar.make(mLinearlayour, getResources().getString(R.string.spinner), Snackbar.LENGTH_SHORT).show();
        }
        if (weight.isEmpty()) {
            flag = false;
            mWeightEdt.setError("Please enter weight.");
        }
        if (height.isEmpty()) {
            flag = false;
            mHeightEdt.setError("Please enter height.");
        }
        if (neck.isEmpty()) {
            flag = false;
            mNeckEdt.setError("Please enter neck.");
        }
        if (shoulder.isEmpty()) {
            flag = false;
            mShoulderEdt.setError("Please enter shoulder.");
        }
        if (chest.isEmpty()) {
            flag = false;
            mChestEdt.setError("Please enter chest.");
        }
        if (upperAbdomen.isEmpty()) {
            flag = false;
            mUpperAbdomenEdt.setError("Please enter Upper Abdomen.");
        }
        if (lowerAbdomen.isEmpty()) {
            flag = false;
            mLowerAbdomenedt.setError("Please enter Lower Abdomen.");
        }

        return flag;
    }


    @SuppressLint("ValidFragment")
    public General_Fragment(ViewPager viewPager) {
        this.viewPager = viewPager;
    }

    public General_Fragment() {

    }
}
