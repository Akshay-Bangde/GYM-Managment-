package com.example.smile.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.smile.fragment.General_Fragment;
import com.example.smile.fragment.Questionnaire_Fragment;

public class AddMedicalHealthViewPagerAdapter extends FragmentPagerAdapter {
    ViewPager viewPager;

    public AddMedicalHealthViewPagerAdapter(FragmentManager fm, ViewPager viewPager) {
        super(fm);
        this.viewPager = viewPager;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        if (position == 0) {
            fragment = new General_Fragment(viewPager);
        } else if (position == 1) {
            fragment = new Questionnaire_Fragment(viewPager);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if (position == 0) {
            title = "General";
        } else if (position == 1) {
            title = "Questionnaires";
        }
        return title;
    }

    }
