package com.codef1.oldcode;

/**
 * Created by yuzhu on 2017/12/16.
 */

public class Password {
    private long mID;
    private String mTitle;
    private String mValue;

    public Password(long id, String title, String value) {
        this.mID = id;
        this.mTitle = title;
        this.mValue = value;
    }

    public long getID() {
        return mID;
    }

    public void setID(long mID) {
        this.mID = mID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String mValue) {
        this.mValue = mValue;
    }
}
