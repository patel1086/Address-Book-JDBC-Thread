package com.bridgelabz.addressbookjdbc;

public class Contact {
	public String firstname;
	public String lastname;
	public String type;
	public String address;
	public String city;
	public String state;
	public String zip;
	public String number;
	public String email;

	// constructor
	public Contact(String firstname, String lastname,String type, String address, String city, String state, String zip,String number, String email) {
		this.firstname = firstname;
		this.lastname = lastname;
		this.type=type;
		this.address = address;
		this.city = city;
		this.state = state;
		this.zip = zip;
		this.number = number;
		this.email = email;
	}
}
