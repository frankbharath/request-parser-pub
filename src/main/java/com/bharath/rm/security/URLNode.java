package com.bharath.rm.security;
/**
	* @author bharath
 	* @version 1.0
	* Creation time: Jul 14, 2020 10:57:23 PM
 	* Class Description
*/

import java.util.ArrayList;

public class URLNode {
	@Override
	public String toString() {
		return "URLNode [nodes=" + nodes + "]";
	}

	private ArrayList<Node> nodes=new ArrayList<>();

	public ArrayList<Node> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
	}
}
