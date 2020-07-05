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
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
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
import android.widget.Toast;

import com.example.smile.R;
import com.example.smile.dataObjects.DietPlanModel;
import com.example.smile.dataObjects.FoodCategoryListModel;
import com.example.smile.dataObjects.FoodCategoryModel;
import com.example.smile.dataObjects.FoodEntryListModel;
import com.example.smile.dataObjects.PackageModel;
import com.example.smile.dataObjects.TrainerModel;
import com.example.smile.dataObjects.WaterIntakeListModel;
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

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.smile.activities.SignInActivity.BaseURL;

public class AddFoodEntry_Activity extends AppCompatActivity {
    CircleImageView mImageView;
    private Integer REQUEST_CAMERA = 1, SELECT_FILE = 0;
    String photos = "";
    EditText mFoodName, mUOM, mCalorie, mDescription;
    Spinner SelctfoodSpinner;
    ArrayList<String> mfieldSelectfoodSpin;
    String SelectFoodCategory;
    Button SaveBtn;
    LinearLayout mLinearLayout;
    public String Add_Food_Url =BaseURL+"FoodEntryPost";
    String FoodCategory_URL = BaseURL+"CategoryDDown";
    FoodEntryListModel data =null;
    private ArrayList<FoodCategoryModel> mFoodCategoryArr;
    String foodnameCategoryId;

