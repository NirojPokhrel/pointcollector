package com.niroj.marriagepointcollector;

import java.util.ArrayList;

import com.niroj.marriagepointcollector.ZSystem.ActivityType;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SpinnerAdapter;

public class GameListActivity extends Activity 
implements ActionBar.OnNavigationListener {
	
	private CustomArrayAdapter mGameAdapter;
	private ArrayList<String> mGameList;
	private ArrayList<ArrayList<String>> mNameOfPlayersForEachList;
	private Activity mActivity;
	
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView(R.layout.game_list);
		
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		// Set up the dropdown list navigation in the action bar.'
		SpinnerAdapter spinnerAdapter = new ArrayAdapter<String>(actionBar.getThemedContext(), 
				android.R.layout.simple_list_item_1, android.R.id.text1,
				new String[] {
			getString( R.string.game_list),
			getString(R.string.new_game),
			getString(R.string.title_section2)
		});
		actionBar.setListNavigationCallbacks( spinnerAdapter, this);
		
		mGameList = new ArrayList<String>();
		mGameAdapter = new CustomArrayAdapter(this, mGameList, ActivityType.GAME_LIST_ACTIVITY);
		ListView lv = (ListView) findViewById(R.id.gameListView);
		lv.setAdapter(mGameAdapter);
		//populate a game from a file
		
		mActivity = this;
		Button btn = (Button) findViewById(R.id.startGameBtn);
		btn.setOnClickListener(mStartGameListener);
		
		mNameOfPlayersForEachList= new ArrayList<ArrayList<String>>();
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// TODO Auto-generated method stub

		ZSystem.LogD("itemPosition = " + itemPosition +" itemId = "+itemId);
		switch(itemPosition) {
		case 0:
			return true;
		case 1:
			Intent intent = new Intent( this, CreateGameActivity.class);
			startActivityForResult( intent, ZSystem.FINAL_CREATE_GAME_ACTIVITY_ID);
			return true;
		case 2:
			finish();
			return true;
			default:
				return false;
		}
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(resultCode != RESULT_OK)
		{
			ZSystem.LogE("onActivityResult Error received");
			return;
		}
		if( requestCode == ZSystem.FINAL_CREATE_GAME_ACTIVITY_ID ) {
			String gameName = data.getStringExtra(ZSystem.NAME_STRING);
			ZSystem.LogE("Name = "+gameName);
			mGameAdapter.add(gameName);
			ArrayList<String> listPlayers = data.getStringArrayListExtra(ZSystem.STRING_ARRAY_LIST);
			mNameOfPlayersForEachList.add(listPlayers);
			for( int i=0; i<listPlayers.size(); i++ ) {
				ZSystem.LogD("Player["+i+"]"+"="+listPlayers.get(i));
			}
		}
	}

	private OnClickListener mStartGameListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (mGameList.size() == 0)
				ZSystem.showCustomToast(mActivity, "Game not created yet.");
			else if (mGameList.size() == 1) {
				// Start the game
				startPointCollectorActivity(0);
			} else {
				SelectGameListDialog gameDialog = new SelectGameListDialog();
				gameDialog.show(getFragmentManager(), "Game Select");
			}
		}
	};
	
	private class SelectGameListDialog extends DialogFragment implements DialogInterface.OnCancelListener
	{
		@Override
		public Dialog onCreateDialog( Bundle savedInstanceState )
		{
			CharSequence[] chSeqArray;
			
			ZSystem.LogD("level 0");
			AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
			builder.setTitle(R.string.game_select);

			ZSystem.LogD("level 1");
			chSeqArray = new CharSequence[mGameList.size()];
			ZSystem.LogD("level 2");
			for( int i=0; i<mGameList.size(); i++ ) {
				chSeqArray[i] = mGameList.get(i);
				ZSystem.LogD(chSeqArray[i].toString());
			}
			ZSystem.LogD("level 3");
			builder.setItems(chSeqArray, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					ZSystem.LogD("Item clicked = "+ which );
					startPointCollectorActivity(which);
					dismiss();
				}
			});

			ZSystem.LogD("level 5");
			return builder.create();
		}
		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
			ZSystem.showCustomToast(mActivity, "Select a game to proceed");
		}
	};
	
	private void startPointCollectorActivity(int position) {
		Intent intent = new Intent(this, PointCollectorActivity.class);
		intent.putExtra( ZSystem.NAME_STRING, mGameList.get(position));
		intent.putStringArrayListExtra(ZSystem.NAME_OF_PLAYERS, mNameOfPlayersForEachList.get(position));
		
		startActivity(intent);
	}
	
}
