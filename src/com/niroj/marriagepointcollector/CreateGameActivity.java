package com.niroj.marriagepointcollector;

import java.util.ArrayList;
import java.util.HashMap;

import com.niroj.marriagepointcollector.ZSystem.ActivityType;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class CreateGameActivity extends Activity {
	private Activity mActivity;
	
	private ArrayList<HashMap<String,String>> mPeopleList;
	private AutoCompleteTextView mAutoCompleteView;
	private Button mAddPlayerBtn;
	private SimpleAdapter mAdapter;
	
	private ArrayList<String> mPlayerList;
	private CustomArrayAdapter mPlayerAddAdapter;
	private EditText mEtGameName;
	private String mStrGameName;
	private ArrayList<String> mPlayerNameList;
	
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate( savedInstanceState );
		setContentView(R.layout.game_info_input);

		mActivity = this;
		mPeopleList = new ArrayList<HashMap<String,String>>();
		createPeopleList();		
		mAutoCompleteView = (AutoCompleteTextView)findViewById(R.id.auto_complete);
		//mAutoCompleteView.setOnItemClickListener(mItemClickListener);
		//mAdapter = new SimpleAdapter( this, mPeopleList, R.layout.autocomplete_item_list, new String[] {"Name"}, new int[] {R.id.autocomplete_text});
		//mAutoCompleteView.setAdapter(mAdapter);
		
		mAddPlayerBtn = (Button) findViewById(R.id.add_player_button);
		mAddPlayerBtn.setOnClickListener( mAddPlayerClickListener );
		
		mPlayerList = new ArrayList<String>();
		mPlayerAddAdapter = new CustomArrayAdapter(this, mPlayerList, ActivityType.CREATE_GAME_ACTIVITY);
		ListView lvPlayers = (ListView)findViewById(R.id.list_of_players);
		lvPlayers.setAdapter(mPlayerAddAdapter);
		
		mEtGameName = (EditText) findViewById(R.id.inputGameName);
		mPlayerNameList = new ArrayList<String>();
	}
	
	@SuppressWarnings("deprecation")
	public void createPeopleList() {
		mPeopleList.clear();
		Cursor people = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		while( people.moveToNext() ) {
			String contactName = people.getString(people.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			HashMap<String,String> name = new HashMap<String,String>();
			name.put("Name", contactName);
			//ZSystem.LogD(contactName);
			mPeopleList.add(name);
		}
		people.close();
		startManagingCursor(people);
	}
	

	private OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			HashMap<String,String> map = (HashMap<String,String>)parent.getItemAtPosition(position);
			String name = map.get("Name");
			mAutoCompleteView.setText(name);
		}
	};
	
	private OnClickListener mAddPlayerClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String name = mAutoCompleteView.getText().toString();
			mAutoCompleteView.getText().clear();
			mAutoCompleteView.setHint(getString(R.string.player_name));
			if( name.length() > 1 ) {
				mPlayerAddAdapter.add(name);
				mPlayerNameList.add(name);
			}
			else {
				ZSystem.showCustomToast( mActivity, "Enter name of length > 1.");
			}
		}
	};
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem menuItem = menu.add(0, FINAL_GAME_INPUT_FILLED,100, "Done").setIcon(this.getResources().getDrawable(R.drawable.ic_action_done));
		menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected( MenuItem item ) {
		switch(item.getItemId()) {
		case FINAL_GAME_INPUT_FILLED:
			if(mPlayerAddAdapter.getCount() < 2 ) {
				ZSystem.showCustomToast(this, "Minimum number of players is 2.");
			} else if( mEtGameName.getText().length() < 1 ) {
				ZSystem.showCustomToast(this, "Enter game name.");
			}
			else {
				mStrGameName = mEtGameName.getText().toString();
				//prepareReturnIntent();
				startPointCollectorActivity();
				finish();
			}
			return true;
			default:
				return false;
		}
	}
	
	public void prepareReturnIntent() {
		Intent returnIntent = new Intent();
		
		returnIntent.putExtra(ZSystem.NAME_STRING, mStrGameName );
		returnIntent.putStringArrayListExtra(ZSystem.STRING_ARRAY_LIST, mPlayerNameList);
		setResult(RESULT_OK, returnIntent);
	}
	
	private void startPointCollectorActivity() {
		Intent intent = new Intent(this, PointCollectorActivity.class);
		intent.putExtra( ZSystem.NAME_STRING, mStrGameName);
		intent.putStringArrayListExtra(ZSystem.NAME_OF_PLAYERS, mPlayerNameList);
		
		startActivity(intent);
	}
	
	private final int FINAL_GAME_INPUT_FILLED = 1;
}
