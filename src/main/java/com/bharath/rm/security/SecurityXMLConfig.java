package com.bharath.rm.security;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.method.P;

import com.bharath.rm.common.Utils;
import com.bharath.rm.constants.SecurityXMLUtilConstants;
import com.bharath.rm.model.Parameter;
import com.bharath.rm.model.Regex;
import com.bharath.rm.model.URL;

import com.bharath.rm.model.Parameter.ParameterBuilder;
import com.bharath.rm.model.URL.URIBuilder;
import com.sun.istack.FinalArrayList;

/**
 * @author bharath
 * @version 1.0
 * Creation time: Jun 15, 2020 10:59:14 PM
 * 
 * The class SecurityXMLUtilHandler will parse the regex's, templates and url's that are present in the /WEB-INF/security.xml. 
 * The security.xml defines an url and its properties such as method, authentication, parameters. All the url's will be parsed 
 * and stored in the HashMap. When a new request come, the corresponding properties will be returned from the HashMap. This 
 * class will be utilized only during Tomcat startup.
 */

public final class SecurityXMLConfig {
	
	/** The logger will be used to log information such as exceptions, info or debug. **/
	private static final Logger log = LoggerFactory.getLogger(SecurityXMLConfig.class);

	/** The security.xml is located in the /src/main/resources. */
	private static final String SECURITYXMLPATH = "security.xml";
	
	/** A map that stores regex name and it's pattern. It will be cleared when all the url's are processed.  */
	private final HashMap<String, String> regexMaps=new HashMap<>();
	
	/** A map that stores template name and it's parameters in List. It will be cleared when all the url's are processed.  */
	private final HashMap<String, List<ParameterBuilder>> templateBuilderMaps=new HashMap<>();
	
	/** A map that stores template name and it's parameters in HashMap. It will be cleared when all the url's are processed.  */
	private final HashMap<String,HashMap<String, Parameter>> templateMaps=new HashMap<>();
	
	public static final URLNode URLNODES=new URLNode();
	
	/** XMLInputFactory instance. */
	private final XMLInputFactory factory = XMLInputFactory.newInstance();
	
	/**
	 * This method is the entry point and it is invoked from the Listener class.
	 *
	 * @throws Exception the exception
	 */
	public static void init() {
		SecurityXMLConfig config=new SecurityXMLConfig();
		config.parseSecurityXML();
	}
	
	/**
	 * Parses the security XML.
	 */
	private void parseSecurityXML() {
		log.info("Parsing security.xml");
		final ClassLoader classLoader = Utils.classloader();
		 try(InputStream inputStream = classLoader.getResourceAsStream(SECURITYXMLPATH);) {
			 final XMLEventReader reader = factory.createXMLEventReader(inputStream);
			 while (reader.hasNext()) {
                final XMLEvent event = reader.nextEvent();
                switch(event.getEventType()) {
                	case XMLStreamConstants.START_ELEMENT:
                		final StartElement startElement = event.asStartElement();
                       final String startName = startElement.getName().getLocalPart();
                       if(SecurityXMLUtilConstants.REGEXES.equals(startName)) {
                       	parseRegexes(reader);
                       }else if(SecurityXMLUtilConstants.TEMPLATES.equals(startName)) {
                       	parseTemplates(reader);
                       }else if(SecurityXMLUtilConstants.URLS.equals(startName)) {
                       	parseURLS(reader);
                       }
                       break;
                }
			 }
			 log.info("Finished parsing security.xml");
		 } catch (Exception e) {
			 log.error("Problem while parsing security.xml", e);
		}
	}
	
