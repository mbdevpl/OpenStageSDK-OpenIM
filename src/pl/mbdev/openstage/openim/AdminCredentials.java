package pl.mbdev.openstage.openim;

import pl.mbdev.xml.XmlObject;
import pl.mbdev.xml.XmlTree;

/**
 * Object containing password of the administrator.
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
public class AdminCredentials extends XmlObject {
	
	private final String password;
	
	public AdminCredentials(XmlTree t) {
		this(t.getAttribute("password"));
	}
	
	public AdminCredentials(String password) {
		this.password = password;
	}
	
	@Override
	protected XmlTree getXmlData(XmlTree t) {
		t.addAttribute("password", password);
		return t;
	}
	
	public boolean passwordEquals(Object object) {
		return this.password.equals(object);
	}
	
}
