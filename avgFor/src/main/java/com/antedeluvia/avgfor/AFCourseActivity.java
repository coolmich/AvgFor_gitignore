package com.antedeluvia.avgfor;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.TextView;

public class AFCourseActivity extends FragmentActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.container_with_padding);

		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.pure_list_container_with_padding);
		if(fragment == null){
			fragment = new AFCourseFragment();
			fm.beginTransaction().add(R.id.pure_list_container_with_padding,fragment).commit();
			
		}
		int titleId = getResources().getIdentifier("action_bar_title", "id",
	            "android");
		TextView actionBarTitle = (TextView) findViewById(titleId);
		actionBarTitle.setTextColor(getResources().getColor(R.color.pale));
	}
}
