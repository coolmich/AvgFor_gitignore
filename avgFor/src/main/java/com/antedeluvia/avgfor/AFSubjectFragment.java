package com.antedeluvia.avgfor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;
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

	}

//    @Override
//    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
//        super.onCreateView(inflater, container, savedInstanceState);
//        getListView().setFastScrollEnabled(true);
//    }
	
	@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
		AFSubject subject = mSubjectList.get(position);
        Intent i = new Intent(getActivity(), AFCourseActivity.class); 
        i.putExtra(AFSubjectFragment.EXTRA_NAME, subject.getId());
        i.setClass(getActivity(), AFCourseActivity.class);
        try{
            startActivityForResult(i, AFSeatActivity.FORADDCLASS);
        }catch(ActivityNotFoundException e){
        	System.err.println("error in startactivity");
        	e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(data == null){
            System.err.println("data is null");
            return;
        }else{
            Intent i = new Intent();
            i.putExtra("classAdded", 1);
            getActivity().setResult(Activity.RESULT_OK, i);
            getActivity().finish();
            System.err.println("class added");
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
	
	private class AFSubjectAdapter<AFSubejct> extends ArrayAdapter implements SectionIndexer{
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

        @Override
        public Object[] getSections() {
            String[] str = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M",
                    "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
            return str;
        }

        @Override
        public int getPositionForSection(int i) {
            String alpha = (String)getSections()[i];
            int index = 0;
            while(index < mSubjectList.size() && !mSubjectList.get(index).getAbbr().startsWith(alpha) ){
                index++;
            }
            return index;
        }

        @Override
        public int getSectionForPosition(int i) {
            String abbr = mSubjectList.get(i).getAbbr();
            int index = 0;
            while(!abbr.startsWith((String)getSections()[index])){
                index++;
            }
            return index;
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
            /*
            getListView().setFastScrollEnabled(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getListView().setFastScrollAlwaysVisible(true);
            }*/
            getListView().setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    switch (scrollState) {
                        case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                            getListView().setFastScrollAlwaysVisible(false);
                            break;
                        case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                            getListView().setFastScrollAlwaysVisible(true);
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                     int totalItemCount) {
                    // nothing to do here -- this gets called many times per scroll
                }

            });
	    }
	}
	
}
