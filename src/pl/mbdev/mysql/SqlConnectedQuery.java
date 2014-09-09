package pl.mbdev.mysql;

import java.sql.ResultSet;

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
public class SqlConnectedQuery extends SqlQuery {
	
	private final SqlConnection connection;
	
	/**
	 * @param connection
	 */
	public SqlConnectedQuery(SqlConnection connection) {
		this(connection, null, "");
	}
	
	/**
	 * @param connection
	 * @param statement
	 */
	public SqlConnectedQuery(SqlConnection connection, String statement) {
		this(connection, null, statement);
	}
	
	/**
	 * @param connection
	 * @param type
	 * @param statement
	 */
	public SqlConnectedQuery(SqlConnection connection, QueryType type, String statement) {
		super(type, statement);
		this.connection = connection;
	}
	
	/**
	 * @return result of the query
	 */
	public Object execute() {
		return super.executeOn(connection);
	}
	
	/**
	 * SELECT * FROM 'Table' WHERE column = 'value'
	 * 
	 * @param statement
	 * @return result set
	 */
	public ResultSet select(String statement) {
		this.setQueryType(QueryType.SELECT);
		this.setStatement(statement);
		return (ResultSet) this.execute();
	}
	
	/**
	 * INSERT INTO 'Table' (column1, column2) VALUES (row1val1, row1val2),(row2val1,
	 * row2val2)
	 * 
	 * @param statement
	 * @return true if success
	 */
	public boolean insertInto(String statement) {
		this.setQueryType(QueryType.INSERT_INTO);
		this.setStatement(statement);
		return (Boolean) this.execute();
	}
	
	/**
	 * CREATE TABLE [IF NOT EXISTS] 'Table' ('column1' INT, 'column2' VARCHAR(50))
	 * 
	 * @param statement
	 * @return true if success
	 */
	public boolean createTable(String statement) {
		this.setQueryType(QueryType.CREATE_TABLE);
		this.setStatement(statement);
		return (Boolean) this.execute();
	}
	
	/**
	 * DELETE FROM 'Table' WHERE column = 'value'
	 * 
	 * @param statement
	 * @return true if success
	 */
	public boolean deleteFrom(String statement) {
		this.setQueryType(QueryType.DELETE_FROM);
		this.setStatement(statement);
		return (Boolean) this.execute();
	}
	
	/**
	 * UPDATE 'Table' SET column1 = value, column2 = value WHERE column = 'value'
	 * 
	 * @param statement
	 * @return true if success
	 */
	public boolean update(String statement) {
		this.setQueryType(QueryType.UPDATE);
		this.setStatement(statement);
		return (Boolean) this.execute();
	}
	
	/**
	 * DROP [TEMPORARY] TABLE [IF EXISTS] 'table1', 'table2'
	 * 
	 * @param statement
	 * @return true if success
	 */
	public boolean dropTable(String statement) {
		this.setQueryType(QueryType.DROP_TABLE);
		this.setStatement(statement);
		return (Boolean) this.execute();
	}
	
}
