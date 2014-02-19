package com.lpalac4.eventplanner;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class BYOSysItemCreationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_byosys_item_creation);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.byosys_item_creation, menu);
		return true;
	}

}
