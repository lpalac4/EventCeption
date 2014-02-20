package com.lpalac4.eventplanner;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TableLayout.LayoutParams;

/**
 * Custom view class that will display song information from the server query results.
 * @author Luis
 *
 */
public class JukeBoxView extends View {
	
	public JukeBoxModel model;
	public TextView songname;
	public TextView songartist;
	public Button votebutton;
	
	public LinearLayout scrollitem;
		
	public JukeBoxView(Context context, JukeBoxModel model_){
		super(context);
		
		model = model_;
		scrollitem = new LinearLayout(context);
		scrollitem.setOrientation(LinearLayout.VERTICAL);
		
		songname = new TextView(context);
		songartist = new TextView(context);
		votebutton = new Button(context);
		
		songname.setText("Song: " + model.songName);
		songartist.setText("Artist: " + model.songArtist);
		LinearLayout txtlayout = new LinearLayout(context);
		txtlayout.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.3f));
		txtlayout.setOrientation(LinearLayout.VERTICAL);
		txtlayout.addView(songname);
		txtlayout.addView(songartist);
		
		votebutton.setText("Vote");
		votebutton.setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 0.7f));
		votebutton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				JukeBoxActivity.setVoteId(model.songid, (Button) v);
			}
		});
		LinearLayout h1 = new LinearLayout(context);
		h1.setWeightSum(1.0f);
		h1.addView(txtlayout);
		h1.addView(votebutton);
		
		scrollitem.addView(h1);	
		
	}
	
	public LinearLayout getView(){
		return scrollitem;
	}
}
