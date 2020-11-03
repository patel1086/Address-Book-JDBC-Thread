package com.bridgelabz.addressbookjdbc;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

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

}
