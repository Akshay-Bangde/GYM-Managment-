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
import com.example.smile.activities.Add_Equipment_Acitivity;
import com.example.smile.dataObjects.Equipment_Model;
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

public class Gym_Equipment_Adapter extends RecyclerView.Adapter<Gym_Equipment_Adapter.ViewHolder>{

        private Context mcontext;
        private ArrayList<Equipment_Model> eqpListdata;
        private   String Delete_URL=BaseURL+"EquipmentGrid";
        private CategoryCallBackInterface categoryCallBackInterface;

    public Gym_Equipment_Adapter(Context mcontext, ArrayList<Equipment_Model> eqpListdata) {
        this.mcontext = mcontext;
        this.eqpListdata = eqpListdata;
        this.categoryCallBackInterface=(CategoryCallBackInterface) mcontext;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View listItem= layoutInflater.inflate(R.layout.content_gym__equipment_, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {

        viewHolder.eqpsrno.setText(eqpListdata.get(i).geteSerialid());
        viewHolder.eqptrnsno.setText(eqpListdata.get(i).getEtransaction_id());
        viewHolder.eqpname.setText(eqpListdata.get(i).geteName());
        viewHolder.eqpdesc.setText(eqpListdata.get(i).geteDescription());
        viewHolder.eqpRate.setText(eqpListdata.get(i).geteRate());
        viewHolder.eqpqty.setText(eqpListdata.get(i).geteQty());
        viewHolder.eqpNetAmt.setText(eqpListdata.get(i).geteNetamt());


        viewHolder.eqpedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mcontext, Add_Equipment_Acitivity.class);
                intent.putExtra("packageData", eqpListdata.get(i));
                mcontext.startActivity(intent);
            }
        });


        viewHolder.eqpdelete.setOnClickListener(new View.OnClickListener() {
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
                        String id = eqpListdata.get(i).getId();
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
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                }
            });


            return null;
        }
    }




    @Override
    public int getItemCount() {
       return eqpListdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView eqpsrno;
        TextView eqptrnsno;
        TextView eqpname;
        TextView eqpdesc;
        TextView eqpRate;
        TextView eqpqty;
        TextView eqpNetAmt;
        Button eqpdelete,eqpedit;

        public ViewHolder( View itemView) {
            super(itemView);

            eqpsrno=itemView.findViewById(R.id.eqp_sr_no);
            eqptrnsno=itemView.findViewById(R.id.eqp_id);
            eqpname=itemView.findViewById(R.id.eqp_name);
            eqpdesc=itemView.findViewById(R.id.eqp_description);
            eqpRate=itemView.findViewById(R.id.eqp_purchase_rate);
            eqpqty=itemView.findViewById(R.id.eqp_qty);
            eqpNetAmt=itemView.findViewById(R.id.eqp_net_amount);
            eqpdelete=itemView.findViewById(R.id.eqpDelete);
            eqpedit=itemView.findViewById(R.id.eqpEdit);
        }
    }


}
