package com.niroj.activity;

import com.niroj.marriagepointcollector.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class CreateGameActivity extends Activity {
	
	private Activity mActivity;
	private EditText mEtGameName;
	private static final int REQUEST_CODE_ADD_NEW_PLAYER = 2;
	@Override
	public void onCreate(Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.create_new_game);
		mActivity = this;
		Button btnCreateGame, btnAddNewPlayer;
		
		mEtGameName = (EditText) findViewById(R.id.gameName);
		btnCreateGame = (Button) findViewById(R.id.createGame);
		btnAddNewPlayer = (Button) findViewById(R.id.addNewPlayer);
		
		btnCreateGame.setOnClickListener(mOnClickListener);
		btnAddNewPlayer.setOnClickListener(mOnClickListener);
	}
	
	private OnClickListener mOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			switch(view.getId()) {
			case R.id.createGame:
				break;
			case R.id.addNewPlayer:
				//This should call start Activity for result
				Intent intent = new Intent(mActivity, CreateNewPlayerActivity.class );
				mActivity.startActivityForResult( intent, REQUEST_CODE_ADD_NEW_PLAYER  );
				break;
			}
		}
	};
	
	@Override
	protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
		if( resultCode != RESULT_OK )
			return;
		if( requestCode == REQUEST_CODE_ADD_NEW_PLAYER ) {
			//Access the data from the create new player
		}
	}
}
