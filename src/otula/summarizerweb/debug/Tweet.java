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

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import otula.utils.ISODateAdapter;

/**
 * a single tweet
 */
@XmlRootElement(name=Definitions.ELEMENT_TWEET)
@XmlAccessorType(XmlAccessType.NONE)
public class Tweet {
	@XmlElement(name=Definitions.ELEMENT_BACKEND_TAG)
	@XmlElementWrapper(name=Definitions.ELEMENT_BACKEND_TAG_LIST)
	private List<Tag> _backendTags = null;
	@XmlElement(name=Definitions.ELEMENT_BIGRAM)
	@XmlElementWrapper(name=Definitions.ELEMENT_BIGRAM_LIST)
	private List<Tag> _bigrams = null;
	@XmlElement(name=Definitions.ELEMENT_HASHTAG)
	@XmlElementWrapper(name=Definitions.ELEMENT_HASHTAG_LIST)
	private List<Tag> _hashTags = null;
	@XmlElement(name = Definitions.ELEMENT_PHOTO_URL)
	private String _photoUrl = null;
	@XmlElement(name=Definitions.ELEMENT_TAG)
	@XmlElementWrapper(name=Definitions.ELEMENT_TAG_LIST)
	private List<Tag> _tags = null;
	@XmlElement(name = Definitions.ELEMENT_TEXT)
	private String _text = null;
	@XmlJavaTypeAdapter(ISODateAdapter.class)
	@XmlElement(name = Definitions.ELEMENT_TIMESTAMP)
	private Date _timestamp = null;	
	@XmlElement(name = Definitions.ELEMENT_TRANSLATED_TEXT)
	private String _translatedText = null;
	@XmlElement(name=Definitions.ELEMENT_TRIGRAM)
	@XmlElementWrapper(name=Definitions.ELEMENT_TRIGRAM_LIST)
	private List<Tag> _trigrams = null;
	@XmlElement(name=Definitions.ELEMENT_WORD)
	@XmlElementWrapper(name=Definitions.ELEMENT_WORD_LIST)
	private List<Tag> _words = null;
	
	/**
	 * @return the backendTags
	 */
	public List<Tag> getBackendTags() {
		return _backendTags;
	}

	/**
	 * @param backendTags the backendTags to set
	 */
	public void setBackendTags(List<Tag> backendTags) {
		_backendTags = backendTags;
	}

	/**
	 * @return the bigrams
	 */
	public List<Tag> getBigrams() {
		return _bigrams;
	}
	
	/**
	 * @param bigrams the bigrams to set
	 */
	public void setBigrams(List<Tag> bigrams) {
		_bigrams = bigrams;
	}
	
	/**
	 * @return the hashTags
	 */
	public List<Tag> getHashTags() {
		return _hashTags;
	}
	
	/**
	 * @param hashTags the hashTags to set
	 */
	public void setHashTags(List<Tag> hashTags) {
		_hashTags = hashTags;
	}
	
	/**
	 * @return the photoUrl
	 */
	public String getPhotoUrl() {
		return _photoUrl;
	}
	
	/**
	 * @param photoUrl the photoUrl to set
	 */
	public void setPhotoUrl(String photoUrl) {
		_photoUrl = photoUrl;
	}
	
	/**
	 * @return the tags
	 */
	public List<Tag> getTags() {
		return _tags;
	}
	
	/**
	 * @param tags the tags to set
	 */
	public void setTags(List<Tag> tags) {
		_tags = tags;
	}
	
	/**
	 * @return the text
	 */
	public String getText() {
		return _text;
	}
	
	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		_text = text;
	}
	
	/**
	 * @return the timestamp
	 */
	public Date getTimestamp() {
		return _timestamp;
	}
	
	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(Date timestamp) {
		_timestamp = timestamp;
	}
	
	/**
	 * @return the translatedText
	 */
	public String getTranslatedText() {
		return _translatedText;
	}
	
	/**
	 * @param translatedText the translatedText to set
	 */
	public void setTranslatedText(String translatedText) {
		_translatedText = translatedText;
	}
	
	/**
	 * @return the trigrams
	 */
	public List<Tag> getTrigrams() {
		return _trigrams;
	}
	
	/**
	 * @param trigrams the trigrams to set
	 */
	public void setTrigrams(List<Tag> trigrams) {
		_trigrams = trigrams;
	}
	
	/**
	 * @return the words
	 */
	public List<Tag> getWords() {
		return _words;
	}
	
	/**
	 * @param words the words to set
	 */
	public void setWords(List<Tag> words) {
		_words = words;
	}
}
