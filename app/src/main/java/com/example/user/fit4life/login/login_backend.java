package com.example.user.fit4life.login;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.user.fit4life.API.API_Interface;
import com.example.user.fit4life.Functions.Errorhandling;
import com.example.user.fit4life.Objects.Active_user;
import com.example.user.fit4life.SQL_Database.Database_create_helper;
import com.example.user.fit4life.SQL_Database.SQLdatabase;
import com.example.user.fit4life.SQL_Database.SyncDB;
import com.example.user.fit4life.Settings;
import com.example.user.fit4life.main.Homescreen;
import org.json.JSONException;
import org.json.JSONObject;

public class login_backend{


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
    private API_Interface API;



    public login_backend(Context context, String password, SQLdatabase db) {
        this.context = context;
        this.db = db;
        this.password = password;
        settings = new Settings();
        API = new API_Interface(context);
    }

    public void Apirequest(String[] data){
        if (isNetworkAvailable()) {
            checkapiresult(API.login(data));
        } else {
            // no connection go on with offline DBcheck!
            offline_L_Status_handler();
        }
        // complete login go to home screen.
        logincomplete(logintype, userID, login, Fsync, dialog);
    }


    private void checkapiresult(String result){
        JSONObject json;
        // transforms string to JSON format

        // result is only null if no something went wrong
        if(result != null) {
            try {
                json = new JSONObject(result);

                // logs JSON check if works
                Log.d("All Products: ", json.toString());

                // set check true for later.
                online_L_Status_handler(json);
            } catch (JSONException e) {
                Errorhandling errorhandling = new Errorhandling(context, null, null, null, e);
                errorhandling.execute();
            }
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
                        // is a function that wil sync database values with current local db.
                        new SyncDB(this.context, "F_login", json.getString("userID"), db);
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
            new Database_create_helper(context, db, db.getReadableDatabase(), db.getWritableDatabase()).refresh_db_jsonfile();

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

