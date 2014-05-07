package com.antedeluvia.avgfor;

import org.json.JSONException;
import org.json.JSONObject;

public class AFCourse {
	private String mId;
	private String mName;
	private String mNumber;
	private String mFullName;

	public AFCourse(String id, String name, String number, String fullName){
		mId = id;
		mName = name;
		mNumber = number;
		mFullName = fullName;
	}
	
	public AFCourse(JSONObject jsobj){
		try {
			mId = jsobj.getString("c_id");
			mName = jsobj.getString("c_name");
			mNumber = jsobj.getString("c_number");
			mFullName = jsobj.getString("c_fullName");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public String toString(){
		return mName;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return mId;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		mId = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return mName;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		mName = name;
	}

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return mFullName;
	}

	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		mFullName = fullName;
	}
	
}
