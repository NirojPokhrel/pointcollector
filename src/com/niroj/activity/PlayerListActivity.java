package com.niroj.activity;

import java.util.ArrayList;
import java.util.List;

import com.niroj.marriagepointcollector.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class PlayerListActivity extends Activity {
	
	private Activity mActivity;
	private static int REQUEST_CODE_ADD_NEW_PLAYER;
	private ArrayList<String> mListOfPlayers;
	private PlayerListAdapter mListAdapter;
	
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		mActivity = this;
		setContentView(R.layout.player_list);
		Button btn = (Button) findViewById(R.id.addNewPlayer);
		btn.setOnClickListener(mOnClickListener);

		mListOfPlayers = new ArrayList<String>();
		mListOfPlayers.add("Niroj Pokhrel");
		mListOfPlayers.add("Bikalpa Pokhrel");
		mListOfPlayers.add("Bijay Basnet");
		PlayerListAdapter adapter = new PlayerListAdapter(this, mListOfPlayers);

		ListView lv = (ListView) findViewById(R.id.listOfPlayers);
		lv.setAdapter(adapter);
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
	
	private class PlayerListAdapter extends ArrayAdapter<String> {
		private Context mContext;
		
		public PlayerListAdapter(Context context, List<String> objects) {
			super(context, 0, objects);
			// TODO Auto-generated constructor stub
			mContext = context;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent ) {
			String str = getItem(position);
			
			if( convertView == null ) {
				LayoutInflater lp = LayoutInflater.from(mContext);
				convertView = lp.inflate(R.layout.player_list_browse, parent, false);
			}
			TextView tv = (TextView) convertView.findViewById(R.id.playerName);
			tv.setOnTouchListener(mChangebackgroundListener);
			tv.setText(str);
			tv.setTag(str);
			return convertView;
		}
		
		private OnTouchListener mChangebackgroundListener = new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch( event.getAction() ) {
				case MotionEvent.ACTION_DOWN:
					v.setBackgroundResource(R.drawable.player_list_background_touch);
					return true;
				case MotionEvent.ACTION_CANCEL:
					v.setBackgroundResource(R.drawable.player_list_background);
					return false;
				case MotionEvent.ACTION_UP:
					v.setBackgroundResource(R.drawable.player_list_background);
					Intent intent = new Intent(mActivity, PlayerInfoActivity.class);
					String str = (String)v.getTag();
					intent.putExtra(PlayerInfoActivity.PLAYER_NAME, str);
					mActivity.startActivity(intent);
					return true;
				}
				return false;
			}
		};
		
	};
}
