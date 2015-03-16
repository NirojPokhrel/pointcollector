package com.niroj.marriagepointcollector;

public class UserData {
	public String mName;
	public int mPoint;
	
	public UserData( String name, int point ) {
		mName = new String(name);
		mPoint = point;
	}
	
	public String getName() {
		return mName;
	}
	
	public int getPoint() {
		return mPoint;
	}
}
