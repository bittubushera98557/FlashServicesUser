package com.FlashScreen.flashservice.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by indiaweb on 10/10/2017.
 */


public class CityDb extends SQLiteOpenHelper
{
    public static final String tableName = "CityDb";
    static String DATABASE_NAME="CityTable";
    public static final String KEY_ID="id";
    public static String col1 = "CITYID";
    public static String col2 = "CITY_NAME";


    public CityDb(Context _cntxt)
    {

        super(_cntxt, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sql="CREATE TABLE "+tableName+" ("+KEY_ID+" INTEGER PRIMARY KEY, "+col1+" TEXT, "+col2+" TEXT)";//, "+col3+" TEXT, "+col4+" TEXT, "+col5+" TEXT)";//, "+col6+" TEXT, "+col7+" TEXT, "+col8+" TEXT)";
        db.execSQL(sql);
        System.out.println("Table is CreaTED");
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2)
    {

    }

    public long insertRecord(String ID,String NAME)//, String CATEGORY,String QUOTES,  String APPROVE)//,String PRICE, String QUANTITY,String MPRICE,  String IMAGE)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(col1, ID);
        cv.put(col2, NAME);
        long cnt = 	db.insert(tableName, null, cv);
        return cnt;
    }

    public ArrayList<String> ID()
    {
        ArrayList<String> temp = new ArrayList<String>();

        SQLiteDatabase db= getReadableDatabase();

        String nam []={col1};

        Cursor cursor =db.query(tableName, nam, null, null, null, null, null);
        while(cursor.moveToNext())//row wise row
        {
            temp.add(cursor.getString(0));

        }
        return temp;
    }
    public ArrayList<String> NAME()
    {
        ArrayList<String> temp = new ArrayList<String>();

        SQLiteDatabase db= getReadableDatabase();

        String nam []={col2};

        Cursor cursor =db.query(tableName, nam, null, null, null, null, null);
        while(cursor.moveToNext())//row wise row
        {
            temp.add(cursor.getString(0));

        }
        return temp;
    }

    /////// GET BY ID   /////////////////////////

    public String getName(String id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Log.e("databasecityuu", "" + id);
        String NamePro = "";

        ArrayList<String> arrayList = new ArrayList<>();

        String selectQuery = "SELECT * FROM "+ tableName+" WHERE CITYID ="+id;

        Cursor cursor = null;
        try {
             cursor = db.rawQuery(selectQuery, null);
            if(cursor.getCount() >0)
            {
                while (cursor.moveToNext()) {
                    // Convert blob data to byte array

                    NamePro = cursor.getString(cursor.getColumnIndex(col2));

                    Log.e("databasecity ", " " + NamePro);
                    arrayList.add(NamePro);

                    return  NamePro;
                }
            }
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();

        }

        return  NamePro;
    }
    public String getId(String id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Log.e("database112", "" + id);
        String NamePro = "";

        String selectQuery = "SELECT * FROM "+ tableName+" WHERE CITY_NAME ="+id;

        Cursor cursor = null;
        try {
             cursor = db.rawQuery(selectQuery, null);
            if(cursor.getCount() >0)
            {
                while (cursor.moveToNext()) {
                    // Convert blob data to byte array

                    NamePro = cursor.getString(cursor.getColumnIndex(col1));

                    Log.e("database111 ", " " + NamePro);
                    return  NamePro;


                }
            }
        }
        finally {
            // this gets called even if there is an exception somewhere above
            if(cursor != null)
                cursor.close();

        }

        return  NamePro;
    }

    /////////// UPDATE AND DONE ///////////////////


    public void update(String nam, String n,String mob,String m,String email,String e,String add, String a)
    {
        String whr1 = col1+"=?";
        String whr2 = col2+"=?";


        String[] str1= {nam};
        String[] str2= {mob};
        String[] str3= {email};
        String[] str4= {add};

        SQLiteDatabase db= getWritableDatabase();

        ContentValues cv= new ContentValues();
        cv.put(col1,n);
        cv.put(col2,m);


        db.update(tableName, cv, whr1, str1);
        db.update(tableName, cv, whr2, str2);


    }
    public void delrow( String nam)
    {
        String whr = col1+"=?";
        String[] str= {nam};
        SQLiteDatabase db= getWritableDatabase();
        db.delete(tableName, whr, str);
    }
    public void delall()
    {
        SQLiteDatabase db =getWritableDatabase();
        db.delete(tableName, null, null);
    }
}


