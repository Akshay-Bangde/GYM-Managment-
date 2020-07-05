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
import com.example.smile.activities.Duration_PeriodActivity;
import com.example.smile.activities.GymActivityList_Activity;
import com.example.smile.activities.Package_List_Activity;
import com.example.smile.activities.ProfileActivity;
import com.example.smile.activities.Workout_Exercise_list_Activity;
import com.example.smile.activities.Workout_CategoryActivity;

public class Setting_Fragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        //Duration Period
        {
            LinearLayout duration_period = (LinearLayout)view.findViewById(R.id.duration_period);
            duration_period.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent duration_period = new Intent(getActivity(), Duration_PeriodActivity.class);
                    startActivity(duration_period);

                }
            });

            // WorkOut Category
            LinearLayout workout_catergory = (LinearLayout)view.findViewById(R.id.workout_category);
            workout_catergory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), Workout_CategoryActivity.class);
                    startActivity(intent);
                }
            });

            //Gym Activity
            final LinearLayout gym_activity = (LinearLayout)view.findViewById(R.id.gym_activity);
           gym_activity.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent = new Intent(getActivity(), GymActivityList_Activity.class);
                   startActivity(intent);
               }
           });
            //Add Package
            final LinearLayout add_package = (LinearLayout)view.findViewById(R.id.gym_package);
            add_package.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), Package_List_Activity.class);
                    startActivity(intent);
                }
            });



            //Workout Exercise
            LinearLayout workout_exercise = (LinearLayout)view.findViewById(R.id.workout_exercise);
            workout_exercise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), Workout_Exercise_list_Activity.class);
                    startActivity(intent);
                }
            });

            //Gym profile
            LinearLayout profile = (LinearLayout)view.findViewById(R.id.Gymprofile);
            profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ProfileActivity.class);
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
        getActivity().setTitle("Setting Master");
    }


}
