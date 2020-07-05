package com.example.smile.activities;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.smile.R;
import com.example.smile.utils.NetworkConnectivity;

import org.jetbrains.annotations.NotNull;
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

public class SalaryListPayNow extends AppCompatActivity {

    TextView mBasicSalary, mOverTime, mNetSalary;
    EditText mBonus, mHours, mRemark, DDno;
    Spinner mPaymentTypeSpinner;
    ArrayList<String> paymentTypeArraylist;
    String paymentType;
    public String add_url = BaseURL + "Equipement";
    LinearLayout mPaymentTypelayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_salary_list_pay_now);
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


        init();
        ClicksHandler();
        Savebutton();

    }

    public void init() {
        mBasicSalary = findViewById(R.id.basicSalary);
        mOverTime = findViewById(R.id.overtime);
        mNetSalary = findViewById(R.id.netsalary);

        mBonus = findViewById(R.id.bonus);
        mHours = findViewById(R.id.hours);
        mRemark = findViewById(R.id.remarks);
        mPaymentTypeSpinner = findViewById(R.id.paymentTypeSpinner);
        mPaymentTypelayout = findViewById(R.id.paymentTypelayout);

        paymentTypeArraylist = new ArrayList<>();
        paymentTypeArraylist.add("Select");
        paymentTypeArraylist.add("Cash");
        paymentTypeArraylist.add("Cheque");
        paymentTypeArraylist.add("DD");

        ArrayAdapter<String> fieldAdpater = new ArrayAdapter<String>(SalaryListPayNow.this,
                android.R.layout.simple_list_item_1, paymentTypeArraylist);
        fieldAdpater.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mPaymentTypeSpinner.setAdapter(fieldAdpater);


    }

    public void Savebutton() {
        boolean networkConectivity = NetworkConnectivity.isConnected(SalaryListPayNow.this);
        if (networkConectivity) {
            if (validation()) {
                String basicsalary = mBasicSalary.getText().toString();
                String bonus = mBonus.getText().toString();
                String hours = mHours.getText().toString();
                String overtime = mOverTime.getText().toString();
                String netsalary = mNetSalary.getText().toString();
                String remarks = mRemark.getText().toString();
                String paymenttype = paymentType;
                String DDNo = DDno.getText().toString();

                PostHandler handler = new PostHandler(basicsalary, bonus, hours,
                        overtime, netsalary, remarks, paymenttype, DDNo);
                String result = null;
                try {
                    result = handler.execute(add_url).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        } else {
            NetworkConnectivity.networkConnetivityShowDialog(SalaryListPayNow.this);

        }
    }

    public void ClicksHandler() {
        mPaymentTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                if (selectedItem.equals("Select")) {
                    mPaymentTypelayout.setVisibility(View.GONE);
                } else if (selectedItem.equals("Cash")) {
                    mPaymentTypelayout.setVisibility(View.GONE);

                } else if (selectedItem.equals("Cheque")) {

                    mPaymentTypelayout.setVisibility(View.VISIBLE);
                } else if (selectedItem.equals("DD")) {

                    mPaymentTypelayout.setVisibility(View.VISIBLE);
                }

                paymentType = paymentTypeArraylist.get(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public class PostHandler extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();
        String basicSalary, bonus, hours, overtime, netSalary, remarks, type, ddNo;

        public PostHandler(String basicSalary, String bonus, String hours,
                           String overtime, String netSalary, String remarks,
                           String type, String ddNo) {
            this.basicSalary = basicSalary;
            this.bonus = bonus;
            this.hours = hours;
            this.overtime = overtime;
            this.netSalary = netSalary;
            this.remarks = remarks;
            this.type = type;
            this.ddNo = ddNo;
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody formBody = new FormBody.Builder()
                    .add("equipname", basicSalary)
                    .add("details", bonus)
                    .add("rate", hours)
                    .add("qty", overtime)
                    .add("amount", netSalary)
                    .add("amount", remarks)
                    .add("amount", type)
                    .add("amount", ddNo)
                    .build();


            Request request = new Request.Builder()
                    .url(add_url)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                    final String myResponse = response.body().string();
                    SalaryListPayNow.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject forecast = null;
                            try {
                                forecast = new JSONObject(myResponse);
                                String message = forecast.getString("message");
                                // mSaveBtn.setVisibility(View.VISIBLE);
                                // add_eqp_progressBar.setVisibility(View.GONE);

                                showDialog(message);

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

        final Dialog dialog = new Dialog(SalaryListPayNow.this);
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


    //for validations
    public Boolean validation() {
        String bonus = mBonus.getText().toString();
        String hours = mHours.getText().toString();
        String remarks = mRemark.getText().toString();
        boolean flag = true;
        if (bonus.isEmpty()) {
            flag = false;
            mBonus.setError(getResources().getString(R.string.plz_enter_the_equipment_name));
        }
        if (hours.isEmpty()) {
            flag = false;
            mHours.setError(getResources().getString(R.string.plz_enter_the_purchase_rate));
        }
        if (remarks.isEmpty()) {
            flag = false;
            mRemark.setError(getResources().getString(R.string.plz_enter_the_quantity));
        }
        return flag;
    }

}
