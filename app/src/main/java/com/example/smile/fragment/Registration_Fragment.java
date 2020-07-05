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
import com.example.smile.activities.Employee_AttendanceActivity;
import com.example.smile.activities.Member_AttendanceActivity;
import com.example.smile.activities.MemberGroupListActivity;
import com.example.smile.activities.Member_ListActivity;
import com.example.smile.activities.Renew_Activity;
import com.example.smile.activities.Trainer_Employee_Activity;

public class Registration_Fragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.fragment_registeration, container, false);
        //Trainer_Employee_Activity
        {
            LinearLayout trainer_employee = (LinearLayout) view.findViewById(R.id.trainer_employee);
            trainer_employee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent trainer_employee = new Intent(getActivity(), Trainer_Employee_Activity.class);
                    startActivity(trainer_employee);

                }
            });

        }

        //Renew_Activity
        {
            LinearLayout renew = (LinearLayout) view.findViewById(R.id.renew);
            renew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent renew = new Intent(getActivity(), Renew_Activity.class);
                    startActivity(renew);

                }
            });

        }
        //Member_Group Activity
        {
            LinearLayout member_group = (LinearLayout) view.findViewById(R.id.member_group);
            member_group.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent member_group = new Intent(getActivity(), MemberGroupListActivity.class);
                    startActivity(member_group);
                }
            });

        }
        // Member Zist
        LinearLayout memberList = (LinearLayout) view.findViewById(R.id.member);
        memberList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent memberList = new Intent(getActivity(), Member_ListActivity.class);
                startActivity(memberList);
            }
        });

        //Member Attendance List
        LinearLayout member_attendance = (LinearLayout) view.findViewById(R.id.member_attendance);
        member_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent member_attendance = new Intent(getActivity(), Member_AttendanceActivity.class);
                startActivity(member_attendance);
            }
        });

        // Employee Attendance
        LinearLayout employee_attendance = (LinearLayout) view.findViewById(R.id.employee_attendance);
        employee_attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent employee_attendance = new Intent(getActivity(), Employee_AttendanceActivity.class);
                startActivity(employee_attendance);
            }
        });

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Registration Master");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
