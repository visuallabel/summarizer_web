/**
 * Copyright 2015 Tampere University of Technology, Pori Unit
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

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import analyzer.database.DataFactory;
import otula.summarizerweb.Media.MediaType;
import otula.summarizerweb.VisualObject.VisualObjectType;
import otula.utils.XMLFormatter;

/**
 * class for tasks of type back-end feedback.
 */
public class BackendFeedbackTask extends AbstractTask {
	private static final Logger LOGGER = Logger.getLogger(BackendFeedbackTask.class);
	private List<Media> _media = null;

	/**
	 * 
	 * @param taskId
	 * @param callbackURI
	 */
	private BackendFeedbackTask(String taskId, String callbackURI) {
		super(taskId, callbackURI, TaskType.BACKEND_FEEDBACK);
	}

	@Override
	public void run() {
		for(Media p : _media){
			List<VisualObject> voids = p.getVisualObjects();
			if(voids == null || voids.isEmpty()){
				LOGGER.debug("Ignored media with no visual objects.");
				continue;
			}
			String guid = p.getGUID();
			if(StringUtils.isBlank(guid)){
				LOGGER.warn("Ignored media with invalid or missing guid.");
				continue;
			}
			for(VisualObject vo : voids){
				if(!VisualObjectType.KEYWORD.equals(vo.getType())){
					LOGGER.debug("Ignored visual object type of invalid type.");
					continue;
				}
				
				String backendId = vo.getBackendId();
				if(StringUtils.isBlank(backendId)){
					LOGGER.debug("Ignored visual object with missing backend id.");
					continue;
				}
				
				String visualObjectId = vo.getVisualObjectId();
				if(StringUtils.isBlank(visualObjectId)){
					LOGGER.warn("Ignored visual object with invalid or missing id.");
					continue;
				}
				
				String value = vo.getValue();
				if(StringUtils.isBlank(value)){
					LOGGER.warn("Ignored visual object without value.");
					continue;
				}
				DataFactory.addPicsomTag(value, guid, backendId, visualObjectId);
			}		
		}
	}

	/**
	 * 
	 * @param taskDoc
	 * @return list of media or null if none in the document
	 */
	public static List<Media> extractMediaList(Document taskDoc) {
		NodeList nodes = taskDoc.getElementsByTagName(Definitions.ELEMENT_MEDIA);
		int nodeCount = nodes.getLength();
		if(nodeCount < 1){
			LOGGER.error("No "+Definitions.ELEMENT_MEDIA+" provided.");
			return null;
		}
		
		List<Media> media = new ArrayList<>(nodes.getLength());
		XMLFormatter formatter = new XMLFormatter();
		for(int i=0;i<nodeCount;++i){
			Media m = formatter.toObject(nodes.item(i), Media.class);
			MediaType mediaType = m.getMediaType();
			if(MediaType.PHOTO.equals(mediaType)){
				media.add(m);
			}else{
				LOGGER.warn("Ignored media of unsupported type: "+mediaType);
			}		
		}

		return media;
	}

	/**
	 * 
	 * @param taskId
	 * @param callbackUri
	 * @param mediaList
	 * @return a new task or null on failure
	 */
	public static AbstractTask getTask(String taskId, String callbackUri, List<Media> mediaList) {
		if(mediaList == null || mediaList.isEmpty()){
			LOGGER.warn("Invalud media list.");
			return null;
		}
		BackendFeedbackTask task = new BackendFeedbackTask(taskId, callbackUri);
		task._media = mediaList;
		return task;
	}
}
