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

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import analyzer.process.TextAnalyzer;
import otula.utils.XMLFormatter;


/**
 * Task for processing user feedback received from the front-end.
 *
 */
public class FeedbackTask extends AbstractTask{
	private static final Logger LOGGER = Logger.getLogger(FeedbackTask.class);
	private List<VisualObject> _tags = null;

	/**
	 * 
	 * @param taskId
	 * @param callbackURI
	 */
	public FeedbackTask(String taskId, String callbackURI) {
		super(taskId, callbackURI, TaskType.FACEBOOK_PROFILE_SUMMARIZATION_FEEDBACK);
	}

	@Override
	public void run() {
		LOGGER.debug("Executing feedback task.");
		TextAnalyzer textAnalyzer = new TextAnalyzer();
		for(VisualObject object : _tags){
			textAnalyzer.addFeedback(object.getValue(), object.getRank(), object.getUserId());
		}
		LOGGER.debug("Feedback updated.");
	}
	
	/**
	 * 
	 * @param taskId
	 * @param callbackUri
	 * @param tags
	 * @return task or null on failure (bad tag list)
	 */
	public static FeedbackTask getTask(String taskId, String callbackUri, List<VisualObject> tags){
		if(tags == null || tags.isEmpty()){
			LOGGER.warn("Invalid tag list.");
			return null;
		}
		FeedbackTask fTask = new FeedbackTask(taskId, callbackUri);
		fTask._tags = tags;
		return fTask;
	}

	/**
	 * 
	 * @param taskDoc
	 * @return tag list extracted from the given document or null if none was extracted
	 */
	public static List<VisualObject> extractTagList(Document taskDoc) {
		NodeList nodes = taskDoc.getElementsByTagName(Definitions.ELEMENT_VISUAL_OBJECT);
		int nodeCount = nodes.getLength();
		if(nodeCount < 1){
			LOGGER.error("No "+Definitions.ELEMENT_VISUAL_OBJECT+" provided.");
			return null;
		}
		
		List<VisualObject> tags = new ArrayList<>(nodes.getLength());
		XMLFormatter formatter = new XMLFormatter();
		for(int i=0;i<nodeCount;++i){
			tags.add(formatter.toObject(nodes.item(i), VisualObject.class));
		}

		return tags;
	}
}
