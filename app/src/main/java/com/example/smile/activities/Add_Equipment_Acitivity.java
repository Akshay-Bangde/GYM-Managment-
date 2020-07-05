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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smile.R;
import com.example.smile.dataObjects.DietModel;
import com.example.smile.dataObjects.Equipment_Model;
import com.example.smile.dataObjects.MemberGroupListModel;
import com.example.smile.utils.NetworkConnectivity;
import com.example.smile.utils.Permissions;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

public class Add_Equipment_Acitivity extends AppCompatActivity {

    Button mSaveBtn;
    EditText mEquipmentName, mDetails, mPurchaseRate, mQuantity;
    TextView mNetAmount;
    private CircleImageView mImageView;
    private Integer REQUEST_CAMERA = 1, SELECT_FILE = 0;
    public String add_url = BaseURL + "Equipement";
    public String message;
    ProgressBar add_eqp_progressBar;
    TextView txt;
    String photos = "";
    Equipment_Model data = null;
    String Message;
    TextView heading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__equipment);

        init();

        Intent intent = getIntent();
        if(intent != null ){
            try {
                data = (Equipment_Model) intent.getSerializableExtra("packageData");
            }catch (Exception e){
                Log.d("exception", e.toString());
            }
        }
        //for toolbar
        add_eqp_progressBar=findViewById(R.id.pb);
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

                if (data != null) {

                    mEquipmentName.setText(data.geteName());
                    mDetails.setText(data.geteDescription());
                    mPurchaseRate.setText(data.geteRate());
                    mQuantity.setText(data.geteQty());
                    mNetAmount.setText(data.geteNetamt());
                    heading.setText("Edit Equipment");

                }



        mNetAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPurchaseRate.getText().toString().isEmpty() || mQuantity.getText().toString().isEmpty()) {

                    mNetAmount.setText("0");
                } else {
                    rate_into_quantity();
                }
            }
        });

        //for camera and to select image from camera and gallery
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

        Savebutton();


        //on click listener on save button


    }

    private void SelectImage() {
        boolean checkPermissions = Permissions.checkPermission(Add_Equipment_Acitivity.this);
        if (checkPermissions) {
            final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(Add_Equipment_Acitivity.this);
            alertDialog.setTitle("Add Photo !");
            alertDialog.setItems(items, new DialogInterface.OnClickListener() {
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
            alertDialog.show();
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
                mImageView.setImageURI(SelectedImageUri);
            }
        } else if (requestCode == RESULT_CANCELED) {
            Toast.makeText(this, getResources().getString(R.string.picture_was_not_taken), Toast.LENGTH_SHORT).show();
        }
    }

    public void getBase64Image(Bitmap bitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArrayImage = baos.toByteArray();
        photos = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);
    }

    private void rate_into_quantity() {
        Long total = (Long.parseLong(mQuantity.getText().toString())) * (Long.parseLong(mPurchaseRate.getText().toString()));
        String totall = Long.toString(total);

        mNetAmount.setText(totall);

    }

    public void Savebutton(){
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean networkConectivity = NetworkConnectivity.isConnected(Add_Equipment_Acitivity.this);
                if (networkConectivity) {
                    if (validation()) {
                        mSaveBtn.setVisibility(View.GONE);
                        add_eqp_progressBar.setVisibility(View.VISIBLE);
                        if (data != null) {
                            String id = data.getId();
                            String equipname = mEquipmentName.getText().toString();
                            String details = mDetails.getText().toString();
                            String rate = mPurchaseRate.getText().toString();
                            String qty = mQuantity.getText().toString();
                            String amount = mNetAmount.getText().toString();

                            EditHandler handler = new EditHandler(id, equipname, details, rate, qty, amount);
                            String result = null;
                            try {
                                result = handler.execute(add_url).get();

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        } else {
                            String equipname = mEquipmentName.getText().toString();
                            String details = mDetails.getText().toString();
                            String rate = mPurchaseRate.getText().toString();
                            String qty = mQuantity.getText().toString();
                            String amount = mNetAmount.getText().toString();

                            PostHandler handler = new PostHandler(equipname, details, rate, qty, amount);
                            String result = null;
                            try {
                                result = handler.execute(add_url).get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    else {
                        NetworkConnectivity.networkConnetivityShowDialog(Add_Equipment_Acitivity.this);
                    }
                }
            }

        });
    }

    //for initialing all widgets
    private void init() {
        mImageView = findViewById(R.id.profile_image);
        mEquipmentName = findViewById(R.id.equipment_name);
        mDetails = findViewById(R.id.details);
        mPurchaseRate = findViewById(R.id.purchaseRate);
        mQuantity = findViewById(R.id.Quantity);
        mSaveBtn = findViewById(R.id.save);
        mNetAmount = findViewById(R.id.equipment_net_amount);
        txt = findViewById(R.id.equipmentname);
        heading = findViewById(R.id.heading);

    }

    //for validations
    public Boolean validation() {
        String activityname = mEquipmentName.getText().toString();
        //       String details = mDetails.getText().toString();
        String purchase_rate = mPurchaseRate.getText().toString();
        String quantity = mQuantity.getText().toString();
        boolean flag = true;
        if (activityname.isEmpty()) {
            flag = false;
            mEquipmentName.setError(getResources().getString(R.string.plz_enter_the_equipment_name));
        }
       /* if ( details.isEmpty() )
        {
            flag = false;
             mDetails.setError(getResources().getString(R.string.plz_enter_details));
        }*/
        if (purchase_rate.isEmpty()) {
            flag = false;
            mPurchaseRate.setError(getResources().getString(R.string.plz_enter_the_purchase_rate));
        }
        if (quantity.isEmpty()) {
            flag = false;
            mQuantity.setError(getResources().getString(R.string.plz_enter_the_quantity));
        }
        return flag;
    }


    public class EditHandler extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();
        String id,equipname, details, rate, qty, amount;


        public EditHandler(String id, String equipname, String details, String rate, String qty, String amount) {
            this.id = id;
            this.equipname = equipname;
            this.details = details;
            this.rate = rate;
            this.qty = qty;
            this.amount = amount;
        }

        @Override
        protected String doInBackground(String... params) {
            RequestBody formBody = new FormBody.Builder()
                    .add("id", id)
                    .add("equipname", equipname)
                    .add("details", details)
                    .add("rate", rate)
                    .add("qty", qty)
                    .add("amount", amount)
                    .add("photos", photos)
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
                    Add_Equipment_Acitivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject forecast = null;
                            try {
                                forecast = new JSONObject(myResponse);
                                Message = forecast.getString("message");
                                mSaveBtn.setVisibility(View.VISIBLE);
                                add_eqp_progressBar.setVisibility(View.GONE);
                                reset();
                                showDialog(Message);
                                heading.setText("Add Equipment");
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
        String equipname, details, rate, quantity, amount;

        public PostHandler(String equipname, String details, String rate, String quantity, String amount) {

            this.equipname = equipname;
            this.details = details;
            this.rate = rate;
            this.quantity = quantity;
            this.amount = amount;
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody formBody = new FormBody.Builder()
                    .add("equipname", equipname)
                    .add("details", details)
                    .add("rate", rate)
                    .add("qty", quantity)
                    .add("amount", amount)
                     .add("photos", photos)
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
                    Add_Equipment_Acitivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject forecast = null;
                            try {
                                forecast = new JSONObject(myResponse);
                                message = forecast.getString("message");
                                mSaveBtn.setVisibility(View.VISIBLE);
                                add_eqp_progressBar.setVisibility(View.GONE);
                                reset();
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

            final Dialog dialog = new Dialog(Add_Equipment_Acitivity.this);
            dialog.setContentView(R.layout.custom_alert_dialog_box);
            TextView mesg = dialog.findViewById(R.id.msg);
            mesg.setText(message);
            Button okbtn = dialog.findViewById(R.id.okbtn);
            okbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                   /* Intent intent = new Intent(Add_Equipment_Acitivity.this, Gym_Equipment_Activity.class);
                    ActivityOptions options = ActivityOptions.makeCustomAnimation(Add_Equipment_Acitivity.this, R.anim.push_down_in, R.anim.push_down_out);
                    startActivity(intent, options.toBundle());*/
                    finish();
                    overridePendingTransition(R.anim.push_down_in,R.anim.push_down_out);


                }
            });

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.show();


        }

        private void reset() {
            mEquipmentName.setText("");
            mDetails.setText("");
            mPurchaseRate.setText("");
            mQuantity.setText("");
            mNetAmount.setText("");
        }

    }




