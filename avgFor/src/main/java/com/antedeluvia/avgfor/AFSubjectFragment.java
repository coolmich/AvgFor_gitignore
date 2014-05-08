package com.antedeluvia.avgfor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

//@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AFSubjectFragment extends ListFragment {
	private ArrayList<AFSubject> mSubjectList;
	public final static String EXTRA_NAME = "subject specific name";
	private final String SUBJECTURL = "http://avgfor.com/api/subject";
	//public static final String SUBJECT_CODE = "subject_http_request";

	//@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		mSubjectList = new ArrayList<AFSubject>();
		new AFSubjectHttpTask().execute(SUBJECTURL);
		/*
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			android.app.ActionBar ab = getActivity().getActionBar();
			ab.setDisplayHomeAsUpEnabled(true);
			//ab.setHomeButtonEnabled(true);
			setHasOptionsMenu(true);
			//ab.setHomeAsUpIndicator(R.drawable.ic_navigation_drawer);
		}*/
	}
	
	@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
		AFSubject subject = mSubjectList.get(position);
        Intent i = new Intent(getActivity(), AFCourseActivity.class); 
        i.putExtra(AFSubjectFragment.EXTRA_NAME, subject.getId());
        i.setClass(getActivity(), AFCourseActivity.class);
        try{
        	startActivity(i);
        }catch(ActivityNotFoundException e){
        	System.err.println("error in startactivity");
        	e.printStackTrace();
        }
    }
	
	public void updateListFromHttp(String obj){
		JSONObject jsobj;
		try {
			jsobj = new JSONObject(obj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.err.println("error in updateUIFromHttp");
			e.printStackTrace();
			return;
		}
		char[] alphabet = {'A'};
		for(int i = 0; i < 26; i++){
			String key = new String(alphabet);
			if(jsobj.has(key)){
				System.err.println("has key:"+key);
				try {
					JSONArray jsarr = jsobj.getJSONArray(key);
					System.err.println("jsonarray length:"+jsarr.length());
					int index = 0;
					while(index < jsarr.length()){
						mSubjectList.add(new AFSubject(jsarr.getJSONObject(index)));
						index++;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					System.err.println("error in getJSONArray");
					e.printStackTrace();
				}
			}
			alphabet[0]++;
		}
	}
	
	private class AFSubjectAdapter<AFSubejct> extends ArrayAdapter{
		public AFSubjectAdapter(ArrayList<AFSubject> list){
			super(getActivity(),R.layout.two_line_item, list);
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent){
			// make sure the view is not null
			if(convertView == null){
				convertView = getActivity().getLayoutInflater().inflate(R.layout.two_line_item, null);
			}
			// find the corresponding item
			AFSubject subject = mSubjectList.get(position);
			// configure text
			TextView titleView = (TextView) convertView.findViewById(R.id.item_title);
			titleView.setText(subject.getAbbr());
			TextView subTitleView = (TextView) convertView.findViewById(R.id.item_subtitle);
			subTitleView.setText(subject.getName());
          
			return convertView;
		}	
	}
	
	private class AFSubjectHttpTask extends AsyncTask<String, Integer, String>{
		
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
			updateListFromHttp(result);
			//set adapter
			ArrayAdapter<AFSubject> adapter = new AFSubjectAdapter<AFSubject>(mSubjectList);
			setListAdapter(adapter);
	    }
	}
	
}
