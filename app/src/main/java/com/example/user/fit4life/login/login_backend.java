package com.example.user.fit4life.login;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.user.fit4life.Functions.Errorhandling;
import com.example.user.fit4life.Functions.background_httprequest;
import com.example.user.fit4life.Objects.Active_user;

import com.example.user.fit4life.SQL_Database.SQLdatabase;
import com.example.user.fit4life.SQL_Database.SyncDB;
import com.example.user.fit4life.Settings;
import com.example.user.fit4life.main.Homescreen;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class login_backend extends AsyncTask<String ,Void,String> {


    private Context context;
    private AlertDialog dialog;
    private SQLdatabase db;
    private String password;
    private Boolean login = false;
    private Boolean logintype;
    private Settings settings;
    private int userID;
    private boolean Fsync = false;
    private Active_user activeUser;
    private SyncDB sync;
    public volatile static boolean DBcomplete = false;



    public login_backend(Context context, String password, SQLdatabase db) {
        this.context = context;
        this.db = db;
        this.password = password;
        settings = new Settings();
    }
    @Override
    protected void onPreExecute() {
        // creates dialog.
        dialog = new AlertDialog.Builder(context).create();
        dialog.setTitle("login Status");

    }

    /**
     *
     * @param s is result of function doinbackground gives
     * is a json object in string format
     *
     *          it checks if you have connection
     *          if conection is avail able it checks if
     */
    @Override
    protected void onPostExecute(String s) {


        JSONObject json;
            // transforms string to JSON format

        // s is only null if no connection!
        if(s != null) {
            try {
                json = new JSONObject(s);

                // logs JSON check if works
                Log.d("All Products: ", json.toString());

                // set check true for later.
                online_L_Status_handler(json);
            } catch (JSONException e) {
                Errorhandling errorhandling = new Errorhandling(context, null, null, null, e);
                errorhandling.execute();
            }
        } else {
            // no connection go on with offline DBcheck!
            offline_L_Status_handler();
        }
        // complete login go to home screen.
        logincomplete(logintype, userID, login, Fsync, dialog);
    }

    // post login info
    @Override
    protected String doInBackground(String... voids) {
        // make strings data for post request.

        //  checks for connection.
        if (isNetworkAvailable()) {

            String email = voids[0];
            String password = voids[1];

            ArrayList<String> varname = new ArrayList<>();
            varname.add("email");
            varname.add("password");
            ArrayList<String> var = new ArrayList<>();
            var.add(email);
            var.add(password);
            // post url

            String httpURL = settings.getBaseServerUrl() +  "/login.php";
            // post method
            background_httprequest http = new background_httprequest(httpURL, "POST", varname, var, context);
            // return result.
            return http.connect().toString();
        } else {
            return null;
        }

    }
    private void online_L_Status_handler(JSONObject json){
        logintype = true;
            try {
                // get if succeeded from PHP json
                // succes = 1 if login is correct.
                int succes = json.getInt("succes");
                if (succes == 1) {
                    int flogin = json.getInt("f_login");
                    login = true;
                    Fsync = true;
                    if (flogin == 1) {

                        // if first login sync local DB.
                        sync = new SyncDB(this.context, "F_login", "POST", json.getString("userID"), db);
                        // userID + syncway
                        sync.execute();

                    }

                } else {

                }
                // user id is string from json.
                userID = Integer.parseInt(json.getString("userID"));
                // catch is needed but its unpractical ^ onpostexec will do this already.
            } catch (JSONException e) {
                Errorhandling errorhandling = new Errorhandling(context,null, null, null, e);
                errorhandling.execute();
            }

    }
    private void offline_L_Status_handler(){
        logintype = false;
        if(db.getpass() != null) {
            BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), db.getpass().toCharArray());
            if(result.verified) {
                login = true;
                userID = db.getuserID();
                result = null;
            }
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    void logincomplete(boolean logintype, int Userid, boolean logincheck, boolean User_first_sync, AlertDialog dialog){
        // true is login succesful
        //TODO login log for extra userinfo. to server


            // My AsyncTask is done and onPostExecute was called
            if(logincheck){
                // new intent here!!!
                Intent intent = new Intent(context, Homescreen.class);
                intent.putExtra("UserID", Userid);
                intent.putExtra("fsync", User_first_sync);
//                intent.putExtra("Active_User_enc", activeUser.getencoded());
                context.startActivity(intent);
            }
            // false is login failed.
            else {
                // different type of message depending of login online or offline
                if(logintype == true){
                    dialog.setMessage("Email or password incorrect.");
                } else {
                    dialog.setMessage("Email or password incorrect." + "\n" +
                            "If this is your first time logging in make sure you have a stable connection.");
                }
                dialog.show();
            }

        }



}

