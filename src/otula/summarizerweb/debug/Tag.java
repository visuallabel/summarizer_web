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
package otula.summarizerweb.debug;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Extracted tag
 */
@XmlRootElement(name=Definitions.ELEMENT_TAG)
@XmlAccessorType(XmlAccessType.NONE)
public class Tag {
	@XmlElement(name=otula.summarizerweb.Definitions.ELEMENT_BACKEND_ID)
	private String _backendId = null;
	@XmlElement(name=Definitions.ELEMENT_FREQUENCY)
	private Integer _frequency = null;
	@XmlElement(name=Definitions.ELEMENT_WEIGHT)
	private Double _weight = null;
	@XmlElement(name=Definitions.ELEMENT_TEXT)
	private String _text = null;
	@XmlElement(name=Definitions.ELEMENT_TRANSLATED_TEXT)
	private String _translatedText = null;
	
	/**
	 * @return the weight
	 */
	public Double getWeight() {
		return _weight;
	}
	
	/**
	 * @param weight the weight to set
	 */
	public void setWeight(Double weight) {
		_weight = weight;
	}
	
	/**
	 * @return the text
	 */
	public String getText() {
		return _text;
	}
	
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		_text = text;
	}
	
	/**
	 * @return the translatedText
	 */
	public String getTranslatedText() {
		return _translatedText;
	}
	
	/**
	 * @param translatedText the translatedText to set
	 */
	public void setTranslatedText(String translatedText) {
		_translatedText = translatedText;
	}

	/**
	 * @return the frequency
	 */
	public Integer getFrequency() {
		return _frequency;
	}

	/**
	 * @param frequency the frequency to set
	 */
	public void setFrequency(Integer frequency) {
		_frequency = frequency;
	}

	/**
	 * @return the backendId
	 */
	public String getBackendId() {
		return _backendId;
	}

	/**
	 * @param backendId the backendId to set
	 */
	public void setBackendId(String backendId) {
		_backendId = backendId;
	}
}
