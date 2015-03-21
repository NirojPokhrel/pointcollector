package com.niroj.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.niroj.database.DataBaseManager;
import com.niroj.database.UserListTable.UserListData;
import com.niroj.marriagepointcollector.R;
import com.niroj.marriagepointcollector.ZSystem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class CreateGameActivity extends Activity {
	
	private Activity mActivity;
	private EditText mEtGameName;
	private String mGameName;
	private DataBaseManager mDbManager;
	private ArrayList<String> mSelectedPlayer;
	private ArrayList<String[]> mListOfPlayers;
	private PlayerListAdapter mPlayerListAdapter;
	private static final int REQUEST_CODE_ADD_NEW_PLAYER = 2;
	@Override
	public void onCreate(Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.create_new_game);
		mActivity = this;
		Button btnCreateGame, btnAddNewPlayer;

		ZSystem.LogD("Level 1");
		mDbManager = DataBaseManager.GetInstance(this);
		ZSystem.LogD("Level 2");
		mListOfPlayers = new ArrayList<String[]>();
		mSelectedPlayer = new ArrayList<String>();
		mPlayerListAdapter = new PlayerListAdapter(this, mListOfPlayers);

		ListView lv = (ListView) findViewById(R.id.listOfPlayers);
		lv.setAdapter(mPlayerListAdapter);
		ZSystem.LogD("Level 3");
		mPopulateThread.run(); //Try doing this to prevent it from being used by both setAdapter class and runnable class
		
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
				Intent intent = new Intent(mActivity, PointCollectorActivity.class);
				//
				mGameName = mEtGameName.getText().toString();
				if( mGameName.length() < 2 ) {
					ZSystem.showCustomToast(mActivity, "Enter the game name");
					
					return;
					
				}
				intent.putExtra(ZSystem.NAME_STRING, mGameName);
				if( mSelectedPlayer.size() < 3 ) {
					ZSystem.showCustomToast(mActivity, "Number of players selected should be > 2");
					
					return;
				}
				intent.putStringArrayListExtra(ZSystem.NAME_OF_PLAYERS, mSelectedPlayer);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
				//Insert currently created game into database
				mInsertIntoDataBase.run();
				mActivity.startActivity(intent);
				break;
			case R.id.addNewPlayer:
				//This should call start Activity for result
				Intent intent1 = new Intent(mActivity, CreateNewPlayerActivity.class );
				mActivity.startActivityForResult( intent1, REQUEST_CODE_ADD_NEW_PLAYER  );
				break;
			}
		}
	};
	
	@Override
	protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
		ZSystem.LogD("Received onActivityResult with resultCode " + resultCode +" requestCode " + requestCode);
		if( resultCode != RESULT_OK )
			return;
		if( requestCode == REQUEST_CODE_ADD_NEW_PLAYER ) {

			//Access the data from the create new player
			String[] str = new String[2];
			
			ZSystem.LogD("Level 0");
			str[0] = data.getStringExtra(CreateNewPlayerActivity.PLAYER_NAME);
			ZSystem.LogD("Level 1");
			str[1] = data.getStringExtra(CreateNewPlayerActivity.DISPLAY_NAME);
			ZSystem.LogD("Level 2");
			mPlayerListAdapter.add(str);
			ZSystem.LogD("Level 3");
			//mPlayerListAdapter.notifyDataSetChanged();
		}
		ZSystem.LogD("Level 4");
	}
	
	private class PlayerListAdapter extends ArrayAdapter<String[]> {

		private Context mContext;
		public PlayerListAdapter(Context context, List<String[]> objects) {
			super(context, 0, objects);
			mContext = context;
		}

		@Override
		public View getView( int position, View convertView, ViewGroup parent ) {
			final String[] str = getItem(position);
			
			if( convertView == null ) {
				LayoutInflater li = LayoutInflater.from(mContext);
				convertView = li.inflate(R.layout.player_list_item, parent, false );
			}
			
			TextView tv = (TextView) convertView.findViewById(R.id.playerName);
			tv.setText(str[0]);
			CheckBox ckBox = (CheckBox) convertView.findViewById(R.id.checkBoxToAddPlayer);
			ckBox.setOnClickListener( new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//Add the player to the list of the players of the game.
					CheckBox ck = (CheckBox) v;
					boolean isChecked = ck.isChecked();
					if( isChecked ) {
						//Add it in the selected list
						mSelectedPlayer.add(str[1]);
					} else {
						//Remove from the selected list
						mSelectedPlayer.remove(str[1]);
					}
				}
				
			});
			
			return convertView;
		}
	};
	
	private Runnable mPopulateThread = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			ArrayList<UserListData> userListData = mDbManager.GetUserListData();
			for( int i=0; i<userListData.size(); i++ ) {
				String[] str = new String[2];
				str[0] = userListData.get(i).mPlayerName;
				str[1] = userListData.get(i).mPlayerDisplayName;
 				mListOfPlayers.add(str);
			}
		}
		
	};
	
	private Runnable mInsertIntoDataBase = new Runnable() {
		@Override
		public void run() {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // Set your date format
			String currentDate = sdf.format(cal.getTime());
			
			mDbManager.CreateNewGame( mGameName, mSelectedPlayer, currentDate );
			mActivity.finish();
		}
	};
}
