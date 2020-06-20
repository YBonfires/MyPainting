package com.example.mypainting.gson;

import java.sql.Timestamp;
import java.util.Map;

public class User {
	
	private int userid;
	
	private String username;
	
	private String email;
	
	private String password;
	
	private String avatarurl;
	
	private int score;
	
	private String create_time;
	
	public User() {
		super();
	}

	public User(String username, String email, String password, String avatarurl, int score) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.avatarurl = avatarurl;
		this.score = score;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAvatarurl() {
		return avatarurl;
	}

	public void setAvatarurl(String avatarurl) {
		this.avatarurl = avatarurl;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getCreate_time() {
		return create_time;
	}
	
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public static User map2user(Map map) {
		User user = new User();
		user.setUserid((int)map.get("userid"));
		user.setUsername((String)map.get("username"));
		user.setEmail((String )map.get("email"));
		user.setPassword((String )map.get("password"));
		user.setAvatarurl((String )map.get("avatarurl"));
		user.setScore((int)map.get("score"));
		user.setCreate_time((String) map.get("create_time"));
		return user;
	}

}
