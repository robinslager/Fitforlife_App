package com.example.user.fit4life.Objects.SQLobjects;

import android.util.Log;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class SqlRow {
    private ArrayList<String> Collumn_names;
    private ArrayList<String> Collumn_values;
    private ArrayList<String> Collumn_type;
    private String Table_Name;
    public SqlRow(ArrayList<String> collumn_names, ArrayList<String> collumn_values, ArrayList<String> collumn_type , String table_Name) {
        if(collumn_names.size() == collumn_values.size()) {
            this.Collumn_names = collumn_names;
            this.Collumn_values = collumn_values;
            if(collumn_values.size() == collumn_type.size()){
                this.Collumn_type = collumn_type;
            }else {
                Log.e(TAG, "SqlRow: collumntype has not same amount of columns as the rest of the row");
            }

        } else {
            Log.e(TAG, "SqlRow: columnname and value have nut equal amount of values / Size in them");
        }
        Table_Name = table_Name;
    }

    public ArrayList<String> getCollumn_names() {
        return Collumn_names;
    }

    public ArrayList<String> getCollumn_values() {
        return Collumn_values;
    }

    public ArrayList<String> getCollumn_type() {
        return Collumn_type;
    }
}
