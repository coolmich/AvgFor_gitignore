package com.antedeluvia.avgfor;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class AFSeatIntentService extends IntentService {
    private static String SEATRESULT = "seat result";
    private static String uID;


    public AFSeatIntentService() {
        super("AFSeatIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i("i", "service running");
        new AFBGSeatTask().execute(AFSeatFragment.SEATURL + uID);
    }

    public static void startSeatServiceOnSchedule(Activity activity){
        Intent i = new Intent(activity, AFSeatIntentService.class);
        PendingIntent pi = PendingIntent.getService(activity, 0, i, 0);

        AlarmManager manager = (AlarmManager)activity.getSystemService(Context.ALARM_SERVICE);
        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 10000, pi);
    }

    @Override
    public void onCreate(){
        super.onCreate();
        Log.i("i","service started");
        SharedPreferences pref = getSharedPreferences("userInfo", 0);
        uID = pref.getString("user_id", "none");
        Log.e("e", "user id recorded is "+uID);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i("i","service stopped");
    }

    public boolean updateSeatsInPreference( String result ){
        //shared preference
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = pref.edit();
        String originalSeats = pref.getString("seats", null);
        if( originalSeats == null || !originalSeats.equals(result)){
            edit.putString("seats", result);
            edit.commit();
            System.err.println("seats updated in pref");
            return true;
        }else{
            Log.d("d", "Seats same as before");
            return false;
        }

    }

    private class AFBGSeatTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String result[] = {null, null};
            InputStream input = AFGeneralHttpTask.getInputStream(params[0]);
            try {
                return AFGeneralHttpTask.convertInputStreamToString(input);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                System.err.println("error in convert input stream to string");
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(String result){
            //if result is null, should notify user
            Log.d("d",result);
            updateSeatsInPreference(result);
        }
    }

}
