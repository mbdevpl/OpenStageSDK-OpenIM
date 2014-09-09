package pl.mbdev.openstage.openim;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Representation of the entry in the Messages table of the database that stores data of
 * OpenStage IM.
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
public class InstantMessage {
	
	private GregorianCalendar timestamp;
	
	private String receiver, sender, message;
	
	/**
	 * @param timestamp
	 * @param receiver
	 * @param sender
	 * @param message
	 */
	public InstantMessage(String timestamp, String receiver, String sender, String message) {
		this.timestamp = new GregorianCalendar();
		if (timestamp != null)
			this.timestamp.setTimeInMillis(Long.parseLong(timestamp));
		this.receiver = receiver;
		this.sender = sender;
		this.message = message;
	}
	
	/**
	 * @return time stamp
	 */
	public GregorianCalendar getTimestamp() {
		return timestamp;
	}
	
	/**
	 * @return date and time
	 */
	public String getDateTime() {
		StringBuffer sb = new StringBuffer();
		int month = timestamp.get(Calendar.MONTH) + 1;
		int day = timestamp.get(Calendar.DAY_OF_MONTH);
		int minute = timestamp.get(Calendar.MINUTE);
		// sb.append(timestamp.get(Calendar.YEAR)).append("/");
		sb.append(month < 10 ? "0" : "").append(month).append("/")
				.append(day < 10 ? "0" : "").append(day).append(" ")
				.append(timestamp.get(Calendar.HOUR_OF_DAY)).append(":")
				.append(minute < 10 ? "0" : "").append(minute);
		return sb.toString();
	}
	
	/**
	 * @return raw time stamp in milliseconds
	 */
	public String getTimestampRaw() {
		return String.valueOf(timestamp.getTimeInMillis());
	}
	
	/**
	 * @return receiver
	 */
	public String getReceiver() {
		return receiver;
	}
	
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}
	
	/**
	 * @return sender
	 */
	public String getSender() {
		return sender;
	}
	
	/**
	 * @return message
	 */
	public String getMessage() {
		return message;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer("[");
		sb.append(getDateTime()).append('(').append(getTimestampRaw()).append("),")
				.append(receiver).append(',').append(sender).append(',').append(message)
				.append(']');
		return sb.toString();
	}
	
	/**
	 * @return string with message contents hidden
	 */
	public String toPrivateString() {
		StringBuffer sb = new StringBuffer("[");
		sb.append(getDateTime()).append('(').append(getTimestampRaw()).append("),")
				.append(receiver).append(',').append(sender).append(',')
				.append("[text hidden]").append(']');
		return sb.toString();
	}
	
}
