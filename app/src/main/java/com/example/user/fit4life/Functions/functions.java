package com.example.user.fit4life.Functions;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import com.example.user.fit4life.R;
import com.example.user.fit4life.main.Food.food_cal_eat_fraq;
import com.example.user.fit4life.main.Food.food_fraq;
import com.example.user.fit4life.main.home.home_fraq;
import com.example.user.fit4life.main.tasks.Tasks_fraq;

public class functions {


    public void displaySelectedScreen(int itemId, FragmentManager fm, Activity activity) {

        //creating fragment object
        Fragment fragment = null;

        //initializing the fragment object which is selected
        switch (itemId) {
            // Handle navigation view item clicks here.


            case R.id.nav_home:
                fragment = new home_fraq();

                break;
            case R.id.nav_tasks:
                fragment = new Tasks_fraq();
                break;

            case R.id.nav_Food:
                fragment =  new food_fraq();
                break;
            case R.id.nav_progress:

                break;

            case R.id.nav_share:

                break;

            case R.id.nav_send:

                break;
            case R.id.nav_call_eat:
                fragment = new food_cal_eat_fraq();
                break;
        }
        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = activity.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

}
