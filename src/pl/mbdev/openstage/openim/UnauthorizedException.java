package pl.mbdev.openstage.openim;

/**
 * Thrown when an unauthorised action is done.
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
public class UnauthorizedException extends RuntimeException {
	
	/**
	 * ID.
	 */
	private static final long serialVersionUID = -2432166167902645336L;
	
	public UnauthorizedException() {
	}
	
	public UnauthorizedException(String message) {
		super(message);
	}
	
	public UnauthorizedException(Throwable cause) {
		super(cause);
	}
	
	public UnauthorizedException(String message, Throwable cause) {
		super(message, cause);
	}
	
}
