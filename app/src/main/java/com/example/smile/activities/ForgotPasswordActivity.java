package com.example.smile.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.smile.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText emailId;
    private Button buttonForgot;
    private TextView back;
    private LinearLayout forgot_layout;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
        initView();
        bindControls();
    }

    private void initView(){
        emailId = findViewById(R.id.email_id);
        buttonForgot = findViewById(R.id.button_forgot);
        back = findViewById(R.id.back);
        forgot_layout = findViewById(R.id.forgot_layout);
    }

    private Boolean validation(){
        String email = emailId.getText().toString();
        Boolean flag = true;
        if(email.isEmpty()){
            flag = false;
            emailId.setError(getResources().getString(R.string.error_email));
        }else if(email.length() > 0 && !email.matches(emailPattern)){
            flag = false;
            Snackbar.make(buttonForgot, getResources().getString(R.string.valid_error_valid_email), Snackbar.LENGTH_SHORT).show();
        }

        return flag;
    }

    private void bindControls(){
        buttonForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validation()){

                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.left_animation, R.anim.left_animation);

            }
        });

    }
}
