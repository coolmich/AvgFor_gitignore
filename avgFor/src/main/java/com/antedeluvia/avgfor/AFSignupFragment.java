package com.antedeluvia.avgfor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.antedeluvia.avgfor.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.cengalabs.flatui.FlatUI;
import com.cengalabs.flatui.views.FlatButton;
import com.cengalabs.flatui.views.FlatCheckBox;
import com.cengalabs.flatui.views.FlatEditText;
import com.cengalabs.flatui.views.FlatRadioButton;
import com.cengalabs.flatui.views.FlatSeekBar;
import com.cengalabs.flatui.views.FlatTextView;
import com.cengalabs.flatui.views.FlatToggleButton;

public class AFSignupFragment extends Fragment {
	
	public EditText text1, text2, text3, text4;
    private LoginSingleton loginuser = LoginSingleton.getInstance();

    public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
	}
	
	public void checkSignup (String result) {
		
		JSONObject jsobj;
		try {
			jsobj = new JSONObject(result);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		System.out.println(jsobj.toString());
			
		try {
            System.err.println("Going in the try catch?????");
			boolean success = jsobj.getBoolean("register");
			if (!success) {
				AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
	            builder1.setMessage("Email/Username has been taken.");
	            builder1.setNeutralButton("OK",
	                    new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int id) {
	                    dialog.cancel();
	                    clearField();
                        loginuser.clear();

	                }
	            });
	            
	            AlertDialog alert11 = builder1.create();
                alert11.show();
			} else {
                /*Intent i = new Intent(getActivity(), AFSeatActivity.class);
                i.setClass(getActivity(), AFSeatActivity.class);
                startActivity(i);*/
                getActivity().finish();
			}
		} catch (JSONException e) {
            // Do nothing
            System.err.println("ERRRRRROR???????");
            e.printStackTrace();
        }
		
	}
	
	public void clearField() {
		text1.setText("");
        text2.setText("");
        text3.setText("");
        text4.setText("");
        text1.requestFocus();
	}
	
	/**
	 * method is used for checking valid email id format.
	 * 
	 * @param email
	 * @return boolean true for valid false for invalid
	 */
	public static boolean isEmailValid(String email) {
	    boolean isValid = false;

	    String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
	    CharSequence inputStr = email;

	    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(inputStr);
	    if (matcher.matches()) {
	        isValid = true;
	    }
	    return isValid;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
		      ViewGroup container, Bundle savedInstanceState) {

	    super.onCreateView(inflater, container, savedInstanceState);
	    View view = inflater.inflate(R.layout.signup_page, container, false);
	    text1 = (EditText) view.findViewById(R.id.editText1);
		text2 = (EditText) view.findViewById(R.id.editText2);
		text3 = (EditText) view.findViewById(R.id.editText3);
		text4 = (EditText) view.findViewById(R.id.editText4);
		text1.requestFocus();
		text1.setHint("Email");
		text2.setHint("Username");
		text3.setHint("Password");
		text4.setHint("Re-enter Password");
		final List<NameValuePair> loginInfo = new ArrayList<NameValuePair>(3);
		
		final FlatButton button = (FlatButton) view.findViewById(R.id.signupBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("unchecked")
			public void onClick(View v) {
                // Perform action on click
            	String email    = text1.getText().toString();
            	String username = text2.getText().toString();
            	String password = text3.getText().toString();
            	String passwd2  = text4.getText().toString();
            	if (!password.equals(passwd2)) {
                    AlertDialog.Builder paswdAlert = new AlertDialog.Builder(getActivity());
                    paswdAlert.setMessage("Passwords do not match!");
                    paswdAlert.setNeutralButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    clearField();
                                }
                            }
                    );
                    AlertDialog alert11 = paswdAlert.create();
                    alert11.show();
                } else if (email.equals("") || username.equals("")
                        || password.equals("") || passwd2.equals("")) {
                    AlertDialog.Builder emptyAlert = new AlertDialog.Builder(getActivity());
                    emptyAlert.setMessage("Field(s) missing.");
                    emptyAlert.setNeutralButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    clearField();
                                }
                            }
                    );
                    AlertDialog alert1 = emptyAlert.create();
                    alert1.show();
            	} else {
	            	loginInfo.add(new BasicNameValuePair("email", email));
	        		loginInfo.add(new BasicNameValuePair("username", username));
	        		loginInfo.add(new BasicNameValuePair("password", password));
                    loginuser.setEmail(email);
                    loginuser.setPwd(password);
	            	new AFSignupHttpTask().execute(loginInfo);
            	}
            }
        });
        
        return view;
	}

	private class AFSignupHttpTask extends AsyncTask<List<NameValuePair>, Integer, String>{
						
		protected void onPostExecute(String result){
			checkSignup(result);
	    }

		@Override
		protected String doInBackground(List<NameValuePair>... arg0) {
			// TODO Auto-generated method stub
			InputStream input = AFGeneralHttpTask.postSignupInfo(arg0[0]);
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