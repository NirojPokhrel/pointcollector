package com.niroj.marriagepointcollector;

import com.niroj.gamedata.FileOperations;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import android.view.Menu;
import android.widget.ArrayAdapter;

public class MainActivity extends Activity implements
		ActionBar.OnNavigationListener {
	
	private Activity mActivity;

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mActivity = this;
		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(
		// Specify a SpinnerAdapter to populate the dropdown list.
				new ArrayAdapter<String>(actionBar.getThemedContext(),
						android.R.layout.simple_list_item_1,
						android.R.id.text1, new String[] {
								getString(R.string.game_options),
								getString(R.string.title_section1),
								getString(R.string.title_section3),
								getString(R.string.delete_history),
								getString(R.string.title_section2),}), this);
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		getActionBar().setSelectedNavigationItem(0); //Reset it
	}

//	@Override
//	public void onSaveInstanceState(Bundle outState) {
//		// Serialize the current dropdown position.
//		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
//				.getSelectedNavigationIndex());
//	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		// When the given dropdown item is selected, show its contents in the
		// container view.
		switch(position) {
		case FINAL_POSITION_GAME_OPTIONS:
			//Launch Game Page
			return true;
		case FINAL_POSITION_START:
			//ZSystem.launchGameListActivity(this);
			ZSystem.launchCreateGameActivity(this);
			return true;
		case FINAL_POSITION_VIEW_HISTORY:
			//Launch Result Viewer Activity
			ZSystem.launchHistoryActivity(this);
			return true;
		case FINAL_POSITION_DELETE_HISTORY:
			DeleteAlertDialog dialog = new DeleteAlertDialog();
			dialog.show(getFragmentManager(), "delete history");
			return true;
		case FINAL_POSITION_QUIT:
			finish();
			return true;
			default:
				return false;
		}
	}
	
	private class DeleteAlertDialog extends DialogFragment {
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState ) {
			AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
			builder.setTitle(R.string.dialog_delete_history);
			builder.setMessage(R.string.dialgo_delete_history_messgae);
			builder.setPositiveButton( "OK", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					FileOperations fileOperations;
					
					fileOperations = FileOperations.getInstance();
					if( fileOperations.deleteHistory() ) {
						ZSystem.showCustomToast(mActivity, "Successfully deleted files");
					} else  {
						ZSystem.showCustomToast(mActivity, "Problem deleting files");
					}
					
				}
			});
			return builder.create();
			
		}
		
	};
	
	private final int FINAL_POSITION_GAME_OPTIONS = 0;
	private final int FINAL_POSITION_START = 1;
	private final int FINAL_POSITION_VIEW_HISTORY = 2;
	private final int FINAL_POSITION_DELETE_HISTORY = 3;
	private final int FINAL_POSITION_QUIT = 4;

}
