package com.example.smile.activities;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.smile.R;
import com.example.smile.dataObjects.GroupNameModel;
import com.example.smile.dataObjects.MemberNameModel;
import com.example.smile.dataObjects.PackageModel;
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

public class Add_Sell_Product_Activity extends AppCompatActivity {
    RadioGroup mRadioGroup;
    RadioButton mIndividualBtn,mGroupBtn;
    Spinner mIndividualSpinner,mGoupSpinner,productNameSpinner,taxSpinner;
    ArrayList<String> individualName,groupName,productNameList,includeTaxList;
    String membername,productName,includeTax;
    ArrayList<ProductNameModel> mProductnameModel;
    ProgressBar progressBar;
    EditText mSellprice,mQantity,mDiscount,mTax,mReceivingAmount;
    TextView mSubTotal,mTaxAmount,mTotalAmount,
            mNetAmount, mTotalSellAMount,mRemainingAmount;
    LinearLayout mLinearlayout;
    Button mSaveButton;
    String Value = "";
    String Add_SellProduct_URL=BaseURL+"AddSellProduct";
    ArrayList<MemberNameModel> mMemberNameArr;
    ArrayList<GroupNameModel> mGroupNameArr;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sell__product);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setNavigationIcon(R.drawable.backbutton);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
            }
        });

        init();

        mProductnameModel = new ArrayList<>();
        productNameList = new ArrayList<String>();
        productNameList.add("Select");

        individualName = new ArrayList<String>();
        individualName.add("Select");

        groupName = new ArrayList<String>();
        groupName.add("Select");

        includeTaxList = new ArrayList<String>();
        includeTaxList.add("No");
        includeTaxList.add("Yes");

        ArrayAdapter<String> taxAdapter = new ArrayAdapter<>(Add_Sell_Product_Activity.this,
                android.R.layout.simple_list_item_1, includeTaxList);
        taxAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        taxSpinner.setAdapter(taxAdapter);

        mRadioGroup = findViewById(R.id.radioBtn);
        mIndividualBtn = findViewById(R.id.individualRadioButton);
        mGroupBtn = findViewById(R.id.groupRadioButton);
        mIndividualSpinner = findViewById(R.id.memberindividualspinner);
        mGoupSpinner = findViewById(R.id.memberGroupSpinner);
        productNameSpinner = findViewById(R.id.spinnerProductName);
        taxSpinner = findViewById(R.id.taxspinner);

        RadioButtonChange();
        getData();

        clickshandler();
        savebuttton();
        Calculations();



    }

    public void init(){
        mGroupNameArr = new ArrayList<GroupNameModel>();
        mMemberNameArr = new ArrayList<MemberNameModel>();


        mRadioGroup = findViewById(R.id.radioBtn);
        mIndividualBtn = findViewById(R.id.individualRadioButton);
        mGroupBtn = findViewById(R.id.groupRadioButton);
        mIndividualSpinner = findViewById(R.id.memberindividualspinner);
        mGoupSpinner = findViewById(R.id.memberGroupSpinner);
        productNameSpinner = findViewById(R.id.spinnerProductName);
        taxSpinner = findViewById(R.id.taxspinner);
        mTax = findViewById(R.id.taxPer);
        mTaxAmount = findViewById(R.id.taxAmount);
        mTotalAmount = findViewById(R.id.totalAmount);
        mSubTotal = findViewById(R.id.subTotal);
        mSellprice = findViewById(R.id.sellingPrice);
        mQantity = findViewById(R.id.quantity);
        mDiscount = findViewById(R.id.discount);
        mReceivingAmount = findViewById(R.id.receivingAmount);
        mSaveButton = findViewById(R.id.save);
        mLinearlayout = findViewById(R.id.linearLayout);
        mNetAmount = findViewById(R.id.netAmount);
        mTotalSellAMount = findViewById(R.id.totalSellAmount);
        mRemainingAmount = findViewById(R.id.remainingAmount);
        progressBar = findViewById(R.id.pb);

    }

    public void clickshandler(){
        taxSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                includeTax = includeTaxList.get(i).toString();
                if (taxSpinner.getSelectedItem().equals("Yes")) {
                    mTax.setBackground(getResources().getDrawable(R.drawable.rounded_corner_for_payments_grey));
                    mTaxAmount.setBackground(getResources().getDrawable(R.drawable.rounded_corner_for_payments_grey));
                    mTax.setEnabled(true);
/*
                    if (data!=null){
                        mTax.setText(data.getSelectTax());
                        mTaxAmount.setText(data.getTaxAmount());
                        mGrandTotal.setText(data.getGrandTotal());
                    } else {
                        mTax.setText("");
                        mTaxAmount.setText("");
                    }*/

                } else if (taxSpinner.getSelectedItem().equals("No")) {
                    mTax.setBackground(getResources().getDrawable(R.drawable.rounded_corner_for_payments));
                    mTaxAmount.setBackground(getResources().getDrawable(R.drawable.rounded_corner_for_payments));
                    mTax.setEnabled(false);
                    mTax.setText("0");
                    mTaxAmount.setText("0");

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        mIndividualSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
             //   membername = individualName.get(i).toString();
                if(mMemberNameArr != null && mMemberNameArr.size() > 0)
                    membername = mMemberNameArr.get(i).member_id.toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mGoupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
               //membername = groupName.get(i).toString();


                if(mGroupNameArr != null && mGroupNameArr.size() > 0)
                    membername = mGroupNameArr.get(i).groupId.toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        productNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                productName = productNameList.get(i).toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void savebuttton(){
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean networkConectivity = NetworkConnectivity.isConnected(Add_Sell_Product_Activity.this);
                if (networkConectivity) {
                    if (validation()) {
                        mSaveButton.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                        String memberType = Value;
                        String memberSpinner = membername;
                        String productid = productName;
                        String sellPrice = mSellprice.getText().toString();
                        String quantity = mQantity.getText().toString();
                        String totalAmt = mTotalAmount.getText().toString();
                        String discount = mDiscount.getText().toString();
                        String subtotal = mSubTotal.getText().toString();
                        String spinnertax = includeTax;
                        String taxper = mTax.getText().toString();
                        String tax_amount = mTaxAmount.getText().toString();
                        String netAmt = mNetAmount.getText().toString();
                        String totalSellAmt = mTotalSellAMount.getText().toString();
                        String receiveAmt = mReceivingAmount.getText().toString();
                        String remainingAmt = mRemainingAmount.getText().toString();


                        PostHandler handler = new PostHandler(memberType,memberSpinner,
                                productid, sellPrice, quantity, totalAmt, discount,
                                subtotal, spinnertax, taxper, tax_amount, netAmt, totalSellAmt,
                                receiveAmt, remainingAmt);

                        String result = null;

                        try {
                            result = handler.execute(Add_SellProduct_URL).get();

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                    }
                }else {
                    NetworkConnectivity.networkConnetivityShowDialog(Add_Sell_Product_Activity.this);

                }
            }

        });

    }

    public class PostHandler extends AsyncTask<String, String, String> {
        OkHttpClient client = new OkHttpClient();

        String type,memberName, productname, sellPrice, qty, totalAmt,
                discount, subTotal, incldeTax, taxPer, taxAmt, netAmt,
                totalSellAmt,receiveAmt,remainingAmt;

        public PostHandler(String type, String memberName,String productname,
                           String sellPrice, String qty, String totalAmt,
                           String discount, String subTotal, String incldeTax,
                           String taxPer, String taxAmt, String netAmt,
                           String totalSellAmt, String receiveAmt,
                           String remainingAmt) {
            this.type = type;
            this.memberName = memberName;
            this.productname = productname;
            this.sellPrice = sellPrice;
            this.qty = qty;
            this.totalAmt = totalAmt;
            this.discount = discount;
            this.subTotal = subTotal;
            this.incldeTax = incldeTax;
            this.taxPer = taxPer;
            this.taxAmt = taxAmt;
            this.netAmt = netAmt;
            this.totalSellAmt = totalSellAmt;
            this.receiveAmt = receiveAmt;
            this.remainingAmt = remainingAmt;
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody formBody = new FormBody.Builder()
                    .add("membertype", type)
                     .add("memberid", memberName)
                    .add("prdname", productname)
                    .add("sell_price", sellPrice)
                    .add("qty", qty)
                    .add("t_amount", totalAmt)
                    .add("discount", discount)
                    .add("sub_total", subTotal)
                    .add("include_tax", incldeTax)
                    .add("tax_count", taxPer)
                    .add("tax_amount", taxAmt)
                    .add("net_amount", netAmt)
                    .add("sell_amt", totalSellAmt)
                    .add("recv_amt", receiveAmt)
                    .add("rem_amt", remainingAmt)
                    .build();

            Request request = new Request.Builder()
                    .url(Add_SellProduct_URL)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String myResponse = response.body().string();
                    Add_Sell_Product_Activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject forecast = null;
                            try {
                                forecast = new JSONObject(myResponse);

                                String Message = forecast.getString("message");
                                mSaveButton.setVisibility(View.VISIBLE);
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

    public void Calculations(){

        mTotalAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalAmt();
            }
        });

        mSubTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subTotal();
            }
        });

        mTaxAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taxCalculation();
            }
        });

        mNetAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                netAmount();
            }
        });

        mRemainingAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remaningAmt();
            }
        });

    }

    private void totalAmt() {
        if (mSellprice.getText().toString().isEmpty() || mQantity.getText().toString().isEmpty()) {
            mSubTotal.setText("0");
        } else {
            long subtotal = (Long.parseLong(mSellprice.getText().toString())) * (Long.parseLong(mQantity.getText().toString()));
            String subtotall = Long.toString(subtotal);
            mTotalAmount.setText(subtotall);
        }
    }

    private void subTotal() {
        if (mTotalAmount.getText().toString().isEmpty() || mQantity.getText().toString().isEmpty()) {
            mSubTotal.setText("0");
        } else {
            long subtotal = (Long.parseLong(mTotalAmount.getText().toString())) - (Long.parseLong(mDiscount.getText().toString()));
            String subtotall = Long.toString(subtotal);
            mSubTotal.setText(subtotall);
        }
    }

    private void taxCalculation() {
        if (mTotalAmount.getText().toString().equals("0") || mTax.getText().toString().equals("0")
                || mTotalAmount.getText().toString().isEmpty() || mTax.getText().toString().isEmpty()) {
            mTaxAmount.setText("0");
        } else {

            Long subamount = Long.parseLong(mTotalAmount.getText().toString());
            float perTax = Float.parseFloat(mTax.getText().toString());
            float newTaxamt = (subamount * perTax) / 100;
            String nTax = Float.toString(newTaxamt);
            mTaxAmount.setText(nTax);
            mTaxAmount.setTextColor(Color.BLACK);


        }

    }

    private void netAmount(){
        if (mSubTotal.getText().toString().isEmpty() || mTaxAmount.getText().toString().isEmpty()) {
            mNetAmount.setText("0");
        } else {
            Float subtotal = (Long.parseLong(mSubTotal.getText().toString())) +
                    (Float.parseFloat(mTaxAmount.getText().toString()));
            String subtotall = Float.toString(subtotal);
            mNetAmount.setText(subtotall);
            mTotalSellAMount.setText(subtotall);
        }
    }

    private void remaningAmt(){
        if (mTotalSellAMount.getText().toString().isEmpty() || mReceivingAmount.getText().toString().isEmpty()) {
            mRemainingAmount.setText("0");
        } else {
            Float subtotal = (Float.parseFloat(mTotalSellAMount.getText().toString())) -
                    (Long.parseLong(mReceivingAmount.getText().toString()));
            String subtotall = Float.toString(subtotal);
            mRemainingAmount.setText(subtotall);

        }
    }

    public void showDialog(final String message) {

        final Dialog dialog = new Dialog(Add_Sell_Product_Activity.this);
        dialog.setContentView(R.layout.custom_alert_dialog_box);
        TextView mesg = dialog.findViewById(R.id.msg);
        mesg.setText(message);
        dialog.setCancelable(false);
        Button okbtn = dialog.findViewById(R.id.okbtn);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
               /* Intent intent = new Intent(Add_Sell_Product_Activity.this, Sell_Product_List_Activity.class);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(Add_Sell_Product_Activity.this, R.anim.push_down_in, R.anim.push_down_out);
                startActivity(intent, options.toBundle());*/
                finish();
                overridePendingTransition(R.anim.push_down_in,R.anim.push_down_out);


            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();


    }

    private boolean validation() {

        String sellPrice = mSellprice.getText().toString();
        String qty = mSellprice.getText().toString();
        String discount = mDiscount.getText().toString();
        String receiveAmt = mReceivingAmount.getText().toString();

        boolean flag = true;
        if (mIndividualSpinner.getSelectedItem().toString().trim().equals("Select")) {
            flag = false;
            Snackbar.make(mLinearlayout, "Please select member name or group name.", Snackbar.LENGTH_SHORT).show();
        }

        if (sellPrice.isEmpty()) {
            flag = false;
            mSellprice.setError("Please enter selling price.");
        }
        if (qty.isEmpty()) {
            flag = false;
            mQantity.setError("Please enter quantity.");
        }

        if (discount.isEmpty()) {
            flag = false;
            mDiscount.setError("Please enter discount.");
        }

        if (receiveAmt.isEmpty()) {
            flag = false;
            mReceivingAmount.setError("Please enter discount.");
        }

        return flag;

    }

    public void getData() {
        try {
            boolean networkConectivity = NetworkConnectivity.isConnected(Add_Sell_Product_Activity.this);
            if (networkConectivity) {
                //  ll_progresbar.setVisibility(View.VISIBLE);
                run(BaseURL + "AddMemberAttendance", "MemName");

                run(BaseURL + "SellProductGroupNameDD", "Packages");

                run(BaseURL + "ProductNameDD", "productName");
            } else {
                NetworkConnectivity.networkConnetivityShowDialog(Add_Sell_Product_Activity.this);
            }
        } catch (Exception ex) {
            Log.d("error", ex.toString());
            ex.printStackTrace();
        }
    }

    public void run(String url, final String name) {

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

                Add_Sell_Product_Activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        JSONObject forecast = null;
                        try {
                            forecast = new JSONObject(myResponse);
                            if (!name.isEmpty() && name.equals("MemName")) {
                                JSONArray jsonArray = forecast.getJSONArray("MemberName");
                                for (int i = 0; i < jsonArray.length(); i++) {


                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String name = object.getString("fullname");
                                    String id = object.getString("id");
                                    String member_id = object.getString("member_id");
                                    individualName.add(name);

                                    MemberNameModel nameModel = new MemberNameModel();
                                    nameModel.id= id;
                                    nameModel.fullname = name;
                                    nameModel.member_id = member_id;
                                    mMemberNameArr.add(nameModel);

                                }
                                MemberNameModel nameModel = new MemberNameModel();
                                nameModel.id = "0";
                                nameModel.member_id="0";
                                nameModel.fullname = "Select";
                                mMemberNameArr.add(0, nameModel);

                                ArrayAdapter<String> memberNameAdapater = new ArrayAdapter<String>(Add_Sell_Product_Activity.this,
                                        android.R.layout.simple_list_item_1, individualName);
                                memberNameAdapater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                mIndividualSpinner.setAdapter(memberNameAdapater);

                            } else if (!name.isEmpty() && name.equals("Packages")){
                                JSONArray jsonArray = forecast.getJSONArray("GroupName");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String groupname = object.getString("groupname");
                                    String groupId = object.getString("membergroups_id");
                                    groupName.add(groupname);

                                    GroupNameModel groupNameModel = new GroupNameModel();
                                    groupNameModel.groupId= groupId;
                                    groupNameModel.groupName = groupname;
                                    mGroupNameArr.add(groupNameModel);

                                }
                                GroupNameModel groupNameModel = new GroupNameModel();
                                groupNameModel.groupId= "0";
                                groupNameModel.groupName = "Select";
                                mGroupNameArr.add(0,groupNameModel);

                                ArrayAdapter<String> packageAdapater = new ArrayAdapter<String>(Add_Sell_Product_Activity.this,
                                        android.R.layout.simple_list_item_1, groupName);
                                packageAdapater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                mGoupSpinner.setAdapter(packageAdapater);
                            }
                            else if (!name.isEmpty() && name.equals("productName")){
                                JSONArray jsonArray = forecast.getJSONArray("ProNamDD");
                                for (int i = 0; i <jsonArray.length(); i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String productName = object.getString("prdname");
                                    String productId = object.getString("product_id");
                                    productNameList.add(productName);

                                    ProductNameModel productNameModel = new ProductNameModel();
                                    productNameModel.productId = productId;
                                    productNameModel.productName = productName;
                                    mProductnameModel.add(productNameModel);

                                }

                                ProductNameModel productNameModel = new ProductNameModel();
                                productNameModel.productName = "Select";
                                productNameModel.productId = "0";

                                mProductnameModel.add(0,productNameModel);
                                ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(Add_Sell_Product_Activity.this,
                                        android.R.layout.simple_list_item_1,productNameList);
                                yearAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
                                productNameSpinner.setAdapter(yearAdapter);

                            }

                        } catch (Exception e) {

                            e.printStackTrace();
                        }

                    }
                });

            }
        });
    }

    private void RadioButtonChange() {

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButtonClicked(checkedId);

                //Select the value
                RadioButton button = (RadioButton) findViewById(checkedId);
                if (button.isChecked()) {
                    Value = button.getText().toString();
                }
            }
        });


    }

    private void radioButtonClicked(int flag) {

        switch (flag) {
            case R.id.individualRadioButton:
                if (mIndividualBtn.isChecked()) {
                    mIndividualSpinner.setVisibility(View.VISIBLE);
                    mGoupSpinner.setVisibility(View.GONE);
                }
                break;
            case R.id.groupRadioButton:
                if (mGroupBtn.isChecked()) {
                    mIndividualSpinner.setVisibility(View.GONE);
                    mGoupSpinner.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }

    }




}
