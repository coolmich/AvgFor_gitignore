package com.antedeluvia.avgfor;

/**
 * Created by yingbaihe on 11/16/14.
 */
import org.json.JSONException;
import org.json.JSONObject;

public class AFDepartment {
    private String mId;
    private String mName;
    private String mAbbr;

    public AFDepartment(String id){
        mId = id;
    }

    public AFDepartment(String id, String name, String abbr){
        mId = id;
        mName = name;
        mAbbr = abbr;
    }

    public AFDepartment(JSONObject jsobj){
        try {
            mId = jsobj.getString("_id");
            mName = jsobj.getString("name");
            mAbbr = jsobj.getString("code");
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