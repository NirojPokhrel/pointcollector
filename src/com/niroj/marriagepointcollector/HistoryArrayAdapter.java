package com.niroj.marriagepointcollector;

import java.util.ArrayList;
import java.util.List;

import com.niroj.gamedata.DataType;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class HistoryArrayAdapter extends ArrayAdapter<DataType>{
	private Context mContext;

	public HistoryArrayAdapter(Context context, List<DataType> list ) {
		super(context, 0, list);
		mContext = context;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		DataType data = getItem(position);
		
		//if( convertView == null ) {
			LayoutInflater li = LayoutInflater.from(mContext);
			convertView = li.inflate(R.layout.view_history_player_list, parent, false);
		//}

		LinearLayout playerLL = (LinearLayout) convertView.findViewById(R.id.lLPlayerHistory);
		TextView tv = (TextView) convertView.findViewById(R.id.gameNameHistory);

		if(playerLL == null ) {
			ZSystem.LogD("ListOfPlayerNames is null");
			
			return convertView;
		}
		tv.setText(data.mGameName);
		
		for( int i=0; i<data.mPlayerPoint.size(); i++ ) {
			View LLView = li.inflate(R.layout.view_history_player_list_each_row, null, false);
			TextView tvName = (TextView)LLView.findViewById(R.id.playerNameHistory);
			TextView tvPoint = (TextView) LLView.findViewById(R.id.playerPointHistory);
			tvName.setText(data.mPlayers.get(i));
			tvPoint.setText(String.valueOf(data.mPlayerPoint.get(i)));
			playerLL.addView(LLView);
			ZSystem.LogD(" player = " + data.mPlayers.get(i) + " point = " + data.mPlayerPoint.get(i) );
		}
		return convertView;
	}

}
