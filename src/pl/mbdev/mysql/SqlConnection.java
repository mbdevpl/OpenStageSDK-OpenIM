package pl.mbdev.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
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
public class SqlConnection {
	
	private SqlCredentials credentials;
	
	private Connection connection;
	
	/**
	 * @param credentials
	 */
	public SqlConnection(SqlCredentials credentials) {
		this.credentials = credentials;
	}
	
	/**
	 * 
	 */
	public void connect() {
		System.out.print("MySQL: connecting... ");
		String url = "jdbc:mysql://" + credentials.getHostname() + "/";
		String driver = "com.mysql.jdbc.Driver";
		
		try {
			Class.forName(driver).newInstance();
			
			connection = DriverManager.getConnection(url + credentials.getDatabaseName(),
					credentials.getUserName(), credentials.getPassword());
			
			System.out.println("ok.");
			
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException");
			throw new NullPointerException(
					"com.mysql.jdbc.Driver was not found in the system");
		} catch (InstantiationException e) {
			System.out.println("InstantiationException");
			e.printStackTrace();
			throw new RuntimeException("");
		} catch (IllegalAccessException e) {
			System.out.println("IllegalAccessException");
			e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("SQLException");
			e.printStackTrace();
			throw new IllegalArgumentException("sql connection not established, "
					+ "may be credentials problem");
		}
		
	}
	
	/**
	 * 
	 * @return true if disconnected successfully
	 */
	public boolean disconnect() {
		System.out.print("MySQL: disconnecting... ");
		try {
			if (connection != null)
				connection.close();
		} catch (SQLException e) {
			System.out.println("SQLExceptione");
			e.printStackTrace();
		}
		System.out.println("ok.");
		return true;
	}
	
	/**
	 * @return connection
	 * @throws SQLException
	 */
	public Statement getStatement() throws SQLException {
		return connection.createStatement();
	}
	
}
