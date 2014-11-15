package com.antedeluvia.avgfor;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.android.Facebook;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import android.app.AlertDialog;
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

    private static final String TAG = "AFLoginFragment";

    private UiLifecycleHelper uiHelper;

    private LoginSingleton loginuser = LoginSingleton.getInstance();

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private void onSessionStateChange(Session session, SessionState state,
                                      Exception exception) {

        if (state.isOpened()) {
            Log.i(TAG, "Logged in...");
            postUserInfo(session);

        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uiHelper = new UiLifecycleHelper(getActivity(), callback);
        uiHelper.onCreate(savedInstanceState);
        Log.i(TAG, "in here");
    }

	public void checkLoginInfo (String result) {

		JSONObject jsobj;
		try {
			jsobj = new JSONObject(result);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

        String token = null;
        try {
            token = jsobj.getString("token");
            Log.e(TAG, token.toString());
            SharedPreferences pref = getActivity().getSharedPreferences(AFSeatIntentService.USERFILE, 0);
            SharedPreferences.Editor edit = pref.edit();
            edit.putString(AFSeatIntentService.USERIDKEY, token);
            edit.putBoolean(AFSeatIntentService.USERFIRSTKEY, true);
            edit.commit();
            Log.e("d", "token at login fragment is " + token);

            Intent i = new Intent(getActivity(), AFSeatActivity.class);
            i.setClass(getActivity(), AFSeatActivity.class);
            startActivity(i);
            getActivity().finish();

        } catch (JSONException e) {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
            builder1.setMessage("Invalid Email/Password");
            builder1.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            //clearField();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
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
        // For scenarios where the main activity is launched and user
        // session is not null, the session state change notification
        // may not be triggered. Trigger it if it's open/closed.
        Session session = Session.getActiveSession();
        if (session != null &&
                (session.isOpened() || session.isClosed()) ) {
            onSessionStateChange(session, session.getState(), null);
        }

        uiHelper.onResume();
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater,
		      ViewGroup container, Bundle savedInstanceState) {

	    super.onCreateView(inflater, container, savedInstanceState);
	    View view = inflater.inflate(R.layout.login_page, container, false);

        LoginButton authButton = (LoginButton) view.findViewById(R.id.authButton);
        authButton.setFragment(this);
        authButton.setReadPermissions(Arrays.asList("email"));

        return view;
	}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }


    public void postUserInfo(final Session session) {

        // Make an API call to get user data and define a
        // new callback to handle the response.

        final List<NameValuePair> loginInfo = new ArrayList<NameValuePair>(3);
        Request request = Request.newMeRequest(session,
                new Request.GraphUserCallback() {

                    @Override
                    public void onCompleted(GraphUser user, Response response) {
                        // If the response is successful
                        if (session == Session.getActiveSession()) {
                            if (user != null) {
                                String email = user.asMap().get("email").toString();
                                String fbLink = user.getLink();
                                String fbId = user.getId();
                                String name = user.getName();
                                Log.i(TAG, "Logged in successfully. User details are below..");
                                Log.i(TAG, "firstname : " + user.getFirstName());
                                Log.i(TAG, "lastname  : " + user.getLastName());
                                Log.i(TAG, "name      : " + user.getName());
                                Log.i(TAG, "userid    : " + user.getId());
                                Log.i(TAG, "email     : " + user.asMap().get("email"));
                                loginInfo.add(new BasicNameValuePair("email", email));
                                loginInfo.add(new BasicNameValuePair("fbLink", fbLink));
                                loginInfo.add(new BasicNameValuePair("fbId", fbId));
                                loginInfo.add(new BasicNameValuePair("name", name));
                                loginuser.setEmail(email);
                                new AFLoginHttpTask().execute(loginInfo);
                            }
                        }

                        if (response.getError() != null) {
                            Log.i(TAG, "An error is occurred! Message: " + response.getError().getErrorMessage());
                        }

                    }

                });

        request.executeAsync();
    }

	private class AFLoginHttpTask extends AsyncTask<List<NameValuePair>, Integer, String>{

		protected void onPostExecute(String result){
			checkLoginInfo(result);
	    }

		@Override
		protected String doInBackground(List<NameValuePair>... arg0) {
			// TODO Auto-generated method stub
			InputStream input = AFGeneralHttpTask.postLoginInfo2(arg0[0]);
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

