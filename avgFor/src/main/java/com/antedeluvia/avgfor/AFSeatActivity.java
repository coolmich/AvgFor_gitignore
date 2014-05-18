package com.antedeluvia.avgfor;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class AFSeatActivity extends ActionBarActivity {
	public SlidingMenu menu;
	public static final int INTERNETERR =  500;
	private final String DIALOG = "dialog tag";
    public static int FORADDCLASS = 202;
    private boolean classAdded = false;
    private AFMenuFragment menuFrag;
    public static String uID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.container_with_padding);
		// three-line drawer icon
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			android.app.ActionBar ab = getActionBar();
			ab.setDisplayHomeAsUpEnabled(true);
		}
        initSlidingMenu();
		// if there's internet then create seat fragment
		FragmentManager fm = getSupportFragmentManager();
		Fragment fragment = fm.findFragmentById(R.id.pure_list_container_with_padding);
		if( internetConnected() ){
			System.err.println("connected!");
			if(fragment == null){
				fragment = new AFSeatFragment();
				fm.beginTransaction().add(R.id.pure_list_container_with_padding,fragment).commit();
			}
		}else{
			System.err.println("no internet!");
			AFAlertFragment dialog = AFAlertFragment.newInstance(INTERNETERR);
			dialog.show(fm, DIALOG);
		}

		// set app name color
		int titleId = getResources().getIdentifier("action_bar_title", "id",
                "android");
		TextView actionBarTitle = (TextView) findViewById(titleId);
		actionBarTitle.setTextColor(getResources().getColor(R.color.pale));

        // get user id
        SharedPreferences pref = getSharedPreferences("userInfo", 0);
        uID = pref.getString("user_id", "none");
        Log.e("e", "user id recorded is "+uID);

        // start service
        AFSeatIntentService.startSeatServiceOnSchedule(this);
	}

	private void initSlidingMenu() {
        menu = new SlidingMenu(this);  
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);  
        menu.setShadowWidthRes(R.dimen.shadow_width);  
        menu.setShadowDrawable(R.drawable.shadow); 
        menu.setBehindWidth(200);
        //menu.setFadeEnabled(true);
        //menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);  
        menu.setFadeDegree(0.35f);  
        menu.setSelectorDrawable(R.drawable.ic_navigation_drawer);
        menu.setSelectorEnabled(true);
        /*menu.setActionBarSlideIcon(new ActionBarSlideIcon(this,
                R.drawable.ic_navigation_drawer, 
                R.string.navigation_drawer_open, 
                R.string.navigation_drawer_close));*/
        //menu.setBehindScrollScale(0.5f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);  
        menu.setMenu(R.layout.menu_frame);
        menuFrag = AFMenuFragment.newInstance("seat");
        getSupportFragmentManager().beginTransaction().replace(R.id.menu_frame, menuFrag).commit();
    }
	@Override  
    public void onBackPressed() {  
        if (menu.isMenuShowing()) {  
            menu.showContent();  
        } else {  
            super.onBackPressed();  
        }  
    }
	
	// option menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.afseat, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		System.err.println("something clicked");

		switch(item.getItemId()){
		case R.id.add_class_btn: 
			System.err.println("plus clicked");
			Intent i = new Intent(this, AFSubjectActivity.class);
            i.putExtra("FromMainForAdd", 100);
            startActivityForResult(i, FORADDCLASS);
			return true;
		case android.R.id.home:
			menu.showMenu();
			System.err.println("home btn clicked");
			return true;
//		case R.id.af_refresh_btn:
//			refreshSeatFragment();
//			return true;
		default: 
			return super.onOptionsItemSelected(item);
		}
	}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(data == null){
            System.err.println("data is null");
            return;
        }
        classAdded = true;
        System.err.println("class added");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if( classAdded ){
            Toast.makeText(this, "Loading for newly added class...", Toast.LENGTH_LONG).show();
            refreshSeatFragment();
            classAdded = false;
        }
    }
	// check whether internet is available 	
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

    public void refreshSeatFragment(){
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = new AFSeatFragment();
        fm.beginTransaction().replace(R.id.pure_list_container_with_padding,fragment).commit();
        menuFrag.toggleMenuColor(menuFrag.getView(), (TextView)menuFrag.getView().findViewById(R.id.menu_seat_row));
    }

    public void refreshHelpFragment(){
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = new AFHelpFragment();
        fm.beginTransaction().replace(R.id.pure_list_container_with_padding,fragment).commit();
        menuFrag.toggleMenuColor(menuFrag.getView(), (TextView)menuFrag.getView().findViewById(R.id.menu_help_row));
    }





}
