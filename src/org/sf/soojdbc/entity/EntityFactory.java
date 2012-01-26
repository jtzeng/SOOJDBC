package org.sf.soojdbc.entity;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;

import org.sf.soojdbc.db.DbConnection;

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
 * @author rvbiljouw This class manages entities in the database.
 * @param <T>
 *            a type that is used for returning.
 */
public class EntityFactory<T> {
	private DbConnection connection;
	private Class<T> entityClass;
	private int limit;

	public EntityFactory(DbConnection connection, Class<T> entityClass) {
		this.entityClass = entityClass;
		this.connection = connection;

		if (!isValid()) {
			throw new RuntimeException("No idea what to do with "
					+ this.entityClass.getCanonicalName()
					+ " as it does not contain a table definition.");
		}
	}

	/**
	 * Return the connection wrapper that this entity factory is related to.
	 * 
	 * @return connection
	 */
	public DbConnection getConnection() {
		return this.connection;
	}

	/**
	 * Get the entity class
	 * 
	 * @return
	 */
	public Class<T> getEntityClass() {
		return this.entityClass;
	}

	/**
	 * Check if the entity has a table definition.
	 * 
	 * @return true/false
	 */
	private boolean isValid() {
		return this.entityClass.isAnnotationPresent(Table.class);
	}

	/**
	 * Get the name of the table definition
	 * 
	 * @return table name
	 */
	private String getTableName() {
		return this.entityClass.getAnnotation(Table.class).name();
	}

	/**
	 * Issue an update query for this entity
	 * 
	 * @param entity
	 *            the entity to be updated
	 * @return the entity updated.
	 */
	public boolean create(T entity, String... conditions) {
		try {
			Statement query = this.connection.getConnection().createStatement();
			String queryString = "INSERT INTO " + getTableName() + " ";
			String columnsString = "(";
			String valuesString = " VALUES (";
			int fieldCount = 0;
			for (Field field : this.entityClass.getDeclaredFields()) {
				if (field.isAnnotationPresent(Column.class)) {
					Column column = field.getAnnotation(Column.class);

					field.setAccessible(true);
					if (!column.autoincrement()) {
						columnsString += column.name()
								+ (fieldCount == this.entityClass
										.getDeclaredFields().length - 1 ? ""
										: ", ");

						valuesString += "'"
								+ field.get(entity)
								+ "'"
								+ (fieldCount == this.entityClass
										.getDeclaredFields().length - 1 ? ""
										: ", ");
					}
					fieldCount++;
				}
			}
			columnsString += ")";
			valuesString += ")";
			query.execute(queryString + columnsString + valuesString);
			return query.getUpdateCount() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Issue an update query for this entity
	 * 
	 * @param entity
	 *            the entity to be updated
	 * @return the entity updated.
	 */
	public boolean update(T entity, String... conditions) {
		try {
			Statement query = this.connection.getConnection().createStatement();
			String queryString = "UPDATE " + getTableName() + " SET ";

			int fieldCount = 0;
			for (Field field : this.entityClass.getDeclaredFields()) {
				if (field.isAnnotationPresent(Column.class)) {
					Column column = field.getAnnotation(Column.class);

					field.setAccessible(true);
					if (!column.autoincrement()) {

						queryString += column.name()
								+ "='"
								+ field.get(entity)
								+ "'"
								+ (fieldCount == this.entityClass
										.getDeclaredFields().length - 1 ? ""
										: ",") + " ";
					}
					fieldCount++;
				}
			}

			return query.executeUpdate(queryString
					+ concatenateQueryConditions(conditions)) > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Fetch a single result from the table
	 * 
	 * @param conditions
	 *            selection conditions
	 * @return entity
	 */
	public T get(String... conditions) {
		try {
			Statement query = this.connection.getConnection().createStatement();			
			if (!query.execute("SELECT * FROM " + this.getTableName() + " "
					+ concatenateQueryConditions(conditions))) {
				return null;
			} else {
				ResultSet resultSet = query.getResultSet();
				if (resultSet.next()) {

					T aNewEntity = this.entityClass.newInstance();
					for (Field field : this.entityClass.getDeclaredFields()) {
						if (field.isAnnotationPresent(Column.class)) {
							field.setAccessible(true);

							Column column = field.getAnnotation(Column.class);
							String columnName = column.name();

							if (field.getType() == int.class) {
								field.set(aNewEntity,
										resultSet.getInt(columnName)); // Set
																		// the
																		// value
																		// in
								// the new entity
							} else {
								field.set(aNewEntity,
										resultSet.getString(columnName));
							}
						}
					}

					return aNewEntity;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Fetch a single result from the table
	 * 
	 * @param conditions
	 *            selection conditions
	 * @return entity
	 */
	public ArrayList<T> getMultiple(String... conditions) {
		try {
			Statement query = this.connection.getConnection().createStatement();
			query.setMaxRows(this.limit);

			if (!query.execute("SELECT * FROM " + this.getTableName() + " "
					+ concatenateQueryConditions(conditions))) {
				return null;
			} else {
				ArrayList<T> resultsArrayList = new ArrayList<T>();
				ResultSet resultSet = query.getResultSet();
				while (resultSet.next()) {
					T aNewEntity = this.entityClass.newInstance();
					for (Field field : this.entityClass.getDeclaredFields()) {
						if (field.isAnnotationPresent(Column.class)) {
							field.setAccessible(true);

							Column column = field.getAnnotation(Column.class);
							String columnName = column.name();

							if (field.getType() == int.class) {
								field.set(aNewEntity,
										resultSet.getInt(columnName)); // Set
																		// the
																		// value
																		// in
								// the new entity
							} else {
								field.set(aNewEntity,
										resultSet.getString(columnName));
							}
						}
					}

					resultsArrayList.add(aNewEntity);
				}
				return resultsArrayList;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Set the maximum amount of results in a result set.
	 * 
	 * @param limit
	 *            the limit of rows being fetched
	 */
	public void setLimit(int limit) {
		this.limit = limit;
	}

	/**
	 * Concatenates the query conditions
	 * 
	 * @param conditions
	 *            the conditions as a dynamic array
	 * @return conditions string
	 */
	private String concatenateQueryConditions(String... conditions) {
		String whereClause = "WHERE ";
		for (int i = 0; i < conditions.length; i++) {
			whereClause += conditions[i] + "='" + conditions[i + 1] + "'"
					+ (i + 1 == conditions.length - 1 ? "" : " AND ");
			i++;
		}
		return conditions.length > 0 ? whereClause : "";
	}

}
