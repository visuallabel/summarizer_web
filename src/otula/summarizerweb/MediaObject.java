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

import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * Minimal implementation of VisualObject for Summarizer
 */
@XmlRootElement(name=Definitions.ELEMENT_MEDIA_OBJECT)
@XmlAccessorType(XmlAccessType.NONE)
public class MediaObject {
	private static final Logger LOGGER = Logger.getLogger(MediaObject.class);
	private static final Integer DEFAULT_RANK = 0;
	@XmlElement(name = Definitions.ELEMENT_BACKEND_ID)
	private String _backendId = null;
	@XmlElement(name = Definitions.ELEMENT_CONFIDENCE)
	private Double _confidence = null;
	@XmlElement(name = Definitions.ELEMENT_OBJECT_ID)
	private String _objectId = null;
	@XmlElement(name = Definitions.ELEMENT_RANK)
	private Integer _rank = DEFAULT_RANK;
	@XmlElement(name = Definitions.ELEMENT_STATUS)
	private ConfirmationStatus _status = ConfirmationStatus.CANDIDATE;
	@XmlElement(name = Definitions.ELEMENT_MEDIA_OBJECT_TYPE)
	private VisualObjectType _type = VisualObjectType.KEYWORD;
	@XmlElement(name = Definitions.ELEMENT_USER_ID)
	private String _userId = null;
	@XmlElement(name = Definitions.ELEMENT_VALUE)
	private String _value = null;
	@XmlElement(name = Definitions.ELEMENT_MEDIA_OBJECT_ID)
	private String _visualObjectId = null;

	/**
	 * Subset of Visual Object types
	 * 
	 */
	@XmlEnum
	public enum VisualObjectType{
		/** visual object is a keyword or a tag */
		KEYWORD
	}  // enum VisualObjectType

	/**
	 * Subset of Visual object's confirmation statuses
	 * 
	 */
	@XmlEnum
	public enum ConfirmationStatus{
		/** the visual object is a candidate */
		CANDIDATE;

		@Override
		public String toString() {
			return name(); // make sure the correct value is returned
		}
	}  // enum ConfirmationStatus

	/**
	 * for serialization
	 */
	public MediaObject(){
		// nothing needed
	}
	
	/**
	 * 
	 * @param backendId
	 * @param confidence
	 * @param objectId
	 * @param userId
	 * @param value
	 */
	public MediaObject(String backendId, Double confidence, String objectId, String userId, String value){
		_backendId = backendId;
		_confidence = confidence;
		_objectId = objectId;
		_userId = userId;
		_value = value;
	}

	/**
	 * 
	 * @return confidence estimate for this object
	 */
	public Double getConfidence() {
		return _confidence;
	}

	/**
	 * 
	 * @param confidence
	 */
	public void setConfidence(Double confidence) {
		_confidence = confidence;
	}
	
	/**
	 * 
	 * @return user id value
	 */
	public String getUserId() {
		return _userId;
	}

	/**
	 * 
	 * @param userId
	 */
	public void setUserId(String userId) {
		_userId = userId;
	}

	/**
	 * 
	 * @return value
	 */
	public String getValue() {
		return _value;
	}

	/**
	 * 
	 * @return globally non-unique back-end generated id
	 */
	public String getObjectId() {
		return _objectId;
	}

	/**
	 * 
	 * @param objectId
	 */
	public void setObjectId(String objectId) {
		_objectId = objectId;
	}

	/**
	 * 
	 * @return back-end id
	 */
	public String getBackendId() {
		return _backendId;
	}

	/**
	 * 
	 * @param backendId
	 */
	public void setBackendId(String backendId) {
		_backendId = backendId;
	}

	/**
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		_value = value;
	}

	/**
	 * @return the rank
	 */
	public Integer getRank() {
		return _rank;
	}

	/**
	 * @param rank the rank to set
	 */
	public void setRank(Integer rank) {
		_rank = rank;
	}

	/**
	 * @return the status
	 */
	public ConfirmationStatus getStatus() {
		return _status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(ConfirmationStatus status) {
		_status = status;
	}

	/**
	 * @return the type
	 */
	public VisualObjectType getType() {
		return _type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(VisualObjectType type) {
		_type = type;
	}
	
	/**
	 * @return the visualObjectId
	 */
	public String getVisualObjectId() {
		return _visualObjectId;
	}

	/**
	 * @param visualObjectId the visualObjectId to set
	 */
	public void setVisualObjectId(String visualObjectId) {
		_visualObjectId = visualObjectId;
	}

	/**
	 * for sub-classing, use the static
	 * 
	 * @return true if userId, objectId, value and rank are given
	 * @see #isValid(MediaObject)
	 */
	protected boolean isValid(){
		if(StringUtils.isBlank(_userId) || StringUtils.isBlank(_objectId) || StringUtils.isBlank(_value) || _rank == null){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 
	 * @param visualObject can be null
	 * @return true if the passed object is valid
	 */
	public static boolean isValid(MediaObject visualObject){
		return (visualObject == null ? false : visualObject.isValid());
	}
	
	/**
	 * 
	 * @param objects can be null
	 * @return true if the passed list of objects is valid
	 */
	public static boolean isValid(Collection<MediaObject> objects){
		if(objects == null || objects.isEmpty()){
			LOGGER.warn("Empty visual object collection.");
			return false;
		}else{
			for(MediaObject o : objects){
				if(!o.isValid()){
					return false;
				}
			}
			return true;
		} // else
	}
}
