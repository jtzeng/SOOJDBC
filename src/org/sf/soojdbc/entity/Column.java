package org.sf.soojdbc.entity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

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
 * A class representing a column, in a table, in a database.
 * 
 * @author rvbiljouw
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

	/**
	 * The name of the column in the entity's table.
	 * 
	 * @return name
	 */
	public String name();

	/**
	 * Whether the column is auto-incremented or not (usually true for PKs)
	 * Note: setting this isn't mandatory.
	 * 
	 * @return true when auto incrementing, false otherwise.
	 */
	public boolean autoincrement() default false;

	/**
	 * The name of the column that contains a FK Note: setting this isn't
	 * mandatory. Not currently in use
	 * 
	 * @return keyColumn
	 */
	public String keyColumn() default "";

	/**
	 * The entity above FK points to. Note: setting this isn't mandatory. Not
	 * currently in use
	 * 
	 * @return entity
	 */
	public String entity() default "";

}
