package com.antedeluvia.avgfor;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AFHelpFragment extends Fragment {


    public AFHelpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_afhelp, container, false);
        WebView web = (WebView)view.findViewById(R.id.helpWebView);
        web.getSettings().setJavaScriptEnabled(true);
        web.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                return false;
            }
        });
        web.loadUrl("file:///android_asset/help.html");
        return view;
    }



}
