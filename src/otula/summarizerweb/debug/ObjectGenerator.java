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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * A test class for generating random objects
 */
@Deprecated
public class ObjectGenerator {
	private static final String PREFIX_EN = "EN_";
	private static final String PREFIX_FI = "FI_";
	private static final String RANDOM_URL = "http://otula.pori.tut.fi/d2i/leijona_album_art.jpg";
	private static final int TAG_LENGTH = 10;
	private static final int TEXT_LENGTH = 180;
	private Random _random = new Random();
	
	/**
	 * 
	 * @param maxTags
	 * @param maxTweets
	 * @param screenNames
	 * @return randomly generated list of users
	 */
	public List<User> generateRandomUsers(int maxTags, int maxTweets, String[] screenNames){
		List<User> users = new ArrayList<>(screenNames.length);
		for(String screenName : screenNames){
			User user = new User();
			user.setScreenName(screenName);
			List<Tweet> tweets = new ArrayList<>(maxTweets);
			for(int i=0;i<maxTweets;++i){
				tweets.add(generateRandomTweet(maxTags));
			}
			user.setTweets(tweets);
			users.add(user);
		}
		return users;
	}
	
	/**
	 * 
	 * @param maxTags
	 * @return randomly generated list of tags
	 */
	public List<Tag> generateRandomTags(int maxTags){
		if(maxTags < 1){
			return null;
		}
		List<Tag> tags = new ArrayList<>(maxTags);
		double previousWeight = _random.nextDouble();
		for(int i=0;i<maxTags;++i){
			Tag tag = new Tag();
			tag.setText(PREFIX_FI+RandomStringUtils.randomAlphabetic(TAG_LENGTH));
			tag.setTranslatedText(PREFIX_EN+RandomStringUtils.randomAlphabetic(TAG_LENGTH));
			previousWeight += _random.nextDouble();
			tag.setWeight(previousWeight);
			tags.add(tag);
		}
		Collections.reverse(tags); // reverse order so that the tag with the highest weight is the first one
		return tags;
	}
	
	/**
	 * 
	 * @param maxTags
	 * @return randomly generated list of tweets
	 */
	public Tweet generateRandomTweet(int maxTags){
		Tweet tweet = new Tweet();
		tweet.setText(PREFIX_FI+RandomStringUtils.randomAlphabetic(TEXT_LENGTH));
		tweet.setTranslatedText(PREFIX_EN+RandomStringUtils.randomAlphabetic(TEXT_LENGTH));
		tweet.setBigrams(generateRandomTags(maxTags));
		tweet.setHashTags(generateRandomTags(maxTags));
		tweet.setPhotoUrl(RANDOM_URL);
		tweet.setTags(generateRandomTags(maxTags));
		tweet.setTrigrams(generateRandomTags(maxTags));
		tweet.setWords(generateRandomTags(maxTags));
		tweet.setTimestamp(new Date(Math.abs(_random.nextLong())));
		return tweet;
	}
}
