package com.niroj.database;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.niroj.database.GameListTable.GameListData;
import com.niroj.database.UserListTable.UserListData;
import com.niroj.database.GameListTable.GameListData;
import com.niroj.database.UserPointTable.UserPointData;

import android.content.Context;
import android.graphics.Bitmap;

public class DataBaseManager {
	private static DataBaseManager mDBManager;
	private Context mContext;
	private ArrayList<UserListData> mUserListData;
	private ArrayList<GameListData> mGameListData;
	private ArrayList<UserListData> mCurrentGamePlayer;
	private ArrayList<ArrayList<UserPointData>> mLLUserPointData;
	
	private UserListTable mUserListTable;
	private GameListTable mGameListTable;
	private ArrayList<UserPointTable> mUserPointList;
	
	private DataBaseManager(Context context) {
		mContext = context;
		
		mUserListTable = new UserListTable(mContext);
		mGameListTable = new GameListTable(mContext);
		mUserPointList = new ArrayList<UserPointTable>();
		
		mUserListData = new ArrayList<UserListData>();
		mGameListData = new ArrayList<GameListData>();
		mCurrentGamePlayer = new ArrayList<UserListData>();
		mLLUserPointData = new ArrayList<ArrayList<UserPointData>>();
	}
	
	public static DataBaseManager GetInstance(Context context) {
		if( mDBManager == null ) {
			synchronized(DataBaseManager.class) {
				if( mDBManager == null ) {
					mDBManager = new DataBaseManager(context);
				}
			}
		}
		
		return mDBManager;
	}

	public void Open(Context context) {
		mUserListTable.open();
		mGameListTable.open();
		
		mUserListData = (ArrayList<UserListData>) mUserListTable.GetListOfUser();
		mGameListData = (ArrayList<GameListData>) mGameListTable.GetAllGameData();
	}
	
	public void Close() {
		mUserListTable.close();
		mGameListTable.close();
	}
	
	public void CreateNewPlayer(String playerName, String displayName, Bitmap image ) {
		
	}
	
	public void CreateNewGame( String gameName, ArrayList<String> playerNames, String date ) {
		GameListData gameData = new GameListData();
		
		gameData.mGameName = gameName;
		gameData.mListOfPlayerName = PlayerNameToJson(playerNames);
		gameData.mDate = date;
		mGameListTable.InsertGameData(gameData);
		
		//MAKE A CLASS TO KEEP ALL THESE VALUES AS A CURRENT GAME INSTANCES. OTHERWISE IT IS LOOKING CONFUSING.
		//All the players are already created that is ensured by top level
		/*
		 * Now create an instance of each player for the current run.
		 */
		if( mUserListData == null ) {
			//Do some initialization first
		} else {
			for( int i=0; i<playerNames.size(); i++ ) {
				int index = FindIndex(playerNames.get(i));
				mCurrentGamePlayer.add(mUserListData.get(index));
			}
		}
		
		//Now time to initialize all the variables
		for( int i=0; i<mCurrentGamePlayer.size(); i++ ) {
			UserPointTable userPointTable = new UserPointTable(mContext, mCurrentGamePlayer.get(i).mPlayerDisplayName);
			//We'll have to set a name of the player too here 
			mUserPointList.add(userPointTable);
		}
		
		for( int i=0; i<mUserPointList.size(); i++ ) {
			ArrayList<UserPointData> userPointData = (ArrayList<UserPointData>) mUserPointList.get(i).GetPointOfUser();
			mLLUserPointData.add( userPointData );
		}
	}
	
	private int FindIndex( String str ) {
		for( int i=0; i<mUserListData.size(); i++ ) {
			if( str.equals(mUserListData.get(i).mPlayerName))
				return i;
		}	
		
		return -1;
	}
	
	public String PlayerNameToJson(ArrayList<String> nameList ) {
		JSONArray jsonArray = new JSONArray();
		
		for( int i=0; i<nameList.size(); i++ ) {
			JSONObject jObj = new JSONObject();
			try {
				jObj.put(PLAYER_NAMES, nameList.get(i));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			jsonArray.put(jObj);
		}
		
		JSONObject jFinalObj = new JSONObject();
		try {
			jFinalObj.put(NAMES_ARRAYLIST, jFinalObj);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jFinalObj.toString();
	}
	
	public ArrayList<String> JsonToPlayerList(String jsonString) throws JSONException {
		ArrayList<String> listPlayer = new ArrayList<String>();
		JSONObject jReader = new JSONObject(jsonString);
		
		JSONArray jArray = jReader.getJSONArray(NAMES_ARRAYLIST);
		for( int i=0; i<jArray.length(); i++ ) {
			JSONObject jObject = jArray.getJSONObject(i);
			String str = jObject.getString(PLAYER_NAMES);
			listPlayer.add(str);
		}
		
		return listPlayer;
	}
	
	public static String NAMES_ARRAYLIST = "player_name_list"; 
	public static String PLAYER_NAMES= "player_names";
}
