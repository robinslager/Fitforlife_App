package com.example.user.fit4life.SQL_Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.user.fit4life.Objects.Active_user;
import com.example.user.fit4life.Objects.product;

import java.util.ArrayList;


public class SQLdatabase extends SQLiteOpenHelper {

    private static final String DATABASE = "Fit4life.db";
    private static final String TABLE_USER = "Users";
    private static final String U_COL1 = "ID";
    private static final String U_COL2 = "klas_ID";
    private static final String U_COL3 = "email";
    private static final String U_COL4 = "wachtwoord";
    private static final String U_COL5 = "voornaam";
    private static final String U_COL6 = "achternaam";
    private static final String U_COL7 = "leerling_nummer";
    private static final String U_COL8 = "opleiding";
    private static final String U_COL9 = "rol";
    private static final String U_COL10 = "verwijderd";
    private static final String U_COL11 = "verwijderd_datum";
    private static final String U_COL12 = "pass_change";
    private static final String U_COL13 = "profielafbeelding";

    private static final String TABLE_KLASSEN = "klassen";
    private static final String K_COL1 = "ID";
    private static final String K_COL2 = "User_ID";
    private static final String K_COL3 = "Klasnaam";
    private static final String K_COL4 = "Datum";
    private static final String K_COL5 = "Verwijderd";
    private static final String K_COL6 = "Datum_verwijderd";

    private static final String TABLE_OEFENINGEN = "oefeningen";
    private static final String O_COL1 = "ID";
    private static final String O_COL2 = "Catagorie_ID";
    private static final String O_COL3 = "Naam";
    private static final String O_COL4 = "Uitleg";
    private static final String O_COL5 = "Punten";
    private static final String O_COL6 = "Datum";
    private static final String O_COL7 = "Verwijderd";
    private static final String O_COL8 = "Datum_verwijderd";

    private static final String TABLE_CAL_BURN = "calories_burnt";
    private static final String CB_COL1 = "ID";
    private static final String CB_COL2 = "User_ID";
    private static final String CB_COL3 = "Oefening_type";
    private static final String CB_COL4 = "Duratie";
    private static final String CB_COL5 = "Aantal_keren";
    private static final String CB_COL6 = "Datum";

    private static final String TABLE_CAL_EAT = "calories_eaten";
    private static final String CE_COL1 = "ID";
    private static final String CE_COL2 = "User_ID";
    private static final String CE_COL3 = "Voedsel";
    private static final String CE_COL4 = "Calories_eaten";
    private static final String CE_COL5 = "Datum";

    private static final String TABLE_EXC_SCHEME = "trainings_schema";
    private static final String ES_COL1 = "ID";
    private static final String ES_COL2 = "User_ID";
    private static final String ES_COL3 = "Klas_ID";
    private static final String ES_COL4 = "Oefening_ID";
    private static final String ES_COL5 = "Gewicht";
    private static final String ES_COL6 = "Aantal";
    private static final String ES_COL7 = "Duratie";
    private static final String ES_COL8 = "Afstand";
    private static final String ES_COL9 = "Datum";
    private static final String ES_COL10 = "Oefening_gedaan";
    private static final String ES_COL11 = "Verwijderd";
    private static final String ES_COL12 = "Verwijderd_Datum";

    private static final String TABLE_EXC_CATAGORIE = "trainings_catagorie";
    private static final String EC_COL1 = "ID";
    private static final String EC_COL2 = "Naam";
    private static final String EC_COL3 = "Datum";
    private static final String EC_COL4 = "Verwijderd";
    private static final String EC_COL5 = "Verwijderd_Datum";

    private static final String TABLE_PRODUCT_EXAMPL = "Product_Example";
    private static final String PE_COL1 = "ID";
    private static final String PE_COL2 = "Naam";
    private static final String PE_COL3 = "Merk";
    private static final String PE_COL4 = "C_P_100";


    private static final String TABLE_TABLE_SYNC = "Tables_updated";
    private static final String TTS_COL1 = "Table_name";
    private static final String TTS_COL2 = "Updated";
    private static final String TTS_COL3 = "Last_sync";


