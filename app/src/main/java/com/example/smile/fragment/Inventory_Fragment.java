package com.example.smile.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.smile.R;
import com.example.smile.activities.Diet_List_Activity;
import com.example.smile.activities.Duration_PeriodActivity;
import com.example.smile.activities.Gym_Equipment_Activity;
import com.example.smile.activities.Product_List_Activity;
import com.example.smile.activities.Sell_Product_List_Activity;
import com.example.smile.activities.Stock_List_Activity;
import com.example.smile.activities.Suppliers_Activity;

public class Inventory_Fragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.fragment_inventory, container, false);

        //Gym Equipment
        {
            LinearLayout gym_equipment = (LinearLayout) view.findViewById(R.id.gym_equipment);
            gym_equipment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), Gym_Equipment_Activity.class);
                    startActivity(intent);

                }
            });
            // Diet plan
            LinearLayout diet = (LinearLayout) view.findViewById(R.id.dietPlan);
            diet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), Diet_List_Activity.class);
                    startActivity(intent);
                }
            });
            // Add product
            LinearLayout add = (LinearLayout) view.findViewById(R.id.add_product);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), Product_List_Activity.class);
                    startActivity(intent);
                }
            });
            // Sell product
            LinearLayout sell = (LinearLayout) view.findViewById(R.id.sellProduct);
            sell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), Sell_Product_List_Activity.class);
                    startActivity(intent);
                }
            });

            // Stock management
            LinearLayout stock = (LinearLayout) view.findViewById(R.id.stock);
            stock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), Stock_List_Activity.class);
                    startActivity(intent);

                }
            });


            // Suppliers
            LinearLayout suppliers = (LinearLayout) view.findViewById(R.id.suppliers);
            suppliers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), Suppliers_Activity.class);
                    startActivity(intent);

                }
            });

            return view;

        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Inventory Master");
    }
}
