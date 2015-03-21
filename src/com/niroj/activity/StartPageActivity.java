package com.niroj.activity;

import com.niroj.marriagepointcollector.R;
import com.niroj.marriagepointcollector.ZSystem;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;



public class StartPageActivity extends Activity {

	private Activity mActivity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = this;
		
		setContentView(R.layout.dummy_start_page_view);

		SharedPreferences sharedPref = getSharedPreferences(ZSystem.SHARED_PREFERENCES, Context.MODE_PRIVATE);
		boolean isOldGame = sharedPref.getBoolean(ZSystem.SHARED_PREF_CHECK_AVAILABILITY, false);
		ZSystem.LogD("isOldGame : "+isOldGame);
		if( isOldGame ) {

			getFragmentManager().beginTransaction()
					.add(R.id.container, new PreviousGameSelectionOptionFragment()).commit();
		} else {

			getFragmentManager().beginTransaction()
					.add(R.id.container, new NewGameSelectionOptionFragment()).commit();
			
		}
	}
	
	private class PreviousGameSelectionOptionFragment extends Fragment {
		public PreviousGameSelectionOptionFragment() {
			
		}
		
		@Override
		public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceSstate ) {
			View rootView = inflater.inflate(R.layout.prev_activity_options, container, false);
			
			Button continueGameBtn, saveAndStartBtn;
			
			continueGameBtn = (Button) rootView.findViewById(R.id.continueGame);
			saveAndStartBtn = (Button) rootView.findViewById(R.id.saveAndStart);
			
			continueGameBtn.setOnClickListener(mOnClickListener);
			saveAndStartBtn.setOnClickListener(mOnClickListener);
			
			
			return rootView;
		}
		
		private OnClickListener mOnClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent intent = null;
				switch(view.getId()) {
				case R.id.continueGame:
					intent = new Intent(mActivity, PointCollectorActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
					mActivity.startActivity(intent);
					finish();
					break;
				case R.id.saveAndStart:
					mActivity.getFragmentManager().beginTransaction()
							.add(R.id.container, new NewGameSelectionOptionFragment()).commit();
					break;
					default:
						return;
				}
			}
		};
	};
	
	private class NewGameSelectionOptionFragment extends Fragment {
		
		public NewGameSelectionOptionFragment() {
			
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState ) {
			View rootView = inflater.inflate(R.layout.new_activity_main, container, false);

			//setContentView(R.layout.new_activity_main);
			
			Button btnCreateGame, btnPlayers, btnHistory;
			
			btnCreateGame = (Button) rootView.findViewById(R.id.createNewGame);
			btnPlayers = (Button) rootView.findViewById(R.id.checkPlayers);
			btnHistory = (Button) rootView.findViewById(R.id.history);
			
			btnCreateGame.setOnClickListener(mOnClickListener);
			btnPlayers.setOnClickListener(mOnClickListener);
			btnHistory.setOnClickListener(mOnClickListener);
			
			return rootView;
			
		}
		
		private OnClickListener mOnClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				Intent intent = null;
				switch(view.getId()) {
				case R.id.createNewGame:
					intent = new Intent(mActivity, CreateGameActivity.class);
					break;
				case R.id.checkPlayers:
					intent = new Intent(mActivity, PlayerListActivity.class);
					break;
				case R.id.history:
					intent = new Intent(mActivity, GameHistoryActivity.class);
					break;
					default:
						return;
				}
				
				mActivity.startActivity(intent);
			}
		};
	};
}
