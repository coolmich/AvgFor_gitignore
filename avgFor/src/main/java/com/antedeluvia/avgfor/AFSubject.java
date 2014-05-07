package com.antedeluvia.avgfor;

import org.json.JSONException;
import org.json.JSONObject;

public class AFSubject {
	private String mId;
	private String mName;
	private String mAbbr;
	
	public AFSubject(String id){
		mId = id;
	}
	
	public AFSubject(String id, String name, String abbr){
		mId = id;
		mName = name;
		mAbbr = abbr;
	}
	
	public AFSubject(JSONObject jsobj){
		try {
			mId = jsobj.getString("subject_id");
			mName = jsobj.getString("subject_name");
			mAbbr = jsobj.getString("subject_sc");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.err.println("error in afsubject constructor with jsonobject");
			e.printStackTrace();
		}
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
	 * @return the abbr
	 */
	public String getAbbr() {
		return mAbbr;
	}

	/**
	 * @param abbr the abbr to set
	 */
	public void setAbbr(String abbr) {
		mAbbr = abbr;
	}

	
}
