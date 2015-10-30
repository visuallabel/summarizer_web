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
package otula.summarizerweb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.StringUtils;

/**
 * Minimal implementation of the Media class for summarization purposes.
 */
@XmlRootElement(name=Definitions.ELEMENT_MEDIA)
@XmlAccessorType(XmlAccessType.NONE)
public class Media {
	@XmlElement(name = Definitions.ELEMENT_GUID)
	private String _guid = null;
	@XmlElement(name = Definitions.ELEMENT_MEDIA_TYPE)
	private MediaType _mediaType = null;
	@XmlElementWrapper(name = Definitions.ELEMENT_MEDIA_OBJECTLIST)
	@XmlElement(name = Definitions.ELEMENT_MEDIA_OBJECT)
	private List<MediaObject> _visualObjects = null;
	
	/**
	 * Media type declaration.
	 */
	@XmlEnum
	public enum MediaType {
		/** media is of photo content */
		@XmlEnumValue(value = Definitions.MEDIA_TYPE_PHOTO)
		PHOTO,
		/** media is of video content */
		@XmlEnumValue(value = Definitions.MEDIA_TYPE_VIDEO)
		VIDEO;
	} // enum MediaType
	
	/**
	 * for serialization
	 */
	public Media(){
		// nothing needed
	}
	
	/**
	 * 
	 * @param guid
	 */
	public Media(String guid){
		_guid = guid;
	}
	
	/**
	 * @return the guid
	 */
	public String getGUID() {
		return _guid;
	}
	
	/**
	 * @param guid the guid to set
	 */
	public void setGUID(String guid) {
		_guid = guid;
	}
	
	/**
	 * @return the visualObjects
	 */
	public List<MediaObject> getVisualObjects() {
		return _visualObjects;
	}
	
	/**
	 * @param visualObjects the visualObjects to set
	 */
	public void setVisualObjects(List<MediaObject> visualObjects) {
		_visualObjects = visualObjects;
	}
	
	/**
	 * 
	 * @param visualObject
	 */
	public void addVisualObject(MediaObject visualObject){
		if(_visualObjects == null){
			_visualObjects = new ArrayList<>();
		}
		_visualObjects.add(visualObject);
	}
	
	/**
	 * 
	 * for sub-classing, use the static
	 * 
	 * @return true if object is valid
	 * @see #isValid(Media)
	 */
	protected boolean isValid(){
		if(StringUtils.isBlank(_guid)){
			return false;
		}else if(_visualObjects != null && !_visualObjects.isEmpty()){
			return MediaObject.isValid(_visualObjects);
		}else{
			return true;
		}
	}
	
	/**
	 * 
	 * @param media
	 * @return true if media is valid and not null
	 */
	public static boolean isValid(Media media){
		return (media == null ? false : media.isValid());
	}
	
	/**
	 * 
	 * @param media
	 * @return true if the list is not null or empty and all media are valid
	 */
	public static boolean isValid(Collection<Media> media){
		if(media == null || media.isEmpty()){
			return false;
		}else{
			for(Media p : media){
				if(!Media.isValid(p)){
					return false;
				}
			}
			return true;
		}
	}

	/**
	 * @return the mediaType
	 */
	public MediaType getMediaType() {
		return _mediaType;
	}

	/**
	 * @param mediaType the mediaType to set
	 */
	public void setMediaType(MediaType mediaType) {
		_mediaType = mediaType;
	}
}
