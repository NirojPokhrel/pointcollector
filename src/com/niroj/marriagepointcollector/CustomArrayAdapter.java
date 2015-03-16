package com.niroj.marriagepointcollector;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

public class CustomArrayAdapter extends ArrayAdapter<String>{
	private Context mContext;
	private Activity mActivity;
	private ZSystem.ActivityType meActivityType;
	private String mName;

	public CustomArrayAdapter(Context context, List<String> list, ZSystem.ActivityType eActivityType ) {
		super(context, 0, list);
		// TODO Auto-generated constructor stub
		mContext = context;
		mActivity = (Activity) mContext;
		meActivityType = eActivityType;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		final String name = getItem(position);
		
		if( convertView == null ) {
			LayoutInflater li = LayoutInflater.from(mContext);
			convertView = li.inflate(R.layout.list_view_row_item, parent, false);
		}
		TextView tvName = (TextView)convertView.findViewById(R.id.playerName);
		tvName.setText(name);
		

		ImageButton removeBtn = (ImageButton) convertView.findViewById(R.id.removePlayer);
		removeBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch( meActivityType ) {
				case GAME_LIST_ACTIVITY:
					mName = name;
					UserConfirmationDialog dialog = new UserConfirmationDialog();
					dialog.show(mActivity.getFragmentManager(), "Confirmation");
					break;
				case CREATE_GAME_ACTIVITY:
					remove(name);
					break;
					default:
						break;
				}
			}
		});
		
		return convertView;
	}
	
	private class UserConfirmationDialog extends DialogFragment {
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
			
			builder.setTitle(R.string.dialog_name);
			builder.setMessage(R.string.dialog_msg);
			builder.setPositiveButton( R.string.dialog_positive, new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					remove(mName);
					dismiss();
				}
			});
			builder.setNegativeButton(R.string.dialog_negative, new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dismiss();
				}
				
			});
			return builder.create();
		}
	}

}
