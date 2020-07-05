package com.example.smile.adapters;

import android.app.Dialog;
import android.content.Context;
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
import com.example.smile.dataObjects.WorkoutCategoryModel;
import com.example.smile.utils.CategoryCallBackInterface;
import com.example.smile.utils.WorkoutCategoryInterface;

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

public class WorkoutCategoryAdapter extends
        RecyclerView.Adapter<WorkoutCategoryAdapter.CustomViewHolder> {


    String Delete_URL = BaseURL+"WorkoutCategoryGrid";
    CategoryCallBackInterface categoryCallBackInterface;
    WorkoutCategoryInterface workoutCategoryInterface;

    Context context;
    private ArrayList<WorkoutCategoryModel> items;

    public WorkoutCategoryAdapter(Context context,ArrayList<WorkoutCategoryModel> items){
        this.context = context;
        workoutCategoryInterface = (WorkoutCategoryInterface) this.context;
        this.items = items;
        this.categoryCallBackInterface = (CategoryCallBackInterface) context;
    }


    @NonNull
    @Override
    public WorkoutCategoryAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WorkoutCategoryAdapter.CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.content_workout__category,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutCategoryAdapter.CustomViewHolder customViewHolder, final int position) {
        customViewHolder.srNo.setText(items.get(position).getSerial_no());
        customViewHolder.bodypart.setText(items.get(position).getBodypart());
        customViewHolder.mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                workoutCategoryInterface.getBodyPart(items.get(position));

            }
        });


        customViewHolder.mDelete.setOnClickListener(new View.OnClickListener() {
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
        private TextView bodypart;
        public Button mEdit,mDelete;

        public CustomViewHolder(View itemView) {
            super(itemView);


            srNo = itemView.findViewById(R.id.ID);
            bodypart = itemView.findViewById(R.id.BODYPARTNAME);
            mEdit = itemView.findViewById(R.id.edit);
            mDelete = itemView.findViewById(R.id.delete);

        } }
}


