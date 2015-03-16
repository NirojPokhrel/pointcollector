package com.niroj.marriagepointcollector;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class HistoryPlayerAdapter extends ArrayAdapter<UserData>{
	private Context mContext;

	public HistoryPlayerAdapter(Context context, List<UserData> objects) {
		// TODO Auto-generated constructor stub
		super(context, 0, objects);
		mContext = context;
	}
	
	@Override
	public View getView( int position, View convertView, ViewGroup parent ) {
		ZSystem.LogD("Ever called ??");
		UserData userData = getItem(position);

		ZSystem.LogD("Level 0");
		//if( convertView == null ) {
			LayoutInflater li = LayoutInflater.from(mContext);
			convertView = li.inflate(R.layout.view_history_player_list_each_row, parent, false);
		//}
			ZSystem.LogD("Level 1");
		TextView tvName = (TextView) convertView.findViewById(R.id.playerNameHistory);
		TextView tvPoint = (TextView) convertView.findViewById(R.id.playerPointHistory);

		ZSystem.LogD("Level 2");
		if( tvName == null ) {
			ZSystem.LogD("tvName is null");
		} 
		if ( tvPoint == null ) {
			ZSystem.LogD("tvPoint is null");
		}
		if( userData == null ) {
			ZSystem.LogD("User Data is null");
		}
		ZSystem.LogD("Level 3 Name = " + userData.mName + " Point = " + userData.mPoint);
		tvName.setText(userData.mName);
		ZSystem.LogD("Level 3 0");
		//tvPoint.setText(userData.mPoint);
		ZSystem.LogD("Level 4");
		
		return convertView;
	}

}
