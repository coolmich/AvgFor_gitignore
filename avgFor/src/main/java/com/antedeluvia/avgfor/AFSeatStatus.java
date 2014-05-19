package com.antedeluvia.avgfor;

import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AFSeatStatus {
    private int mID;
    private String mName;
    private int mStatus;  // 0 for not full, 1 for full

    public AFSeatStatus( Cursor cursor){
        mID = cursor.getInt(cursor.getColumnIndex(AFSeatDatabaseHelper.SEAT_ID));
        mName = cursor.getString(cursor.getColumnIndex(AFSeatDatabaseHelper.NAME));
        mStatus = cursor.getInt(cursor.getColumnIndex(AFSeatDatabaseHelper.STATUS));
        cursor.close();
    }

    public AFSeatStatus(JSONObject jsonObject){
        // get name
        mName = AFSeatStatus.formUniqueName(jsonObject);
        // get status
        mStatus = 1;
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("seats");
            int index = 0;
            while( index < jsonArray.length() ){
                if( !jsonArray.getJSONObject(index).getString("limit").equals("WaitList") ){
                    mStatus = 0;
                    break;
                }
                index++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String formUniqueName( JSONObject jsonObject ){
        try {
            return jsonObject.getString("course")+":"+jsonObject.getString("time");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getName() {
        return mName;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus( int i ){ mStatus = i; }

    public int getmID() {
        return mID;
    }
}
