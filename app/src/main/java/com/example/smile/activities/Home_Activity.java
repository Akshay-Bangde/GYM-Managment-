package com.example.smile.activities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smile.R;
import com.example.smile.fragment.Accounting_Fragment;
import com.example.smile.fragment.DietTrainingSchedule_Fragment;
import com.example.smile.fragment.Inventory_Fragment;
import com.example.smile.fragment.Registration_Fragment;
import com.example.smile.fragment.Setting_Fragment;

import static com.example.smile.activities.SignInActivity.jname;
import static com.example.smile.activities.SignInActivity.jusername;

public class Home_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    long back_pressed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hview=navigationView.getHeaderView(0);

        TextView text_name=hview.findViewById(R.id.txt_name);
        TextView text_username=hview.findViewById(R.id.txt_username);

        text_name.setText(jname);
        text_username.setText(jusername);

        displaySelectedScreen(R.id.nav_registration);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
       }

    private void displaySelectedScreen(int itemId) {
        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_registration:
                fragment = new Registration_Fragment();
                break;
            case R.id.nav_setting:
                fragment = new Setting_Fragment();
                break;
            case R.id.nav_inventory:
                fragment = new Inventory_Fragment();
                break;
            case R.id.nav_accounting:
                fragment = new Accounting_Fragment();
                break;

            case R.id.nav_dietTrainingSchedule:
                fragment = new DietTrainingSchedule_Fragment();
                break;
            case R.id.nav_enquiry:
                Intent intent= new Intent(Home_Activity.this, Enquiry_Activity.class);
                startActivity(intent);


        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(back_pressed + 2000 >System.currentTimeMillis()){
            super.onBackPressed();
        }
        else{
            Toast.makeText(getBaseContext(), getResources().getString(R.string.Press_once_again_to_exit), Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    /*@Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.getItem(0).setIcon(R.drawable.registration_nav);
        menu.getItem(1).setIcon(R.drawable.setting);
        menu.getItem(2).setIcon(R.drawable.inventory);
        menu.getItem(3).setIcon(R.drawable.accounting);
   *//*     if(menu.findItem(R.id.nav_registration) != null){
            menu.findItem(R.id.nav_registration).setIcon(R.drawable.registration_nav);
        }
*//*
        return super.onPrepareOptionsMenu(menu);
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home_Activity/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatementif

        if (id == R.id.profile) {
            Intent intent = new Intent(Home_Activity.this,ProfileActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.help) {
            return true;
        } else if (id == R.id.logout) {
            final Dialog dialog=new Dialog(Home_Activity.this);
            dialog.setContentView(R.layout.log_out_alert_dialog_box);
            TextView mesg = dialog.findViewById(R.id.message);
            mesg.setText("Do you want to logout ?");
            Button nobtn=dialog.findViewById(R.id.text_no);
            Button yesbtn=dialog.findViewById(R.id.text_yes);

            nobtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            yesbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent =new Intent(Home_Activity.this, SignInActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();


    }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());

        //make this method blank
        return true;
        }
    }

