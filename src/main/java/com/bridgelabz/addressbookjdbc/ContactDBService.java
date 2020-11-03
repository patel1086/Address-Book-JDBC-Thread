package com.bridgelabz.addressbookjdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class ContactDBService {
	private static Connection getConnection() {
		String jdbcURL="jdbc:mysql://localhost:3306/addressbook?allowPublicKeyRetrieval=true&useSSL=false";
		String username="root";
		String password="1234";
		Connection connection=null;
		try {
			System.out.println("connecting to database: "+jdbcURL);
			connection=DriverManager.getConnection(jdbcURL,username,password);
			System.out.println("Connection established successfully!!!!"+connection);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return connection;
	}

	public ArrayList<Contact> readData() {
		ArrayList<Contact> contactList=new ArrayList<>();
		String sql="Select * from address_book";
		try(Connection connection=getConnection();){
			Statement statement=connection.createStatement();
			ResultSet resultSet=statement.executeQuery(sql);
			while(resultSet.next()) {
				String firstName=resultSet.getString("first_name");
				String lastName=resultSet.getString("last_name");
				String type=resultSet.getString("type");
				String address=resultSet.getString("address");
				String city=resultSet.getString("city");
				String state=resultSet.getString("state");
				String zip=resultSet.getString("ZIP");
				String number=resultSet.getString("phone_number");
				String email=resultSet.getString("email");
				contactList.add(new Contact(firstName,lastName,type,address,city,state,zip,number,email));
			}
			
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return contactList;
	}
}

	



