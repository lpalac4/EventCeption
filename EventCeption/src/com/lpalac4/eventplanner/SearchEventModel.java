package com.lpalac4.eventplanner;

public class SearchEventModel {
	public String eventid;
	public String name;
	public String date;
	public String location;
	public String host;
	public String details;
	
	public SearchEventModel(String venueId, String venueName, String startTime, String locationAddress, String detailsURL, String venueHost){
	
		if(venueId != null)
			eventid = venueId;
		else 
			eventid = "n/a";
		
		if(venueName != null)
			name = venueName;
		else
			name = "unavailable";
		
		if(startTime != null)
			date = startTime;
		else
			date = "not set";
		
		if(locationAddress != null)
			location = locationAddress;
		else
			location = "not set";
		
		if(detailsURL != null)
			details = detailsURL;
		else
			details = "not available";
		
		if(venueHost != null)
			host = venueHost;
		else 
			host = "unavailable";
	}
}
