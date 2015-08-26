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

import javax.xml.bind.annotation.XmlEnum;

/**
 * 
 * Base class for runnable tasks.
 *
 */
public abstract class AbstractTask implements Runnable{
	private String _taskId = null;
	private String _callbackURI = null;
	private TaskType _taskType = null;
	
	/**
	 * TaskType enumeration. Contains only the code valid for this context, other enumerations defined by D2I Content Analysis
	 * specification are not included.
	 *
	 */
	@XmlEnum
	public enum TaskType{
		/** back-end feedback task */
		BACKEND_FEEDBACK,
		/** facebook profile summarization task */
		FACEBOOK_PROFILE_SUMMARIZATION,
		/** facebook profile summarization feedback task */
		FACEBOOK_PROFILE_SUMMARIZATION_FEEDBACK,
		/** twitter profile summarization task */
		TWITTER_PROFILE_SUMMARIZATION;
		
		/**
		 * 
		 * @return task type as string
		 */
		public String toTaskTypeString(){
			return name().toUpperCase();
		}
		
		/**
		 * 
		 * @param s
		 * @return the string as TaskType or null if not a valid type
		 */
		public static TaskType fromTaskTypeString(String s){
			if(s == null || s.isEmpty()){
				return null;
			}
			for(TaskType t : TaskType.values()){
				if(t.toTaskTypeString().equalsIgnoreCase(s)){
					return t;
				}
			}
			return null;
		}
	}	// enum TaskType

	/**
	 * 
	 * @return task id
	 */
	public String getTaskId() {
		return _taskId;
	}

	/**
	 * 
	 * @return callback uri
	 */
	public String getCallbackURI() {
		return _callbackURI;
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
	 * @param taskId
	 * @param callbackURI
	 * @param type
	 */
	public AbstractTask(String taskId, String callbackURI, TaskType type){
		_taskId = taskId;
		_callbackURI = callbackURI;
		_taskType = type;
	}
}
