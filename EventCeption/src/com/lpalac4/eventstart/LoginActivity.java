package com.lpalac4.eventstart;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.lpalac4.eventplanner.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

/*
 * Login activity that verifies credentials using a HTTP POST request to my test server
 * 
 */
public class LoginActivity extends Activity {

	private Thread authThread;
	private EditText usernameEdit;
	private EditText passwordEdit;
	private Button loginButton;
	private int validLogin;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		setContentView(R.layout.activity_login);
		
		usernameEdit = (EditText) findViewById(R.id.login_username);
		passwordEdit = (EditText) findViewById(R.id.login_password);
		loginButton = (Button) findViewById(R.id.eventquery_resultsbtn);
		validLogin = 0;

		
		authThread = new Thread(new Runnable() {

			@Override
			public void run() {
				authenticate(usernameEdit.getText().toString(), passwordEdit.getText().toString());

			}
		});
		
		loginButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(validateEntries())
					authThread.start();
				//wait for network thread to finish
				try {
					authThread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
					return;
				}
				
				if(validLogin != 0){
					startActivity(new Intent(LoginActivity.this, MainMenuActivity.class));
				}
				
			}
		});
		
	}

	protected boolean validateEntries() {
		if(usernameEdit.getText().toString().equals("") && usernameEdit.getText().toString().equals(""))
			return false;
		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_, menu);
		return true;
	}

	public void authenticate(String username, String password){
		String result = "";

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("username",username));
		nameValuePairs.add(new BasicNameValuePair("password",password));
		InputStream is = null; 

		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://73.50.48.191/phpscripts/authenticate.php");
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		}catch(Exception e){
			Log.e("log_tag", "Error in http connection "+ e.toString());
			return;
		}

		try{
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();

			result = sb.toString();
		}catch(Exception e){
			Log.e("log_tag", "Error converting result "+e.toString());
			return;
		}

		//parse json data
		try{
			JSONObject json_data = new JSONObject(result);
			validLogin = json_data.getInt("valid");
			
		}catch(JSONException e){
			Log.e("log_tag", "Error parsing data "+e.toString());
			return;
		}

	}
}


