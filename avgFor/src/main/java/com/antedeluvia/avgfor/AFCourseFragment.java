package com.antedeluvia.avgfor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AFCourseFragment extends ListFragment {
	private ArrayList<AFCourse> mCourseList;
	public final static String EXTRA_NAME = "course specific name";
	private final String COURSEURL = "http://avgfor.com/api/course/";
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		mCourseList = new ArrayList<AFCourse>();
		String id = getActivity().getIntent().getStringExtra(AFSubjectFragment.EXTRA_NAME);
		String url = COURSEURL.concat(id);
		new AFCourseHttpTask().execute(url);		
	}
	
	@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
		AFCourse course = mCourseList.get(position);
        Intent i = new Intent(getActivity(), AFClassActivity.class); 
        i.putExtra(AFCourseFragment.EXTRA_NAME, course.getId());
        i.setClass(getActivity(), AFClassActivity.class);
        try{
        	startActivity(i);
        }catch(ActivityNotFoundException e){
        	System.err.println("error in startactivity");
        	e.printStackTrace();
        }
    }
	
	public void updateListFromHttp(String result){
		JSONArray jsarr;
		try {
			jsarr = new JSONArray(result);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		int index = 0;
		while(index < jsarr.length()){
			JSONObject jsobj;
			try {
				jsobj = (JSONObject) jsarr.get(index);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			mCourseList.add(new AFCourse(jsobj));
			index++;
		}
	}
	
	
	private class AFCourseAdapter<AFCourse> extends ArrayAdapter{
		public AFCourseAdapter(ArrayList<AFCourse> list){
			super(getActivity(),R.layout.two_line_item, list);
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent){
			// make sure the view is not null
			if(convertView == null){
				convertView = getActivity().getLayoutInflater().inflate(R.layout.two_line_item, null);
			}
			// find the corresponding item
			AFCourse course = (AFCourse) mCourseList.get(position);
			// configure text
			TextView titleView = (TextView) convertView.findViewById(R.id.item_title);
			titleView.setText(((com.antedeluvia.avgfor.AFCourse) course).getFullName());
			TextView subTitleView = (TextView) convertView.findViewById(R.id.item_subtitle);
			subTitleView.setText(((com.antedeluvia.avgfor.AFCourse) course).getName());
          
			return convertView;
		}	
	}
	
	private class AFCourseHttpTask extends AsyncTask<String, Integer, String>{
		
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
			ArrayAdapter<AFSubject> adapter = new AFCourseAdapter<AFCourse>(mCourseList);
			setListAdapter(adapter);
	    }
	}
}
