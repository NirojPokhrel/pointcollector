package com.niroj.marriagepointcollector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class ZSystem {
	public static final String TAG = "Zero";
	public static final String STRING_ARRAY_LIST = "StringArray";
	public static final String NAME_STRING = "String";
	
	public static final String NAME_OF_PLAYERS = "PlayerName";
	
	public static final int FINAL_GAME_LIST_ACTIVITY_ID = 1011;
	public static final int FINAL_CREATE_GAME_ACTIVITY_ID = 1012;
	
	public static enum ActivityType
	{
		GAME_LIST_ACTIVITY,
		CREATE_GAME_ACTIVITY
	};
	
	public static void LogD( String cDebugStr ) {
		Log.d(TAG, cDebugStr);
	}
	
	public static void LogE( String cErrorStr ) {
		Log.e(TAG, cErrorStr);
	}
	
	public static void launchGameListActivity( Context cContext ) {
		Intent intent = new Intent( cContext, GameListActivity.class );
		
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		cContext.startActivity(intent);
	}
	
	public static void launchCreateGameActivity( Context cContext ) {
		Intent intent = new Intent( cContext, CreateGameActivity.class );
		
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		cContext.startActivity(intent);
	}
	
	public static void launchHistoryActivity( Context cContext ) {
		Intent intent = new Intent( cContext, HistoryActivity.class);
		
		cContext.startActivity(intent);
	}
	
	public static void showCustomToast( Activity cActivity, String cMsgString ) {
		
		LayoutInflater inflater = cActivity.getLayoutInflater();
		View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) cActivity.findViewById(R.id.toast_layout_root));
		
		TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText(cMsgString);

		Toast toast = new Toast(cActivity.getApplicationContext());
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
	}
}
