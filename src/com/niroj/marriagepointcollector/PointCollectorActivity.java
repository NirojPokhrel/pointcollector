package com.niroj.marriagepointcollector;

import java.util.ArrayList;

import com.niroj.gamedata.DataType;
import com.niroj.gamedata.FileOperations;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class PointCollectorActivity extends Activity {
	private PointCollectorArrayAdapter mPointCollectorAdapter;
	private ArrayList<ArrayList<Integer>> mLLPoints;
	private LinearLayout mLLayoutEditBox;
	private EditText[] mEditTextArray;
	private String mGameName = null;
	ArrayList<String> mPlayersName;
	private boolean mIsDataDirty = false;
	private FileOperations mFileOps; 
	private Activity mActivity = null;
	private boolean mbIsReady = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();

		setContentView(R.layout.point_collector_view);
		mGameName = intent.getStringExtra(ZSystem.NAME_STRING);
		mPlayersName = intent
				.getStringArrayListExtra(ZSystem.NAME_OF_PLAYERS);

		LinearLayout lLayout = (LinearLayout) findViewById(R.id.listPlayerNames);
		for (int i = 0; i < mPlayersName.size(); i++) {
			TextView textView = new TextView(this);
			LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
					0, LayoutParams.WRAP_CONTENT, 1.0f);

			lParams.setMargins(5, 5, 5, 5);
			textView.setLayoutParams(lParams);
			textView.setText(mPlayersName.get(i));
			textView.setTextSize(24);
			textView.setBackgroundColor(Color.GRAY);
			textView.setTextColor(Color.RED);
			textView.setGravity(Gravity.CENTER);
			lLayout.addView(textView);
		}

		// Initialize List view
		mLLPoints = new ArrayList<ArrayList<Integer>>();
		mPointCollectorAdapter = new PointCollectorArrayAdapter(this, mLLPoints);

		ListView liView = (ListView) findViewById(R.id.listPointCollector);
		liView.setAdapter(mPointCollectorAdapter);

		// Initialize LinearLayout for edittext and hide it untile the button is
		mEditTextArray = new EditText[mPlayersName.size()];
		mLLayoutEditBox = (LinearLayout) findViewById(R.id.editPointCollector);
		for (int i = 0; i < mPlayersName.size(); i++) {
			EditText editView = new EditText(this);
			LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
					0, LayoutParams.WRAP_CONTENT, 1.0f);

			lParams.setMargins(5, 5, 5, 5);
			editView.setLayoutParams(lParams);
			editView.setText("0");
			editView.setTextSize(24);
			editView.setBackgroundColor(Color.WHITE);
			editView.setTextColor(Color.BLACK);
			editView.setGravity(Gravity.CENTER);
			editView.setInputType(EditorInfo.TYPE_NUMBER_FLAG_SIGNED);
			editView.setRawInputType(Configuration.KEYBOARD_QWERTY);
			//editView.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
			mLLayoutEditBox.addView(editView);
			mEditTextArray[i] = editView;
		}
		mLLayoutEditBox.setVisibility(View.GONE);
		
		mFileOps = FileOperations.getInstance();
		
		mActivity = this;
	}
	
