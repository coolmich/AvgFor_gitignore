package com.antedeluvia.avgfor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.v4.app.FragmentActivity;

public class AFSeatFragment extends ListFragment {
	private ArrayList<AFSeat> mSeatList;
	private String SEATURL="http://avgfor.com/api/seat/getUserCoursesSeats/";
	private AFSeatAdapter<AFSeat> mSeatAdapter;
	// test with 301
    LoginSingleton loginuser = LoginSingleton.getInstance();
	//private String userId = "22";
    private String userId = loginuser.getUID();
	public static final int EMPTYSEATLIST = 100;
	private final String EMPTYTAG = "seat list is empty";


	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		mSeatList = new ArrayList<AFSeat>();
		
		//fetch data
		new AFSeatHttpTask().execute(SEATURL+userId);
	}
	
	private class AFSeatAdapter<AFSeat> extends BaseAdapter{
		private int VIEW_TYPE_COUNT = 3;
		private int COURSE_TITLE_TYPE = 0;
		private int COURSE_SEAT_TYPE = 1;
		private int COURSE_SEAT_FULL_TYPE = 2;

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mSeatList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mSeatList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		@Override
		public int getViewTypeCount(){
			return VIEW_TYPE_COUNT;
		}
		
		@Override
		public int getItemViewType(int position){
			AFSeat seat = (AFSeat) getItem(position);
			if(((com.antedeluvia.avgfor.AFSeat) seat).whetherTitle()){
				return COURSE_TITLE_TYPE;
			}else if(((com.antedeluvia.avgfor.AFSeat) seat).getLimit().equals("WaitList")){
				return COURSE_SEAT_FULL_TYPE;
			}else{
				return COURSE_SEAT_TYPE;
			}
		}
		
		@Override
		public boolean isEnabled(int position){
			return false;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final int type = getItemViewType(position);
			AFSeat seat = (AFSeat) getItem(position);
			if(convertView == null){
				if(type == COURSE_TITLE_TYPE){
					convertView = getActivity().getLayoutInflater().inflate(R.layout.seat_title_row, null);
				}else if(type == COURSE_SEAT_TYPE){
					convertView = getActivity().getLayoutInflater().inflate(R.layout.seat_info_row, null);
				}else{
					convertView = getActivity().getLayoutInflater().inflate(R.layout.seat_info_row_red, null);
				}
			}
			if(type == COURSE_TITLE_TYPE){
				TextView titleView = (TextView) convertView.findViewById(R.id.seat_lec_title);
				titleView.setText(((com.antedeluvia.avgfor.AFSeat) seat).getCourse());
				TextView subTitleView = (TextView) convertView.findViewById(R.id.seat_lec_day);
				subTitleView.setText(((com.antedeluvia.avgfor.AFSeat) seat).getDay());
				TextView timeTextView = (TextView) convertView.findViewById(R.id.seat_lec_time);
				timeTextView.setText(((com.antedeluvia.avgfor.AFSeat) seat).getTime());
			}else{
				// seat number
				TextView seatClass = (TextView)convertView.findViewById(R.id.seat_class_number);
				seatClass.setText(((com.antedeluvia.avgfor.AFSeat) seat).getSection());
				// day time place
				TextView day = (TextView)convertView.findViewById(R.id.seat_class_day);
				day.setText(((com.antedeluvia.avgfor.AFSeat) seat).getDay());
				TextView time = (TextView)convertView.findViewById(R.id.seat_class_time);
				time.setText(((com.antedeluvia.avgfor.AFSeat) seat).getTime());
				TextView place = (TextView)convertView.findViewById(R.id.seat_class_place);
				place.setText(((com.antedeluvia.avgfor.AFSeat) seat).getBuilding()+" "+((com.antedeluvia.avgfor.AFSeat) seat).getRoom());
				// seat availability configure
				TextView tx = (TextView) convertView.findViewById(R.id.seat_stat);
				String availability = ((com.antedeluvia.avgfor.AFSeat) seat).getAvailable();
	            String limit = ((com.antedeluvia.avgfor.AFSeat) seat).getLimit();
	            tx.setText(availability+"/"+limit);
	            // prograss bar
	            ProgressBar pb = (ProgressBar) convertView.findViewById(R.id.progressbar);
	            if(limit.equals("WaitList")){
	                pb.setProgress(100);
                }else{
                    int ava = Integer.parseInt(availability);
                    int lim = Integer.parseInt(limit);
                    float progress = (float) (ava*1.0/lim);
                    pb.setProgress(100-(int)(100*progress));
              }
			}
			
			return convertView;
		}
		
		
		
	}
	
	private class AFSeatHttpTask extends AsyncTask<String, Integer, String>{
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
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
			
			//TODO
			updateListFromHttp(result);
			//set adapter
			mSeatAdapter = new AFSeatAdapter<AFSeat>();
			setListAdapter(mSeatAdapter);
	    }
	}
	
	public void updateListFromHttp(String result){
		JSONArray jsarr;
		//check list
		System.err.println("so the fukking result is "+result);			
		try {
			jsarr = new JSONArray(result);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			FragmentManager fm = getActivity().getSupportFragmentManager();
			AFAlertFragment frag = AFAlertFragment.newInstance(EMPTYSEATLIST);
			frag.show(fm, EMPTYTAG);
			e.printStackTrace();
			return;
		}
		int index = 0;
		while(index < jsarr.length()){
			try {
				//mJSONObjList.add(jsarr.getJSONObject(index));
				/*add all the seat and lecture*/
				JSONObject aClass = jsarr.getJSONObject(index);
				mSeatList.add(new AFSeat(aClass.getString("course"),
						aClass.getString("section"), aClass.getString("day"),
						aClass.getString("time"), aClass.getString("building"),
						aClass.getString("room"),aClass.getString("professor")));
				JSONArray seatArray = aClass.getJSONArray("seats");
				for(int i = 0; i < seatArray.length(); i++){
					mSeatList.add(new AFSeat(seatArray.getJSONObject(i)));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			index++;
		}
	}
	

	
	
	
}
