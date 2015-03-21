package com.niroj.database;

import java.util.ArrayList;
import java.util.List;

import com.niroj.marriagepointcollector.ZSystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class GameListTable {
	
	/*
	 * There will be only one instance of this. It will keep the list of all the games played so far. And information regarding each
	 * game.
	 * List of Players can be searched from the UserListTable which will have the information about the table of each players.
	 */
	
	public static final String GAME_LIST_TABLE = "game_list_table";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_GAME_NAME = "game_name";
	public static final String COLUMN_PLAYER_NAME = "list_of_players";
	public static final String COLUMN_DATE = "date";
	
	private CustomSQLiteOpenHelper mSQLiteOpenHelper;
	private SQLiteDatabase mDataBase;
	
	public static class GameListData {
		public int mID;
		public String mGameName;
		public String mListOfPlayerDisplayName;
		public String mDate;
	}
	
	public static String GetGameListTableCreateCmd() {
		String cmd = " CREATE TABLE " +
				GAME_LIST_TABLE +
				" ( " +
				COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				COLUMN_GAME_NAME + " TEXT, " +
				COLUMN_PLAYER_NAME + " TEXT, " +
				COLUMN_DATE + " TEXT " +
				" )";
		
		return cmd;
	}
	
	private final String[] ALL_COLUMN_GAME_LIST_TABLE = { COLUMN_ID, COLUMN_GAME_NAME, COLUMN_PLAYER_NAME, COLUMN_DATE }; 
	
	public GameListTable(Context context) {
		mSQLiteOpenHelper = CustomSQLiteOpenHelper.GetInstance(context);
	}

	public void open() {
		mDataBase = mSQLiteOpenHelper.getWritableDatabase();
	}
	
	public void close() {
		mSQLiteOpenHelper.close();
	}
	
	public long InsertGameData( GameListData gameListData ) {
		ContentValues container = new ContentValues();
		
		container.put(COLUMN_GAME_NAME, gameListData.mGameName);
		container.put(COLUMN_PLAYER_NAME, gameListData.mListOfPlayerDisplayName);
		container.put(COLUMN_DATE, gameListData.mDate);
		
		return mDataBase.insert(GAME_LIST_TABLE, null, container);
	}
	
	public List<GameListData> GetAllGameData() {
		List<GameListData> listGames = new ArrayList<GameListData>();
		
		Cursor cursor = mDataBase.query(GAME_LIST_TABLE, ALL_COLUMN_GAME_LIST_TABLE, null, null, null, null, null);
		
		cursor.moveToFirst();
		while( !cursor.isAfterLast() ) {
			ZSystem.LogD("GetAllGameData: Inside cursor loop");
			GameListData gameData = CursorToData(cursor);
			
			listGames.add(gameData);
			cursor.moveToNext();
		}
		
		
		return listGames;
	}
	
	private GameListData CursorToData( Cursor cursor ) {
		GameListData gameData;
		
		gameData = new GameListData();
		gameData.mGameName = cursor.getString(1); // 1 is for GAME_NAME
		gameData.mListOfPlayerDisplayName = cursor.getString(2);
		gameData.mDate = cursor.getString(3);
		
		return gameData;
	}
	
}
