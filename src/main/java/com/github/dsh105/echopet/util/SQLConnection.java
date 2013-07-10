package com.github.dsh105.echopet.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.github.dsh105.echopet.EchoPet;


public class SQLConnection {
	
	private EchoPet ec;
	private Connection con;
	
	public SQLConnection(EchoPet ec) {
		this.ec = ec;
		this.connect();
	}
	
	public Connection getConnection() {
		return this.con;
	}
	
	public boolean connect() {
		String host = ec.getMainConfig().getString("sql.host", "localhost");
		int port = ec.getMainConfig().getInt("sql.port", 3306);
		String db = ec.getMainConfig().getString("sql.database", "EchoPet");
		String user = ec.getMainConfig().getString("sql.username", "none");
		String pass = ec.getMainConfig().getString("sql.password", "none");
		if (user == "none" || pass == "none") {
			ec.log("Invalid SQL Username or Password");
			return false;
		}
		try {
			this.con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db, user, pass);
			return true;
		} catch (SQLException e) {
			ec.log("Failed to connect to SQL Database: + \n" + e.getMessage());
		}
		return false;
	}
}