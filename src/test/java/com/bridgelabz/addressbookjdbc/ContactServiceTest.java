package com.bridgelabz.addressbookjdbc;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ContactServiceTest {
	@Test
	public void givenDB_ReadData() {
		ContactService contactService = new ContactService();
		List<Contact> contactList = new ArrayList<Contact>();
		contactList = contactService.readData();
		Assert.assertEquals(2, contactList.size());
	}

	@Test
	public void givenCityName_ShouldReturnAllContact() {
		ContactService contactService = new ContactService();
		List<Contact> contactList = new ArrayList<Contact>();
		contactList = contactService.findContactByCity("Jodhpur");
		Assert.assertEquals(1, contactList.size());
	}

	@Test
	public void givenCityName_UsingPreParedStatement_ShouldReturnAllContact() {
		ContactService contactService = new ContactService();
		List<Contact> contactList = new ArrayList<Contact>();
		contactList = contactService.findContactUsingPreparedByCity("Jodhpur");
		Assert.assertEquals(1, contactList.size());
	}

	@Test
	public void updateCityName_ShouldUpdateDB() {
		ContactService contactService = new ContactService();
		int result = contactService.updateCityByFirstName("Ram", "Ajmer");
		Assert.assertEquals(1, result);
	}

	@Test
	public void updateCityName_UsingPreparedStatement_ShouldUpdateDB() {
		ContactService contactService = new ContactService();
		int result = contactService.updateCityByFirstNameUsingPrepared("Ram", "Jaipur");
		Assert.assertEquals(1, result);
	}

	@Test
	public void givenDateRange_ShouldReturnContactList() {
		ContactService contactService = new ContactService();
		List<Contact> contactList = new ArrayList<Contact>();
		contactList = contactService.getContactBetweenDateRange("2020-05-05", "2020-11-02");
		Assert.assertEquals(2, contactList.size());
	}

	@Test
	public void givenDateRange_UsingPreParedStatement_ShouldReturnContactList() {
		ContactService contactService = new ContactService();
		List<Contact> contactList = new ArrayList<Contact>();
		contactList = contactService.getContactBetweenDateRangeUsingPrepared("2020-05-05", "2020-11-02");
		Assert.assertEquals(2, contactList.size());
	}

	@Test
	public void givenMultipleContact_WhenAddedToDB_ShouldMatchUpContactEntries() {
		Contact[] arrayOfContact = {
				new Contact("Amar", "Parsad", "Brother", "Pavta", "Ajmer", "Rajasthan", "142016", "9636718081","amar@gmail.com", LocalDate.now()),
				new Contact("Bhanu", "Parsad", "Brother", "Suryanagar", "Bikaner", "Rajasthan", "163516", "9636373538","bhanu@gmail.com", LocalDate.now()) };
		ContactService contactService = new ContactService();
		contactService.readData();
		Instant start = Instant.now();
		contactService.addContact(Arrays.asList(arrayOfContact));
		Instant end = Instant.now();
		System.out.println("Duration without thread: " + Duration.between(start, end));
		Assert.assertEquals(6, contactService.countEntries());
	}

	@Test
	public void given4Contact_WhenAddedToDBWithThreads_ShouldMatchUpEmployeeEntries() {
		Contact[] arrayOfContact = {
				new Contact("Amar", "Parsad", "Brother", "Pavta", "Ajmer", "Rajasthan", "142016", "9636718081","amar@gmail.com", LocalDate.now()),
				new Contact("Bhanu", "Parsad", "Brother", "Suryanagar", "Bikaner", "Rajasthan", "163516", "9636373538","bhanu@gmail.com", LocalDate.now()),
				new Contact("Champak", "Lal", "Family", "Radhepur", "Banswara", "Rajasthan", "175983", "9829998639","champak@gmail.com", LocalDate.now()),
				new Contact("Dhiraj", "Kumar", "Family", "Avantika Nagar", "Dholpur", "Rajasthan", "187690","9414416789", "dhiraj@gmail.com", LocalDate.now()) };
		ContactService contactService = new ContactService();
		contactService.readData();
		Instant start = Instant.now();
		contactService.addContactWithThreads(Arrays.asList(arrayOfContact));
		Instant end = Instant.now();
		System.out.println("Duration with thread: " + Duration.between(start, end));
		Assert.assertEquals(8, contactService.countEntries());
	}

	@Test
	public void given2Contact_UpdateCityDetailsWithThreads_ShouldMatchUpWithDB() {
		Contact[] arrayOfContact = { new Contact("Amar", "Jaipur"), new Contact("Dhiraj", "Jaipur") };
		ContactService contactService = new ContactService();
		contactService.readData();
		Instant start = Instant.now();
		contactService.updateContactWithThreads(Arrays.asList(arrayOfContact));
		Instant end = Instant.now();
		System.out.println("Duration with thread: " + Duration.between(start, end));
		List<Contact> contactList = Arrays.asList(arrayOfContact);
		contactList.forEach(contact -> {
			Runnable task = () -> {
				boolean result = contactService.checkContactInSyncWithDB(contact.firstname);
				Assert.assertTrue(result);
			};
			Thread thread = new Thread(task, contact.firstname);
			thread.start();
		});
	}

	@Before
	public void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 3000;
	}

	public Contact[] getContactList() {
		Response response = RestAssured.get("/contacts");
		System.out.println("AddressBook Contact Details " + response.asString());
		Contact[] arrayOfContacts = new Gson().fromJson(response.asString(), Contact[].class);
		return arrayOfContacts;
	}

	private Response addContactToJsonServer(Contact contact) {
		String empJson = new Gson().toJson(contact);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(empJson);
		return request.post("/contacts");
	}

	@Test
	public void giveContactDataInJSONServer_WhenRetrieved_ShouldMatchTheCount() {
		Contact[] arrayOfContacts = getContactList();
		ContactService employeePayrollService;
		employeePayrollService = new ContactService(new ArrayList<Contact>(Arrays.asList(arrayOfContacts)));
		long entries = employeePayrollService.countEntries(ContactService.IOService.REST_IO);
		Assert.assertEquals(5, entries);
	}

	@Test
	public void givenNewContact_WhenAdded_ShouldMatch201ResponseAndCount() {
		Contact[] arrayOfContact = getContactList();
		ContactService contactService;
		contactService = new ContactService(new ArrayList<Contact>(Arrays.asList(arrayOfContact)));
		Contact contact = new Contact("Amar", "Parsad", "Brother", "Pavta", "Ajmer", "Rajasthan", "142016","9636718081", "amar@gmail.com", LocalDate.now());
		Response response = addContactToJsonServer(contact);
		int statusCode = response.getStatusCode();
		Assert.assertEquals(201, statusCode);
		contact = new Gson().fromJson(response.asString(), Contact.class);
		contactService.addContact(contact, ContactService.IOService.REST_IO);
		long entries = contactService.countEntries(ContactService.IOService.REST_IO);
		Assert.assertEquals(2, entries);
	}

	@Test
	public void givenMultipleContact_WhenAdded_ShouldMatch201ResponseAndCount() {
		Contact[] arrayOfContacts = getContactList();
		ContactService contactService;
		contactService = new ContactService(new ArrayList<Contact>(Arrays.asList(arrayOfContacts)));
		Contact[] arrayOfNewContacts = {
				new Contact("Champak", "Lal", "Family", "Radhepur", "Banswara", "Rajasthan", "175983", "9829998639","champak@gmail.com", LocalDate.now()),
				new Contact("Bhanu", "Parsad", "Brother", "Suryanagar", "Bikaner", "Rajasthan", "163516", "9636373538","bhanu@gmail.com", LocalDate.now()),
				new Contact("Dhiraj", "Kumar", "Family", "Avantika Nagar", "Dholpur", "Rajasthan", "187690","9414416789", "dhiraj@gmail.com", LocalDate.now()) };
		for (Contact contact : arrayOfNewContacts) {
			Response response = addContactToJsonServer(contact);
			int statusCode = response.getStatusCode();
			Assert.assertEquals(201, statusCode);
			contact = new Gson().fromJson(response.asString(), Contact.class);
			contactService.addContact(contact, ContactService.IOService.REST_IO);
		}
		long entries = contactService.countEntries(ContactService.IOService.REST_IO);
		Assert.assertEquals(4, entries);
	}

	@Test
	public void givenNewCityForContact_WhenUpdated_ShouldMatch200Response() {
		Contact[] arrayOfContact = getContactList();
		ContactService contactService;
		contactService = new ContactService(new ArrayList<Contact>(Arrays.asList(arrayOfContact)));
		contactService.updateCityByFirstName("David", "Jaipur", ContactService.IOService.REST_IO);
		Contact contact = contactService.getContactData("David");
		String contactJSon = new Gson().toJson(contact);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(contactJSon);
		Response response = request.put("/contacts/" + contact.id);
		int statusCode = response.getStatusCode();
		Assert.assertEquals(200, statusCode);
	}

	@Test
	public void givenContactToDelete_WhenDeleted_ShouldMatch200ResponseAndCount() {
		Contact[] arrayOfEmps = getContactList();
		ContactService contactService;
		contactService = new ContactService(new ArrayList<Contact>(Arrays.asList(arrayOfEmps)));
		Contact contact = contactService.getContactData("Champak");
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		Response response = request.delete("/contacts/" + contact.id);
		int statusCode = response.getStatusCode();
		Assert.assertEquals(200, statusCode);
		contactService.deleteContact(contact.firstname);
		long entries = contactService.countEntries(ContactService.IOService.REST_IO);
		Assert.assertEquals(4, entries);
	}
}