	/**
	 * This method parses the regexes and adds it to the HashMap 
	 * For eg., <regex name="email" pattern="^[a-zA-Z0-9._%+-]+@[A-Z0-9.-]+\\.[a-zA-Z]{2,6}$">
	 * @param reader the reader is an iterator that is currently parsing the security.xml
	 * @throws Exception the exception
	 */
	private void parseRegexes(final XMLEventReader reader) throws Exception {
		log.info("Started parsing regexes");
		while(reader.hasNext()) {
			final XMLEvent event = reader.nextEvent();
			switch(event.getEventType()) {
				case XMLStreamConstants.START_ELEMENT:
					final StartElement startElement = event.asStartElement();
                    final String startName = startElement.getName().getLocalPart();
                    if(SecurityXMLUtilConstants.REGEX.equals(startName)) {
                    	final Regex regex=parseRegex(startElement);
                    	log.info("Added regex - "+regex.getRegexName());
                    	regexMaps.put(regex.getRegexName(), regex.getRegexValue());
                    }
				break;
				case XMLStreamConstants.END_ELEMENT:
					final EndElement endElement = event.asEndElement();
					final String endName = endElement.getName().getLocalPart();
					if(SecurityXMLUtilConstants.REGEXES.equals(endName)) {
						log.info("Finished parsing regexes");
						return;
					}
				break;
			}
		}
	}
	
	/**
	 * This method parses the templates and adds it to the HashMap 
	 * <templates>
	 *  	<template name="test">...<parameters/>...</template>
	 *  	<template name="test_1">...<parameters/>...</template>
	 *  </templates>
	 * @param reader the reader is an iterator that is currently parsing the security.xml
	 * @throws Exception the exception
	 */
	private void parseTemplates(final XMLEventReader reader) throws Exception  {
		log.info("Started parsing templates");
		while(reader.hasNext()) {
			final XMLEvent event = reader.nextEvent();
			switch(event.getEventType()) {
				case XMLStreamConstants.START_ELEMENT:
					final StartElement startElement = event.asStartElement();
                    final String startName = startElement.getName().getLocalPart();
					if(SecurityXMLUtilConstants.TEMPLATE.equals(startName)) {
						String templateName=null;
						final Iterator<Attribute> attributes = startElement.getAttributes();
						if(attributes.hasNext()) {
							final Attribute attribute = attributes.next();
							if(SecurityXMLUtilConstants.NAME.equals(attribute.getName().toString())){
								templateName=attribute.getValue();
							}
						}
						String error = null;
						if(!Utils.isValidString(templateName)) {
					        error="No template name associated";
					    }
						if(error!=null) {
				        	throw new NullPointerException(error);
				        }
						parseTemplate(reader,templateName);
					}
				break;
				case XMLStreamConstants.END_ELEMENT:
					EndElement endElement = event.asEndElement();
					String endName = endElement.getName().getLocalPart();
					if(SecurityXMLUtilConstants.TEMPLATES.equals(endName)) {
					 	log.info("Finished parsing templates");
						return;
					}
				break;
			}
		}
	}
	
	/**
	 * This method parses the templates and stores that in a temporary HashMap
	 * For eg.,
	 * <template name="test">
	 * 		<parameters>
	 * 			<parameter name="test_1" regex="cleartext" max-length="128" required="true"/>
	 * 			<parameter name="test_2" regex="email" max-length="64" required="true"/>
	 * 			<parameter name="test" type="object" template="test" requried="true"/>
	 * 		</parameters>
	 * 	</template>
	 * @param reader the reader is an iterator that is currently parsing the security.xml
	 * @param templateName the template name
	 * @throws Exception the exception
	 */
	private void parseTemplate(final XMLEventReader reader, final String templateName) throws Exception {
		log.info("Parsing the template - "+templateName);
		final List<Parameter.ParameterBuilder> list=new ArrayList<>();
		while(reader.hasNext()) {
			final XMLEvent event = reader.nextEvent();
			switch(event.getEventType()) {
				case XMLStreamConstants.START_ELEMENT:
					final StartElement startElement = event.asStartElement();
                    final String startName = startElement.getName().getLocalPart();
                    if(SecurityXMLUtilConstants.PARAMETER.equals(startName)) {
                    	list.add(parseParameter(startElement));
                    }
				break;
				case XMLStreamConstants.END_ELEMENT:
					final EndElement endElement = event.asEndElement();
					final String endName = endElement.getName().getLocalPart();
					if(SecurityXMLUtilConstants.TEMPLATE.equals(endName)) {
						templateBuilderMaps.put(templateName, list);
						return;
					}
				break;
			}
		}
	}
	
