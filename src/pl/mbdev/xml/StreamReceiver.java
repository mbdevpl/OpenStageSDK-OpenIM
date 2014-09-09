package pl.mbdev.xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Used to receive lines of text.
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
public class StreamReceiver extends BufferedReader {
	
	/**
	 * Constructs new stream receiver.
	 * 
	 * @param in
	 *           input stream
	 */
	public StreamReceiver(InputStream in) {
		super(new InputStreamReader(in));
	}
	
	/**
	 * Receives string from the stream.
	 * 
	 * @return reference to one line of text
	 * @throws IOException
	 *            if there was an error while reading this line
	 */
	public String receiveString() throws IOException {
		return this.readLine();
	}
}
