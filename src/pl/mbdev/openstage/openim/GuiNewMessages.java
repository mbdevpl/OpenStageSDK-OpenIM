/**
 * 
 */
package pl.mbdev.openstage.openim;

import java.util.HashMap;

import pl.mbdev.openstage.IppAlert;
import pl.mbdev.openstage.IppCommand;
import pl.mbdev.openstage.IppCommand.DisplayOn;

/**
 * Seen when a push request causes the IM to launch, serves as an alert that informs about
 * new messages waiting in the inbox.
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
public class GuiNewMessages extends GuiIm {
	
	/**
	 * @param serverAddress
	 * @param hidden
	 */
	public GuiNewMessages(String serverAddress, HashMap<String, String> hidden) {
		super(hidden);
		screen.add(new IppAlert("Open IM: New messages", serverAddress,
				"Please visit your inbox.", null, null, IppAlert.Type.INFO, null));
		screen.add(new IppCommand("Go to inbox", null, null, DisplayOn.OPTIONS));
	}
}
