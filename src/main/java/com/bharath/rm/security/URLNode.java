package com.bharath.rm.security;

import java.util.ArrayList;

/**
 * @author bharath
 * @version 1.0
 * Creation time: Jul 14, 2020 10:57:23 PM
*/

public class URLNode {
	
	/** The nodes. */
	private ArrayList<Node> nodes=new ArrayList<>();

	/**
	 * Gets the nodes.
	 *
	 * @return the nodes
	 */
	public ArrayList<Node> getNodes() {
		return nodes;
	}

	/**
	 * Sets the nodes.
	 *
	 * @param nodes the new nodes
	 */
	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
	}
}
