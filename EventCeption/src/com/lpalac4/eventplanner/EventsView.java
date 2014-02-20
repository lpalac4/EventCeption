package com.lpalac4.eventplanner;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;

/**
 * Custome View to hold data for an event to be displayed in a scrollbar.
 * @author Luis
 *
 */
public class EventsView extends View {

	public SearchEventModel model;
	public Context context;
	private LinearLayout scrollviewItem;
	
	public EventsView(Context context_, SearchEventModel model_){
		super(context_);
		
		model = model_;
		context = context_;
		scrollviewItem = new LinearLayout(context);
		scrollviewItem.setOrientation(LinearLayout.VERTICAL);
		scrollviewItem.setWeightSum(1.0f);
		
		TextView name = new TextView(context);
		TextView date = new TextView(context);
		TextView location = new TextView(context);
		TextView host = new TextView(context);
		Button details = new Button(context);
		
		
		name.setText(model.name);
		name.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0, 0.1f));
		date.setText(model.date);
		date.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0, 0.1f));
		location.setText(model.location);
		location.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0, 0.3f));
		host.setText(model.host);
		host.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0, 0.1f));
		details.setText("Details");
		details.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0, 0.4f));
		details.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				EventsDetailsActivity.eventModel = model;
				context.startActivity(new Intent(context, EventsDetailsActivity.class));
			}
		});
		
		scrollviewItem.addView(name);
		scrollviewItem.addView(date);
		scrollviewItem.addView(host);
		scrollviewItem.addView(location);
		scrollviewItem.addView(details);
	}
	
	
	public LinearLayout getView(){
			return scrollviewItem;
	}

}
