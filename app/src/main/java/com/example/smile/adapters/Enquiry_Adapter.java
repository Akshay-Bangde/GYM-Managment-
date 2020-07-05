package com.example.smile.adapters;

import android.app.Dialog;
import android.content.Context;
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
import com.example.smile.dataObjects.Enquiry_Model;
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

public class Enquiry_Adapter extends RecyclerView.Adapter<Enquiry_Adapter.EviewHolder> {

     private Context mcontext;
     private ArrayList<Enquiry_Model> enquiryModelArrayList;
     private CategoryCallBackInterface categoryCallBackInterface;
     String Delete_URL=BaseURL+"InquiryGrid";



    public Enquiry_Adapter(Context mcontext, ArrayList<Enquiry_Model> enquiryModelArrayList) {
        this.mcontext = mcontext;
        this.enquiryModelArrayList = enquiryModelArrayList;
        this.categoryCallBackInterface = (CategoryCallBackInterface) mcontext;
    }

    @Override
    public Enquiry_Adapter.EviewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater=LayoutInflater.from(viewGroup.getContext());
        View listItem= layoutInflater.inflate(R.layout.content_enquiry_grid, viewGroup, false);
        EviewHolder eviewHolder = new EviewHolder(listItem);
        return eviewHolder;


    }

    @Override
    public void onBindViewHolder(Enquiry_Adapter.EviewHolder eviewHolder, final  int i) {


        eviewHolder.msrno.setText(enquiryModelArrayList.get(i).getE_serial_id());
        eviewHolder.mDate.setText(enquiryModelArrayList.get(i).getE_enqDate());
        eviewHolder.mName.setText(enquiryModelArrayList.get(i).getE_name());
        eviewHolder.mContact.setText(enquiryModelArrayList.get(i).getE_contact());
        eviewHolder.mEmail.setText(enquiryModelArrayList.get(i).getE_email());
        eviewHolder.mCity.setText(enquiryModelArrayList.get(i).getE_city());
        eviewHolder.mRemark.setText(enquiryModelArrayList.get(i).getE_remark());
        eviewHolder.mStatus.setText(enquiryModelArrayList.get(i).getE_enqStatus());
        eviewHolder.mFollowDate.setText(enquiryModelArrayList.get(i).getE_followUp());




        eviewHolder.eDelete.setOnClickListener(new View.OnClickListener() {
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
                        String id = enquiryModelArrayList.get(i).getE_serial_id();
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
        return enquiryModelArrayList.size();
    }


     class EviewHolder extends RecyclerView.ViewHolder {


        TextView msrno,mDate,mName,mContact,mEmail,mCity,mRemark,mStatus,mFollowDate;
        Button eDelete;

            EviewHolder( View itemView) {
            super(itemView);

            msrno=itemView.findViewById(R.id.e_srno);
            mDate=itemView.findViewById(R.id.enq_date);
            mName=itemView.findViewById(R.id.e_name);
            mContact=itemView.findViewById(R.id.e_contact);
            mEmail=itemView.findViewById(R.id.e_email);
            mCity=itemView.findViewById(R.id.e_city);
            mRemark=itemView.findViewById(R.id.e_remark);
            mStatus=itemView.findViewById(R.id.e_status);
            mFollowDate=itemView.findViewById(R.id.e_nextfd);
            eDelete=itemView.findViewById(R.id.enq_delete);
        }
    }
}
