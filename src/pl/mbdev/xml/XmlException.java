package pl.mbdev.xml;

/**
 * This exception is thrown when methods in {@code XmlTree} or other XML classes receive
 * wrong or corrupted XML data.
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
public class XmlException extends RuntimeException {
	
	/**
	 * ID.
	 */
	private static final long serialVersionUID = 7382384595921768731L;
	
	/**
	 * Constructs new XML exception.
	 * 
	 * @param description
	 *           reference to {@code StringBuffer} with description of the cause.
	 */
	public XmlException(StringBuffer description) {
		super(description.toString());
	}
	
}
