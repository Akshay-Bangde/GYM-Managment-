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
import android.support.design.widget.CoordinatorLayout;
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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smile.R;
import com.example.smile.dataObjects.ActivityListModel;
import com.example.smile.dataObjects.WorkoutExerciseListModel;
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

public class Add_Workout_ExerciseActivity extends AppCompatActivity {
    private EditText mWorkoutName;
    Button mAddButton;
    Spinner mBodypartSpinner;
    CoordinatorLayout mCoordinatorlayout;
    ArrayList<String> bodyPartArrayList;

    private CircleImageView mImageView;
    private Integer REQUEST_CAMERA = 1, SELECT_FILE = 0;
    String bodyPartSpinner;
    String Message;
    String photos = null;
    String bodyPartSpinner_URL = BaseURL + "WorkoutExercise";
    String AddWorkoutExercise_URL = BaseURL + "WorkoutExercise";
    WorkoutExerciseListModel data = null;
    TextView toolbar_title ;
    ProgressBar AddProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_activity_workout__exercise);

        Intent intent = getIntent();
        if (intent != null) {
            try {
                data = (WorkoutExerciseListModel) intent.getSerializableExtra("packageData");
            } catch (Exception e) {
                Log.d("exception", e.toString());
            }
        }


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

        mAddButton = findViewById(R.id.addBtn);
        mCoordinatorlayout = findViewById(R.id.coordinator_layout);
        mWorkoutName = findViewById(R.id.workout_name);
        mBodypartSpinner = findViewById(R.id.Spinnerbody_part_name);
        mImageView = findViewById(R.id.workout_exerciseImageView);
        toolbar_title = findViewById(R.id.toolbar_title);
        AddProgress=findViewById(R.id.pb);


        if (data != null) {

            mWorkoutName.setText(data.getWorkoutName());
            toolbar_title.setText("Edit Workout Exercise");
        }


        bodyPartArrayList = new ArrayList<String>();
        bodyPartArrayList.add("Select");


        mBodypartSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                bodyPartSpinner = bodyPartArrayList.get(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        // for camera and to select image from camera and gallery
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        AddButton();

        run(bodyPartSpinner_URL);
    }

    public void setSpinnerData(){
        String body_part = data.getBodypart();
        int pos = 0;
        if(bodyPartArrayList != null && bodyPartArrayList.size() > 0) {
            for (int i = 0; i < bodyPartArrayList.size(); i++) {
                if (body_part.equals(bodyPartArrayList.get(i))) {
                    pos = i;
                }
            }
        }
        mBodypartSpinner.setSelection(pos);
    }


    public void showDialog(final String message) {

        final Dialog dialog = new Dialog(Add_Workout_ExerciseActivity.this);
        dialog.setContentView(R.layout.custom_alert_dialog_box);
        TextView mesg = dialog.findViewById(R.id.msg);
        mesg.setText(message);
        Button okbtn = dialog.findViewById(R.id.okbtn);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                /*Intent intent = new Intent(Add_Workout_ExerciseActivity.this, Workout_Exercise_list_Activity.class);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(Add_Workout_ExerciseActivity.this, R.anim.push_down_in, R.anim.push_down_out);
                startActivity(intent, options.toBundle());
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);*/
                finish();
                overridePendingTransition(R.anim.push_down_in,R.anim.push_down_out);

            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();


    }

    //  Submit the record
    private void AddButton() {
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean networkConectivity = NetworkConnectivity.isConnected(Add_Workout_ExerciseActivity.this);
                if (networkConectivity) {
                    if (validation()) {
                        mAddButton.setVisibility(View.GONE);
                        AddProgress.setVisibility(View.VISIBLE);

                        if (data != null) {
                            String id = data.getId();
                            String bodyPartspiner = bodyPartSpinner;
                            String workoutName = mWorkoutName.getText().toString();

                            EditHandler handler = new EditHandler(id, bodyPartspiner, workoutName);

                            String result = null;
                            try {
                                result = handler.execute(AddWorkoutExercise_URL).get();

                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        } else {
                            String bodyPartspiner = bodyPartSpinner;
                            String workoutName = mWorkoutName.getText().toString();

                            PostHandler handler = new PostHandler(bodyPartspiner, workoutName);

                            String result = null;
                            try {
                                result = handler.execute(AddWorkoutExercise_URL).get();

                            } catch (InterruptedException e) {
                                e.printStackTrace();


                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    else {
                        NetworkConnectivity.networkConnetivityShowDialog(Add_Workout_ExerciseActivity.this);

                    }
                }
            }
        });
    }

    public class EditHandler extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();
        String id, bodyPartSpinner, bodyPart;


        public EditHandler(String id, String bodyPartSpinner, String bodyPart) {
            this.id = id;
            this.bodyPartSpinner = bodyPartSpinner;
            this.bodyPart = bodyPart;
        }

        @Override
        protected String doInBackground(String... params) {
            RequestBody formBody = new FormBody.Builder()
                    .add("id", id)
                    .add("bodypart", bodyPartSpinner)
                    .add("workout", bodyPart)
                    .add("profile", photos)
                    .build();

            Request request = new Request.Builder()
                    .url(AddWorkoutExercise_URL)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String myResponse = response.body().string();
                    Add_Workout_ExerciseActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject forecast = null;
                            try {
                                forecast = new JSONObject(myResponse);
                                Message = forecast.getString("message");
                                mAddButton.setVisibility(View.VISIBLE);
                                AddProgress.setVisibility(View.GONE);
                                reset();
                                showDialog(Message);
                                toolbar_title.setText("Add Workout Exercise");

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
        String bodyPartSpinner, bodyPart;

        public PostHandler(String bodyPartSpinner, String bodyPart) {
            this.bodyPartSpinner = bodyPartSpinner;
            this.bodyPart = bodyPart;
        }

        @Override
        protected String doInBackground(String... params) {
            RequestBody formBody = new FormBody.Builder()
                    .add("bodypart", bodyPartSpinner)
                    .add("workout", bodyPart)
                    .add("profile", photos)
                    .build();

            Request request = new Request.Builder()
                    .url(AddWorkoutExercise_URL)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String myResponse = response.body().string();
                    Add_Workout_ExerciseActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject forecast = null;
                            try {
                                forecast = new JSONObject(myResponse);
                                Message = forecast.getString("message");
                                mAddButton.setVisibility(View.VISIBLE);
                                AddProgress.setVisibility(View.GONE);
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

    //  Select Image from Mobile and Camera
    public void selectImage() {
        boolean checkPermissions = Permissions.checkPermission(Add_Workout_ExerciseActivity.this);
        if (checkPermissions) {
            final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(Add_Workout_ExerciseActivity.this);
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

    // Get webservice for body Part Spinner
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

                Add_Workout_ExerciseActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //JSONObject for Package Interested
                        try {
                            JSONObject forecast = new JSONObject(myResponse);
                            JSONArray jsonArray = forecast.getJSONArray("Exe");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String BodyPart = object.getString("bodypart");
                                bodyPartArrayList.add(BodyPart);

                            }
                            ArrayAdapter<String> bodyPartAdapter = new ArrayAdapter<String>(Add_Workout_ExerciseActivity.this,
                                    android.R.layout.simple_list_item_1, bodyPartArrayList);
                            bodyPartAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                            mBodypartSpinner.setAdapter(bodyPartAdapter);

                            if(data != null){
                                setSpinnerData();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }

    //  All Validation
    private boolean validation() {
        String workout_name = mWorkoutName.getText().toString();
        boolean flag = true;
        if (workout_name.isEmpty()) {
            flag = false;
            mWorkoutName.setError(getResources().getString(R.string.plz_enter_the_exercise));
        }
        if (mBodypartSpinner.getSelectedItem().toString().trim().equals("Select")) {
            flag = false;
            Snackbar.make(mCoordinatorlayout, getResources().getString(R.string.plz_select_body_part), Snackbar.LENGTH_SHORT).show();
        }
        return flag;
    }


    private void reset() {
        mWorkoutName.setText("");
        mBodypartSpinner.setSelection(0);


    }
}
