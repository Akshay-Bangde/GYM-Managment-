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
import com.example.smile.activities.Add_Member_DeatailsActivity;
import com.example.smile.activities.Add_Staff_Activity;
import com.example.smile.activities.Indiviual_Member_PaymentActivity;
import com.example.smile.dataObjects.MemberListModel;
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

public class MemberListAdapter extends
        RecyclerView.Adapter<MemberListAdapter.CustomViewHolder> {
    String Delete_URL = BaseURL+"MemberRegistrationGrid";
    CategoryCallBackInterface categoryCallBackInterface;

    Context context;
    private ArrayList<MemberListModel> items;

    public MemberListAdapter(Context context, ArrayList<MemberListModel> items){
        this.context = context;
        this.items = items;
        this.categoryCallBackInterface = (CategoryCallBackInterface) context;
    }


    @NonNull
    @Override
    public MemberListAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MemberListAdapter.CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.content_member__list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MemberListAdapter.CustomViewHolder customViewHolder, final int position) {
        customViewHolder.id.setText(items.get(position).getSerialNo());
        customViewHolder.memberName.setText(items.get(position).getName());
        customViewHolder.punchId.setText(items.get(position).getPunchId());
        customViewHolder.contactNO.setText(items.get(position).getContactno());
        customViewHolder.dob.setText(items.get(position).getDob());
        customViewHolder.doj.setText(items.get(position).getDoj());
        customViewHolder.remainingAmount.setText(items.get(position).getRemains());
        customViewHolder.status.setText(items.get(position).getStatus());


        customViewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Add_Member_DeatailsActivity.class);
                intent.putExtra("packageData",items.get(position));
                intent.putExtra("role","edit");
                context.startActivity(intent);
            }
        });

        customViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(context);
                dialog.setContentView(R.layout.log_out_alert_dialog_box);
                TextView mesg = dialog.findViewById(R.id.message);
                mesg.setText("Do You Want To Delete This Record ?");
                Button nobtn=dialog.findViewById(R.id.text_no);
                Button yesbtn=dialog.findViewById(R.id.text_yes);

                nobtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                yesbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        String id = items.get(position).getId();
                        categoryCallBackInterface.onDeleteItemListener(position);
                        DeleteHandler handler = new DeleteHandler(id, position);
                        String result = null;
                        try {
                            result = handler.execute(Delete_URL).get();

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                    }
                });
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();



            }
        });

        customViewHolder.pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Indiviual_Member_PaymentActivity.class);
                intent.putExtra("packageData",items.get(position));
                context.startActivity(intent);
            }
        });

    }


    public class DeleteHandler extends AsyncTask<String, Void, String> {
        OkHttpClient client = new OkHttpClient();
        String id;
        int position;
        public DeleteHandler(String id, int pos) {
            this.id = id;
            this.position = pos;
        }

        @Override
        protected String doInBackground(String... params) {
            RequestBody formBody = new FormBody.Builder()
                    .add("id", id)
                    .build();

            Request request = new Request.Builder()
                    .post(formBody)
                    .url(Delete_URL)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }
                @Override
                public void onResponse(@NotNull Call call,@NotNull Response response) throws IOException {

                }
            });



            return null;
        }

    }



    @Override
    public int getItemCount() {
        return items.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        private TextView id;
        private TextView memberName;
        private TextView punchId;
        private TextView contactNO;
        private TextView dob;
        private TextView doj;
        private TextView remainingAmount;
        private TextView status;

        public Button edit;
        public Button delete;
        public Button pay;
        public Button view;

        public CustomViewHolder(View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.ID);
            memberName = itemView.findViewById(R.id.NAME);
            punchId = itemView.findViewById(R.id.PUNCHID);
            contactNO = itemView.findViewById(R.id.CONTACTNO);
            dob = itemView.findViewById(R.id.DOB);
            doj = itemView.findViewById(R.id.DOJ);
            remainingAmount = itemView.findViewById(R.id.REMANING_AMOUNT);
            status = itemView.findViewById(R.id.STATUS);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
            pay = itemView.findViewById(R.id.payNow);
            view = itemView.findViewById(R.id.viewhistory);

        }
    }
}


