package com.niroj.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.niroj.database.DataBaseManager;
import com.niroj.database.UserPointTable;
import com.niroj.database.GameListTable.GameListData;
import com.niroj.database.UserPointTable.UserPointData;
import com.niroj.marriagepointcollector.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class GameHistoryActivity extends Activity {
	
	private ArrayList<ArrayList<String>> mLListString;
	private GameHistoryAdapter mAdapter;
	private DataBaseManager mDbManager;
	@Override
	public void onCreate(Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_history);
		
		mDbManager = DataBaseManager.GetInstance(this);		
		mLListString = new ArrayList<ArrayList<String>>();
		mAdapter = new GameHistoryAdapter(this, mLListString);
		ListView lv = (ListView) findViewById(R.id.listOfGames);
		lv.setAdapter(mAdapter);
		mGetHistory.run();
	}

	
	private class GameHistoryAdapter extends ArrayAdapter<ArrayList<String>> {
		private Context mContext;
		
		public GameHistoryAdapter(Context context, List<ArrayList<String>> objects) {
			super(context, 0, objects);
			// TODO Auto-generated constructor stub
			mContext = context;
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent ) {
			ArrayList<String> strList = getItem(position);
			
			if( convertView == null ) {
				LayoutInflater lp = LayoutInflater.from(mContext);
				convertView = lp.inflate(R.layout.player_list_browse, parent, false);
			}
			TextView tv = (TextView) convertView.findViewById(R.id.playerName);
			tv.setOnTouchListener(mChangebackgroundListener);
			String htmlString = ConvertToHtmlString(strList);
			tv.setText(Html.fromHtml(htmlString));
			return convertView;
		}
		
		private String ConvertToHtmlString( ArrayList<String> strList ) {
			String str;
			
			str = "<h1>" + strList.get(0) +"</h1>";
			for( int i=1; i<strList.size()-1; i += 2 ) {
				String name = strList.get(i);
				String value = strList.get(i+1);
				str += name + " : " + value + "<br>";
			}
			
			return str;
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
				case MotionEvent.ACTION_UP:
					v.setBackgroundResource(R.drawable.player_list_background);
					return true;
				}
				return false;
			}
		};
		
	};
	
	
	private Runnable mGetHistory = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			ArrayList<GameListData> gameListData = mDbManager.GetGameListData();
			
			for( int i=0; i<gameListData.size(); i++ ) {
				ArrayList<String> listGameInfo = new ArrayList<String>();
				GameListData gameData = gameListData.get(i);
				
				listGameInfo.add(gameData.mGameName);
				String playerJson = gameData.mListOfPlayerDisplayName;
				try {
					ArrayList<String> playerDisplayName = mDbManager.JsonToPlayerList(playerJson);
					for( int j=0; j<playerDisplayName.size(); j++ ) {
						listGameInfo.add(playerDisplayName.get(j));
						ArrayList<UserPointData> userPointTableData = mDbManager.GetPlayerInfo(playerDisplayName.get(j));
						int sum = 0;
						for( int k=0; k<userPointTableData.size(); k++ ) {
							if( userPointTableData.get(k).mAssociatedGameName.equals(gameData.mGameName)) {
								sum += userPointTableData.get(k).mGamePoint;
							}
						}
						listGameInfo.add(Integer.toString(sum));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mAdapter.add(listGameInfo);
			}
		}
		
	};
}
