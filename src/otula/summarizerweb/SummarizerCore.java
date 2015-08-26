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

import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import otula.summarizerweb.AbstractTask.TaskType;

/**
 * The Core methods for SummarizerWeb.
 *
 */
public final class SummarizerCore {
	private static final Logger LOGGER = Logger.getLogger(SummarizerCore.class);
	
	/**
	 * 
	 */
	private SummarizerCore(){
		// nothing needed
	}
	
	/**
	 * 
	 * @param taskDoc
	 * @return response
	 */
	public static Response addTask(Document taskDoc){
		NodeList nodes = taskDoc.getElementsByTagName(Definitions.ELEMENT_TASK_TYPE);
		if(nodes.getLength() < 1){
			LOGGER.error("No "+Definitions.ELEMENT_TASK_TYPE+" provided.");
			return null;
		}

		TaskType type = TaskType.fromTaskTypeString(nodes.item(0).getTextContent());
		if(type == null){
			LOGGER.error("Bad or missing "+Definitions.ELEMENT_TASK_TYPE+".");
			return null;
		}

		nodes = taskDoc.getElementsByTagName(Definitions.ELEMENT_CALLBACK_URI);
		if(nodes.getLength() < 1){
			LOGGER.error("No "+Definitions.ELEMENT_CALLBACK_URI+" provided.");
			return null;
		}
		String callbackUri = nodes.item(0).getTextContent();
		if(callbackUri.isEmpty()){
			LOGGER.error("Bad or missing "+Definitions.ELEMENT_CALLBACK_URI+".");
			return null;
		}

		nodes = taskDoc.getElementsByTagName(Definitions.ELEMENT_TASK_ID);
		if(nodes.getLength() < 1){
			LOGGER.error("No "+Definitions.ELEMENT_TASK_ID+" provided.");
			return null;
		}
		String taskId = nodes.item(0).getTextContent();
		if(taskId.isEmpty()){
			LOGGER.error("Bad or missing "+Definitions.ELEMENT_TASK_ID+".");
			return null;
		}

		nodes = taskDoc.getElementsByTagName(Definitions.ELEMENT_BACKEND_ID);
		if(nodes.getLength() < 1){
			LOGGER.error("No "+Definitions.ELEMENT_BACKEND_ID+" provided.");
			return null;
		}
		String backendId = nodes.item(0).getTextContent();
		if(backendId.isEmpty()){
			LOGGER.error("Bad or missing "+Definitions.ELEMENT_BACKEND_ID+".");
			return null;
		}

		nodes = taskDoc.getElementsByTagName(Definitions.ELEMENT_USER_ID);
		if(nodes.getLength() < 1){
			LOGGER.error("No "+Definitions.ELEMENT_USER_ID+" provided.");
			return null;
		}
		String userId = nodes.item(0).getTextContent();
		if(userId.isEmpty()){
			LOGGER.error("Bad or missing "+Definitions.ELEMENT_USER_ID+".");
			return null;
		}
		
		LOGGER.debug("Received task of type: "+type.name()+" with id: "+taskId+" for user, id: "+userId);
		
		Response s = new Response();
		AbstractTask task = null;
		switch(type){
			case FACEBOOK_PROFILE_SUMMARIZATION:{
				Document profile = FacebookSummarizationTask.extractProfile(taskDoc);
				if(profile == null){
					LOGGER.error("Bad or missing "+Definitions.ELEMENT_PROFILE+".");
					return null;
				}
				task = FacebookSummarizationTask.getTask(taskId, callbackUri, userId, backendId, profile);
				break;
			}
			case FACEBOOK_PROFILE_SUMMARIZATION_FEEDBACK:{
				List<VisualObject> tagList = FeedbackTask.extractTagList(taskDoc);
				if(!VisualObject.isValid(tagList)){
					LOGGER.error("Bad or missing "+Definitions.ELEMENT_VISUAL_OBJECTLIST+".");
					return null;
				}
				task = FeedbackTask.getTask(taskId, callbackUri, tagList);
				break;
			}
			case TWITTER_PROFILE_SUMMARIZATION:{
				Document profile = TwitterSummarizationTask.extractProfile(taskDoc);
				if(profile == null){
					LOGGER.error("Bad or missing "+Definitions.ELEMENT_PROFILE+".");
					return null;
				}
				task = TwitterSummarizationTask.getTask(taskId, callbackUri, userId, backendId, profile);
				break;
			}
			case BACKEND_FEEDBACK:
				List<Media> mediaList = BackendFeedbackTask.extractMediaList(taskDoc);
				if(!Media.isValid(mediaList)){
					LOGGER.error("Bad or missing "+Definitions.ELEMENT_MEDIALIST+".");
					return null;
				}
				task = BackendFeedbackTask.getTask(taskId, callbackUri, mediaList);
				break;
			default:
				LOGGER.error("Unsupported taskType: "+type.name());
				break;
		}
		
		if(task == null){
			s.setStat("Failed to create task.");
		}else{
			scheduleTask(task);
			s.setStat(Definitions.STATUS_OK);
		}
		s.setMethod(Definitions.METHOD_ADD_TASK);
		s.setService(ServiceInitializer.getServiceInfo().getServletContextPath());
		return s;
	}
	
	/**
	 * 
	 * @param task
	 */
	public static void scheduleTask(AbstractTask task){
		ServiceInitializer.getTaskScheduler().executeTask(task);
	}
}
