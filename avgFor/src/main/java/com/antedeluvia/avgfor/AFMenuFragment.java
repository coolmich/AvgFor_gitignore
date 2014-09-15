package com.antedeluvia.avgfor;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AFMenuFragment extends Fragment {
    private static final String MENUTAG = "menu tag";
    public static final int LOGOUT = 200;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	}

    public static AFMenuFragment newInstance(String item){
        Bundle args = new Bundle();
        args.putString(MENUTAG, item);
        AFMenuFragment frag = new AFMenuFragment();
        frag.setArguments(args);
        return frag;
    }
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		View view = inflater.inflate(R.layout.menu_row, null);

        String type = getArguments().getString(MENUTAG);

        TextView tx = findActiveView(view, type);
        tx.setBackgroundColor(0xff2A323B);

        configureOnClickListener(view);

        return view;
	}

    private TextView findActiveView(View view, String type){
        if(type.equals("seat")){
            return (TextView)(view.findViewById(R.id.menu_seat_row));
        }else{
            return null;
        }
    }

    private void configureOnClickListener(final View bigView){
        final TextView tx1 = (TextView)bigView.findViewById(R.id.menu_seat_row);
        tx1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AFSeatActivity ac = (AFSeatActivity)getActivity();
                ac.refreshSeatFragment(false);
                ac.menu.toggle();
            }
        });

        TextView tx2 = (TextView)bigView.findViewById(R.id.menu_help_row);
        tx2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AFSeatActivity ac = (AFSeatActivity)getActivity();
                ac.refreshHelpFragment();
                ac.menu.toggle();
            }
        });

        TextView tx3 = (TextView)bigView.findViewById(R.id.menu_feedback_row);
        tx3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("plain/text");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"avgfor@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "Avgfor Feedback");
                startActivity(Intent.createChooser(i, "Send via email"));
            }
        });

        TextView tx4 = (TextView)bigView.findViewById(R.id.menu_logout_row);
        tx4.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                FragmentManager fm = getActivity().getSupportFragmentManager();
                AFAlertFragment.newInstance(LOGOUT).show(fm, "alert");
            }
        });

    }

    // highly hard coded
    public void toggleMenuColor(View view, TextView tx){
        view.findViewById(R.id.menu_seat_row).setBackgroundColor(0xff212226);
        view.findViewById(R.id.menu_help_row).setBackgroundColor(0xff212226);
        tx.setBackgroundColor(0xff2A323B);
    }

}
