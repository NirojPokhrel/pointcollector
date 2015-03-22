package com.niroj.database;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CustomSQLiteOpenHelper extends SQLiteOpenHelper {

	private static CustomSQLiteOpenHelper mOpenHelper;
	private static final String DATABASE_NAME = "pointcollector.db";
	private static final int DATABASE_VERSION = 1;
	
	private CustomSQLiteOpenHelper(Context context, String name,
			CursorFactory factory, int version,
			DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
		// TODO Auto-generated constructor stub
	}


	private CustomSQLiteOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}
	
	private CustomSQLiteOpenHelper(Context context) {
		// TODO Auto-generated constructor stub
		super( context, DATABASE_NAME, null, DATABASE_VERSION );
	}
	
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		/*
		 * Create two tables which has to be created anyhow !!!!
		 */
		db.execSQL(UserListTable.GetUserListTableCreateCmd());
		db.execSQL(GameListTable.GetGameListTableCreateCmd());
	}

	
	public static CustomSQLiteOpenHelper GetInstance(Context context) {
		// TODO Auto-generated constructor stub
		
		if( mOpenHelper == null ) {
			synchronized(CustomSQLiteOpenHelper.class) {
				if( mOpenHelper == null ) {
					mOpenHelper = new CustomSQLiteOpenHelper(context);
				}
			}
		}
		
		return mOpenHelper;
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS " + GameListTable.GAME_LIST_TABLE );
		db.execSQL("DROP TABLE IF EXISTS " + UserListTable.USER_LIST_TABLE);
		
		onCreate(db);
	}

}
