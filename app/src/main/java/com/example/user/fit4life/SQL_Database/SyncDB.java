package com.example.user.fit4life.SQL_Database;

import android.annotation.SuppressLint;
import android.content.Context;
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


public class SyncDB extends AsyncTask<String ,Void,String>  {
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
            String[] userdata = new String[13];
            JSONObject user = users.getJSONObject(0);

            userdata[0] = user.getString("ID");
            userdata[1] = user.getString("klas_ID");
            userdata[2] = user.getString("email");
            userdata[3] = user.getString("password");
            userdata[4] = user.getString("voornaam");
            userdata[5] = user.getString("achternaam");
            userdata[6] = user.getString("leerlingnummer");
            userdata[7] = user.getString("opleiding");
            userdata[8] = user.getString("rol");
            userdata[9] = user.getString("verwijderd");
            userdata[10] = user.getString("verwijderd_datum");
            userdata[11] = user.getString("wachtwoord_veranderen_bij_login");
            userdata[12] = user.getString("profielafbeelding");

            db.insertdata("user", userdata);
            int Userid = Integer.parseInt(user.getString("ID"));

        } catch (JSONException e) {
            Log.e("My App", "Could not parse malformed JSON: \"" + s + "\"");
            e.printStackTrace();
        }

    }

}

