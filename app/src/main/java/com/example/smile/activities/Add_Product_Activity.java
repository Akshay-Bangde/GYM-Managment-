package com.example.smile.activities;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
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
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smile.R;
import com.example.smile.dataObjects.Product_List_Model;
import com.example.smile.utils.NetworkConnectivity;
import com.example.smile.utils.Permissions;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
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

public class Add_Product_Activity extends AppCompatActivity {


    ImageView mImageView;
    private Integer REQUEST_CAMERA = 1, SELECT_FILE = 0;
    EditText mPOnumber, mProductName, mRateQty, mQuantity, mTax, mDescription;
    Spinner mSuppliername, mincludetax;
    Button mSave;
    LinearLayout mLinearlayout;
    ArrayList<String> supplierArrayList, taxArrayList;
    TextView mSubTotal, mGrandTotal, mTaxAmount, mtitle;
    String proSpinnerUrl = BaseURL + "AddProduct";
    String add_product_url = BaseURL + "AddProduct";
    String Message;
    String supplierName, includeTax;
    String photos = null;
    Product_List_Model data = null;
RelativeLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__product);
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
        run(proSpinnerUrl);


        mSuppliername.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                supplierName = supplierArrayList.get(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mincludetax.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                includeTax = taxArrayList.get(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        Intent intent = getIntent();
        if (intent != null) {

            try {
                data = (Product_List_Model) intent.getSerializableExtra("packageData");

            } catch (Exception e) {
                Log.d("exception", e.toString());
            }
        }
        if (data != null) {

            mProductName.setText(data.getProname());
            mPOnumber.setText(data.getPpNo());
            if (data.getDescription() != null) {
                mDescription.setText(data.getDescription());
            } else {
                mDescription.setText("");
            }
            mSubTotal.setText(data.getProSubtotal());
            mRateQty.setText(data.getUnitRate());
            mQuantity.setText(data.getProQty());
            mTax.setText(data.getTaxPercent());
            mTaxAmount.setText(data.getTaxAmount());
            if (data.getGrandTotal() != null) {
                mGrandTotal.setText(data.getGrandTotal());
            } else  {
                mGrandTotal.setText("");
            }
            mImageView.setVisibility(View.GONE);
            mtitle.setText(R.string.reorder);
            mSave.setText(R.string.reorder);
        }


        mSubTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setmSubTotal();
            }
        });

        mTaxAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taxCalculation();
            }
        });

        mGrandTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mTax.getText().toString().isEmpty() ||
                        mRateQty.getText().toString().isEmpty() ||
                        mQuantity.getText().toString().isEmpty()) {
                    String subbtotal = mSubTotal.getText().toString();
                    mGrandTotal.setText(subbtotal);
                } else {
                    Long subbtotal = Long.parseLong(mSubTotal.getText().toString());
                    float taxAmount = Float.parseFloat(mTaxAmount.getText().toString());
                    float grandAddition = subbtotal + taxAmount;
                    String sgstPer = Float.toString(grandAddition);
                    mGrandTotal.setText(sgstPer);
                    mGrandTotal.setTextColor(Color.BLACK);

                }


            }
        });

        mincludetax.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                includeTax = taxArrayList.get(i).toString();
                if (mincludetax.getSelectedItem().equals("Yes")) {
                    mTax.setBackground(getResources().getDrawable(R.drawable.rounded_corner_for_payments_grey));
                    mTaxAmount.setBackground(getResources().getDrawable(R.drawable.rounded_corner_for_payments_grey));
                    mTax.setEnabled(true);

                    if (data!=null){
                        mTax.setText(data.getSelectTax());
                        mTaxAmount.setText(data.getTaxAmount());
                        mGrandTotal.setText(data.getGrandTotal());
                    } else {
                        mTax.setText("");
                        mTaxAmount.setText("");
                    }


                } else if (mincludetax.getSelectedItem().equals("No")) {
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

        taxArrayList = new ArrayList<>();
        taxArrayList.add("No");
        taxArrayList.add("Yes");
        ArrayAdapter<String> taxAdapter = new ArrayAdapter<String>(Add_Product_Activity.this,
                android.R.layout.simple_list_item_1, taxArrayList);
        taxAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        mincludetax.setAdapter(taxAdapter);

        if (data != null) {
            setTaxData();
        }

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        SaveButton();


    }

    public void setSupplierData(){
        String suppliername = data.getsName();
        int pos = 0;
        if(supplierArrayList != null && supplierArrayList.size() > 0) {
            for (int i = 0; i < supplierArrayList.size(); i++) {
                if (suppliername.equals(supplierArrayList.get(i))) {
                    pos = i;
                }
            }
        }
        mSuppliername.setSelection(pos);
    }


    public void setTaxData(){
        String tax = data.getSelectTax();
        int pos = 0;
        if(taxArrayList != null && taxArrayList.size() > 0) {
            for (int i = 0; i < taxArrayList.size(); i++) {
                if (tax.equals(taxArrayList.get(i))) {
                    pos = i;
                }
            }
        }
        mincludetax.setSelection(pos);
    }

    //all fields initialized
    private void init() {
        relativeLayout = findViewById(R.id.relativeLayout);
        mImageView = findViewById(R.id.profile_image);
        mtitle = findViewById(R.id.productTitle);
        mSave = findViewById(R.id.save);
        mPOnumber = findViewById(R.id.poNumber);
        mProductName = findViewById(R.id.product_name);
        mSuppliername = findViewById(R.id.supplier_name);
        mRateQty = findViewById(R.id.Rate);
        mQuantity = findViewById(R.id.Quantity);
        mincludetax = findViewById(R.id.taxspinner);
        mLinearlayout = findViewById(R.id.productLayout);
        mTax = findViewById(R.id.Tax);
        mDescription = findViewById(R.id.pro_description);
        mSubTotal = findViewById(R.id.subTotal);
        mGrandTotal = findViewById(R.id.grand_total);
        mTaxAmount = findViewById(R.id.taxAmount);

        supplierArrayList = new ArrayList<String>();
        supplierArrayList.add("Select");

    }

    public void SaveButton() {
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean networkConectivity = NetworkConnectivity.isConnected(Add_Product_Activity.this);
                if (networkConectivity) {
                    if (validation()) {
                        if (data != null) {
                            String id = data.getId();
                            String pono = mPOnumber.getText().toString();
                            String prdname = mProductName.getText().toString();
                            String supliername = supplierName;
                            String details = mDescription.getText().toString();
                            String rate = mRateQty.getText().toString();
                            String qty = mQuantity.getText().toString();
                            String sub_total = mSubTotal.getText().toString();
                            String spinnertax = includeTax;
                            String taxper = mTax.getText().toString();
                            String tax_amount = mTaxAmount.getText().toString();
                            String grandTotal = mGrandTotal.getText().toString();

                            EditHandler handler = new EditHandler(id, prdname, pono, details, supliername,
                                    rate, qty, sub_total, spinnertax, taxper, tax_amount,grandTotal);

                            String result = null;
                            try {
                                result = handler.execute(add_product_url).get();

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        } else {
                            String pono = mPOnumber.getText().toString();
                            String prdname = mProductName.getText().toString();
                            String supliername = supplierName;
                            String details = mDescription.getText().toString();
                            String rate = mRateQty.getText().toString();
                            String qty = mQuantity.getText().toString();
                            String sub_total = mSubTotal.getText().toString();
                            String spinnertax = includeTax;
                            String taxper = mTax.getText().toString();
                            String tax_amount = mTaxAmount.getText().toString();
                            String grandTotal = mGrandTotal.getText().toString();

                            PostHandler handler = new PostHandler(prdname, pono, supliername,
                                    details, rate, qty, sub_total, spinnertax, taxper, tax_amount,grandTotal);

                            String result = null;

                            try {
                                result = handler.execute(add_product_url).get();

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                } else {
                    NetworkConnectivity.networkConnetivityShowDialog(Add_Product_Activity.this);

                }
            }


        });

    }

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

                Add_Product_Activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            JSONObject forecast = new JSONObject(myResponse);

                            JSONArray jsonArray = forecast.getJSONArray("Supiler");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject supplierObject = jsonArray.getJSONObject(i);

                                String sName = supplierObject.getString("name");
                                supplierArrayList.add(sName);
                            }

                            ArrayAdapter<String> supplierAdapter = new ArrayAdapter<String>(Add_Product_Activity.this,
                                    android.R.layout.simple_list_item_1, supplierArrayList);
                            supplierAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                            mSuppliername.setAdapter(supplierAdapter);


                            if (data != null) {
                                setSupplierData();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });

            }
        });

    }

    public class EditHandler extends AsyncTask<String, String, String> {
        OkHttpClient client = new OkHttpClient();

        String id,prdname, pono, supliername, details, rate, qty,
                sub_total, tax, taxper, tax_amount, grandTotal;

        public EditHandler(String id, String prdname, String pono,
                           String supliername, String details,
                           String rate, String qty, String sub_total,
                           String tax, String taxper, String tax_amount,
                           String grandTotal) {
            this.id = id;
            this.prdname = prdname;
            this.pono = pono;
            this.supliername = supliername;
            this.details = details;
            this.rate = rate;
            this.qty = qty;
            this.sub_total = sub_total;
            this.tax = tax;
            this.taxper = taxper;
            this.tax_amount = tax_amount;
            this.grandTotal = grandTotal;
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody formBody = new FormBody.Builder()
                    .add("id", id)
                    .add("uploadphoto", photos)
                    .add("prdname", prdname)
                    .add("pono", pono)
                    .add("supliername", supliername)
                    .add("details", details)
                    .add("rate", rate)
                    .add("qty", qty)
                    .add("sub_total", sub_total)
                    .add("tax", tax)
                    .add("taxper", taxper)
                    .add("tax_amount", tax_amount)
                    .add("total", grandTotal)

                    .build();

            Request request = new Request.Builder()
                    .url(add_product_url)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String myResponse = response.body().string();
                    Add_Product_Activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject forecast = null;
                            try {
                                forecast = new JSONObject(myResponse);

                                Message = forecast.getString("message");

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

    public class PostHandler extends AsyncTask<String, String, String> {
        OkHttpClient client = new OkHttpClient();

        String prdname, pono, supliername, details, rate, qty,
                sub_total, tax, taxper, tax_amount, grandTotal;

        public PostHandler(String prdname, String pono, String supliername,
                           String details, String rate, String qty,
                           String sub_total, String tax, String taxper,
                           String tax_amount, String grandTotal) {
            this.prdname = prdname;
            this.pono = pono;
            this.supliername = supliername;
            this.details = details;
            this.rate = rate;
            this.qty = qty;
            this.sub_total = sub_total;
            this.tax = tax;
            this.taxper = taxper;
            this.tax_amount = tax_amount;
            this.grandTotal = grandTotal;
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody formBody = new FormBody.Builder()
                    .add("uploadphoto", photos)
                    .add("prdname", prdname)
                    .add("pono", pono)
                    .add("supliername", supliername)
                    .add("details", details)
                    .add("rate", rate)
                    .add("qty", qty)
                    .add("sub_total", sub_total)
                    .add("tax", tax)
                    .add("taxper", taxper)
                    .add("tax_amount", tax_amount)
                    .add("total", grandTotal)

                    .build();

            Request request = new Request.Builder()
                    .url(add_product_url)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String myResponse = response.body().string();
                    Add_Product_Activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject forecast = null;
                            try {
                                forecast = new JSONObject(myResponse);

                                Message = forecast.getString("message");

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

    //calculation for subtotal amount
    private void setmSubTotal() {

        if (mRateQty.getText().toString().isEmpty() || mQuantity.getText().toString().isEmpty()) {
            mSubTotal.setText("0");
        } else {

            long subtotal = (Long.parseLong(mRateQty.getText().toString())) * (Long.parseLong(mQuantity.getText().toString()));
            String subtotall = Long.toString(subtotal);
            mSubTotal.setText(subtotall);
        }
    }

    private void taxCalculation() {
        if (mSubTotal.getText().toString().equals("0") || mTax.getText().toString().equals("0")
                || mSubTotal.getText().toString().isEmpty() || mTax.getText().toString().isEmpty()) {
            mTaxAmount.setText("0");
        } else {

            Long subamount = Long.parseLong(mSubTotal.getText().toString());
            float perTax = Float.parseFloat(mTax.getText().toString());
            float newTaxamt = (subamount * perTax) / 100;
            String nTax = Float.toString(newTaxamt);
            mTaxAmount.setText(nTax);
            mTaxAmount.setTextColor(Color.BLACK);


        }

    }

    //for selection of image with camera or gallery
    private void SelectImage() {

        boolean checkPermissions = Permissions.checkPermission(Add_Product_Activity.this);
        if (checkPermissions) {

            final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(Add_Product_Activity.this);
            builder.setTitle("Add Photo !");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    if (items[i].equals("Camera")) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, REQUEST_CAMERA);

                    } else if (items[i].equals("Gallery")) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);
                    } else if (items[i].equals("Cancel")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bundle bundle = data.getExtras();
                final Bitmap bitmap = (Bitmap) bundle.get("data");

                getBase64Image(bitmap);

                mImageView.setImageBitmap(bitmap);
            } else if (requestCode == SELECT_FILE) {
                Uri SelectedImageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), SelectedImageUri);
                    getBase64Image(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                mImageView.setImageURI(SelectedImageUri);
            }
        } else if (requestCode == RESULT_CANCELED) {
            Toast.makeText(this, getResources().getString(R.string.picture_was_not_taken), Toast.LENGTH_SHORT).show();
        }
    }

    public void getBase64Image(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArrayImage = baos.toByteArray();
        photos = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
    }

    public void showDialog(final String message) {

        final Dialog dialog = new Dialog(Add_Product_Activity.this);
        dialog.setContentView(R.layout.custom_alert_dialog_box);
        TextView mesg = dialog.findViewById(R.id.msg);
        mesg.setText(message);
        Button okbtn = dialog.findViewById(R.id.okbtn);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
               /* Intent intent = new Intent(Add_Product_Activity.this, Product_List_Activity.class);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(Add_Product_Activity.this, R.anim.push_down_in, R.anim.push_down_out);
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

        String poNumber = mPOnumber.getText().toString();
        String productName = mProductName.getText().toString();
        String rateQty = mRateQty.getText().toString();
        String quantity = mQuantity.getText().toString();

        boolean flag = true;
        if (poNumber.isEmpty()) {
            flag = false;
            mPOnumber.setError(getResources().getString(R.string.plz_enter_the_ponum));
        }
        if (productName.isEmpty()) {
            flag = false;
            mProductName.setError(getResources().getString(R.string.plz_enter_the_productname));
        }
        if (rateQty.isEmpty()) {
            flag = false;
            mRateQty.setError(getResources().getString(R.string.plz_enter_the_rate));
        }
        if (quantity.isEmpty()) {
            flag = false;
            mQuantity.setError(getResources().getString(R.string.plz_enter_the_quantity));
        }
        if (mSuppliername.getSelectedItem().toString().trim().equals("Select")) {
            flag = false;
            Snackbar.make(relativeLayout, "Please select supplier name.", Snackbar.LENGTH_SHORT).show();
        }

        return flag;


    }
}