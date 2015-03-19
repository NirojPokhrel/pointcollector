package com.niroj.activity;

import com.niroj.marriagepointcollector.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

public class PlayerInfoActivity extends Activity {
	@Override
	public void onCreate( Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_info);
		Intent intent = getIntent();
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.image_holder);
		String name = intent.getStringExtra(PLAYER_NAME);
		
		ImageView imgView = (ImageView) findViewById(R.id.userImage);
		imgView.setImageBitmap(bmp);
		TextView tv = (TextView) findViewById(R.id.textBox);
		String htmlStr = "<h2>"+ name +"</h2>";
		htmlStr += "<h3><u>Game Stats</u></h3>";
		htmlStr += "Total Win : " + "330" +"<br>";
		htmlStr += "Total Loss : " + "500" +"<br>";
		htmlStr += "Net : " +"170" +"<br>";
		htmlStr += "Number of Games Player :" + "10";
		
		tv.setText(Html.fromHtml(htmlStr));
		
	}
	
	public static final String PLAYER_NAME = "player_name";
}
