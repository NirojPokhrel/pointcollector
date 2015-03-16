package com.niroj.marriagepointcollector;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PointCollectorArrayAdapter extends
		ArrayAdapter<ArrayList<Integer>> {
	private Context mContext;

	public PointCollectorArrayAdapter(Context context,
			List<ArrayList<Integer>> objects) {
		super(context, 0, objects);
		// TODO Auto-generated constructor stub
		mContext = context;
	}
	
	@SuppressLint("ResourceAsColor") @Override
	public View getView( int position, View convertView, ViewGroup parent) {
		
		ArrayList<Integer> playerPoints;
		LinearLayout lLayout;
		
		playerPoints = getItem(position);
		//if(convertView == null ){
			LayoutInflater li = LayoutInflater.from(mContext);
			convertView = li.inflate(R.layout.dummy_linearlayout, null, false); 
		//}

		lLayout = (LinearLayout) convertView;
		for( int i=0; i<playerPoints.size(); i++ ) {

			TextView textView = new TextView(mContext);
			LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams( 0, LayoutParams.WRAP_CONTENT, 1.0f);
			
			lParams.setMargins(5, 5, 5, 5);
			textView.setLayoutParams(lParams);
			textView.setText(playerPoints.get(i).toString());
			textView.setTextSize(24);
			if( playerPoints.get(i) < 0 )
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
