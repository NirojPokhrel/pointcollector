package com.niroj.database;

import java.util.ArrayList;
import java.util.List;

import com.niroj.marriagepointcollector.ZSystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


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
	private String mPlayerName;
	
	public static class UserPointData {
		public int mID;
		public String mAssociatedGameName;
		public int mGamePoint;
	}
	
	public static String GetUserPointTableCreateCmd(String playerName) {
		String cmd = " CREATE TABLE " +
				USER_POINT_TABLE + playerName +
				" ( " +
				COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				COLUMN_ASSOCIATED_GAME + " TEXT, " +
				COLUMN_POINT_FOR_EACH_GAME +" INTEGER " +
				" )";
		return cmd;		
	}
	
	private final String[] ALL_COLUMNS_USER_POINT_TABLE = { COLUMN_ID, COLUMN_ASSOCIATED_GAME, COLUMN_POINT_FOR_EACH_GAME };
	
	public UserPointTable(Context context, String playerName) {
		mPlayerName = playerName;
		mSQLiteOpenHelper = CustomSQLiteOpenHelper.GetInstance(context); 
	}
	
	public void Open() {
		mDatabase = mSQLiteOpenHelper.getWritableDatabase();
	}
	
	public void Close() {
		mSQLiteOpenHelper.close();
	}
	
	public long InsertUserPointInTable( UserPointData pointData ) {
		ContentValues content = new ContentValues();
		String tableName;
		
		tableName = USER_POINT_TABLE + mPlayerName;
		content.put(COLUMN_ASSOCIATED_GAME, pointData.mAssociatedGameName);
		content.put(COLUMN_POINT_FOR_EACH_GAME, pointData.mGamePoint);
		
		return mDatabase.insert(tableName, null, content);
	}
	
	public List<UserPointData> GetPointOfUser() {
		List<UserPointData> userPointList;
		String tableName;
		
		ZSystem.LogD("GetPointOfUser Level 0");
		tableName = USER_POINT_TABLE + mPlayerName;
		ZSystem.LogD("GetPointOfUser Level 01");
		userPointList = new ArrayList<UserPointData>();
		ZSystem.LogD("GetPointOfUser Level 02");
		ZSystem.LogD("GetPointOfUser: "+tableName);
		if( mDatabase == null ) {
			ZSystem.LogD("Database is null");
		}
		Cursor cursor = mDatabase.query( tableName, ALL_COLUMNS_USER_POINT_TABLE, null, null, null, null, null);
		ZSystem.LogD("GetPointOfUser Level 1");
		cursor.moveToFirst();

		ZSystem.LogD("GetPointOfUser Level 2");
		while(!cursor.isAfterLast()) {
			UserPointData userPoint;
			
			userPoint = CursorToUserPoint(cursor);
			userPointList.add(userPoint);
			cursor.moveToNext();
		}

		ZSystem.LogD("GetPointOfUser Level 3");
		return userPointList;
	}
	
	private UserPointData CursorToUserPoint( Cursor cursor ) {
		UserPointData pointData = new UserPointData();
		
		pointData.mAssociatedGameName = cursor.getString(1);
		pointData.mGamePoint = cursor.getInt(2);
		
		return pointData;
	}
	
	public static void CreateTable( String playerName, Context context ) {
		SQLiteOpenHelper sqliteOpenHelper = CustomSQLiteOpenHelper.GetInstance(context); 
		SQLiteDatabase sqliteDatabase = sqliteOpenHelper.getWritableDatabase();
		sqliteDatabase.execSQL(GetUserPointTableCreateCmd(playerName));
		ZSystem.LogD("CreateTable cmd: "+GetUserPointTableCreateCmd(playerName));
	}
}
