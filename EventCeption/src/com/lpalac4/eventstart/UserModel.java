package com.lpalac4.eventstart;

/*
 * Model used for user accounts;
 */
public class UserModel {

	private String username;
	private String userpassword;
	public int userid;
	
	public UserModel(String username_, String userpassword_, int userid_){
		username = username_;
		userpassword = userpassword_;
		userid = userid_;
	}
	
}
