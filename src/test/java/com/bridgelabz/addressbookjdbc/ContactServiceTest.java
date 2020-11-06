package com.bridgelabz.addressbookjdbc;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class ContactServiceTest {
//	@Test
//	public void givenDB_ReadData() {
//		ContactService contactService = new ContactService();
//		List<Contact> contactList = new ArrayList<Contact>();
//		contactList = contactService.readData();
//		Assert.assertEquals(2, contactList.size());
//	}
//	
//	@Test
//	public void givenCityName_ShouldReturnAllContact() {
//		ContactService contactService = new ContactService();
//		List<Contact> contactList = new ArrayList<Contact>();
//		contactList = contactService.findContactByCity("Jodhpur");
//		Assert.assertEquals(1, contactList.size());
//	}
//	
//	@Test
//	public void givenCityName_UsingPreParedStatement_ShouldReturnAllContact() {
//		ContactService contactService = new ContactService();
//		List<Contact> contactList = new ArrayList<Contact>();
//		contactList = contactService.findContactUsingPreparedByCity("Jodhpur");
//		Assert.assertEquals(1, contactList.size());
//	}
//	
//	@Test
//	public void updateCityName_ShouldUpdateDB() {
//		ContactService contactService = new ContactService();
//		int result=contactService.updateCityByFirstName("Ram","Ajmer");
//		Assert.assertEquals(1, result);
//	}
//	
//	@Test
//	public void updateCityName_UsingPreparedStatement_ShouldUpdateDB() {
//		ContactService contactService = new ContactService();
//		int result=contactService.updateCityByFirstNameUsingPrepared("Ram","Jaipur");
//		Assert.assertEquals(1, result);
//	}
//	
//	@Test
//	public void givenDateRange_ShouldReturnContactList() {
//		ContactService contactService = new ContactService();
//		List<Contact> contactList = new ArrayList<Contact>();
//		contactList = contactService.getContactBetweenDateRange("2020-05-05","2020-11-02");
//		Assert.assertEquals(2, contactList.size());
//	}
//	
//	@Test
//	public void givenDateRange_UsingPreParedStatement_ShouldReturnContactList() {
//		ContactService contactService = new ContactService();
//		List<Contact> contactList = new ArrayList<Contact>();
//		contactList = contactService.getContactBetweenDateRangeUsingPrepared("2020-05-05","2020-11-02");
//		Assert.assertEquals(2, contactList.size());
//	}
//	
//	@Test
//	public void givenMultipleContact_WhenAddedToDB_ShouldMatchUpContactEntries() {
//		Contact[] arrayOfContact= {new Contact("Amar","Parsad","Brother","Pavta","Ajmer","Rajasthan","142016","9636718081","amar@gmail.com",LocalDate.now()),
//				new Contact("Bhanu","Parsad","Brother","Suryanagar","Bikaner","Rajasthan","163516","9636373538","bhanu@gmail.com",LocalDate.now())
//		};
//		ContactService contactService= new ContactService();
//		contactService.readData();
//		Instant start = Instant.now();
//		contactService.addContact(Arrays.asList(arrayOfContact));
//		Instant end = Instant.now();
//		System.out.println("Duration without thread: " + Duration.between(start, end));
//		Assert.assertEquals(6, contactService.countEntries());
//	}
	
	@Test
	public void given4Contact_WhenAddedToDBWithThreads_ShouldMatchUpEmployeeEntries() {
		Contact[] arrayOfContact= {new Contact("Amar","Parsad","Brother","Pavta","Ajmer","Rajasthan","142016","9636718081","amar@gmail.com",LocalDate.now()),
				new Contact("Bhanu","Parsad","Brother","Suryanagar","Bikaner","Rajasthan","163516","9636373538","bhanu@gmail.com",LocalDate.now()),
				new Contact("Champak","Lal","Family","Radhepur","Banswara","Rajasthan","175983","9829998639","champak@gmail.com",LocalDate.now()),
				new Contact("Dhiraj","Kumar","Family","Avantika Nagar","Dholpur","Rajasthan","187690","9414416789","dhiraj@gmail.com",LocalDate.now())
		};
		ContactService contactService= new ContactService();
		contactService.readData();
		Instant start = Instant.now();
		contactService.addContactWithThreads(Arrays.asList(arrayOfContact));
		Instant end = Instant.now();
		System.out.println("Duration with thread: " + Duration.between(start, end));
		Assert.assertEquals(8, contactService.countEntries());
	}

}
