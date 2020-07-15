package com.bharath.rm.security;

import java.util.ArrayList;

import com.bharath.rm.model.URL;

/**
	* @author bharath
 	* @version 1.0
	* Creation time: Jul 15, 2020 1:04:25 AM
 	* Class Description
*/
public class Node {
	@Override
	public String toString() {
		return "Node [value=" + value + ", urls=" + urls + ", urlNode=" + urlNode + "]";
	}
	private String value;
	private ArrayList<URL> urls=new ArrayList<>();
	private URLNode urlNode=new URLNode();
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public ArrayList<URL> getUrls() {
		return urls;
	}
	public void setUrls(ArrayList<URL> urls) {
		this.urls = urls;
	}
	public URLNode getUrlNode() {
		return urlNode;
	}
	public void setUrlNode(URLNode urlNode) {
		this.urlNode = urlNode;
	}
	
}
