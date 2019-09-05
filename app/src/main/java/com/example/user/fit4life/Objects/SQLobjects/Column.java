package com.example.user.fit4life.Objects.SQLobjects;

import android.util.Log;

import java.util.Objects;

import static android.support.constraint.Constraints.TAG;

public class Column {
    private String Column_name;
    private String Column_Type;
    private String Column_Option_Action;
    private String Column_Option_default;

    public Column(String column_name, String column_Type, String column_Option_Action, String column_Option_default) {

        Column_name = column_name;
        Column_Type = column_Type;
        Column_Option_Action = column_Option_Action;
        Column_Option_default = column_Option_default;
    }


    public String Create_Column_Sqlline(){
        String Sqlline = "";
        Sqlline += Column_name +" ";
        Sqlline += Column_Type + " ";
        if(Column_Option_Action.length() != 0){
            if(Objects.equals(Column_Option_Action, "NN")){
                Sqlline += "NOT NULL ";
            }
        }
        if(Column_Option_default.length() != 0){
            Sqlline += "DEFAULT " + Column_Option_default;
        }
//        Log.i(TAG, "Create_Column_Sqlline: " + Sqlline);
        return Sqlline;

    }
}
