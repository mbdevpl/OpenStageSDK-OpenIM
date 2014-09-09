package pl.mbdev.openstage.openim;

import java.util.ArrayList;
import java.util.HashMap;

import pl.mbdev.openstage.IppCommand;
import pl.mbdev.openstage.IppCommand.DisplayOn;
import pl.mbdev.openstage.IppStringItem;
import pl.mbdev.openstage.IppTextField;

/**
 * Seen when writing a new message or replying to a message.
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
public class GuiWriteMessage extends GuiImForm {
	
	public GuiWriteMessage(String serverAddress, ArrayList<Pair> info,
			ArrayList<InstantMessage> msgs, HashMap<String, String> hidden) {
		super(serverAddress, "New message", info, msgs, hidden);
		
		form.add(new IppStringItem("From:", hidden.get("phonenumber")));
		
		String send = hidden.get("send");
		form.add(new IppTextField("To:", send != null ? send : "", "send"));
		hidden.remove("send");
		
		String msg = hidden.get("message");
		form.add(new IppTextField("Message", msg != null ? msg : "", "message"));
		hidden.remove("message");
		
		screen.add(new IppCommand("Send", null, null, DisplayOn.OPTIONS));
		screen.add(new IppCommand(IppCommand.Type.BACK, DisplayOn.OPTIONS));
	}
	
}
