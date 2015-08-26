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
package otula.summarizerweb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * Response root element, as specified by the D2I Content Analysis Specification.
 *
 */
@XmlRootElement(name=Definitions.ELEMENT_RESPONSE)
@XmlAccessorType(XmlAccessType.NONE)
public class Response {
	@XmlElement(name=Definitions.ELEMENT_STATUS)
	private String _stat = null; // actually an enumeration, but for this minimal implementation we don't care about the status
	@XmlAttribute(name=Definitions.ATTRIBUTE_METHOD)
	private String _method = null;
	@XmlAttribute(name=Definitions.ATTRIBUTE_SERVICE)
	private String _service = null;
	
	/**
	 * 
	 * @return status string
	 */
	public String getStat() {
		return _stat;
	}
	
	/**
	 * 
	 * @param stat
	 */
	public void setStat(String stat) {
		_stat = stat;
	}
	
	/**
	 * 
	 * @return method name
	 */
	public String getMethod() {
		return _method;
	}
	
	/**
	 * 
	 * @param method
	 */
	public void setMethod(String method) {
		_method = method;
	}
	
	/**
	 * 
	 * @return service name
	 */
	public String getService() {
		return _service;
	}
	
	/**
	 * 
	 * @param service
	 */
	public void setService(String service) {
		_service = service;
	}	
}
