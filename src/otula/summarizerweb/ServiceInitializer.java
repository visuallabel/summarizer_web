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
package otula.summarizerweb;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import otula.utils.CommonUtils;

/**
 * 
 * Performs tasks required for the initialization of the service context.
 *
 */
public class ServiceInitializer implements ServletContextListener{
	private static final Logger LOGGER = Logger.getLogger(ServiceInitializer.class);
	private static final String PARAMETER_PROPERTY_FILE = "propertyfile";
	private static ServiceInfo _serviceInfo = null;
	private static Scheduler _taskScheluder = null;
	
	@Override
	public void contextDestroyed(ServletContextEvent e) {
		_taskScheluder.shutdownWorkers();
		_taskScheluder = null;
		_serviceInfo = null;
		LOGGER.info("Context destroyed.");
	}

	@Override
	public void contextInitialized(ServletContextEvent e) {
		loadProperties(e.getServletContext());
		_taskScheluder = new Scheduler();
		LOGGER.info("Context created.");
	}
	
	/**
	 * load properties and initialize ServiceInfo
	 * 
	 * @param context
	 * @throws IllegalArgumentException
	 */
	private void loadProperties(ServletContext context) throws IllegalArgumentException{
		_serviceInfo = new ServiceInfo();
		_serviceInfo.setServletContextPath(context.getContextPath());
		
		_serviceInfo._properties = new Properties();
		try {
			_serviceInfo._properties.load(getClass().getClassLoader().getResourceAsStream(context.getInitParameter(PARAMETER_PROPERTY_FILE)));
		} catch (IOException ex) {
			LOGGER.error(ex, ex);
			throw new IllegalArgumentException("Could not load properties.");
		}
	}
	
	/**
	 * 
	 * @return service info
	 */
	public static ServiceInfo getServiceInfo(){
		return _serviceInfo;
	}
	
	/**
	 * 
	 * @return scheduler
	 */
	public static Scheduler getTaskScheduler(){
		return _taskScheluder;
	}

	/**
	 * Contains global property information for SummarizerWeb
	 */
	public class ServiceInfo{
		private static final String PROPERTY_FACEBOOK_SUMMARIZER_CONFIG_FILE_PATH = "core.summarizer.facebook.config_file_path";
		private static final String PROPERTY_TWITTER_SUMMARIZER_CONFIG_FILE_PATH = "core.summarizer.twitter.config_file_path";
		private static final String PROPERTY_SUMMARIZER_DATA_INPUT_PATH = "core.summarizer.data_input_path";
		private static final String PROPERTY_SUMMARIZER_FACEBOOK_KEEP_XML = "core.summarizer.facebook.keep_xml";
		private static final String PROPERTY_SUMMARIZER_TWITTER_KEEP_XML = "core.summarizer.twitter.keep_xml";
		private String _servletContextPath = null;
		private Properties _properties = null;

		/**
		 * 
		 * @return servlet context path
		 */
		public String getServletContextPath() {
			return _servletContextPath;
		}
		
		/**
		 * 
		 * @return file path to facebook configuration file
		 */
		public String getFacebookConfigurationFilePath(){
			return _properties.getProperty(PROPERTY_FACEBOOK_SUMMARIZER_CONFIG_FILE_PATH);
		}
		
		/**
		 * 
		 * @return file path to twitter configuration file
		 */
		public String getTwitterConfigurationFilePath(){
			return _properties.getProperty(PROPERTY_TWITTER_SUMMARIZER_CONFIG_FILE_PATH);
		}
		
		/**
		 * 
		 * @return data input path
		 */
		public String getDataInputPath(){
			return _properties.getProperty(PROPERTY_SUMMARIZER_DATA_INPUT_PATH);
		}
		
		/**
		 * 
		 * @return true if facebook profile xml files should <i>not</i> be removed after analysis
		 */
		public boolean keepFacebookXML(){
			if(CommonUtils.isTrueString(_properties.getProperty(PROPERTY_SUMMARIZER_FACEBOOK_KEEP_XML))){
				return true;
			}else{
				return false;
			}
		}
		
		/**
		 * 
		 * @return true if twitter profile xml files should <i>not</i> be removed after analysis
		 */
		public boolean keepTwitterXML(){
			if(CommonUtils.isTrueString(_properties.getProperty(PROPERTY_SUMMARIZER_TWITTER_KEEP_XML))){
				return true;
			}else{
				return false;
			}
		}
		
		/**
		 * 
		 * @param path context path, the starting / is will be removed, if one is present
		 */
		private void setServletContextPath(String path){
			if(path.startsWith("/")){
				_servletContextPath = path.substring(1);
			}else{
				_servletContextPath = path;
			}
		}
	}	// class ServiceInfo
}
