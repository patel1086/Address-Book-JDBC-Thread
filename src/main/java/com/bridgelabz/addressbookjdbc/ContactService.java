package com.bridgelabz.addressbookjdbc;

import java.util.ArrayList;
import java.util.List;

public class ContactService {

	public List<Contact> readData() {
		return new ContactDBService().readData();
	}

	public List<Contact> findContactByCity(String city) {
		return new ContactDBService().findContactByCityName(city);
	}

}
