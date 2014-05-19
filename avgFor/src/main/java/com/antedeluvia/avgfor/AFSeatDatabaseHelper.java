package com.antedeluvia.avgfor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AFSeatDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "seatsDB";
    private static final int VERSION = 1;
    public static final String SEAT_TB = "seats";
    public static final String SEAT_ID = "id";
    public static final String NAME = "name";
    public static final String STATUS = "status";

    public AFSeatDatabaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
        Log.e("e","in constructor of database helper");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.e("e","in onCreate of database helper");
        String createTable = "create table " + SEAT_TB + " (" + SEAT_ID + " integer primary key autoincrement, "
                + NAME + " varchar(100), " + STATUS + " integer);";
        Log.e("e","execution command is : "+createTable);
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }


    public AFSeatStatus queryOneSeatStatus( String name ){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(SEAT_TB, null, NAME+"=?", new String[]{ name }, null, null, null, null);
        if( cursor.moveToNext() ){
            AFSeatStatus obj = new AFSeatStatus(cursor);
            db.close();
            return obj;
        }else{
            db.close();
            return null;
        }
    }

    public void insertSeatStatusIntoDB(AFSeatStatus obj){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues val = new ContentValues();
        val.put(NAME, obj.getName());
        val.put(STATUS, obj.getStatus());
        db.insert(SEAT_TB, null, val);
        db.close();
    }

    public int updateSeatStatusInDB(AFSeatStatus obj, int id){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues val = new ContentValues();
        val.put(STATUS, obj.getStatus());
        int num = db.update(SEAT_TB, val, SEAT_ID+"=?", new String[]{ String.valueOf(id)} );
        db.close();
        return num;
    }

    public void analysizeStatus( String result ){
        try {
            JSONArray jsonArray = new JSONArray( result );
            // analyse for every course
            for( int i = 0; i < jsonArray.length(); i++ ){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                AFSeatStatus seatStatus = queryOneSeatStatus( AFSeatStatus.formUniqueName(jsonObject) );
                AFSeatStatus newSS = new AFSeatStatus(jsonObject);
                /* for testing notification
                if(i == 0){
                    newSS.setStatus(1);
                }*/
                //Log.d("d","status is: "+newSS.getStatus());
                // end of playing around
                if( seatStatus == null ){
                    // insert into database
                    // insert the object into database
                    insertSeatStatusIntoDB(newSS);
                    Log.d("d", "new data inserted================================");
                }else{
                    // check whether the status change
                    if( newSS.getStatus() != seatStatus.getStatus() ){
                        // notification stuff
                        Log.d("d", "notification============================================");
                        // update in database after assign ID
                        updateSeatStatusInDB(newSS, seatStatus.getmID());
                    }else{
                        Log.d("d", "Nothing changed============================");
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
