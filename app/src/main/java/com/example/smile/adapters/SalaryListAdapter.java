package com.example.smile.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.smile.R;
import com.example.smile.activities.Add_Product_Activity;
import com.example.smile.activities.Add_Staff_Activity;
import com.example.smile.activities.SalaryListPayNow;
import com.example.smile.dataObjects.SalaryListModel;
import com.example.smile.dataObjects.StaffListModel;
import com.example.smile.utils.CategoryCallBackInterface;

import org.jetbrains.annotations.NotNull;

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

public class SalaryListAdapter extends
        RecyclerView.Adapter<SalaryListAdapter.CustomViewHolder> {

    Context context;
    private ArrayList<SalaryListModel> items;

    public SalaryListAdapter(Context context, ArrayList<SalaryListModel> items){
        this.context = context;
        this.items = items;
    }


    @NonNull
    @Override
    public SalaryListAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SalaryListAdapter.CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.content_salary_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SalaryListAdapter.CustomViewHolder customViewHolder, final int position) {
        customViewHolder.id.setText(items.get(position).getSerialNo());
        customViewHolder.staffName.setText(items.get(position).getName());
        customViewHolder.contactNO.setText(items.get(position).getContactno());
        customViewHolder.employeeType.setText(items.get(position).getEmployeeType());
        customViewHolder.salary.setText(items.get(position).getSalary());
        customViewHolder.trainingCharge.setText(items.get(position).getTrainingCharge());
        customViewHolder.doj.setText(items.get(position).getDoj());


        customViewHolder.paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SalaryListPayNow.class);
                intent.putExtra("packageData",items.get(position));
                context.startActivity(intent);
            }
        });


    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        private TextView id;
        private TextView staffName;
        private TextView doj;
        private TextView contactNO;
        private TextView employeeType;
        private TextView salary;
        private TextView trainingCharge;
        public Button paynow;


        public CustomViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.ID);
            staffName = itemView.findViewById(R.id.NAME);
            doj = itemView.findViewById(R.id.DOJ);
            contactNO = itemView.findViewById(R.id.CONTACTNO);
            employeeType = itemView.findViewById(R.id.EMPLOYEETYPE);
            salary = itemView.findViewById(R.id.SALARY);
            trainingCharge = itemView.findViewById(R.id.TRAINING_CARGE);
            paynow = itemView.findViewById(R.id.payNow);

        }
    }
}


