package pl.mbdev.xml;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 * XML data represented by a tree.
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
public class XmlTree {
	
	/**
	 * When true, every important operation will be logged in the console.
	 */
	private static final boolean logOutput = false;
	
	/**
	 * When true, a lot of helpful text will be written to console when using methods to
	 * parse or create XML strings. Used for debugging.
	 */
	private static final boolean debugOutput = false;
	
	/**
	 * Array of strings that are not allowed as any part of: name of node, value of the
	 * node, attribute name or attribute value.
	 */
	private static final String[] unallowedStrings = { "<", ">", "=", "?" };
	
	/**
	 * Array of strings that are not allowed as a: node name or attribute name.
	 */
	private static final String[] unallowedNames = { "" };
	
	/**
	 * Creates XML header with XML document version information, encoding, and link to DTD
	 * file.
	 * 
	 * @param dtdUrl
	 *           URL to DTD file
	 * @param name
	 *           name of the root node
	 * @return reference to String incorporating the full XML header
	 */
	public static String createHeader(String dtdUrl, String name) {
		return new StringBuffer()
				.append("<?xml version=\"1.0\" encoding=\"iso-8859-2\"?><!DOCTYPE ")
				.append(name).append(" SYSTEM \"").append(dtdUrl).append("\">").toString();
	}
	
	/**
	 * Creates DTD URL out of name of the node and web address with the file.
	 * 
	 * @param domainAndUri
	 *           URI should not end with slash, and should begin with {@code http://}
	 * @param name
	 *           name of the file should not have any extension
	 * @return reference to string with full path to DTD file
	 */
	public static String createDtdUrl(String domainAndUri, String name) {
		return new StringBuffer().append(domainAndUri).append("/").append(name)
				.append(".dtd").toString();
	}
	
	/**
	 * Creates XML node name for a given object.
	 * 
	 * @param o
	 *           object for which the name will be created
	 * @return name of the node, in lower-case
	 */
	public static String createName(Xml o) {
		return o.getClass().getSimpleName().toLowerCase();
	}
	
	/**
	 * Creates XML node name for a given object.
	 * 
	 * @param c
	 *           class for which the name will be created
	 * @return name of the node, in lower-case
	 */
	public static String createName(Class<? extends XmlObject> c) {
		return c.getSimpleName().toLowerCase();
	}
	
	/**
	 * Checks if a given DTD URL is semantically correct.
	 * 
	 * @param dtdUrl
	 *           URL to be checked
	 * @return true if given string is a correct URL to dtd file
	 */
	public static boolean checkIfDtdCorrect(String dtdUrl) {
		if (!containsUnallowedChars(dtdUrl) && dtdUrl.startsWith("http://")
				&& dtdUrl.endsWith(".dtd"))
			return true;
		return false;
	}
	
	/**
	 * Checks if a given node name is semantically correct.
	 * 
	 * @param name
	 *           node name to be checked
	 * @return true if given string is a correct node name
	 */
	public static boolean checkIfNameCorrect(String name) {
		if (!containsUnallowedChars(name) && !isUnallowedName(name))
			return true;
		return false;
	}
	
	private static boolean containsUnallowedChars(String string) {
		for (String s : unallowedStrings)
			if (string.contains(s))
				return true;
		return false;
	}
	
	private static boolean isUnallowedName(String string) {
		for (String s : unallowedNames)
			if (string.equals(s))
				return true;
		return false;
	}
	
	/**
	 * URL to DTD file.
	 */
	private String dtdUrl;
	
	/**
	 * Name of the node.
	 */
	private String name;
	
	/**
	 * List of attributes.
	 */
	private Hashtable<String, String> attr;
	
	/**
	 * Value of the node.
	 */
	private String value;
	
	/**
	 * Sub-nodes for this node.
	 */
	private Hashtable<String, XmlTree> subNodes;
	
	/**
	 * Creates an empty XML tree.
	 */
	public XmlTree() {
		dtdUrl = "";
		name = "";
		attr = new Hashtable<String, String>();
		value = "";
		subNodes = new Hashtable<String, XmlTree>();
	}
	
	/**
	 * Creates a copy of an another XML tree.
	 * 
	 * @param tree
	 *           source to be copied
	 */
	public XmlTree(XmlTree tree) {
		if (logOutput)
			System.out.println("constructing XmlTree from XmlTree...");
		this.dtdUrl = new String(tree.getDtdUrl());
		this.name = new String(tree.getName());
		this.attr = new Hashtable<String, String>(tree.attr);
		this.value = new String(tree.getValue());
		this.subNodes = new Hashtable<String, XmlTree>(tree.subNodes);
	}
	
