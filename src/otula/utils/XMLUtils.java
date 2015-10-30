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
package otula.utils;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * 
 * Utility class for xml parsing
 *
 */
public final class XMLUtils {
	private static final Logger LOGGER = Logger.getLogger(XMLUtils.class);
	
	/**
	 * 
	 */
	private XMLUtils() {
		// nothing needed
	}
	
	/**
	 * 
	 * @param in the inputstream, this is closed automatically
	 * @return the given inputstream processed into a document
	 * @throws IllegalArgumentException
	 */
	public static Document toXMLDocument(InputStream in) throws IllegalArgumentException{
		if(in == null){
			throw new IllegalArgumentException("Bad XML data.");
		}
		try {
			return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
		} catch (SAXException | IOException | ParserConfigurationException ex) {
			LOGGER.error(ex, ex);
			throw new IllegalArgumentException("Bad XML data.");
		} finally {
			try {
				in.close();
			} catch (IOException ex) {
				LOGGER.error(ex, ex);
			}
		}
	}
}
