package com.example.smile.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.smile.R;
import com.example.smile.dataObjects.MemberAttendanceModel;
import com.example.smile.dataObjects.MemberGroupListModel;

import java.util.ArrayList;

public class MemberAttendanceAdapter extends
        RecyclerView.Adapter<MemberAttendanceAdapter.CustomViewHolder> {
    Context context;
    private ArrayList<MemberAttendanceModel> items;

    public MemberAttendanceAdapter(Context context, ArrayList<MemberAttendanceModel> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.content_member__attendance, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MemberAttendanceAdapter.CustomViewHolder customViewHolder, int position) {
       // customViewHolder.id.setText(items.get(position).getId());
        customViewHolder.name.setText(items.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

      //  private TextView id;
        private TextView name;

        public CustomViewHolder(View itemView) {
            super(itemView);

          //  id = itemView.findViewById(R.id.ID);
            name = itemView.findViewById(R.id.memberName);


        }
    }
}


