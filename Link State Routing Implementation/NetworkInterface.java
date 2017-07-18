/**
 * @authors Luke Agnew
 * 			Ammar Qureshi
 * 			Vincent Lat
 * 			Keith Tunstead 
 */

package cs.tcd.ie;

import java.util.List;

public interface NetworkInterface {
	/**
	 * Enter a new segment in the graph.
	 */
	public void addDirectRoute(Node start, Node end, int distance);
	
	/**
	 * Get the value of a segment.
	 */
	public int getDistance(Node start, Node end);
	
	/**
	 * Get the list of cities that can be reached from the given node.
	 */
	public List<Node> getDestinations(Node node); 
	
	/**
	 * Get the list of cities that lead to the given node.
	 */
	public List<Node> getPredecessors(Node node);
	
	/**
	 * @return the transposed graph of this graph, as a new RoutesMap instance.
	 */
	public NetworkInterface getInverse();
}
