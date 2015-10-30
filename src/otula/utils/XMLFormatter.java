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

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * 
 * This class can be used to to print (marshal) object to xml strings.
 *
 */
public class XMLFormatter {
	private static final Logger LOGGER = Logger.getLogger(XMLFormatter.class);
	private boolean _omitXMLDeclaration = true;
	
	/**
	 * 
	 * @param o annotated xml object
	 * @return the object as a xml string or null if bad object
	 */
	public <T> String toString(T o){
		String retval = null;
		try {
			Marshaller marshaller = createMarshaller(JAXBContext.newInstance(o.getClass()));
			StringWriter w = new StringWriter();
			marshaller.marshal(o, w);
			retval = w.toString();
		} catch (JAXBException ex) {
			LOGGER.error(ex, ex);
		}
		
		return retval;
	}
	
	/**
	 * 
	 * @param doc
	 * @return the document as string or null if not a valid document
	 */
	public String toString(Document doc){
		String result = null;
		try {
			Transformer tf = TransformerFactory.newInstance().newTransformer();
			if(_omitXMLDeclaration){
				tf.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			}else{
				tf.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			}
			StringWriter w = new StringWriter();
			tf.transform(new DOMSource(doc), new StreamResult(w));
			result = w.toString();
		} catch (TransformerFactoryConfigurationError | TransformerException ex) {
			LOGGER.error(ex, ex);
		}
		return result;
	}
	
	/**
	 * create and return new marshaller, and set the default values
	 * @param context
	 * @return new marshaller for the given context
	 * @throws JAXBException 
	 */
	private Marshaller createMarshaller(JAXBContext context) throws JAXBException{
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		if(_omitXMLDeclaration){
			m.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
		}
		return m;
	}

	/**
	 * 
	 * @return true if this formatter will not print <?xml declaration
	 */
	public boolean isOmitXMLDeclaration() {
		return _omitXMLDeclaration;
	}

	/**
	 * 
	 * @param omitXML if true, this formatter will not print <?xml declaration, by default the declaration will not be printed
	 */
	public void setOmitXMLDeclaration(boolean omitXML) {
		_omitXMLDeclaration = omitXML;
	}
	
	/**
	 * 
	 * @param node
	 * @param cls
	 * @return the given node converted to an object
	 * @throws IllegalArgumentException on bad xml
	 */
	@SuppressWarnings("unchecked")
	public <T> T toObject(Node node, Class<T> cls) throws IllegalArgumentException{
		if(node == null){
			return null;
		}
		T retval = null;
		try{
			JAXBContext context = JAXBContext.newInstance(cls);
			Object o = context.createUnmarshaller().unmarshal(node);
			if(o.getClass() != cls){
				throw new IllegalArgumentException("Contents not of expected type.");
			}else{
				retval = (T) o;
			}
		} catch(JAXBException ex){
			LOGGER.error(ex, ex);
			throw new IllegalArgumentException("Failed to parse xml.");
		}
		return retval;
	}
}
