package com.lpalac4.eventplanner;

public class JukeBoxModel {

	public String eventid;
	public String songid;
	public String songName;
	public String songArtist;
	public String length;
	public int songrawid;
	
	public JukeBoxModel(String eventid_, String songid_, String songName_, String songArtist_, String length_, int rawid){
		eventid = eventid_;
		songid = songid_;
		songName = songName_;
		songArtist = songArtist_;
		length = length_;
		songrawid = rawid;
	}
}