	/**
	 * This method parses the parameter and returns the builder.
	 * For eg.,
	 * <parameter name="test_1" regex="cleartext" max-length="128" required="true"/>
	 *
	 * @param startElement the parameter element
	 * @return the parameter builder
	 * @throws Exception the exception
	 */
	private ParameterBuilder parseParameter(final StartElement startElement) throws Exception{
		return parseParameter(startElement,false);
	}
	
	/**
	 * This method parses the parameter and returns the builder. 
	 * For eg.,
	 * <parameter name="test_1" regex="cleartext" max-length="128" required="true"/>
	 * or
	 * <parameter name="test_2" type="object" template="test" required="true"/>
	 * @param startElement the parameter element
	 * @param getInnerTemplates if it is set to true and if the parameter is of object type, then the object template will be retrieved.
	 * @return the parameter builder
	 * @throws Exception the exception
	 */
	private ParameterBuilder parseParameter(final StartElement startElement, final boolean getInnerTemplates) throws Exception{
		final ParameterBuilder builder=new ParameterBuilder();
		final Iterator<Attribute> attributes = startElement.getAttributes();
		while(attributes.hasNext()){
			 final Attribute attribute = attributes.next();
			 final String name=attribute.getName().toString();
			 final String value=attribute.getValue();
			 if(!Utils.isValidString(value)) {
				 throw new NullPointerException("Invalid value associated for "+name+".");
			 }
			 if(SecurityXMLUtilConstants.NAME.equals(name)){
				 builder.setParameterName(value);
			 }else if(SecurityXMLUtilConstants.REGEX.equals(name)) {
				 if(regexMaps.containsKey(value)) {
					 builder.setRegex(regexMaps.get(value));
				 }else {
					 builder.setRegex(value);
				 }
			 }else if(SecurityXMLUtilConstants.MAXLENGTH.equals(name)) {
				 builder.setMaxLength(Integer.parseInt(value));
			 }else if(SecurityXMLUtilConstants.MINLENGTH.equals(name)) {
				 builder.setMinLength(Integer.parseInt(value));
			 }else if(SecurityXMLUtilConstants.REQUIRED.equals(name)) {
				 builder.setRequired(Boolean.parseBoolean(value));
			 }else if(SecurityXMLUtilConstants.TYPE.equals(name)) {
				 builder.setType(value);
			 }else if(SecurityXMLUtilConstants.TEMPLATE.equals(name)) {
				 builder.setTemplate(value);
				 if(getInnerTemplates) {
					 builder.setParameters(getTemplateforParameter(value,SecurityXMLUtilConstants.MIN_DEPTH_LEVEL));
				 }
			 }
		}
		return builder;
	}
	