	/**
	 * Creates a tree representing a String with XML contents.
	 * 
	 * @param xmlStr
	 *           string containing the whole XML file
	 */
	public XmlTree(String xmlStr) {
		this();
		
		if (logOutput)
			System.out.println("constructing XmlTree from String...");
		
		try {
			// removing newline characters
			xmlStr = xmlStr.replace(">\n", ">");
			xmlStr = xmlStr.replace(">\r", ">");
			
			// simple check of header
			if (xmlStr.indexOf("<?xml ") == 0) {
				// URL of document type (DTD)
				int doctypeBegPos = xmlStr.indexOf("!DOCTYPE"), doctypeEndPos = xmlStr
						.indexOf('>', doctypeBegPos);
				dtdUrl = xmlStr.substring(xmlStr.indexOf('"', doctypeBegPos) + 1,
						xmlStr.lastIndexOf('"', doctypeEndPos));
				xmlStr = xmlStr.substring(doctypeEndPos + 1);
				
			} else
				dtdUrl = "";
			
			if (debugOutput)
				System.out.println("  got DTD = " + dtdUrl);
			
			// name of the node
			int nameEndPos = -1;
			if (xmlStr.indexOf(' ') > xmlStr.indexOf('>') || xmlStr.indexOf(' ') == -1)
				nameEndPos = xmlStr.indexOf('>');
			else
				nameEndPos = xmlStr.indexOf(' ');
			name = xmlStr.substring(1, nameEndPos);
			
			if (debugOutput)
				System.out.println("  got NAME = " + name);
			
			// attributes
			if (xmlStr.charAt(nameEndPos) != '>') {
				String attrStr = xmlStr.substring(nameEndPos + 1,
						xmlStr.indexOf('>', nameEndPos + 1));
				// attrStr = attrStr.substring(attrStr.indexOf(' ') + 1);
				while (attrStr.indexOf('"') != -1) {
					int splitIndex = attrStr.indexOf("=\"");
					int endIndex = 0;
					if (attrStr.indexOf("\" ") == -1)
						endIndex = attrStr.lastIndexOf('"');
					else {
						if (attrStr.contains("\" \""))
							endIndex = attrStr.indexOf("\" ", splitIndex + 2);
						else
							endIndex = attrStr.indexOf("\" ");
					}
					String key = attrStr.substring(0, splitIndex);
					String value = attrStr.substring(splitIndex + 2, endIndex);
					attr.put(key, value);
					
					if (debugOutput)
						System.out.println("  got ATTR [" + key + "=" + value + "]");
					
					if (attrStr.length() > endIndex + 2)
						attrStr = attrStr.substring(endIndex + 2);
					else
						attrStr = "";
				}
				
				if (debugOutput)
					System.out.println("  got all ATTR");
			} else if (debugOutput)
				System.out.println("  got ZERO ATTR");
			
			// sub-nodes OR value of the node
			if (xmlStr.indexOf('>') < xmlStr.lastIndexOf('<')) {
				int subNodesBegin = xmlStr.indexOf('>') + 1;
				int subNodesEnd = xmlStr.indexOf("</" + name);
				String subStr = xmlStr.substring(subNodesBegin, subNodesEnd);
				if (subStr.indexOf('<') != -1) {
					if (debugOutput) {
						System.out.println("xml=" + xmlStr + ";");
						System.out.println("sub=" + subStr + ";");
					}
					do {
						int nameEnd = 0;
						if (subStr.indexOf(' ') == -1
								|| subStr.indexOf('>') < subStr.indexOf(' '))
							nameEnd = subStr.indexOf('>');
						else
							nameEnd = subStr.indexOf(' ');
						
						String name = subStr.substring(1, nameEnd);
						
						int currSubNodeEnd = 0;
						if (subStr.indexOf('>') - 1 == subStr.indexOf("/>"))
							currSubNodeEnd = subStr.indexOf("/>") + 2;
						else
							currSubNodeEnd = subStr.indexOf("</" + name) + 2 + name.length() + 1;
						
						int subscript = 0;
						String suffix = "";
						do {
							suffix = String.valueOf(subscript);
							for (int i = suffix.length(); i < 5; i++)
								suffix = "0" + suffix;
							subscript++;
						} while (subNodes.get(name + suffix) != null);
						
						if (debugOutput)
							System.out.println(" currSub=" + subStr.substring(0, currSubNodeEnd)
									+ ";");
						
						XmlTree subTree = new XmlTree(subStr.substring(0, currSubNodeEnd));
						subNodes.put(name + suffix, subTree);
						subStr = subStr.substring(currSubNodeEnd);
						
					} while (subStr.indexOf('<') != -1);
					
					if (debugOutput)
						System.out.println("  got all SUBN = " + subNodes.keySet());
				} else {
					value = subStr;
					
					if (debugOutput)
						System.out.println("  got VALUE = " + value);
				}
			} else if (debugOutput)
				System.out.println("  got ZERO SUBN");
			
		} catch (Exception e) {
			StringBuffer s = new StringBuffer();
			s.append("Error while parsing xml!\n [ ").append(e.getClass().getSimpleName())
					.append(": ").append(e.getMessage()).append("\n '");
			s.append(xmlStr).append("'\n name=").append(name).append(",dtd=").append(dtdUrl)
					.append(",attr=").append(attr).append(",value=").append(value)
					.append(" ]");
			throw new IllegalArgumentException(s.toString());
		}
		
		if (logOutput)
			System.out.println(" done, it is " + this.getName());
		
		// if(name.equals("row"))
		// System.out.println("GAMEROW CONSTR. " + this.toString());
	}
	
