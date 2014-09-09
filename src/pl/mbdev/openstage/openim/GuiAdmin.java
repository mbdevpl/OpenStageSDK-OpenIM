package pl.mbdev.openstage.openim;

import java.util.ArrayList;
import java.util.HashMap;

import pl.mbdev.mysql.SqlCredentials;
import pl.mbdev.openstage.IppCommand;
import pl.mbdev.openstage.IppHidden;
import pl.mbdev.openstage.IppStringItem;
import pl.mbdev.openstage.IppTextField;
import pl.mbdev.openstage.IppCommand.DisplayOn;

/**
 * GUI (graphical user interface) seen by the the administrator.
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
public class GuiAdmin extends GuiImForm {
	
	public GuiAdmin(String serverAddress, ArrayList<Pair> info,
			HashMap<String, String> hidden, ArrayList<UserCredentials> users,
			SqlCredentials dbCredentials, String adminPass) {
		super(serverAddress, "Administration", info, null, hidden);
		
		form.add(new IppStringItem("", "= Users ="));
		
		for (UserCredentials user : users) {
			IppStringItem si = new IppStringItem(user.getNickname(), user.getPhoneNumber()
					+ ", " + user.getShortNumber() + ", " + user.getIpAddress());
			si.add(new IppCommand("Delete", "deleteuser", user.getPhoneNumber(),
					DisplayOn.LISTITEM));
			form.add(si);
		}
		
		form.add(new IppStringItem("", "= Database ="));
		form.add(new IppTextField("host:", dbCredentials.getHostname(), "dbhost"));
		form.add(new IppTextField("name:", dbCredentials.getDatabaseName(), "dbname"));
		form.add(new IppTextField("user:", dbCredentials.getUserName(), "dbuser"));
		form.add(new IppTextField("password:", dbCredentials.getPassword(), "dbpass"));
		
		form.add(new IppStringItem("", "= Administator ="));
		form.add(new IppTextField("password:", adminPass, "newadminpass"));
		
		screen.add(new IppCommand("Submit", "adminpass", adminPass, DisplayOn.OPTIONS));
		screen.add(new IppCommand("Uninstall", "uninstall", "yes", DisplayOn.OPTIONS));
		screen.add(new IppCommand("Reinstall", "reinstall", "yes", DisplayOn.OPTIONS));
		screen.add(new IppHidden(IppHidden.Type.PHONENUMBER, "phonenumber"));
		screen.add(new IppHidden(IppHidden.Type.IPADDRESS, "ipaddress"));
	}
}
