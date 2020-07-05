package com.example.smile.activities;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.smile.R;
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

public class SignInActivity extends AppCompatActivity implements TextWatcher,CompoundButton.OnCheckedChangeListener {

    public static final String BaseURL="http://gymapi.neatandcleanmarketing.com/api/";
    private EditText musername, mpassword;
    private LinearLayout mlogin_layout;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Button login_btn;
    Toolbar toolbar;
    CheckBox mRememberMeCheckBox;
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor mEditor;
    private static final String PREF_NAME ="prefx";
    private static final String KEY_REMEMBER ="remember";
    private static final String KEY_USERNAME ="username";
    private static final String KEY_PASSWORD ="password";
    ProgressBar progressBar;
    FrameLayout frameLayout;
    ImageView showPassword,hidePassword;

    public static  String jname;
    public static  String jusername;
    final String Login_URL = BaseURL+"Login/GetLogin" ;
    String Message;
    String Unsuccess = "Login Failed";
    String Successful = "Login Successful";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        frameLayout = findViewById(R.id.content_frame);
        showPassword = findViewById(R.id.showImageView);
        hidePassword = findViewById(R.id.hideImageView);

        LoginButton();

        //Coding for remember_me checkbox
        mSharedPreferences = getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mRememberMeCheckBox = findViewById(R.id.remember_me_checkBox);

        if (mSharedPreferences.getBoolean(KEY_REMEMBER,false))
            mRememberMeCheckBox.setChecked(true);
        else mRememberMeCheckBox.setChecked(false);

        musername.setText(mSharedPreferences.getString(KEY_USERNAME,""));
        mpassword.setText(mSharedPreferences.getString(KEY_PASSWORD,""));

        musername.addTextChangedListener(SignInActivity.this);
        mpassword.addTextChangedListener(SignInActivity.this);
        mRememberMeCheckBox.setOnCheckedChangeListener(SignInActivity.this);
        jusername=musername.getText().toString();
        ShowHidePassword();

    }

    public void ShowHidePassword(){

        hidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPassword.setVisibility(View.VISIBLE);
                hidePassword.setVisibility(View.GONE);
                mpassword.setInputType(1);
                mpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        });

        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPassword.setVisibility(View.GONE);
                hidePassword.setVisibility(View.VISIBLE);
                mpassword.setInputType(1);
                mpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

    }

    public void showDialog(){

        final Dialog dialog = new Dialog(SignInActivity.this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_alert_dialog_box);
        final TextView mesg = dialog.findViewById(R.id.msg);
        mesg.setText("Login Successful");
        Button okbtn=dialog.findViewById(R.id.okbtn);
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                // if(Message.equals(Successful)) {

                Intent intent = new Intent(SignInActivity.this, Home_Activity.class);
                //ActivityOptions options = ActivityOptions.makeCustomAnimation(SignInActivity.this,R.anim.right_to_left_slide,R.anim.left_to_right_slide);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);

                //  }



            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(600,500);
        dialog.show();



    }


    public void LoginButton() {

        mlogin_layout = findViewById(R.id.login_layout);
        musername = findViewById(R.id.username);
        mpassword = findViewById(R.id.password);
        login_btn = findViewById(R.id.button_login);
        progressBar=findViewById(R.id.pb);

        login_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean networkConectivity =  NetworkConnectivity.isConnected(SignInActivity.this);
                if (networkConectivity) {
                    if (validation()) {
                        progressBar.setVisibility(View.VISIBLE);
                        login_btn.setVisibility(View.GONE);

                        showDialog();
                        //  String email = musername.getText().toString();
                        //  String password = mpassword.getText().toString();

                      /*  PostHandler handler = new PostHandler(email, password);

                        String result = null;

                        try {
                            result = handler.execute(Login_URL).get();

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }*/
                    }
                }
                else {
                    NetworkConnectivity.networkConnetivityShowDialog(SignInActivity.this);

                }
            }


        });

    }

    // Code for web API
    public class PostHandler extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();
        String email, password;

        public PostHandler(String user, String password) {
            this.email = user;
            this.password = password;
        }

        @Override
        protected String doInBackground(String... params) {
            RequestBody formBody = new FormBody.Builder()
                    .add("email", email)
                    .add("password", password)
                    .build();

            Request request = new Request.Builder()
                    .url(Login_URL)
                    .post(formBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String myResponse = response.body().string();
                    SignInActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject  forecast = null;
                            try {
                                forecast = new JSONObject(myResponse);
                                progressBar.setVisibility(View.GONE);
                                login_btn.setVisibility(View.VISIBLE);


                                JSONObject jsonObjectuser=forecast.getJSONObject("User");
                                jname=jsonObjectuser.getString("name");


                                Message = forecast.getString("message");
                                showDialog();



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


    //Validation for all edit Text
    public Boolean validation() {
        String name = musername.getText().toString();
        String pass = mpassword.getText().toString();

        Boolean flag = true;
        if (name.isEmpty() && pass.isEmpty()) {
            flag = false;
            musername.setError(getResources().getString(R.string.error_email));
            mpassword.setError(getResources().getString(R.string.error_password));
        } else if (name.length() > 0 && !name.matches(emailPattern)) {
            flag = false;
            Snackbar.make(mlogin_layout, getResources().getString(R.string.valid_error_valid_email), Snackbar.LENGTH_SHORT).show();
        }

        return flag;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        ManagePrefs();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
    public void ManagePrefs(){
        if (mRememberMeCheckBox.isChecked()){
            mEditor.putString(KEY_USERNAME,musername.getText().toString().trim());
            mEditor.putString(KEY_PASSWORD,mpassword.getText().toString().trim());
            mEditor.putBoolean(KEY_REMEMBER,true);
            mEditor.apply();
        }
        else {
            mEditor.putBoolean(KEY_REMEMBER,false);
            mEditor.remove(KEY_PASSWORD);
            mEditor.remove(KEY_USERNAME);
            mEditor.apply();
        }
    }
}