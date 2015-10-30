/**
 * Copyright 2014 Tampere University of Technology, Pori Department
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
package otula.summarizerweb.debug;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import otula.summarizerweb.Response;

/**
 * Response with debug fields
 */
@XmlRootElement(name=otula.summarizerweb.Definitions.ELEMENT_RESPONSE)
@XmlAccessorType(XmlAccessType.NONE)
public class DebugResponse extends Response {
	@XmlElement(name=Definitions.ELEMENT_USER)
	@XmlElementWrapper(name=Definitions.ELEMENT_USER_LIST)
	private List<User> _users = null;

	/**
	 * @return the users
	 */
	public List<User> getUsers() {
		return _users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(List<User> users) {
		_users = users;
	}
}
