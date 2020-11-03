package com.bridgelabz.addressbookjdbc;

import java.util.ArrayList;
import java.util.List;

public class ContactService {

	public ArrayList<Contact> readData() {
		return new ContactDBService().readData();
	}

}
