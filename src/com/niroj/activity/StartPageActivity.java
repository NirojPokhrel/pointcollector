package com.niroj.activity;

import com.niroj.marriagepointcollector.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;



public class StartPageActivity extends Activity {

	private Activity mActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = this;
		
		setContentView(R.layout.new_activity_main);
		
		Button btnCreateGame, btnPlayers, btnHistory;
		
		btnCreateGame = (Button) findViewById(R.id.createNewGame);
		btnPlayers = (Button) findViewById(R.id.checkPlayers);
		btnHistory = (Button) findViewById(R.id.history);
		
		btnCreateGame.setOnClickListener(mOnClickListener);
		btnPlayers.setOnClickListener(mOnClickListener);
		btnHistory.setOnClickListener(mOnClickListener);
	}
	
	
	private OnClickListener mOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			Intent intent = null;
			switch(view.getId()) {
			case R.id.createNewGame:
				intent = new Intent(mActivity, CreateGameActivity.class);
				break;
			case R.id.checkPlayers:
				intent = new Intent(mActivity, PlayerListActivity.class);
				break;
			case R.id.history:
				intent = new Intent(mActivity, GameHistoryActivity.class);
				break;
				default:
					return;
			}
			
			mActivity.startActivity(intent);
		}
	};
}
