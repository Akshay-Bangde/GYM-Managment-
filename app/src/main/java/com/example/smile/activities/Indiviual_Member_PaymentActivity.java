package com.example.smile.activities;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.smile.R;
import com.example.smile.dataObjects.MemberGroupListModel;
import com.example.smile.dataObjects.MemberListModel;
import com.example.smile.dataObjects.PackageModel;
import com.example.smile.dataObjects.StaffListModel;
import com.example.smile.dataObjects.TrainerModel;
import com.example.smile.utils.NetworkConnectivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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

public class Indiviual_Member_PaymentActivity extends AppCompatActivity {

    Spinner spinner_package, spinner_trainer, spinnertrainerCheck, mPayementOption_Spinner;
    ArrayList<String> package_List, trainerCheck, trainer_List, mPaymentOption;
    String packages, trainername, personltrainercheck, selectPaymentType,
            selectPackageId, trainer_Id;
    LinearLayout mlinearLayout, mpt_linearLayout;
    private CoordinatorLayout mCoordinatorLayout;

    EditText mRegistrationAmount, mDiscount, mPayAmount, mGstPercent, mChequeNo;
    Button mSaveBtn;
    TextView packageChrge, mPackageTotalAmount, mtrainerCharge, mSubtotal, mGstAmount,
            mNetAmount, mRemainingAmount, mTotalAmount;

    private ArrayList<PackageModel> mPackagesArr;
    private ArrayList<TrainerModel> mTrainerArr;
    final String Spinner_URL = BaseURL + "Group";
    final String Add_payment_URL = BaseURL + "AddPaymentDetails";
    String Grid_URL = BaseURL + "MemberRegistrationGrid";

