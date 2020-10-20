package com.bharath.rm.security;

import java.util.ArrayList;

import com.bharath.rm.model.URL;

/**
 * The Class Node.
 *
 * @author bharath
 * @version 1.0
 * Creation time: Jul 15, 2020 1:04:25 AM
 */
public class Node {
	
	/** The value. */
	private String value;
	
	/** The urls. */
	private ArrayList<URL> urls=new ArrayList<>();
	
	/** The url node. */
	private URLNode urlNode=new URLNode();
	
	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * Gets the urls.
	 *
	 * @return the urls
	 */
	public ArrayList<URL> getUrls() {
		return urls;
	}
	
	/**
	 * Sets the urls.
	 *
	 * @param urls the new urls
	 */
	public void setUrls(ArrayList<URL> urls) {
		this.urls = urls;
	}
	
	/**
	 * Gets the url node.
	 *
	 * @return the url node
	 */
	public URLNode getUrlNode() {
		return urlNode;
	}
	
	/**
	 * Sets the url node.
	 *
	 * @param urlNode the new url node
	 */
	public void setUrlNode(URLNode urlNode) {
		this.urlNode = urlNode;
	}
	
}
