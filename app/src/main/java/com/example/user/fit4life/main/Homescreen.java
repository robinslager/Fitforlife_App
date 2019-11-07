package com.example.user.fit4life.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import com.example.user.fit4life.Functions.functions;
import com.example.user.fit4life.Objects.Active_user;
import com.example.user.fit4life.R;
import com.example.user.fit4life.SQL_Database.SQLdatabase;
import com.google.android.material.navigation.NavigationView;


public class Homescreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Context context;
    AlertDialog dialog;
    private String current_fraq = null;
    private FragmentManager fragmentManager;
    private functions fn;
    private View view;
    private Active_user activeUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);
        fn = new functions();
        SQLdatabase DB = new SQLdatabase(this);

        boolean check = false;
        if (!check) {
            // dialog login!
            context = this;
            dialog = new AlertDialog.Builder(context).create();
            dialog.setTitle("login Status");

            Intent intent = getIntent();
            Boolean fsync = intent.getBooleanExtra("fsync", false);
            if (fsync == false) {
                dialog.setMessage("Login Succesful");
            } else {
                dialog.setMessage("Login Succesful" + "\n" + "dowloading neccesary information in the background!");
            }
            dialog.show();
        }

        // Active_user / some usefull info
        int IDuser = getIntent().getIntExtra("UserID", 0);
//        activeUser = DB.getuser(IDuser);

        /*
        navigagiation byusing fraqments
        Be aware. activit2maindrawer hs a invisable item with menu in it.
        this for uses outside naf andfurther than the fitst activity!
         */
        fragmentManager = getSupportFragmentManager();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        view = getCurrentFocus();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menuoption = navigationView.getMenu();
        MenuItem iteminvis = menuoption.findItem(R.id.invis_menu);
        iteminvis.setVisible(false);


//        if (current_fraq != null) {
//            switch (current_fraq) {
//                case "food":
//                    MenuItem item = menuoption.findItem(R.id.nav_Food);
//                    onNavigationItemSelected(item);
//                    item.setChecked(true);
//                    break;
//
//
//            }
//
//            current_fraq = null;
//        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //calling the method displayselectedscreen and passing the id of selected menu
        fn.displaySelectedScreen(item.getItemId(), fragmentManager, this);
        //make this method blank
        return true;
    }


}

