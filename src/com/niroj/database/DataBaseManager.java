package com.niroj.database;

import java.io.ByteArrayOutputStream;
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
import android.graphics.Bitmap.CompressFormat;
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
		//Wrong thing to do !!! Be very careful about it !!!
		Open(context);
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
			ZSystem.LogD("Open: Level 1");
			mUserListTable.open();
			ZSystem.LogD("Open: Level 2");
			mGameListTable.open();

			ZSystem.LogD("Open: Level 3");
			mGameListData = (ArrayList<GameListData>) mGameListTable.GetAllGameData();
			ZSystem.LogD("Open: Level 4");
			mUserListData = (ArrayList<UserListData>) mUserListTable.GetListOfUser();
			ZSystem.LogD("Open: Level 5");
			for( int i=0; i<mUserListData.size(); i++ ) {
				ZSystem.LogD("Open: Inside for loop " + mUserListData.size());
				ZSystem.LogD("Open: Inside for loop " + mUserListData.get(i).mPlayerDisplayName );
				UserPointTable userPointTable = new UserPointTable(context, mUserListData.get(i).mPlayerDisplayName);

				userPointTable.Open();
				ZSystem.LogD("Open: Inside for loop Level 0");
				mUserPointTableList.add(userPointTable);
				if( userPointTable == null ) {
					ZSystem.LogD("Open: Inside for loop Level userPointTable is null");
				}
				ZSystem.LogD("Open: Inside for loop Level 1");
				mLLUserPointData.add((ArrayList<UserPointData>) userPointTable.GetPointOfUser());
				ZSystem.LogD("Open: Inside for loop Level 2");
			}
			ZSystem.LogD("Open: Level 7");
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
		ZSystem.LogD("Error entering the user entry: " + mUserListTable.InsertUserDataInTable(userListData));
		mUserListData.add(userListData);

		UserPointTable.CreateTable(displayName, mContext);
		
		UserPointTable userPointTable = new UserPointTable(mContext, displayName);
		userPointTable.Open();
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
//		int bytes = image.getByteCount();
//		
//
//		ZSystem.LogD(" bytes count :" +bytes);
//		ByteBuffer buffer = ByteBuffer.allocate(bytes);
//		image.copyPixelsToBuffer(buffer);
//		if( BitmapFactory.decodeByteArray(buffer.array(), 0, bytes) == null ) {
//			ZSystem.LogD("It's also not fine here");
//		} else {
//			ZSystem.LogD("It's fine here");
//		}
//		return buffer.array();
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		image.compress(CompressFormat.PNG, 70, stream);
	    return stream.toByteArray();
	}
	
	//Let user program call it directly
	public static Bitmap ConvertByteStreamToBitmap( byte[] imageByteStream ) {
		ZSystem.LogD(" ImageByteStream length :" +imageByteStream.length);
		Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteStream, 0, imageByteStream.length);
		if( bitmap == null ) {
			ZSystem.LogD("Bitmap is null");
		}
		return bitmap;
	}
	
	public void CreateNewGame( String gameName, ArrayList<String> displayNames, String date ) {
		GameListData gameData = new GameListData();
		
		gameData.mGameName = gameName;
		gameData.mListOfPlayerDisplayName = PlayerNameToJson(displayNames);
		gameData.mDate = date;
		mGameListTable.InsertGameData(gameData);
	}
	
	private int FindIndex( String str ) {
		for( int i=0; i<mUserListData.size(); i++ ) {
			if( str.equals(mUserListData.get(i).mPlayerName))
				return i;
		}	
		
		return -1;
	}
	
	public UserListData GetUserData(String userDisplayName) {
		ZSystem.LogD("mUserListData.size() :" + mUserListData.size());
		for (int i = 0; i < mUserListData.size(); i++) {
			ZSystem.LogD("userDisplayName from list :"
					+ mUserListData.get(i).mPlayerDisplayName
					+ " From function calll :" + userDisplayName);

			if (mUserListData.get(i).mPlayerDisplayName.equals(userDisplayName))
				return mUserListData.get(i);
		}

		return null;
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
			jFinalObj.put(NAMES_ARRAYLIST, jsonArray);
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
	
	public void InsertGamePoint( String game, ArrayList<String> players, ArrayList<Integer> points) {
		for( int i=0; i<players.size(); i++ ) {
			for( int k=0; k<mUserListData.size(); k++ ) {
				if( players.get(i).equals(mUserListData.get(k).mPlayerDisplayName ) ){
					UserPointTable userTable = mUserPointTableList.get(k);
					UserPointData userPointData = new UserPointData();
					userPointData.mAssociatedGameName = game;
					userPointData.mGamePoint = points.get(i);
					userTable.InsertUserPointInTable(userPointData);
					
					break;
				}
			}
		}
	}
	
	public static String NAMES_ARRAYLIST = "player_name_list"; 
	public static String PLAYER_NAMES= "player_names";
}
