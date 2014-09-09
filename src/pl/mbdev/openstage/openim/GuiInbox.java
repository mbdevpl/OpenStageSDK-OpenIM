package pl.mbdev.openstage.openim;

import java.util.ArrayList;
import java.util.HashMap;

import pl.mbdev.openstage.IppCommand;
import pl.mbdev.openstage.IppSpacer;
import pl.mbdev.openstage.IppStringItem;
import pl.mbdev.openstage.IppCommand.DisplayOn;
import pl.mbdev.openstage.IppTextField;

/**
 * Shows list of messages and allows replying or writing a new one; core GUI entity of the
 * Open IM.
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
public class GuiInbox extends GuiImForm {
	
	public GuiInbox(String serverAddress, ArrayList<Pair> info,
			ArrayList<InstantMessage> msgs, HashMap<String, String> hidden) {
		super(serverAddress, "Inbox", info, msgs, hidden);
		
		if (msgs.size() == 0) {
			form.add(new IppSpacer());
			form.add(new IppStringItem("", "inbox is empty"));
			form.add(new IppSpacer());
		}
		Integer i = 0;
		for (InstantMessage im : msgs) {
			String msg = im.getMessage();
			i++;
			IppStringItem si = new IppStringItem(im.getDateTime(), msg);
			si.add(new IppCommand("Reply", IppCommand.Type.SELECT, "write", im.getSender(),
					DisplayOn.LISTITEM, false));
			si.add(new IppCommand("Delete", IppCommand.Type.SELECT, "delete", im
					.getTimestampRaw(), DisplayOn.LISTITEM, false));
			form.add(si);
		}
		screen.add(new IppCommand("New message", "write", "_new", DisplayOn.OPTIONS));
		screen.add(new IppCommand("Refresh", null, null, DisplayOn.OPTIONS));
		screen.add(new IppCommand("Edit profile", "edit", "yes", DisplayOn.OPTIONS));
		
		form.add(new IppStringItem("", "= Administration ="));
		IppTextField pass = new IppTextField("password:", "adminpass");
		pass.add(new IppCommand("Admin login", "admin", "yes", DisplayOn.LISTITEM));
		form.add(pass);
		
	}
	
}
