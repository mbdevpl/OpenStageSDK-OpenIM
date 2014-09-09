package pl.mbdev.xml;

/**
 * Interface that allows conversion of an object to XML. Please use {@code XmlObject},
 * when a class does not have to extend something different than {@code Object}.
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
public interface Xml {
	/**
	 * Converts this object into XML tree.
	 * 
	 * @return XML representation of the object
	 */
	public abstract XmlTree toXml();
}
