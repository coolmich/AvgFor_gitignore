package com.antedeluvia.avgfor;

import com.antedeluvia.avgfor.R.id;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.TextView;

public class AFLoginActivity extends FragmentActivity {

    public static final int INTERNETERR =  500;
    private final String DIALOG = "dialog tag";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        if( !checkLogin() ) {
            setContentView(R.layout.login);

            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.login_fragment);

            if( internetConnected() ){
                System.err.println("connected!");
                if(fragment == null){
                    Log.i(DIALOG, "what the hell");
                    fragment = new AFLoginFragment();
                    fm.beginTransaction().add(id.login_fragment,fragment).commit();
                }
            }else {
                System.err.println("no internet!");
                AFAlertFragment dialog = AFAlertFragment.newInstance(INTERNETERR);
                dialog.show(fm, DIALOG);
            }

            // set app name color
            int titleId = getResources().getIdentifier("action_bar_title", "id",
                    "android");
            TextView actionBarTitle = (TextView) findViewById(titleId);
            actionBarTitle.setTextColor(getResources().getColor(R.color.pale));
        }else{
            Intent i = new Intent(this, AFSeatActivity.class);
            startActivity(i);
            finish();
        }
	}

    private boolean checkLogin(){
        SharedPreferences pref = getSharedPreferences(AFSeatIntentService.USERFILE, 0);
        String id = pref.getString(AFSeatIntentService.USERIDKEY, "none");
        if( id.equals("none") ){
            return false;
        }
        pref.edit().putBoolean(AFSeatIntentService.USERFIRSTKEY, false);
        return true;
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.login_fragment);

        if (fragment != null) {
            fragment.onResume();
        }

    }

    public boolean internetConnected(){
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

}
