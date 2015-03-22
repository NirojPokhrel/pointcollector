package com.niroj.activity;

import java.util.ArrayList;

import com.niroj.database.DataBaseManager;
import com.niroj.database.UserListTable.UserListData;
import com.niroj.database.UserPointTable.UserPointData;
import com.niroj.marriagepointcollector.R;
import com.niroj.marriagepointcollector.ZSystem;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

public class PlayerInfoActivity extends Activity {
	private DataBaseManager mDbManager;
	
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_info);
		Intent intent = getIntent();
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.image_holder);
		String displayName = intent.getStringExtra(PLAYER_DISPLAY_NAME);
		String name = intent.getStringExtra(PLAYER_NAME);
		mDbManager = DataBaseManager.GetInstance(this);
		UserListData userListData = mDbManager.GetUserData(displayName);

		if( userListData == null ) {
			ZSystem.LogE("userListData is null");
			
			return;
		}
		ImageView imgView = (ImageView) findViewById(R.id.userImage);
		if( userListData.mImage == null ) {
			ZSystem.LogD("Bitmap byte data is null");
		}
		imgView.setImageBitmap(DataBaseManager.ConvertByteStreamToBitmap( userListData.mImage));
		TextView tv = (TextView) findViewById(R.id.textBox);

		ArrayList<UserPointData> userListPoint = mDbManager.GetPlayerInfo(displayName);

		int win = 0, loss = 0, numOfGames = 0;
		String prev = null;
		for( int i=0; i<userListPoint.size(); i++ ) {
			if( prev != null ) {
				if( !userListPoint.get(i).mAssociatedGameName.equals(prev))
					numOfGames++;
			}
			if( userListPoint.get(i).mGamePoint < 0 ) {
				loss += userListPoint.get(i).mGamePoint;
			} else {
				win += userListPoint.get(i).mGamePoint;
			}
		}
		String htmlStr = convertToHtml( name, win, loss, win-loss, numOfGames);
		tv.setText(Html.fromHtml(htmlStr));
	}
	
	private String convertToHtml(String name, int win, int loss, int net, int numOfGamesPlayed ) {
		String htmlStr = "<h2>"+ name +"</h2>";
		htmlStr += "<h3><u>Game Stats</u></h3>";
		htmlStr += "Total Win : " + win +"<br>";
		htmlStr += "Total Loss : " + loss +"<br>";
		htmlStr += "Net : " + numOfGamesPlayed +"<br>";
		htmlStr += "Number of Games Player :" + "10";
		
		return htmlStr;
	}

	public static final String PLAYER_NAME = "player_name";
	public static final String PLAYER_DISPLAY_NAME = "player_display_name";
}