    String mSerialid, mMemberId;
    String Message;
    String id;
    MemberListModel data = null;
    ProgressBar progressBar;
    LinearLayout mPaymentTypelayout;
    Bundle bundle ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indiviual__member_);
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


        // Bundle extras = new Bundle();
         bundle = getIntent().getExtras();
        if (bundle != null) {
          //  mMemberId = bundle.getString("memberID");
            mSerialid = bundle.getString("serialId");
        }

        init();

        run(Spinner_URL);
        clicksHandler();


        SaveButton();

        Intent intent = getIntent();
        if (intent != null) {
            try {
                data = (MemberListModel) intent.getSerializableExtra("packageData");
            } catch (Exception e) {
                Log.d("exception", e.toString());
            }
        }


    }

    public void init() {
        mPaymentTypelayout = findViewById(R.id.paymentTypeLayout);
        progressBar = findViewById(R.id.progressBar);
        mlinearLayout = findViewById(R.id.linearLayout);
        mpt_linearLayout = findViewById(R.id.pt_linearLayout);
        mCoordinatorLayout = findViewById(R.id.coordinator_layout);
        packageChrge = findViewById(R.id.totalamount);
        mRegistrationAmount = findViewById(R.id.registrationAmount);
        mPayAmount = findViewById(R.id.payAmount);
        mDiscount = findViewById(R.id.Discount);
        mGstPercent = findViewById(R.id.gstEdit);
        mGstAmount = findViewById(R.id.GstAmount);
        mNetAmount = findViewById(R.id.netAmount);
        mRemainingAmount = findViewById(R.id.remainingAmount);
        mTotalAmount = findViewById(R.id.totalAmount);
        mChequeNo = findViewById(R.id.chequeNO);
        mSubtotal = findViewById(R.id.subTotalAmount);
        mPayementOption_Spinner = findViewById(R.id.payment_option_spinner);
        mSaveBtn = findViewById(R.id.save);
        spinner_package = findViewById(R.id.package_spinner);
        spinner_trainer = findViewById(R.id.trainer_spinner);
        spinnertrainerCheck = findViewById(R.id.trainerCheck);
        mPackageTotalAmount = findViewById(R.id.TOTALPACKAGEAMOUNT);
        mtrainerCharge = findViewById(R.id.trainerCharge);
        mPackagesArr = new ArrayList<>();
        mTrainerArr = new ArrayList<>();

        package_List = new ArrayList<String>();
        package_List.add("Select Package");

        trainerCheck = new ArrayList<String>();
        trainerCheck.add("Select");
        trainerCheck.add("Yes");
        trainerCheck.add("No");

        ArrayAdapter<String> checktrainerAdapter = new ArrayAdapter<String>
                (Indiviual_Member_PaymentActivity.this, android.R.layout.simple_list_item_1, trainerCheck);
        checktrainerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnertrainerCheck.setAdapter(checktrainerAdapter);


        trainer_List = new ArrayList<String>();
        trainer_List.add("Select Trainer");

        mPaymentOption = new ArrayList<String>();
        mPaymentOption.add("Select");
        mPaymentOption.add("Cash");
        mPaymentOption.add("Cheque");

        ArrayAdapter<String> paymentOptionAdapeter = new ArrayAdapter<String>
                (Indiviual_Member_PaymentActivity.this, android.R.layout.simple_list_item_1, mPaymentOption);
        paymentOptionAdapeter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPayementOption_Spinner.setAdapter(paymentOptionAdapeter);


    }

    private void clicksHandler() {

        mRegistrationAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String registrationAmount = mRegistrationAmount.getText().toString();
                if (registrationAmount != null && !registrationAmount.isEmpty()) {
                    SubTotalCalculation();
                    RemainingAmount();
                    NetAmount();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDiscount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String discount = mDiscount.getText().toString();
                if (discount != null && !discount.isEmpty())
                    SubTotalCalculation();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mGstPercent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String gstPercent = mGstPercent.getText().toString();
                if (gstPercent != null && !gstPercent.isEmpty()) {
                    GSTCalculation();
                    NetAmount();
                    RemainingAmount();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mPayAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String amount = mPayAmount.getText().toString();
                if (amount != null && !amount.isEmpty()) {
                    RemainingAmount();
                    TotalAmount();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        spinner_package.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    packageChrge.setText("0");
                } else {
                    if (mPackagesArr != null && mPackagesArr.size() > 0) {
                        packageChrge.setText(mPackagesArr.get(position - 1).totalAmount);
                        mPackageTotalAmount.setText(mPackagesArr.get(position - 1).totalAmount);
                    }


                }

                packages = package_List.get(position).toString();
                selectPackageId = mPackagesArr.get(position).packageId.toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnertrainerCheck.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("Select")) {
                    mpt_linearLayout.setVisibility(View.GONE);
                    int pos = 0;
                    spinner_trainer.setSelection(pos);
                    mtrainerCharge.setText("0");

                } else if (selectedItem.equals("Yes")) {
                    mpt_linearLayout.setVisibility(View.VISIBLE);

                } else if (selectedItem.equals("No")) {

                    mpt_linearLayout.setVisibility(View.GONE);
                    int pos = 0;
                    spinner_trainer.setSelection(pos);
                    mtrainerCharge.setText("0");
                    String packageCharge = packageChrge.getText().toString();
                    if (packageCharge != null && !packageCharge.isEmpty() && !packageCharge.equals("0")) {
                        mPackageTotalAmount.setText(packageCharge);
                    } else {
                        mPackageTotalAmount.setText("0");
                    }
                }
                personltrainercheck = trainerCheck.get(position).toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_trainer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    mtrainerCharge.setText("0");
                } else {
                    if (mTrainerArr != null && mTrainerArr.size() > 0) {
                        mtrainerCharge.setText(mTrainerArr.get(position - 1).trainerCharge);
                        String totalAmount = mPackageTotalAmount.getText().toString();

                        int amount = 0;
                        if (totalAmount != null && !totalAmount.isEmpty() && mTrainerArr.get(position - 1).trainerCharge != null && !mTrainerArr.get(position - 1).trainerCharge.isEmpty()) {
                            amount = Integer.parseInt(totalAmount) + Integer.parseInt(mTrainerArr.get(position - 1).trainerCharge);
                            mPackageTotalAmount.setText(amount + "");
                        }
                    }
                }

                trainername = trainer_List.get(position).toString();
                trainer_Id = mTrainerArr.get(position).trainerId.toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mPayementOption_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("Select")) {
                    mPaymentTypelayout.setVisibility(View.GONE);

                } else if (selectedItem.equals("Cash")) {
                    mPaymentTypelayout.setVisibility(View.GONE);
                } else if (selectedItem.equals("Cheque")) {

                    mPaymentTypelayout.setVisibility(View.VISIBLE);
                }

                selectPaymentType = mPaymentOption.get(position).toString();
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    //Get SPINNER DATA API
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

                Indiviual_Member_PaymentActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        //JSONObject for PackageName
                        try {
                            JSONObject forecast = new JSONObject(myResponse);
                            JSONArray jsonArray = forecast.getJSONArray("Packages");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String packname = object.getString("onlyname");
                                String package_id = object.getString("package_id");
                                String total_amount = object.getString("total_amount");
                                package_List.add(packname);

                                PackageModel packageModel = new PackageModel();
                                packageModel.packageName = packname;
                                packageModel.packageId = package_id;
                                packageModel.totalAmount = total_amount;
                                mPackagesArr.add(packageModel);

                            }
                            PackageModel packageModel = new PackageModel();
                            packageModel.packageName = "Select";
                            packageModel.packageId = "0";
                            packageModel.totalAmount = "123";
                            mPackagesArr.add(0, packageModel);
                            ArrayAdapter<String> packageAdapter = new ArrayAdapter<>(Indiviual_Member_PaymentActivity.this,
                                    android.R.layout.simple_list_item_1, package_List);
                            packageAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                            spinner_package.setAdapter(packageAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //JSONObject for Enquired By
                        try {
                            JSONObject forecast = new JSONObject(myResponse);
                            JSONArray jsonArray = forecast.getJSONArray("Trainers");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String trainerName = object.getString("onlyname");
                                String trainer_id = object.getString("staff_id");
                                String trainerCharge = object.getString("Charge");
                                trainer_List.add(trainerName);

                                TrainerModel trainerModel = new TrainerModel();
                                trainerModel.trainerName = trainerName;
                                trainerModel.trainerId = trainer_id;
                                trainerModel.trainerCharge = trainerCharge;
                                mTrainerArr.add(trainerModel);


                            }
                            TrainerModel trainerModel = new TrainerModel();
                            trainerModel.trainerName = "Select";
                            trainerModel.trainerId = "0";
                            trainerModel.trainerCharge = "123";
                            mTrainerArr.add(0, trainerModel);
                            ArrayAdapter<String> trainerAdapter = new ArrayAdapter<>(Indiviual_Member_PaymentActivity.this,
                                    android.R.layout.simple_list_item_1, trainer_List);
                            trainerAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                            spinner_trainer.setAdapter(trainerAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
        });
    }

    public void SaveButton() {
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean networkConectivity = NetworkConnectivity.isConnected(Indiviual_Member_PaymentActivity.this);
                if (networkConectivity) {
                    if (validation()) {
                        mSaveBtn.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);

                        if (data != null ) {
                            String id = data.getId();
                           // String member_id = data.getPunchId();
                            String packageName = selectPackageId;
                            String packageCharge = packageChrge.getText().toString();
                            String trainerCheck = personltrainercheck;
                            String trainerName = trainer_Id;
                            String trainerCharge = mtrainerCharge.getText().toString();
                            String regAmount = mRegistrationAmount.getText().toString();
                            String ToaPackAmount = mPackageTotalAmount.getText().toString();
                            String discount = mDiscount.getText().toString();
                            String subttotal = mSubtotal.getText().toString();
                            String payAmt = mPayAmount.getText().toString();
                            String gstPer = mGstPercent.getText().toString();
                            String gstAmt = mGstAmount.getText().toString();
                            String netAmt = mNetAmount.getText().toString();
                            String reamaingAmt = mRemainingAmount.getText().toString();
                            String totalAmt = mTotalAmount.getText().toString();
                            String payemntType = selectPaymentType;
                            String chequeDDNumber = mChequeNo.getText().toString();

                            EditHandler handler = new EditHandler(id,/*member_id,*/ packageName, packageCharge, trainerCheck,
                                    trainerName, trainerCharge, regAmount, ToaPackAmount, discount, subttotal,
                                    payAmt, gstPer, gstAmt, netAmt, reamaingAmt, totalAmt, payemntType, chequeDDNumber);

                            String result = null;
                            try {
                                result = handler.execute(Add_payment_URL).get();

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                        else if (bundle != null) {
                            String serialId = mSerialid;
                          //  String member_id = mMemberId;
                            String packageName = selectPackageId;
                            String packageCharge = packageChrge.getText().toString();
                            String trainerCheck = personltrainercheck;
                            String trainerName = trainer_Id;
                            String trainerCharge = mtrainerCharge.getText().toString();
                            String regAmount = mRegistrationAmount.getText().toString();
                            String ToaPackAmount = mPackageTotalAmount.getText().toString();
                            String discount = mDiscount.getText().toString();
                            String subttotal = mSubtotal.getText().toString();
                            String payAmt = mPayAmount.getText().toString();
                            String gstPer = mGstPercent.getText().toString();
                            String gstAmt = mGstAmount.getText().toString();
                            String netAmt = mNetAmount.getText().toString();
                            String reamaingAmt = mRemainingAmount.getText().toString();
                            String totalAmt = mTotalAmount.getText().toString();
                            String payemntType = selectPaymentType;
                            String chequeDDNumber = mChequeNo.getText().toString();

                            PostHandler handler = new PostHandler(serialId,/*member_id,*/packageName, packageCharge, trainerCheck,
                                    trainerName,trainerCharge, regAmount, ToaPackAmount, discount, subttotal,
                                    payAmt, gstPer, gstAmt, netAmt,reamaingAmt,totalAmt,payemntType,chequeDDNumber);

                            String result = null;
                            try {
                                result = handler.execute(Add_payment_URL).get();

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                } else {
                    NetworkConnectivity.networkConnetivityShowDialog(Indiviual_Member_PaymentActivity.this);

                }
            }
        });
    }

    //for Post Request
    public class PostHandler extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();
        String serialId, /*member_id,*/ packageId, packageCharge, trainerCheck, trainerId, trainerCharge,
                registrationAmount, totalPackageAmount, discount, subtotal, payAmount,
                gstPercent, gstAmount, netAmount, remainingAmount,
                totalAmount, paymentType, chequeDDnumber;

        public PostHandler(String serialid, /*String member_id,*/
                           String packageId, String packageCharge,
                           String trainerCheck, String trainerId,
                           String trainerCharge, String registrationAmount,
                           String totalPackageAmount, String discount,
                           String subtotal, String payAmount, String gstPercent,
                           String gstAmount, String netAmount,
                           String remainingAmount, String totalAmount,
                           String paymentType, String chequeDDnumber) {

            this.serialId = serialid;
         //  this.member_id = member_id;
            this.packageId = packageId;
            this.packageCharge = packageCharge;
            this.trainerCheck = trainerCheck;
            this.trainerId = trainerId;
            this.trainerCharge = trainerCharge;
            this.registrationAmount = registrationAmount;
            this.totalPackageAmount = totalPackageAmount;
            this.discount = discount;
            this.subtotal = subtotal;
            this.payAmount = payAmount;
            this.gstPercent = gstPercent;
            this.gstAmount = gstAmount;
            this.netAmount = netAmount;
            this.remainingAmount = remainingAmount;
            this.totalAmount = totalAmount;
            this.paymentType = paymentType;
            this.chequeDDnumber = chequeDDnumber;

        }

        @Override
        protected String doInBackground(String... params) {
            RequestBody formBody = new FormBody.Builder()

                    .add("id", serialId)
                   // .add("member_id", member_id)
                    .add("packid", this.packageId)
                    .add("traing_amt", packageCharge)
                    .add("prsntr", trainerCheck)
                    .add("prsntrid", this.trainerId)
                    .add("pttraing_amt", trainerCharge)
                    .add("regamount", registrationAmount)
                    .add("totalamt", totalPackageAmount)
                    .add("discamt", discount)
                    .add("subtotal", subtotal)
                    .add("payamt", payAmount)
                    .add("txper", gstPercent)
                    .add("txamt", gstAmount)
                    .add("netamount", netAmount)
                    .add("remains", remainingAmount)
                    .add("finalamt", totalAmount)
                    .add("paytype", paymentType)
                    .add("chqddnum", chequeDDnumber)
                    .build();

            Request request = new Request.Builder()
                    .url(Add_payment_URL)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String myResponse = response.body().string();
                    Indiviual_Member_PaymentActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject forecast = null;
                            try {
                                forecast = new JSONObject(myResponse);
                                Message = forecast.getString("message");
                                mSaveBtn.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
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

    public class EditHandler extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();
        String  id,/*member_id,*/ packageId, packageCharge, trainerCheck, trainerId, trainerCharge,
                registrationAmount, totalPackageAmount, discount, subtotal, payAmount,
                gstPercent, gstAmount, netAmount, remainingAmount,
                totalAmount, paymentType, chequeDDnumber;

        public EditHandler(  String id,/*String member_id,*/
                                           String packageId, String packageCharge,
                                           String trainerCheck, String trainerId,
                                           String trainerCharge, String registrationAmount,
                                           String totalPackageAmount, String discount,
                                           String subtotal, String payAmount, String gstPercent,
                                           String gstAmount, String netAmount,
                                           String remainingAmount, String totalAmount,
                                           String paymentType, String chequeDDnumber) {
            this.id =id;
          ///  this.member_id = member_id;
            this.packageId = packageId;
            this.packageCharge = packageCharge;
            this.trainerCheck = trainerCheck;
            this.trainerId = trainerId;
            this.trainerCharge = trainerCharge;
            this.registrationAmount = registrationAmount;
            this.totalPackageAmount = totalPackageAmount;
            this.discount = discount;
            this.subtotal = subtotal;
            this.payAmount = payAmount;
            this.gstPercent = gstPercent;
            this.gstAmount = gstAmount;
            this.netAmount = netAmount;
            this.remainingAmount = remainingAmount;
            this.totalAmount = totalAmount;
            this.paymentType = paymentType;
            this.chequeDDnumber = chequeDDnumber;
        }

        @Override
        protected String doInBackground(String... params) {
            RequestBody formBody = new FormBody.Builder()
                    .add("id",id)
                    //.add("member_id", member_id)
                    .add("packid", this.packageId)
                    .add("traing_amt", packageCharge)
                    .add("prsntr", trainerCheck)
                    .add("prsntrid", this.trainerId)
                    .add("pttraing_amt", trainerCharge)
                    .add("regamount", registrationAmount)
                    .add("totalamt", totalPackageAmount)
                    .add("discamt", discount)
                    .add("subtotal", subtotal)
                    .add("payamt", payAmount)
                    .add("txper", gstPercent)
                    .add("txamt", gstAmount)
                    .add("netamount", netAmount)
                    .add("remains", remainingAmount)
                    .add("finalamt", totalAmount)
                    .add("paytype", paymentType)
                    .add("chqddnum", chequeDDnumber)
                    .build();

            Request request = new Request.Builder()
                    .url(Add_payment_URL)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String myResponse = response.body().string();
                    Indiviual_Member_PaymentActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject forecast = null;
                            try {
                                forecast = new JSONObject(myResponse);
                                Message = forecast.getString("message");
                                mSaveBtn.setVisibility(View.VISIBLE);
                                progressBar.setVisibility(View.GONE);
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

        final Dialog dialog = new Dialog(Indiviual_Member_PaymentActivity.this);
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

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();


    }

    public void SubTotalCalculation() {
        if (mPackageTotalAmount.getText().toString().isEmpty()
                || mDiscount.getText().toString().isEmpty()
                || mRegistrationAmount.getText().toString().isEmpty()) {
            mSubtotal.setText("0");
            mRemainingAmount.setText("0");
        }

        if (
                mDiscount.getText().toString().isEmpty()
                        || mDiscount.getText().toString().equals("0")
        ) {

            try {
                if (mRegistrationAmount.getText().toString() != null && !mRegistrationAmount.getText().toString().isEmpty() && mPackageTotalAmount.getText().toString() != null && !mPackageTotalAmount.getText().toString().isEmpty()) {
                    long subtotal = (Long.parseLong(mPackageTotalAmount.getText().toString())) -
                            (Long.parseLong(mRegistrationAmount.getText().toString()));
                    String subtotall = Long.toString(subtotal);
                    mSubtotal.setText(subtotall);
                    mRemainingAmount.setText(subtotall);
                }
            } catch (Exception e) {

            }
        }
        if (
                mRegistrationAmount.getText().toString().isEmpty()
                        || mRegistrationAmount.getText().toString().equals("0")
        ) {

            long subtotal = (Long.parseLong(mPackageTotalAmount.getText().toString())) -
                    (Long.parseLong(mDiscount.getText().toString()));
            String subtotall = Long.toString(subtotal);
            mSubtotal.setText(subtotall);
            mRemainingAmount.setText(subtotall);
        }
        if (!mPackageTotalAmount.getText().toString().isEmpty()
                || !mRegistrationAmount.getText().toString().isEmpty()
                || !mDiscount.getText().toString().isEmpty()
        ) {
            try {
                if (mRegistrationAmount.getText().toString() != null && !mRegistrationAmount.getText().toString().isEmpty() && mPackageTotalAmount.getText().toString() != null && !mPackageTotalAmount.getText().toString().isEmpty()) {
                    long subtotal = (Long.parseLong(mPackageTotalAmount.getText().toString())) -
                            (Long.parseLong(mRegistrationAmount.getText().toString())) -
                            (Long.parseLong(mDiscount.getText().toString()));
                    String subtotall = Long.toString(subtotal);
                    mSubtotal.setText(subtotall);
                    mRemainingAmount.setText(subtotall);
                }
            } catch (Exception e) {
            }
        }
    }

    public void GSTCalculation() {
        if (mPackageTotalAmount.getText().toString().isEmpty()
                || mGstPercent.getText().toString().isEmpty()) {
            mGstAmount.setText("0");
        } else {

            Long subtotal = (Long.parseLong(mPackageTotalAmount.getText().toString()));
            float gstPer = Float.parseFloat(mGstPercent.getText().toString());
            float gstAmount = (subtotal * gstPer) / 100;
            String gstAmt = Float.toString(gstAmount);
            mGstAmount.setText(gstAmt);
        }
    }

    public void NetAmount() {
        if (mPackageTotalAmount.getText().toString().isEmpty()
                || mGstAmount.getText().toString().isEmpty()) {
            mNetAmount.setText("0");
        } else {

            Float subtotal = (Float.parseFloat(mPackageTotalAmount.getText().toString())) +
                    (Float.parseFloat(mGstAmount.getText().toString()));
            String subtotall = Float.toString(subtotal);
            mNetAmount.setText(subtotall);
        }
    }

    public void RemainingAmount() {
        if (mPackageTotalAmount.getText().toString().isEmpty()
                || mRegistrationAmount.getText().toString().isEmpty()) {
            mRemainingAmount.setText("0");
        }
        if (!mPackageTotalAmount.getText().toString().isEmpty()
                || !mRegistrationAmount.getText().toString().isEmpty()
                || mRegistrationAmount.getText().toString().equals("0")

        ) {

            long subtotal = (Long.parseLong(mPackageTotalAmount.getText().toString())) -
                    (Long.parseLong(mRegistrationAmount.getText().toString()));
            String subtotall = Long.toString(subtotal);
            mRemainingAmount.setText(subtotall);
        }
        if (!mPayAmount.getText().toString().isEmpty()
                || !mNetAmount.getText().toString().isEmpty()
                || !mPackageTotalAmount.getText().toString().isEmpty()
                || !mRegistrationAmount.getText().toString().isEmpty()
                || mPayAmount.getText().toString().equals("0")
                || mNetAmount.getText().toString().equals("0")
        ) {

            Float subtotal = (Float.parseFloat(mNetAmount.getText().toString())) -
                    (Float.parseFloat(mPayAmount.getText().toString()));
            String subtotall = Float.toString(subtotal);
            mRemainingAmount.setText(subtotall);
        }
    }

    public void TotalAmount() {
        if (mRemainingAmount.getText().toString().isEmpty()) {
            mTotalAmount.setText("0");
        } else {

            float subtotal = (Float.parseFloat(mRemainingAmount.getText().toString()));
            String subtotall = Float.toString(subtotal);
            mTotalAmount.setText(subtotall);
        }

    }

    public boolean validation() {
        boolean flag = true;
        if (spinner_package.getSelectedItem().toString().trim().equals("Select Package")) {
            flag = false;
            Snackbar.make(mlinearLayout, getResources().getString(R.string.plz_select_package), Snackbar.LENGTH_SHORT).show();
        }
        else if (spinnertrainerCheck.getSelectedItem().toString().trim().equals("Select")) {
            flag = false;
            Snackbar.make(mlinearLayout, "Please select personal trainer.", Snackbar.LENGTH_SHORT).show();
        }
        else if (spinner_trainer.getSelectedItem().toString().trim().equals("Select Trainer")) {
            flag = false;
            Snackbar.make(mlinearLayout, "Please select personal trainer name.", Snackbar.LENGTH_SHORT).show();
        }
        else if (mPayementOption_Spinner.getSelectedItem().toString().trim().equals("Select")) {
            flag = false;
            Snackbar.make(mlinearLayout, getResources().getString(R.string.plz_select_payment_method), Snackbar.LENGTH_SHORT).show();
        }
        else if (mPayementOption_Spinner.getSelectedItem().toString().trim().equals("Cheque")) {
            if(mChequeNo.getText().toString().isEmpty()) {
                flag = false;
                mChequeNo.setError("Please enter DD/Ref number.");

            }
        }

        return flag;
    }


}
