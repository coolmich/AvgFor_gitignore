package com.antedeluvia.avgfor;

import com.antedeluvia.avgfor.R;
import com.antedeluvia.avgfor.R.id;
import com.antedeluvia.avgfor.R.layout;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.TextView;

public class AFSignupActivity extends FragmentActivity {

    public static final int INTERNETERR =  500;
    private final String DIALOG = "dialog tag";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);
		
		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.signup_fragment);
        if( internetConnected() ){
            System.err.println("connected!");
            if(fragment == null){
                fragment = new AFSignupFragment();
                fm.beginTransaction().add(id.signup_fragment,fragment).commit();
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
