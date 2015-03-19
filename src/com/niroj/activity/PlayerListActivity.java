package com.niroj.activity;

import java.util.ArrayList;
import java.util.List;

import com.niroj.marriagepointcollector.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class PlayerListActivity extends Activity {
	
	private Activity mActivity;
	private static int REQUEST_CODE_ADD_NEW_PLAYER;
	
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		mActivity = this;
		setContentView(R.layout.player_list);
		Button btn = (Button) findViewById(R.id.addNewPlayer);
		btn.setOnClickListener(mOnClickListener);
	}
	
	private OnClickListener mOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			switch (view.getId()) {
			case R.id.addNewPlayer:
				// This should call start Activity for result
				Intent intent = new Intent(mActivity,
						CreateNewPlayerActivity.class);
				mActivity.startActivityForResult(intent,
						REQUEST_CODE_ADD_NEW_PLAYER);
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
