package com.antedeluvia.avgfor;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AFMenuFragment extends Fragment {
    private static final String MENUTAG = "menu tag";
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		/*
		mMenuList = new ArrayList<String>();
		mMenuList.add("Seat");
		mMenuList.add("Log out");
		ArrayAdapter<String> adapter = new AFNavigationAdapter(mMenuList);
		*/
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

        return view;
	}

    private TextView findActiveView(View view, String type){
        if(type.equals("seat")){
            return (TextView)(view.findViewById(R.id.menu_seat_row));
        }else{
            return null;
        }
    }
	
	/*
	private class AFNavigationAdapter<String> extends ArrayAdapter{
		public AFNavigationAdapter(ArrayList<String> list){
			super(getActivity(),R.layout.menu_row, list);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent){
			// make sure the view is not null
			if(convertView == null){
				convertView = getActivity().getLayoutInflater().inflate(R.layout.menu_row, null);
			}
			TextView tx = (TextView) convertView.findViewById(R.id.navi_row_title);
			tx.setText(mMenuList.get(position));
			//tx.setTextColor(Color.WHITE);
			return convertView;
		}
	}*/

}
