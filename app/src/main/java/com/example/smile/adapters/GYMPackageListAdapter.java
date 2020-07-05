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
import com.example.smile.activities.AddGymActivity_Activity;
import com.example.smile.activities.Add_Package_Activity;
import com.example.smile.dataObjects.GYMPackageListModel;
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

public class GYMPackageListAdapter extends
        RecyclerView.Adapter<GYMPackageListAdapter.CustomViewHolder> {

    String Delete_URL = BaseURL+"PackageGrid";
    CategoryCallBackInterface categoryCallBackInterface;

    Context context;
    private ArrayList<GYMPackageListModel> items;



    public GYMPackageListAdapter(Context context, ArrayList<GYMPackageListModel> items){
        this.context = context;
        this.items = items;
        this.categoryCallBackInterface = (CategoryCallBackInterface) context;
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.content_gym_package_list_,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder customViewHolder, final int position) {
        customViewHolder.srNo.setText(items.get(position).getSerial_no());
        customViewHolder.packagename.setText(items.get(position).getPackagename());
        customViewHolder.validity.setText(items.get(position).getDuration());
        customViewHolder.packageamount.setText(items.get(position).getPackageamount());
        customViewHolder.activityamount.setText(items.get(position).getActivityamount());
        customViewHolder.totalamount.setText(items.get(position).getTotalamount());

        customViewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Add_Package_Activity.class);
                intent.putExtra("packageData",items.get(position));
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

        private TextView srNo;
        private TextView packagename;
        private TextView validity;
        private TextView packageamount;
        private TextView activityamount;
        private TextView totalamount;
        Button edit,delete;


        public CustomViewHolder(View itemView) {
            super(itemView);


            srNo = itemView.findViewById(R.id.ID);
            packagename = itemView.findViewById(R.id.ACTIVITYNAME);
            validity = itemView.findViewById(R.id.VALIDITY);
            packageamount = itemView.findViewById(R.id.PACKAGEAMOUNT);
            activityamount = itemView.findViewById(R.id.ACTIVITYAMOUNT);
            totalamount = itemView.findViewById(R.id.TOTALAMOUNT);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);


        } }
}


