package pl.mbdev.xml;

import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * Used to send lines of text.
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
public class StreamSender extends PrintWriter {
	
	/**
	 * Creates new stream sender.
	 * 
	 * @param out
	 *           output stream
	 */
	public StreamSender(OutputStream out) {
		super(out);
	}
	
	/**
	 * Sends a single line of text.
	 * 
	 * @param s
	 *           line to be sent.
	 */
	public void send(String s) {
		this.write(s + "\n");
		this.flush();
	}
}
