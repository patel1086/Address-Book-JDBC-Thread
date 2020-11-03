package com.bridgelabz.addressbookjdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;

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
	
	public static void main(String[] args) {
		String sql="select * from address_book";
		try (Connection connection = getConnection();) {
			java.sql.Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			while(resultSet.next()) {
				String name=resultSet.getString("first_name");
				System.out.println("1 "+name);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

	



