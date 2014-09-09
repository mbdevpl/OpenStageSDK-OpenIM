package pl.mbdev.openstage.openim;

import java.io.PrintWriter;
import java.util.HashMap;

import pl.mbdev.openstage.IppCommand;
import pl.mbdev.openstage.IppDisplay;
import pl.mbdev.openstage.IppHidden;
import pl.mbdev.openstage.IppScreen;
import pl.mbdev.openstage.IppCommand.DisplayOn;

/**
 * Screen with hidden elements.
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
public abstract class GuiIm {
	
	protected IppDisplay display;
	
	protected IppScreen screen;
	
	private HashMap<String, String> hidden;
	
	public GuiIm(HashMap<String, String> hidden) {
		this.hidden = hidden;
		
		display = new IppDisplay(1, -1);
		screen = new IppScreen(1);
		display.add(screen);
	}
	
	/**
	 * Send.
	 * 
	 * @param out
	 *           writer of the servlet
	 */
	public void sendTo(PrintWriter out) {
		screen.add(new IppCommand(IppCommand.Type.EXIT, DisplayOn.OPTIONS));
		
		for (String key : hidden.keySet())
			if (hidden.get(key) != null)
				screen.add(new IppHidden(IppHidden.Type.VALUE, key, hidden.get(key)));
			else
				System.out.println("hidden field " + key + " was null");
		
		display.sendTo(out);
	}
	
}
