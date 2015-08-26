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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * Resolve and redirect to real photo url if possible.
 * 
 */
public class TwitterRedirectServlet extends HttpServlet {
	/** default serial uid */
	private static final long serialVersionUID = 6953211079082341252L;
	private static final String HEADER_LOCATION = "Location";
	private static final Logger LOGGER = Logger.getLogger(TwitterRedirectServlet.class);
	private static final String PREFIX_HTTPS = "https://";
	private static final String TAG_A_MEDIA = "<a class=\"media";

	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
		String urlParameter = req.getParameter(Definitions.PARAMETER_URL);
		if(StringUtils.isBlank(urlParameter)){
			LOGGER.warn("Missing parameter "+Definitions.PARAMETER_URL);
			resp.setStatus(400);
			return;
		}
		try{
			URL url = new URL(urlParameter);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			int status = connection.getResponseCode();
			if(status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM || status == HttpURLConnection.HTTP_SEE_OTHER){
				url = new URL(connection.getHeaderField(HEADER_LOCATION));
				connection.disconnect();
				connection = (HttpURLConnection) url.openConnection();
			}
			try (BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()))){ 
				String page = IOUtils.toString(input);
				int index = page.indexOf(TAG_A_MEDIA); // find the beginning of <a class="media tag
				if(index < 1){
					LOGGER.warn("Could not find correct a tag.");
					resp.setStatus(404);
					return;
				}
				index = page.indexOf(PREFIX_HTTPS, index); // beginning of attribute value containing data-url
				resp.sendRedirect(page.substring(index, page.indexOf('"', index))); // use the end of attribute value containing data-url as limiter
				return;
			} finally {
				connection.disconnect();
			}
		}catch(IOException ex){
			LOGGER.warn(ex, ex);
		}

		resp.setStatus(400);
	}

}
