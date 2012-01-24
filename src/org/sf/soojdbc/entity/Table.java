package org.sf.soojdbc.entity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * SOOJDBC is a simple object oriented JDBC wrapper.
 * Copyright (C) 2012 Rick van Biljouw
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
 * @author rvbiljouw
 * A class representing a table in the database.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Table {

	/**
	 * The name of the table this Table represents.
	 * @return name
	 */
	public String name();
	
}
