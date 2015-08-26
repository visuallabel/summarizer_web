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
package otula.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;


/**
 * A thread-safe utility class for processing Strings.
 */
public final class StringUtils {
	private static final ThreadLocal<SimpleDateFormat> ISO_DATE = new ThreadLocal<SimpleDateFormat>(){ // create thread local to overcome multi-thread issues with simple date format
		/* (non-Javadoc)
		 * @see java.lang.ThreadLocal#initialValue()
		 */
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		}
	};
	private static final Logger LOGGER = Logger.getLogger(StringUtils.class);
	private static final String ZULU = "Z";

	/**
	 * 
	 */
	private StringUtils(){
		// nothing needed
	}

	/**
	 * 
	 * @param started
	 * @param finished
	 * @return string representation of the time interval between the given dates
	 */
	public static String getDurationString(Date started, Date finished){		
		long duration_ms = finished.getTime() - started.getTime();
		long ms = ((duration_ms % 86400000) % 3600000) % 1000;
		long s = (((duration_ms % 86400000) % 3600000) % 60000) /1000;
		long min = ((duration_ms % 86400000) % 3600000)/60000;
		long h = (duration_ms % 86400000)/3600000;
		StringBuilder sb = new StringBuilder();
		if(h > 0){
			sb.append(String.valueOf(h));
			sb.append(" h");
		}
		if(min > 0){
			if(sb.length() > 0)
				sb.append(" ");
			sb.append(String.valueOf(min));
			sb.append(" min");
		}
		if(s > 0){
			if(sb.length() > 0)
				sb.append(" ");
			sb.append(String.valueOf(s));
			sb.append(" s");
		}
		if(ms > 0){
			if(sb.length() > 0)
				sb.append(" ");
			sb.append(String.valueOf(ms));
			sb.append(" ms");
		}
		if(sb.length() < 1){
			sb.append(" less than 1 ms");
		}
		return sb.toString();
	}

	/**
	 * This method is synchronized for the conversion
	 * 
	 * @param date
	 * @return null if null passed, otherwise the passed string in format yyyy-MM-dd'T'HH:mm:ssZ
	 */
	public static String dateToISOString(Date date){
		return (date == null ? null : ISO_DATE.get().format(date));
	}
	
	/**
	 * Note: this method will accept microseconds, but the actual microsecond values will be ignored,
	 * passing 2012-05-23T10:32:20.000000Z equals to passing 2012-05-23T10:32:20Z. 
	 * 
	 * Both Z and +XXXX are accepted timezone formats.
	 * 
	 * @param date the passed string in format yyyy-MM-dd'T'HH:mm:ssZ
	 * @return the passed string parsed as ISODate or null if the string contains invalid date string
	 */
	public static Date ISOStringToDate(String date){
		try {
			int pointIndex = date.indexOf('.');
			if(pointIndex > 0){ // strip milliseconds 2012-05-23T10:32:20.XXXXXXZ
				if(date.endsWith(ZULU)){ // strip the tailing Z as it creates issues with simple date format
					date = date.substring(0, pointIndex)+"+0000";
				}else{
					int plusIndex = date.indexOf('+', pointIndex);
					if(plusIndex < 0){
						LOGGER.error("Invalid date string.");
						return null;
					}
					date = date.substring(0, pointIndex) + date.substring(plusIndex);
				}
			}else if(date.endsWith(ZULU)){ // strip the tailing Z as it creates issues with simple date format
				date = date.substring(0, date.length()-1)+"+0000";
			}
			return ISO_DATE.get().parse(date);	
		} catch (ParseException ex) {//+0300
			LOGGER.error(ex, ex);
			return null;
		}
	}
	
	/**
	 * @see javax.xml.bind.DatatypeConverter#parseDateTime(String)
	 * 
	 * @param date
	 * @return the passed string parsed to date or null if invalid date was passed
	 */
	public static Date stringToDate(String date){
		if(org.apache.commons.lang3.StringUtils.isBlank(date)){
			return null;
		}
		return javax.xml.bind.DatatypeConverter.parseDateTime(date).getTime();
	}
}
