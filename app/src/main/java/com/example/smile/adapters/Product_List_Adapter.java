package com.example.smile.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.smile.R;
import com.example.smile.activities.Add_Product_Activity;
import com.example.smile.dataObjects.Product_List_Model;

import java.util.ArrayList;

public class Product_List_Adapter extends RecyclerView.Adapter<Product_List_Adapter.ProVIewHolder> {

    private Context context;
    private ArrayList<Product_List_Model> productListModelArrayList;


    public Product_List_Adapter(Context context, ArrayList<Product_List_Model> productListModelArrayList) {
        this.context = context;
        this.productListModelArrayList = productListModelArrayList;


    }

    @Override
    public ProVIewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View listItem= layoutInflater.inflate(R.layout.content_product_list, viewGroup, false);
        ProVIewHolder proVIewHolder=new ProVIewHolder(listItem);
        return proVIewHolder;
    }

    @Override
    public void onBindViewHolder(ProVIewHolder proVIewHolder, final int i) {

        proVIewHolder.psrno.setText(productListModelArrayList.get(i).getSrno());
        proVIewHolder.pName.setText(productListModelArrayList.get(i).getProname());
        proVIewHolder.pPPno.setText(productListModelArrayList.get(i).getPpNo());
        proVIewHolder.psName.setText(productListModelArrayList.get(i).getsName());
        proVIewHolder.pUnitRate.setText(productListModelArrayList.get(i).getUnitRate());
        proVIewHolder.pQty.setText(productListModelArrayList.get(i).getProQty());
        proVIewHolder.pSubTotal.setText(productListModelArrayList.get(i).getProSubtotal());
        proVIewHolder.pTax.setText(productListModelArrayList.get(i).getSelectTax());
        proVIewHolder.pNetAmount.setText(productListModelArrayList.get(i).getGrandTotal());


        proVIewHolder.btnreorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Add_Product_Activity.class);
                intent.putExtra("packageData",productListModelArrayList.get(i));
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return productListModelArrayList.size();
    }

    public class ProVIewHolder extends RecyclerView.ViewHolder {

        TextView psrno,pName,pPPno,psName,pUnitRate,pQty,pSubTotal,pTax,pNetAmount;
        Button btnreorder;


        public ProVIewHolder( View itemView) {
            super(itemView);

            psrno=itemView.findViewById(R.id.proSrNo);
            pName=itemView.findViewById(R.id.pro_name);
            pPPno=itemView.findViewById(R.id.pro_ppn);
            psName=itemView.findViewById(R.id.pro_spname);
            pUnitRate=itemView.findViewById(R.id.pro_unit_rate);
            pQty=itemView.findViewById(R.id.pro_qty);
            pSubTotal=itemView.findViewById(R.id.pro_subtotal);
            pTax=itemView.findViewById(R.id.pro_tax);
            pNetAmount=itemView.findViewById(R.id.pro_net_amount);
            btnreorder=itemView.findViewById(R.id.reorderbtn);
        }
    }
}
