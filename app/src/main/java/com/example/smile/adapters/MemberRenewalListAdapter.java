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
import com.example.smile.activities.Add_Staff_Activity;
import com.example.smile.dataObjects.MemberRenewalListModel;
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

public class MemberRenewalListAdapter extends
        RecyclerView.Adapter<MemberRenewalListAdapter.CustomViewHolder> {
    //String Delete_URL = BaseURL+"StaffGrid";

    Context context;
    private ArrayList<MemberRenewalListModel> items;

    public MemberRenewalListAdapter(Context context, ArrayList<MemberRenewalListModel> items){
        this.context = context;
        this.items = items;
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.content_renew,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder customViewHolder, final int position) {
        customViewHolder.id.setText(items.get(position).getSerialNo());
        customViewHolder.name.setText(items.get(position).getName());
        customViewHolder.doj.setText(items.get(position).getDoj());
        customViewHolder.bookingDate.setText(items.get(position).getBookingDate());
        customViewHolder.expiryDate.setText(items.get(position).getExpiryDate());
        customViewHolder.packages.setText(items.get(position).getPackages());
        customViewHolder.duration.setText(items.get(position).getDuration());


        /*customViewHolder.renew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Add_Staff_Activity.class);
                intent.putExtra("packageData",items.get(position));
                intent.putExtra("role","edit");
                context.startActivity(intent);
            }
        });*/


    }






    @Override
    public int getItemCount() {
        return items.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView id;
        private TextView name;
        private TextView doj;
        private TextView bookingDate;
        private TextView expiryDate;
        private TextView packages;
        private TextView duration;
        public Button renew;


        public CustomViewHolder(View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.ID);
            name = itemView.findViewById(R.id.NAME);
            doj = itemView.findViewById(R.id.DOJ);
            bookingDate = itemView.findViewById(R.id.BOOKING_DATE);
            expiryDate = itemView.findViewById(R.id.EXPIRY_DATE);
            packages = itemView.findViewById(R.id.PACK);
            duration = itemView.findViewById(R.id.DURATION);
            renew = itemView.findViewById(R.id.renew);
        }
    }
}


