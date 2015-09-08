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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import otula.utils.CommonUtils;
import analyzer.output.ObjectOutput;
import analyzer.process.TextAnalyzer;
import analyzer.segmentation.Centroid;

/**
 * re-implementation of FacebookSummarization task for Twitter profile summarization.
 *
 */
public class TwitterSummarizationTask extends FacebookSummarizationTask {
	private static final Logger LOGGER = Logger.getLogger(TwitterSummarizationTask.class);

	/**
	 * 
	 * @param taskId
	 * @param callbackURI
	 */
	private TwitterSummarizationTask(String taskId, String callbackURI) {
		super(taskId, callbackURI);
		setTaskType(TaskType.TWITTER_PROFILE_SUMMARIZATION);
	}

	/**
	 * 
	 * @param taskId
	 * @param callbackUri
	 * @param userId
	 * @param backendId
	 * @param profile
	 * @return task or null on failure (bad xml)
	 */
	public static TwitterSummarizationTask getTask(String taskId, String callbackUri, String userId, String backendId, Document profile){
		TwitterSummarizationTask tTask = new TwitterSummarizationTask(taskId, callbackUri);
		tTask.setUserId(userId);
		tTask.setBackendId(backendId);
		tTask.setProfile(profile);
		tTask.setTaskType(TaskType.TWITTER_PROFILE_SUMMARIZATION);
		return tTask;
	}
	
	@Override
	protected AnalysisResults analyze(String inputPath){
		List<MediaObject> tags = new ArrayList<>();
		List<Media> photos = new ArrayList<>();
		TextAnalyzer ta = new TextAnalyzer();
		String configurationFile = ServiceInitializer.getServiceInfo().getTwitterConfigurationFilePath();
		LOGGER.debug("Starting Twitter summarization for file: "+inputPath+", using configuration file: "+configurationFile);
		ObjectOutput out = ta.runCFTwitterAnalysis(configurationFile, inputPath);
		
		List<Centroid> centroids = out.getSingleOutput();
		LOGGER.debug("Centroid count for task id "+getTaskId()+": "+centroids.size());
		for(Centroid c : centroids){
			MediaObject vo = MediaObject.getMediaObject(getBackendId(), c.getTfidf(), c.getUniqueID(), getUserId(), c.getTag());
			if(vo == null){
				LOGGER.debug("Ignored invalid media object.");
				continue;
			}
			
			String photoGUID = c.getPhotoUID();
			if(StringUtils.isBlank(photoGUID)){
				tags.add(vo);
			}else{
				Media p = getPhoto(photos, photoGUID);
				p.addVisualObject(vo);
			}
		}
		LOGGER.debug("Photo count for task id "+getTaskId()+": "+photos.size());
		
		return new AnalysisResults(tags, photos);
	}
	
	@Override
	protected boolean keepXML(){
		return ServiceInitializer.getServiceInfo().keepTwitterXML();
	}
	
	@Override
	protected String generateInputPath(){
		StringBuilder path = new StringBuilder(ServiceInitializer.getServiceInfo().getDataInputPath());
		path.append(File.separator);
		path.append(CommonUtils.generateUniqueString());
		path.append('_');
		
		NodeList nodes = getProfile().getElementsByTagName(Definitions.ELEMENT_TWITTER_ID);
		if(nodes.getLength() < 1){
			LOGGER.warn("No element "+Definitions.ELEMENT_TWITTER_ID+".");
		}else{
			path.append(nodes.item(0).getTextContent());
			path.append('_');
		}
		
		nodes = getProfile().getElementsByTagName(Definitions.ELEMENT_SCREENNAME);
		if(nodes.getLength() < 1){
			LOGGER.warn("No element "+Definitions.ELEMENT_SCREENNAME+".");
		}else{
			path.append(nodes.item(0).getTextContent());
			path.append('_');
		}
		
		path.append(getTaskType().name());
		path.append(".xml");
		return path.toString();
	}
}
