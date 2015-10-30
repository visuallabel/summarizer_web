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
package otula.summarizerweb.debug;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.Logger;

import otula.summarizerweb.Response;
import otula.summarizerweb.ServiceInitializer;
import analyzer.content.socialmedia.HashTag;
import analyzer.database.DataFactory;
import analyzer.database.TwitterPicture;
import analyzer.database.TwitterProfile;
import analyzer.ner.NamedEntity;
import analyzer.segmentation.Centroid;

/**
 * debug methods for summarizer web
 */
public final class DebugCore {
	private static final String BACKEND_ID_PICSOM = "2";
	private static final Logger LOGGER = Logger.getLogger(DebugCore.class);
	
	/**
	 * 
	 */
	private DebugCore(){
		// nothing needed
	}
	
	/**
	 * 
	 * @param maxTags
	 * @param maxTweets
	 * @param twitterIds
	 * @return response
	 */
	public static Response getTags(int maxTags, int maxTweets, String[] twitterIds) {
		DebugResponse s = new DebugResponse();
		s.setMethod(Definitions.METHOD_GET_TAGS);
		s.setService(ServiceInitializer.getServiceInfo().getServletContextPath());
		
		if(ArrayUtils.isEmpty(twitterIds)){
			s.setStat(Definitions.PARAMETER_TWITTER_ID+" has no valid values.");
			return s;
		}
		
		if(maxTweets == 0){
			s.setStat(Definitions.PARAMETER_MAX_TWEETS+" == 0.");
			return s;
		}else if(maxTweets < 0){
			LOGGER.debug(Definitions.PARAMETER_MAX_TWEETS+" < 0, using default: "+Definitions.DEFAULT_MAX_TWEETS);
			maxTweets = Definitions.DEFAULT_MAX_TWEETS;
		}
		
		if(maxTags < 0){
			LOGGER.debug(Definitions.PARAMETER_MAX_TAGS+" < 0, using default: "+Definitions.DEFAULT_MAX_TAGS);
			maxTags = Definitions.DEFAULT_MAX_TAGS;
		}
		
		List<User> users = new ArrayList<>(twitterIds.length);
		for(int i=0;i<twitterIds.length;++i){
			String screenName = twitterIds[i];
			TwitterProfile tp = DataFactory.getProfile(screenName);
			if(tp == null){
				LOGGER.debug("User not found, screen name: "+screenName);
				continue;
			}
			User user = new User();
			user.setScreenName(screenName);
			if(tp.pictures == null || tp.pictures.isEmpty()){
				LOGGER.debug("No pictures for user, screen name: "+screenName);
			}else{
				List<Tweet> tweets = new ArrayList<>(tp.pictures.size());
				for(TwitterPicture p : tp.pictures){
					Tweet t = new Tweet();
					t.setBackendTags(convertBackendTags(p.picsomTags, BACKEND_ID_PICSOM));
					t.setPhotoUrl(p.pictureurl);
					t.setTimestamp(p.timestamp);
					t.setWords(convertCentroids(p.unigrams, maxTags));
					t.setBigrams(convertCentroids(p.twograms, maxTags));
					t.setTrigrams(convertCentroids(p.trigrams, maxTags));
					t.setTags(convertEntities(p.ne, maxTags));
					t.setText(p.tweetText);
					t.setHashTags(convertHashTags(p.hashtags.values()));
					tweets.add(t);
					if(--maxTweets == 0){ // get only the max tweets
						break;
					}
				}
				if(tweets.isEmpty()){
					LOGGER.debug("No tweets for user, screen name: "+screenName);
				}else{	
					user.setTweets(tweets);
				}
			} // else
			users.add(user);
		} // for
		if(users.isEmpty()){
			LOGGER.debug("No users found.");
		}else{
			s.setUsers(users);
		}
		
		s.setStat(otula.summarizerweb.Definitions.STATUS_OK);
		return s;
	}
	
	/**
	 * 
	 * @param tags
	 * @param backendId
	 * @return the converted tags or null if null or empty list was passed
	 */
	private static List<Tag> convertBackendTags(Collection<String> tags, String backendId) {
		if(tags == null || tags.isEmpty()){
			LOGGER.debug("No back-end tags for back-end id: "+backendId);
			return null;
		}
		
		List<Tag> tagList = new ArrayList<>(tags.size());
		for(String tag : tags){
			Tag t = new Tag();
			t.setText(tag);
			tagList.add(t);
			t.setBackendId(backendId);
		}
		return tagList;
	}

	/**
	 * 
	 * @param hashTags
	 * @return the hashtags as tags or null if null or empty list was passed
	 */
	private static List<Tag> convertHashTags(Collection<HashTag> hashTags){
		if(hashTags == null || hashTags.isEmpty()){
			LOGGER.warn("No hash tags.");
			return null;
		}
		List<Tag> tags = new ArrayList<>(hashTags.size());
		for(HashTag ht : hashTags){
			Tag t = new Tag();
			t.setText(ht.tag);
			if(ht.tfidf > 0){
				t.setWeight(ht.tfidf);
			}
		}
		return (tags.isEmpty() ? null : tags);
	}
	
	/**
	 * 
	 * @param entities
	 * @param maxEntities 
	 * @return passed entity list as tags or null if null or empty list was passed
	 */
	private static List<Tag> convertEntities(List<NamedEntity> entities, int maxEntities){
		if(entities == null || entities.isEmpty()){
			LOGGER.debug("No entities.");
			return null;
		}
		List<Tag> tags = new ArrayList<>(entities.size());
		for(NamedEntity e : entities){
			Tag tag = new Tag();
			tag.setText(e.namedEntityOriginalLanguage);
			tag.setTranslatedText(e.namedEntity);
			if(e.frequency > 0){
				tag.setFrequency(e.frequency);
			}
			if(e.tfidf > 0){
				tag.setWeight(e.tfidf);
			}
			tags.add(tag);
			if(--maxEntities == 0){
				break;
			}
		}
		return tags;
	}
	
	/**
	 * 
	 * @param centroids
	 * @param maxTags 
	 * @return the passed centroid list as tags or null if null or empty list was passed
	 */
	private static List<Tag> convertCentroids(List<Centroid> centroids, int maxTags){
		if(centroids == null || centroids.isEmpty()){
			LOGGER.debug("No centroids.");
			return null;
		}
		List<Tag> tags = new ArrayList<>(centroids.size());
		for(Centroid c : centroids){
			Tag tag = new Tag();
			tag.setText(c.getOriginalTag());
			tag.setTranslatedText(c.getTag());
			double weight = c.getTfidf();
			if(weight > 0){
				tag.setWeight(weight);
			}
			tags.add(tag);
			if(--maxTags == 0){
				break;
			}
		}
		return tags;
	}
}
