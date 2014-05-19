package com.antedeluvia.avgfor;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class AFSeatIntentService extends IntentService {
    private static String SEATRESULT = "seat result";
    private static String uID;
    private static final int INTERVAL = 20000;
    public static final String SEATFILE = "seatRawData";
    public static final String USERFILE = "userInfo";
    public static final String SEATRAWKEY = "seats";
    public static final String USERIDKEY = "user_id";
    public static final String USERFIRSTKEY = "first_time";


    public AFSeatIntentService() {
        super("AFSeatIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if( AFSeatActivity.internetConnected(this) ) {
            new AFBGSeatTask().execute(AFSeatFragment.SEATURL + uID);
        }
    }

    public static void startSeatServiceOnSchedule(Activity activity, boolean isOn){
        Intent i = new Intent(activity, AFSeatIntentService.class);
        PendingIntent pi = PendingIntent.getService(activity, 0, i, 0);

        AlarmManager manager = (AlarmManager)activity.getSystemService(Context.ALARM_SERVICE);
        if( isOn ) {
            manager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), INTERVAL, pi);
        }else{
            manager.cancel(pi);
            pi.cancel();
        }
    }

    @Override
    public void onCreate(){
        super.onCreate();
        SharedPreferences pref = getSharedPreferences(USERFILE, 0);
        uID = pref.getString(USERIDKEY, "none");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    public boolean updateSeatsInPreference( String result ){
        AFSeatDatabaseHelper helper = new AFSeatDatabaseHelper(this);
        helper.analysizeStatus(result);
        //shared preference
        SharedPreferences pref = getSharedPreferences(SEATFILE, 0);
        SharedPreferences.Editor edit = pref.edit();
        String originalSeats = pref.getString(SEATRAWKEY, null);
        if( originalSeats == null || !originalSeats.equals(result)){
            edit.putString(SEATRAWKEY, result);
            edit.commit();
            Log.e("e","seats updated in pref");
            return true;
        }else{
            Log.e("e", "Seats same as before");
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
