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
import com.example.smile.activities.BalanceSheet_Activity;
import com.example.smile.activities.Expense_List_Activity;
import com.example.smile.activities.Gym_Equipment_Activity;
import com.example.smile.activities.Member_Payment_Report_Activity;
import com.example.smile.activities.Package_Payment_History_Activity;
import com.example.smile.activities.Salary_List_Activity;
import com.example.smile.activities.Transaction_Report_Activity;

public class Accounting_Fragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.fragment_accounting, container, false);
        {
            // Expense Master
            LinearLayout gym_equipment = (LinearLayout) view.findViewById(R.id.expenseList);
            gym_equipment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), Expense_List_Activity.class);
                    startActivity(intent);

                }
            });

            // Package Payment History
            LinearLayout payment = (LinearLayout) view.findViewById(R.id.paymentHistory);
            payment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), Package_Payment_History_Activity.class);
                    startActivity(intent);

                }
            });
            // Salary Management
            LinearLayout salary = (LinearLayout) view.findViewById(R.id.salaryManage);
            salary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), Salary_List_Activity.class);
                    startActivity(intent);

                }
            });
            // Membre paymnet Report
            LinearLayout MemberReport = (LinearLayout) view.findViewById(R.id.memberPaymentReport);
            MemberReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), Member_Payment_Report_Activity.class);
                    startActivity(intent);

                }
            });
            // Transaction Report
            LinearLayout TReport = (LinearLayout) view.findViewById(R.id.TranReport);
            TReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), Transaction_Report_Activity.class);
                    startActivity(intent);

                }
            });
            // Transaction Report
            LinearLayout balanceSheet = (LinearLayout) view.findViewById(R.id.balanceSheet);
            balanceSheet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), BalanceSheet_Activity.class);
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
        getActivity().setTitle("Accounting Master");
    }
}
