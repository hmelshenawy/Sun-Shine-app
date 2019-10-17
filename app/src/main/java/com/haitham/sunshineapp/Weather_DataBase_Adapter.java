package com.haitham.sunshineapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.SystemClock;
import android.widget.Toast;

import java.util.ArrayList;

public class Weather_DataBase_Adapter  {


    ArrayList<Forcast_List> forcatList;
    Weather_DataBase_Helper mhelper;
    SQLiteDatabase mSQLite;


    public Weather_DataBase_Adapter(Context context) {
         mhelper = new Weather_DataBase_Helper(context);
         mSQLite = mhelper.getWritableDatabase();

    }

    public long insertData(String date, String maxTemp,String currtemp, String condition, String humidity){

        ContentValues contentValues = new ContentValues();
        contentValues.put( mhelper.COLUMN_DATE , date);
        contentValues.put(mhelper.COLUMN_MAXTEMP, maxTemp);
        contentValues.put(mhelper.COLUMN_CURRTEMP, currtemp);
        contentValues.put(mhelper.COLUMN_CONDITION , condition);
        contentValues.put( mhelper.COLUMN_HUMIDITY , humidity);
        long l = mSQLite.insert(mhelper.TABLE_NAME, null, contentValues);

        return l;
    }

    public ArrayList<Forcast_List> getData(){

        forcatList = new ArrayList<>();
        String [] columns = {Weather_DataBase_Helper.COLUMN_DATE, Weather_DataBase_Helper.COLUMN_MAXTEMP,
         Weather_DataBase_Helper.COLUMN_CURRTEMP,Weather_DataBase_Helper.COLUMN_CONDITION,Weather_DataBase_Helper.COLUMN_HUMIDITY};

       Cursor cursor= mSQLite.query(Weather_DataBase_Helper.TABLE_NAME,columns,null,null,null,null,null );

       while (cursor.moveToNext()){

           String date = cursor.getString(0);
           String MaxT = cursor.getString(1);
           String currTemp = cursor.getString(2);
           String Condition = cursor.getString(3);
           String humidity = cursor.getString(4);

           Forcast_List object = new Forcast_List();
           object.temp = MaxT;
           object.curr_temp = currTemp;
           object.humidity = humidity;
           object.dt_txt = date;
           object.condition=Condition;
            forcatList.add(object);
       }
       return forcatList;
    }

    public void delete_AllData(){

        String [] columns = {Weather_DataBase_Helper.COLUMN_DATE, Weather_DataBase_Helper.COLUMN_MAXTEMP,
                Weather_DataBase_Helper.COLUMN_MINTEMP,Weather_DataBase_Helper.COLUMN_CURRTEMP,Weather_DataBase_Helper.COLUMN_HUMIDITY};
        String delete = "DELETE FROM "+Weather_DataBase_Helper.TABLE_NAME;
        //int n =  mSQLite.delete(Weather_DataBase_Helper.TABLE_NAME, Weather_DataBase_Helper.UID+"=?", null);
        mSQLite.execSQL(delete);

    }



    static class Weather_DataBase_Helper extends SQLiteOpenHelper {

       // CREATE TABLE" +TABLE_NAME+ "(" +UID+ " INTEGER PRIMARY KEY AUTOINCREMENT," +COLUMN_DATE+ "VARCHAR(255)," +COLUMN_MAXTEMP+ " VARCHAR(255)," +COLUMN_MINTEMP+ " VARCHAR(225)," +COLUMN_HUMIDITY+ " VARCHAR(255));
        private static final String DATABASE_NAME = "My_DataBase";
        private static final String TABLE_NAME = "Forcast_Table";
        private static final int DATABASE_VER = 1;
        private static final String UID = "_ID";
        private static final String COLUMN_DATE ="Date";
        private static final String COLUMN_MAXTEMP = "Max_Temp";
        private static final String COLUMN_MINTEMP = "Min_Temp";
        private static final String COLUMN_CURRTEMP = "Current_Temp";
        private static final String COLUMN_HUMIDITY = "Humidity";
        private static final String COLUMN_CONDITION = "Condition";
        private static final String CREATE_TABLE =" CREATE TABLE " + TABLE_NAME
               + " ( "
                + UID +  " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_DATE + " VARCHAR(255), "
                + COLUMN_MAXTEMP + " VARCHAR(255), "
                + COLUMN_CURRTEMP + " VARCHAR(255), "
                + COLUMN_CONDITION + " VARCHAR(255), "
                + COLUMN_HUMIDITY + " VARCHAR(255));";
        private static final String DROP_TABLE = "  DROP TABLE IF EXISTS "+TABLE_NAME+"; ";
        Context context;

          Weather_DataBase_Helper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VER);
            this.context = context;
           // Toast.makeText(context, "helper constarctor", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

            try {
                sqLiteDatabase.execSQL(CREATE_TABLE);
            }catch (SQLException e){
                System.out.println("SQL Exception");
            }
            Toast.makeText(context, "SQL Create done!!",Toast.LENGTH_LONG).show();
           // System.out.println("SQL Create");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

            sqLiteDatabase.execSQL(DROP_TABLE);
            onCreate(sqLiteDatabase);
            Toast.makeText(context, "SQL Updrade done!!",Toast.LENGTH_LONG).show();
            //System.out.println("SQL update");
        }
    }
}
