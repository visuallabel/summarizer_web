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
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;

import otula.summarizerweb.AbstractTask.TaskType;

/**
 * 
 * This class represents a basic "Task response", ie. the analyzed content that is to be sent back to the front-ends ervice.
 * 
 * The XML annotations are primarily made for marshalling (object->xml string), but SHOULD also work for converting xml strings to objects.
 *
 */
@XmlRootElement(name=Definitions.ELEMENT_TASK_RESULTS)
@XmlAccessorType(XmlAccessType.NONE)
public class TaskResults {
	private static final Logger LOGGER = Logger.getLogger(TaskResults.class);
	@XmlElement(name=Definitions.ELEMENT_TASK_ID)
	private String _taskId = null;
	@XmlElement(name=Definitions.ELEMENT_TASK_TYPE)
	private TaskType _taskType = null;
	@XmlElement(name=Definitions.ELEMENT_BACKEND_ID)
	private String _backendId = null;
	@XmlElement(name=Definitions.ELEMENT_STATUS)
	private TaskStatus _status = null;
	@XmlElementWrapper(name=Definitions.ELEMENT_MEDIA_OBJECTLIST)
	@XmlElement(name=Definitions.ELEMENT_MEDIA_OBJECT)
	private List<MediaObject> _tags = null;
	@XmlElementWrapper(name=Definitions.ELEMENT_MEDIALIST)
	@XmlElement(name=Definitions.ELEMENT_MEDIA)
	private List<Media> _media = null;
	
	/**
	 * Subset of Enumeration codes for TaskStatus as needed by the summarizer.
	 * 
	 */
	@XmlEnum
	public enum TaskStatus{
		/** task has completed successfully */
		COMPLETED,
		/** an error has occured while processing the task */
		ERROR
	}	// enum TaskStatus
	
	/**
	 * Required for serialization.
	 */
	public TaskResults(){
		// nothing needed
	}
	
	/**
	 * 
	 * @param taskId
	 * @param type
	 * @param backendId
	 */
	public TaskResults(String taskId, TaskType type, String backendId){
		_taskId = taskId;
		_taskType = type;
		_backendId = backendId;
	}

	/**
	 * 
	 * @return task id
	 */
	public String getTaskId() {
		return _taskId;
	}

	/**
	 * 
	 * @param taskId
	 */
	public void setTaskId(String taskId) {
		_taskId = taskId;
	}

	/**
	 * 
	 * @return task type
	 */
	public TaskType getTaskType() {
		return _taskType;
	}

	/**
	 * 
	 * @param taskType
	 */
	public void setTaskType(TaskType taskType) {
		_taskType = taskType;
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
	 * @return status
	 */
	public TaskStatus getStatus() {
		return _status;
	}

	/**
	 * 
	 * @param status
	 */
	public void setStatus(TaskStatus status) {
		_status = status;
	}

	/**
	 * 
	 * @return list of tags
	 */
	public List<MediaObject> getTags() {
		return _tags;
	}

	/**
	 * 
	 * @param tags
	 */
	public void setTags(List<MediaObject> tags) {
		_tags = tags;
	}

	/**
	 * @return the medias
	 */
	public List<Media> getMedia() {
		return _media;
	}

	/**
	 * @param medias the medias to set
	 */
	public void setMedia(List<Media> medias) {
		_media = medias;
	}
	
	/**
	 * 
	 * @param tag
	 */
	public void addTag(MediaObject tag){
		if(_tags == null){
			_tags = new ArrayList<>();
		}
		_tags.add(tag);
	}
	
	/**
	 * 
	 * @param mediaGUID
	 * @param tag
	 */
	public void addTag(String mediaGUID, MediaObject tag){
		if(mediaGUID == null || mediaGUID.isEmpty()){
			LOGGER.warn("Invalid guid.");
			return;
		}
		Media p = null;
		if(_media == null){
			_media = new ArrayList<>();
			_media.add((p = new Media(mediaGUID)));
		}else{
			for(Media t : _media){
				if(mediaGUID.equals(t.getGUID())){
					p = t;
					break;
				}
			} // for
			if(p == null){ // no existing media with the given guid
				_media.add((p = new Media(mediaGUID)));
			}
		}
		p.addVisualObject(tag);
	}
}
