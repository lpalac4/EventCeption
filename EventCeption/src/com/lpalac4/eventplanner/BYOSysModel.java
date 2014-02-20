package com.lpalac4.eventplanner;

/**
 * Model for the Bring Your Own system.
 * @author Luis
 *
 */
public class BYOSysModel {

	private int eventId;
	public String category;
	public String itemName;
	public int quantity;
	public int donatorUserId;
	public boolean multipleAllowed;
	
	public BYOSysModel(int eventId_, String category_, String itemName_, int quantity_, int donatorUserId_, boolean multipleAllowed_){
		eventId = eventId_;
		category = category_;
		itemName = itemName_;
		quantity = quantity_;
		donatorUserId = donatorUserId_;
		multipleAllowed = multipleAllowed_;
		
	}
	
	
}
