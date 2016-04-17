/**
 * 
 */
package com.src.spatiotemporal.Algorithms;

import java.util.List;

import com.src.spatiotemporal.Entity.Block;
import com.src.spatiotemporal.Entity.Node;

/**
 * @author Raghavendra
 *
 */
public interface Algorithms {
	//May declare any kinds of data structures here which are 
	//common to all the algorithms
	
	//Move function is common to all algorithms, so declare
	//here and implement in respective extending algorithm classes
	public void move();
	
	//This function is the starting point of all the algorithms
	//fetch route may inturn call many other methods specific
	//to that algorithm
	public void fetchRoute(Double latitude, Double longitude);

	public List<Node> fetchRoute(Block b,String hour, String min, String sec);
	
}
