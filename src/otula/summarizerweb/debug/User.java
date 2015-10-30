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

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Twitter User element
 */
@XmlRootElement(name=Definitions.ELEMENT_USER)
@XmlAccessorType(XmlAccessType.NONE)
public class User {
	@XmlElement(name=Definitions.ELEMENT_SCREENNAME)
	private String _screenName = null;
	@XmlElement(name=Definitions.ELEMENT_TWEET)
	@XmlElementWrapper(name=Definitions.ELEMENT_TWEET_LIST)
	private List<Tweet> _tweets = null;
	
	/**
	 * @return the screenName
	 */
	public String getScreenName() {
		return _screenName;
	}
	
	/**
	 * @param screenName the screenName to set
	 */
	public void setScreenName(String screenName) {
		_screenName = screenName;
	}
	
	/**
	 * @return the tweets
	 */
	public List<Tweet> getTweets() {
		return _tweets;
	}
	
	/**
	 * @param tweets the tweets to set
	 */
	public void setTweets(List<Tweet> tweets) {
		_tweets = tweets;
	}
}
