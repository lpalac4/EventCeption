package com.lpalac4.eventplanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * Jukebox activity allows users to play their music on their phone and vote on the next song to play.
 * @author Luis
 *
 */
public class JukeBoxActivity extends Activity {

	private ScrollView searchLayout;
	public static String voteId;
	public static String currentPlayingId;
	public static ArrayList<JukeBoxModel> playlist;
	private static Button currentVoteView;
	public JukeBoxModel currentmodel;
	public boolean validVote;
	private Chronometer timer;
	private SeekBar timebar;
	protected long songprogress;
	private MediaPlayer player;
	protected boolean playing;
	protected boolean freshstart;
	private TextView songname;
	private TextView artistname;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		setContentView(R.layout.activity_juke_box);
		searchLayout = (ScrollView) findViewById(R.id.jukebox_scrollview);

		LinearLayout l1 = new LinearLayout(this);
		l1.setOrientation(LinearLayout.VERTICAL);

		playlist = new ArrayList<JukeBoxModel>();
		playlist.add(new JukeBoxModel("1", "0", "danger zone", "kenny loggins", "213", R.raw.danger_zone));
		playlist.add(new JukeBoxModel("1", "1", "livin on a prayer", "bon jovi", "247", R.raw.livin_on_a_prayer));
		playlist.add(new JukeBoxModel("1", "2", "push it to the limit", "scarface", "182", R.raw.push_it_to_the_limit));
		playlist.add(new JukeBoxModel("1", "3", "i need a hero", "bonnie tyler", "350", R.raw.i_need_a_hero));
		playlist.add(new JukeBoxModel("1", "4", "final countdown ", "europe", "309", R.raw.final_countdown));	
		playlist.add(new JukeBoxModel("1", "5", "karma chameleon", "culture club", "241", R.raw.karma_chameleon));

		for(JukeBoxModel model : playlist){
			if(model == null)
				break;
			JukeBoxView newSong = new JukeBoxView(this, model);
			l1.addView(newSong.getView());
		}

		searchLayout.addView(l1);
		currentmodel = playlist.get(0);

		songname = (TextView) findViewById(R.id.jukebox_nametxt);
		//songname.setText(currentmodel.songName);
		artistname = (TextView) findViewById(R.id.jukebox_artisttxt);
		//artistname.setText(currentmodel.songArtist);

		timebar = (SeekBar) findViewById(R.id.jukebox_seekbar);
		timebar.setMax(Integer.valueOf(currentmodel.length));
		timebar.setProgress(0);