	/**
	 * Parses all the urls present in the security.xml
	 * <urls>......<url>.....</urls>
	 * @param reader the reader is an iterator that is currently parsing the security.xml
	 * @throws Exception the exception
	 */
	private void parseURLS(final XMLEventReader reader) throws Exception {
		log.info("Started parsing urls");
		while(reader.hasNext()) {
			final XMLEvent event = reader.nextEvent();
			switch(event.getEventType()) {
				case XMLStreamConstants.START_ELEMENT:
					final StartElement startElement = event.asStartElement();
					final String startName = startElement.getName().getLocalPart();
					if(SecurityXMLUtilConstants.URL.equals(startName)) {
						parseURL(reader,startElement);
					}
				break;
				case XMLStreamConstants.END_ELEMENT:
					final EndElement endElement = event.asEndElement();
					final String endName = endElement.getName().getLocalPart();
					if(SecurityXMLUtilConstants.URLS.equals(endName)) {
						log.info("Finished parsing urls");
						return;
					}
				break;
			}
		}
	}
	/**
	 * Parses the url.
	 * For eg.,
	 * 	<url value="/admin/test" method="post" authentication="true">
	 * 		<parameters>
	 * 			<parameter name="orgname" regex="cleartext" max-length="128" required="true"/>
	 * 			<parameter name="email" regex="email" max-length="64" required="true"/>
	 * 			<parameter name="password" regex="password" min-length="8" max-length="64" required="true"/>
	 * 			<parameter name="test" type="object" template="test" required="true"/>
	 * 		</parameters>
	 * 	</url>
	 *
	 * @param reader the reader is an iterator that is currently parsing the security.xml
	 * @param startElement the url element
	 * @throws Exception the exception
	 */
	private void parseURL(final XMLEventReader reader,final StartElement startElement) throws Exception {
		URIBuilder builder=new URIBuilder();
		Iterator<Attribute> attributes = startElement.getAttributes();
		while(attributes.hasNext()){
			 Attribute attribute = attributes.next();
			 String name=attribute.getName().toString();
			 String value=attribute.getValue();
			 if(!Utils.isValidString(value)) {
				 throw new NullPointerException("Invalid value associated for "+name+".");
			 }
			 if(SecurityXMLUtilConstants.VALUE.equals(name)){
				 log.info("Parsing the url "+value);
				 builder.setUrl(value);
			 }else if(SecurityXMLUtilConstants.AUTHENTICATION.equals(name)){
				 builder.setAuthentication(Boolean.parseBoolean(value));
			 }else if(SecurityXMLUtilConstants.METHOD.equals(name)) {
				 builder.setMethod(value);
			 }
		}
		HashMap<String, Parameter> map=new HashMap<>();
		while(reader.hasNext()) {
			final XMLEvent event = reader.nextEvent();
			switch(event.getEventType()) {
				case XMLStreamConstants.START_ELEMENT:
					StartElement element = event.asStartElement();
                    String qName = element.getName().getLocalPart();
                    if(SecurityXMLUtilConstants.PARAMETER.equals(qName)) {
                    	ParameterBuilder paramBuilder=parseParameter(element,true);
                    	map.put(paramBuilder.getParameterName(),paramBuilder.build());
                    }
				break;
				case XMLStreamConstants.END_ELEMENT:
					EndElement endElement = event.asEndElement();
					String endName = endElement.getName().getLocalPart();
					if(SecurityXMLUtilConstants.URL.equals(endName)) {
						builder.setParameters(map);
						URL url=builder.build();
						String[] split=url.getUrl().split("/");
						createURLNodeTree(URLNODES,url,split,1);
						return;
					}
				break;
			}
		}
		
	}
	
	public static void createURLNodeTree(final URLNode urlnode, final URL url, final String[] urlTokens, int index) {
		ArrayList<Node> nodes= urlnode.getNodes();
		for(Node node:nodes) {
			if(urlTokens[index].equals(node.getValue())) {
				if(index==urlTokens.length-1) {
					node.getUrls().add(url);
				}else {
					createURLNodeTree(node.getUrlNode(),url,urlTokens,++index);
				}
				return;
			}
		}
		Node node=new Node();
		node.setValue(urlTokens[index++]);
		if(index==urlTokens.length) {
			node.getUrls().add(url);
		}else {
			createURLNodeTree(node.getUrlNode(),url,urlTokens,index);
		}
		nodes.add(node);
	}
	
	public static URL getUrl(final URLNode urlnode, final String[] searchTokens, int index, String protocol) {
		ArrayList<Node> nodes= urlnode.getNodes();
		ArrayList<Node> regexNodes=new ArrayList<>();
		for(Node node:nodes) {
			String value=node.getValue();
			if(searchTokens[index].equals(value)) {
				if(index==searchTokens.length-1) {
					ArrayList<URL> urls=node.getUrls();
					for(URL url:urls ) {
						String[] urltokens=url.getUrl().split("/");
						if(searchTokens.length==urltokens.length && url.getMethod().toLowerCase().equals(protocol)) {
							return url;
						}
					}
				}else {
					URL url=getUrl(node.getUrlNode(),searchTokens,index+1,protocol);
					if(url!=null) {
						return url;
					}
				}
			}else  {
				Pattern pattern = Pattern.compile(value);
				Matcher matcher = pattern.matcher(searchTokens[index]);				
				if(matcher.matches()) {
					regexNodes.add(node);
				}
			}
		}
		for(Node node:regexNodes) {
			if(index==searchTokens.length-1) {
				ArrayList<URL> urls=node.getUrls();
				for(URL url:urls ) {
					String[] urltokens=url.getUrl().split("/");
					if(searchTokens.length==urltokens.length && url.getMethod().toLowerCase().equals(protocol)) {
						return url;
					}
				}
			}else {
				URL url=getUrl(node.getUrlNode(),searchTokens,index+1,protocol);
				if(url!=null) {
					return url;
				}
			}
		}
		return null;
	}
	
