package com.example.user.fit4life.Objects.SQLobjects;

import org.json.JSONObject;

import java.util.ArrayList;

public class TableData {
    private ArrayList<SqlRow> data;
    private String Table_name;
    private JSONObject Table;

    public TableData(ArrayList<SqlRow> data, JSONObject table, String table_name) {
        this.data = data;
        Table_name = table_name;
        Table = table;
    }

    public JSONObject getTable() {
        return Table;
    }

    public ArrayList<SqlRow> getData() {
        return data;
    }

    public String getTable_name() {
        return Table_name;
    }
}
