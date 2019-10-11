package com.example.user.fit4life.API;

import android.content.Context;

public class API_Interface extends ApiBase {

    Context context;

    public API_Interface(Context context) {
        this.context = context;
    }

    public String login(String[] Values){
        String result = "";
        String[] Keys = new String[2];
        Keys[0] = "email";
        Keys[1] = "password";
        result = this.connect(context, Keys, Values, "Login");
        return result;
    }

    public String All_Data_retrieve(String[] Values){
        String result = "";
        String[] Keys = new String[1];
        Keys[0] = "user";
        result = this.connect(context, Keys, Values, "F_login");
        return result;
    }

    public String get_db_file(){
        String result = "";
        String[] Keys = new String[1];
        result = this.connect(context, null, null, "getfile");
        return result;
    }
}
