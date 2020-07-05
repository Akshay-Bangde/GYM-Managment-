package com.example.smile.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.smile.R;
import com.example.smile.activities.Add_Diet_Plan_Activity;
import com.example.smile.dataObjects.DietModel;
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

public class DietAdapter extends RecyclerView.Adapter<DietAdapter.ViewHolder> {

    private Context mcontext;
    private ArrayList<DietModel> dietModelArrayList;
    String Delete_URL=BaseURL+"DietPlanGrid";
    CategoryCallBackInterface categoryCallBackInterface;

    public DietAdapter(Context mcontext, ArrayList<DietModel> dietModelArrayList) {
        this.mcontext = mcontext;
        this.dietModelArrayList = dietModelArrayList;
        this.categoryCallBackInterface=(CategoryCallBackInterface) mcontext;
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View listItem= layoutInflater.inflate(R.layout.content_diet__list_, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {

        viewHolder.dietserialID.setText(dietModelArrayList.get(i).getSerial_id());
        viewHolder.plan_name.setText(dietModelArrayList.get(i).getPlanName());
        viewHolder.prework.setText(dietModelArrayList.get(i).getPreWorkout());
        viewHolder.postwork.setText(dietModelArrayList.get(i).getPostWorkout());

        viewHolder.lunch.setText(dietModelArrayList.get(i).getLunchh());
        viewHolder.dinner.setText(dietModelArrayList.get(i).getDinnerr());



        viewHolder.dietEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext, Add_Diet_Plan_Activity.class);
                intent.putExtra("packageData", dietModelArrayList.get(i));
                mcontext.startActivity(intent);
            }
        });



        viewHolder.dietDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(mcontext);
                dialog.setContentView(R.layout.log_out_alert_dialog_box);
                TextView mesg = dialog.findViewById(R.id.message);
                mesg.setText("Do You Want To Delete This Record ?");
                Button nobtn = dialog.findViewById(R.id.text_no);
                Button yesbtn = dialog.findViewById(R.id.text_yes);

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
                        String id = dietModelArrayList.get(i).getId();
                        categoryCallBackInterface.onDeleteItemListener(i);
                        DeleteHandler handler = new DeleteHandler(id, i);
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
        return dietModelArrayList.size();

    }
        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView dietserialID;
            TextView plan_name;
            TextView prework;
            TextView postwork;
            TextView lunch;
            TextView dinner;
            Button dietDelete, dietEdit;

            public ViewHolder( View itemView) {
                super(itemView);

                dietserialID=itemView.findViewById(R.id.d_srno);
                plan_name=itemView.findViewById(R.id.dietName);
                prework=itemView.findViewById(R.id.preWork);
                postwork=itemView.findViewById(R.id.postWork);

                lunch=itemView.findViewById(R.id.lunch);
                dinner=itemView.findViewById(R.id.dinner);

                dietDelete=itemView.findViewById(R.id.diet_delete);
                dietEdit=itemView.findViewById(R.id.diet_edit);

            }
        }
    }
