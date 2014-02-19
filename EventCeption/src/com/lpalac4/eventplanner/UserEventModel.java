package com.lpalac4.eventplanner;

import java.util.ArrayList;

public class UserEventModel {

	public int eventId;
	private int ownerId;
	private String date;
	private String desc;
	private String location;
	private BYOSysModel byosys;
	private ArrayList<Integer> guestList;
	private JukeBoxModel jukeBox;
	
	public UserEventModel(int eventId_, int ownerId_, String date_, String desc_, BYOSysModel byosys_, ArrayList<Integer> guestList_, JukeBoxModel jukeBox_, String location_){
		eventId = eventId_;
		ownerId = ownerId_;
		date = date_;
		desc = desc_;
		byosys = byosys_;
		guestList = guestList_;
		jukeBox = jukeBox_;
		location = location_;
	}
}
