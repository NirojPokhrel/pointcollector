package com.niroj.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserListTable {
	
	/*
	 * This is one per application. And it is used to keep the information about all the players who have played so far. In addition
	 * it has information regarding the table name of the corresponding player. Using that table name the point aggregation can be 
	 * done.
	 */
	
	public static final String USER_LIST_TABLE = "user_list_table";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_PLAYER_NAME ="player_name";
	public static final String COLUMN_PLAYER_DISPLAY_NAME = "player_display_name";
	public static final String COLUMN_PLAYER_IMAGE = "player_image";
	public static final String COLUMN_PLAYER_TABLE_NAME = "player_table_name";

	
	private CustomSQLiteOpenHelper mSQLiteOpenHelper;
	private SQLiteDatabase mDataBase;
	
	public class UserListData {
		public int mID;
		public String mPlayerName;
		public String mPlayerDisplayName;
		public byte[] mImage;
		public String mPlayerTableName;
	}

	public static String GetUserListTableCreateCmd() {
		String cmd = " CREATE TABLE " +
				USER_LIST_TABLE + 
				" ( " +
				COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				COLUMN_PLAYER_NAME + " TEXT, " +
				COLUMN_PLAYER_DISPLAY_NAME + " TEXT, " + 
				COLUMN_PLAYER_IMAGE + " BLOB, " +
				COLUMN_PLAYER_TABLE_NAME + " TEXT " +
				" )";
		
		return cmd;
	}
	
	
	private static final String[] ALL_COLUMN_USER_LIST_DATA = { COLUMN_ID, COLUMN_PLAYER_NAME, COLUMN_PLAYER_DISPLAY_NAME,
		COLUMN_PLAYER_IMAGE, COLUMN_PLAYER_TABLE_NAME };
	
	public UserListTable(Context context) {
		mSQLiteOpenHelper = CustomSQLiteOpenHelper.GetInstance(context);
	}
	
	public void open() {
		mDataBase = mSQLiteOpenHelper.getWritableDatabase();
	}
	
	public void close() {
		mSQLiteOpenHelper.close();
		mDataBase = null;
	}
	
	public long InsertUserDataInTable( UserListData userData ) {
		ContentValues contents;
		
		contents = new ContentValues();
		
		contents.put(COLUMN_PLAYER_NAME, userData.mPlayerName);
		contents.put(COLUMN_PLAYER_DISPLAY_NAME, userData.mPlayerDisplayName);
		contents.put(COLUMN_PLAYER_IMAGE, userData.mImage);
		contents.put(COLUMN_PLAYER_TABLE_NAME, userData.mPlayerTableName);
		
		return mDataBase.insert(COLUMN_PLAYER_TABLE_NAME, null, contents);
	}
	
	public List<UserListData> GetListOfUser() {
		List<UserListData> userDataList = new ArrayList<UserListData>();
		
		Cursor cursor = mDataBase.query(COLUMN_PLAYER_TABLE_NAME, ALL_COLUMN_USER_LIST_DATA, null, null, null, null, null);
		cursor.moveToFirst();
		while(cursor.isAfterLast()) {
			UserListData userData = CursorToUserData(cursor);
			
			cursor.moveToNext();
		}
		
		return userDataList;
	}
	
	private UserListData CursorToUserData(Cursor cursor) {
		UserListData userData = new UserListData();
		
		userData.mPlayerName = cursor.getString(1);
		userData.mPlayerDisplayName = cursor.getString(2);
		userData.mImage = cursor.getBlob(3);
		userData.mPlayerTableName = cursor.getString(4);
		
		return userData;
	}
}
