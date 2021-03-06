package com.viewlift.ccavenue.screens;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.viewlift.R;

public class StatusActivity extends Activity {
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_status);
		
		Intent mainIntent = getIntent();
		TextView tv4 = findViewById(R.id.textView1);
		tv4.setText(mainIntent.getStringExtra("transStatus"));
	}
	
	public void showToast(String msg) {
		Toast.makeText(this, "Toast: " + msg, Toast.LENGTH_LONG).show();
	}
} 