package com.bridgelabz.addressbookjdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContactService {

	public ArrayList<Contact> contactList;

	public ContactService(ArrayList<Contact> contactList) {
		this();
		this.contactList = contactList;
	}

	public enum IOService {
		CONSOLE_IO, FILE_IO, DB_IO, REST_IO
	}

	public ContactService() {

	}

	public List<Contact> readData() {
		return new ContactDBService().readData();
	}

	public List<Contact> findContactByCity(String city) {
		return new ContactDBService().findContactByCityName(city);
	}

	public List<Contact> findContactUsingPreparedByCity(String city) {
		return new ContactDBService().findContactUsingPreparedByCityName(city);
	}

	//Method to update city name by first name for JDBC and Json server both
	public void updateCityByFirstName(String name, String city, IOService ioService) {
		if (ioService.equals(IOService.DB_IO)) {
			int result = new ContactDBService().updateCityByFirstName(name, city);
			if (result == 0)
				return;
		}
		Contact contact = this.getContactData(name);
		if (contact != null)
			contact.city = city;
	}

	//Method to get contact data
	public Contact getContactData(String name) {
		return this.contactList.stream().filter(contactData -> contactData.firstname.equals(name)).findFirst()
				.orElse(null);
	}

	//Method to update city name using first name by preparedstatement in JDBC
	public int updateCityByFirstNameUsingPrepared(String name, String city) {
		return new ContactDBService().updateCityByFirstNameUsingPrepared(name, city);
	}

	public List<Contact> getContactBetweenDateRange(String date1, String date2) {
		return new ContactDBService().getContactBetweenDateRange(date1, date2);
	}

	//Method to get total contact details b/w two date from Database (JDBC)
	public List<Contact> getContactBetweenDateRangeUsingPrepared(String date1, String date2) {
		return new ContactDBService().getContactBetweenDateRange(date1, date2);
	}

	//Method to add list of contact details
	public void addContact(List<Contact> contactList) {
		contactList.forEach(contact -> {
			this.addContact(contact.firstname, contact.lastname, contact.type, contact.address, contact.city,
					contact.state, contact.zip, contact.number, contact.email, contact.date);
		});

	}

	//Method to add contact details in Database (JDBC)
	private void addContact(String firstname, String lastname, String type, String address, String city, String state,
			String zip, String number, String email, LocalDate date) {
		new ContactDBService().addContact(firstname, lastname, type, address, city, state, zip, number, email, date);
	}

	//Method to count total entries in DataBase (JDBC)
	public int countEntries() {
		List<Contact> contactList = new ArrayList<>();
		contactList = new ContactDBService().readData();
		return contactList.size();

	}

	//Method to add contacts details using thread
	public void addContactWithThreads(List<Contact> contactList) {
		Map<Integer, Boolean> contactAdditionStatus = new HashMap<>();
		contactList.forEach(contact -> {
			Runnable task = () -> {
				contactAdditionStatus.put(contact.hashCode(), false);
				System.out.println("Contact Being Added: " + Thread.currentThread().getName());
				this.addContact(contact.firstname, contact.lastname, contact.type, contact.address, contact.city,contact.state, contact.zip, contact.number, contact.email, contact.date);
				contactAdditionStatus.put(contact.hashCode(), true);
				System.out.println("Contact Added: " + Thread.currentThread().getName());
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

	//Method to update contact details using thread
	public void updateContactWithThreads(List<Contact> contactList) {
		Map<Integer, Boolean> contactUpdateStatus = new HashMap<>();
		contactList.forEach(contact -> {
			Runnable task = () -> {
				contactUpdateStatus.put(contact.hashCode(), false);
				System.out.println("Contact city Being Updated: " + Thread.currentThread().getName());
				this.updateCityByFirstName(contact.firstname, contact.city);
				contactUpdateStatus.put(contact.hashCode(), true);
				System.out.println("Contact city updated: " + Thread.currentThread().getName());
			};
			Thread thread = new Thread(task, contact.firstname);
			thread.start();
		});
		while (contactUpdateStatus.containsValue(false)) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {

			}
		}

	}

	//Method to update city by contact's first name
	public int updateCityByFirstName(String name, String city) {
		return new ContactDBService().updateCityByFirstName(name, city);

	}

	//Method to check contact in sync with database
	public boolean checkContactInSyncWithDB(String firstname) {
		ContactDBService contactDBService = new ContactDBService();
		List<Contact> contactList = new ArrayList<>();
		contactList = contactDBService.getContactData(firstname);
		return contactList.get(0).firstname.equals(contactDBService.getContactData(firstname).get(0).firstname);

	}

	//Method to count entries for Json File RestAssured API
	public long countEntries(IOService iOService) {
		if (iOService.equals(IOService.REST_IO))
			return contactList.size();
		return 0;
	}

	//Method to add contact in DB and contactList depend on IOService
	public void addContact(Contact contact, IOService iOService) {
		if (iOService.equals(IOService.DB_IO)) {
			new ContactDBService().addContact(contact.firstname, contact.lastname, contact.type, contact.address,
					contact.city, contact.state, contact.zip, contact.number, contact.email, contact.date);
		} else
			contactList.add(contact);
	}

	//Method to delete or remote contact from contactList
	public void deleteContact(String firstname) {
		Contact contact = this.getContactData(firstname);
		contactList.remove(contact);
	}
}
