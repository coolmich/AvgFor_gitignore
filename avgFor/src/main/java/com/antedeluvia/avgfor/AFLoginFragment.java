package com.antedeluvia.avgfor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.antedeluvia.avgfor.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class AFLoginFragment extends Fragment {
	
	public EditText text1, text2;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}
	
	public void checkLoginInfo (String result) {
		
		JSONObject jsobj;
		String uid = null;
		try {
			jsobj = new JSONObject(result);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
			
		// System.err.println(jsobj.toString());
		try {
			uid = jsobj.getString("user_id");
		} catch (JSONException e) {
			AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
            builder1.setMessage("Invalid Email/Password");
            builder1.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                    clearField();
                }
            });

            AlertDialog alert11 = builder1.create();
            alert11.show();
		}
		
		if (uid != null) {
            LoginSingleton loginuser = LoginSingleton.getInstance();
			loginuser.setUID(uid);
            // save uid to file
            SharedPreferences pref = getActivity().getSharedPreferences(AFSeatIntentService.USERFILE, 0);
            SharedPreferences.Editor edit = pref.edit();
            edit.putString(AFSeatIntentService.USERIDKEY, uid);
            edit.putBoolean(AFSeatIntentService.USERFIRSTKEY, true);
            edit.commit();
            Log.e("d", "uid at login fragment is " + uid);

            Intent i = new Intent(getActivity(), AFSeatActivity.class);
            i.setClass(getActivity(), AFSeatActivity.class);
            startActivity(i);
            getActivity().finish();
		}
		
	}
	
	public void clearField() {
		text1.setText("");
        text2.setText("");
        text1.requestFocus();
	}

    @Override
    public void onResume() {
        super.onResume();
        LoginSingleton loginuser = LoginSingleton.getInstance();
        text1.setText(loginuser.getEmail());
        text2.setText(loginuser.getPwd());
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater,
		      ViewGroup container, Bundle savedInstanceState) {

	    super.onCreateView(inflater, container, savedInstanceState);
	    View view = inflater.inflate(R.layout.login_page, container, false);
	    text1 = (EditText) view.findViewById(R.id.editText1);
		text2 = (EditText) view.findViewById(R.id.editText2);
		text1.setHint("Username");
		text2.setHint("Password");
		final List<NameValuePair> loginInfo = new ArrayList<NameValuePair>(2);
		
		final Button button = (Button) view.findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("unchecked")
			public void onClick(View v) {
                // Perform action on click
            	String username = text1.getText().toString();
            	String password = text2.getText().toString();
        		loginInfo.add(new BasicNameValuePair("username", username));
        		loginInfo.add(new BasicNameValuePair("password", password));
            	new AFLoginHttpTask().execute(loginInfo);
            }
        });
        
        final Button button2 = (Button) view.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.err.println("start new activity");
				Intent i = new Intent(getActivity(), AFSignupActivity.class);
				i.setClass(getActivity(), AFSignupActivity.class);
				startActivity(i);
			}
        });
        return view;
	}

	private class AFLoginHttpTask extends AsyncTask<List<NameValuePair>, Integer, String>{
						
		protected void onPostExecute(String result){
			checkLoginInfo(result);
	    }

		@Override
		protected String doInBackground(List<NameValuePair>... arg0) {
			// TODO Auto-generated method stub
			InputStream input = AFGeneralHttpTask.postLoginInfo(arg0[0]);
			try {
				return AFGeneralHttpTask.convertInputStreamToString(input);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("error in convert input stream to string");
				e.printStackTrace();
				return null;
			}
		}
	}
}