    public SQLdatabase(Context context) {
        // creating DB
        super(context, DATABASE, null, 3);
        //TODO retrieve DB version from online DB(or otherway for dinamic db version) see settings


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // all tables that are created
        // users

        db.execSQL("CREATE TABLE " + TABLE_USER + " (" + U_COL1 + " INTEGER, " + U_COL2 + " INTEGER, " + U_COL3 + " TEXT, " + U_COL4 + " TEXT," + " " + U_COL5 + " TEXT," + U_COL6 + " TEXT, " + U_COL7 + " INTEGER, " + U_COL8 + " TEXT," + U_COL13 + " TEXT, " + U_COL9 + " INTEGER, " + U_COL10 + " " + "INTEGER," + U_COL11 + " REAL," + U_COL12 + " INTEGER)");
        //klassen
        db.execSQL("CREATE TABLE " + TABLE_KLASSEN + " (" + K_COL1 + " INTEGER, " + K_COL2 + " INTEGER, " + K_COL3 + " TEXT, " + K_COL4 + " REAL, " + K_COL5 + " INTEGER, " + K_COL6 + " REAL)");
        // oefeningen
        db.execSQL("CREATE TABLE " + TABLE_OEFENINGEN + " (" + O_COL1 + " INTEGER, " + O_COL2 + " INTEGER, " + O_COL3 + " TEXT, " + O_COL4 + " TEXT, " + O_COL5
                + " INTEGER, " + O_COL6 + " REAL, " + O_COL7 + " INTEGER, " + O_COL8 + " REAL )");
        // calories burnt
        db.execSQL("CREATE TABLE " + TABLE_CAL_BURN + " (" + CB_COL1 + " INTEGER, " + CB_COL2 + " INTEGER, " + CB_COL3 + " TEXT, " + CB_COL4 + " INTEGER, " + CB_COL5 + " INTEGER, " + CB_COL6 + " REAL)");
        // calories eaten
        db.execSQL("CREATE TABLE " + TABLE_CAL_EAT + " (" + CE_COL1 + " INTEGER, " + CE_COL2 + " INTEGER, " + CE_COL3 + " TEXT, " + CE_COL4 + " INTEGER, " + CE_COL5 + " REAL)");
        // training schema
        db.execSQL("CREATE TABLE " + TABLE_EXC_SCHEME + " (" + ES_COL1 + " INTEGER, " + ES_COL2 + " INTEGER, " + ES_COL3 + " INTEGER, " + ES_COL4 + " INTEGER, " + ES_COL5 + " INTEGER," +
                ES_COL6 + " INTEGER," + ES_COL7 + " INTEGER," + ES_COL8 + " INTEGER," + ES_COL9 + " REAL," + ES_COL10 + " INTEGER," + ES_COL11 + " INTEGER," + ES_COL12 + " REAL)");
        // training Catogorien
        db.execSQL("CREATE TABLE " + TABLE_EXC_CATAGORIE + " (" + EC_COL1 + " INTEGER, " + EC_COL2 + " TEXT,  " + EC_COL3 + " REAL,  " + EC_COL4 + " INTEGER,  " + EC_COL5 + " REAL)");
        // table that keeps track of which tables are updated
        // this helps for les data trafic
        db.execSQL("CREATE TABLE " + TABLE_TABLE_SYNC + " (" + TTS_COL1 + " TEXT," + TTS_COL2 + " INTEGER," + TTS_COL3 + " REAL)");
        db.execSQL("CREATE TABLE " + TABLE_PRODUCT_EXAMPL + " (" + PE_COL1 + "INTEGER," + PE_COL2 + "TEXT," + PE_COL3 + "TEXT," + PE_COL4 + "INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_KLASSEN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TABLE_SYNC);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXC_CATAGORIE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXC_SCHEME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAL_EAT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CAL_BURN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OEFENINGEN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_EXAMPL);
        onCreate(db);
    }

    public void insertdata(String TableName, String[] data) {
        String Table = "";
        // inserts from live DB depending on string table name.
        ContentValues cv = new ContentValues();
        switch (TableName) {
            case "user":
                cv.put(U_COL1, data[0]);
                cv.put(U_COL2, data[1]);
                cv.put(U_COL3, data[2]);
                cv.put(U_COL4, data[3]);
                cv.put(U_COL5, data[4]);
                cv.put(U_COL6, data[5]);
                cv.put(U_COL7, data[6]);
                cv.put(U_COL8, data[7]);
                cv.put(U_COL9, data[8]);
                cv.put(U_COL10, data[9]);
                cv.put(U_COL11, data[10]);
                cv.put(U_COL12, data[11]);
                cv.put(U_COL13, data[12]);
                Table = TABLE_USER;
                break;
            case "klas":
                cv.put(data[0], K_COL1);
                cv.put(data[1], K_COL2);
                cv.put(data[2], K_COL3);
                cv.put(data[3], K_COL4);
                cv.put(data[4], K_COL5);
                cv.put(data[5], K_COL6);
                Table = TABLE_KLASSEN;
                break;
            case "oefening":
                cv.put(data[0], O_COL1);
                cv.put(data[1], O_COL2);
                cv.put(data[2], O_COL3);
                cv.put(data[3], O_COL4);
                cv.put(data[4], O_COL5);
                cv.put(data[5], O_COL6);
                cv.put(data[6], O_COL7);
                cv.put(data[7], O_COL8);
                Table = TABLE_OEFENINGEN;
                break;
            case "c_burnt":
                cv.put(data[0], CB_COL1);
                cv.put(data[1], CB_COL2);
                cv.put(data[2], CB_COL3);
                cv.put(data[3], CB_COL4);
                cv.put(data[4], CB_COL5);
                cv.put(data[5], CB_COL6);
                Table = TABLE_CAL_BURN;
                break;
            case "c_eat":
                cv.put(data[0], CE_COL1);
                cv.put(data[1], CE_COL2);
                cv.put(data[2], CE_COL3);
                cv.put(data[3], CE_COL4);
                cv.put(data[4], CE_COL5);
                Table = TABLE_CAL_EAT;
                break;
            case "schema":
                cv.put(data[0], ES_COL1);
                cv.put(data[1], ES_COL2);
                cv.put(data[2], ES_COL3);
                cv.put(data[3], ES_COL4);
                cv.put(data[4], ES_COL5);
                cv.put(data[5], ES_COL6);
                cv.put(data[6], ES_COL7);
                cv.put(data[7], ES_COL8);
                cv.put(data[8], ES_COL9);
                cv.put(data[9], ES_COL10);
                cv.put(data[10], ES_COL11);
                cv.put(data[11], ES_COL12);
                Table = TABLE_EXC_SCHEME;
                break;
            case "catagorie":
                cv.put(data[0], EC_COL1);
                cv.put(data[1], EC_COL2);
                cv.put(data[2], EC_COL3);
                cv.put(data[3], EC_COL4);
                cv.put(data[4], EC_COL5);
                Table = TABLE_EXC_CATAGORIE;
                break;
            case "table_sync":
                cv.put(data[0], EC_COL1);
                cv.put(data[1], EC_COL2);
                cv.put(data[2], EC_COL3);
                cv.put(data[3], EC_COL4);
                cv.put(data[4], EC_COL5);
                Table = TABLE_EXC_CATAGORIE;
                break;
        }
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(Table, null, cv);
    }
    // todo make sepperate function for everything save everything in db


    public String getpass() {
        //todo change function for if multimle users login
        String Password;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor passwordres = db.rawQuery("SELECT * FROM " + TABLE_USER, null);
        if (passwordres.getCount() == 0) {
            return null;
        } else {
            passwordres.moveToFirst();
            Password = passwordres.getString(3);
            return Password;
        }
    }

    public Active_user getuser(int UserID) {
        int klasid;
        String username;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor userres = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE ID = " + UserID, null);
        if (userres.getCount() != 0) {
            userres.moveToFirst();


            Active_user user = new Active_user(UserID, userres.getInt(1), userres.getString(4), userres.getString(2), userres.getString(9), null);
            System.out.println(UserID + " " + userres.getInt(1) + " " + userres.getString(4) + " " + userres.getString(2) + " " + userres.getString(9));
            return user;
        } else {
            //TODO error no users found
            return null;
        }
    }

    public int getuserID() {
        int userid;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor userres = db.rawQuery("SELECT * FROM " + TABLE_USER, null);
        if (userres.getCount() == 0) {
            //todo check null parse int error on login
            return Integer.parseInt(null);
        } else {
            userres.moveToFirst();
            userid = userres.getInt(0);
            return userid;
        }
    }

    public ArrayList<product> getproducts(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor productres = db.rawQuery("SELECT * FROM " + TABLE_PRODUCT_EXAMPL, null);
        ArrayList<product> products = new ArrayList<>();
        if (products.size() == 0){
            return products;
        } else {
            productres.moveToFirst();
            while (productres.moveToNext()) {
                product p = new product(productres.getString(2), productres.getString(3), productres.getInt(4));
                products.add(p);
            }
            return products;
        }
    }
}