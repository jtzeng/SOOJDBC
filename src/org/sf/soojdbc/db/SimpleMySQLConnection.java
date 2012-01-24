package org.sf.soojdbc.db;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * SOOJDBC is a simple object oriented JDBC wrapper. Copyright (C) 2012 Rick van
 * Biljouw
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; If not, see <http://www.gnu.org/licenses/>. A simple class that
 * allows connection to a MySQL server (if the JDBC connector is on the
 * classpath, that is.)
 * 
 * @author rvbiljouw
 * 
 */
public class SimpleMySQLConnection implements DbConnection {
	private Connection connection;
	private String hostname;
	private String database;
	private String username;
	private String password;

	@Override
	public void init() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			System.out.println("Failed to initialize database driver");
		}
	}

	@Override
	public boolean connect() {
		try {

			this.connection = DriverManager.getConnection("jdbc:mysql:// "
					+ this.hostname + "/" + this.database, this.username,
					this.password);

			return !this.connection.isClosed();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean destroy() {
		try {
			this.connection.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Connection getConnection() {
		return this.connection;
	}

}
