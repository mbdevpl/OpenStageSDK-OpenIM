package pl.mbdev.openstage.openim;

import java.util.ArrayList;
import java.util.HashMap;

import pl.mbdev.openstage.IppCommand;
import pl.mbdev.openstage.IppCommand.DisplayOn;
import pl.mbdev.openstage.IppStringItem;
import pl.mbdev.openstage.IppTextField;

/**
 * Authorisation form, used to register the user.
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
public class GuiAuthorization extends GuiImForm {
	
	public GuiAuthorization(String serverAddress, ArrayList<Pair> info,
			HashMap<String, String> hidden) {
		super(serverAddress, "", info, null, hidden);
		
		String ip = hidden.get("ipaddress");
		
		String num = hidden.get("phonenumber");
		
		String shortNum = hidden.get("shortNum");
		if (shortNum == null)
			shortNum = num.substring(7);
		
		String nick = hidden.get("nickname");
		if (nick == null)
			nick = "u".concat(ip.substring(ip.lastIndexOf('.') + 1));
		
		form.add(new IppTextField("Nickname", nick, "nickname"));
		form.add(new IppTextField("Short number", shortNum, "shortnumber"));
		form.add(new IppStringItem("Phone number", num));
		form.add(new IppStringItem("IP address", ip));
		screen.add(new IppCommand("Create account", IppCommand.Type.SELECT, "register",
				"yes", DisplayOn.OPTIONS, false));
	}
	
}