//	@Override
//	public void onDestroy() {
//		//Check if the data is dirty then save
//		if( mIsDataDirty ) {
//			//Save it in file
//			DataType dataType = createWritableData();
//			mFileOps.writeDataInFile(dataType);
//			mIsDataDirty = false;
//		}
//	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem menuItem = menu.add(0, FINAL_POINTER_NEW_INPUT_ADD_DONE, 100, "Done")
				.setIcon(
						this.getResources().getDrawable(
								R.drawable.ic_action_done));
		menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		menuItem = menu.add(0, FINAL_POINTER_NEW_INPUT_ADD, 200, "Add").setIcon(
				this.getResources().getDrawable(R.drawable.ic_action_new));
		menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		
		menuItem = menu.add( 0,FINAL_POINTER_SUM, 300, "Sum");
		menuItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);
		
		menuItem = menu.add(0, FINAL_POINTER_SAVE_AND_EXIT, 400, "Save And Exit");
		menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case FINAL_POINTER_NEW_INPUT_ADD_DONE: {
			if( !mbIsReady )
				return true;
			mbIsReady = false;
			int sum = 0;
			ArrayList<Integer> pointList = new ArrayList<Integer>();
			for( int i=0; i<mEditTextArray.length; i++ ) {
				int value = Integer.parseInt(mEditTextArray[i].getText().toString());
				ZSystem.LogD(" value = " + value );
				pointList.add(value);
				sum += value;
			}
			if( sum != 0) {
				ZSystem.showCustomToast(this, "Wrong Value Entered!!!Check Again");
				
				return true;
			}
			mPointCollectorAdapter.add(pointList);
			mLLayoutEditBox.setVisibility(View.GONE);
			mIsDataDirty = true;
			return true;	
		}
		case FINAL_POINTER_NEW_INPUT_ADD: {
			mLLayoutEditBox.setVisibility(View.VISIBLE);
			mbIsReady = true;
			for(int i=0; i<mEditTextArray.length; i++ )
				mEditTextArray[i].setText("0");
			return true;
		}
		case FINAL_POINTER_SAVE_AND_EXIT: {
			//Save the data in the file
			DataType dataType = createWritableData();
			mFileOps.writeDataInFile(dataType);
			mIsDataDirty = false;
			finish();
			return true;
		}
		case FINAL_POINTER_SUM: {
			//Display a dialog box with info of outstanding sum of all the players
			SelectGameListDialog dialog = new SelectGameListDialog();
			dialog.show(getFragmentManager(), "Sum");
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	private DataType createWritableData(){
		DataType dataType = new DataType();
		ArrayList<Integer> pointList = new ArrayList<Integer>();
		boolean isFirstIteration = true;
		
		ZSystem.LogD("Trying to write here");
		for( int i=0; i<mLLPoints.size();i++ ) {
			ArrayList<Integer>  list = mLLPoints.get(i);
			int sum = 0;
			ZSystem.LogD(" Outside for");
			for( int j=0; j<list.size(); j++ ) {
				int val = list.get(j);
				ZSystem.LogD(" val = " + val );
				if( isFirstIteration ) {
					pointList.add(val);
				} else {
					sum = pointList.get(j);
					sum += val;
					pointList.set(j, sum);
					ZSystem.LogD(" sum = " + sum );
				}
			}
			isFirstIteration = false;
		}
		dataType.mGameName = mGameName;
		dataType.mNumberOfPlayers = mPlayersName.size();
		dataType.mPlayers = mPlayersName;
		dataType.mPlayerPoint = pointList;
		printData( dataType );
		
		return dataType;
	}
	
	private ArrayList<Integer> getEachPlayerPoint() {
		ArrayList<Integer> pointList = new ArrayList<Integer>();
		boolean isFirstIteration = true;
		
		ZSystem.LogD("Trying to write here");
		for( int i=0; i<mLLPoints.size();i++ ) {
			ArrayList<Integer>  list = mLLPoints.get(i);
			int sum = 0;
			ZSystem.LogD(" Outside for");
			for( int j=0; j<list.size(); j++ ) {
				int val = list.get(j);
				ZSystem.LogD(" val = " + val );
				if( isFirstIteration ) {
					pointList.add(val);
				} else {
					sum = pointList.get(j);
					sum += val;
					pointList.set(j, sum);
					ZSystem.LogD(" sum = " + sum );
				}
			}
			isFirstIteration = false;
		}
		
		return pointList;
	}
	
	private void printData( DataType dataType ) {
		ZSystem.LogD(dataType.mGameName);
		for( int i=0; i<dataType.mPlayerPoint.size(); i++ ) {
			ZSystem.LogD(" dataType.mPlayers = " + dataType.mPlayers.get(i) +" dataType.mPlayerPoint = " + dataType.mPlayerPoint.get(i));
		}
	}
	
	private class SelectGameListDialog extends DialogFragment implements DialogInterface.OnCancelListener
	{
		@Override
		public Dialog onCreateDialog( Bundle savedInstanceState )
		{	
			AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
			builder.setTitle("Total Sum");
			LayoutInflater li = LayoutInflater.from(mActivity);
			LinearLayout lLayoutView = (LinearLayout)li.inflate(R.layout.show_sum_layout, null);
			
			ArrayList<Integer> listPoint = getEachPlayerPoint();
			
			for( int i=0; i<listPoint.size(); i++ ) {
				View LLView = li.inflate(R.layout.view_history_player_list_each_row, null, false);
				TextView tvName = (TextView)LLView.findViewById(R.id.playerNameHistory);
				TextView tvPoint = (TextView) LLView.findViewById(R.id.playerPointHistory);
				tvName.setText( mPlayersName.get(i));
				tvPoint.setText(String.valueOf(listPoint.get(i)));
				lLayoutView.addView(LLView);
			}
			
			builder.setView(lLayoutView);
			
			return builder.create();
		}
	};

	private final int FINAL_POINTER_NEW_INPUT_ADD_DONE = 1;
	private final int FINAL_POINTER_NEW_INPUT_ADD = 2;
	private final int FINAL_POINTER_SAVE_AND_EXIT = 3;
	private final int FINAL_POINTER_SUM = 4;
}