	/**
	 * Creates a tree that represents an object that is convertable to XML.
	 * 
	 * @param o
	 *           object for conversion
	 */
	public XmlTree(XmlObject o) {
		this(o.toXml());
		
		if (logOutput)
			System.out.println("constructed XmlTree from XmlObject");
	}
	
	/**
	 * Creates a tree representing an object that implements Xml class.
	 * 
	 * @param o
	 *           the object
	 */
	public XmlTree(Xml o) {
		this(o.toXml());
		
		if (logOutput)
			System.out.println("constructed XmlTree from Xml");
	}
	
	/**
	 * Creates new XmlTree from an external file.
	 * 
	 * @param filePath
	 *           path to the file
	 * @return tree that was encoded in the file
	 * @throws FileNotFoundException
	 *            if the file was not found
	 * @throws IOException
	 *            if there was an exception while reading the file
	 */
	public static XmlTree createFromFile(String filePath) throws FileNotFoundException,
			IOException {
		StreamReceiver in = new StreamReceiver(new FileInputStream(filePath));
		if (logOutput)
			System.out.println("constructing XmlTree from file " + filePath + "...");
		
		StringBuffer treeStr = new StringBuffer();
		while (in.ready()) {
			treeStr = treeStr.append(in.readLine());
		}
		
		return new XmlTree(treeStr.toString());
	}
	
	/**
	 * Sets DTD URL to a given value
	 * 
	 * @param dtdUrl
	 *           a DTD URL
	 */
	public void setDtdUrl(String dtdUrl) {
		if (!checkIfDtdCorrect(dtdUrl))
			throw new XmlException(new StringBuffer("given dtd is not allowed"));
		this.dtdUrl = dtdUrl;
	}
	
	/**
	 * Gets DTD URL of this XML tree.
	 * 
	 * @return URL
	 */
	public String getDtdUrl() {
		return dtdUrl;
	}
	
	/**
	 * Sets name of this node.
	 * 
	 * @param name
	 */
	public void setName(String name) {
		if (!checkIfNameCorrect(name))
			throw new XmlException(new StringBuffer("given name: '").append(name).append(
					"' is not allowed"));
		this.name = name;
	}
	
	/**
	 * Returns name of this node.
	 * 
	 * @return name of this node
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Adds new attribute to this node.
	 * 
	 * @param name
	 *           name of the attribute
	 * @param value
	 *           value of the attribute
	 */
	public void addAttribute(String name, String value) {
		this.attr.put(name, value);
	}
	
	/**
	 * Removes attribute that has given name.
	 * 
	 * @param name
	 */
	public void removeAttribute(String name) {
		attr.remove(name);
	}
	
	/**
	 * Gets the attribute value for attribute with the given name.
	 * 
	 * @param attrName
	 *           name of the attribute
	 * @return value of the attribute
	 */
	public String getAttribute(String attrName) {
		// attr.
		// for(Iterator<String> it = attr.keySet().iterator(); it != null) {
		// if(it.next().equals(attrName))
		// return
		// }
		return attr.get(attrName);
	}
	
	/**
	 * Sets the value of this node.
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * Returns value of this node.
	 * 
	 * @return value of this node, empty string if there is no value
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Adds new subnode with the specific subscript.
	 * 
	 * @param node
	 * @param subscript
	 */
	public void addSubNode(XmlTree node, int subscript) {
		String sub = String.valueOf(subscript);
		for (int i = sub.length(); i < 5; i++)
			sub = "0" + sub;
		
		this.subNodes.put(node.getName() + sub, node);
	}
	
	/**
	 * Adds new sub node at the first free index.
	 * 
	 * @param node
	 *           sub-node
	 */
	public void addSubNode(XmlTree node) {
		int subscript = 0;
		while (true) {
			String name = String.valueOf(subscript);
			for (int i = name.length(); i < 5; i++)
				name = "0" + name;
			name = node.getName() + name;
			
			if (subNodes.get(name) == null) {
				this.subNodes.put(name, node);
				break;
			}
			subscript++;
		} // while(this.subNodes.get(node.getName() + s).equals(node));
	}
	
