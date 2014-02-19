package com.lpalac4.eventweather;

import java.net.HttpURLConnection;
import java.net.URL;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * ConditionsModelView extends View and will contain the weather information for one day.  Instances of ConditionsModelView will be added to a ScrollView
 * to allow the user to view the 5 day forecast.
 * @author Luis Palacios
 */
public class ConditionsView extends View {
	
	private ImageView icon;
	private TextView currentTemp;
	private TextView highTemp;
	private TextView lowTemp;
	private TextView description;
	private TextView precipitation;
	private TextView date;
	private ConditionsModel ConditionsModel;
	
	private LinearLayout finalLayout;
	
	public ConditionsView(Context context, ConditionsModel ConditionsModel_) {
		super(context);
		
		icon = new ImageView(context);
		icon.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 0, 0.2f));
		ConditionsModel = ConditionsModel_;
		boolean currentTempAvail = (ConditionsModel.currentTemp != null);
		// Assign icon image according to the condition information.
		// Done off of the main thread to insure UI responsiveness.
		Thread thread = new Thread(new Runnable(){
			@Override
			public void run() {

				try {
					URL iconUrl = new URL(ConditionsModel.icon); 
					HttpURLConnection conn = (HttpURLConnection) iconUrl.openConnection();
					conn.setInstanceFollowRedirects(false);
					conn.setReadTimeout(10000 /* milliseconds */);
					conn.setConnectTimeout(15000 /* milliseconds */);
					conn.setRequestMethod("GET");
					conn.setDoInput(true);
					conn.connect();
					icon.setImageBitmap(BitmapFactory.decodeStream(conn.getInputStream()));
				}
				catch(Exception e){

				}
			}
		});

		thread.start(); 
		
		// Wait for thread to join the main thread.
		try{
			thread.join();
		}
		catch(InterruptedException e){
			
		}
		
		/** The following UI elements are done programmatically although I could choose to do it with static XML but
		 *  I sometimes prefer the dynamic route to keep me thinking in oop.
		 */
		date = new TextView(context);
		date.setText(String.valueOf(ConditionsModel.date));
		date.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 0, 0.1f));
		
		if(currentTempAvail){
			currentTemp = new TextView(context);
			currentTemp.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 0, 0.1f));
			currentTemp.setText("Current Temp(F): " + String.valueOf(ConditionsModel.currentTemp));
		}
		
		highTemp = new TextView(context);
		highTemp.setText("High(F): " + String.valueOf(ConditionsModel.maxTemp));
		highTemp.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 0, 0.1f));
		
		lowTemp = new TextView(context);
		lowTemp.setText("Low(F): " + String.valueOf(ConditionsModel.minTemp));
		lowTemp.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 0, 0.1f));
		
		description = new TextView(context);
		description.setText(ConditionsModel.description);
		description.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 0, 0.1f));
		
		precipitation = new TextView(context);
		precipitation.setText("Precip: " + String.valueOf(ConditionsModel.precipitation));
		precipitation.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 0, 0.1f));
		
		finalLayout = new LinearLayout(context);
		finalLayout.setWeightSum(1.0f);
		finalLayout.setOrientation(LinearLayout.VERTICAL);
		finalLayout.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 0.35f));
		
		finalLayout.addView(icon);
		finalLayout.addView(date);
		if(ConditionsModel.currentTemp != null){
			finalLayout.addView(currentTemp);
		}
		finalLayout.addView(lowTemp);
		finalLayout.addView(highTemp);
		finalLayout.addView(description);
		finalLayout.addView(precipitation);
	}
	
	public LinearLayout getView(){
		return finalLayout;
	}

}

