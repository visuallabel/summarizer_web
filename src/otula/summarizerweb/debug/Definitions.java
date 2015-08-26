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
package otula.summarizerweb.debug;

/**
 * Definitions for debug
 */
public final class Definitions {
	/* elements */
	/** xml element declaration */
	protected static final String ELEMENT_BACKEND_TAG = "backendTag";
	/** xml element declaration */
	protected static final String ELEMENT_BACKEND_TAG_LIST = "backendTagList";
	/** xml element declaration */
	protected static final String ELEMENT_BIGRAM = "bigram";
	/** xml element declaration */
	protected static final String ELEMENT_BIGRAM_LIST = "bigramList";
	/** xml element declaration */
	protected static final String ELEMENT_FREQUENCY = "frequency";
	/** xml element declaration */
	protected static final String ELEMENT_HASHTAG = "hashtag";
	/** xml element declaration */
	protected static final String ELEMENT_HASHTAG_LIST = "hashtagList";
	/** xml element declaration */
	protected static final String ELEMENT_PHOTO_URL = "photoUrl";
	/** xml element declaration */
	protected static final String ELEMENT_SCREENNAME = "screenName";
	/** xml element declaration */
	protected static final String ELEMENT_TAG = "tag";
	/** xml element declaration */
	protected static final String ELEMENT_TAG_LIST = "tagList";
	/** xml element declaration */
	protected static final String ELEMENT_TEXT = "text";
	/** xml element declaration */
	protected static final String ELEMENT_TIMESTAMP = "timestamp";
	/** xml element declaration */
	protected static final String ELEMENT_TRANSLATED_TEXT = "translatedText";
	/** xml element declaration */
	protected static final String ELEMENT_TRIGRAM = "trigram";
	/** xml element declaration */
	protected static final String ELEMENT_TRIGRAM_LIST = "trigramList";
	/** xml element declaration */
	protected static final String ELEMENT_TWEET = "tweet";
	/** xml element declaration */
	protected static final String ELEMENT_TWEET_LIST = "tweetList";
	/** xml element declaration */
	protected static final String ELEMENT_USER = "user";
	/** xml element declaration */
	protected static final String ELEMENT_USER_LIST = "userList";
	/** xml element declaration */
	protected static final String ELEMENT_WEIGHT = "weight";
	/** xml element declaration */
	protected static final String ELEMENT_WORD = "word";
	/** xml element declaration */
	protected static final String ELEMENT_WORD_LIST = "wordList";
	
	/* methods */
	/** service method declaration */
	protected static final String METHOD_GET_TAGS = "getTags";
	
	/* parameters */
	/** service method parameter declaration */
	public static final String PARAMETER_MAX_TAGS = "max_tags";
	/** service method parameter declaration */
	public static final String PARAMETER_MAX_TWEETS = "max_tweets";
	/** service method parameter declaration */
	public static final String PARAMETER_TWITTER_ID = "twitter_id";
	/** service method parameter declaration */
	public static final String PARAMETER_URL = "url";
	
	/* common */
	/** default upper limit for retrieved tags */
	protected static final int DEFAULT_MAX_TAGS = 5;
	/** default upper limit for retrieved tweets */
	protected static final int DEFAULT_MAX_TWEETS = Integer.MAX_VALUE;
	
	/**
	 * 
	 */
	private Definitions(){
		// nothing needed
	}
}
