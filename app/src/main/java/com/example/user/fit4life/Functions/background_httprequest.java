package com.example.user.fit4life.Functions;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import com.example.user.fit4life.R;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class background_httprequest {

    private final String urlweb;
    private String method;
    private ArrayList<String> var;
    private ArrayList<String> varname;
    private Context context;

    public background_httprequest(String urlweb, String method, ArrayList<String> varname, ArrayList<String> var, Context context) {

        this.urlweb = urlweb;
        if (method == "GET" || method == "POST") {
            //todo exeption!
            this.method = method;

        } else {
            this.method = null;
        }
        this.varname = varname;
        this.var = var;
        this.context = context;

    }
    // this function is a separate function because of multiple uses.
    // now if i need t use it again it takes less time.
    // can always be changed later.

    public StringBuilder connect() {




// result is for the return.
        StringBuilder result = new StringBuilder();

// data is for sending data
        StringBuilder data = new StringBuilder();


        try {

            // checks if all info isset.
            if ((var.size() == varname.size() && var.size() != 0) && method != null && urlweb != null) {

                // makes connection and set ruled for connection.
                URL url = new URL(urlweb);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();
                http.setRequestMethod(method);
                http.setDoInput(true);
                http.setDoOutput(true);

                // makes string of all post data and make writer
                // writer in this case writes the post request and sends it.
                OutputStream ops = http.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(ops, StandardCharsets.UTF_8));
                for (int i = 0; i < var.size(); i++) {

                    if (i < var.size() - 1) {
                        data.append(URLEncoder.encode(varname.get(i), "UTF-8")).append("=").append(URLEncoder.encode(var.get(i), "UTF-8")).append("&&");
                    } else {
                        data.append(URLEncoder.encode(varname.get(i), "UTF-8")).append("=").append(URLEncoder.encode(var.get(i), "UTF-8"));
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

                // closes everything inclouding the hole connection.

            } else {
                //todo exeption info not set!
                Log.e("SOME", "connect: Wrong");

            }
            // if something goes wrong error messege
        } catch (IOException e) {
            Errorhandling errorhandling = new Errorhandling(e, null, null, null);
            errorhandling.execute();
            Toast.makeText(context, R.string.system_error, Toast.LENGTH_LONG).show();
        }

        // returns result will get caught by onPostexecution.
        return result;
    }
}

