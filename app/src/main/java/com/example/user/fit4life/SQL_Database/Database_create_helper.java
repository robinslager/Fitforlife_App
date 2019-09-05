package com.example.user.fit4life.SQL_Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;
import com.example.user.fit4life.Functions.background_httprequest;
import com.example.user.fit4life.Objects.SQLobjects.Column;
import com.example.user.fit4life.Objects.SQLobjects.SqlRow;
import com.example.user.fit4life.Objects.SQLobjects.TableData;
import com.example.user.fit4life.Settings;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;

import static android.support.constraint.Constraints.TAG;
import static com.example.user.fit4life.Functions.functions.persistDate;

public class Database_create_helper {

    private JSONObject table = null;
    private SQLdatabase db;
    private SQLiteDatabase writeabledb;
    private SQLiteDatabase readabledb;
    private boolean First_exeq;
    private Context context;
    private static final String TABLE_TABLES = "TABLES";
    private static final String T_col1 = "Table_Name";
    private static final String T_col2 = "Collumnames";
    private static final String T_col3 = "Extra";

    private static final String TABLE_VERSIONS = "Migrate";
    private static final String V_col1 = "Version";
    private static final String V_col2 = "data";
    private static final String V_col3 = "Date";
    private static final String V_col4 = "Current";

    /**
     * @param context context from SQLdatabase
     * @param db      database
     *                <p>
     *                helps creating database from a single json file.
     */
    public Database_create_helper(Context context, SQLdatabase db, SQLiteDatabase read, SQLiteDatabase write) {
        this.db = db;
        this.writeabledb = write;
        this.readabledb = read;
        this.context = context;
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void Start() {
        JSONObject Tables = null;
        try {
            Tables = new JSONObject(loadJSONFromAsset(context));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Cursor Result;
        /*
        tries checking if there anything in tables Tables
        if not this sugest that there is no migration exequted.
        try is used becuase on instalation none of the tables are created.
        also it wil read anything in json file and check for diffrences.
         */
        try {
            Initialization();
        } catch (Throwable t) {
            Log.i(TAG, "Start: are already defined");
        } finally {
            Result = readabledb.rawQuery("SELECT * FROM TABLES", null);
        }

        if (Result.getCount() == 0) {
            First_exeq = true;

            // if is null first get tables in db for migrate purposes
            Extract_Json_file_to_migrate(Tables);
            Execute_current_migration(Tables);
        } else if (Result.getCount() == 1) {
            // check if current is available / small check for the tables
//             if table not available than exeq table migration
            Execute_current_migration(Tables);
            First_exeq = false;
        }
        First_exeq = false;
    }

    // creates first 2 necessary Tables
    private void Initialization() {
        writeabledb.execSQL("CREATE TABLE " + TABLE_TABLES +
                " (" +
                T_col1 + " TEXT," +
                T_col2 + " TEXT," +
                T_col3 + " TEXT )");
        writeabledb.execSQL("CREATE TABLE " + TABLE_VERSIONS +
                " (" +
                V_col1 + " INTEGER," +
                V_col2 + " TEXT," +
                V_col3 + " BLOB," +
                V_col4 + " INTEGER Default 0)");
    }

    // reads json file and returs json object
    private String loadJSONFromAsset(Context context) {
        String json = "";
        File directory = new File(context.getFilesDir()+File.separator+"files");

        if(!directory.exists()) {
            directory.mkdir();
        }

        File newFile = new File(directory, "db.json");

        if(!newFile.exists()){
            try {
                InputStream is = context.getAssets().open("db.json");

                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer);
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
        } else {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(newFile));

                try {
                    StringBuilder sb = new StringBuilder();
                    String line = br.readLine();

                    while (line != null) {
                        sb.append(line);
                        sb.append(System.lineSeparator());
                        line = br.readLine();
                    }
                    json = sb.toString();
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        return json;
    }

    // makes a table save / migration from json file
    private Boolean Extract_Json_file_to_migrate(JSONObject Tables) {
        // first time if default db migration fails
        if (First_exeq) {
            ContentValues cv = new ContentValues();
            cv.put("Version", 2);
            cv.put("data", Tables.toString());
            cv.put("Date", persistDate(new Date()));
            cv.put("Current", 1);
            db.insertdata("Migrate", cv);
            Log.i(TAG, "Extract_Json_file_to_migrate: Completed");
            // return true when db is different from previous one.
            return false;
        } else {
            // checks for diffrences in data base if there is a diffrence than make new migration
            Cursor result = db.Query("SELECT * FROM Migrate");
            if (result.getCount() == 0) {
                Log.e(TAG, "Something went wrong in Extract_Json_file_to_migrate");
                return false;
            } else {
                result.moveToLast();
                String dataresult = result.getString(1);
                Integer versionresult = result.getInt(0);
                if (dataresult.equals(Tables.toString())) {
                    return false;
                } else {
                    ContentValues cv = new ContentValues();
                    cv.put("Version", versionresult + 1);
                    cv.put("data", Tables.toString());
                    cv.put("Date", persistDate(new Date()));
                    cv.put("Current", 1);
                    db.insertdata("Migrate", cv);

                    writeabledb.execSQL("UPDATE Migration SET Current = 0 WHERE Version = " + versionresult);

                    return true;
                }
            }
        }
    }

    private void Execute_current_migration(JSONObject current_Table_data) {
        try {
            JSONArray tables = current_Table_data.getJSONArray("Tables");
            for (int i = 0; i < tables.length(); i++) {
                JSONObject Table = tables.getJSONObject(i);
                JSONArray Columns = Table.getJSONArray("Columns");
                // makes the table
                CreateTable(Table);
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
    }

    private void CreateTable(JSONObject Table) {
        /*
        creates a single table from a json object.
        reason for a special function is because we need it in checkDBchanges!
         */
        String TABLE_NAME = "";
        String Query;
        JSONObject columnobject = null;
        ArrayList<Column> Columnlist = new ArrayList<>();
        ContentValues cv = new ContentValues();
        try {
            JSONArray Columns = Table.getJSONArray("Columns");
            TABLE_NAME = Table.getString("Tablename");
            Query = "CREATE TABLE " + TABLE_NAME + "(";
            // loop through all columns and make from every column a column object.
            for (int j = 0; j < Columns.length(); j++) {
                columnobject = Columns.getJSONObject(j);

                String Column_Name = columnobject.getString("Colname");
                String Column_Type = columnobject.getString("Type");
                String Column_Action = columnobject.getString("Action");
                String Column_Default = columnobject.getString("Default");

                Column column = new Column(Column_Name, Column_Type, Column_Action, Column_Default);
                if (j != Columns.length() - 1) {
                    Query += column.Create_Column_Sqlline() + ",";
                } else {
                    Query += column.Create_Column_Sqlline();
                }
                Log.i(TAG, "Execute_current_migration: " + Columnlist);
            }
            Query += ")";
            try {
                writeabledb.execSQL(Query);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Throwable t) {
                Log.e(TAG, "something went wrong with creating");
            }
            cv.put("Table_Name", TABLE_NAME);
            cv.put("Collumnames", Columns.toString());
            db.insertdata("TABLES", cv);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void checkDBchanges() {
        String TABLE_NAME = "";
        Cursor result;
        ArrayList<TableData> tableData = new ArrayList<>();
        result = db.Query("SELECT * FROM TABLES");
        ArrayList<JSONObject> newtables = new ArrayList<>();

        if (result.getCount() == 0) {
            Log.e(TAG, "checkDBchanges has no tables found");
        } else {
            try {
                // reads json file and checks for diffrences
                JSONObject Tablesobject = new JSONObject(loadJSONFromAsset(context));
                boolean Tablecheck = Extract_Json_file_to_migrate(Tablesobject);
                // tablecheck returns true if json file has changed.
                if (Tablecheck) {
                    JSONArray tables = Tablesobject.getJSONArray("Tables");
                    // Loop through all tables in TABLES table.
                    for (int i = 0; i < tables.length(); i++) {
                        ArrayList<SqlRow> rows = new ArrayList<>();
                        JSONObject Table = tables.getJSONObject(i);
                        TABLE_NAME = Table.getString("Tablename");
                        JSONArray Columns = Table.getJSONArray("Columns");
                        // if json file has been changed. we check which tables are diffrent looking only at collumns.
                        // if there is a chancge in collumns part we want to save all data of that table
                        result = db.Query("SELECT * FROM TABLES WHERE Table_Name = " + TABLE_NAME);
                        // if result is 0 table does not exist what means its a new table.
                        if (result.getCount() != 0) {
                            result.moveToFirst();
                            String columnresult = result.getString(1);
                            if (!columnresult.equals(Columns.toString())) {
                                // if collumns are not equal diffrence in db has been made
                                // so we save old Table name and all old collumnnames for data transfer.
                                // and all data from that table
                                rows = getTabledata(TABLE_NAME);
                                tableData.add(new TableData(rows, Table, TABLE_NAME));
                                writeabledb.execSQL(new StringBuilder().append("DROP TABLE IF EXISTS ").append(TABLE_NAME).toString());
                                // execute create table part
                            }
                        } else {
                            newtables.add(Table);
                        }

                    }
                    // all tables that have been changed in updated json file have been droped.
                    // all data from those chacges have been saved in arraylist tabledata.
                    // new json tableobject has been saved in there as well
                    if (tableData.size() == 0 && newtables.size() == 0) {

                    } else {
                        if (newtables.size() != 0) {
                            for (int i = 0; i < newtables.size(); i++) {
                                CreateTable(newtables.get(i));
                            }
                        }
                        if (tableData.size() != 0) {
                            for (int i = 0; i < tableData.size(); i++) {
                                TableData tableDataobj = tableData.get(i);
                                CreateTable(tableDataobj.getTable());
                                ArrayList<Integer> equal_colnames = getsimmilarcolumns(tableDataobj.getTable(), tableDataobj.getData());
                                InsertOldData(tableDataobj, equal_colnames);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void InsertOldData(TableData data, ArrayList<Integer> Equalcols) {
        ArrayList<SqlRow> rows = data.getData();
        String Tablename = data.getTable_name();
        ArrayList<ArrayList<String>> columnresults;
        // though all old row results
        for (int i = 0; i < rows.size(); i++) {
            ContentValues cv = new ContentValues();
            columnresults = new ArrayList<>();
            SqlRow row = rows.get(i);
            ArrayList<String> column_Names = row.getCollumn_names();
            ArrayList<String> column_Value = row.getCollumn_values();
            ArrayList<String> column_Type = row.getCollumn_type();
            // will loop through numbers for the amount of columns we need for values
            for (int j = 0; j < Equalcols.size(); j++) {
                ArrayList<String> single_column = new ArrayList<>();
                // Equalcols.get(j) is the column number that we need for values
                // column number is all columns counted from 0 to last column
                // and we only use get the columns that is still in the new table
                single_column.add(column_Names.get(Equalcols.get(j)));
                single_column.add(column_Value.get(Equalcols.get(j)));
                single_column.add(column_Type.get(Equalcols.get(j)));
                columnresults.add(single_column);
            }
            for (ArrayList<String> column : columnresults) {
                switch (column.get(2)) {
                    case "Integer":
                        cv.put(column.get(0), Integer.parseInt(column.get(1)));
                        break;
                    default:
                        cv.put(column.get(0), column.get(1));
                        break;
                }
            }
            db.insertdata(Tablename, cv);
        }
    }

    private ArrayList<Integer> getsimmilarcolumns(JSONObject Table, ArrayList<SqlRow> rows) {
        ArrayList<Integer> equal = new ArrayList<>();
        try {
            JSONArray columns = Table.getJSONArray("Columns");
            SqlRow row = rows.get(0);
            ArrayList<String> colnames = row.getCollumn_names();

            // loop trough all column names from old table
            for (int i = 0; i < colnames.size(); i++) {
                String name = colnames.get(i);
                // loop through all collumnnames from new table
                for (int j = 0; j < columns.length(); j++) {
                    JSONObject columnobject = columns.getJSONObject(j);

                    String Column_Name = columnobject.getString("Colname");
                    if (Column_Name.equals(name)) {
                        equal.add(j);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // returns all collumn names that are in commen from old table and new table.
        return equal;
    }

    private ArrayList<SqlRow> getTabledata(String TABLE_NAME) {
        ArrayList<SqlRow> rows = new ArrayList<>();
        Cursor result;
        Cursor collumntyperesult;
        // get all values in table
        result = db.Query("SELECT * FROM " + TABLE_NAME);
        //get all collumnnames
        String[] oldColumnnames = result.getColumnNames();
        // get all collumn types.
        collumntyperesult = db.Query("PRAGMA table_info(" + TABLE_NAME + ")");
        if (result.getCount() != 0) {
            result.moveToFirst();
            // loop trough all result rows
            for (int j = 0; j < result.getCount(); j++) {
                // row is all key / collumnnames and all values / collumnvalues
                // in a way that is good for inserting back in new table.
                // we need column names for if a column has deleted and keeping
                ArrayList<String> values = new ArrayList<>();
                ArrayList<String> keys = new ArrayList<>();
                ArrayList<String> collumntype = new ArrayList<>();
                collumntyperesult.moveToFirst();
                // loop trough all collumns in result: Name, Value, Type
                for (int k = 0; k < oldColumnnames.length; k++) {
                    keys.add(oldColumnnames[k]);
                    values.add(result.getString(k));
                    collumntype.add(collumntyperesult.getString(2));
                    collumntyperesult.moveToNext();
                }
                rows.add(new SqlRow(keys, values, collumntype, TABLE_NAME));
                result.moveToNext();
            }
        }
        return rows;
    }

    public void refresh_db_jsonfile() {
        String httpURL = new Settings().getBaseServerUrl() + "index.php?DW=getfile";
        ArrayList<String> var = new ArrayList<>();
        ArrayList<String> varname = new ArrayList<>();
        String result = new background_httprequest(httpURL, "POST", varname, var, context).connect().toString();
        //todo write new dbjson file.  and check if if it is different
        write_db_file(result);
        checkDBchanges();


    }

    public void write_db_file(String json) {
        File directory = new File(context.getFilesDir()+File.separator+"files");

        if(!directory.exists())
            directory.mkdir();

        File newFile = new File(directory, "db.json");

        if(!newFile.exists()){
            try {
                newFile.createNewFile();
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