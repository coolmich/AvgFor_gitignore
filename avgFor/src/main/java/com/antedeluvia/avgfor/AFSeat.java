package com.antedeluvia.avgfor;

import org.json.JSONException;
import org.json.JSONObject;

public class AFSeat {

	private String mSectionId;
	private String mType;
	private String mSection;
	private String mDay;
	private String mTime;
	private String mBuilding;
	private String mRoom;
	private String mProf;
	private String mAvailable;
	private String mLimit;
	private String mWaitList;
	private boolean isTitle;
	private String mCourse;
	
	public AFSeat(String sectionId, String type, String section, String day,
			String time, String building, String room, String prof,
			String available, String limit, String waitList) {
		mSectionId = sectionId;
		mType = type;
		mSection = section;
		mDay = day;
		mTime = time;
		mBuilding = building;
		mRoom = room;
		mProf = prof;
		mAvailable = available;
		mLimit = limit;
		mWaitList = waitList;
	}
	
	public AFSeat(String course, String section, String day, String time, 
			String building, String room, String prof){
		mCourse = course;
		mSection = section;
		mDay = day;
		mTime = time;
		mBuilding = building;
		mRoom = room;
		mProf = prof;
		isTitle = true;
	}
	public AFSeat(JSONObject js) {
		// TODO Auto-generated constructor stub
		try {
			mSectionId = js.getString("section_id");
			mType = js.getString("type");
			mSection = js.getString("section");
			mDay = js.getString("day");
			mTime = js.getString("time");
			mBuilding = js.getString("building");
			mLimit = js.getString("limit");
			mRoom = js.getString("room");
			mProf = js.getString("professor");
			mAvailable = js.getString("avaliable");
			mWaitList = js.getString("waitlist");
			isTitle = false;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean whetherTitle(){
		return isTitle;
	}
	public String getCourse(){
		return mCourse;
	}
	public String toString(){
		if(whetherTitle()==true){
			return getCourse()+" this is the title course";
		}else{
			return getSectionId() + "this is the seat";
		}
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
	 * @return the building
	 */
	public String getBuilding() {
		return mBuilding;
	}
	/**
	 * @param building the building to set
	 */
	public void setBuilding(String building) {
		mBuilding = building;
	}
	/**
	 * @return the room
	 */
	public String getRoom() {
		return mRoom;
	}
	/**
	 * @param room the room to set
	 */
	public void setRoom(String room) {
		mRoom = room;
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
	 * @return the available
	 */
	public String getAvailable() {
		return mAvailable;
	}
	/**
	 * @param available the available to set
	 */
	public void setAvailable(String available) {
		mAvailable = available;
	}
	/**
	 * @return the limit
	 */
	public String getLimit() {
		return mLimit;
	}
	/**
	 * @param limit the limit to set
	 */
	public void setLimit(String limit) {
		mLimit = limit;
	}
	/**
	 * @return the waitList
	 */
	public String getWaitList() {
		return mWaitList;
	}
	/**
	 * @param waitList the waitList to set
	 */
	public void setWaitList(String waitList) {
		mWaitList = waitList;
	}
	
}
