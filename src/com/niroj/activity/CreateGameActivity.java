package com.niroj.activity;

import java.util.ArrayList;
import java.util.List;

import com.niroj.database.DataBaseManager;
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
	private DataBaseManager mDbManager;
	private ArrayList<String> mListOfPlayers;
	private static final int REQUEST_CODE_ADD_NEW_PLAYER = 2;
	@Override
	public void onCreate(Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.create_new_game);
		mActivity = this;
		Button btnCreateGame, btnAddNewPlayer;

		mListOfPlayers = new ArrayList<String>();
		mListOfPlayers.add("Niroj Pokhrel");
		mListOfPlayers.add("Bikalpa Pokhrel");
		mListOfPlayers.add("Bijay Basnet");
		PlayerListAdapter adapter = new PlayerListAdapter(this, mListOfPlayers);

		ListView lv = (ListView) findViewById(R.id.listOfPlayers);
		lv.setAdapter(adapter);
		
		mEtGameName = (EditText) findViewById(R.id.gameName);
		btnCreateGame = (Button) findViewById(R.id.createGame);
		btnAddNewPlayer = (Button) findViewById(R.id.addNewPlayer);
		
		btnCreateGame.setOnClickListener(mOnClickListener);
		btnAddNewPlayer.setOnClickListener(mOnClickListener);
		
		mDbManager = DataBaseManager.GetInstance(this);
	}
	
	private OnClickListener mOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			switch(view.getId()) {
			case R.id.createGame:
				Intent intent = new Intent(mActivity, PointCollectorActivity.class);
				ArrayList<String> arrayList = new ArrayList<String>();
				arrayList.add("niroj");
				arrayList.add("bikalpa");
				arrayList.add("bijay");
				intent.putExtra(ZSystem.NAME_STRING,"Deepawali");
				intent.putStringArrayListExtra(ZSystem.NAME_OF_PLAYERS, arrayList);
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
			mContext = context;
		}

		@Override
		public View getView( int position, View convertView, ViewGroup parent ) {
			String str = getItem(position);
			
			if( convertView == null ) {
				LayoutInflater li = LayoutInflater.from(mContext);
				convertView = li.inflate(R.layout.player_list_item, parent, false );
			}
			
			TextView tv = (TextView) convertView.findViewById(R.id.playerName);
			tv.setText(str);
			CheckBox ckBox = (CheckBox) convertView.findViewById(R.id.checkBoxToAddPlayer);
			ckBox.setOnClickListener( new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//Add the player to the list of the players of the game.
				}
				
			});
			
			return convertView;
		}
	};
}
