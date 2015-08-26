"use strict";
/**
 * debug javascript functions for summarizer
 */
var summarizerDebug = {
	URI_GET_TAGS : "/SummarizerWeb/getTags?max_tweets=100&max_tags=5&screen_name=",
	URL_TWITTER_REDIRECT : "/SummarizerWeb/twitterRedirect?url=",
	
	/**
	 * @param {callback} callback the callback function, called with a list of tweets for the given user or  null if no tweets were found
	 * @param {string} screenName
	 */
	getTweets : function(callback, screenName){
		var uri = summarizerDebug.URI_GET_TAGS + screenName;
		console.log("Calling GET "+uri);
		$.ajax({
			url : uri,
			success : function(data) {
				callback(data.getElementsByTagName("tweet"));
			},
			error : function(jqXHR, textStatus, errorThrown) {
				console.log('getTweets failed: '+jqXHR.status+' '+textStatus+' '+errorThrown);
			}
		});
	},
	
	/**
	 * @param {DomNode} node
     * @param requireWeight if true the node will only be generated if valid weight value is present
	 * @return html string for li element
	 */
	createLi : function(node, requireWeight){
        var weight = $(node).children("weight").text().trim();
        if(requireWeight && (weight.length < 1 || Number(weight) <= 0)){
            console.log("Ignoring node with invalid weight.");
            return "";
        }
    
		var content = '<li>';
		var text = $(node).children("text").text();
		var translatedText = $(node).children("translatedText").text();
		if(text.trim().length > 0){
			content += text;
			if(translatedText.trim().length > 0){
				content += ' / ';
			}
		}			
		content += translatedText+' '+weight+' '+$(node).children("frequency").text()+'</li>';
		return content;
	},
	
	/**
	 * @param {NodeList} tweets list of tweets
	 */
	populateTweets : function(tweets){
		var tweetsDiv = $("#tweets-div");
		tweetsDiv.empty();
		for(var i=0;i<tweets.length;++i){
			var tweet = tweets.item(i);
			var content = '<div class="tweet-container">';
			content += '<div class="timestamp">'+$(tweet).children("timestamp").text()+'</div>';
			content += '<div class="description">'+$(tweet).children("text").text()+'<br>'+$(tweet).children("translatedText").text()+'</div>';
			
			var nodes = tweet.getElementsByTagName("word");
			if(nodes.length > 0){
				content += '<div class="word-list"><h1>Top 5 words</h1><ul class="taglist">';
				for(var j=0;j<nodes.length;++j){
					content += summarizerDebug.createLi(nodes.item(j), true);
				}
				content += '</ul></div>';
			}
			
			nodes = tweet.getElementsByTagName("bigram");
			if(nodes.length > 0){
				content += '<div class="bigram-list"><h1>Top 5 bigrams</h1><ul class="taglist">';
				for(var j=0;j<nodes.length;++j){
					content += summarizerDebug.createLi(nodes.item(j), true);
				}
				content += '</ul></div>';
			}
			
			nodes = tweet.getElementsByTagName("trigram");
			if(nodes.length > 0){
				content += '<div class="trigram-list"><h1>Top 5 trigrams</h1><ul class="taglist">';
				for(var j=0;j<nodes.length;++j){
					content += summarizerDebug.createLi(nodes.item(j), true);
				}
				content += '</ul></div>';
			}

			nodes = tweet.getElementsByTagName("tag");
			if(nodes.length > 0){
				content += '<div class="named-entity-list"><h1>Named Entity Tags</h1><ul class="taglist">';
				for(var j=0;j<nodes.length;++j){
					content += summarizerDebug.createLi(nodes.item(j), true);
				}
				content += '</ul></div>';
			}
			
			nodes = tweet.getElementsByTagName("hashtag");
			if(nodes.length > 0){
				content += '<div class="hashtag-list"><h1>Hashtags</h1><ul class="taglist">';
				for(var j=0;j<nodes.length;++j){
					content += summarizerDebug.createLi(nodes.item(j));
				}
				content += '</ul></div>';
			}
			
			content += '</div>';
			tweetsDiv.append(content);
			
			var appended = tweetsDiv.children().last();
			
			var photo = new Image();
			photo.photoDiv = appended;
			$(photo).error(function(){
				console.log("Failed to load image.");
				this.photoDiv.css("display","none");
			});
			photo.src = summarizerDebug.URL_TWITTER_REDIRECT+tweet.getElementsByTagName("photoUrl")[0].textContent;
			appended.prepend(photo);
		}
	},
	
	/**
	 * 
	 */
	getTagsButton : function(){
		var screenName = document.getElementById("screenname-input").value;
		if(screenName.trim().length < 1){
			console.log("No screen name.");
			return;
		}
		summarizerDebug.getTweets(summarizerDebug.populateTweets, screenName);
	}
};
