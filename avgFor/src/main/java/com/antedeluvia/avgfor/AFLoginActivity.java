package com.antedeluvia.avgfor;

import com.antedeluvia.avgfor.R;
import com.antedeluvia.avgfor.R.id;
import com.antedeluvia.avgfor.R.layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.TextView;

public class AFLoginActivity extends FragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.login_fragment);
		if(fragment == null){
			fragment = new AFLoginFragment();
			fm.beginTransaction().add(R.id.login_fragment,fragment).commit();
			
		}

        // set app name color
        int titleId = getResources().getIdentifier("action_bar_title", "id",
                "android");
        TextView actionBarTitle = (TextView) findViewById(titleId);
        actionBarTitle.setTextColor(getResources().getColor(R.color.pale));
	}

    @Override
    protected void onPostResume() {
        super.onPostResume();

    }
}
