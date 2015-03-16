package com.niroj.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


public class UserPointTable {

	/* 
	 * This is used to maintain the table to keep the update of the user point data.
	 * Each user will have one of these tables. So, we can simply use this table to 
	 * keep the point for the user. Each point will go to each user database.
	 */
	
	public static final String USER_POINT_TABLE = "user_point_table"; //+Display name of the user for which this table is being created.
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_ASSOCIATED_GAME = "associated_game";
	public static final String COLUMN_POINT_FOR_EACH_GAME = "game_point";
	
	private CustomSQLiteOpenHelper mSQLiteOpenHelper;
	private SQLiteDatabase mDatabase;
	
	public class UserPointData {
		public int mID;
		public String mAssociatedGameName;
		public int mGamePoint;
	}
	
	public static String GetUserPointTableCreateCmd(String playerName) {
		String cmd = " CREATE TABLE " +
				USER_POINT_TABLE + playerName +
				" ( " +
				COLUMN_ID + " INTEGER PRIMARY KEY AUTO INCREMENT, " +
				COLUMN_ASSOCIATED_GAME + " TEXT, " +
				COLUMN_POINT_FOR_EACH_GAME +" INTEGER " +
				" )";
		return cmd;		
	}
	
	private final String[] ALL_COLUMNS_USER_POINT_TABLE = { COLUMN_ID, COLUMN_ASSOCIATED_GAME, COLUMN_POINT_FOR_EACH_GAME };
	
	public UserPointTable(Context context) {
		mSQLiteOpenHelper = CustomSQLiteOpenHelper.GetInstance(context); 
	}
}
