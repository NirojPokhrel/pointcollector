package com.niroj.activity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.niroj.database.DataBaseManager;
import com.niroj.database.UserPointTable.UserPointData;
import com.niroj.gamedata.DataType;
import com.niroj.gamedata.FileOperations;
import com.niroj.marriagepointcollector.R;
import com.niroj.marriagepointcollector.ZSystem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
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
	//private FileOperations mFileOps;
	private Activity mActivity = null;
	private boolean mbIsReady = false;
	SharedPreferences.Editor mPrefEditor;
	private DataBaseManager mDbManager;
	private ArrayList<Integer> mCurrentIntegerList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = this;
		mDbManager = DataBaseManager.GetInstance(this);

		SharedPreferences sharedPref = getSharedPreferences(ZSystem.SHARED_PREFERENCES, Context.MODE_PRIVATE);
		mPrefEditor = sharedPref.edit();
		

		// Initialize List view
		mLLPoints = new ArrayList<ArrayList<Integer>>();
		mPointCollectorAdapter = new PointCollectorArrayAdapter(this, mLLPoints);
		
		boolean isOldGame = sharedPref.getBoolean(ZSystem.SHARED_PREF_CHECK_AVAILABILITY, false);
		if( isOldGame ) {
			Set<String> playerSet = sharedPref.getStringSet(ZSystem.SHARED_PREF_SET_OF_PLAYERS, null);
			//Check if new ArrayList<> has to be created ?? Most probably yes
			//Initialize the array as well here from the previous data
			mPlayersName = new ArrayList<String>(playerSet);
			mGameName = sharedPref.getString(ZSystem.SHARED_PREF_GAME_NAME, null);
		} else {
			Intent intent = getIntent();
			mGameName = intent.getStringExtra(ZSystem.NAME_STRING);
			mPlayersName = intent.getStringArrayListExtra(ZSystem.NAME_OF_PLAYERS);
			
		}

		setContentView(R.layout.point_collector_view);

		setTitle(mGameName);
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
			// editView.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
			mLLayoutEditBox.addView(editView);
			mEditTextArray[i] = editView;
		}
		mLLayoutEditBox.setVisibility(View.GONE);

		//mFileOps = FileOperations.getInstance();
		if(isOldGame) 
			PopulateList();
	}
	
	private void PopulateList() {
		ArrayList<ArrayList<Integer>> llIntegerArray = new ArrayList<ArrayList<Integer>>();
		int count = 0;
		
		for( int i=0; i<mPlayersName.size(); i++ ) {
			ArrayList<UserPointData> userPointList = mDbManager.GetPlayerInfo(mPlayersName.get(i));
			count = 0;
			for( int j=0; j<userPointList.size(); j++ ) {
				if( userPointList.get(j).mAssociatedGameName.equals(mGameName)) {
					ArrayList<Integer> arrayList;
					if( i== 0 ) {
						arrayList = new ArrayList<Integer>();
						llIntegerArray.add(arrayList);
					} else {
						arrayList = llIntegerArray.get(count++);
					}
					
					arrayList.add(userPointList.get(j).mGamePoint);
				}
			}
		}

		for( int i=0; i<llIntegerArray.size(); i++ ) {
			mPointCollectorAdapter.add(llIntegerArray.get(i));
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		// Set the shared preferences here
		if(mIsDataDirty) {
			if( mPrefEditor == null ) {
				ZSystem.LogD("Pref Editor is null");
				return;
			}
			mPrefEditor.putBoolean(ZSystem.SHARED_PREF_CHECK_AVAILABILITY, true);
			mPrefEditor.putString(ZSystem.SHARED_PREF_GAME_NAME, mGameName);
			Set<String> set = new HashSet<String>(mPlayersName);
			mPrefEditor.putStringSet(ZSystem.SHARED_PREF_SET_OF_PLAYERS, set);
			ZSystem.LogD("All the values have been stored!!!");
			mPrefEditor.commit();
		} else {
			mPrefEditor.putBoolean(ZSystem.SHARED_PREF_CHECK_AVAILABILITY, false);
			mPrefEditor.commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuItem menuItem = menu.add(0, FINAL_POINTER_NEW_INPUT_ADD_DONE, 100,
				"Done").setIcon(
				this.getResources().getDrawable(R.drawable.ic_action_done));
		menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		menuItem = menu.add(0, FINAL_POINTER_NEW_INPUT_ADD, 200, "Add")
				.setIcon(
						this.getResources().getDrawable(
								R.drawable.ic_action_new));
		menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		menuItem = menu.add(0, FINAL_POINTER_SUM, 300, "Sum");
		menuItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);

		menuItem = menu.add(0, FINAL_POINTER_SAVE_AND_EXIT, 400,
				"Save And Exit");
		menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case FINAL_POINTER_NEW_INPUT_ADD_DONE: {
			ZSystem.LogD("Inside FINAL_POINTER_NEW_INPUT_ADD_DONE");
			if (!mbIsReady)
				return true;
			mbIsReady = false;
			int sum = 0;
			ArrayList<Integer> pointList = new ArrayList<Integer>();
			for (int i = 0; i < mEditTextArray.length; i++) {
				int value = Integer.parseInt(mEditTextArray[i].getText()
						.toString());
				pointList.add(value);
				sum += value;
			}
			if (sum != 0) {
				mbIsReady = true;
				ZSystem.showCustomToast(this,
						"Wrong Value Entered!!!Check Again");

				return false;
			}
			mPointCollectorAdapter.add(pointList);
			mLLayoutEditBox.setVisibility(View.GONE);
			mIsDataDirty = true;

			if(mLLPoints.size() % 6 == 0 ) {
				WarningDialogForKatFad dialog1 = new WarningDialogForKatFad();
				dialog1.show(getFragmentManager(), "Warning");
			}
			mCurrentIntegerList = pointList;
			mWriteInToDataBase.run();
			
			return true;
		}
		case FINAL_POINTER_NEW_INPUT_ADD: {
			mLLayoutEditBox.setVisibility(View.VISIBLE);
			mbIsReady = true;
			for (int i = 0; i < mEditTextArray.length; i++)
				mEditTextArray[i].setText("0");
			return true;
		}
		case FINAL_POINTER_SAVE_AND_EXIT: {
			// Save the data in the file
			DataType dataType = createWritableData();
			//mFileOps.writeDataInFile(dataType);
			mIsDataDirty = false;
			finish();
			return true;
		}
		case FINAL_POINTER_SUM: {
			// Display a dialog box with info of outstanding sum of all the
			// players
			SelectGameListDialog dialog = new SelectGameListDialog();
			dialog.show(getFragmentManager(), "Sum");
			return true;
		}
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private DataType createWritableData() {
		DataType dataType = new DataType();
		ArrayList<Integer> pointList = new ArrayList<Integer>();
		boolean isFirstIteration = true;

		ZSystem.LogD("Trying to write here");
		for (int i = 0; i < mLLPoints.size(); i++) {
			ArrayList<Integer> list = mLLPoints.get(i);
			int sum = 0;
			ZSystem.LogD(" Outside for");
			for (int j = 0; j < list.size(); j++) {
				int val = list.get(j);
				ZSystem.LogD(" val = " + val);
				if (isFirstIteration) {
					pointList.add(val);
				} else {
					sum = pointList.get(j);
					sum += val;
					pointList.set(j, sum);
					ZSystem.LogD(" sum = " + sum);
				}
			}
			isFirstIteration = false;
		}
		dataType.mGameName = mGameName;
		dataType.mNumberOfPlayers = mPlayersName.size();
		dataType.mPlayers = mPlayersName;
		dataType.mPlayerPoint = pointList;
		printData(dataType);

		return dataType;
	}

	private ArrayList<Integer> getEachPlayerPoint() {
		ArrayList<Integer> pointList = new ArrayList<Integer>();
		boolean isFirstIteration = true;

		for (int i = 0; i < mLLPoints.size(); i++) {
			ArrayList<Integer> list = mLLPoints.get(i);
			int sum = 0;
			ZSystem.LogD(" Outside for");
			for (int j = 0; j < list.size(); j++) {
				int val = list.get(j);
				ZSystem.LogD(" val = " + val);
				if (isFirstIteration) {
					pointList.add(val);
				} else {
					sum = pointList.get(j);
					sum += val;
					pointList.set(j, sum);
					ZSystem.LogD(" sum = " + sum);
				}
			}
			isFirstIteration = false;
		}

		return pointList;
	}

	private void printData(DataType dataType) {
		ZSystem.LogD(dataType.mGameName);
		for (int i = 0; i < dataType.mPlayerPoint.size(); i++) {
			ZSystem.LogD(" dataType.mPlayers = " + dataType.mPlayers.get(i)
					+ " dataType.mPlayerPoint = "
					+ dataType.mPlayerPoint.get(i));
		}
	}

	private class SelectGameListDialog extends DialogFragment implements
			DialogInterface.OnCancelListener {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
			builder.setTitle("Total Sum");
			LayoutInflater li = LayoutInflater.from(mActivity);
			LinearLayout lLayoutView = (LinearLayout) li.inflate(
					R.layout.show_sum_layout, null);

			ArrayList<Integer> listPoint = getEachPlayerPoint();

			for (int i = 0; i < listPoint.size(); i++) {
				View LLView = li
						.inflate(R.layout.view_history_player_list_each_row,
								null, false);
				TextView tvName = (TextView) LLView
						.findViewById(R.id.playerNameHistory);
				TextView tvPoint = (TextView) LLView
						.findViewById(R.id.playerPointHistory);
				tvName.setText(mPlayersName.get(i));
				tvPoint.setText(String.valueOf(listPoint.get(i)));
				lLayoutView.addView(LLView);
			}

			builder.setView(lLayoutView);

			return builder.create();
		}
	};


	private class WarningDialogForKatFad extends DialogFragment implements
			DialogInterface.OnCancelListener {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
			builder.setTitle("KATFAD");
			builder.setMessage("Number of Games completed : " + (mLLPoints.size()) );
			builder.setPositiveButton("OK", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					dismiss();
				}
			});

			return builder.create();
		}
	};
	
	public class PointCollectorArrayAdapter extends
			ArrayAdapter<ArrayList<Integer>> {
		private Context mContext;

		public PointCollectorArrayAdapter(Context context,
				List<ArrayList<Integer>> objects) {
			super(context, 0, objects);
			// TODO Auto-generated constructor stub
			mContext = context;
		}

		@SuppressLint("ResourceAsColor")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ArrayList<Integer> playerPoints;
			LinearLayout lLayout;

			playerPoints = getItem(position);
			// if(convertView == null ){
			LayoutInflater li = LayoutInflater.from(mContext);
			convertView = li.inflate(R.layout.dummy_linearlayout, null, false);
			// }

			lLayout = (LinearLayout) convertView;
			for (int i = 0; i < playerPoints.size(); i++) {

				TextView textView = new TextView(mContext);
				LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
						0, LayoutParams.WRAP_CONTENT, 1.0f);

				lParams.setMargins(5, 5, 5, 5);
				textView.setLayoutParams(lParams);
				textView.setText(playerPoints.get(i).toString());
				textView.setTextSize(24);
				if (playerPoints.get(i) < 0)
					textView.setBackgroundColor(0xFFF7AFAB);
				else
					textView.setBackgroundColor(0xFFDACBF5);
				textView.setTextColor(Color.BLACK);
				textView.setGravity(Gravity.CENTER);
				lLayout.addView(textView);
			}
			return lLayout;

		}

	}
	
	private Runnable mWriteInToDataBase = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			mDbManager.InsertGamePoint(mGameName, mPlayersName, mCurrentIntegerList);
		}
		
	};

	private final int FINAL_POINTER_NEW_INPUT_ADD_DONE = 1;
	private final int FINAL_POINTER_NEW_INPUT_ADD = 2;
	private final int FINAL_POINTER_SAVE_AND_EXIT = 3;
	private final int FINAL_POINTER_SUM = 4;
}
