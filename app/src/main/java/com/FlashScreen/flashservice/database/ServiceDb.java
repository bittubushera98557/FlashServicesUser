package com.FlashScreen.flashservice.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by indiaweb on 9/11/2017.
 */

/**
 * Created by indiaweb on 6/13/2017.
 */
public class ServiceDb extends SQLiteOpenHelper
{
    public static final String tableName = "ServiceDb";
    static String DATABASE_NAME="ServiceTable";
    public static final String KEY_ID="id";
    public static String col1 = "CATID";
    public static String col2 = "CAT_NAME";
    public static String col3 = "SERVICEID";
    public static String col4 = "SERVICE_NAME";
    public static String col5 = "PRICE";


    public ServiceDb(Context _cntxt)
    {

        super(_cntxt, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sql="CREATE TABLE "+tableName+" ("+KEY_ID+" INTEGER PRIMARY KEY, "+col1+" TEXT, "+col2+" TEXT, "+col3+" TEXT, "+col4+" TEXT, "+col5+" TEXT)";//, "+col6+" TEXT, "+col7+" TEXT, "+col8+" TEXT)";
        db.execSQL(sql);
        System.out.println("Table is CreaTED");
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2)
    {

    }

    public long insertRecord(String QID,String CATID, String CATEGORY,String QUOTES,  String APPROVE)//,String PRICE, String QUANTITY,String MPRICE,  String IMAGE)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(col1, QID);
        cv.put(col2, CATID);
        cv.put(col3, CATEGORY);
        cv.put(col4, QUOTES);
        cv.put(col5, APPROVE);
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
    public ArrayList<String> CATID()
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
    public ArrayList<String> CATEGORY()
    {
        ArrayList<String> temp = new ArrayList<String>();

        SQLiteDatabase db= getReadableDatabase();

        String mob []={col3};

        Cursor cursor =db.query(tableName, mob, null, null, null, null, null);
        while(cursor.moveToNext())//row wise row
        {
            temp.add(cursor.getString(0));

        }
        return temp;
    }
    public ArrayList<String> QUOTES()
    {
        ArrayList<String> temp = new ArrayList<String>();

        SQLiteDatabase db= getReadableDatabase();

        String email []={col4};

        Cursor cursor =db.query(tableName, email, null, null, null, null, null);
        while(cursor.moveToNext())//row wise row
        {
            temp.add(cursor.getString(0));

        }
        return temp;
    }
    public ArrayList<String> APPROVE()
    {
        ArrayList<String> temp = new ArrayList<String>();

        SQLiteDatabase db= getReadableDatabase();

        String add []={col5};

        Cursor cursor =db.query(tableName, add, null, null, null, null, null);
        while(cursor.moveToNext())//row wise row
        {
            temp.add(cursor.getString(0));

        }
        return temp;
    }
    /////// GET BY ID   /////////////////////////

    public String getCat_Name(String id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Log.e("database112", "" + id);
        String NamePro = "";

        ArrayList<String> arrayList = new ArrayList<>();

        String selectQuery = "SELECT * FROM "+ tableName+" WHERE CATID ="+id;


        Cursor cursor = null;
        try {
             cursor = db.rawQuery(selectQuery, null);
            if(cursor.getCount() >0)
            {
                while (cursor.moveToNext()) {
                    // Convert blob data to byte array

                    NamePro = cursor.getString(cursor.getColumnIndex(col2));

                    Log.e("database77777 ", " " + NamePro);


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
    public String getService_Name(String id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Log.e("database112", "" + id);
        String NamePro = "";
        /*if(id.equalsIgnoreCase("Select Services"))
        {
            id=""+1;
        }*/
        String selectQuery = "SELECT * FROM "+ tableName+" WHERE SERVICEID = "+"'"+id+"'";


        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);
            // do some work with the cursor here.
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    // Convert blob data to byte array

                    NamePro = cursor.getString(cursor.getColumnIndex(col4));

                    Log.e("database111 ", " " + NamePro);
                    return NamePro;


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
    public String getService_Price(String id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Log.e("database112", "" + id);
        String NamePro = "";

        String selectQuery = "SELECT * FROM "+ tableName+" WHERE SERVICEID ="+id;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.getCount() >0)
        {
            while (cursor.moveToNext()) {
                // Convert blob data to byte array

                NamePro = cursor.getString(cursor.getColumnIndex(col5));

                Log.e("database111 ", " " + NamePro);
                return  NamePro;


            }
        }


        return  NamePro;
    }
    /////////// UPDATE AND DONE ///////////////////


    public void update(String nam, String n,String mob,String m,String email,String e,String add, String a)
    {
        String whr1 = col1+"=?";
        String whr2 = col2+"=?";
        String whr3 = col3+"=?";
        String whr4 = col4+"=?";

        String[] str1= {nam};
        String[] str2= {mob};
        String[] str3= {email};
        String[] str4= {add};

        SQLiteDatabase db= getWritableDatabase();

        ContentValues cv= new ContentValues();
        cv.put(col1,n);
        cv.put(col2,m);
        cv.put(col3,e);
        cv.put(col4,a);

        db.update(tableName, cv, whr1, str1);
        db.update(tableName, cv, whr2, str2);
        db.update(tableName, cv, whr3, str3);
        db.update(tableName, cv, whr4, str4);

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


