package com.bridgelabz.addressbookjdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactService {

	public List<Contact> readData() {
		return new ContactDBService().readData();
	}

	public List<Contact> findContactByCity(String city) {
		return new ContactDBService().findContactByCityName(city);
	}

	public List<Contact> findContactUsingPreparedByCity(String city) {
		return new ContactDBService().findContactUsingPreparedByCityName(city);
	}

	public int updateCityByFirstName(String name, String city) {
		return new ContactDBService().updateCityByFirstName(name,city);
	}

	public int updateCityByFirstNameUsingPrepared(String name, String city) {
		return new ContactDBService().updateCityByFirstNameUsingPrepared(name,city);
	}

	public List<Contact> getContactBetweenDateRange(String date1, String date2) {
		return new ContactDBService().getContactBetweenDateRange(date1,date2);
	}

	public List<Contact> getContactBetweenDateRangeUsingPrepared(String date1, String date2) {
		return new ContactDBService().getContactBetweenDateRange(date1,date2);
	}

	public void addContact(List<Contact> contactList) {
		contactList.forEach(contact->{
			this.addContact(contact.firstname,contact.lastname,contact.type,contact.address,contact.city,contact.state,
					contact.zip,contact.number,contact.email,contact.date);
		});
		
	}

	private void addContact(String firstname, String lastname, String type, String address, String city, String state,
			String zip, String number, String email,LocalDate date) 
	{
		new ContactDBService().addContact(firstname,lastname,type,address,city,state,zip,number,email,date);
	}

	public int countEntries() {
		List<Contact> contactList = new ArrayList<>();
		contactList = new ContactDBService().readData();
		return contactList.size();

	}

	public void addContactWithThreads(List<Contact> contactList) {
		Map<Integer, Boolean> contactAdditionStatus = new HashMap<>();
		contactList.forEach(contact->{
			Runnable task=()->{
				contactAdditionStatus.put(contact.hashCode(), false);
				this.addContact(contact.firstname,contact.lastname,contact.type,contact.address,contact.city,contact.state,
					contact.zip,contact.number,contact.email,contact.date);
				contactAdditionStatus.put(contact.hashCode(), false);
			};
			Thread thread = new Thread(task, contact.firstname);
			thread.start();
		});
		while (contactAdditionStatus.containsValue(false)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {

			}
		}
	}

}
