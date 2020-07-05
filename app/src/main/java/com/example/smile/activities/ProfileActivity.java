package com.example.smile.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.smile.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private EditText mGymName,mGymContactNo,mGymEmail,mGymAddress,mCurrencytype,mInvestmentAmount,mUsername,
            mInvoiceterms,mInvoicegreetings,mUpcomingrenewals;
    Button mUpdate;
    LinearLayout mLinearLayout;
    private CircleImageView mImageView;
    private Integer REQUEST_CAMERA = 1, SELECT_FILE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        mImageView = findViewById(R.id.profile_image);
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

        mGymName = findViewById(R.id.gym_name);
        mGymContactNo = findViewById(R.id.gymContact_no);
        mGymEmail = findViewById(R.id.gymEmail);
        mGymAddress = findViewById(R.id.gymAddress);
        mCurrencytype = findViewById(R.id.currencyType);
        mInvestmentAmount = findViewById(R.id.investmentAmount);
        mUsername = findViewById(R.id.username);
        mInvoiceterms = findViewById(R.id.invoiceTerms);
        mInvoicegreetings = findViewById(R.id.invoiceGreetings);
        mUpcomingrenewals = findViewById(R.id.gymUpcomingRenewal);
        mLinearLayout = findViewById(R.id.gymLinearlayout);
        mUpdate = findViewById(R.id.gymUpdate);


        UpdateButton();
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectImage();
            }
        });

    }
    private void SelectImage() {
        final CharSequence [] items = {"Camera","Gallery","Cancel"};
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfileActivity.this);
        alertDialog.setTitle("Add Photo !");
        alertDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (items[i].equals("Camera")){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
                    startActivityForResult(intent,REQUEST_CAMERA);
                }
                else if (items[i].equals("Gallery")){
                    Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/");
                    startActivityForResult(Intent.createChooser(intent,"select file"),SELECT_FILE);
                }
                else if (items[i].equals("Cancel")){
                    dialog.dismiss();
                }
            }
        });
        alertDialog.show();

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (resultCode == REQUEST_CAMERA) {
                Bundle bundle = data.getExtras();
                final Bitmap bitmap = (Bitmap) bundle.get("data");
                mImageView.setImageBitmap(bitmap);
            } else if (requestCode == SELECT_FILE) {
                Uri SelectedImageUri = data.getData();
                mImageView.setImageURI(SelectedImageUri);
            }
        }
        else if (requestCode == RESULT_CANCELED){
            Toast.makeText(this, getResources().getString(R.string.picture_was_not_taken), Toast.LENGTH_SHORT).show();
        }
    }

    private void UpdateButton() {

        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()){

                }
            }
        });
    }

    private boolean validation() {
        String gymName = mGymName.getText().toString();
        String gymEmail = mGymEmail.getText().toString();
        String gymContact = mGymContactNo.getText().toString();
        String gymAddress = mGymAddress.getText().toString();
        String gymCurrencytype = mCurrencytype.getText().toString();
        String gymInvestmentAmount = mInvestmentAmount.getText().toString();
        String GymUsername = mUsername.getText().toString();
        String gymInvoiceterms = mInvoiceterms.getText().toString();
        String gymInvoicegreetings = mInvoicegreetings.getText().toString();
        String gymUpcomingrenewals = mUpcomingrenewals.getText().toString();
        boolean flag = true;
        if (gymName.isEmpty()){
            flag = false;
                mGymName.setError(getResources().getString(R.string.plz_enter_gym_name));
            }
        if (gymEmail.isEmpty()){
            flag = false;
           mGymEmail.setError(getResources().getString(R.string.plz_enter_email));
           }
        if (gymContact.isEmpty()){
            flag = false;
            mGymContactNo.setError(getResources().getString(R.string.plz_enter_contact_no));
        }
        if (gymCurrencytype.isEmpty()){
            flag = false;
            mCurrencytype.setError(getResources().getString(R.string.plz_enter_currency_type));
        }
        if (gymInvestmentAmount.isEmpty()){
            flag = false;
            mInvestmentAmount.setError(getResources().getString(R.string.plz_enter_capital_amount));
        }
        if (GymUsername.isEmpty()){
            flag = false;
            mUsername.setError(getResources().getString(R.string.plz_enter_username));
        }
        if (gymAddress.isEmpty()){
            flag = false;
            mGymAddress.setError(getResources().getString(R.string.plz_enter_address));
        }
        if (gymInvoiceterms.isEmpty()){
            flag = false;
            mInvoiceterms.setError(getResources().getString(R.string.plz_enter_invoice_terms));
        }
        if (gymInvoicegreetings.isEmpty()){
            flag = false;
            mInvoicegreetings.setError(getResources().getString(R.string.plz_enter_greeting));
        }
        if (gymUpcomingrenewals.isEmpty()){
            flag = false;
            mUpcomingrenewals.setError(getResources().getString(R.string.plz_enter_upcoming_renewal));
        }

        else if (gymContact.length()<10){
            flag = false;
            Snackbar.make(mLinearLayout,getResources().getString(R.string.plz_enter_valid_contact_no),Snackbar.LENGTH_SHORT).show();
        }

        return flag;
    }
}
