package com.lpalac4.eventplanner;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * Activity which connects to the Eventful API via GET request.
 * @author Luis
 *
 */
public class EventAPI {

	private static final String key = "JsPjvNG4mHM8GBBW";
	private static final String endpoint = "http://api.eventful.com/json/events/search?app_key="+ key + "&location=";
	private String query;
	public SearchEventModel[] eventsViewResults;
	public JSONObject resultjson;

	public EventAPI(String city, String country){
		query = endpoint + city + "," + country;
		eventsViewResults = null;

	}

	public void callEventsAPI(){

		Thread networkThread = new Thread(new Runnable() {
			String result = "";
			InputStream is = null; 

			@Override
			public void run() {
				try{
					URL url = new URL(query);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setInstanceFollowRedirects(false);
					conn.setReadTimeout(10000);
					conn.setConnectTimeout(10000);
					conn.setRequestMethod("GET");
					conn.setDoInput(true);
					conn.connect();
					is = conn.getInputStream();
				}catch(Exception e){
					Log.e("log_tag", "Error in http connection "+ e.toString());
				}

				try{
					BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
					StringBuilder sb = new StringBuilder();
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					is.close();

					result = sb.toString();
				}catch(Exception e){
					Log.e("log_tag", "Error converting result "+e.toString());
				}

				//parse json data
				try{
					resultjson = new JSONObject(result);
				}

				catch(JSONException e){
					Log.e("log_tag", "Error parsing data "+e.toString());
				}

			}
		});



		networkThread.start(); 
		// Wait for thread to finish.
		try{
			networkThread.join();
		}
		catch(InterruptedException e){
			eventsViewResults = null;	
		}

		
		try {
			JSONObject jsontmp = resultjson.getJSONObject("events");
			JSONArray jsonArray = jsontmp.getJSONArray("event");
			eventsViewResults = new SearchEventModel[25];

			for(int i = 0; i < jsonArray.length(); i++){
				if(i > 24)
					break;
				JSONObject json_data = jsonArray.getJSONObject(i);	

				String venueId = json_data.getString("id");
				String venueName = json_data.getString("title");
				String startTime = json_data.getString("start_time");
				String locationAddress = json_data.getString("venue_address") + " " + json_data.getString("city_name") + " " + json_data.getString("region_abbr");
				String detailsURL = json_data.getString("description");
				String host = json_data.getString("venue_name");

				SearchEventModel newModel = new SearchEventModel(venueId, venueName, startTime, locationAddress, detailsURL, host);
				eventsViewResults[i] = newModel;


			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}



//event id="E0-001-065283697-3@2014022600">
//<title>Foreign Fire Insurance Tax Board Meeting</title>
//<url>
//http://chicago.eventful.com/events/foreign-fire-insurance-tax-/E0-001-065283697-3@2014022600?utm_source=apis&utm_medium=apim&utm_campaign=apic
//</url>
//<description>
//<strong>Foreign Fire Insurance Tax Board Meeting</strong><br><p>In accordance with the Illinois Open Meetings Act, notice is hereby given that the Aurora Fire Foreign Fire Insurance Tax Board will meet at 8:30 am. on the Fourth Wednesday of each month during 2014.  These meetings will be held at the Aurora Central Fire Station, 75 N. Broadway, Aurora IL.<p><strong>Location: </strong>Aurora Central Fire Station, 75 N. Broadway, Aurora IL.<p><strong>Contact: </strong>John Lehman, Fire Chief, (630) 256-4000<p><strong>General</strong><p>January 22nd, 2014<p>8:30 a.m.<p><a href="http://aurora-il.org/agendasearch.php" rel="nofollow">Agendas & Minutes Archive</a><p><a href="http://aurora-il.org/cal_parser.php?dateID=6232" rel="nofollow">Add to Calendar</a></p></p></p></p></p></p></p></p>
//</description>
//<start_time>2014-02-26 00:00:00</start_time>
//<stop_time/>
//<tz_id/>
//<tz_olson_path/>
//<tz_country/>
//<tz_city/>
//<venue_id>V0-001-000180001-7</venue_id>
//<venue_url>
//http://chicago.eventful.com/venues/aurora-illinois-united-states-/V0-001-000180001-7?utm_source=apis&utm_medium=apim&utm_campaign=apic
//</venue_url>
//<venue_name>Aurora, Illinois, United States</venue_name>
//<venue_display>0</venue_display>
//<venue_address/>
//<city_name>Aurora</city_name>
//<region_name>Illinois</region_name>
//<region_abbr>IL</region_abbr>
//<postal_code/>
//<country_name>United States</country_name>
//<country_abbr2>US</country_abbr2>