/**
 * Copyright 2014 Tampere University of Technology, Pori Unit
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package otula.utils;

import java.util.Date;
import java.util.UUID;

import otula.summarizerweb.Definitions;

/**
 * 
 * Common utilities for all classes
 *
 */
public final class CommonUtils {
	/**
	 * 
	 */
	private CommonUtils(){
		// nothing needed
	}
	
	/**
	 * 
	 * @return unique string
	 */
	public static String generateUniqueString(){
		return StringUtils.dateToISOString(new Date())+"_"+UUID.randomUUID().toString();
	}
	
	/**
	 * 
	 * @param s
	 * @return true if s != null AND s equals to "true"
	 */
	public static boolean isTrueString(String s){
		if(s == null){
			return false;
		}else{
			return Definitions.STRING_TRUE.equalsIgnoreCase(s.trim());
		}
	}
}
