package com.lpalac4.eventplanner;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * Event activity that displays the data to the user.
 * @author Luis
 *
 */
public class EventsQueryActivity extends Activity {

	public static EventAPI eventQuery;
	public Button searchBtn;
	public Button userCreatedBtn;
	public ScrollView userCreatedLayout;
	public ScrollView searchLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	    
		setContentView(R.layout.activity_events_query);
		
		searchBtn = (Button) findViewById(R.id.eventquery_resultsbtn);
		searchBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				userCreatedLayout.setVisibility(LinearLayout.GONE);
				searchLayout.setVisibility(LinearLayout.VISIBLE);				
			}
		});
		
		userCreatedBtn = (Button) findViewById(R.id.eventsquery_usercreatedbtn);
		userCreatedBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				searchLayout.setVisibility(LinearLayout.VISIBLE);
				userCreatedLayout.setVisibility(LinearLayout.VISIBLE);
				queryDbEvents();
			}
		});
		
		userCreatedLayout = (ScrollView) findViewById(R.id.eventsquery_usercreatedscrollview);
		searchLayout = (ScrollView) findViewById(R.id.eventsquery_searchscrollview);
		
		LinearLayout l1 = new LinearLayout(this);
		l1.setOrientation(LinearLayout.VERTICAL);

		for(SearchEventModel model : eventQuery.eventsViewResults){
			if(model == null)
				break;
			EventsView newEvent = new EventsView(this, model);
			l1.addView(newEvent.getView());
		}
		
		searchLayout.addView(l1);
		searchLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				userCreatedBtn.setBackgroundColor(Color.BLUE);
			}
		});
		userCreatedLayout.setVisibility(LinearLayout.GONE);
		
	}

	protected void queryDbEvents() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.events_view, menu);
		return true;
	}

}
