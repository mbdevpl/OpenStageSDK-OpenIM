package pl.mbdev.openstage.openim;

import java.util.ArrayList;
import java.util.HashMap;
import pl.mbdev.openstage.IppForm;
import pl.mbdev.openstage.IppStringItem;

/**
 * Instant messaging GUI with a form and list of system messages.
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
public abstract class GuiImForm extends GuiIm {
	
	protected IppForm form;
	
	/**
	 * Constructs new GUI of the OpenStage Instant Messaging.
	 * 
	 * @param serverAddress
	 *           full address of the application
	 * @param title
	 *           title of the screen
	 * @param info
	 *           system messages to display
	 * @param msgs
	 *           user messages to display
	 * @param hidden
	 *           hidden parameters of the form
	 */
	public GuiImForm(String serverAddress, String title, ArrayList<Pair> info,
			ArrayList<InstantMessage> msgs, HashMap<String, String> hidden) {
		super(hidden);
		
		form = new IppForm("Open IM: ".concat(title), serverAddress);
		screen.add(form);
		
		for (Pair p : info)
			form.add(new IppStringItem(p.get1(), p.get2()));
	}
}
