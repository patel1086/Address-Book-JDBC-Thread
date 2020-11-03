package com.bridgelabz.addressbookjdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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

	public List<Contact> readData() {
		List<Contact> contactList=new ArrayList<>();
		String sql="Select * from address_book";
		try(Connection connection=getConnection();){
			Statement statement=connection.createStatement();
			ResultSet resultSet=statement.executeQuery(sql);
			contactList=getAddressBookData(resultSet);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return contactList;
	}

	public List<Contact> findContactByCityName(String city) {
		List<Contact> contactList=new ArrayList<>();
		String sql=String.format("Select * from address_book where city='%s';",city);
		try(Connection connection=getConnection();){
			Statement statement=connection.createStatement();
			ResultSet resultSet=statement.executeQuery(sql);
			contactList=getAddressBookData(resultSet);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return contactList;
	}
	public static List<Contact> getAddressBookData(ResultSet resultSet) throws SQLException{
		List<Contact> contactList=new ArrayList<>();
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
		return contactList;
	}

	public List<Contact> findContactUsingPreparedByCityName(String city) {
		List<Contact> contactList=new ArrayList<>();
		String sql=String.format("Select * from address_book where city=?");
		try(Connection connection=getConnection();){
			PreparedStatement pst=connection.prepareStatement(sql);
			pst.setString(1,city);
			ResultSet resultSet=pst.executeQuery();
			contactList=getAddressBookData(resultSet);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return contactList;
	}

	public int updateCityByFirstName(String name, String city) {
		String sql=String.format("update address_book set city='%s' where first_name='%s'",city,name);
		try(Connection connection=getConnection();){
			Statement statement=connection.createStatement();
			return statement.executeUpdate(sql);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int updateCityByFirstNameUsingPrepared(String name, String city) {
		String sql=String.format("update address_book set city=? where first_name=?");
		try(Connection connection=getConnection();){
			PreparedStatement pst=connection.prepareStatement(sql);
			pst.setString(1,city);
			pst.setString(2,name);
			return pst.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
}

	



