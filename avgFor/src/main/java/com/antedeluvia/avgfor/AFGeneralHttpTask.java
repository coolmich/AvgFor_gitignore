package com.antedeluvia.avgfor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.util.Log;

public class AFGeneralHttpTask {

    final private static String ADDCLASSURL = "http://avgfor.com/api/together/create";
    final private static String LOGINURL = "http://avgfor.com/api/user/login";
    final private static String SIGNUPURL = "http://AvgFor.com/api/user/register/";

	public static InputStream getInputStream(String url){
		try {
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();
 
            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
 
            // receive response as inputStream
            InputStream inputStream = httpResponse.getEntity().getContent();
 
            // convert input stream to string
            if(inputStream != null){
                System.err.println("inputstream works");
                return inputStream;
            }
            else{
                System.err.println("inputstream does not work");
                return null;
            }
 
        } catch (Exception e) {
            System.err.println("doInBackground not work");
            Log.e("MYAPP","exception",e);
            return null;
        }
	}

    public static InputStream postAddClass(List<NameValuePair> postaddclass){
        try{
            HttpClient httpClient = new DefaultHttpClient();
            // make a post request object
            HttpPost httpPost = new HttpPost(ADDCLASSURL);
            //System.err.println("jsonString looks as such:"+jsonString);
            // put strin got entity
            try {
                //StringEntity entity = new StringEntity(jsonString, "UTF-8");
                //httpPost.setEntity(entity);
                httpPost.setEntity((HttpEntity) new UrlEncodedFormEntity(postaddclass));
                //set header
                //httpPost.setHeader("Accept", "application/json");
                //httpPost.setHeader("Content-Type", "application/json");
                try {
                    HttpResponse response = httpClient.execute(httpPost);
                    return response.getEntity().getContent();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static InputStream postLoginInfo (List<NameValuePair> info) {
        try {
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            HttpPost hpost = new HttpPost(LOGINURL);

            hpost.setEntity((HttpEntity) new UrlEncodedFormEntity(info));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(hpost);

            // receive response as inputStream
            InputStream inputStream = response.getEntity().getContent();

            // convert input stream to string
            if(inputStream != null){
                System.err.println("inputstream works");
                return inputStream;
            }
            else{
                System.err.println("inputstream does not work");
                return null;
            }

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            System.err.println("doInBackground not work");
            Log.e("MYAPP","exception",e);
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.err.println("doInBackground not work");
            Log.e("MYAPP","exception",e);
            return null;
        }
    }

    public static InputStream postSignupInfo (List<NameValuePair> info) {
        try {
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            HttpPost hpost = new HttpPost(SIGNUPURL);

            hpost.setEntity((HttpEntity) new UrlEncodedFormEntity(info));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(hpost);

            // receive response as inputStream
            InputStream inputStream = response.getEntity().getContent();

            // convert input stream to string
            if(inputStream != null){
                System.err.println("inputstream works");
                return inputStream;
            }
            else{
                System.err.println("inputstream does not work");
                return null;
            }

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            System.err.println("doInBackground not work");
            Log.e("MYAPP","exception",e);
            return null;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.err.println("doInBackground not work");
            Log.e("MYAPP","exception",e);
            return null;
        }
    }

	public static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        char[] buffer = new char[500];
        int num = 0;
        StringBuffer result = new StringBuffer();
        while( (num = bufferedReader.read(buffer, 0, buffer.length)) > 0 ){
            result.append(buffer, 0, num);
        } 
        inputStream.close();
        return result.toString();
 
    }
}
