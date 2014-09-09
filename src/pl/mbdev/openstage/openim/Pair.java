package pl.mbdev.openstage.openim;

/**
 * Pair of {@link java.lang.String} objects.
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
public class Pair {
	
	private final String s1;
	private final String s2;
	
	public Pair(String s1, String s2) {
		this.s1 = s1;
		this.s2 = s2;
	}
	
	public String get1() {
		return s1;
	}
	
	public String get2() {
		return s2;
	}
	
}
