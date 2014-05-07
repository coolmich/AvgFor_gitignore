package com.antedeluvia.avgfor;

import org.json.JSONException;
import org.json.JSONObject;

public class AFClass {

    private String mId;
	private String mSection;
	private String mSectionId;
	private String mDay;
	private String mTime;
	private String mFullName;
	private String mProf;
	private String mType;
	private static final String NOLECTURE = "Lecture has no seat information, please add the discussion below";
	
	public AFClass(String id, String section, String sectionId, 
			String day, String time, String name, String prof, String type){
		mId = id;
		mSection = section;
		mSectionId = sectionId;
		mDay = day;
		mTime = time;
		mFullName = name;
		mProf = prof;
		mType = type;				
	}
	
	public AFClass(JSONObject jsobj){
		try {
			mId = jsobj.getString("cls_id");
			mSection = jsobj.getString("cls_section");
			mSectionId = jsobj.getString("cls_section_id");
			mDay = jsobj.getString("cls_day");
			mTime = jsobj.getString("cls_time");
			mFullName = jsobj.getString("cls_course_fullName");
			mProf = jsobj.getString("cls_professor_name");
			mType = jsobj.getString("cls_meeting_type");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    public String getmId() {
        return mId;
    }

    /**
	 * @return the section
	 */
	public String getSection() {
		return mSection;
	}

	/**
	 * @param section the section to set
	 */
	public void setSection(String section) {
		mSection = section;
	}

	/**
	 * @return the sectionId
	 */
	public String getSectionId() {
		return mSectionId;
	}

	/**
	 * @param sectionId the sectionId to set
	 */
	public void setSectionId(String sectionId) {
		mSectionId = sectionId;
	}

	/**
	 * @return the day
	 */
	public String getDay() {
		return mDay;
	}

	/**
	 * @param day the day to set
	 */
	public void setDay(String day) {
		mDay = day;
	}

	/**
	 * @return the time
	 */
	public String getTime() {
		return mTime;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(String time) {
		mTime = time;
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

	/**
	 * @return the prof
	 */
	public String getProf() {
		return mProf;
	}

	/**
	 * @param prof the prof to set
	 */
	public void setProf(String prof) {
		mProf = prof;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return mType;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		mType = type;
	}
		
	public static String getMsgForDialog(AFClass item){
		String msg = null;
		if(item.getSectionId().length() != 0){
			msg = "Add "+item.getType()+" "+item.getSection()+" to watchlist?";
		}else{
			if(item.getType() == "Lecture"){
				msg = AFClass.NOLECTURE;
			}else{
				msg = item.getType() + " has no seat information, please add the lecture above";
			}
		}
		return msg;
	}

}
