package pl.mbdev.openstage.openim;

import java.util.ArrayList;
import java.util.HashMap;

import pl.mbdev.openstage.IppCommand;
import pl.mbdev.openstage.IppCommand.DisplayOn;
import pl.mbdev.openstage.IppHidden;
import pl.mbdev.openstage.IppStringItem;
import pl.mbdev.openstage.IppTextField;

/**
 * Seen when the {@link UnauthorizedException} is thrown.
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
public class GuiUnauthorized extends GuiImForm {
	
	public GuiUnauthorized(String serverAddress, ArrayList<Pair> info,
			ArrayList<InstantMessage> msgs, HashMap<String, String> hidden) {
		super(serverAddress, "Something's wrong...", info, msgs, hidden);
		
		form.add(new IppStringItem("", "Administration"));
		IppTextField tf = new IppTextField("password:", "adminpass");
		tf.add(new IppCommand("Admin login", "admin", "yes", DisplayOn.LISTITEM));
		form.add(tf);
		
		screen.add(new IppCommand("Restart client", null, null, DisplayOn.OPTIONS));
		
		if (!hidden.containsKey("phonenumber") || hidden.get("phonenumber") == null)
			screen.add(new IppHidden(IppHidden.Type.PHONENUMBER, "phonenumber"));
		if (!hidden.containsKey("ipaddress") || hidden.get("ipaddress") == null)
			screen.add(new IppHidden(IppHidden.Type.IPADDRESS, "ipaddress"));
	}
	
}
