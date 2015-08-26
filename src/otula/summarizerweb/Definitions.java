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

/**
 * 
 * Common definitions for SummarizerWeb.
 *
 */
public final class Definitions {
	/* elements */
	/** xml element declaration */
	public static final String ELEMENT_BACKEND_ID = "backendId";
	/** xml element declaration */
	public static final String ELEMENT_CALLBACK_URI = "callbackUri";
	/** xml element declaration */
	public static final String ELEMENT_CONFIDENCE = "confidence";
	/** xml element declaration */
	public static final String ELEMENT_FACEBOOK_ID = "facebookId";
	/** xml element declaration */
	public static final String ELEMENT_GUID = "UID";
	/** xml element declaration */
	public static final String ELEMENT_OBJECT_ID = "objectId";
	/** xml element declaration */
	public static final String ELEMENT_MEDIA = "media";
	/** xml element declaration */
	public static final String ELEMENT_MEDIALIST = "mediaList";
	/** xml element declaration */
	public static final String ELEMENT_MEDIA_TYPE = "mediaType";
	/** xml element declaration */
	public static final String ELEMENT_PROFILE = "profile";
	/** xml element declaration */
	public static final String ELEMENT_RANK = "rank";
	/** xml element declaration */
	public static final String ELEMENT_RESPONSE = "response";
	/** xml element declaration */
	public static final String ELEMENT_SCREENNAME = "screenName";
	/** xml element declaration */
	public static final String ELEMENT_STATUS = "status";
	/** xml element declaration */
	public static final String ELEMENT_TASK_ID = "taskId";
	/** xml element declaration */
	public static final String ELEMENT_TASK_RESULTS = "taskResults";
	/** xml element declaration */
	public static final String ELEMENT_TASK_TYPE = "taskType";
	/** xml element declaration */
	public static final String ELEMENT_TWITTER_ID = "twitterId";
	/** xml element declaration */
	public static final String ELEMENT_USER_ID = "userId";
	/** xml element declaration */
	public static final String ELEMENT_VALUE = "value";
	/** xml element declaration */
	public static final String ELEMENT_VISUAL_OBJECT = "object";
	/** xml element declaration */
	public static final String ELEMENT_VISUAL_OBJECT_ID = "visualObjectId";
	/** xml element declaration */
	public static final String ELEMENT_VISUAL_OBJECT_TYPE = "objectType";
	/** xml element declaration */
	public static final String ELEMENT_VISUAL_OBJECTLIST = "objectList";
	
	/* attributes */
	/** xml response attribute declaration */
	protected static final String ATTRIBUTE_METHOD = "method";
	/** xml response attribute declaration */
	protected static final String ATTRIBUTE_SERVICE = "service";
	
	/* methods */
	/** service method declaration */
	protected static final String METHOD_ADD_TASK = "addTask";
	
	/* media types */
	/** media type name for photo/image content */
	public static final String MEDIA_TYPE_PHOTO = "PHOTO";
	/** media type name for video content */
	public static final String MEDIA_TYPE_VIDEO = "VIDEO";
	
	/* common */
	/** UTF-8 encoding charset */
	public static final String CHARSET_UTF8 = "UTF-8";
	/** HTTP uri parameter separator */
	public static final char SEPARATOR_VALUES = ',';
	/** string value for boolean true */
	public static final String STRING_TRUE = "true";
	/** OK status code */
	public static final String STATUS_OK = "OK";
	
	/**
	 * 
	 */
	private Definitions(){
		// nothing needed
	}
}
