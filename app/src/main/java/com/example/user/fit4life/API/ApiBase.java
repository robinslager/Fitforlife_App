package com.example.user.fit4life.API;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.example.user.fit4life.Functions.Errorhandling;
import com.example.user.fit4life.Settings;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class ApiBase extends AsyncTask<String ,Void,String> {

    private ArrayList<String> Key;
    private ArrayList<String> Value;
    private Context context;

    public String connect(Context context, String[] keys, String[] Values, String Dataway){
        // initialize array
        this.context = context;
        String result = "";
        Key = new ArrayList<>();
        Value = new ArrayList<>();
        // post mathod
        Key.add("DW");
        Value.add(Dataway);
        if(keys != null) {
            if (keys.length == Values.length) {
                for (int i = 0; i < keys.length; i++) {
                    Key.add(keys[i]);
                    Value.add(Values[i]);
                }
            }
        }
        try {
            result = execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }


    @Override
    protected String doInBackground(String... strings) {
        StringBuilder result = new StringBuilder();
        StringBuilder data = new StringBuilder();

        Settings settings = new Settings();

// data is for sending data

        try {
            // checks if all info isset.
            if ((Key.size() == Value.size())) {

                // makes connection and set ruled for connection.
                URL url = new URL(settings.getBaseServerUrl());
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod("POST");

                http.setDoInput(true);
                http.setDoOutput(true);

                // makes string of all post data and make writer
                // writer in this case writes the post request and sends it.
                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, StandardCharsets.UTF_8));
                if(Value.size() == 1){
                    data.append(URLEncoder.encode(Key.get(0), "UTF-8")).append("=").append(URLEncoder.encode(Value.get(0), "UTF-8"));
                } else {
                    if (Value.size() != 0) {
                        for (int i = 0; i < Value.size(); i++) {

                            if (i < Value.size() - 1) {
                                data.append(URLEncoder.encode(Key.get(i), "UTF-8")).append("=").append(URLEncoder.encode(Value.get(i), "UTF-8")).append("&&");
                            } else {
                                data.append(URLEncoder.encode(Key.get(i), "UTF-8")).append("=").append(URLEncoder.encode(Value.get(i), "UTF-8"));
                            }
                        }

                    }
                }

                // writes all data and sends it also closes all open objects.
                writer.write(data.toString());
                writer.flush();
                writer.close();
                ops.close();

                // tries to read the responce of the write action that just happend.
                InputStream ips = http.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(ips, StandardCharsets.UTF_8));
                String line = "";

                // you can compare this with a echo everything that gets echod of the php page gets readed and
                // converted to 1 string.
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
                ips.close();
                http.disconnect();
                Log.d(TAG, "connect: completed");

                // closes everything inclouding the hole connection.

            } else {
//                Throwable t = "Background_httprequest: Connection failer no provided information";
                //todo exeption info not set!
                new Errorhandling(context, null, null, context.getClass().toString(), null);
                Log.e("SOME", "connect: Wrong");

            }
            // if something goes wrong error messege
        } catch (IOException e) {
            Errorhandling errorhandling = new Errorhandling(context, e, null, null, null);
            errorhandling.execute();
            Log.e("SOME", "connect: Wrong");
        }

        // returns result will get caught by onPostexecution.
        return result.toString();
    }
}
