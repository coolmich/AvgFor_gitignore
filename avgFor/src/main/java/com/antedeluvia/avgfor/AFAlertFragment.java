package com.antedeluvia.avgfor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class AFAlertFragment extends DialogFragment{
	private final static String DIALOGTYPE = "dialog type";
	
	public static AFAlertFragment newInstance(int type){
		Bundle args = new Bundle();
		args.putInt(DIALOGTYPE, type);
		AFAlertFragment fragment = new AFAlertFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		int type = getArguments().getInt(DIALOGTYPE);
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		switch(type){
		case(AFSeatActivity.INTERNETERR):
			builder.setTitle("Oops").setMessage("Please check your internet, my dear friend.").setNegativeButton(R.string.ok, null);
			break;
		case(AFSeatFragment.EMPTYSEATLIST):
			builder.setTitle("Oops, no class yet").setMessage("Use the add button at the top to add class.").setNegativeButton(R.string.ok, null);
            break;
        case(AFMenuFragment.LOGOUT):
            builder.setTitle("Caveat").setMessage("Are you sure to log out?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    // remove user id in preference
                    SharedPreferences pref = getActivity().getSharedPreferences("userInfo", 0);
                    pref.edit().remove("user_id").commit();
                    // cancel service
                    AFSeatIntentService.startSeatServiceOnSchedule(getActivity(), false);
                    // go to login activity
                    Intent intent = new Intent(getActivity(), AFLoginActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }).setNegativeButton("Cancel", null);
            break;
		}
		return builder.create();
	}
}