	/**
	 * If a parameter contains object, the object must follow a template. The template in-turn contains parameter.
	 * For eg., Let us consider an url, /admin/update. This url contains parameter such as adminid, update_details.
	 * The update_details is an object that contains update information. The object might contain
	 * 	{
	 * 		email:"test@test.com",
	 * 		role:"admin"
	 *  }
	 *  
	 *  In security.xml, it will be represented as 
	 *  <template name="update_details_template">
	 *  	<parameter name="email" regex="^[\w]$"/>
	 *  	<parameter name="role" regex="^[0-9]$"/>
	 *  </template>
	 *  
	 *  <url name="/admin/update" method="post" authentication="true">
	 *  	<parameter name="adminid" regex="^[0-9]$"></parameter>
	 *  	<parameter name="update_details" type="object" template="update_details_template"></parameter>
	 *  </url>
	 *  
	 *  This method will take the template name and will return corresponding paramater's if the template has been defined in the security.xml.
	 *  There is a possibility that template might contain a parameter that points to another template itself and it can go.
	 *  For eg.,
	 *  <template name="test">
	 *  	<parameter name="email" regex="^[\w]$"/>
	 *  	<parameter name="role" regex="^[0-9]$"/>
	 *  	<parameter name="data" type="object" template="test_1"/>
	 *  </template>
	 *  To restrict the level from growing, maximum depth level has been restricted to 3
	 *  template_1-----
	 *  				template_2-----
	 *  								template_3-----
	 *  
	 *
	 * @param templateName the template name
	 * @param level the level
	 * @return HashMap<String, Parameter>
	 * @throws Exception the exception
	 */
	private HashMap<String, Parameter> getTemplateforParameter(final String templateName,final int level) throws Exception {
		if(level==SecurityXMLUtilConstants.MAX_DEPTH_LEVEL) {
			throw new Exception("The depth of the object must not exceed "+level);
		}
		if(templateBuilderMaps.containsKey(templateName)) {
			if(templateMaps.containsKey(templateName)) {
				return templateMaps.get(templateName);
			}
			final List<ParameterBuilder> parameterbuilders=templateBuilderMaps.get(templateName);
			final HashMap<String, Parameter> map=new HashMap<>();
			for(ParameterBuilder parameterbuilder:parameterbuilders) {
				if(SecurityXMLUtilConstants.OBJECT.equals(parameterbuilder.getType())) {
					parameterbuilder.setParameters(getTemplateforParameter(parameterbuilder.getTemplate(),level+1));
				}
				map.put(parameterbuilder.getParameterName(),parameterbuilder.build());
			}
			templateMaps.put(templateName, map);
			return map;
		}
		throw new Exception("Template not found for "+templateName);
	}
	
	
	
	/**
	 * Parses the regex.
	 * For eg., <regex name="cleartext" pattern="^[a-zA-Z0-9_()&amp;\\s]+$"/>
	 * @param startElement the regex element
	 * @return the regex
	 */
	private Regex parseRegex(final StartElement startElement) {
		final Iterator<Attribute> attributes = startElement.getAttributes();
    	String regexName = null;
    	String regexValue = null;
        while(attributes.hasNext()){
            final Attribute attribute = attributes.next();
            if(SecurityXMLUtilConstants.NAME.equals(attribute.getName().toString())){
            	regexName=attribute.getValue();
            }else if(SecurityXMLUtilConstants.PATTERN.equals(attribute.getName().toString())) {
            	regexValue=attribute.getValue();
            }
        }
        String error = null;
        if(!Utils.isValidString(regexName)) {
        	error="Invalid regex name.";
        }else if(!Utils.isValidString(regexValue)) {
        	error="Invalid regex pattern is associated for "+regexName+".";
        }
        if(error!=null) {
        	throw new NullPointerException(error);
        }
    	return new Regex(regexName, regexValue);
	}
	
	public static URL getURL(HttpServletRequest request) {
		return getUrl(SecurityXMLConfig.URLNODES, request.getRequestURI().split("/"), 1, request.getMethod().toLowerCase());
	}
}
