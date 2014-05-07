package com.antedeluvia.avgfor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AFClassFragment extends ListFragment {

	private ArrayList<AFClass> mClassList;
	private static AFClass mClass;	//used for dialog to determine message
	private final String DIALOG = "dialog tag";
	private final String CLASSURL = "http://avgfor.com/api/classes/";
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		mClassList = new ArrayList<AFClass>();
		String id = getActivity().getIntent().getStringExtra(AFCourseFragment.EXTRA_NAME);
		String url = CLASSURL.concat(id);
		System.err.println(url);
		new AFClassHttpGetTask().execute(url);
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
			mClassList.add(new AFClass(jsobj));
			index++;
		}
	}
	
	@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
		mClass = mClassList.get(position);
		FragmentManager fm = getActivity().getSupportFragmentManager();
		AFClassAlertDialogFragment dialog = new AFClassAlertDialogFragment();
		dialog.show(fm, DIALOG);
	}
	
	private class AFClassAdapter<AFClass> extends ArrayAdapter{
		public AFClassAdapter(ArrayList<AFClass> list){
			super(getActivity(),R.layout.class_list_item, list);
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent){
			// make sure the view is not null
			if(convertView == null){
				convertView = getActivity().getLayoutInflater().inflate(R.layout.class_list_item, null);
			}
			// find the corresponding item
			AFClass item = (AFClass) mClassList.get(position);
			/* configure row padding
			if( !((com.antedeluvia.avgfor.AFClass) item).getType().equals("Lecture")){
				LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.add_class_row);
				layout.setPadding(100, 0, 0, 0);
				System.err.println("set padding");
			}*/
			// configure text
			TextView classTitleView = (TextView) convertView.findViewById(R.id.class_title);
			classTitleView.setText(((com.antedeluvia.avgfor.AFClass) item).getSection());
			TextView profView = (TextView) convertView.findViewById(R.id.class_professor);
			profView.setText(((com.antedeluvia.avgfor.AFClass) item).getProf());
			TextView dayView = (TextView) convertView.findViewById(R.id.class_day);
			dayView.setText(((com.antedeluvia.avgfor.AFClass) item).getDay());
			System.out.println(((com.antedeluvia.avgfor.AFClass) item).getDay());
			TextView timeView = (TextView) convertView.findViewById(R.id.class_time);
			timeView.setText(((com.antedeluvia.avgfor.AFClass) item).getTime());
            // configure image
            ImageView imageView = (ImageView)convertView.findViewById(R.id.class_pic);
            if(((com.antedeluvia.avgfor.AFClass) item).getType().equals("Lecture")){
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.lecture));
            }else if(((com.antedeluvia.avgfor.AFClass) item).getType().equals("Discussion")){
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.discussion));
            }else if(((com.antedeluvia.avgfor.AFClass) item).getType().equals("Laboratory")){
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.lab));
            }else if(((com.antedeluvia.avgfor.AFClass) item).getType().equals("tutorial")){
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.tutorial));
            }else if(((com.antedeluvia.avgfor.AFClass) item).getType().equals("Seminar")){
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.seminar));
            }else{
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.other));
            }
			return convertView;
		}	
	}
	
	private class AFClassHttpGetTask extends AsyncTask<String, Integer, String>{
		
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
			ArrayAdapter<AFClass> adapter = new AFClassAdapter<AFClass>(mClassList);
			setListAdapter(adapter);
	    }
	}

    private class AFClassHttpPostTask extends AsyncTask<List<NameValuePair>, Integer, String>{
        @Override
        protected String doInBackground(List<NameValuePair>... params){
            InputStream input = AFGeneralHttpTask.postAddClass(params[0]);
            try {
                return AFGeneralHttpTask.convertInputStreamToString(input);
            }catch (IOException e){
                System.err.println("error in convert input stream to string");
                e.printStackTrace();
                return null;
            }
        }
        protected void onPostExecute(String result){
            System.err.println("Post request return such: "+result);
        }
    }
	
	public class AFClassAlertDialogFragment extends DialogFragment{
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState){
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

			
			if( mClass.getSectionId().length() != 0 ){
				// Are you sure to add?
				builder.setTitle("Add").setMessage(AFClass.getMsgForDialog(mClass)).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       // User cancelled the dialog
	                	   System.err.println("cancel clicked");
	                   }
	               }).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       // FIRE ZE MISSILES!
                           //JSONObject jsObj = new JSONObject();
                           List<NameValuePair> postaddclass = new ArrayList<NameValuePair>(2);
                           try {
                               //jsObj.put("user_id","22");
                               //jsObj.put("cls_id",mClass.getmId());
                               postaddclass.add(new BasicNameValuePair("user_id", "22"));
                               postaddclass.add(new BasicNameValuePair("cls_id", mClass.getmId()));

                           } catch (Exception e) {
                               e.printStackTrace();
                               System.err.println("json created failed");
                           }
                           //new AFClassHttpPostTask().execute("http://avgfor.com/api/together/create",jsObj.toString());
                           new AFClassHttpPostTask().execute(postaddclass);
	                	   System.err.println("yes clicked");
	                   }
	               });
			}else{
				// no seat information
				builder.setTitle("Oops").setMessage(AFClass.getMsgForDialog(mClass)).setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                       // User cancelled the dialog
	                	   System.err.println("cancel clicked");
	                   }
	               });
			}
			return builder.create();	
		}
	}
	
	
}
