package com.example.user.fit4life.Functions;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;
import com.example.user.fit4life.R;
import com.example.user.fit4life.Settings;
import org.json.JSONException;

import java.io.*;


import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;


public class Errorhandling extends AsyncTask<String, Void, String> {

    private IOException err_io = null;
    private Throwable err_trow = null;
    private String err_info = null;
    private JSONException error_json = null;
    private String current_err;
    private Settings settings;
    Context context;


    public Errorhandling(Context context, IOException err_io, Throwable err_trow, String err_info, JSONException error_json) {
        this.err_io = err_io;
        this.err_trow = err_trow;
        this.err_info = err_info;
        this.error_json = error_json;
        this.context = context;
        settings = new Settings();

    }

    @Override
    protected void onPreExecute() {
        Toast.makeText(context, R.string.system_error, Toast.LENGTH_LONG).show();
    }

    @Override
    protected String doInBackground(String... strings) {
        if (err_io != null) {
            current_err = "IO exeption";
        } else if (err_trow != null) {
            current_err = "throwable";
        } else if (error_json != null) {
            current_err = "JSON";
        }
        String remoteFile = settings.getBaseServerUrl() +  "error_writer.php";
        StringBuilder result = new StringBuilder();
        try {
            // makes input with
            URL url = new URL(remoteFile);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoInput(true);
            http.setDoOutput(true);
            // sends data to php file.
            OutputStream ops = http.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, StandardCharsets.UTF_8));
            String err_srting = URLEncoder.encode("key", "UTF-8") + "=" + URLEncoder.encode("asubordh", "UTF-8") + "&&" +
                    URLEncoder.encode("error-type", "UTF-8") + "=" + URLEncoder.encode(current_err, "UTF-8") + "&&";

            // checks for current error`s
            if (current_err == "IO exeption") {
                String cause = Arrays.toString(err_io.getStackTrace());
                String message = err_io.getMessage();
                err_srting = err_srting + URLEncoder.encode("message", "UTF-8") + "=" + URLEncoder.encode(message, "UTF-8") + "&&" +
                        URLEncoder.encode("cause", "UTF-8") + "=" + URLEncoder.encode(cause, "UTF-8");
            } else if (current_err == "throwable") {
                String cause = Arrays.toString(err_trow.getStackTrace());
                String message = err_trow.getMessage();

                err_srting = err_srting + URLEncoder.encode("cause", "UTF-8") + "=" + URLEncoder.encode(cause, "UTF-8") + "&&" +
                        URLEncoder.encode("message", "UTF-8") + "=" + URLEncoder.encode(message, "UTF-8") + "&&" +
                        URLEncoder.encode("extra", "UTF-8") + "=" + URLEncoder.encode(err_info, "UTF-8");
            } else if (current_err == "JSON") {
                String message = error_json.getMessage();
                String stacktrace = Arrays.toString(error_json.getStackTrace());
                err_srting = err_srting + URLEncoder.encode("message", "UTF-8") + "=" + URLEncoder.encode(message, "UTF-8") + "&&" +
                        URLEncoder.encode("cause", "UTF-8") + "=" + URLEncoder.encode(stacktrace, "UTF-8");
            }
            // write current error in txt file on server
            writer.write(err_srting);
            writer.flush();
            writer.close();
            ops.close();
            // returns result from php file
            InputStream ips = http.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(ips, StandardCharsets.UTF_8));
            String line = "";
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
            ips.close();
            http.disconnect();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result.toString();
    }
}