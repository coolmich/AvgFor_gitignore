package com.antedeluvia.avgfor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
		}
		return builder.create();
	}
}