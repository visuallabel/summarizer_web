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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import otula.summarizerweb.TaskResults.TaskStatus;
import otula.utils.CommonUtils;
import otula.utils.XMLFormatter;
import analyzer.output.ObjectOutput;
import analyzer.process.TextAnalyzer;
import analyzer.segmentation.Centroid;

/**
 * 
 * Facebook Summarization Task, submitted by the front-end for analysis.
 *
 */
public class FacebookSummarizationTask extends AbstractTask{
	private static final XMLFormatter FORMATTER = new XMLFormatter();
	private static final Logger LOGGER = Logger.getLogger(FacebookSummarizationTask.class);
	private String _backendId = null;
	private String _userId = null;
	private Document _profile = null;

	/**
	 * 
	 * @param taskId
	 * @param callbackURI
	 */
	protected FacebookSummarizationTask(String taskId, String callbackURI) {
		super(taskId, callbackURI, TaskType.FACEBOOK_PROFILE_SUMMARIZATION);
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
	public static FacebookSummarizationTask getTask(String taskId, String callbackUri, String userId, String backendId, Document profile){
		FacebookSummarizationTask sTask = new FacebookSummarizationTask(taskId, callbackUri);
		sTask._userId = userId;
		sTask._backendId = backendId;
		sTask._profile = profile;
		return sTask;
	}

	/**
	 * set new profile from the given task
	 * 
	 * @param task
	 * @return profile extracted from the given task
	 */
	public static Document extractProfile(Document task){
		NodeList nodes = task.getElementsByTagName(Definitions.ELEMENT_PROFILE);
		if(nodes.getLength() < 1){
			LOGGER.warn("No "+Definitions.ELEMENT_PROFILE+" provided.");
			return null;
		}

		try {	// create new document from the profile element stripping out all remaining elements
			Document profile = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Node root = profile.appendChild(profile.createElement(Definitions.ELEMENT_PROFILE));
			nodes = nodes.item(0).getChildNodes();
			for(int i=0, length = nodes.getLength();i<length;++i){
				root.appendChild(profile.importNode(nodes.item(i), true));
			}

			return profile;
		} catch (ParserConfigurationException ex) {
			LOGGER.warn(ex, ex);
			return null;
		}
	}

	@Override
	public void run() {
		LOGGER.debug("Starting task with parameters: taskId: "+getTaskId()+", backendId: "+_backendId+", userId: "+_userId+", taskType: "+getTaskType().toTaskTypeString()+", callbackUri: "+getCallbackURI());

		String inputPath = generateInputPath();
		if(!createXMLFile(inputPath)){ // print the generated xml document to disk for the summarizer
			LOGGER.error("Failed to create new file.");
			return;
		}

		AnalysisResults results = analyze(inputPath);
		LOGGER.debug("Summarization finished.");

		if(keepXML()){
			LOGGER.debug("Keeping xml input: "+inputPath);
		}else if(!removeFile(inputPath)){	// try to remove the generated xml file if required
			LOGGER.warn("Failed to remove xml file: "+inputPath);
		}
		
		postResults(results, TaskStatus.COMPLETED);	// this will only submit successfully completed results
	}
	
	/**
	 * 
	 * @return true if facebook profile xml is kept (not deleted) after analysis
	 */
	protected boolean keepXML(){
		return ServiceInitializer.getServiceInfo().keepFacebookXML();
	}
	
	/**
	 * 
	 * @return randomly generated input path for a file
	 */
	protected String generateInputPath(){
		StringBuilder path = new StringBuilder(ServiceInitializer.getServiceInfo().getDataInputPath());
		path.append(File.separator);
		path.append(CommonUtils.generateUniqueString());
		path.append('_');
		
		NodeList nodes = _profile.getElementsByTagName(Definitions.ELEMENT_FACEBOOK_ID);
		if(nodes.getLength() < 1){
			LOGGER.warn("No element "+Definitions.ELEMENT_FACEBOOK_ID+".");
		}else{
			path.append(nodes.item(0).getTextContent());
			path.append('_');
		}
		
		path.append(getTaskType().name());
		path.append(".xml");
		return path.toString();
	}
	
	/**
	 * 
	 * @param inputPath 
	 * @return list of visual objects extracted from a file located in the input path or null if none was extracted
	 */
	protected AnalysisResults analyze(String inputPath){
		List<MediaObject> tags = new ArrayList<>();
		List<Media> photos = new ArrayList<>();
		TextAnalyzer ta = new TextAnalyzer();
		String configurationFile = ServiceInitializer.getServiceInfo().getFacebookConfigurationFilePath();
		LOGGER.debug("Starting Facebook summarization for file: "+inputPath+", using configuration file: "+configurationFile);
		ObjectOutput out = ta.runCFAnalysis(configurationFile, inputPath);
		
		List<Centroid> centroids = out.getSingleOutput();
		LOGGER.debug("Centroid count for task id "+getTaskId()+": "+centroids.size());
		for(Centroid c : centroids){
			String value = c.getTag();
			MediaObject vo = MediaObject.getMediaObject(_backendId, c.getTfidf(), c.getUniqueID(), _userId, value);
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
	
	/**
	 * This will add new photo in the list if nothing is found, and return the created photo
	 * 
	 * @param photos
	 * @param photoGUID
	 * @return return the photo with the given guid
	 */
	protected Media getPhoto(List<Media> photos, String photoGUID){
		Media found = null;
		for(Media p : photos){
			if(p.getGUID().equals(photoGUID)){
				found = p;
				break;
			}
		}
		if(found == null){
			found = new Media(photoGUID);
			photos.add(found);
		}
		return found;
	}

	/**
	 * 
	 * @param results
	 * @param status
	 */
	private void postResults(AnalysisResults results, TaskStatus status){
		String taskId = getTaskId();
		TaskType taskType = getTaskType();
		TaskResults task = new TaskResults(taskId, taskType, _backendId);
		
		int tagCount = results.countObjects();
		if(tagCount > 0){
			task.setTags(results.getObjects());
		}
		
		int photoCount = results.countPhotos();
		if(photoCount > 0){
			task.setMedia(results.getPhotos());
		}
		
		task.setStatus(status);
		
		String uri = getCallbackURI();
		LOGGER.debug("Sending tags to "+uri+", tagCount: "+tagCount+", photoCount: "+photoCount+", task id: "+taskId+", task type: "+taskType.name());

		HttpPost post = new HttpPost(uri);
		post.setEntity(new StringEntity(FORMATTER.toString(task), Definitions.CHARSET_UTF8));
		post.setHeader("Content-Type", "text/xml; charset=utf-8");
		
		try(CloseableHttpClient client = HttpClients.createDefault()) {	// post results to front-end
			LOGGER.debug("Calling "+uri);
			try(CloseableHttpResponse response = client.execute(post)){
				StatusLine sl = response.getStatusLine();
				int statusCode = sl.getStatusCode();
				if(statusCode < 200 || statusCode >= 300){
					LOGGER.error("Server responded: "+statusCode+", "+sl.getReasonPhrase());
				}else{
					LOGGER.debug("Results submitted successfully.");
				}
			}
		} catch (IOException ex) {	// we could re-try later, but for now, let's just ignore failed attempts.
			LOGGER.error(ex, ex);
		}
	}

	/**
	 * 
	 * @param inputFilePath
	 * @return true on success
	 */
	private boolean createXMLFile(String inputFilePath){
		LOGGER.debug("Creating profile xml file to path: "+inputFilePath);
		try (PrintWriter writer  = new PrintWriter(inputFilePath, Definitions.CHARSET_UTF8)){	// write in UTF8, though this is largely irrelevant, as Mead will print the results in ASCII
			writer.print((new XMLFormatter()).toString(_profile));
			writer.flush();
		} catch (IOException ex) {
			LOGGER.error(ex, ex);
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param filePath
	 * @return true on success
	 */
	private boolean removeFile(String filePath){
		LOGGER.debug("Removing file from path: "+filePath);
		return (new File(filePath)).delete();
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
	 * @return profile
	 */
	public Document getProfile() {
		return _profile;
	}

	/**
	 * 
	 * @param profile
	 */
	public void setProfile(Document profile) {
		_profile = profile;
	}
	
	/**
	 * contains analysis results
	 *
	 */
	public static class AnalysisResults{
		private List<Media> _photos = null;
		private List<MediaObject> _objects = null;
		
		/**
		 * 
		 * @param objects
		 * @param photos
		 */
		public AnalysisResults(List<MediaObject> objects, List<Media> photos){
			_objects = objects;
			_photos = photos;
		}

		/**
		 * @return the photos
		 */
		public List<Media> getPhotos() {
			return _photos;
		}

		/**
		 * @return the objects
		 */
		public List<MediaObject> getObjects() {
			return _objects;
		}
		
		/**
		 * 
		 * @return number of photos in this result set
		 */
		public int countPhotos(){
			return (_photos == null ? 0 : _photos.size());
		}
		
		/**
		 * 
		 * @return number of objects in this result set
		 */
		public int countObjects(){
			return (_objects == null ? 0 : _objects.size());
		}
	} // class AnalysisResults
}