		timebar.setOnSeekBarChangeListener( new OnSeekBarChangeListener()
		{
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser)
			{

			}

			public void onStartTrackingTouch(SeekBar seekBar)
			{

			}

			public void onStopTrackingTouch(SeekBar seekBar)
			{

			}
		});

		timer = (Chronometer) findViewById(R.id.jukebox_chronometer);
		timer.setOnChronometerTickListener(new OnChronometerTickListener() {

			@Override
			public void onChronometerTick(Chronometer chronometer) {
				songprogress = chronometer.getBase() - SystemClock.elapsedRealtime();

				//				if(songprogress > Long.valueOf(currentmodel.length)){	
				//					endsong();
				//					fetchNewSong();
				//					return;
				//				}

				timebar.setProgress(timebar.getProgress() + 1);

			}
		});

		player = new MediaPlayer();


		Button playpause = (Button) findViewById(R.id.jukebox_playpausebtn);
		playpause.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(playing && !freshstart){
					player.pause();
					playing = false;
					timer.stop();
				}
				else if(!playing && !freshstart){
					player.start();
					playing = true;
					timer.start();
				}
				else if(freshstart){
					player = MediaPlayer.create(JukeBoxActivity.this, currentmodel.songrawid);
					player.setOnCompletionListener(new OnCompletionListener() {					
						@Override
						public void onCompletion(MediaPlayer mp) {
							endsong();
							
							Thread fetchThread = new Thread(new Runnable() {
								
								@Override
								public void run() {
									fetchNewSong();	
									return;
								}
							});
							
							fetchThread.start();
							
							try {
								fetchThread.join();
							} catch (InterruptedException e) {					
								e.printStackTrace();
								return;
							}
							
							songname.setText(currentmodel.songName);
							artistname.setText(currentmodel.songArtist);
							resetVoteID();
						}
					});

					player.setLooping(false);
					freshstart = false;
					playing = true;
					timer.setBase(SystemClock.elapsedRealtime());
					timebar.setMax(Integer.valueOf(currentmodel.length));
					timer.start();
					player.start();
					songname.setText(currentmodel.songName);
					artistname.setText(currentmodel.songArtist);
				}

			}
		});

		Button nextsongbtn =  (Button) findViewById(R.id.jukebox_nextsongbtn);
		nextsongbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!freshstart)
					endsong();
				
				Thread fetchThread = new Thread(new Runnable() {
					
					@Override
					public void run() {
						fetchNewSong();	
						return;
					}
				});
				
				fetchThread.start();
				
				try {
					fetchThread.join();
					songname.setText(currentmodel.songName);
					artistname.setText(currentmodel.songArtist);
					resetVoteID();
				} catch (InterruptedException e) {					
					e.printStackTrace();
					return;
				}

			}
		});

		playing = false;
		freshstart = true;

	}

	protected void fetchNewSong() {
		/** get the most voted for song from the server and replace this with it **/
//		int currentindex = Integer.parseInt(currentmodel.songid);
//		currentmodel = playlist.get(currentindex + 1);
//		songname.setText(currentmodel.songName);
//		artistname.setText(currentmodel.songArtist);
		
		InputStream in;
		String result = "";
		
		try {
			URL url = new URL("http://73.50.48.191/phpscripts/getmostvotedsong.php");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setInstanceFollowRedirects(false);
			conn.setReadTimeout(10000);
			conn.setConnectTimeout(10000);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.connect();
			in = conn.getInputStream();
		} catch (MalformedURLException e) {
			return;
		} catch (IOException e) {
			return;
		} catch (Exception e){
			return;
		}
		
		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(in,"iso-8859-1"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			in.close();

			result = sb.toString();
		}catch(Exception e){
			Log.e("log_tag", "Error converting result "+e.toString());
			return;
		}

		try{
			JSONObject json_data = new JSONObject(result);
			currentmodel = playlist.get(json_data.getInt("songid") - 1);

		}catch(JSONException e){
			Log.e("log_tag", "Error parsing data "+e.toString());
			return;
		}
		
	}

	protected void endsong() {
		timebar.setProgress(0);
		timer.stop();
		player.release();
		playing = false;
		freshstart = true;
	}

	public void resetVoteID(){
		voteId = null;
		currentVoteView.setTextColor(Color.BLACK);
		currentVoteView = null;
	}

	public static void setVoteId(String id, Button v){
		voteId = id;
		if(currentVoteView != null){
			currentVoteView.setTextColor(Color.BLACK);
		}
		currentVoteView = v;
		currentVoteView.setTextColor(Color.GREEN);

		Thread voteThread = new Thread(new Runnable() {

			@Override
			public void run() {
				voteForSong();
				return;
			}
		});
		
		voteThread.start();
		
		try {
			voteThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.juke_box, menu);
		return true;
	}

	public static void voteForSong(){

		String result = "";
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("songid", String.valueOf((Integer.valueOf(voteId) + 1))));
		//InputStream is = null; 

		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://73.50.48.191/phpscripts/voteforsong.php");
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			//is = entity.getContent();
		}catch(Exception e){
			Log.e("log_tag", "Error in http connection "+ e.toString());
			return;
		}

//		try{
//			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
//			StringBuilder sb = new StringBuilder();
//			String line = null;
//			while ((line = reader.readLine()) != null) {
//				sb.append(line + "\n");
//			}
//			is.close();
//
//			result = sb.toString();
//		}catch(Exception e){
//			Log.e("log_tag", "Error converting result "+e.toString());
//		}
//
//		//parse json data
//		try{
//			JSONObject json_data = new JSONObject(result);
//			validVote = json_data.getBoolean("valid");
//
//		}catch(JSONException e){
//			Log.e("log_tag", "Error parsing data "+e.toString());
//		}
		
		return;

	}


}
