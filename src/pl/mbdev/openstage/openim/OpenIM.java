package pl.mbdev.openstage.openim;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import pl.mbdev.mysql.SqlConnectedQuery;
import pl.mbdev.mysql.SqlConnection;
import pl.mbdev.mysql.SqlCredentials;
import pl.mbdev.openstage.push.Push;
import pl.mbdev.xml.XmlTree;

/**
 * Generates the content and decides which screen is shown; the core class of Open IM.
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
public class OpenIM {
	
	/**
	 * Name of the file that contains credentials for MySQL database.
	 */
	public static final String DATABASE_CREDENTIALS_FILE_PATH = "openstageim_database_credentials.xml";
	
	/**
	 * Name of the file that contains credentials for administrator of OpenStage IM.
	 */
	public static final String ADMIN_CREDENTIALS_FILE_PATH = "openstageim_admin_credentials.xml";
	
	public OpenIM(HttpServletRequest request, PrintWriter out) {
		String serverAddr = request.getRequestURL().toString();
		ArrayList<Pair> info = new ArrayList<Pair>();
		ArrayList<InstantMessage> msgs = new ArrayList<InstantMessage>();
		HashMap<String, String> hidden = new HashMap<String, String>();
		
		String admin = request.getParameter("admin");
		String adminPass = request.getParameter("adminpass");
		String write = request.getParameter("write");
		String send = request.getParameter("send");
		String register = request.getParameter("register");
		String delete = request.getParameter("delete");
		String message = request.getParameter("message");
		
		AdminCredentials adminCredentials = null;
		SqlCredentials dbCredentials = null;
		SqlConnection conn = null;
		SqlConnectedQuery sql = null;
		
		try {
			// administration data file
			adminCredentials = null;
			try {
				adminCredentials = new AdminCredentials(
						XmlTree.createFromFile(ADMIN_CREDENTIALS_FILE_PATH));
				
				// administration password change
				String newAdminPass = request.getParameter("newadminpass");
				if (newAdminPass != null && adminPass != null) {
					System.out.println("attempt to change admin password");
					if (adminCredentials.passwordEquals(adminPass)) {
						adminCredentials = new AdminCredentials(newAdminPass);
						adminCredentials.toXml().saveToFile(ADMIN_CREDENTIALS_FILE_PATH);
						System.out.println("admin password changed");
						adminPass = newAdminPass;
					}
				}
			} catch (FileNotFoundException e) {
				System.out.println("access to not configuerd im");
				info.add(new Pair("Info:", "Please configure the IM."));
				if (admin == null)
					info.add(new Pair("Info:", "Default password is 1234."));
				adminCredentials = new AdminCredentials("1234");
				adminCredentials.toXml().saveToFile(ADMIN_CREDENTIALS_FILE_PATH);
				
				throw new UnauthorizedException("OpenStage IM is not configured.");
			}
			
			// database coordinates file
			dbCredentials = null;
			try {
				SqlCredentials newCredentials = null;
				// set new db credentials if password was provided and was correct
				if (adminPass != null && adminCredentials.passwordEquals(adminPass)) {
					String dbHost = request.getParameter("dbhost");
					String dbName = request.getParameter("dbname");
					String dbUser = request.getParameter("dbuser");
					String dbPass = request.getParameter("dbpass");
					if (dbHost != null && dbName != null && dbUser != null && dbPass != null) {
						newCredentials = new SqlCredentials(dbHost, dbName, dbUser, dbPass);
						newCredentials.toXml().saveToFile(DATABASE_CREDENTIALS_FILE_PATH);
						dbCredentials = newCredentials;
					}
				}
				if (newCredentials == null) {
					// load db credentials
					dbCredentials = new SqlCredentials(
							XmlTree.createFromFile(DATABASE_CREDENTIALS_FILE_PATH));
				}
				
			} catch (FileNotFoundException e) {
				dbCredentials = new SqlCredentials("localhost", "openim", "root", "root");
				dbCredentials.toXml().saveToFile(DATABASE_CREDENTIALS_FILE_PATH);
				System.out.println("cannot access file with database credentials");
				info.add(new Pair("Info:", "Configure the database."));
				
				throw new UnauthorizedException("Database is not configured.");
			}
			
			// database connection
			conn = null;
			sql = null;
			try {
				conn = new SqlConnection(dbCredentials);
				conn.connect();
				sql = new SqlConnectedQuery(conn);
				
				if (sql != null) {
					this.install(sql);
					System.out.println("database tables present");
				}
				
			} catch (RuntimeException e) {
				System.out.println("incorrect database credentials detected");
				System.out.println("host=" + dbCredentials.getHostname() + " db="
						+ dbCredentials.getDatabaseName());
				info.add(new Pair("Info:", "Reconfigure the database."));
				
				throw new UnauthorizedException("Database is not properly configured.");
			}
			
			// user authorization
			UserCredentials user = null;
			boolean pushed = false;
			try {
				user = new UserCredentials(request.getParameter("ipaddress"),
						request.getParameter("phonenumber"),
						request.getParameter("shortnumber"), request.getParameter("nickname"));
				if (user.getIpAddress() == null || user.getPhoneNumber() == null)
					throw new NullPointerException();
				if (!userAuthorization(sql, user))
					user = null;
			} catch (NullPointerException e) {
				user = this.findUser(sql, request.getParameter("ipaddress"));
				pushed = true;
			}
			
			// creating new user profile
			if (user == null && register != null && register.equals("yes") && sql != null) {
				user = new UserCredentials(request.getParameter("ipaddress"),
						request.getParameter("phonenumber"),
						request.getParameter("shortnumber"), request.getParameter("nickname"));
				
				boolean noConflicts = true;
				int infoSize = info.size();
				
				if (user.getNickname().length() < 2)
					info.add(new Pair("Error:", "nickname is too short, "
							+ "it must have at least 2 characters"));
				
				if (findUser(sql, user.getIpAddress()) != null)
					info.add(new Pair("Error:", "there already is a user with your IP, "
							+ "contact administrator to resolve network conflicts"));
				if (findUser(sql, user.getPhoneNumber()) != null)
					info.add(new Pair("Error:",
							"there already is a registred user that has your phone number, "
									+ "contact administrator to resolve such conflict"));
				if (findUser(sql, user.getShortNumber()) != null)
					info.add(new Pair("Error:", "short phone number '" + user.getShortNumber()
							+ "' is taken, " + "choose a different one"));
				if (findUser(sql, user.getNickname()) != null)
					info.add(new Pair("Error:", "nickname '" + user.getNickname()
							+ "' is taken, " + "choose a different nickname"));
				
				if (infoSize < info.size())
					noConflicts = false;
				if (noConflicts)
					sql.insertInto("Users (phonenumber, shortnumber, ipaddress, nickname) "
							+ "VALUES (" + user.getPhoneNumber() + "," + user.getShortNumber()
							+ ",'" + user.getIpAddress() + "','" + user.getNickname() + "')");
			}
			
			// perform administrative actions
			if (adminPass != null && adminCredentials.passwordEquals(adminPass)) {
				String deleteUser = request.getParameter("deleteuser");
				String uninstall = request.getParameter("uninstall");
				String reinstall = request.getParameter("reinstall");
				
				if (deleteUser != null) {
					if (sql != null && sql.deleteFrom("Users WHERE phonenumber=" + deleteUser)) {
						info.add(new Pair("Info:", "User data was deleted."));
						System.out.println("removed a record from the user list");
					}
				}
				if (sql != null && uninstall != null && uninstall.equals("yes")) {
					this.uninstall(sql);
				}
				if (sql != null && reinstall != null && reinstall.equals("yes")) {
					this.reinstall(sql);
				}
				
			}
			
			// check if user wants to display administrator view instead of
			if (admin != null && admin.equals("yes"))
				throw new UnauthorizedException();
			
			// checking user data
			if (user != null) {
				hidden.put("phonenumber", user.getPhoneNumber());
				hidden.put("ipaddress", user.getIpAddress());
				hidden.put("shortnumber", user.getShortNumber());
				hidden.put("nickname", user.getNickname());
				
				if (pushed) {
					new GuiNewMessages(serverAddr, hidden).sendTo(out);
					
				} else if (write != null
						|| (send != null && !sendMessage(sql, new InstantMessage(null, send,
								user.getPhoneNumber(), message), serverAddr))) {
					if (send == null && write != null && !write.equals("_new"))
						hidden.put("send", write);
					if (message != null)
						hidden.put("message", message);
					new GuiWriteMessage(serverAddr, info, msgs, hidden).sendTo(out);
				} else {
					
					// delete messages if ordered
					if (delete != null) {
						String deleteWhere = "timestamp=" + delete + " and receiver= "
								+ user.getPhoneNumber();
						ResultSet set = sql.select("* FROM Messages WHERE " + deleteWhere);
						try {
							System.out.print("deleting message: ");
							if (set.first())
								do {
									System.out.print("[" + set.getString("timestamp") + "] ");
								} while (set.next());
							sql.deleteFrom("Messages WHERE " + deleteWhere);
							info.add(new Pair("Info:", "Message was deleted."));
							System.out.println(" ok.");
						} catch (SQLException e) {
							System.out.println("SQLException");
							e.printStackTrace();
						}
					}
					
					// add messages to inbox
					ResultSet set = sql.select("* FROM Messages WHERE receiver="
							+ user.getPhoneNumber() + " ORDER BY timestamp DESC");
					try {
						System.out.println("messages for this user:");
						if (set.first())
							do {
								InstantMessage im = new InstantMessage(
										set.getString("timestamp"), set.getString("receiver"),
										set.getString("sender"), set.getString("message"));
								msgs.add(im);
								System.out.println(im.toPrivateString());
							} while (set.next());
					} catch (SQLException e) {
						info.add(new Pair("Error:", "Couldn't access inbox."));
						e.printStackTrace();
					}
					
					new GuiInbox(serverAddr, info, msgs, hidden).sendTo(out);
				}
			} else {
				hidden.put("phonenumber", request.getParameter("phonenumber"));
				hidden.put("ipaddress", request.getParameter("ipaddress"));
				if (hidden.get("phonenumber") == null || hidden.get("ipaddress") == null)
					throw new UnauthorizedException("request without any login data");
				info.add(new Pair("Info:", "Detected a new user, please confirm your info."));
				new GuiAuthorization(serverAddr, info, hidden).sendTo(out);
			}
			
		} catch (UnauthorizedException e) {
			if (admin != null && admin.equals("yes") && adminPass != null
					&& adminCredentials.passwordEquals(adminPass)) {
				// obtain user list from db, if there is a connection
				ArrayList<UserCredentials> users = new ArrayList<UserCredentials>();
				if (sql != null) {
					ResultSet set = sql.select("* FROM Users"); // ORDER BY shortnumber ASC
					try {
						System.out.println("getting all users list:");
						if (set.first())
							do {
								UserCredentials u = new UserCredentials(
										set.getString("ipaddress"), set.getString("phonenumber"),
										set.getString("shortnumber"), set.getString("nickname"));
								users.add(u);
								System.out.println(u.toString());
							} while (set.next());
					} catch (SQLException ex) {
						info.add(new Pair("Error:", "Couldn't select users ."));
						e.printStackTrace();
					}
				}
				
				hidden.put("adminpass", adminPass);
				new GuiAdmin(serverAddr, info, hidden, users, dbCredentials, adminPass)
						.sendTo(out);
			} else {
				System.out.println("unauthorized access attempt detected");
				System.out.println(e.getLocalizedMessage());
				
				msgs.clear();
				if (e.getLocalizedMessage() != null && e.getLocalizedMessage().length() > 0)
					info.add(new Pair("Reason:", e.getLocalizedMessage()));
				// hidden.put("datafile", DATABASE_CREDENTIALS_FILE_PATH);
				new GuiUnauthorized(serverAddr, info, msgs, hidden).sendTo(out);
			}
		} catch (IOException e) {
			//
		} finally {
			if (sql != null && conn != null)
				conn.disconnect();
			
		}
	}
	
	/**
	 * Installs the OpenStage IM to a MySQL database.
	 */
	private void install(SqlConnectedQuery sql) {
		System.out.print("Installing OpenStage IM... ");
		sql.createTable("IF NOT EXISTS Messages (id INT NOT NULL AUTO_INCREMENT, "
				+ "timestamp BIGINT NOT NULL, " + "sender BIGINT NOT NULL, "
				+ "receiver BIGINT NOT NULL, " + "message VARCHAR(160) NOT NULL, "
				+ "PRIMARY KEY (id))");
		sql.createTable("IF NOT EXISTS Users (id SMALLINT NOT NULL AUTO_INCREMENT, "
				+ "phonenumber BIGINT NOT NULL, " + "shortnumber SMALLINT NOT NULL, "
				+ "ipaddress VARCHAR(15) NOT NULL, " + "nickname VARCHAR(50) NOT NULL, "
				+ "PRIMARY KEY (id))");
		System.out.println("ok.");
	}
	
	/**
	 * Uninstalls the OpenStage IM from a MySQL database.
	 */
	private void uninstall(SqlConnectedQuery sql) {
		try {
			System.out.print("Deleting all tables of OpenStage IM... ");
			sql.dropTable("Users");
			sql.dropTable("Messages");
			System.out.println("ok.");
		} catch (Exception e) {
			System.out.println("Exception");
			e.printStackTrace();
		}
	}
	
	/**
	 * Reinstalls the OpenStage IM on a MySQL database.
	 */
	private void reinstall(SqlConnectedQuery sql) {
		this.uninstall(sql);
		this.install(sql);
	}
	
	private boolean
			sendMessage(SqlConnectedQuery sql, InstantMessage im, String serverAddr) {
		try {
			if (im.getMessage() == null)
				throw new NullPointerException("expected message to be sent");
			
			// find user data of the receiver
			UserCredentials receiver = findUser(sql, im.getReceiver());
			
			// send message
			im.setReceiver(receiver.getPhoneNumber());
			sql.insertInto("Messages (timestamp, sender, receiver, message) VALUES ("
					+ im.getTimestampRaw() + ", " + im.getSender() + ", " + im.getReceiver()
					+ ", '" + im.getMessage() + "')");
			System.out.println("Message sent.");
			
			// display confirmation
			// information.put("Info:", "Message sent to " + params.get("sendTo") + ".");
			// hidden.remove("replyto");
			// hidden.remove("sendto");
			
			// send push request
			if (new Push(serverAddr, "OpenIM", Push.RequestType.FORCE, "ipaddress",
					receiver.getIpAddress()).sendTo(receiver.getIpAddress()))
				System.out.println("Push request sent.");
			else
				System.out.println("Unable to perform push request.");
			
			return true;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return false;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private UserCredentials findUser(SqlConnectedQuery sql, String clue) {
		String findWhere = "ipaddress='" + clue + "' or phonenumber='" + clue
				+ "' or shortnumber='" + clue + "' or nickname='" + clue + "'";
		try {
			ResultSet s = sql.select("* FROM Users WHERE " + findWhere);
			UserCredentials user = null;
			s.first();
			do {
				if (user != null)
					System.out.println("Found many matching users...");
				
				user = new UserCredentials(s.getString("ipaddress"),
						s.getString("phonenumber"), s.getString("shortnumber"),
						s.getString("nickname"));
				
				System.out.println("found user: [" + s.getString("phonenumber") + ","
						+ s.getString("shortnumber") + "," + s.getString("ipaddress") + ","
						+ s.getString("nickname") + "]");
			} while (s.next());
			return user;
		} catch (SQLException e) {
			return null;
		}
	}
	
	private boolean userAuthorization(SqlConnectedQuery sql, UserCredentials user) {
		try {
			ResultSet s = sql
					.select("* FROM Users WHERE phonenumber = " + user.getPhoneNumber()
							+ " and ipaddress = '" + user.getIpAddress() + "'");
			if (s.first()) {
				boolean moreThanOneUser = false;
				do {
					if (moreThanOneUser)
						System.out.println("detected many users matching single login data, "
								+ "messages will be sent to all of them");
					user.setShortNumber(s.getString("shortnumber"));
					user.setNickname(s.getString("nickname"));
					System.out.println("user authentication: " + user.toString());
					moreThanOneUser = true;
				} while (s.next());
			} else
				return false;
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			if (user.getShortNumber() != null && user.getNickname() != null) {
				sql.insertInto("Users (phonenumber, shortnumber, ipaddress, nickname) "
						+ "VALUES (" + user.getPhoneNumber() + ", " + user.getShortNumber()
						+ ", '" + user.getIpAddress() + "', '" + user.getNickname() + "')");
				return true;
			} else {
				return false;
			}
		}
	}
	
}
