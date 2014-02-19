package com.lpalac4.eventstart;

import com.lpalac4.eventplanner.R;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.view.Gravity;
import android.view.Menu;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LauncherActivity extends Activity {
	private static final int STARTTIMER = 3000;
	public static int windowHeight;
	public static int windowWidth;
	private FrameLayout finalLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        // Set some window settings.
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		windowHeight = getResources().getDisplayMetrics().heightPixels;
		windowWidth = getResources().getDisplayMetrics().widthPixels;
		
		// Instantiate layouts that will be used in this activity.
		finalLayout = new FrameLayout(this);
		finalLayout.setBackgroundResource(R.drawable.background);
		
		LinearLayout titleLayout = new LinearLayout(this);
		titleLayout.setOrientation(LinearLayout.VERTICAL);
		titleLayout.setGravity(Gravity.LEFT | Gravity.BOTTOM);
		
		
		// Instantiate TextView widgets.
		TextView name = new TextView(this);
		name.setText("Weather & Events App by Luis Palacios");
		TextView poweredBy = new TextView(this);
		poweredBy.setText("This app uses the WorldWeatherOnline and Eventful API's");
		
		TextView eventception = new TextView(this);	
		LinearLayout.LayoutParams titleparams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		titleparams.gravity = Gravity.CENTER;
		eventception.setBackgroundResource(R.drawable.eventception);
		eventception.setLayoutParams(titleparams);
		
		// Add views to proper hierarchy.
		titleLayout.addView(name);
		titleLayout.addView(poweredBy);
		finalLayout.addView(titleLayout);
		finalLayout.addView(eventception);
		
		// Instantiate a handler that will transition to main activity after STARTTIMER(ms).
		Handler timer = new Handler();
        timer.postDelayed(
        		new Runnable() {				
        			@Override
        			public void run() {
        				startActivity(new Intent(LauncherActivity.this, LoginActivity.class));
        				finish();
        			}
        		}, STARTTIMER);
		
		// Set the view to be displayed.
		setContentView(finalLayout);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig){
	  super.onConfigurationChanged(newConfig);
	  setContentView(finalLayout);
	}


}
