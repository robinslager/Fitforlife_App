package com.example.user.fit4life.SQL_Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import com.example.user.fit4life.API.API_Interface;
import com.example.user.fit4life.Settings;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class SyncDB  {
    private static final String TAG = "a";
    private SQLdatabase db;
    private String Syncmethod;
    private String Action;
    private String UserID;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private Settings settings;
    private API_Interface API;



    public SyncDB(Context context, String syncmethod, String userID, SQLdatabase dbs) {
        Syncmethod = syncmethod;

        UserID = userID;
        this.context = context;
        db = dbs;
        settings = new Settings();
        API = new API_Interface(context);
    }

    private void startsync(){
        String result = "";
        String[] data = new String[5];
        switch (Syncmethod) {
            case "F_login":
                data[0] = UserID;
                result = API.All_Data_retrieve(data);
                break;
            case "getfile":
                result = API.get_db_file(data);
                break;
            default:
                break;
        }
        continiuesync(Syncmethod, result);

    }

    private void continiuesync(String Syncmeth, String res){
        switch (Syncmeth){
            case "F_login":
                F_login(res);
                break;
            case "getfile":
                write_db_file(res);
                break;
        }
    }


    public void F_login(String result){
        JSONObject data;
        try {
            // Data Live db!
            data = new JSONObject(result);
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


        } catch (JSONException e) {
            Log.e("My App", "Could not parse malformed JSON: \"" + result + "\"");
            e.printStackTrace();
        }
    }
    private void write_db_file(String json) {
        File directory = new File(context.getFilesDir()+File.separator+"files");
        String check = json;
        if(!directory.exists())
            directory.mkdir();
            directory.setReadable(true);
            directory.setWritable(true);
            directory.setExecutable(true);


        File newFile = new File(directory, "db.json");

        if(!newFile.exists()){
            try {
                newFile.createNewFile();
                newFile.setReadable(true);
                newFile.setWritable(true);
                newFile.setExecutable(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try  {
            FileOutputStream fOut = new FileOutputStream(newFile);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fOut);
            outputWriter.write(json);
            outputWriter.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

