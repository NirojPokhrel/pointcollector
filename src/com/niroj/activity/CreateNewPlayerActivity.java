package com.niroj.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.niroj.database.DataBaseManager;
import com.niroj.database.UserListTable.UserListData;
import com.niroj.marriagepointcollector.R;
import com.niroj.marriagepointcollector.ZSystem;

public class CreateNewPlayerActivity extends Activity {
	private Activity mActivity;
	private EditText mEtPlayerName, mEtDisplayName;
	private ImageView mImgView;
	private Bitmap mImgBmp;
	private DataBaseManager mDbManager;
	protected String mPlayerName;
	protected String mDisplayName;
	
	@Override
	public void onCreate(Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_new_player);
		mActivity = this;
		mDbManager = DataBaseManager.GetInstance(this);
		
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
				Intent intent = new Intent();
				mPlayerName = mEtPlayerName.getEditableText().toString();
				mDisplayName = mEtDisplayName.getEditableText().toString();
				if( mPlayerName.length() == 0 ) {
					//Toast to enforce user to fill the text
					ZSystem.showCustomToast(mActivity, "Enter the player's full name.");
					return;
				}
				if( mDisplayName.length() == 0 ) {
					//Toast to enforce user to fill the text

					ZSystem.showCustomToast(mActivity, "Enter the player's display name.");
					return;
				}
				intent.putExtra( PLAYER_NAME, mPlayerName);
				intent.putExtra( DISPLAY_NAME, mDisplayName );
				setResult(RESULT_OK, intent );
				mCreateNewPlayer.run();
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
			mImgBmp = imageBitmap;
		}
	}
	
	private Runnable mCreateNewPlayer = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if( mImgBmp == null ) {
				mImgBmp = BitmapFactory.decodeResource(getResources(), R.drawable.image_holder);
			} 
			
			mDbManager.CreateNewPlayer(mPlayerName, mDisplayName, mImgBmp);
			//Finish the activity only after writing into the database
			mActivity.finish();
		}
		
	};
	
	
	public static final String PLAYER_NAME = "player_name";
	public static final String DISPLAY_NAME = "display_name";
	private static final int REQUEST_IMAGE_CAPTURE = 1;
}