	/**
	 * Gets a sub node with a given subscript.
	 * 
	 * @param nodeName
	 * @param subscript
	 * @return reference to the specified childnode
	 */
	public XmlTree getSubNode(String nodeName, int subscript) {
		// System.out.println(this);
		// int j = 0;
		// for(XmlTree t: subNodes.values()) {
		// if(t.name.equals(nodeName)) {
		// j++;
		// if(j > i) return t;
		// }
		// }
		String s = String.valueOf(subscript);
		for (int i = s.length(); i < 5; i++)
			s = "0" + s;
		
		XmlTree node = subNodes.get(nodeName + s);
		if (node == null)
			throw new NullPointerException("could not find subnode with name: " + nodeName
					+ s);
		return node;
	}
	
	/**
	 * Checks if this XML node has a name that corresponds to a given class.
	 * 
	 * @param o
	 *           object convertible to XML
	 * @return true if the name created for a given class matches the name of the node
	 */
	public boolean hasCorrectName(Xml o) {
		return (createName(o).equals(this.getName()));
	}
	
	/**
	 * @return true if this XML tree has correct contents, according to the dtd it declares
	 */
	public boolean validate() {
		// TODO Auto-generated method stub
		throw new RuntimeException("not yet implemented!");
		// return false;
	}
	
	/**
	 * Saves this tree to some external file.
	 * 
	 * @param filePath
	 * @throws IOException
	 */
	public void saveToFile(String filePath) throws IOException {
		if (logOutput)
			System.out.println("saving XmlTree to file " + filePath + "...");
		
		StreamSender out;
		try {
			out = new StreamSender(new FileOutputStream(filePath));
		} catch (FileNotFoundException e) {
			throw new IOException("Xml file cannot be created.");
		}
		
		String treeStr = this.toXmlString();
		treeStr = treeStr.replace(">", ">\n");
		out.write(treeStr);
		out.flush();
		out.close();
	}
	
	@Override
	public String toString() {
		return new StringBuffer().append("XmlTree=[name=").append(getName())
				.append(",attr= ").append(attr.entrySet()).append(" ,value=")
				.append(getValue()).append(",subNodes= ").append(subNodes.keySet())
				.append(" ]").toString();
	}
	
	private StringBuffer toXmlString(boolean ommitHeaderAndDtd) {
		StringBuffer str = new StringBuffer();
		
		if (!ommitHeaderAndDtd)
			str.append(createHeader(dtdUrl, name));
		str.append("<").append(name);
		
		if (attr.size() > 0) {
			Enumeration<String> e = attr.keys();
			while (e.hasMoreElements()) {
				String key = e.nextElement();
				str.append(" ").append(key).append("=\"").append(attr.get(key)).append("\"");
			}
		}
		
		if (value != null && value.length() > 0)
			str.append(">").append(value).append("</").append(name).append(">");
		else if (subNodes.size() > 0) {
			str.append(">");
			
			ArrayList<String> nodes = Collections.list(subNodes.keys());
			Collections.sort(nodes);
			for (String key : nodes) {
				str.append(subNodes.get(key).toXmlString(true));
			}
			
			str.append("</").append(name).append(">");
		} else
			str.append(" />");
		
		return str;
	}
	
	/**
	 * Converts this tree to its XML string representation
	 * 
	 * @return string that encodes this tree
	 */
	public String toXmlString() {
		return this.toXmlString(false).toString();
	}
	
	/**
	 * returns the contents of this node and sub-nodes, recursively.
	 * 
	 * @return multi-line string with indentation
	 */
	public String toFullString() {
		return toFullString(new StringBuffer(), 0, "").toString();
	}
	
	/**
	 * Appends this object's string representation to the end of a given string buffer.
	 * 
	 * @param s
	 * @param indent
	 * @param prefix
	 * @return multi-line string with indentation
	 */
	private StringBuffer toFullString(StringBuffer s, int indent, String prefix) {
		for (int i = 0; i < indent; i++)
			s.append(" ");
		
		if (!prefix.equals(""))
			s.append("XmlTree");
		else
			s.append(prefix);
		s.append("=[name=").append(getName());
		if (attr.size() > 0)
			s.append(",attr= ").append(attr.entrySet()).append(" ");
		if (value.length() > 0)
			s.append(",value=").append(getValue());
		
		if (subNodes.size() > 0) {
			for (String sub : Collections.list(subNodes.keys())) {
				s.append("\n");
				subNodes.get(sub).toFullString(s, indent + 2, sub);
			}
			s.append("\n");
			for (int i = 0; i < indent; i++)
				s.append(" ");
		}
		s.append("]");
		return s;
	}
	
}
