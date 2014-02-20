package com.lpalac4.eventstart;

import com.lpalac4.eventplanner.EventAPI;
import com.lpalac4.eventplanner.EventsDetailsActivity;
import com.lpalac4.eventplanner.EventsQueryActivity;
import com.lpalac4.eventplanner.JukeBoxActivity;
import com.lpalac4.eventplanner.R;
import com.lpalac4.eventweather.WeatherAPI;
import com.lpalac4.eventweather.WeatherQueryActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

/**
 * Main activity used for the application.  Allows a user to enter their query into two different edit text boxes and will check if a WWO responds
 * with valid information before it transitions to the results activity.
 * @author Luis Palacios
 */
public class MainMenuActivity extends Activity {

	// Final layout of activity.
		private static LinearLayout finalLayout;
		// Button that can be updated with queried information.
		public static TextView homeCity;
		// Edit box to enter city or zip code.
		private EditText cityInput;
		// Edit box to enter country if needed.
		private EditText countryInput;
		// Intent to transition to results activity.
		protected Intent resultsActivityIntent;
		// Text box with a prompt to guide user.
		private TextView prompt;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
			
			setContentView(R.layout.activity_main_menu);
			
			// Initialize intent to be used later on.
			resultsActivityIntent = new Intent(MainMenuActivity.this, WeatherQueryActivity.class);
			
			homeCity = (TextView) findViewById(R.id.eventsscrollview_name);
			
			Button settings = (Button) findViewById(R.id.main_menu_exitbutton);
			settings.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					exitApp();
					
				}
			});			
			
			prompt = (TextView)findViewById(R.id.eventsscrollview_date);
			
			/** 
			 * The edit text boxes required listeners for both on click (to erase the current entry) and on edit changes to 
			 * know when the user presser Enter on the keyboard.
			 */
			cityInput = (EditText) findViewById(R.id.mainmenu_cityedit);
			//cityInput.setSingleLine();
			
			// On click listener to make deleting entries easier.
			cityInput.setOnClickListener(new View.OnClickListener() {			
				@Override
				public void onClick(View v) {
					cityInput.setText("");				
				}
			});
			
			// Listener that eats up events where the user hits enter on the edit box.
			cityInput.setOnEditorActionListener(new OnEditorActionListener() {			
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					if (event != null&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
		                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		                in.hideSoftInputFromWindow(countryInput.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);	               
		               return true;
		            }
					return false;
				}
			});
			
			countryInput = (EditText)findViewById(R.id.mainmenu_countryedit);
			//countryInput.setSingleLine();
			// On click listener to make deleting entries easier.
			countryInput.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					countryInput.setText("");				
				}
			});
			// Listener that eats up events where the user hits enter on the edit box.
			countryInput.setOnEditorActionListener(new OnEditorActionListener() {			
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					if (event != null&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
		                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		                in.hideSoftInputFromWindow(countryInput.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);	               
		               return true;
		            }
					return false;
				}
			});
			/** end of edit boxes **/
			
			
			// The search button is what initializes the GET request to WWO and verifies a valid reponse.
			// Only then does it call the next activity.
			Button searchWeatherButton = (Button)findViewById(R.id.mainmenu_searchweatherbtn);
			
			searchWeatherButton.setOnClickListener(new View.OnClickListener() {
				
				// The registered listener starts a validation process to insure the user enters proper information and 
				// that the server responds.
				@Override
				public void onClick(View v) {
					String city = cityInput.getText().toString();
					String country = countryInput.getText().toString();

					if(city.equals("") || city.equals("city")){
						prompt.setText("Make sure to enter the city/zipcode and if necessary country");					
					}
					else{
						resultsActivityIntent.putExtra("city", city);
						resultsActivityIntent.putExtra("country", country);
						WeatherQueryActivity.weatherQuery = new WeatherAPI(city, country);
						try{						
							WeatherQueryActivity.weatherQuery.callAPI();
							if(WeatherQueryActivity.weatherQuery.getConditionsModel() == null){
								prompt.setText("Entry did not return valid data");
							}
							else{
								startActivity(resultsActivityIntent);
							}
						}
						catch(Exception e){
							prompt.setText("Entry did not return valid data");
						}
					}

				}				

			});
			
			Button searchEventsbtn = (Button) findViewById(R.id.mainmenu_searcheventsbutton);
			searchEventsbtn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String city = cityInput.getText().toString();
					String country = countryInput.getText().toString();

					if(city.equals("") || city.equals("city")){
						prompt.setText("Make sure to enter the city/zipcode and if necessary country");					
					}
					else{
						EventAPI eventQuery = new EventAPI(city, country);
						try{						
							eventQuery.callEventsAPI();
							if(eventQuery.eventsViewResults == null){
								prompt.setText("Entry did not return valid data");
							}
							else{
								EventsQueryActivity.eventQuery = eventQuery;
								startActivity(new Intent(MainMenuActivity.this, EventsQueryActivity.class));
							}
						}
						catch(Exception e){
							prompt.setText("Entry did not return valid data");
						}
					}

				}				
					
				
			});
			
			Button jukeboxButton = (Button) findViewById(R.id.mainmenu_jukeboxbutton);
			jukeboxButton.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					startActivity(new Intent(MainMenuActivity.this, JukeBoxActivity.class));					
				}
			});
		}

		protected void exitApp() {
			super.finish();
		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			//getMenuInflater().inflate(R.menu.weather_main, menu);
			return true;
		}
		@Override
		public void onConfigurationChanged(Configuration newConfig){
			super.onConfigurationChanged(newConfig);
			setContentView(finalLayout);
	    }
		

}
