package com.niroj.marriagepointcollector;

import java.util.ArrayList;

import com.niroj.gamedata.DataType;
import com.niroj.gamedata.FileOperations;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class HistoryActivity extends Activity{
	
	private ArrayList<DataType> mGameList;
	private FileOperations mFileOps;
	private HistoryArrayAdapter mHistoryAdapter;
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_history_activity);
		
		mFileOps = FileOperations.getInstance();
		mGameList = mFileOps.readDataFromFile();
		if(mGameList == null ) {
			ZSystem.showCustomToast(this,"History Not Found");
			
			return;
		}
		ListView lv = (ListView)findViewById(R.id.listOfGamesHistory);
		
		mHistoryAdapter = new HistoryArrayAdapter(this, mGameList);
		lv.setAdapter(mHistoryAdapter);
		
	}

}
