package com.antedeluvia.avgfor;

import com.antedeluvia.avgfor.R;
import com.antedeluvia.avgfor.R.id;
import com.antedeluvia.avgfor.R.layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public class AFSignupActivity extends FragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);
		
		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.signup_fragment);
		if(fragment == null){
			fragment = new AFSignupFragment();
			fm.beginTransaction().add(R.id.signup_fragment,fragment).commit();
			
		}
	}
}