    TextView heading;
    ProgressBar dietProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_entry);
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
        heading = findViewById(R.id.toolbar_title);
        dietProgress=findViewById(R.id.pb);
        mFoodName = findViewById(R.id.foodName);
        mUOM = findViewById(R.id.Uom);
        mCalorie = findViewById(R.id.calorie);
        mDescription = findViewById(R.id.description);
        mImageView = findViewById(R.id.profile_image);
        SelctfoodSpinner = findViewById(R.id.selected_food_spinner);

        mFoodCategoryArr = new ArrayList<>();

        mfieldSelectfoodSpin = new ArrayList<String>();
        mfieldSelectfoodSpin.add("Select");
        ArrayAdapter<String> packageAdapter = new ArrayAdapter<String>(AddFoodEntry_Activity.this,
                android.R.layout.simple_list_item_1, mfieldSelectfoodSpin);
        packageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SelctfoodSpinner.setAdapter(packageAdapter);

        Intent intent = getIntent();
        if (intent != null) {
            try {
                data = (FoodEntryListModel) intent.getSerializableExtra("packageData");
            } catch (Exception e) {
                Log.d("exception", e.toString());
            }
        }

        if (data != null) {

            mFoodName.setText(data.getFoodname());
            mUOM.setText(data.getUom());
            mCalorie.setText(data.getCalorie());
            mDescription.setText(data.getDescription());
            heading.setText("Edit Food Entry");


        }
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        SaveButton();

        run(FoodCategory_URL);

        SelctfoodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SelectFoodCategory = mfieldSelectfoodSpin.get(position);
                if(mFoodCategoryArr != null && mFoodCategoryArr.size() > 0)
               foodnameCategoryId = mFoodCategoryArr.get(position).foodCategoryId.toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    public void setfoodCatNamr(){
        String foodCatName = data.getFoodCatName();
        int pos = 0;
        if(mfieldSelectfoodSpin != null && mfieldSelectfoodSpin.size() > 0) {
            for (int i = 0; i < mfieldSelectfoodSpin.size(); i++) {
                if (foodCatName.equals(mfieldSelectfoodSpin.get(i))) {
                    pos = i;
                }
            }
        }
        SelctfoodSpinner.setSelection(pos);
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

                AddFoodEntry_Activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //JSONObject for Batches
                        try {
                            JSONObject forecast = new JSONObject(myResponse);
                            JSONArray jsonArray = forecast.getJSONArray("Category");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                String id = object.getString("id");
                                String foodCatId = object.getString("foodcategories_id");
                                String foodName = object.getString("name");
                                mfieldSelectfoodSpin.add(foodName);

                                FoodCategoryModel foodCategoryModel = new FoodCategoryModel();
                                foodCategoryModel.setId(id);
                                foodCategoryModel.setFoodCategoryId(foodCatId);
                                foodCategoryModel.setFoodCategoryName(foodName);
                                mFoodCategoryArr.add(foodCategoryModel);


                            }
                            FoodCategoryModel foodCategoryModel = new FoodCategoryModel();
                            foodCategoryModel.foodCategoryName="Select";
                            foodCategoryModel.foodCategoryId="0";
                            mFoodCategoryArr.add(0,foodCategoryModel);

                            ArrayAdapter<String> foodcatAdapter = new ArrayAdapter<>(AddFoodEntry_Activity.this,
                                    android.R.layout.simple_list_item_1, mfieldSelectfoodSpin);
                            foodcatAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                            SelctfoodSpinner.setAdapter(foodcatAdapter);
                        if (data !=null ){
                            setfoodCatNamr();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
            }
        });
    }

    public void SaveButton(){

        mLinearLayout = findViewById(R.id.Linear_Layout);
        SaveBtn = findViewById(R.id.savebtn);
        SaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean networkConectivity =  NetworkConnectivity.isConnected(AddFoodEntry_Activity.this);
                if (networkConectivity) {
                    if (Validation()) {
                        SaveBtn.setVisibility(View.GONE);
                        dietProgress.setVisibility(View.VISIBLE);
                        if (data != null) {
                            String id = data.getId();
                            String foodName = mFoodName.getText().toString();
                            String muom = mUOM.getText().toString();
                            String description = mDescription.getText().toString();
                            String calorie = mCalorie.getText().toString();
                            String selectFoodCatorgy = foodnameCategoryId;
                            EditHandler handler = new EditHandler(id, foodName, muom,
                                    description, calorie, selectFoodCatorgy);
                            String result = null;
                            try {
                                result = handler.execute().get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        } else {

                            String foodName = mFoodName.getText().toString();
                            String muom = mUOM.getText().toString();
                            String description = mDescription.getText().toString();
                            String calorie = mCalorie.getText().toString();
                            String selectFoodCatorgy = foodnameCategoryId;
                            PostHandler handler = new PostHandler(foodName, muom,
                                    description, calorie, selectFoodCatorgy);
                            String result = null;
                            try {
                                result = handler.execute().get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
                else {
                    NetworkConnectivity.networkConnetivityShowDialog(AddFoodEntry_Activity.this);

                }
            }
        });

    }

    public class EditHandler extends AsyncTask<String,Void, String> {
        String id, foodName, muom, description, calorie, selectFoodCateogory;

        OkHttpClient client = new OkHttpClient();

        public EditHandler(String id, String foodName, String muom,
                           String description, String calorie,
                           String selectFoodCateogory) {
            this.id = id;
            this.foodName = foodName;
            this.muom = muom;
            this.description = description;
            this.calorie = calorie;
            this.selectFoodCateogory = selectFoodCateogory;
        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody formBody = new FormBody.Builder()
                    .add("id",id)
                    .add("fname",foodName)
                    .add("uom",muom)
                    .add("calorie",calorie)
                    .add("description",description)
                    .add("foodcat",selectFoodCateogory)
                    .add("profile",photos)
                    .build();

            Request request = new Request.Builder()
                    .url(Add_Food_Url)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    final String MyResponse = response.body().string();
                    AddFoodEntry_Activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            JSONObject forecast = null;
                            try {
                                forecast = new JSONObject(MyResponse);
                                String Message = forecast.getString("message");
                                SaveBtn.setVisibility(View.VISIBLE);
                                dietProgress.setVisibility(View.GONE);
                                showDialog(Message);
                                heading.setText("Add Food Entry");
                            }
                            catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
            return null;
        }
    }


    public class PostHandler extends AsyncTask<String,Void, String> {
        String foodName, muom, description, calorie, selectFoodCateogory;

        OkHttpClient client = new OkHttpClient();

        public PostHandler(String foodName, String muom, String description, String calorie, String selectFoodCateogory) {
            this.foodName = foodName;
            this.muom = muom;
            this.description = description;
            this.calorie = calorie;
            this.selectFoodCateogory = selectFoodCateogory;

        }

        @Override
        protected String doInBackground(String... strings) {

            RequestBody formBody = new FormBody.Builder()
                    .add("fname",foodName)
                    .add("uom",muom)
                    .add("calorie",calorie)
                    .add("description",description)
                    .add("foodcat",selectFoodCateogory)
                    .add("profile",photos)
                    .build();

            Request request = new Request.Builder()
                    .url(Add_Food_Url)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    final String MyResponse = response.body().string();
                    AddFoodEntry_Activity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            JSONObject forecast = null;
                            try {
                                forecast = new JSONObject(MyResponse);
                                String Message = forecast.getString("message");
                                SaveBtn.setVisibility(View.VISIBLE);
                                dietProgress.setVisibility(View.GONE);
                                showDialog(Message);
                            }
                            catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
            return null;
        }
    }

    public boolean Validation(){
        String foodName = mFoodName.getText().toString();
        boolean flag = true;
        if (foodName.isEmpty()){
            flag = false;
            mFoodName.setError("Please Enter Food Name");
        }
        else if (SelctfoodSpinner.getSelectedItem().toString().trim().equals("Select")){
            flag = false;

            Snackbar.make(mLinearLayout, "Please Select Food Category",Snackbar.LENGTH_SHORT).show();

        }
        return flag;
    }

    public void showDialog(final String message) {

        final Dialog dialog = new Dialog(AddFoodEntry_Activity.this);
        dialog.setContentView(R.layout.custom_alert_dialog_box);
        TextView mesg = dialog.findViewById(R.id.msg);
        mesg.setText(message);
        Button okbtn = dialog.findViewById(R.id.okbtn);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
                overridePendingTransition(R.anim.push_down_in,R.anim.push_down_out);

            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();


    }

    public void selectImage() {
        boolean checkPermissions = Permissions.checkPermission(AddFoodEntry_Activity.this);
        if (checkPermissions) {
            final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(AddFoodEntry_Activity.this);
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

}
