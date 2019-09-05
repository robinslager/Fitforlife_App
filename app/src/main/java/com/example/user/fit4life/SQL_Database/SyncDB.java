package com.example.user.fit4life.SQL_Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import com.example.user.fit4life.Functions.background_httprequest;
import com.example.user.fit4life.Objects.Active_user;
import com.example.user.fit4life.Settings;
import com.example.user.fit4life.login.login_backend;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


public class SyncDB extends AsyncTask<String ,Void,String>  {
    private static final String TAG = "a";
    private SQLdatabase db;
    private String Syncmethod;
    private String Action;
    private String UserID;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private Settings settings;



    public SyncDB(Context context, String syncmethod, String action, String userID, SQLdatabase dbs) {
        Syncmethod = syncmethod;
        Action = action;
        UserID = userID;
        this.context = context;
        db = dbs;
        settings = new Settings();
    }

    @Override
    protected void onPreExecute() {

    }



    @Override
    protected String doInBackground(String... voids) {

        String httpURL = settings.getBaseServerUrl() + "index.php?DW=" + Syncmethod;

        ArrayList<String> varname = new ArrayList<>();
        varname.add("user");
        ArrayList<String> var = new ArrayList<>();
        var.add(UserID);

        return new background_httprequest(httpURL, Action, varname, var, context).connect().toString();
    }

    @Override
    protected void onPostExecute(String s) {
        JSONObject data;
        try {
            // Data Live db!
            data = new JSONObject(s);
            JSONArray users = data.getJSONArray("users");
            Log.d("array", data.toString());

            ContentValues cv = new ContentValues();
            JSONObject user = users.getJSONObject(0);

            cv.put("ID", user.getInt("ID"));
            cv.put("Klas_ID", user.getInt("klas_ID"));
            cv.put("Email", user.getString("email"));
            cv.put("Wachtwoord", user.getString("password"));
            cv.put("Voornaam", user.getString("voornaam"));
            cv.put("Achternaam", user.getString("achternaam"));
            cv.put("Profielfoto", user.getString("profielafbeelding"));
            cv.put("Rol", user.getInt("rol"));
//            cv.put("Account_Verwijderd", user.getString("verwijderd"));
            cv.put("Wachtwoord_Wijzig", user.getInt("wachtwoord_veranderen_bij_login"));
            try {
                db.insertdata("Users", cv);
            }
            catch (Throwable t){

            }
//            if(rowInserted != -1) {
////Insert success.
//            } else {
////Inser failed.
//            }



        } catch (JSONException e) {
            Log.e("My App", "Could not parse malformed JSON: \"" + s + "\"");
            e.printStackTrace();
        }

    }

}

