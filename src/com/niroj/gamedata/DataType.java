package com.niroj.gamedata;

import java.util.ArrayList;

public class DataType implements java.io.Serializable {
	public String mGameName;
	public int mNumberOfPlayers;
	public ArrayList<String> mPlayers;
	public ArrayList<Integer> mPlayerPoint;
	
	public DataType() {
		mGameName = new String();
		mNumberOfPlayers = 0;
		mPlayers = new ArrayList<String>();
		mPlayerPoint = new ArrayList<Integer>();
	}
}
