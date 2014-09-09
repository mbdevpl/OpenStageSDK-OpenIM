package pl.mbdev.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * <pre>
 * Copyright 2011 Mateusz Bysiek,
 *     mb@mbdev.pl, http://mbdev.pl/
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </pre>
 * 
 * @author Mateusz Bysiek
 */
public class SqlQuery {
	
	/**
	 * Defines kinds of SQL queries.
	 * 
	 * @author Mateusz Bysiek
	 */
	protected enum QueryType {
		/**
		 * SELECT * FROM 'Table' WHERE column = 'value'
		 */
		SELECT,
		/**
		 * INSERT INTO 'Table' (column1, column2) VALUES (row1val1, row1val2),(row2val1,
		 * row2val2)
		 */
		INSERT_INTO("INSERT INTO"),
		/**
		 * CREATE TABLE [IF NOT EXISTS] 'Table' ('column1' INT, 'column2' VARCHAR(50))
		 */
		CREATE_TABLE("CREATE TABLE"),
		/**
		 * DELETE FROM 'Table' WHERE column = 'value'
		 */
		DELETE_FROM("DELETE FROM"),
		/**
		 * UPDATE 'Table' SET column1 = value, column2 = value WHERE column = 'value'
		 */
		UPDATE,
		/**
		 * DROP [TEMPORARY] TABLE [IF EXISTS] 'table1', 'table2'
		 */
		DROP_TABLE("DROP TABLE");
		
		private final String altName;
		
		QueryType() {
			this.altName = null;
		}
		
		QueryType(String altName) {
			this.altName = altName;
		}
		
		public String toString() {
			return altName == null ? name() : altName;
		}
		
		public String toLowerCase() {
			return toString().toLowerCase();
		}
	}
	
	private QueryType type;
	
	private String statement;
	
	/**
	 * Creates new
	 * 
	 * @param statement
	 */
	public SqlQuery(String statement) {
		this(null, statement);
	}
	
	/**
	 * Creates new, fully prepared SQL query.
	 * 
	 * @param type
	 * @param statement
	 */
	public SqlQuery(QueryType type, String statement) {
		try {
			setQueryType(type);
			setStatement(statement);
		} catch (IllegalArgumentException e) {
			// ignored in the constructor
		}
	}
	
	/**
	 * @param type
	 */
	public void setQueryType(QueryType type) {
		this.type = type;
	}
	
	protected void setStatement(String statement) {
		this.statement = statement;
		if (type == null && statement != null) {
			String s = statement.toLowerCase();
			if (s.startsWith(QueryType.SELECT.toLowerCase()))
				type = QueryType.SELECT;
			else if (s.startsWith(QueryType.INSERT_INTO.toLowerCase()))
				type = QueryType.INSERT_INTO;
			else if (s.startsWith(QueryType.CREATE_TABLE.toLowerCase()))
				type = QueryType.CREATE_TABLE;
			else if (s.startsWith(QueryType.DELETE_FROM.toLowerCase()))
				type = QueryType.DELETE_FROM;
			else if (s.startsWith(QueryType.UPDATE.toLowerCase()))
				type = QueryType.UPDATE;
			else if (s.startsWith(QueryType.DROP_TABLE.toLowerCase()))
				type = QueryType.DROP_TABLE;
			else
				throw new IllegalArgumentException("The query type needs to be defined "
						+ "when the statement is ambigous.");
		}
	}
	
	/**
	 * @param connection
	 * @return result of the query
	 */
	public Object executeOn(SqlConnection connection) {
		if (type == null)
			this.setStatement(statement); // done to override type if statement defines it
			
		// constructing the full statement
		StringBuffer sb = new StringBuffer();
		if (!statement.toLowerCase().startsWith(type.toString().toLowerCase()))
			sb.append(type).append(" ");
		sb.append(statement).append(';');
		String fullStatement = sb.toString();
		
		try {
			Statement sqlStatement = connection.getStatement();
			if (type.equals(QueryType.SELECT)) {
				ResultSet set = sqlStatement.executeQuery(fullStatement);
				return set;
			} else if (type.equals(QueryType.INSERT_INTO)
					|| type.equals(QueryType.CREATE_TABLE)
					|| type.equals(QueryType.DELETE_FROM) || type.equals(QueryType.UPDATE)
					|| type.equals(QueryType.DROP_TABLE)) {
				sqlStatement.executeUpdate(fullStatement);
				return true;
			} else
				throw new IllegalArgumentException("Illegal type of sql statement.");
		} catch (SQLException e) {
			System.err.println("The following statement caused an exception:");
			System.err.println(fullStatement);
			System.err.println("The SQL exception details:");
			e.printStackTrace();
		}
		throw new NullPointerException(
				"unhandled situation caused executing statement to return null");
	}
	
	/**
	 * @param connection
	 * @param statement
	 * @return result set
	 */
	public static ResultSet select(SqlConnection connection, String statement) {
		return (ResultSet) new SqlQuery(QueryType.SELECT, statement).executeOn(connection);
	}
	
	/**
	 * @param connection
	 * @param statement
	 * @return true if success
	 */
	public static boolean insertInto(SqlConnection connection, String statement) {
		return (Boolean) new SqlQuery(QueryType.INSERT_INTO, statement)
				.executeOn(connection);
	}
	
	/**
	 * @param connection
	 * @param statement
	 * @return true if success
	 */
	public static boolean createTable(SqlConnection connection, String statement) {
		return (Boolean) new SqlQuery(QueryType.CREATE_TABLE, statement)
				.executeOn(connection);
	}
	
	/**
	 * @param connection
	 * @param statement
	 * @return true if success
	 */
	public static boolean deleteFrom(SqlConnection connection, String statement) {
		return (Boolean) new SqlQuery(QueryType.DELETE_FROM, statement)
				.executeOn(connection);
	}
	
	/**
	 * @param connection
	 * @param statement
	 * @return true if success
	 */
	public static boolean update(SqlConnection connection, String statement) {
		return (Boolean) new SqlQuery(QueryType.UPDATE, statement).executeOn(connection);
	}
	
	/**
	 * @param connection
	 * @param statement
	 * @return true if success
	 */
	public static boolean dropTable(SqlConnection connection, String statement) {
		return (Boolean) new SqlQuery(QueryType.DROP_TABLE, statement)
				.executeOn(connection);
	}
	
}
