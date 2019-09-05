package com.example.user.fit4life.SQL_Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.user.fit4life.Objects.Active_user;
import java.io.File;


import static android.content.ContentValues.TAG;


public class SQLdatabase extends SQLiteOpenHelper {

    private Database_create_helper dch;
    private Context context;
    private static final String DATABASE = "Fitforlife.db";
    private SQLiteDatabase readdb;
    private SQLiteDatabase writedb;


    private static final String TABLE_TABLES = "TABLES";
    private static final String TABLE_VERSIONS = "Migrate";

    public SQLdatabase(Context context) {
        // creating DB
        super(context, DATABASE, null, 1);
        this.context = context;


        readdb = this.getReadableDatabase();
        writedb = this.getWritableDatabase();

        if(doesDatabaseExist(context, DATABASE)){
            Log.i(TAG, "DB exist");
            dch = new Database_create_helper(context, this, readdb, writedb);
        } else {
            onUpgrade(this.getWritableDatabase(), 0, 0);
        }
//        reset to version 1


        //TODO retrieve DB version from online DB(or otherway for dinamic db version) see settings


    }

    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // all tables that are created
        // users

        new Database_create_helper(context, this, readdb, writedb).Start();

        }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // check db if tables are gone
        try {
            Cursor Result = readdb.rawQuery("SELECT * FROM TABLES", null);
            if(Result.getCount() != 0) {
                for (int i = 0; i < Result.getCount(); i++) {
                    Result.moveToPosition(i);
                    db.execSQL("DROP TABLE IF EXISTS " + Result.getString(0));
                }
            }    
        } catch (Throwable t){
            Log.e(TAG, "onUpgrade: Tables not available1");
        }
        

//        db.execSQL("DROP TABLE IF EXISTS Users");
        // this to cal last
        if(oldVersion == newVersion) {
            try {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_TABLES);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_VERSIONS);
            } catch (Throwable t){
                Log.i(TAG, "onUpgrade:  Tables not available2");
            }
        }

        // todo check tables drop all table table name from db if TAble_Tables exist
        onCreate(db);

    }
    public Cursor Query(String query){
        Cursor Result = readdb.rawQuery(query, null);
        return Result;
    }

    public void insertdata(String TableName, ContentValues cv) {
        writedb.beginTransaction();
        try {
            writedb.insert(TableName, null, cv);
            writedb.setTransactionSuccessful();
        } catch (Throwable t){
            Log.e(TAG, "insertdata: something went wrong with inserting");
        } finally {
            writedb.endTransaction();
        }



    }
    // todo make sepperate function for everything save everything in db


    public String getpass() {
        //todo change function for if multimle users login
        Cursor passwordres;
        String Password = "";
        passwordres = this.Query("SELECT * FROM Users");
        if (passwordres.getCount() == 0) {
            return null;
        } else {
            passwordres.moveToFirst();
            Password = passwordres.getString(3);
            passwordres = null;
            return Password;
        }
    }

    public Active_user getuser(int UserID) {
        int klasid;
        String username;

        Cursor userres = readdb.rawQuery("SELECT * FROM Users" + " WHERE ID = " + UserID, null);
        if (userres.getCount() != 0) {
            userres.moveToFirst();
            Active_user user = new Active_user(UserID, userres.getInt(1), userres.getString(4), userres.getString(2), userres.getString(7), null);
            System.out.println(UserID + " " + userres.getInt(1) + " " + userres.getString(4) + " " + userres.getString(2) + " " + userres.getString(7));
            return user;
        } else {
            //TODO error no users found
            return null;
        }
    }



    public int getuserID() {
        int userid;
        Cursor userres = readdb.rawQuery("SELECT * FROM Users", null);
        if (userres.getCount() == 0) {
            //todo check null parse int error on login
            return Integer.parseInt(null);
        } else {
            userres.moveToFirst();
            userid = userres.getInt(0);
            return userid;
        }
    }

//    public ArrayList<product> getproducts(){
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor productres = db.rawQuery("SELECT * FROM " + TABLE_PRODUCT_EXAMPL, null);
//        ArrayList<product> products = new ArrayList<>();
//        if (products.size() == 0){
//            return products;
//        } else {
//            productres.moveToFirst();
//            while (productres.moveToNext()) {
//                product p = new product(productres.getString(2), productres.getString(3), productres.getInt(4));
//                products.add(p);
//            }
//            return products;
//        }
//    }
}