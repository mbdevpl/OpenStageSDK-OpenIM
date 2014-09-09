package pl.mbdev.openstage.openim;

/**
 * Contains data, which identify a single user.
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
public class UserCredentials {
	
	private final String ipAddress;
	private final String phoneNumber;
	private String shortNumber;
	private String nickname;
	
	public UserCredentials(String ipAddress, String phoneNumber, String shortNumber,
			String nickname) {
		this.ipAddress = ipAddress;
		this.phoneNumber = phoneNumber;
		this.shortNumber = shortNumber;
		this.nickname = nickname;
	}
	
	public String getIpAddress() {
		return ipAddress;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public String getNickname() {
		return nickname;
	}
	
	public String getShortNumber() {
		return shortNumber;
	}
	
	public void setShortNumber(String shortNumber) {
		this.shortNumber = shortNumber;
	}
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	@Override
	public String toString() {
		return "[" + getPhoneNumber() + "," + getShortNumber() + "," + getIpAddress() + ","
				+ getNickname() + "]";
	}
	
}
