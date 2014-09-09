package pl.mbdev.mysql;

import pl.mbdev.xml.XmlException;
import pl.mbdev.xml.XmlObject;
import pl.mbdev.xml.XmlTree;

/**
 * Credentials for a given MySQL server that can be loaded from and saved to local XML
 * file.
 * 
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
public class SqlCredentials extends XmlObject {
	
	private String hostname;
	private String databaseName;
	private String userName;
	private String password;
	
	/**
	 * @param t
	 */
	public SqlCredentials(XmlTree t) {
		if (!t.hasCorrectName(this))
			throw new XmlException(new StringBuffer(
					"wrong XML tree was given to SqlCredentials constructor"));
		
		this.hostname = t.getAttribute("hostname");
		this.databaseName = t.getAttribute("databaseName");
		this.userName = t.getAttribute("userName");
		this.password = t.getAttribute("password");
	}
	
	/**
	 * @param hostname
	 * @param databaseName
	 * @param userName
	 * @param password
	 */
	public SqlCredentials(String hostname, String databaseName, String userName,
			String password) {
		this.hostname = hostname;
		this.databaseName = databaseName;
		this.userName = userName;
		this.password = password;
	}
	
	/**
	 * @return the hostname
	 */
	public String getHostname() {
		return hostname;
	}
	
	/**
	 * @return the databaseName
	 */
	public String getDatabaseName() {
		return databaseName;
	}
	
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	
	@Override
	protected XmlTree getXmlData(XmlTree t) {
		t.addAttribute("hostname", hostname);
		t.addAttribute("databaseName", databaseName);
		t.addAttribute("userName", userName);
		t.addAttribute("password", password);
		return t;
	}
	
}
