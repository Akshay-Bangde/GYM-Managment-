package com.example.smile.activities;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.smile.R;
import com.example.smile.dataObjects.StaffListModel;
import com.example.smile.dataObjects.SuppliersListModel;
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

public class Add_Supplier_Activity extends AppCompatActivity {

    private TextInputEditText mSupplierName, mContactNo, mSupplierEmail, mStatecode,mCity,mGstNo;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Button mSave;

    private LinearLayout mLinearLayout;

    final String AddStaff_URL = BaseURL+"Supplier";
    SuppliersListModel data = null;
    ProgressBar dietProgress;
    TextView toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__supplier);
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
        Intent intent = getIntent();
        if (intent != null) {
            try {
                data = (SuppliersListModel) intent.getSerializableExtra("packageData");

            } catch (Exception e) {
                Log.d("exception", e.toString());
            }
        }


        if (data != null) {
            mSupplierName.setText(data.getSupplierName());
            mContactNo.setText(data.getPhone());
            mSupplierEmail.setText(data.getEmail());
            mStatecode.setText(data.getStateCode());
            mCity.setText(data.getCity());
            if (data.getGstNo() != null){
                mGstNo.setText(data.getGstNo());
            }else {
                mGstNo.setText("");
            }
            toolbar_title.setText("Edit Supplier");

            }

        dietProgress=findViewById(R.id.pb);
        mSave = findViewById(R.id.save);
        SaveButton();





    }

    public void SaveButton(){
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean networkConectivity = NetworkConnectivity.isConnected(Add_Supplier_Activity.this);
                if (networkConectivity) {
                    if (validation()) {
                        mSave.setVisibility(View.GONE);
                        dietProgress.setVisibility(View.VISIBLE);

                        if (data != null) {
                            String id = data.getId();
                            String name = mSupplierName.getText().toString();
                            String phone = mContactNo.getText().toString();
                            String email = mSupplierEmail.getText().toString();
                            String stateCode = mStatecode.getText().toString();
                            String city = mCity.getText().toString();
                            String gstNo = mGstNo.getText().toString();
                            EditHandler handler = new EditHandler(id,name, phone, email,
                                    stateCode, city, gstNo);
                            String result = null;
                            try {
                                result = handler.execute(AddStaff_URL).get();

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }

                        }
                        else {
                            String name = mSupplierName.getText().toString();
                            String phone = mContactNo.getText().toString();
                            String email = mSupplierEmail.getText().toString();
                            String stateCode = mStatecode.getText().toString();
                            String city = mCity.getText().toString();
                            String gstNo = mGstNo.getText().toString();
                            PostHandler handler = new PostHandler(name, phone, email,
                                    stateCode, city, gstNo);

                            String result = null;
                            try {
                                result = handler.execute(AddStaff_URL).get();

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                else {
                    NetworkConnectivity.networkConnetivityShowDialog(Add_Supplier_Activity.this);

                }
            }
        });
    }

    public void init(){

        mSupplierName = findViewById(R.id.suppliers_name);
        mContactNo = findViewById(R.id.contact_no);
        mSupplierEmail = findViewById(R.id.suppliers_email);
        mStatecode = findViewById(R.id.state_code);
        mCity = findViewById(R.id.city);
        mGstNo = findViewById(R.id.gstNo);
        mLinearLayout = findViewById(R.id.linear_layout);
        toolbar_title = findViewById(R.id.toolbar_title);
    }

    public class EditHandler extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();
        String id,suppliersName,phone, email, stateCode, city, gstNo;

        public EditHandler(String id, String suppliersName, String phone, String email, String stateCode, String city, String gstNo) {
            this.id = id;
            this.suppliersName = suppliersName;
            this.phone = phone;
            this.email = email;
            this.stateCode = stateCode;
            this.city = city;
            this.gstNo = gstNo;
        }

        @Override
        protected String doInBackground(String... params) {
            RequestBody formBody = new FormBody.Builder()
                    .add("id", id)
                    .add("name", suppliersName)
                    .add("contact", phone)
                    .add("email", email)
                    .add("statecode", stateCode)
                    .add("city", city)
                    .add("gstin", gstNo)
                    .build();

            Request request = new Request.Builder()
                    .url(AddStaff_URL)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String myResponse = response.body().string();
                    Add_Supplier_Activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject forecast = null;
                            try {
                                forecast = new JSONObject(myResponse);

                                String Message = forecast.getString("message");
                                mSave.setVisibility(View.VISIBLE);
                                dietProgress.setVisibility(View.GONE);
                                reset();
                                showDialog(Message);
                                toolbar_title.setText("Add Supplier");

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

    public class PostHandler extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();
        String suppliersName,phone, email, stateCode, city, gstNo;

        public PostHandler(String suppliersName, String phone, String email,
                           String stateCode, String city, String gstNo) {
            this.suppliersName = suppliersName;
            this.phone = phone;
            this.email = email;
            this.stateCode = stateCode;
            this.city = city;
            this.gstNo = gstNo;
        }

        @Override
        protected String doInBackground(String... params) {
            RequestBody formBody = new FormBody.Builder()
                    .add("name", suppliersName)
                    .add("contact", phone)
                    .add("email", email)
                    .add("statecode", stateCode)
                    .add("city", city)
                    .add("gstin", gstNo)
                    .build();

            Request request = new Request.Builder()
                    .url(AddStaff_URL)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String myResponse = response.body().string();
                    Add_Supplier_Activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject forecast = null;
                            try {
                                forecast = new JSONObject(myResponse);

                                String Message = forecast.getString("message");
                                mSave.setVisibility(View.VISIBLE);
                                dietProgress.setVisibility(View.GONE);
                                reset();
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

        final Dialog dialog = new Dialog(Add_Supplier_Activity.this);
        dialog.setContentView(R.layout.custom_alert_dialog_box);
        TextView mesg = dialog.findViewById(R.id.msg);
        mesg.setText(message);
        Button okbtn = dialog.findViewById(R.id.okbtn);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
               /* Intent intent = new Intent(Add_Supplier_Activity.this, Suppliers_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                ActivityOptions options = ActivityOptions.makeCustomAnimation(Add_Supplier_Activity.this,
                        R.anim.push_down_in, R.anim.push_down_out);
                startActivity(intent, options.toBundle());
*/
               finish();
                overridePendingTransition(R.anim.push_down_in,R.anim.push_down_out);


            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();


    }
    private void reset() {
        mSupplierName.setText("");
        mContactNo.setText("");
        mSupplierEmail.setText("");
        mStatecode.setText("");
        mCity.setText("");
        mGstNo.setText("");

    }

    private boolean validation() {
        String supplier_name = mSupplierName.getText().toString();
        String contact_no = mContactNo.getText().toString();
        String supplier_email = mSupplierEmail.getText().toString();
        String state_code = mStatecode.getText().toString();

        boolean flag = true;

        if (supplier_name.isEmpty()) {
            flag = false;
            mSupplierName.setError(getResources().getString(R.string.plz_enter_the_supplier_name));
        }
        if (contact_no.isEmpty()) {
            flag = false;
            mContactNo.setError(getResources().getString(R.string.plz_enter_contact_no));
        }
        if (state_code.isEmpty()) {
            flag = false;
            mStatecode.setError(getResources().getString(R.string.plz_enter_the_state_code));
        }
        else if (contact_no.length() < 10) {
            flag = false;
            Snackbar.make(mLinearLayout, getResources().getString(R.string.plz_enter_valid_contact_no), Snackbar.LENGTH_SHORT).show();
        }
        else if (supplier_email.length() > 0 && !supplier_email.matches(emailPattern)) {
            flag = false;
            Snackbar.make(mLinearLayout, getResources().getString(R.string.valid_error_valid_email), Snackbar.LENGTH_SHORT).show();
        }
        return flag;
    }
}

