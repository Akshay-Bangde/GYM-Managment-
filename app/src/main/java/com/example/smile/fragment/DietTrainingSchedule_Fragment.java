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
import com.example.smile.activities.AddMedicalHealth_Activity;
import com.example.smile.activities.FoodCategory_Activity;
import com.example.smile.activities.FoodEntryList_Activity;
import com.example.smile.activities.Physiotherapy_Treatment_Activity;
import com.example.smile.activities.Trainer_Employee_Activity;
import com.example.smile.activities.TrainingSchedule_Activity;
import com.example.smile.activities.WaterIntakeList_Activity;


public class DietTrainingSchedule_Fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_diet_training_schedule, container, false);

        LinearLayout trainingSchedule = (LinearLayout) view.findViewById(R.id.trainingSchedule);
        trainingSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent trainingSchedule = new Intent(getActivity(), TrainingSchedule_Activity.class);
                startActivity(trainingSchedule);

            }
        });

        LinearLayout foodCategory = (LinearLayout) view.findViewById(R.id.foodCategory);
        foodCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FoodCategory_Activity.class);
                startActivity(intent);

            }
        });
        LinearLayout foodEntry = (LinearLayout) view.findViewById(R.id.foodEntry);
        foodEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FoodEntryList_Activity.class);
                startActivity(intent);

            }
        });


        LinearLayout waterIntake = (LinearLayout) view.findViewById(R.id.waterIntake);
        waterIntake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WaterIntakeList_Activity.class);
                startActivity(intent);

            }
        });

        LinearLayout medicalHealth = (LinearLayout) view.findViewById(R.id.medicalhealth);
        medicalHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddMedicalHealth_Activity.class);
                startActivity(intent);

            }
        });

        LinearLayout physiotherepy = (LinearLayout) view.findViewById(R.id.physiotherapytreatmentt);
        physiotherepy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Physiotherapy_Treatment_Activity.class);
                startActivity(intent);

            }
        });

    return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Diet/Training Schedule");
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



}
