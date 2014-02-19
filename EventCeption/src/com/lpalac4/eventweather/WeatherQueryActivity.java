package com.lpalac4.eventweather;

import com.lpalac4.eventplanner.R;
import com.lpalac4.eventstart.MainMenuActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherQueryActivity extends Activity {

	public static WeatherAPI weatherQuery;
	private TextView cityResults;
	private com.lpalac4.eventweather.ConditionsView[] allConditionsViews;
	private com.lpalac4.eventweather.ConditionsView singleCondition;
	private LinearLayout singleDay;
	private LinearLayout fiveDay;
	private Button setAsHomeButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		setContentView(R.layout.activity_weather_query);
		
		allConditionsViews = new ConditionsView[5];
		
		// Get the passed primitives from the previous Activity.
		String city = getIntent().getStringExtra("city");
		String country = getIntent().getStringExtra("country");

		cityResults = (TextView)findViewById(R.id.weatherquery_userquerytxt);
		cityResults.setText("Your search : " + city + ", " + country);

		
		TextView actualQuery = (TextView)findViewById(R.id.weatherquery_serverquerytxt);
		actualQuery.setText("Actual Query: " + weatherQuery.getConditionsModel()[0].location);

		// Store weather information for one day to be displayed.
		singleCondition = new ConditionsView(this, weatherQuery.getConditionsModel()[0]);

		// Store weather information for five days. 
		for(int i = 0; i < weatherQuery.getConditionsModel().length; i++){		
			allConditionsViews[i] = new ConditionsView(this, weatherQuery.getConditionsModel()[i]);
		}

		FrameLayout centerView = (FrameLayout)findViewById(R.id.forecastframelayout);
		centerView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showFullForecast();

			}
		});

		singleDay = (LinearLayout)findViewById(R.id.singleday_layout);
		singleCondition.getView().setGravity(Gravity.CENTER);
		singleDay.addView(singleCondition.getView());

		fiveDay = (LinearLayout)findViewById(R.id.fiveday_layout);
		for(ConditionsView newView : allConditionsViews){
			fiveDay.addView(newView.getView());
		}

		fiveDay.setVisibility(LinearLayout.GONE);
		//fiveDay.setGravity(Gravity.CENTER);

		setAsHomeButton =(Button)findViewById(R.id.weatherquery_sethomebutton);
		setAsHomeButton.setOnClickListener(new View.OnClickListener() {
			// Using class variables to update the main menu weather information.
			@Override
			public void onClick(View v) {
				ConditionsModel currentC = weatherQuery.getConditionsModel()[0];
				MainMenuActivity.homeCity.setText(currentC.city + "\n" + currentC.currentTemp + "F" + " " + currentC.description);			
			}
		});

	}
	
	/**
	 * Simple method that swaps views from the view of the user.
	 */
	public void showFullForecast(){
		singleDay.setVisibility(View.GONE);
		fiveDay.setVisibility(View.VISIBLE);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.weather_query, menu);
		return true;
	}

}
