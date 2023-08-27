package com.spring.batch.models;

public class User {
	private int userId;
	private String firstName;
	private String lastName;
	private String country;
	
	public User() {
		
	}

	public User(int userId, String firstName, String lastName, String country) {
		super();
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.country = country;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	

}
