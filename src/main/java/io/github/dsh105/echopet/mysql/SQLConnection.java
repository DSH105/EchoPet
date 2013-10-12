package io.github.dsh105.echopet.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.dsh105.echopet.logger.Logger;
import org.bukkit.ChatColor;

import io.github.dsh105.echopet.EchoPet;


public class SQLConnection {

	private Connection con;
	
	String host;
	int port;
	String db;
	String user;
	String pass;
	
	public SQLConnection(String host, int port, String database, String username, String password) {
		this.host = host;
		this.port = port;
		this.db = database;
		this.user = username;
		this.pass = password;
		this.connect();
	}
	
	public Connection getConnection() {
		/*this.close();
		return this.connect();*/
		return this.con;
	}
	
	public boolean close() {
		try {
			if (this.con != null && !this.con.isClosed()) {
				this.con.close();
				return true;
			}
		} catch (SQLException e) {
			Logger.log(Logger.LogLevel.SEVERE, "Could not close Connection to MySQL Database.", e, true);
		}
		return false;
	}
	
	public Connection connect() {
		try {
			this.con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + db, user, pass);
			return this.con;
		} catch (SQLException e) {
			Logger.log(Logger.LogLevel.SEVERE, "Could not connect Connection to MySQL Database.", e, true);
		}
		return null;
	}
}