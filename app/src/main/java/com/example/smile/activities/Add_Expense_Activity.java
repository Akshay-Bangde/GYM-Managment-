package com.example.smile.activities;

import android.app.ActivityOptions;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.smile.R;
import com.example.smile.utils.CategoryCallBackInterface;
import com.example.smile.utils.NetworkConnectivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.smile.activities.SignInActivity.BaseURL;

public class Add_Expense_Activity extends AppCompatActivity {

    EditText exp_date, exp_ref, exp_billno, exp_partyname,
            exp_party_gst_no, exp_sub_amount,
            sGST, cGST, iGST, chequeDDNo;
    TextView exp_gtotal, sGSTamount, cGSTamount, iGSTamount;
    Button exp_btnsave;
    Spinner mGSTAppliSpinner, mPaymentModeSpinner;
    LinearLayout mLinearLayout, mChequeLayout;
    Calendar mDateCalender;
    ArrayList<String> payment_mode;
    String paymentMode;
    final String AddExpenseURL = BaseURL + "AddExpense";
    ProgressBar dietProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__expense);
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

        dietProgress=findViewById(R.id.pb);
        exp_date = findViewById(R.id.addexp_date);
        exp_ref = findViewById(R.id.exp_refno);
        exp_billno = findViewById(R.id.exp_billno);
        exp_partyname = findViewById(R.id.exp_partname);
        exp_party_gst_no = findViewById(R.id.exp_party_gstno);
        exp_sub_amount = findViewById(R.id.exp_subamount);
        exp_gtotal = findViewById(R.id.exp_gtotal);
        mGSTAppliSpinner = findViewById(R.id.GST_App);
        mPaymentModeSpinner = findViewById(R.id.payment_mode);
        mLinearLayout = findViewById(R.id.linear_layout);
        exp_btnsave = findViewById(R.id.exp_save);
        mChequeLayout = findViewById(R.id.chequeLayout);
        chequeDDNo = findViewById(R.id.cheque_no);


        sGST = findViewById(R.id.edt_sgst);
        cGST = findViewById(R.id.edt_cgst);
        iGST = findViewById(R.id.edt_igst);

        sGSTamount = findViewById(R.id.sgstamount);
        cGSTamount = findViewById(R.id.cgstamount);
        iGSTamount = findViewById(R.id.igstamount);


        calender();


        ArrayList<String> gst_applicable = new ArrayList<String>();
        gst_applicable.add("No");
        gst_applicable.add("Yes");

        ArrayAdapter<String> gst_applicableAdapter = new ArrayAdapter<String>(Add_Expense_Activity.this,
                android.R.layout.simple_list_item_1, gst_applicable);
        gst_applicableAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGSTAppliSpinner.setAdapter(gst_applicableAdapter);



        payment_mode = new ArrayList<String>();
        payment_mode.add("Select");
        payment_mode.add("Cash");
        payment_mode.add("Bank Transfer");
        payment_mode.add("Cheque");
        payment_mode.add("DD");

        ArrayAdapter<String> payment_modeAdapter = new ArrayAdapter<String>(Add_Expense_Activity.this,
                android.R.layout.simple_list_item_1, payment_mode);
        payment_modeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPaymentModeSpinner.setAdapter(payment_modeAdapter);


        SaveBtn();
        paymentSpinner();

        DisebleYESNO();
        Calculation();



    }
    private void SaveBtn() {
        exp_btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean networkConectivity = NetworkConnectivity.isConnected(Add_Expense_Activity.this);
                if (networkConectivity) {
                    if (Validation()) {
                        exp_btnsave.setVisibility(View.GONE);
                        dietProgress.setVisibility(View.VISIBLE);

                        String Date = exp_date.getText().toString();
                        String refNo = exp_ref.getText().toString();
                        String billNo = exp_billno.getText().toString();
                        String partyname = exp_partyname.getText().toString();
                        String gstNo = exp_party_gst_no.getText().toString();
                        String subTotal = exp_sub_amount.getText().toString();
                        String sGst = sGST.getText().toString();
                        String cGst = cGST.getText().toString();
                        String iGst = iGST.getText().toString();
                        String sGstAmount = sGSTamount.getText().toString();
                        String cGstAmount = cGSTamount.getText().toString();
                        String iGstAmount = iGSTamount.getText().toString();
                        String pMode = paymentMode;
                        String DDNo = chequeDDNo.getText().toString();
                        String grandTotal = exp_gtotal.getText().toString();


                        PostHandler handler = new PostHandler(Date,refNo,billNo,
                                partyname,gstNo,subTotal,sGst,cGst,iGst,
                                sGstAmount, cGstAmount ,iGstAmount ,
                                pMode ,DDNo ,grandTotal );

                        String result = null;
                        try {
                            result = handler.execute(AddExpenseURL).get();

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    NetworkConnectivity.networkConnetivityShowDialog(Add_Expense_Activity.this);

                }


            }

        });


    }

    public void paymentSpinner()    {

        mPaymentModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                String statusVal = selectedItem;
                if (statusVal.equals("Select")) {
                    mChequeLayout.setVisibility(View.GONE);
                } else if (statusVal.equals("Cash")) {
                    mChequeLayout.setVisibility(View.GONE);
                } else if (statusVal.equals("Bank Transfer")) {
                    mChequeLayout.setVisibility(View.GONE);
                } else if (statusVal.equals("Cheque")) {
                    mChequeLayout.setVisibility(View.VISIBLE);
                } else if (statusVal.equals("DD")) {
                    mChequeLayout.setVisibility(View.VISIBLE);
                }

                paymentMode = payment_mode.get(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void updateLabel() {
        String myFormat = "dd - MMM - yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.UK);
        exp_date.setText(sdf.format(mDateCalender.getTime()));

    }

    public void DisebleYESNO() {

        mGSTAppliSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                String statusVal = selectedItem;
                if (statusVal.equals("No")) {
                    sGST.setEnabled(false);
                    cGST.setEnabled(false);
                    iGST.setEnabled(false);


                    sGST.setText("SGST");
                    cGST.setText("CGST");
                    iGST.setText("IGST");
                    sGST.setBackground(getResources().getDrawable(R.drawable.rounded_corner_for_payments_grey));
                    cGST.setBackground(getResources().getDrawable(R.drawable.rounded_corner_for_payments_grey));
                    iGST.setBackground(getResources().getDrawable(R.drawable.rounded_corner_for_payments_grey));


                    sGSTamount.setEnabled(false);
                    cGSTamount.setEnabled(false);
                    iGSTamount.setEnabled(false);
                    sGSTamount.setText("");
                    cGSTamount.setText("");
                    iGSTamount.setText("");
                } else if (statusVal.equals("Yes")) {
                    sGST.setEnabled(true);
                    cGST.setEnabled(true);
                    iGST.setEnabled(true);
                    sGST.setText("");
                    cGST.setText("");
                    iGST.setText("");

                    sGST.setBackground(getResources().getDrawable(R.drawable.rounded_corner_for_payments));
                    cGST.setBackground(getResources().getDrawable(R.drawable.rounded_corner_for_payments));
                    iGST.setBackground(getResources().getDrawable(R.drawable.rounded_corner_for_payments));


                    sGSTamount.setEnabled(true);
                    cGSTamount.setEnabled(true);
                    iGSTamount.setEnabled(true);

                    cGSTamount.setText("0");
                    sGSTamount.setText("0");
                    iGSTamount.setText("0");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private boolean Validation() {

        boolean flag = true;
        if ((exp_billno.getText().length() <= 0)) {
            flag = false;
            exp_billno.setError((CharSequence) "Please enter the bill/voucher no.");
        }

        if ((exp_partyname.getText().length() <= 0)) {
            flag = false;
            exp_partyname.setError((CharSequence) "Please enter the party name");
        }

        if ((exp_sub_amount.getText().length() <= 0)) {
            flag = false;
            exp_sub_amount.setError((CharSequence) "Please enter the sub amount");
        }
        if ((mGSTAppliSpinner.getSelectedItem().toString().trim().equals("Select"))) {
            flag = false;
            Snackbar.make(mLinearLayout, getResources().getString(R.string.plz_select_GST_type), Snackbar.LENGTH_SHORT).show();
        }
        else if ((mPaymentModeSpinner.getSelectedItem().toString().trim().equals("Select"))) {
            flag = false;
            Snackbar.make(mLinearLayout, getResources().getString(R.string.plz_select_payment_method), Snackbar.LENGTH_SHORT).show();
        }
        return flag;
    }

    public void Calculation() {
        sGSTamount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sGST.getText().toString().isEmpty() || exp_sub_amount.getText().toString().isEmpty()) {

                    sGSTamount.setText("0");
                } else {
                    Long subamount = Long.parseLong(exp_sub_amount.getText().toString());
                    float sgst = Float.parseFloat(sGST.getText().toString());
                    float SGSTpercent = (subamount * sgst) / 100;
                    String sgstPer = Float.toString(SGSTpercent);
                    sGSTamount.setText(sgstPer);
                    sGSTamount.setTextColor(Color.BLACK);

                }
            }
        });

        cGSTamount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cGST.getText().toString().isEmpty() || exp_sub_amount.getText().toString().isEmpty()) {

                    cGSTamount.setText("0");
                } else {
                    Long subamount = Long.parseLong(exp_sub_amount.getText().toString());
                    float cgst = Float.parseFloat(cGST.getText().toString());
                    float CGSTpercent = (subamount * cgst) / 100;
                    String cgstPer = Float.toString(CGSTpercent);
                    cGSTamount.setText(cgstPer);
                    cGSTamount.setTextColor(Color.BLACK);
                }
            }
        });


        iGSTamount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iGST.getText().toString().isEmpty() || exp_sub_amount.getText().toString().isEmpty()) {

                    iGSTamount.setText("0");
                } else {

                    Long subamount = Long.parseLong(exp_sub_amount.getText().toString());
                    float igst = Float.parseFloat(iGST.getText().toString());
                    float IGSTpercent = (subamount * igst) / 100;
                    String igstPer = Float.toString(IGSTpercent);
                    iGSTamount.setText(igstPer);
                    iGSTamount.setTextColor(Color.BLACK);
                }
            }
        });
        exp_gtotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sGSTamount.getText().toString().isEmpty() ||
                        cGSTamount.getText().toString().isEmpty() ||
                        iGSTamount.getText().toString().isEmpty()) {
                    Long subamount = Long.parseLong(exp_sub_amount.getText().toString());
                    String grandTotal = Float.toString(subamount);
                    exp_gtotal.setText(grandTotal);
                }
                else  if (sGSTamount.getText().toString().isEmpty()) {
                    Long subamount = Long.parseLong(exp_sub_amount.getText().toString());
                    float cgstAmount = Float.parseFloat(cGSTamount.getText().toString());
                    float igstAmount = Float.parseFloat(iGSTamount.getText().toString());
                    float gtotal = subamount + cgstAmount + igstAmount;
                    String grandTotal = Float.toString(gtotal);
                    exp_gtotal.setText(grandTotal);
                }
                else if (cGSTamount.getText().toString().isEmpty()) {
                    Long subamount = Long.parseLong(exp_sub_amount.getText().toString());
                    float sgstAmount = Float.parseFloat(sGSTamount.getText().toString());
                    float igstAmount = Float.parseFloat(iGSTamount.getText().toString());
                    float gtotal = subamount + sgstAmount + igstAmount;
                    String grandTotal = Float.toString(gtotal);
                    exp_gtotal.setText(grandTotal);
                }
                else if (iGSTamount.getText().toString().isEmpty()) {
                    Long subamount = Long.parseLong(exp_sub_amount.getText().toString());
                    float cgstAmount = Float.parseFloat(cGSTamount.getText().toString());
                    float sgstAmount = Float.parseFloat(sGSTamount.getText().toString());
                    float gtotal = subamount + cgstAmount + sgstAmount;
                    String grandTotal = Float.toString(gtotal);
                    exp_gtotal.setText(grandTotal);
                }
                else if (sGSTamount.getText().toString().isEmpty() || cGSTamount.getText().toString().isEmpty()) {
                    Long subamount = Long.parseLong(exp_sub_amount.getText().toString());
                    float igstAmount = Float.parseFloat(iGSTamount.getText().toString());
                    float gtotal = subamount + igstAmount;
                    String grandTotal = Float.toString(gtotal);
                    exp_gtotal.setText(grandTotal);
                }
                else if (cGSTamount.getText().toString().isEmpty() || iGSTamount.getText().toString().isEmpty()) {
                    Long subamount = Long.parseLong(exp_sub_amount.getText().toString());
                    float sgstAmount = Float.parseFloat(cGSTamount.getText().toString());
                    float gtotal = subamount + sgstAmount;
                    String grandTotal = Float.toString(gtotal);
                    exp_gtotal.setText(grandTotal);
                }
                else if (sGSTamount.getText().toString().isEmpty() || iGSTamount.getText().toString().isEmpty()) {
                    Long subamount = Long.parseLong(exp_sub_amount.getText().toString());
                    float cgstAmount = Float.parseFloat(cGSTamount.getText().toString());
                    float gtotal = subamount + cgstAmount;
                    String grandTotal = Float.toString(gtotal);
                    exp_gtotal.setText(grandTotal);
                }

                else {
                    Long subamount = Long.parseLong(exp_sub_amount.getText().toString());
                    float sgstAmount = Float.parseFloat(sGSTamount.getText().toString());
                    float cgstAmount = Float.parseFloat(cGSTamount.getText().toString());
                    float igstAmount = Float.parseFloat(iGSTamount.getText().toString());
                    float gtotal = subamount + sgstAmount + cgstAmount + igstAmount;
                    String grandTotal = Float.toString(gtotal);
                    exp_gtotal.setText(grandTotal);
                }
            }
        });


    }

    public class PostHandler extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();
        String expDate, refNo, bilNo, partyName, partyGSTNo, subTotal,
                sgstPer, cgstPer, igstPer, sgstAmount, cgstAmount, igstAmount,
                pmode, paymentOption, grandTotal;

        public PostHandler(String expDate, String refNo, String bilNo,
                           String partyName, String partyGSTNo,
                           String subTotal, String sgstPer,
                           String cgstPer, String igstPer,
                           String sgstAmount, String cgstAmount,
                           String igstAmount, String pmode, String paymentOption,
                           String grandTotal) {
            this.expDate = expDate;
            this.refNo = refNo;
            this.bilNo = bilNo;
            this.partyName = partyName;
            this.partyGSTNo = partyGSTNo;
            this.subTotal = subTotal;
            this.sgstPer = sgstPer;
            this.cgstPer = cgstPer;
            this.igstPer = igstPer;
            this.sgstAmount = sgstAmount;
            this.cgstAmount = cgstAmount;
            this.igstAmount = igstAmount;
            this.pmode = pmode;
            this.paymentOption = paymentOption;
            this.grandTotal = grandTotal;
        }

        @Override
        protected String doInBackground(String... params) {
            RequestBody formBody = new FormBody.Builder()

                    .add("date", expDate)
                    .add("refno", refNo)
                    .add("bill", bilNo)
                    .add("name", partyName)
                    .add("gstno", partyGSTNo)
                    .add("total", subTotal)
                    .add("sgstper", sgstPer)
                    .add("cgstper", cgstPer)
                    .add("igstper", igstPer)
                    .add("sgstamt", sgstAmount)
                    .add("cgstamt", cgstAmount)
                    .add("igstamt", igstAmount)
                    .add("mode", pmode)
                    .add("dd_no", paymentOption)
                    .add("g_total", grandTotal)
                    .build();

            Request request = new Request.Builder()
                    .url(AddExpenseURL)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String myResponse = response.body().string();
                    Add_Expense_Activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject forecast = null;
                            try {
                                forecast = new JSONObject(myResponse);
                                String Message = forecast.getString("message");
                                exp_btnsave.setVisibility(View.VISIBLE);
                                dietProgress.setVisibility(View.GONE);
                                //reset();
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

        final Dialog dialog = new Dialog(Add_Expense_Activity.this);
        dialog.setContentView(R.layout.custom_alert_dialog_box);
        TextView mesg = dialog.findViewById(R.id.msg);
        mesg.setText(message);
        Button okbtn = dialog.findViewById(R.id.okbtn);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(Add_Expense_Activity.this, Expense_List_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                ActivityOptions options = ActivityOptions.makeCustomAnimation(Add_Expense_Activity.this,
                        R.anim.push_down_in, R.anim.push_down_out);
                startActivity(intent, options.toBundle());

            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();


    }

    private void calender() {
        String currentDate = new SimpleDateFormat("dd - MMM - yyyy", Locale.getDefault()).format(new Date());

        mDateCalender = Calendar.getInstance();
        exp_date.setText(currentDate);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                mDateCalender.set(Calendar.YEAR, year);
                mDateCalender.set(Calendar.MONTH, month);
                mDateCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
        exp_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Add_Expense_Activity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mDateCalender.set(Calendar.YEAR, year);
                        mDateCalender.set(Calendar.MONTH, month);
                        mDateCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateLabel();
                    }
                }, mDateCalender.get(Calendar.YEAR), mDateCalender.get(Calendar.MONTH), mDateCalender.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

}