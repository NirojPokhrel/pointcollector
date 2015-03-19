package com.niroj.activity;

import java.util.ArrayList;
import java.util.List;

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
	@Override
	public void onCreate(Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_history);
		
		ArrayList<String> listItem1 = new ArrayList<String>();
		listItem1.add("Dashain");
		listItem1.add("Niroj Pokhrel");
		listItem1.add("300");
		listItem1.add("Bikalpa Pokhrel");
		listItem1.add("500");
		listItem1.add("Bijay Basnet");
		listItem1.add("300");
		

		ArrayList<String> listItem2 = new ArrayList<String>();
		listItem2.add("Tihar");
		listItem2.add("Niroj Pokhrel");
		listItem2.add("300");
		listItem2.add("Bikalpa Pokhrel");
		listItem2.add("500");
		

		ArrayList<String> listItem3 = new ArrayList<String>();
		listItem3.add("Holi");
		listItem3.add("Niroj Pokhrel");
		listItem3.add("300");
		listItem3.add("Bikalpa Pokhrel");
		listItem3.add("500");
		listItem3.add("Bijay Basnet");
		listItem3.add("300");
		listItem3.add("Sandeep Bastola");
		listItem3.add("-300");
		
		mLListString = new ArrayList<ArrayList<String>>();
		mLListString.add(listItem1);
		mLListString.add(listItem2);
		mLListString.add(listItem3);
		mAdapter = new GameHistoryAdapter(this, mLListString);
		ListView lv = (ListView) findViewById(R.id.listOfGames);
		lv.setAdapter(mAdapter);
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
//			"<h1>" +
//				"Game Name"+
//			"</h1>" +
//			"Niroj Pokhrel : 120<br>" +
//			"Bikalpa Pokhrel : 340<br>" +
//			"Bijay Basnet : - 460<br>";
			tv.setText(Html.fromHtml(htmlString));
			//tv.setText(str);
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
}
