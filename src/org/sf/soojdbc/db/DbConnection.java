package org.sf.soojdbc.db;

import java.sql.Connection;

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
 * this program; If not, see <http://www.gnu.org/licenses/>.
 * 
 * @author rvbiljouw Interface used for generic database access This is to allow
 *         the use of multiple dbms's.
 */
public interface DbConnection {

	/**
	 * Do any initialization This is mainly useful for jdbc3 where you had to
	 * call Class.forName before being able to use a driver.
	 */
	public void init();

	/**
	 * Attempt to connect to the database
	 * 
	 * @return true if succeeded
	 */
	public boolean connect();

	/**
	 * Attempt to destroy the connection.
	 * 
	 * @return destroy
	 */
	public boolean destroy();

	/**
	 * Gets a raw jdbc Connection object.
	 * 
	 * @return connection
	 */
	public Connection getConnection();

}
