package com.niroj.database;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.niroj.database.GameListTable.GameListData;
import com.niroj.database.UserListTable.UserListData;
import com.niroj.database.GameListTable.GameListData;
import com.niroj.database.UserPointTable.UserPointData;
import com.niroj.marriagepointcollector.ZSystem;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class DataBaseManager {
	private static DataBaseManager mDBManager;
	private Context mContext;
	private ArrayList<UserListData> mUserListData;
	private ArrayList<GameListData> mGameListData;
	private ArrayList<ArrayList<UserPointData>> mLLUserPointData;
	
	private UserListTable mUserListTable;
	private GameListTable mGameListTable;
	private ArrayList<UserPointTable> mUserPointTableList;
	private int mNumOfReference = 0;
	
	
	//Current Game Info
	private String mCurrentGameName;
	private ArrayList<UserPointTable> mCurrentPlayersTableList;
	private ArrayList<ArrayList<UserPointData>> mCurrentLLGameUserPointData;
	
	private DataBaseManager(Context context) {
		mContext = context;
		
		mUserListTable = new UserListTable(mContext);
		mGameListTable = new GameListTable(mContext);
		mUserPointTableList = new ArrayList<UserPointTable>();
		
		mUserListData = new ArrayList<UserListData>();
		mGameListData = new ArrayList<GameListData>();
		mLLUserPointData = new ArrayList<ArrayList<UserPointData>>();
		
		mCurrentLLGameUserPointData = new ArrayList<ArrayList<UserPointData>>();
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
		if( mNumOfReference == 0 ) {
			mUserListTable.open();
			mGameListTable.open();
		
			mUserListData = (ArrayList<UserListData>) mUserListTable.GetListOfUser();
			mGameListData = (ArrayList<GameListData>) mGameListTable.GetAllGameData();
			for( int i=0; i<mUserListData.size(); i++ ) {
				UserPointTable userPointTable = new UserPointTable(context, mUserListData.get(i).mPlayerDisplayName);
				
				mUserPointTableList.add(userPointTable);
				mLLUserPointData.add((ArrayList<UserPointData>) userPointTable.GetPointOfUser());
			}
		}
		mNumOfReference++;
	}
	
	public void Close() {

		mNumOfReference--;
		if( mNumOfReference > 0 )
			return;
		mUserListTable.close();
		mGameListTable.close();
	}
	
	public void CreateNewPlayer(String playerName, String displayName, Bitmap image ) {
		
		UserListData userListData = new UserListData();
		
		userListData.mPlayerName = playerName;
		userListData.mPlayerDisplayName = displayName;
		userListData.mImage = ConvetBitmapToByteStream(image);
		
		mUserListTable.InsertUserDataInTable(userListData);
		UserPointTable userPointTable = new UserPointTable(mContext, displayName);
		userPointTable.Open();
		userPointTable.CreateTable();
		mUserPointTableList.add(userPointTable);
		if (mLLUserPointData != null) {
			ArrayList<UserPointData> userPointData = (ArrayList<UserPointData>) userPointTable
					.GetPointOfUser();
			mLLUserPointData.add(userPointData);
		} else {
			ZSystem.LogE("Call Open() to initialize DataBaseManager");
		}
	}
	
	public ArrayList<UserPointData> GetPlayerInfo( String playerDisplayName ) {
		for( int i=0; i<mUserListData.size(); i++ ) {
			UserListData userData = mUserListData.get(i);
			if( userData.mPlayerDisplayName.equals(playerDisplayName) ) {
				ArrayList<UserPointData> userPointData = mLLUserPointData.get(i);
				
				return userPointData;
			}
		}
		
		return null;
	}
	
	public ArrayList<GameListData> GetGameListData() {
		if( mGameListData == null )  {
			ZSystem.LogD("Make sure to call Open() functions first to initialize!!!");
			
			return null;
		}
		
		return mGameListData;
	}
	
	public ArrayList<UserListData> GetUserListData() {
		if( mUserListData == null ) {
			ZSystem.LogD("Make sure to call Open() functions first to initialize!!!");
			
			return null;
		}
		
		return mUserListData;
	}
	
	private byte[] ConvetBitmapToByteStream( Bitmap image ) {
		int bytes = image.getByteCount();
		
		ByteBuffer buffer = ByteBuffer.allocate(bytes);
		image.copyPixelsToBuffer(buffer);
		
		return buffer.array();
	}
	
	//Let user program call it directly
	public static Bitmap ConvertByteStreamToBitmap( byte[] imageByteStream ) {
		Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteStream, 0, imageByteStream.length);
		
		return bitmap;
	}
	
	public void CreateNewGame( String gameName, ArrayList<String> playerNames, String date ) {
		GameListData gameData = new GameListData();
		
		mCurrentGameName = gameName;
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
			ZSystem.LogE("Please call Open() function first to initialize");
			
			return;
		} else {
			for( int i=0; i<playerNames.size(); i++ ) {
				int index = FindIndex(playerNames.get(i));
				
				mCurrentPlayersTableList.add(mUserPointTableList.get(index));
				mCurrentLLGameUserPointData.add(mLLUserPointData.get(index));
			}
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
