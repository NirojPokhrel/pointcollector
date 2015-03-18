package com.niroj.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.niroj.marriagepointcollector.R;

public class CreateNewPlayerActivity extends Activity {
	private Activity mActivity;
	private EditText mEtPlayerName, mEtDisplayName;
	private ImageView mImgView;
	
	@Override
	public void onCreate(Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_new_player);
		mActivity = this;
		
		Button btnTakeImage, btnCreateNewPlayer;
		
		btnTakeImage = (Button) findViewById(R.id.cameraBtn);
		btnCreateNewPlayer = (Button) findViewById(R.id.createNewPlayer);
		mEtPlayerName = (EditText) findViewById(R.id.playerName);
		mEtDisplayName = (EditText) findViewById(R.id.displayName);
		mImgView = (ImageView) findViewById(R.id.userImage);
		
		btnTakeImage.setOnClickListener(mOnClickListener);
		btnCreateNewPlayer.setOnClickListener(mOnClickListener);
	}
	
	private OnClickListener mOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			switch(view.getId()) {
			case R.id.cameraBtn:
				Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				if( takePictureIntent.resolveActivity(getPackageManager()) != null ) {
					mActivity.startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
				}
				break;
			case R.id.createNewPlayer:
				Intent intent = getIntent();
				String str1 = mEtPlayerName.getEditableText().toString();
				String str2 = mEtDisplayName.getEditableText().toString();
				if( str1.length() == 0 ) {
					//Toast to enforce user to fill the text
					
					return;
				}
				if( str2.length() == 0 ) {
					//Toast to enforce user to fill the text
					
					return;
				}
				intent.putExtra( PLAYER_NAME, str1);
				intent.putExtra( DISPLAY_NAME, str2 );
				mActivity.finish();
				break;
				default:
					return;
			}
		}
	};
	
	@Override
	public void onActivityResult( int requestCode, int resultCode, Intent data ) {
		if(resultCode != RESULT_OK ) {
			return;
		}
		
		if( requestCode == REQUEST_IMAGE_CAPTURE ) {
			Bundle extras = data.getExtras();
			Bitmap imageBitmap = (Bitmap) extras.get("data");
			mImgView.setImageBitmap(imageBitmap);
		}
	}
	
	
	public static final String PLAYER_NAME = "player_name";
	public static final String DISPLAY_NAME = "display_name";
	private static final int REQUEST_IMAGE_CAPTURE = 1;
}
